package me.maxandroid.hiui.demo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import me.maxandroid.hiui.R
import me.maxandroid.hiui.refresh.HiRefresh
import me.maxandroid.hiui.refresh.HiRefreshLayout
import me.maxandroid.hiui.refresh.HiTextOverView

class HiRefreshDemoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hi_refresh_demo)

        val refreshLayout = findViewById<HiRefreshLayout>(R.id.refresh_layout)
        val xOverView = HiTextOverView(this)
        refreshLayout.setRefreshOverView(xOverView)
        refreshLayout.setRefreshListener(object : HiRefresh.HiRefreshListener {
            override fun onRefresh() {
                Handler().postDelayed({
                    refreshLayout.refreshFinished()
                }, 1000)

            }

            override fun enableRefresh(): Boolean {
                return true
            }

        })
    }
}