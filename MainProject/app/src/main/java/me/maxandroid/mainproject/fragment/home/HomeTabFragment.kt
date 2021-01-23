package me.maxandroid.mainproject.fragment.home

import android.os.Bundle
import android.text.TextUtils
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import me.maxandroid.common.http.ApiFactory
import me.maxandroid.common.ui.compoment.HiAbsListFragment
import me.maxandroid.hilibrary.restful.HiCallback
import me.maxandroid.hilibrary.restful.HiResponse
import me.maxandroid.hilibrary.restful.annotation.CacheStrategy
import me.maxandroid.hiui.item.HiDataItem
import me.maxandroid.mainproject.http.api.HomeApi
import me.maxandroid.mainproject.model.HomeModel

class HomeTabFragment : HiAbsListFragment() {
    private var categoryId: String? = null
    val DEFAULT_HOT_TAB_CATEGORY_ID = "1"

    companion object {
        fun newInstance(categoryId: String): HomeTabFragment {
            val args = Bundle()
            args.putString("categoryId", categoryId)
            val fragment =
                HomeTabFragment()
            fragment.arguments = args
            return fragment
        }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        categoryId = arguments?.getString("categoryId", DEFAULT_HOT_TAB_CATEGORY_ID)

        super.onViewCreated(view, savedInstanceState)

        queryTabCategoryList(CacheStrategy.CACHE_FIRST)

        enableLoadMore { queryTabCategoryList(CacheStrategy.NET_ONLY) }
    }

    override fun onRefresh() {
        super.onRefresh()

        queryTabCategoryList(CacheStrategy.NET_CACHE)
    }

    override fun createLayoutManager(): RecyclerView.LayoutManager {
        val isHotTab = TextUtils.equals(categoryId, DEFAULT_HOT_TAB_CATEGORY_ID)
        return if (isHotTab) super.createLayoutManager() else GridLayoutManager(context, 2)
    }

    private fun queryTabCategoryList(cacheStrategy: Int) {
        ApiFactory.create(HomeApi::class.java)
            .queryTabCategoryList(cacheStrategy, categoryId!!, pageIndex, 10)
            .enqueue(object : HiCallback<HomeModel> {
                override fun onSuccess(response: HiResponse<HomeModel>) {
                    if (response.successful() && response.data != null) {
                        updateUI(response.data!!)
                    } else {
                        finishRefresh(null)
                    }
                }

                override fun onFailed(throwable: Throwable) {
                    //空数据页面
                    finishRefresh(null)
                }
            })
    }

    private fun updateUI(data: HomeModel) {
        if (!isAlive) return

        val dataItems = mutableListOf<HiDataItem<*, *>>()
        data.bannerList?.let {
            dataItems.add(BannerItem(data.bannerList))
        }

        data.subcategoryList?.let {
            dataItems.add(GridItem(data.subcategoryList))
        }

        data.goodsList?.forEachIndexed { index, goodsModel ->
            dataItems.add(
                GoodsItem(
                    goodsModel,
                    TextUtils.equals(categoryId, DEFAULT_HOT_TAB_CATEGORY_ID)
                )
            )
        }
        finishRefresh(dataItems)
    }
}