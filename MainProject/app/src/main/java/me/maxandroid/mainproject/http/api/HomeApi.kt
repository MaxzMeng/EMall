package me.maxandroid.mainproject.http.api

import me.maxandroid.hilibrary.restful.HiCall
import me.maxandroid.hilibrary.restful.annotation.Filed
import me.maxandroid.hilibrary.restful.annotation.GET
import me.maxandroid.hilibrary.restful.annotation.Path
import me.maxandroid.mainproject.model.HomeModel
import me.maxandroid.mainproject.model.TabCategory

interface HomeApi {
    @GET("category/categories")
    fun queryTabList(): HiCall<List<TabCategory>>


    @GET("home/{categoryId}")
    fun queryTabCategoryList(
        @Path("categoryId") categoryId: String,
        @Filed("pageIndex") pageIndex: Int,
        @Filed("pageSize") pageSize: Int
    ): HiCall<HomeModel>
}