package cn.chawloo.base.ext

import android.widget.TextView
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
fun StateLayout?.showLoading(msg: String = "请稍等") {
    this?.showLoading(msg)
}

fun StateLayout?.showSuccess() {
    this?.showContent()
}

fun StateLayout?.showEmpty(
    emptyMsg: String = "暂无数据",
    retryMsg: String = "重新加载",
    retry: () -> Unit = { L.e("点击了重试,但是没有事件") }
) {
    this?.onError {
        findViewById<TextView>(R.id.status_hint_content).text = emptyMsg
        findViewById<TextView>(R.id.btn_retry).text = retryMsg
        findViewById<TextView>(R.id.btn_retry).setOnClickListener {
            retry()
        }
    }
}

fun StateLayout?.showError(
    msg: String = "发生错误",
    retryMsg: String = "重新加载",
    retry: (() -> Unit)? = null
) {
    this?.onError {
        findViewById<TextView>(R.id.status_hint_content).text = msg
        findViewById<TextView>(R.id.btn_retry).text = retryMsg
        findViewById<TextView>(R.id.btn_retry).setOnClickListener {
            retry?.invoke()
        }
    }
}
