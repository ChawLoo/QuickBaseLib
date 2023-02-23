@file:Suppress("unused")

package cn.chawloo.base.ext

import androidx.annotation.StringRes
import com.hjq.toast.Toaster

fun toast(message: CharSequence?) = message?.takeIf { it.isNotBlank() }?.let { Toaster.show(it) }
fun toast(@StringRes message: Int) = Toaster.show(message)
fun toast(t: Throwable?) = t?.message?.takeIf { it.isNotBlank() }?.let { Toaster.show(it) }
fun longToast(@StringRes message: Int) = Toaster.showLong(message)
fun longToast(message: CharSequence?) = message?.takeIf { it.isNotBlank() }?.let { Toaster.showLong(it) }
fun longToast(t: Throwable?) = t?.message?.takeIf { it.isNotBlank() }?.let { Toaster.showLong(it) }
