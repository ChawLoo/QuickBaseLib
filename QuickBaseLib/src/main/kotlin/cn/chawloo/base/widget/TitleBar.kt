package cn.chawloo.base.widget

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.appcompat.widget.AppCompatTextView
import androidx.constraintlayout.widget.ConstraintLayout
import cn.chawloo.base.R
import cn.chawloo.base.databinding.TitleBarBinding
import cn.chawloo.base.ext.*

/**
 * TODO
 * @author Create by 鲁超 on 2022/6/11 0011 11:23:52
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
class TitleBar @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleRes: Int = -1) : ConstraintLayout(context, attrs, defStyleRes) {
    companion object {
        //字体颜色为白色
        const val DARK_MODE = 1

        //字体颜色为黑色
        const val LIGHT_MODE = 0
    }

    private var vb: TitleBarBinding = TitleBarBinding.bind(inflate(context, R.layout.title_bar, this))

    var title = ""
        set(value) {
            vb.tvTitle.text = value
            value.takeIf { it.isBlank() }?.run { vb.tvTitle.gone() }
            field = value
            setActTitle()
        }

    var actionTxt = ""
        set(value) {
            vb.tvAction.text = value
            value.takeIf { it.isBlank() }?.run { vb.tvAction.gone() } ?: vb.tvAction.visible()
            field = value
        }

    var bgColor = Color.TRANSPARENT
        set(value) {
            vb.rlTitleBarContainer.setBackgroundColor(value)
            field = value
        }
    var isDarkMode = if (context.isDarkMode()) DARK_MODE else LIGHT_MODE
        set(value) {
            field = value
            if (value == LIGHT_MODE) {
                //颜色跟实际模式应该相反，暗黑模式，字体和图标是白色或亮色的
                vb.tvTitle.setTextColor(Color.WHITE)
                vb.tvBack.setTextColor(Color.WHITE)
                vb.tvAction.setTextColor(Color.WHITE)
                vb.tvBack.compoundDrawables.forEach {
                    it?.tintColor(Color.WHITE)
                }
            } else {
                vb.tvTitle.setTextColor(Color.BLACK)
                vb.tvBack.setTextColor(Color.BLACK)
                vb.tvAction.setTextColor(Color.BLACK)
                vb.tvBack.compoundDrawables.forEach {
                    it?.tintColor(Color.BLACK)
                }
            }
        }

    fun getBackView(): AppCompatTextView {
        return vb.tvBack
    }

    fun setActionClick(click: OnClickListener) {
        vb.tvAction.doClick {
            click.onClick(this)
        }
    }

    fun setActionIcon(@DrawableRes resId: Int): TextView {
        return vb.tvAction.drawable(DrawableLocation.END, resId).visible()
    }

    fun goneBack() {
        vb.tvBack.gone()
    }

    init {
        val ta = context.obtainStyledAttributes(attrs, R.styleable.TitleBar)
        title = ta.getString(R.styleable.TitleBar_android_title) ?: ""
        actionTxt = ta.getString(R.styleable.TitleBar_actionTxt) ?: ""
        bgColor = ta.getColor(R.styleable.TitleBar_android_background, Color.TRANSPARENT)
        title.takeIf { it.isNotBlank() }?.run {
            vb.tvTitle.text = title
            vb.tvTitle.visible()
        }
        vb.tvBack.drawable(DrawableLocation.START, R.drawable.ic_arrow_left).doClick {
            context.activity?.onBackPressed()
        }
        isDarkMode = ta.getInt(R.styleable.TitleBar_lightText, DARK_MODE)
        ta.recycle()
        setActTitle()
        activityList.takeIf { it.size < 2 || firstActivity == context.activity }?.run {
            vb.tvBack.gone()
        } ?: vb.tvBack.visible()
    }

    private fun setActTitle() {
        context.activity?.title = title
    }
}