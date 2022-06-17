package cn.chawloo.base.startup

import android.content.Context
import androidx.startup.Initializer
import cn.chawloo.base.constants.MKKeys
import cn.chawloo.base.utils.MK
import me.jessyan.autosize.AutoSizeConfig

/**
 * TODO
 * @author Create by 鲁超 on 2021/12/29 0029 9:24:59
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
class AutoSizeInitializer : Initializer<Unit> {
    override fun create(context: Context) {
        val textScale = if (MK.decodeBool(MKKeys.LARGE_TEXT, false)) 1.2f else 1.0F
        AutoSizeConfig.getInstance()?.apply {
            //是否屏蔽系统字体大小对 AndroidAutoSize 的影响, 如果为 true, App 内的字体的大小将不会跟随系统设置中字体大小的改变
            //如果为 false, 则会跟随系统设置中字体大小的改变, 默认为 false
            isExcludeFontScale = true
            //区别于系统字体大小的放大比例, AndroidAutoSize 允许 APP 内部可以独立于系统字体大小之外，独自拥有全局调节 APP 字体大小的能力
            //当然, 在 APP 内您必须使用 sp 来作为字体的单位, 否则此功能无效, 不设置或将此值设为 0 则取消此功能
            privateFontScale = textScale
        }
    }

    override fun dependencies() = listOf(MMKVInitializer::class.java)
}