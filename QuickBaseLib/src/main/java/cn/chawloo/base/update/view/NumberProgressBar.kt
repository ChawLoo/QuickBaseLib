package cn.chawloo.base.update.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.os.Bundle
import android.os.Parcelable
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import cn.chawloo.base.R
import cn.chawloo.base.ext.dp
import cn.chawloo.base.ext.sp
import kotlin.math.max
import kotlin.math.min

/**
 * 带数字的进度条
 * @author Create by 鲁超 on 2022/4/19 0019 13:44:56
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
class NumberProgressBar @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : View(context, attrs, defStyleAttr) {
    companion object {
        private const val INSTANCE_STATE = "saved_instance"
        private const val INSTANCE_TEXT_COLOR = "text_color"
        private const val INSTANCE_TEXT_SIZE = "text_size"
        private const val INSTANCE_REACHED_BAR_HEIGHT = "reached_bar_height"
        private const val INSTANCE_REACHED_BAR_COLOR = "reached_bar_color"
        private const val INSTANCE_UNREACHED_BAR_HEIGHT = "unreached_bar_height"
        private const val INSTANCE_UNREACHED_BAR_COLOR = "unreached_bar_color"
        private const val INSTANCE_MAX = "max"
        private const val INSTANCE_PROGRESS = "progress"
        private const val INSTANCE_SUFFIX = "suffix"
        private const val INSTANCE_PREFIX = "prefix"
        private const val INSTANCE_TEXT_VISIBILITY = "text_visibility"
    }

    private var defaultProgressTextOffset = 3.0F.dp
    private var defaultTextSize = 10F.sp
    private var defaultReachedBarHeight = 1.5F.dp
    private var defaultUnreachedBarHeight = 1.0F.dp

    private var mMaxProgress = 100

    fun setMaxProgress(max: Int) {
        if (max > 0) {
            mMaxProgress = max
            invalidate()
        }
    }

    var mCurrentProgress = 0

    fun setProgress(progress: Int) {
        when {
            progress in 0..mMaxProgress -> {
                mCurrentProgress = progress
                invalidate()
            }
            progress > mMaxProgress -> {
                mCurrentProgress = mMaxProgress
                invalidate()
            }
            else -> {
                mCurrentProgress = 0
                invalidate()
            }
        }
    }

    private var mReachedBarColor = 0

    fun setReachedBarColor(color: Int) {
        mReachedBarColor = color
        mReachedBarPaint.color = color
        invalidate()
    }

    private var mUnreachedBarColor = 0

    fun setUnreachedBarColor(color: Int) {
        mUnreachedBarColor = color
        mUnreachedBarPaint.color = color
        invalidate()
    }

    private var mTextColor = 0

    fun setTextColor(color: Int) {
        mTextColor = color
        mTextPaint.color = color
        invalidate()
    }

    private var mTextSize = 0F

    fun setTextSize(size: Float) {
        mTextSize = size
        mTextPaint.textSize = size
        invalidate()
    }

    private var mReachedBarHeight = 0F

    private var mUnreachedBarHeight = 0F

    private var mSuffix = "%"
        set(value) {
            field = value.ifBlank { "" }
        }

    private var mPrefix = ""
        set(value) {
            field = value.ifBlank { "" }
        }

    private var mDrawTextWidth = 0F

    private var mDrawTextStart = 0F

    private var mDrawTextEnd = 0F

    private var mCurrentDrawText: String = "0"

    private lateinit var mReachedBarPaint: Paint

    private lateinit var mUnreachedBarPaint: Paint

    private lateinit var mTextPaint: Paint

    private val mUnreachedRectF = RectF(0F, 0F, 0F, 0F)

    private val mReachedRectF = RectF(0F, 0F, 0F, 0F)

    private var mOffset = 0F

    private var mDrawUnreachedBar = true

    private var mDrawReachedBar = true

    private var mIfDrawText = true

    init {
        val ta = context.theme.obtainStyledAttributes(attrs, R.styleable.NumberProgressBar, defStyleAttr, 0)

        mReachedBarColor = ta.getColor(R.styleable.NumberProgressBar_progress_reached_color, ContextCompat.getColor(getContext(), R.color.defaultReachedColor))
        mUnreachedBarColor = ta.getColor(R.styleable.NumberProgressBar_progress_unreached_color, ContextCompat.getColor(getContext(), R.color.defaultUnreachedColor))
        mTextColor = ta.getColor(R.styleable.NumberProgressBar_progress_text_color, ContextCompat.getColor(getContext(), R.color.defaultTextColor))
        mTextSize = ta.getDimension(R.styleable.NumberProgressBar_progress_text_size, defaultTextSize)

        mReachedBarHeight = ta.getDimension(R.styleable.NumberProgressBar_progress_reached_bar_height, defaultReachedBarHeight)
        mUnreachedBarHeight = ta.getDimension(R.styleable.NumberProgressBar_progress_unreached_bar_height, defaultUnreachedBarHeight)
        mOffset = ta.getDimension(R.styleable.NumberProgressBar_progress_text_offset, defaultProgressTextOffset)

        mIfDrawText = ta.getBoolean(R.styleable.NumberProgressBar_progress_text_visibility, true)
        setProgress(ta.getInt(R.styleable.NumberProgressBar_progress_current, 0))
        setMaxProgress(ta.getInt(R.styleable.NumberProgressBar_progress_max, 100))
        ta.recycle()
        initPaint()
    }

    private fun initPaint() {
        mReachedBarPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = mReachedBarColor
        }
        mUnreachedBarPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = mUnreachedBarColor
        }
        mTextPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = mTextColor
            textSize = mTextSize
        }
    }

    override fun getSuggestedMinimumWidth(): Int {
        return mTextSize.toInt()
    }

    override fun getSuggestedMinimumHeight(): Int {
        return max(mTextSize, max(mReachedBarHeight, mUnreachedBarHeight)).toInt()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        setMeasuredDimension(measure(widthMeasureSpec, true), measure(heightMeasureSpec, false))
    }

    private fun measure(measureSpec: Int, isWidth: Boolean): Int {
        val mode = MeasureSpec.getMode(measureSpec)
        val size = MeasureSpec.getSize(measureSpec)
        return if (mode == MeasureSpec.EXACTLY) {
            size
        } else {
            var result = if (isWidth) suggestedMinimumWidth else suggestedMinimumHeight
            result += if (isWidth) paddingLeft + paddingRight else paddingTop + paddingBottom
            if (mode == MeasureSpec.AT_MOST) {
                result = if (isWidth) {
                    max(result, size)
                } else {
                    min(result, size)
                }
            }
            result
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (mIfDrawText) {
            calculateDrawRectF()
        } else {
            calculateDrawRectFWithoutProgressText()
        }
        if (mDrawReachedBar) {
            canvas.drawRect(mReachedRectF, mReachedBarPaint)
        }

        if (mDrawUnreachedBar) {
            canvas.drawRect(mUnreachedRectF, mUnreachedBarPaint)
        }

        if (mIfDrawText) {
            canvas.drawText(mCurrentDrawText, mDrawTextStart, mDrawTextEnd, mTextPaint)
        }
    }

    private fun calculateDrawRectF() {
        mCurrentDrawText = String.format("%d", mCurrentProgress * 100 / mMaxProgress)
        mCurrentDrawText = mPrefix + mCurrentDrawText + mSuffix
        mDrawTextWidth = mTextPaint.measureText(mCurrentDrawText)
        if (mCurrentProgress == 0) {
            mDrawReachedBar = false
            mDrawTextStart = paddingLeft.toFloat()
        } else {
            mDrawReachedBar = true
            mReachedRectF.left = paddingLeft.toFloat()
            mReachedRectF.top = (height - mReachedBarHeight) / 2.0F
            mReachedRectF.right = (width - paddingLeft - paddingRight) / (mMaxProgress) * mCurrentProgress - mOffset + paddingLeft
            mReachedRectF.bottom = (height + mReachedBarHeight) / 2.0F
            mDrawTextStart = mReachedRectF.right + mOffset
        }
        mDrawTextEnd = (height - mTextPaint.descent() - mTextPaint.ascent()) / 2.0F
        if (mDrawTextStart + mDrawTextWidth >= width - paddingRight) {
            mDrawTextStart = width - paddingRight - mDrawTextWidth
            mReachedRectF.right = mDrawTextStart - mOffset
        }
        val unreachedBarStart = mDrawTextStart + mDrawTextWidth + mOffset
        if (unreachedBarStart >= width - paddingRight) {
            mDrawUnreachedBar = false
        } else {
            mDrawUnreachedBar = true
            mUnreachedRectF.left = unreachedBarStart
            mUnreachedRectF.right = (width - paddingRight).toFloat()
            mUnreachedRectF.top = (height - mUnreachedBarHeight) / 2.0F
            mUnreachedRectF.bottom = (height + mUnreachedBarHeight) / 2.0F
        }
    }

    private fun calculateDrawRectFWithoutProgressText() {
        mReachedRectF.left = paddingLeft.toFloat()
        mReachedRectF.top = (height - mReachedBarHeight) / 2.0F
        mReachedRectF.right = ((width - paddingLeft - paddingRight) / (mMaxProgress) * mCurrentProgress + paddingLeft).toFloat()
        mReachedRectF.bottom = (height + mReachedBarHeight) / 2.0F
        mUnreachedRectF.left = mReachedRectF.right
        mUnreachedRectF.right = (width - paddingRight).toFloat()
        mUnreachedRectF.top = (height - mUnreachedBarHeight) / 2.0F
        mUnreachedRectF.bottom = (height + mUnreachedBarHeight) / 2.0F
    }

    override fun onSaveInstanceState(): Parcelable {
        return Bundle().apply {
            putParcelable(INSTANCE_STATE, super.onSaveInstanceState())
            putInt(INSTANCE_TEXT_COLOR, mTextColor)
            putFloat(INSTANCE_TEXT_SIZE, mTextSize)
            putFloat(INSTANCE_REACHED_BAR_HEIGHT, mReachedBarHeight)
            putFloat(INSTANCE_UNREACHED_BAR_HEIGHT, mUnreachedBarHeight)
            putInt(INSTANCE_REACHED_BAR_COLOR, mReachedBarColor)
            putInt(INSTANCE_UNREACHED_BAR_COLOR, mUnreachedBarColor)
            putInt(INSTANCE_MAX, mMaxProgress)
            putInt(INSTANCE_PROGRESS, mCurrentProgress)
            putString(INSTANCE_SUFFIX, mSuffix)
            putString(INSTANCE_PREFIX, mPrefix)
            putBoolean(INSTANCE_TEXT_VISIBILITY, mIfDrawText)
        }
    }

    override fun onRestoreInstanceState(state: Parcelable?) {
        if (state is Bundle) {
            mTextColor = state.getInt(INSTANCE_TEXT_COLOR)
            mTextSize = state.getFloat(INSTANCE_TEXT_SIZE)
            mReachedBarHeight = state.getFloat(INSTANCE_REACHED_BAR_HEIGHT)
            mUnreachedBarHeight = state.getFloat(INSTANCE_UNREACHED_BAR_HEIGHT)
            mReachedBarColor = state.getInt(INSTANCE_REACHED_BAR_COLOR)
            mUnreachedBarColor = state.getInt(INSTANCE_UNREACHED_BAR_COLOR)
            setMaxProgress(state.getInt(INSTANCE_MAX))
            setProgress(state.getInt(INSTANCE_PROGRESS))
            mPrefix = state.getString(INSTANCE_PREFIX) ?: ""
            mSuffix = state.getString(INSTANCE_SUFFIX) ?: ""
            mIfDrawText = state.getBoolean(INSTANCE_TEXT_VISIBILITY)
            super.onRestoreInstanceState(state.getParcelable(INSTANCE_STATE))
            return
        } else {
            super.onRestoreInstanceState(state)
        }
    }
}