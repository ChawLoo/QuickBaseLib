package cn.chawloo.base.multistate

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.appcompat.widget.AppCompatImageView
import cn.chawloo.base.R
import cn.chawloo.base.ext.gone
import cn.chawloo.base.ext.visible
import com.safframework.log.L
import com.zy.multistatepage.MultiState
import com.zy.multistatepage.MultiStateContainer
import com.zy.multistatepage.MultiStatePage

/**
 * 自定义错误缺省页
 * @author Create by 鲁超 on 2021/8/24 0024 16:25:20
 *----------Dragon be here!----------/
 *       ┌─┐      ┌─┐
 *     ┌─┘─┴──────┘─┴─┐
 *     │              │
 *     │      ─       │
 *     │  ┬─┘   └─┬   │
 *     │              │
 *     │      ┴       │
 *     │              │
 *     └───┐      ┌───┘
 *         │      │神兽保佑
 *         │      │代码无BUG！
 *         │      └──────┐
 *         │             ├┐
 *         │             ┌┘
 *         └┐ ┐ ┌───┬─┐ ┌┘
 *          │ ┤ ┤   │ ┤ ┤
 *          └─┴─┘   └─┴─┘
 *─────────────神兽出没───────────────/
 */
class CustomErrorState : MultiState() {

    private lateinit var tvErrorMsg: TextView
    private lateinit var imgError: AppCompatImageView
    private lateinit var tvRetry: TextView

    private var retry: () -> Unit = { L.e("点击重试") }

    override fun onCreateMultiStateView(context: Context, inflater: LayoutInflater, container: MultiStateContainer): View {
        return inflater.inflate(R.layout.custom_error_view, container, false)
    }

    override fun onMultiStateViewCreate(view: View) {
        tvErrorMsg = view.findViewById(R.id.status_hint_content)
        imgError = view.findViewById(R.id.error_view_icon)
        tvRetry = view.findViewById(R.id.btn_retry)
        tvRetry.setOnClickListener { retry() }
        setErrorMsg(MultiStatePage.config.errorMsg)
        setErrorIcon(MultiStatePage.config.errorIcon)
    }

    fun setErrorMsg(errorMsg: String) {
        if (errorMsg.isNotBlank()) {
            tvErrorMsg.text = errorMsg
            tvErrorMsg.visible()
        } else {
            tvErrorMsg.gone()
        }
    }

    fun setErrorIcon(@DrawableRes errorIcon: Int) {
        if (errorIcon == -1) {
            imgError.gone()
        } else {
            imgError.visible()
            imgError.setImageResource(errorIcon)
        }
    }

    fun setRetryTxt(retryTxt: String) {
        tvRetry.text = retryTxt
    }

    fun setRetry(retry: (() -> Unit)? = null) {
        if (retry != null) {
            this.retry = retry
            tvRetry.visible()
        }
    }
}