package me.maxandroid.mainproject.http.api

import me.maxandroid.hilibrary.restful.HiCall
import me.maxandroid.hilibrary.restful.annotation.Filed
import me.maxandroid.hilibrary.restful.annotation.GET
import me.maxandroid.hilibrary.restful.annotation.Path
import me.maxandroid.mainproject.model.GoodsList


interface GoodsApi {
    @GET("goods/goods/{categoryId}")
    fun queryCategoryGoodsList(
        @Path("categoryId") categoryId: String,
        @Filed("subcategoryId") subcategoryId: String,
        @Filed("pageSize") pageSize: Int,
        @Filed("pageIndex") pageIndex: Int
    ): HiCall<GoodsList>
}