package me.maxandroid.mainproject

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import androidx.fragment.app.DialogFragment
import com.google.gson.JsonObject
import me.maxandroid.common.http.ApiFactory
import me.maxandroid.common.ui.compoment.HiBaseActivity
import me.maxandroid.hilibrary.restful.HiCallback
import me.maxandroid.hilibrary.restful.HiResponse
import me.maxandroid.hilibrary.util.HiStatusBar
import me.maxandroid.mainproject.http.api.TestApi
import me.maxandroid.mainproject.logic.MainActivityLogic
import java.lang.Exception

class MainActivity : HiBaseActivity(), MainActivityLogic.ActivityProvider {

    private lateinit var activityLogic: MainActivityLogic

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        HiStatusBar.setStatusBar(this, true, Color.WHITE, false)
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val fragments = supportFragmentManager.fragments
        for (fragment in fragments) {
            fragment.onActivityResult(requestCode, resultCode, data)
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN && BuildConfig.DEBUG) {
            try {
                val clazz = Class.forName("me.maxandroid.hi_debugtool.DebugToolDialogFragment")
                val target = clazz.getConstructor().newInstance() as DialogFragment?
                target?.show(supportFragmentManager, "debug_tool")
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return super.onKeyDown(keyCode, event)
    }
}