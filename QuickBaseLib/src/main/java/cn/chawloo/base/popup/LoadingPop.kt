package cn.chawloo.base.popup

import android.content.Context
import android.graphics.Color
import android.view.Gravity
import android.widget.TextView
import cn.chawloo.base.R
import cn.chawloo.base.ext.gone
import cn.chawloo.base.ext.visible
import razerdp.basepopup.BasePopupWindow

/**
 * 等待进度条弹窗
 * @author Create by 鲁超 on 2020/11/21 0021 9:26
 */
class LoadingPop(context: Context, var msg: CharSequence) : BasePopupWindow(context) {
    private var mLoadingTextView: TextView

    init {
        setContentView(R.layout.pop_loading)
        popupGravity = Gravity.CENTER
        isOutSideTouchable = false
        setOutSideDismiss(false)
        setBackgroundColor(Color.TRANSPARENT)
        mLoadingTextView = findViewById(R.id.mLoadingTextView)
        msg.takeIf { it.isNotBlank() }?.run {
            mLoadingTextView.text = msg
            mLoadingTextView.visible()
        } ?: mLoadingTextView.gone()
    }

    override fun showPopupWindow() {
        try {
            if (this.isShowing) this.dismiss()
            else super.showPopupWindow()
        } catch (e: Exception) {
        }

    }
}