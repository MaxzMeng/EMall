package me.maxandroid.hiui.demo

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import me.maxandroid.hiui.R
import me.maxandroid.hiui.tab.top.HiTabTopInfo
import me.maxandroid.hiui.tab.top.HiTabTopLayout
import java.util.*


class HiTabTopDemoActivity : AppCompatActivity() {
    var tabsStr = arrayOf(
        "热门",
        "服装",
        "数码",
        "鞋子",
        "零食",
        "家电",
        "汽车",
        "百货",
        "家居",
        "装修",
        "运动"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hi_tab_top_demo)
        initTabTop()
    }

    private fun initTabTop() {
        val hiTabTopLayout = findViewById<HiTabTopLayout>(R.id.tab_top_layout)
        val infoList: MutableList<HiTabTopInfo<*>> = ArrayList()
        val defaultColor = resources.getColor(R.color.tabBottomDefaultColor)
        val tintColor = resources.getColor(R.color.tabBottomTintColor)
        for (s in tabsStr) {
            val info: HiTabTopInfo<*> = HiTabTopInfo(s, defaultColor, tintColor)
            infoList.add(info)
        }
        hiTabTopLayout.inflateInfo(infoList)
        hiTabTopLayout.addTabSelectedChangeListener { index, prevInfo, nextInfo ->
            Toast.makeText(
                this@HiTabTopDemoActivity,
                nextInfo.name,
                Toast.LENGTH_SHORT
            ).show()
        }
        hiTabTopLayout.defaultSelected(infoList[0])
    }
}