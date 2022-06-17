package cn.chawloo.base.widget.tablayout.listener

import androidx.annotation.DrawableRes

/**
 * 自定义选项卡实体
 *
 * @author Create by 鲁超 on 2020/4/25 0025 19:23
 */
interface CustomTabEntity {
    val tabTitle: String

    @get:DrawableRes
    val tabSelectedIcon: Int

    @get:DrawableRes
    val tabUnselectedIcon: Int
}