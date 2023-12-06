package cn.chawloo.base.ext

import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.DrawableRes
import cn.chawloo.base.R
import com.drake.brv.PageRefreshLayout

/**
 * TODO
 * @author Create by 鲁超 on 2023/12/19 16:13
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
fun PageRefreshLayout?.loading(msg: String = "请稍等") {
    this?.showLoading(msg)
}

fun PageRefreshLayout?.content() {
    this?.showContent()
}

fun PageRefreshLayout?.empty(msg: String = "暂无数据", @DrawableRes iconId: Int? = null) {
    this?.onEmpty {
        iconId?.run {
            findViewById<ImageView>(R.id.empty_view_icon).setImageResource(iconId)
        }
        findViewById<TextView>(R.id.status_hint_content).text = msg
    }?.showEmpty()
}

fun PageRefreshLayout?.error(throwable: Throwable?, retryMsg: String = "重新加载", @DrawableRes iconId: Int? = null, retry: (() -> Unit)? = null) {
    this?.onError {
        iconId?.run {
            findViewById<ImageView>(R.id.error_view_icon).setImageResource(iconId)
        }
        findViewById<TextView>(R.id.status_hint_content).text = throwable?.message?.ifBlank { "发生错误" } ?: "发生错误"
        findViewById<TextView>(R.id.btn_retry).apply {
            text = retryMsg
            doClick {
                retry?.invoke()
            }
            visible()
        }
    }?.showError()
}