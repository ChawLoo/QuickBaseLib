package cn.chawloo.base.popup

import android.content.Context
import androidx.core.content.ContextCompat
import androidx.core.text.color
import cn.chawloo.base.R
import cn.chawloo.base.ext.size
import cn.chawloo.base.ext.sp

/**
 * 显示通用确认弹窗
 */
fun showConfirmWindow(
    c: Context,
    title: String = "温馨提示",
    content: String,
    cancelable: Boolean = true,
    leftStr: String = "取消",
    rightStr: String = "确定",
    cancel: (() -> Unit)? = null,
    confirm: () -> Unit
): CommonConfirmPopupWindow {
    return CommonConfirmPopupWindow.Builder(c)
        .buildMessage {
            color(ContextCompat.getColor(c, R.color.color_33)) {
                size(14.sp) {
                    append(content)
                }
            }
        }
        .cancel(leftStr, cancel)
        .confirm(rightStr, confirm)
        .also {
            if (!cancelable) {
                it.cancelEnable()
            }
        }
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