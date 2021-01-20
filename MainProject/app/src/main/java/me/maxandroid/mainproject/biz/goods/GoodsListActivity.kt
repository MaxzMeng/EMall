package me.maxandroid.mainproject.biz.goods

import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import kotlinx.android.synthetic.main.activity_goods_list.*
import me.maxandroid.common.route.HiRoute
import me.maxandroid.common.ui.compoment.HiBaseActivity
import me.maxandroid.hilibrary.util.HiStatusBar
import me.maxandroid.mainproject.R

@Route(path = "/goods/list")
class GoodsListActivity : HiBaseActivity() {

    @JvmField
    @Autowired
    var categoryTitle: String = ""

    @JvmField
    @Autowired
    var categoryId: String = ""

    @JvmField
    @Autowired
    var subcategoryId: String = ""

    private val FRAGMENT_TAG = "GOODS_LIST_FRAGMENT"
    override fun onCreate(savedInstanceState: Bundle?) {
        HiStatusBar.setStatusBar(this, true, translucent = false)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_goods_list)
       // ARouter.getInstance().inject(this)
        HiRoute.inject(this)

        action_back.setOnClickListener { onBackPressed() }
        category_title.text = categoryTitle

        var fragment = supportFragmentManager.findFragmentByTag(FRAGMENT_TAG)
        if (fragment == null) {
            fragment = GoodsListFragment.newInstance(categoryId, subcategoryId)
        }
        val ft = supportFragmentManager.beginTransaction()
        if (!fragment.isAdded) {
            ft.add(R.id.container, fragment, FRAGMENT_TAG)
        }
        ft.show(fragment).commitNowAllowingStateLoss()
    }
}