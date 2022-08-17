package cn.chawloo.base.ext

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.activity.ComponentActivity
import androidx.activity.OnBackPressedCallback
import androidx.annotation.StringRes
import androidx.core.os.bundleOf
import androidx.core.view.WindowCompat
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LifecycleOwner
import java.util.*
import kotlin.reflect.KClass

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
val activityList: List<Activity> get() = activityCache.toList()
val topActivity: Activity get() = activityCache.last()
val firstActivity: Activity get() = activityCache.first()

fun startActivity(intent: Intent) = topActivity.startActivity(intent)

inline fun <reified T : Activity> startActivity(
    vararg pairs: Pair<String, Any?>,
    crossinline block: Intent.() -> Unit = {}
) = topActivity.startActivity<T>(pairs = pairs, block = block)

inline fun <reified T : Activity> Context.startActivity(
    vararg pairs: Pair<String, Any?>,
    crossinline block: Intent.() -> Unit = {}
) = startActivity(intentOf<T>(*pairs).apply(block))

fun Activity.finishWithResult(vararg pairs: Pair<String, *>) {
    setResult(Activity.RESULT_OK, Intent().putExtras(bundleOf(*pairs)))
    finish()
}

fun Activity.finishWithResult(bundle: Bundle) {
    setResult(Activity.RESULT_OK, Intent().putExtras(bundle))
    finish()
}

fun Activity.finishWithResult(createBundle: Bundle.() -> Bundle) {
    finishWithResult(createBundle)
}

inline fun <reified T : Activity> isActivityExistsInStack(): Boolean = isActivityExistsInStack(T::class.java)

fun <T : Activity> isActivityExistsInStack(clazz: Class<T>): Boolean = activityCache.any { it.javaClass == clazz }

inline fun <reified T : Activity> finishActivity(): Boolean = finishActivity(T::class)

fun <T : Activity> finishActivity(clazz: KClass<T>): Boolean =
    activityCache.removeAll {
        if (it.javaClass == clazz) it.finish()
        it.javaClass == clazz
    }

fun finishAllActivities(): Boolean =
    activityCache.removeAll {
        it.finish()
        true
    }

fun finishAllActivitiesExceptNewest(): Boolean =
    topActivity.let { topActivity ->
        activityCache.removeAll {
            if (it != topActivity) it.finish()
            it != topActivity
        }
    }

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

var Activity.decorFitsSystemWindows: Boolean
    @Deprecated(NO_GETTER, level = DeprecationLevel.ERROR)
    get() = noGetter()
    set(value) = WindowCompat.setDecorFitsSystemWindows(window, value)
inline val Activity.contentView: View get() = (findViewById<View>(android.R.id.content) as ViewGroup).getChildAt(0)

val Context.activity: Activity?
    get() {
        var context: Context? = this
        while (context is ContextWrapper) {
            if (context is Activity) {
                return context
            }
            context = context.baseContext
        }
        return null
    }

inline val Context.context: Context get() = this

inline val Activity.activity: Activity get() = this

inline val FragmentActivity.fragmentActivity: FragmentActivity get() = this

inline val ComponentActivity.lifecycleOwner: LifecycleOwner get() = this