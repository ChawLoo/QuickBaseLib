package cn.chawloo.base.popup

import android.content.Context
import android.view.Gravity
import android.view.animation.AnimationUtils
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import cn.chawloo.base.R
import cn.chawloo.base.databinding.ItemSimpleBottomListBinding
import cn.chawloo.base.ext.doClick
import cn.chawloo.base.ext.dp
import com.drake.brv.utils.divider
import com.drake.brv.utils.linear
import com.drake.brv.utils.setup
import razerdp.basepopup.BasePopupWindow

/**
 * 多选列表弹窗
 * @author Create by 鲁超 on 2020/11/20 0020 18:16
 */
class BottomListPickerPopupWindow(context: Context, dataList: List<String>, picker: (String) -> Unit) : BasePopupWindow(context) {

    init {
        setContentView(R.layout.pop_simple_bottom_list)
        popupGravity = Gravity.BOTTOM
        setMaxHeight(300.dp)
        showAnimation = AnimationUtils.loadAnimation(context, R.anim.pop_bottom_show)
        dismissAnimation = AnimationUtils.loadAnimation(context, R.anim.pop_bottom_dismiss)
        setOverlayNavigationBar(false)
        findViewById<RecyclerView>(R.id.recyclerview)
            .linear()
            .divider(R.drawable.shape_rv_h_divider_1_dp)
            .setup {
                addType<String>(R.layout.item_simple_bottom_list)
                onBind {
                    val binding = getBinding<ItemSimpleBottomListBinding>()
                    binding.tvValue.text = getModel<String>()
                    itemView.background = when (layoutPosition) {
                        0 -> ContextCompat.getDrawable(context, R.drawable.shape_list_corner_white_bg)
                        dataList.size - 1 -> ContextCompat.getDrawable(context, R.drawable.shape_list_corner_white_bg)
                        else -> null
                    }
                }
                onClick(R.id.tv_value) {
                    dismiss()
                    picker(getModel())
                }
            }.models = dataList
        findViewById<TextView>(R.id.tv_cancel).doClick { dismiss() }
    }
}