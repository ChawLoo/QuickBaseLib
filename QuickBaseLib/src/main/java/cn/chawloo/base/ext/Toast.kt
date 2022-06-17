@file:Suppress("unused")

package cn.chawloo.base.ext

import android.content.Context
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import com.hjq.toast.ToastUtils

fun Fragment.toast(message: CharSequence?) = requireActivity().toast(message)
fun Fragment.toast(@StringRes message: Int) = requireActivity().toast(message)
fun Context.toast(message: CharSequence?) = ToastUtils.show(message)
fun Context.toast(@StringRes message: Int) = ToastUtils.show(message)
fun toast(message: CharSequence?) = ToastUtils.show(message)
fun toast(@StringRes message: Int) = ToastUtils.show(message)
fun Fragment.longToast(message: CharSequence?) = requireActivity().longToast(message)
fun Fragment.longToast(@StringRes message: Int) = requireActivity().longToast(message)
fun Context.longToast(message: CharSequence?) = ToastUtils.show(message)
fun Context.longToast(@StringRes message: Int) = ToastUtils.show(message)
fun longToast(message: CharSequence?) = ToastUtils.show(message)
fun longToast(@StringRes message: Int) = ToastUtils.show(message)
