package cn.chawloo.base.widget.tablayout.listener

/**
 * 在选项卡上选择侦听器
 *
 * @author Create by 鲁超 on 2020/4/25 0025 19:24
 */
interface OnTabSelectListener {
    fun onTabSelect(position: Int)
    fun onTabReselect(position: Int)
}