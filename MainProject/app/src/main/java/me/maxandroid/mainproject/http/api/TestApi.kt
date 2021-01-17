package me.maxandroid.mainproject.http.api

import com.google.gson.JsonObject
import me.maxandroid.hilibrary.restful.HiCall
import me.maxandroid.hilibrary.restful.annotation.Filed
import me.maxandroid.hilibrary.restful.annotation.GET

interface TestApi {

    @GET("cities")
    fun listCities(@Filed("name") name: String): HiCall<JsonObject>

}