package me.maxandroid.mainproject

import android.os.Bundle
import android.util.Log
import com.google.gson.JsonObject
import me.maxandroid.common.ui.compoment.HiBaseActivity
import me.maxandroid.hilibrary.log.HiLog
import me.maxandroid.hilibrary.restful.HiCallback
import me.maxandroid.hilibrary.restful.HiResponse
import me.maxandroid.mainproject.http.ApiFactory
import me.maxandroid.mainproject.http.api.TestApi
import me.maxandroid.mainproject.logic.MainActivityLogic
import retrofit2.Call

class MainActivity : HiBaseActivity(), MainActivityLogic.ActivityProvider {

    private lateinit var activityLogic: MainActivityLogic

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        activityLogic = MainActivityLogic(this, savedInstanceState)

        ApiFactory.create(TestApi::class.java).listCities("imooc")
            .enqueue(object : HiCallback<JsonObject> {
                override fun onSuccess(response: HiResponse<JsonObject>) {
                    Log.e("result", response.code.toString())
                }

                override fun onFailed(throwable: Throwable) {

                }
            })
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        activityLogic.onSaveInstanceState(outState)
    }
}