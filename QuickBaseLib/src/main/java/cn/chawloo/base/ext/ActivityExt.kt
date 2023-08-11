package cn.chawloo.base.ext

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import androidx.activity.ComponentActivity
import androidx.activity.OnBackPressedCallback
import androidx.annotation.StringRes
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LifecycleOwner
import cn.chawloo.base.base.BaseActivity
import cn.chawloo.base.constants.MKKeys
import cn.chawloo.base.utils.MK
import java.util.LinkedList

/**
 * TODO
 * @author Create by 鲁超 on 2022/6/9 0009 9:59:19
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
internal val activityCache = LinkedList<Activity>()
fun startActivity(intent: Intent) = topActivity.startActivity(intent)
val activityList: List<Activity> get() = activityCache.toList()
val topActivity: Activity get() = activityCache.last()
val firstActivity: Activity get() = activityCache.first()

fun BaseActivity.findPreAct(): BaseActivity? {
    return findPreActivity(this)
}

fun <A : Activity> findPreActivity(currentAct: Activity): A? {
    try {
        val currentIndex = activityCache.indexOf(currentAct)
        if (currentIndex > 0) {
            return activityCache[currentIndex - 1] as? A
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return null
}

inline fun <reified T : Activity> finishActivity(): Boolean = finishActivity(T::class.java)

fun <T : Activity> finishActivity(clazz: Class<T>): Boolean = activityCache.removeAll {
    if (it.javaClass.name == clazz.name) it.finish()
    it.javaClass.name == clazz.name
}

inline fun <reified T : Activity> finishToActivity(): Boolean = finishToActivity(T::class.java)

fun <T : Activity> finishToActivity(clazz: Class<T>): Boolean {
    for (i in activityCache.lastIndex downTo 0) {
        if (clazz.name == activityCache[i].javaClass.name) {
            return true
        }
        activityCache.removeAt(i).finish()
    }
    return false
}

fun finishAllActivities(): Boolean = activityCache.removeAll {
    it.finish()
    true
}

inline fun <reified T : Activity> finishAllActivitiesExcept(): Boolean = finishAllActivitiesExcept(T::class.java)

fun <T : Activity> finishAllActivitiesExcept(clazz: Class<T>): Boolean = activityCache.removeAll {
    if (it.javaClass.name != clazz.name) it.finish()
    it.javaClass.name != clazz.name
}

fun finishAllActivitiesExceptNewest(): Boolean = finishAllActivitiesExcept(topActivity.javaClass)

/**
 * 判断是否存在
 */
inline fun <reified T : Activity> isActExistsInStack(): Boolean = isActExistsInStack(T::class.java)

/**
 * 判断是否存在
 */
fun <T : Activity> isActExistsInStack(clazz: Class<T>): Boolean = activityCache.any { it.javaClass.name == clazz.name }

fun ComponentActivity.pressBackTwiceToExitApp(toastText: String, delayMillis: Long = 2000, owner: LifecycleOwner = this) =
    pressBackTwiceToExitApp(delayMillis, owner) { toast(toastText) }

fun ComponentActivity.pressBackTwiceToExitApp(@StringRes toastText: Int, delayMillis: Long = 2000, owner: LifecycleOwner = this) =
    pressBackTwiceToExitApp(delayMillis, owner) { toast(toastText) }

fun ComponentActivity.pressBackTwiceToExitApp(
    delayMillis: Long = 2000,
    owner: LifecycleOwner = this,
    onFirstBackPressed: () -> Unit
) = onBackPressedDispatcher.addCallback(owner, object : OnBackPressedCallback(true) {
    private var lastBackTime: Long = 0

    override fun handleOnBackPressed() {
        val currentTime = System.currentTimeMillis()
        if (currentTime - lastBackTime > delayMillis) {
            onFirstBackPressed()
            lastBackTime = currentTime
        } else {
            finishAllActivities()
        }
    }
})

fun ComponentActivity.pressBackToNotExitApp(owner: LifecycleOwner = this) =
    doOnBackPressed(owner) { moveTaskToBack(false) }

fun ComponentActivity.doOnBackPressed(owner: LifecycleOwner = this, onBackPressed: () -> Unit) =
    onBackPressedDispatcher.addCallback(owner, object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() = onBackPressed()
    })

fun Context.asActivity(): Activity? = this as? Activity ?: (this as? ContextWrapper)?.baseContext?.asActivity()

inline val Context.context: Context get() = this

inline val Activity.activity: Activity get() = this

inline val FragmentActivity.fragmentActivity: FragmentActivity get() = this

inline val ComponentActivity.lifecycleOwner: LifecycleOwner get() = this

fun Activity.setFontScale() {
    resources.configuration.fontScale = if (MK.decodeBool(MKKeys.LARGE_TEXT)) 1.2f else 1.0F
}