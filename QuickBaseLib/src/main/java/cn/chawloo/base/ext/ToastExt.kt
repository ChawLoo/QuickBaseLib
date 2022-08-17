@file:Suppress("unused")

package cn.chawloo.base.ext

import android.content.Context
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import com.hjq.toast.ToastUtils

fun Fragment.toast(message: CharSequence?) = message?.takeIf { it.isNotBlank() }?.let { requireActivity().toast(it) }
fun Fragment.toast(@StringRes message: Int) = requireActivity().toast(message)
fun Context.toast(message: CharSequence?) = message?.takeIf { it.isNotBlank() }?.let { ToastUtils.show(it) }
fun Context.toast(@StringRes message: Int) = ToastUtils.show(message)
fun toast(message: CharSequence?) = message?.takeIf { it.isNotBlank() }?.let { ToastUtils.show(it) }
fun toast(@StringRes message: Int) = ToastUtils.show(message)
fun Fragment.longToast(message: CharSequence?) = message?.takeIf { it.isNotBlank() }?.let { requireActivity().longToast(it) }
fun Fragment.longToast(@StringRes message: Int) = requireActivity().longToast(message)
fun Context.longToast(message: CharSequence?) = message?.takeIf { it.isNotBlank() }?.let { ToastUtils.show(it) }
fun Context.longToast(@StringRes message: Int) = ToastUtils.show(message)
fun longToast(message: CharSequence?) = message?.takeIf { it.isNotBlank() }?.let { ToastUtils.show(it) }
fun longToast(@StringRes message: Int) = ToastUtils.show(message)
fun Fragment.toast(t: Throwable?) = t?.message?.takeIf { it.isNotBlank() }?.let { requireActivity().toast(it) }
fun Context.toast(t: Throwable?) = t?.message?.takeIf { it.isNotBlank() }?.let { ToastUtils.show(it) }
fun toast(t: Throwable?) = t?.message?.takeIf { it.isNotBlank() }?.let { ToastUtils.show(it) }
fun Fragment.longToast(t: Throwable?) = t?.message?.takeIf { it.isNotBlank() }?.let { requireActivity().toast(it) }
fun Context.longToast(t: Throwable?) = t?.message?.takeIf { it.isNotBlank() }?.let { ToastUtils.show(it) }
fun longToast(t: Throwable?) = t?.message?.takeIf { it.isNotBlank() }?.let { ToastUtils.show(it) }
