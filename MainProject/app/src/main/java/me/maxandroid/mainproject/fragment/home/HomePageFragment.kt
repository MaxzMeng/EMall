package me.maxandroid.mainproject.fragment.home;

import android.os.Bundle;
import android.util.SparseArray
import android.view.View;

import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager

import kotlinx.android.synthetic.main.fragment_home.*
import me.maxandroid.common.http.ApiFactory

import me.maxandroid.common.ui.compoment.HiBaseFragment;
import me.maxandroid.hilibrary.restful.HiCallback
import me.maxandroid.hilibrary.restful.HiResponse
import me.maxandroid.hiui.tab.bottom.HiTabBottomLayout
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
        topTabLayout.addTabSelectedChangeListener { index, prevInfo, nextInfo ->
            //点击之后选中的那个下标
            if (viewPager.currentItem != index) {
                viewPager.setCurrentItem(index, false)
            }
        }
        viewPager.adapter = HomePagerAdapter(
                childFragmentManager,
                FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT,
                data
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


    inner class HomePagerAdapter(fm: FragmentManager, behavior: Int, val tabs: List<TabCategory>) :
        FragmentPagerAdapter(fm, behavior) {
        val fragments = SparseArray<Fragment>(tabs.size)
        override fun getItem(position: Int): Fragment {
            var fragment = fragments.get(position, null)
            if (fragment == null) {
                fragment = HomeTabFragment.newInstance(tabs[position].categoryId)
                fragments.put(position, fragment)
            }
            return fragment
        }

        override fun getCount(): Int {
            return tabs.size
        }

    }
}