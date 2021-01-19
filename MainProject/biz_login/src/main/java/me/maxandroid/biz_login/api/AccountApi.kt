package me.maxandroid.biz_login.api

import me.maxandroid.biz_login.model.CourseNotice
import me.maxandroid.biz_login.model.UserProfile
import me.maxandroid.hilibrary.restful.HiCall
import me.maxandroid.hilibrary.restful.annotation.Filed
import me.maxandroid.hilibrary.restful.annotation.GET
import me.maxandroid.hilibrary.restful.annotation.POST

interface AccountApi {

    @POST("user/login")
    fun login(
        @Filed("userName") userName: String,
        @Filed("password") password: String
    ): HiCall<String>


    @POST("user/registration")
    fun register(
        @Filed("userName") userName: String,
        @Filed("password") password: String,
        @Filed("imoocId") imoocId:
        String, @Filed("orderId") orderId: String
    ): HiCall<String>


    @GET("user/profile")
    fun profile(): HiCall<UserProfile>


    @GET("notice")
    fun notice(): HiCall<CourseNotice>
}