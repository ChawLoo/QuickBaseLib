package cn.chawloo.base.startup

import android.content.Context
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.startup.Initializer
import cn.chawloo.base.R
import com.zy.multistatepage.MultiStateConfig
import com.zy.multistatepage.MultiStatePage

/**
 * TODO
 * @author Create by 鲁超 on 2022/6/8 0008 16:35:16
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
class MultiStateInitializer : Initializer<Unit> {
    override fun create(context: Context) {
        MultiStatePage.config(
            MultiStateConfig.Builder()
                .alphaDuration(300)
                .errorIcon(R.drawable.ic_error)
                .emptyIcon(R.drawable.ic_empty)
                .emptyMsg("暂无数据")
                .loadingMsg(context.getString(R.string.loading))
                .errorMsg("发生错误!")
                .build()
        )
    }

    override fun dependencies() = emptyList<Class<Initializer<*>>>()
}