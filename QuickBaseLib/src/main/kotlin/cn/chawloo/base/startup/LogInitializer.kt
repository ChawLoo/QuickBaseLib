package cn.chawloo.base.startup

import android.content.Context
import androidx.startup.Initializer
import com.safframework.log.L

/**
 * TODO
 * @author Create by 鲁超 on 2021/12/29 0029 9:23:04
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
class LogInitializer : Initializer<Unit> {
    override fun create(context: Context) {
        L.init("DingGc")
        L.displayThreadInfo(false)
    }

    override fun dependencies() = emptyList<Class<Initializer<*>>>()
}