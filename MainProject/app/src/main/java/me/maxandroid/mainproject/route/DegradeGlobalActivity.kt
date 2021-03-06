package me.maxandroid.mainproject.route

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import me.maxandroid.hiui.empty.EmptyView
import me.maxandroid.hiui.icfont.IconFontTextView
import me.maxandroid.mainproject.R


/**
 * 全局统一错误页
 */
@Route(path = "/degrade/global/activity")
class DegradeGlobalActivity : AppCompatActivity() {
    @JvmField
    @Autowired
    var degrade_title: String? = null
    @JvmField
    @Autowired
    var degrade_desc: String? = null
    @JvmField
    @Autowired
    var degrade_action: String? = null

    private lateinit var emptyView: EmptyView
    private lateinit var actionBack: IconFontTextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ARouter.getInstance().inject(this)
//        HiRoute.inject(this)

        setContentView(R.layout.layout_global_degrade)
        emptyView = findViewById(R.id.empty_view)
        actionBack = findViewById(R.id.action_back)
        emptyView.setIcon(R.string.if_empty)

        if (degrade_title != null) {
            emptyView.setTitle(degrade_title!!)
        }

        if (degrade_desc != null) {
            emptyView.setDesc(degrade_desc!!)
        }

        if (degrade_action != null) {
            emptyView.setHelpAction(listener = View.OnClickListener {
                var intent: Intent = Intent(Intent.ACTION_VIEW, Uri.parse(degrade_action))
                startActivity(intent)
            })
        }

        actionBack.setOnClickListener { onBackPressed() }
    }
}