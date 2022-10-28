package cn.chawloo.base.popup

import android.content.Context
import android.view.Gravity
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import cn.chawloo.base.R
import cn.chawloo.base.ext.dp
import com.drake.brv.utils.dividerSpace
import com.drake.brv.utils.linear
import com.drake.brv.utils.setup
import razerdp.basepopup.BasePopupWindow

/**
 * TODO
 * @author Create by 鲁超 on 2021/3/3 0003 11:07
 */
class CommonListPopupWindow(c: Context, title: String, dataList: List<String>) : BasePopupWindow(c) {
    init {
        setContentView(R.layout.pop_custom_list)
        popupGravity = Gravity.CENTER
        setMaxHeight(300.dp)
        showAnimation = AnimationUtils.loadAnimation(context, R.anim.pop_middle_show)
        dismissAnimation = AnimationUtils.loadAnimation(context, R.anim.pop_middle_dismiss)

        val ivClose = findViewById<ImageView>(R.id.iv_close)
        ivClose.setOnClickListener { dismiss() }

        findViewById<RecyclerView>(R.id.recyclerview)
            .linear()
            .dividerSpace(1.dp)
            .setup {
                addType<String>(R.layout.item_list_tv)
                onBind {
                    findView<TextView>(R.id.text_view).text = getModel<String>()
                }
            }.models = dataList
        findViewById<TextView>(R.id.tv_title).text = title
    }
}