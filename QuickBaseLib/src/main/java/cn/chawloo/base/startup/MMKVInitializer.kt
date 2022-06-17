package cn.chawloo.base.startup

import android.content.Context
import androidx.startup.Initializer
import com.tencent.mmkv.MMKV

/**
 * TODO
 * @author Create by 鲁超 on 2022/1/24 0024 10:39:02
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
class MMKVInitializer : Initializer<Unit> {
    override fun create(context: Context) {
        MMKV.initialize(context)
    }

    override fun dependencies() = emptyList<Class<Initializer<*>>>()
}