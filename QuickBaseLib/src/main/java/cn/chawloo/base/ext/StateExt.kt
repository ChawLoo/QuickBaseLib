package cn.chawloo.base.ext

import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.DrawableRes
import cn.chawloo.base.R
import com.drake.statelayout.StateLayout
import com.safframework.log.L

/**
 * TODO
 * @author Create by 鲁超 on 2022/6/20 20:15
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
fun StateLayout?.loading(msg: String = "请稍等") {
    this?.showLoading(msg)
}

fun StateLayout?.content() {
    this?.showContent()
}

fun StateLayout?.empty(emptyMsg: String = "暂无数据", @DrawableRes iconId: Int? = null) {
    this?.onEmpty {
        iconId?.run {
            findViewById<ImageView>(R.id.empty_view_icon).setImageResource(iconId)
        }
        findViewById<TextView>(R.id.status_hint_content).text = emptyMsg
    }?.showEmpty()
}

fun StateLayout?.error(
    msg: String = "发生错误",
    retryMsg: String = "重新加载",
    @DrawableRes iconId: Int? = null,
    retry: (() -> Unit)? = null
) {
    this?.onError {
        iconId?.run {
            findViewById<ImageView>(R.id.error_view_icon).setImageResource(iconId)
        }
        findViewById<TextView>(R.id.status_hint_content).text = msg
        findViewById<TextView>(R.id.btn_retry).apply {
            text = retryMsg
            doClick {
                retry?.invoke()
            }
            visible()
        }
    }?.showError()
}
