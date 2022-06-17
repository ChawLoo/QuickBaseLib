package cn.chawloo.base.widget.indicator

import android.animation.Animator
import android.animation.AnimatorInflater
import android.content.Context
import android.content.res.ColorStateList
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.view.animation.Interpolator
import android.widget.LinearLayout
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.core.view.ViewCompat
import cn.chawloo.base.R
import kotlin.math.abs


open class BaseCircleIndicator @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = -1) : LinearLayout(context, attrs, defStyleAttr) {
    companion object {
        private const val DEFAULT_INDICATOR_WIDTH = 5
    }

    private var mIndicatorMargin = -1
    private var mIndicatorWidth = -1
    private var mIndicatorHeight = -1

    private var mIndicatorBackgroundResId = 0
    private var mIndicatorUnselectedBackgroundResId = 0

    private var mIndicatorTintColor: ColorStateList? = null
    private var mIndicatorTintUnselectedColor: ColorStateList? = null

    private lateinit var mAnimatorOut: Animator
    private lateinit var mAnimatorIn: Animator
    private lateinit var mImmediateAnimatorOut: Animator
    private lateinit var mImmediateAnimatorIn: Animator

    protected var mLastPosition = -1

    private var mIndicatorCreatedListener: IndicatorCreatedListener? = null

    init {
        val config: Config = handleTypedArray(context, attrs)
        initialize(config)
        if (isInEditMode) {
            createIndicators(3, 1)
        }
    }

    private fun handleTypedArray(context: Context, attrs: AttributeSet?): Config {
        if (attrs == null) {
            return Config.Builder().build()
        }
        val ta = context.obtainStyledAttributes(attrs, R.styleable.BaseCircleIndicator)
        val builder = Config.Builder()
            .width(ta.getDimensionPixelSize(R.styleable.BaseCircleIndicator_ci_width, -1))
            .height(ta.getDimensionPixelSize(R.styleable.BaseCircleIndicator_ci_height, -1))
            .margin(ta.getDimensionPixelSize(R.styleable.BaseCircleIndicator_ci_margin, -1))
            .animator(ta.getResourceId(R.styleable.BaseCircleIndicator_ci_animator, R.animator.scale_with_alpha))
            .animatorReverse(ta.getResourceId(R.styleable.BaseCircleIndicator_ci_animator_reverse, 0))
            .drawable(ta.getResourceId(R.styleable.BaseCircleIndicator_ci_drawable, R.drawable.theme_color_radius))
            .drawableUnselected(ta.getResourceId(R.styleable.BaseCircleIndicator_ci_drawable_unselected, R.drawable.theme_color_radius))
            .orientation(ta.getInt(R.styleable.BaseCircleIndicator_ci_orientation, -1))
            .gravity(ta.getInt(R.styleable.BaseCircleIndicator_ci_gravity, -1))
        ta.recycle()
        return builder.build()
    }

    private fun initialize(config: Config) {
        val miniSize = (TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            DEFAULT_INDICATOR_WIDTH.toFloat(),
            resources.displayMetrics
        ) + 0.5f).toInt()
        mIndicatorWidth = if (config.width < 0) miniSize else config.width
        mIndicatorHeight = if (config.height < 0) miniSize else config.height
        mIndicatorMargin = if (config.margin < 0) miniSize else config.margin
        mAnimatorOut = createAnimatorOut(config)
        mImmediateAnimatorOut = createAnimatorOut(config)
        mImmediateAnimatorOut.duration = 0
        mAnimatorIn = createAnimatorIn(config)
        mImmediateAnimatorIn = createAnimatorIn(config)
        mImmediateAnimatorIn.duration = 0
        mIndicatorBackgroundResId = if (config.backgroundResId == 0) R.drawable.theme_color_radius else config.backgroundResId
        mIndicatorUnselectedBackgroundResId = if (config.unselectedBackgroundId == 0) config.backgroundResId else config.unselectedBackgroundId
        orientation = if (config.orientation == VERTICAL) VERTICAL else HORIZONTAL
        gravity = if (config.gravity >= 0) config.gravity else Gravity.CENTER
    }

    fun tintIndicator(@ColorInt indicatorColor: Int) {
        tintIndicator(indicatorColor, indicatorColor)
    }

    fun tintIndicator(@ColorInt indicatorColor: Int, @ColorInt unselectedIndicatorColor: Int) {
        mIndicatorTintColor = ColorStateList.valueOf(indicatorColor)
        mIndicatorTintUnselectedColor = ColorStateList.valueOf(unselectedIndicatorColor)
        changeIndicatorBackground()
    }

    fun changeIndicatorResource(@DrawableRes indicatorResId: Int) {
        changeIndicatorResource(indicatorResId, indicatorResId)
    }

    fun changeIndicatorResource(@DrawableRes indicatorResId: Int, @DrawableRes indicatorUnselectedResId: Int) {
        mIndicatorBackgroundResId = indicatorResId
        mIndicatorUnselectedBackgroundResId = indicatorUnselectedResId
        changeIndicatorBackground()
    }

    interface IndicatorCreatedListener {
        fun onIndicatorCreated(view: View?, position: Int)
    }

    protected fun createAnimatorOut(config: Config): Animator {
        return AnimatorInflater.loadAnimator(context, config.animatorResId)
    }

    protected fun createAnimatorIn(config: Config): Animator {
        val animatorIn: Animator
        if (config.animatorReverseResId == 0) {
            animatorIn = AnimatorInflater.loadAnimator(context, config.animatorResId)
            animatorIn.interpolator = ReverseInterpolator()
        } else {
            animatorIn = AnimatorInflater.loadAnimator(context, config.animatorReverseResId)
        }
        return animatorIn
    }

    fun createIndicators(count: Int, currentPosition: Int) {
        if (mImmediateAnimatorOut.isRunning) {
            mImmediateAnimatorOut.end()
            mImmediateAnimatorOut.cancel()
        }
        if (mImmediateAnimatorIn.isRunning) {
            mImmediateAnimatorIn.end()
            mImmediateAnimatorIn.cancel()
        }

        val childViewCount = childCount
        if (count < childViewCount) {
            removeViews(count, childViewCount - count)
        } else if (count > childViewCount) {
            val addCount = count - childViewCount
            val orientation = orientation
            for (i in 0 until addCount) {
                addIndicator(orientation)
            }
        }

        var indicator: View?
        for (i in 0 until count) {
            indicator = getChildAt(i)
            if (currentPosition == i) {
                bindIndicatorBackground(indicator, mIndicatorBackgroundResId, mIndicatorTintColor)
                mImmediateAnimatorOut.setTarget(indicator)
                mImmediateAnimatorOut.start()
                mImmediateAnimatorOut.end()
            } else {
                bindIndicatorBackground(indicator, mIndicatorUnselectedBackgroundResId, mIndicatorTintUnselectedColor)
                mImmediateAnimatorIn.setTarget(indicator)
                mImmediateAnimatorIn.start()
                mImmediateAnimatorIn.end()
            }
            mIndicatorCreatedListener?.onIndicatorCreated(indicator, i)
        }
        mLastPosition = currentPosition
    }

    protected fun addIndicator(orientation: Int) {
        val indicator = View(context)
        val params = generateDefaultLayoutParams()
        params.width = mIndicatorWidth
        params.height = mIndicatorHeight
        if (orientation == HORIZONTAL) {
            params.leftMargin = mIndicatorMargin
            params.rightMargin = mIndicatorMargin
        } else {
            params.topMargin = mIndicatorMargin
            params.bottomMargin = mIndicatorMargin
        }
        addView(indicator, params)
    }

    fun animatePageSelected(position: Int) {
        if (mLastPosition == position) {
            return
        }
        if (mAnimatorIn.isRunning) {
            mAnimatorIn.end()
            mAnimatorIn.cancel()
        }
        if (mAnimatorOut.isRunning) {
            mAnimatorOut.end()
            mAnimatorOut.cancel()
        }
        var currentIndicator: View? = null
        if (mLastPosition >= 0 && getChildAt(mLastPosition).also { currentIndicator = it } != null) {
            bindIndicatorBackground(currentIndicator!!, mIndicatorUnselectedBackgroundResId, mIndicatorTintUnselectedColor)
            mAnimatorIn.setTarget(currentIndicator)
            mAnimatorIn.start()
        }
        val selectedIndicator = getChildAt(position)
        if (selectedIndicator != null) {
            bindIndicatorBackground(selectedIndicator, mIndicatorBackgroundResId, mIndicatorTintColor)
            mAnimatorOut.setTarget(selectedIndicator)
            mAnimatorOut.start()
        }
        mLastPosition = position
    }

    protected fun changeIndicatorBackground() {
        val count = childCount
        if (count <= 0) {
            return
        }
        var currentIndicator: View?
        for (i in 0 until count) {
            currentIndicator = getChildAt(i)
            if (i == mLastPosition) {
                bindIndicatorBackground(currentIndicator, mIndicatorBackgroundResId, mIndicatorTintColor)
            } else {
                bindIndicatorBackground(currentIndicator, mIndicatorUnselectedBackgroundResId, mIndicatorTintUnselectedColor)
            }
        }
    }

    private fun bindIndicatorBackground(view: View, @DrawableRes drawableRes: Int, tintColor: ColorStateList?) {
        if (tintColor != null) {
            val indicatorDrawable = DrawableCompat.wrap(
                ContextCompat.getDrawable(context, drawableRes)!!.mutate()
            )
            DrawableCompat.setTintList(indicatorDrawable, tintColor)
            ViewCompat.setBackground(view, indicatorDrawable)
        } else {
            view.setBackgroundResource(drawableRes)
        }
    }

    protected class ReverseInterpolator : Interpolator {
        override fun getInterpolation(value: Float): Float {
            return abs(1.0f - value)
        }
    }
}