package me.maxandroid.mainproject.http.api

import me.maxandroid.hilibrary.restful.HiCall
import me.maxandroid.hilibrary.restful.annotation.CacheStrategy
import me.maxandroid.hilibrary.restful.annotation.Filed
import me.maxandroid.hilibrary.restful.annotation.GET
import me.maxandroid.hilibrary.restful.annotation.Path
import me.maxandroid.mainproject.model.HomeModel
import me.maxandroid.mainproject.model.TabCategory

interface HomeApi {
    @CacheStrategy(CacheStrategy.CACHE_FIRST)
    @GET("category/categories")
    fun queryTabList(): HiCall<List<TabCategory>>


    @GET("home/{categoryId}")
    fun queryTabCategoryList(
        @CacheStrategy cacheStrategy: Int,
        @Path("categoryId") categoryId: String,
        @Filed("pageIndex") pageIndex: Int,
        @Filed("pageSize") pageSize: Int
    ): HiCall<HomeModel>
}