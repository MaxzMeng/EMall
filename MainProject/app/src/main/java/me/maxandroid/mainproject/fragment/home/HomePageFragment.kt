package me.maxandroid.mainproject.fragment.home;

import android.os.Bundle;
import android.util.SparseArray
import android.view.View;

import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager

import kotlinx.android.synthetic.main.fragment_home.*
import me.maxandroid.common.http.ApiFactory

import me.maxandroid.common.ui.compoment.HiBaseFragment;
import me.maxandroid.hilibrary.restful.HiCallback
import me.maxandroid.hilibrary.restful.HiResponse
import me.maxandroid.hiui.tab.bottom.HiTabBottomLayout
import me.maxandroid.hiui.tab.common.IHiTabLayout
import me.maxandroid.hiui.tab.top.HiTabTopInfo
import me.maxandroid.mainproject.R;
import me.maxandroid.mainproject.http.api.HomeApi
import me.maxandroid.mainproject.model.TabCategory

class HomePageFragment : HiBaseFragment() {
    private var topTabSelectIndex: Int = 0
    private val DEFAULT_SELECT_INDEX: Int = 0

    override fun getLayoutId(): Int {
        return R.layout.fragment_home
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        HiTabBottomLayout.clipBottomPadding(view_pager)
        queryTabList()
    }

    private fun queryTabList() {

        ApiFactory.create(HomeApi::class.java)
            .queryTabList().enqueue(object : HiCallback<List<TabCategory>> {
                        override fun onSuccess(response: HiResponse<List<TabCategory>>) {
                                val data = response.data
                                if (response.successful() && data != null) {
                                        updateUI(data)
                                }
                        }

                        override fun onFailed(throwable: Throwable) {

                        }
                })
    }

    private val onTabSelectedListener =
        IHiTabLayout.OnTabSelectedListener<HiTabTopInfo<*>> { index, prevInfo, nextInfo ->
            if (view_pager.currentItem != index) {
                view_pager.setCurrentItem(index, false)
            }
        }

    private fun updateUI(data: List<TabCategory>) {
        //需要小心处理  ---viewmodel+livedata
        if (!isAlive) return

        val topTabs = mutableListOf<HiTabTopInfo<Int>>()
        data.forEachIndexed { index, tabCategory ->
            val defaultColor = ContextCompat.getColor(requireContext(), R.color.color_333)
            val selectColor = ContextCompat.getColor(requireContext(), R.color.color_dd2)
            val tabTopInfo = HiTabTopInfo<Int>(tabCategory.categoryName, defaultColor, selectColor)

            topTabs.add(tabTopInfo)
        }

        val viewPager = view_pager
        val topTabLayout = top_tab_layout
        topTabLayout.inflateInfo(topTabs as List<HiTabTopInfo<*>>)
        topTabLayout.defaultSelected(topTabs[DEFAULT_SELECT_INDEX])
        topTabLayout.addTabSelectedChangeListener(onTabSelectedListener)
        if (viewPager.adapter == null) {
            viewPager.adapter = HomePagerAdapter(
                childFragmentManager,
                FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
            )
            viewPager.addOnPageChangeListener(object : ViewPager.SimpleOnPageChangeListener() {
                override fun onPageSelected(position: Int) {
                    //这个方法被触发有两种可能，第一种切换顶部tab  第二种 手动滑动翻页
                    //如果是 手动滑动翻页人

                    if (position != topTabSelectIndex) {
                        //去通知topTabLayout进行切换
                        topTabLayout.defaultSelected(topTabs[position])
                        topTabSelectIndex = position
                    }
                }
            })
        }
        val adapter = viewPager.adapter
        if (adapter is HomePagerAdapter) {
            adapter.update(data)
        }
    }


    inner class HomePagerAdapter(fm: FragmentManager, behavior: Int) :
        FragmentPagerAdapter(fm, behavior) {
        private val tabs = mutableListOf<TabCategory>()
        private val fragments = SparseArray<Fragment>(tabs.size)
        override fun getItem(position: Int): Fragment {
            val categoryId = tabs[position].categoryId
            val categoryIdKey = categoryId.toInt()
            var fragment = fragments.get(categoryIdKey, null)
            if (fragment == null) {
                fragment = HomeTabFragment.newInstance(tabs[position].categoryId)
                fragments.put(categoryIdKey, fragment)
            }
            return fragment
        }

        override fun getCount(): Int {
            return tabs.size
        }

        //判断一个fragment = `object`，刷新前后在viewpager中的位置有没有发生变化。
        //return PagerAdapter.POSITION_NONE,则暂时detach掉，并不是移除，待会还有可能可以复用
        override fun getItemPosition(`object`: Any): Int {
            //需要判断刷新前后 两次frgment 在viewpager中的位置 有没有改变，如果改变了PagerAdapter.POSITION_NONE ,否则返回PagerAdapter.POSITION_UNCHANGED
            //是为了避免缓存数据 和 接口数据返回的顶部导航栏数据一样的情况，导致页面的fragment 会被先detach ,在attach,重复执行生命周期
            //同时还能兼顾 缓存数据返回的顶部导航栏 和接口返回的数据 不一致的情况。这个case你可以【构造假数据测试,在updateUI如果是缓存数据,则删除前两个元素。】

            //我就拿到了刷新之前 该位置对应的fragment对象
            val indexOfValue = fragments.indexOfValue(`object` as Fragment)
            val fragment = getItem(indexOfValue)
            return if (fragment == `object`) PagerAdapter.POSITION_UNCHANGED else PagerAdapter.POSITION_NONE
        }

        //如果使用position当做Id,刷新前后会出现因复用，导致位置错乱问题
        override fun getItemId(position: Int): Long {
            return tabs[position].categoryId.toLong()
        }

        fun update(list: List<TabCategory>) {
            tabs.clear()
            tabs.addAll(list)
            notifyDataSetChanged()
        }
    }
}