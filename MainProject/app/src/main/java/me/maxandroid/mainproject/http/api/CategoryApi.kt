package me.maxandroid.mainproject.http.api

import me.maxandroid.hilibrary.restful.HiCall
import me.maxandroid.hilibrary.restful.annotation.GET
import me.maxandroid.hilibrary.restful.annotation.Path
import me.maxandroid.mainproject.model.Subcategory
import me.maxandroid.mainproject.model.TabCategory


interface CategoryApi {
    @GET("category/categories")
    fun queryCategoryList(): HiCall<List<TabCategory>>


    @GET("category/subcategories/{categoryId}")
    fun querySubcategoryList(@Path("categoryId") categoryId: String): HiCall<List<Subcategory>>
}