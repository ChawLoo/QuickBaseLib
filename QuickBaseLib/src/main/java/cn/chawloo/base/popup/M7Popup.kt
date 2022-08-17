package cn.chawloo.base.popup

import android.content.Context

/**
 * 显示通用确认弹窗
 */
fun showConfirmWindow(
    c: Context,
    title: String = "温馨提示",
    content: String,
    leftStr: String? = null,
    rightStr: String = "确定",
    cancel: (() -> Unit)? = null,
    confirm: () -> Unit
): CommonConfirmPopupWindow {
    return CommonConfirmPopupWindow.Builder(c)
        .title(title)
        .content(content)
        .cancel(leftStr, cancel)
        .confirm(rightStr, confirm)
        .build()
}

/**
 * 显示通用的长文本弹窗
 */
fun showLongContentWindow(c: Context?, title: String, content: String): CommonLongContentPopupWindow {
    return CommonLongContentPopupWindow(c, title, content).apply { showPopupWindow() }
}


fun showBottomListPopup(c: Context, dataList: List<String>, picker: (String) -> Unit) {
    BottomListPickerPopupWindow(c, dataList, picker).showPopupWindow()
}

fun showListPopup(c: Context, title: String, dataList: List<String>) {
    CommonListPopupWindow(c, title, dataList).showPopupWindow()
}