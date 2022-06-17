package cn.chawloo.base.widget.tablayout.utils

import android.view.View
import android.widget.RelativeLayout
import cn.chawloo.base.widget.tablayout.MsgView

/**
 * TODO
 *
 * @author Create by 鲁超 on 2020/4/25 0025 19:32
 */
object UnreadMsgUtils {
    fun show(msgView: MsgView?, num: Int) {
        if (msgView == null) {
            return
        }
        val lp = msgView.layoutParams as RelativeLayout.LayoutParams
        val dm = msgView.resources.displayMetrics
        msgView.visibility = View.VISIBLE
        if (num <= 0) { //圆点,设置默认宽高
            msgView.strokeWidth = 0
            msgView.text = ""
            lp.width = (5 * dm.density).toInt()
            lp.height = (5 * dm.density).toInt()
        } else {
            lp.height = (18 * dm.density).toInt()
            if (num < 10) { //圆
                lp.width = (18 * dm.density).toInt()
                msgView.text = num.toString()
            } else if (num < 100) { //圆角矩形,圆角是高度的一半,设置默认padding
                lp.width = RelativeLayout.LayoutParams.WRAP_CONTENT
                msgView.setPadding((6 * dm.density).toInt(), 0, (6 * dm.density).toInt(), 0)
                msgView.text = num.toString()
            } else { //数字超过两位,显示99+
                lp.width = RelativeLayout.LayoutParams.WRAP_CONTENT
                msgView.setPadding((6 * dm.density).toInt(), 0, (6 * dm.density).toInt(), 0)
                msgView.text = "99+"
            }
        }
        msgView.layoutParams = lp
    }

    fun setSize(rtv: MsgView?, size: Int) {
        if (rtv == null) {
            return
        }
        val lp = rtv.layoutParams as RelativeLayout.LayoutParams
        lp.width = size
        lp.height = size
        rtv.layoutParams = lp
    }
}