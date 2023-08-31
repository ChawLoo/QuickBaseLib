package cn.chawloo.base.popup

import android.content.Context
import android.text.SpannableStringBuilder
import android.text.SpannedString
import android.view.Gravity
import android.view.LayoutInflater
import android.view.animation.AnimationUtils
import androidx.core.text.buildSpannedString
import androidx.databinding.DataBindingUtil
import cn.chawloo.base.R
import cn.chawloo.base.databinding.PopCustomConfirmBinding
import cn.chawloo.base.ext.doClick
import cn.chawloo.base.ext.gone
import cn.chawloo.base.ext.visible
import razerdp.basepopup.BasePopupWindow

class CommonConfirmPopupWindow(context: Context) : BasePopupWindow(context) {
    fun setContent(spannedString: SpannedString) {
        vb.tvContent.text = spannedString
        update()
    }

    fun cancelEnable() {
        vb.tvCancel.gone()
        vb.vLine.gone()
        vb.tvCancel.doClick { }
        setOutSideDismiss(false)
        setBackPressEnable(false)
        isOutSideTouchable = false
    }

    fun setCancel(cancelText: String, cancelAction: (() -> Unit)? = null) {
        vb.tvCancel.text = cancelText.ifBlank { "取消" }
        vb.tvCancel.doClick {
            dismiss()
            cancelAction?.invoke()
        }
        vb.tvCancel.visible()
        vb.vLine.visible()
        update()
    }

    fun setConfirm(confirmText: String, confirm: () -> Unit) {
        vb.tvConfirm.text = confirmText.ifBlank { "确定" }
        vb.tvConfirm.doClick {
            dismiss()
            confirm()
        }
        update()
    }

    private val vb: PopCustomConfirmBinding

    init {
        vb = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.pop_custom_confirm, null, false)
        contentView = vb.root
        popupGravity = Gravity.CENTER
        showAnimation = AnimationUtils.loadAnimation(context, R.anim.pop_middle_show)
        dismissAnimation = AnimationUtils.loadAnimation(context, R.anim.pop_middle_dismiss)
    }

    class Builder(private val context: Context) {
        private var cancelable: Boolean = true
        private var cancelBtn: String = "取消"
        private var cancelAction: (() -> Unit)? = null
        private var confirmText: String = "确定"
        private var confirmAction: () -> Unit = {}
        private var superContent: SpannedString = SpannedString("")
        private var dismissListener: () -> Unit = {}

        /**
         * 富文本设置文案，可以包含标题和文本，自由度更高
         * 示例：常用的 buildSpannedString { }
         */

        fun buildMessage(builderAction: SpannableStringBuilder.() -> Unit): Builder {
            superContent = buildSpannedString(builderAction)
            return this
        }

        /**
         * 消失回调
         */
        fun dismissCallBack(block: () -> Unit): Builder {
            dismissListener = block
            return this
        }

        /**
         * 是否禁用取消
         */
        fun cancelEnable(): Builder {
            cancelable = false
            return this
        }

        /**
         * 取消设置
         * @param cancelText 取消按钮文案
         * @param cancelAction 取消事件
         */
        fun cancel(cancelText: String = "取消", cancelAction: (() -> Unit)? = null): Builder {
            this.cancelable = true
            this.cancelBtn = cancelText
            this.cancelAction = cancelAction
            return this
        }

        /**
         * 设置确认按钮
         * @param confirmText 确认按钮文案
         * @param confirmAction 确认事件回调
         */
        fun confirm(confirmText: String, confirmAction: (() -> Unit)): Builder {
            this.confirmText = confirmText
            this.confirmAction = confirmAction
            return this
        }

        /**
         * 构建 返回弹窗  并自动显示
         */
        fun build(autoShow: Boolean = true): CommonConfirmPopupWindow {
            return CommonConfirmPopupWindow(context).apply {
                setContent(this@Builder.superContent)
                if (cancelable) {
                    setCancel(this@Builder.cancelBtn, this@Builder.cancelAction)
                } else {
                    cancelEnable()
                }
                setConfirm(this@Builder.confirmText, this@Builder.confirmAction)
            }.also {
                it.onDismissListener = object : OnDismissListener() {
                    override fun onDismiss() {
                        this@Builder.dismissListener()
                    }
                }
                if (autoShow) it.showPopupWindow()
            }
        }
    }
}