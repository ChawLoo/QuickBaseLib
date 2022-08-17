package cn.chawloo.base.popup

import android.content.Context
import android.view.Gravity
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import cn.chawloo.base.R
import cn.chawloo.base.ext.visible
import razerdp.basepopup.BasePopupWindow

class CommonConfirmPopupWindow(context: Context) : BasePopupWindow(context) {
    fun setTitle(title: String = "温馨提示") {
        findViewById<TextView>(R.id.tv_title).text = title
        this.update()
    }

    fun setContent(content: String) {
        findViewById<TextView>(R.id.tv_content).text = content
        this.update()
    }

    fun setCancel(cancelText: String?, cancelAction: (() -> Unit)? = null) {
        cancelText?.takeIf { it.isNotBlank() }?.run {
            findViewById<TextView>(R.id.tv_cancel).apply {
                text = cancelText
                visible()
            }
            findViewById<View>(R.id.v_line).visible()
            this@CommonConfirmPopupWindow.update()
        }
        findViewById<TextView>(R.id.tv_cancel).setOnClickListener {
            dismiss()
            cancelAction?.invoke()
        }
    }

    fun setConfirm(confirmText: String?, confirm: () -> Unit) {
        findViewById<TextView>(R.id.tv_confirm).apply {
            text = confirmText
        }
        findViewById<TextView>(R.id.tv_confirm).setOnClickListener {
            dismiss()
            confirm()
        }
        this.update()
    }

    init {
        setContentView(R.layout.pop_custom_confirm)
        popupGravity = Gravity.CENTER
        showAnimation = AnimationUtils.loadAnimation(context, R.anim.pop_middle_show)
        dismissAnimation = AnimationUtils.loadAnimation(context, R.anim.pop_middle_dismiss)
        findViewById<ImageView>(R.id.iv_close).setOnClickListener { dismiss() }
    }

    class Builder(private val context: Context) {
        private var title: String = "温馨提示"
        private var content: String = ""
        private var cancelText: String? = null
        private var cancelAction: (() -> Unit)? = null
        private var confirmText: String = "温馨提示"
        private var confirmAction: () -> Unit = {}

        fun title(title: String): Builder {
            this.title = title
            return this
        }

        fun content(content: String): Builder {
            this.content = content
            return this
        }

        fun cancel(cancelText: String?, cancelAction: (() -> Unit)? = null): Builder {
            this.cancelText = cancelText
            this.cancelAction = cancelAction
            return this
        }

        fun confirm(confirmText: String, confirmAction: (() -> Unit)): Builder {
            this.confirmText = confirmText
            this.confirmAction = confirmAction
            return this
        }

        fun build(): CommonConfirmPopupWindow {
            return CommonConfirmPopupWindow(context).apply {
                setTitle(this@Builder.title)
                setContent(this@Builder.content)
                setCancel(this@Builder.cancelText, this@Builder.cancelAction)
                setConfirm(this@Builder.confirmText, this@Builder.confirmAction)
            }.also {
                it.showPopupWindow()
            }
        }
    }
}