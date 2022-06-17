package cn.chawloo.base.widget.tablayout

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.StateListDrawable
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import cn.chawloo.base.R
import cn.chawloo.base.ext.dp

/**
 * 消息视图
 * @author Create by 鲁超 on 2021/3/19 0019 11:24
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
class MsgView : AppCompatTextView {
    private var mContext: Context? = null
    private val gdBackground = GradientDrawable()
    private var mBackgroundColor = 0
        set(value) {
            field = value
            setBgSelector()
        }
    private var cornerRadius = 0
        set(value) {
            field = value.dp
            setBgSelector()
        }
    var strokeWidth = 0
        set(value) {
            field = value.dp
            setBgSelector()
        }
    private var strokeColor = 0
        set(value) {
            field = value
            setBgSelector()
        }
    private var isRadiusHalfHeight = false
        set(value) {
            field = value
            setBgSelector()
        }
    private var isWidthHeightEqual = false
        set(value) {
            field = value
            setBgSelector()
        }

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyle: Int) : super(context, attrs, defStyle) {
        this.mContext = context
        obtainAttributes(context, attrs)
    }

    private fun obtainAttributes(context: Context, attrs: AttributeSet?) {
        val ta = context.obtainStyledAttributes(attrs, R.styleable.MsgView)
        mBackgroundColor = ta.getColor(R.styleable.MsgView_mv_backgroundColor, Color.TRANSPARENT)
        cornerRadius = ta.getDimensionPixelSize(R.styleable.MsgView_mv_cornerRadius, 0)
        strokeWidth = ta.getDimensionPixelSize(R.styleable.MsgView_mv_strokeWidth, 0)
        strokeColor = ta.getColor(R.styleable.MsgView_mv_strokeColor, Color.TRANSPARENT)
        isRadiusHalfHeight = ta.getBoolean(R.styleable.MsgView_mv_isRadiusHalfHeight, false)
        isWidthHeightEqual = ta.getBoolean(R.styleable.MsgView_mv_isWidthHeightEqual, false)
        ta.recycle()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        if (isWidthHeightEqual && width > 0 && height > 0) {
            val max = width.coerceAtLeast(height)
            val measureSpec = MeasureSpec.makeMeasureSpec(max, MeasureSpec.EXACTLY)
            super.onMeasure(measureSpec, measureSpec)
            return
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        if (isRadiusHalfHeight) {
            cornerRadius = height / 2
        } else {
            setBgSelector()
        }
    }

    private fun setDrawable(gd: GradientDrawable, color: Int, strokeColor: Int) {
        gd.setColor(color)
        gd.cornerRadius = cornerRadius.toFloat()
        gd.setStroke(strokeWidth, strokeColor)
    }

    fun setBgSelector() {
        val bg = StateListDrawable()
        setDrawable(gdBackground, mBackgroundColor, strokeColor)
        bg.addState(intArrayOf(-android.R.attr.state_pressed), gdBackground)
        background = bg
    }
}