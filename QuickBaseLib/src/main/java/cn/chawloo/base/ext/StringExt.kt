package com.zhong360.base.ext

import java.util.regex.Pattern

/**
 * TODO
 * @author Create by 鲁超 on 2021/04/10/0010 20:41
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

fun String?.isMobile(): Boolean {
    if (isNullOrBlank()) {
        return false
    }
    val regMobile = "^(1[3-9][0-9])\\d{8}$"
    val regexMobile = Pattern.compile(regMobile)
    return regexMobile.matcher(this).matches()
}