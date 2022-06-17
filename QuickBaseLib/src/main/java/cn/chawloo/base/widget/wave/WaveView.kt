package cn.chawloo.base.widget.wave

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.view.animation.LinearInterpolator
import cn.chawloo.base.R
import kotlin.math.cos
import kotlin.math.sin

/**
 * TODO
 * @author Create by 鲁超 on 2022/6/10 0010 18:17:18
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
class WaveView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = -1) : View(context, attrs, defStyleAttr) {
    companion object {
        private const val MOVE_DIRECTION_LEFT = -1
        private const val MOVE_DIRECTION_RIGHT = -2
        private const val WAVE_LOCATION_TOP = -1
        private const val WAVE_LOCATION_BOTTOM = -2
        private const val DRAW_MODE_BEZIER = -1
        private const val DRAW_MODE_SIN = -2
        private const val DRAW_MODE_COS = -3
    }

    /**
     * 控件宽度
     */
    private var mTotalWidth = 0

    /**
     * 控件高度
     */
    private var mTotalHeight = 0

    /**
     * 是否开始动画
     */
    private var mStartAnim = false

    /**
     * 波浪数量
     */
    private var mWaveCount = 0

    /**
     * 波浪颜色
     */
    private var mWaveColor = 0

    /**
     * 波浪颜色2
     */
    private var mWaveColor2 = 0

    /**
     * 水位线高度
     */
    private var mWaterLevelHeight = 0f

    /**
     * 水位线纵坐标
     */
    private var mWaterLevelY = 0f

    /**
     * 波浪振幅
     */
    private var mWaveAmplitude = 0f

    /**
     * 波浪振幅2
     */
    private var mWaveAmplitude2 = 0f

    /**
     * 波浪波长
     */
    private var mWaveLength = 0f

    /**
     * 波浪波长2
     */
    private var mWaveLength2 = 0f

    /**
     * 波浪波长占总宽度百分比
     */
    private var mWaveLengthPercent = 0f

    /**
     * 波浪波长占总宽度百分比2
     */
    private var mWaveLengthPercent2 = 0f

    /**
     * 波浪默认偏移量
     */
    private var mDefOffset = 0f

    /**
     * 波浪默认偏移量2
     */
    private var mDefOffset2 = 0f

    /**
     * 波浪默认偏移量占波长的百分比
     */
    private var mDefOffsetPercent = 0f

    /**
     * 波浪默认偏移量占波长的百分比2
     */
    private var mDefOffsetPercent2 = 0f

    /**
     * 波浪偏移量
     */
    private var mAnimOffset = 0f

    /**
     * 波浪偏移量2
     */
    private var mAnimOffset2 = 0f

    /**
     * 波浪当前偏移量
     */
    private var mLastAnimOffset = 0f

    /**
     * 波浪当前偏移量2
     */
    private var mLastAnimOffset2 = 0f

    /**
     * 波浪周期时长
     */
    private var mCycleDuration = 0

    /**
     * 波浪周期时长2
     */
    private var mCycleDuration2 = 0

    /**
     * 波浪移动方向（左、右）
     */
    private var mMoveDirection = 0

    /**
     * 波浪位置（顶部、底部）
     */
    private var mWaveLocation = 0

    /**
     * 波浪绘制模式
     */
    private var mDrawMode = 0

    private var mAnimator: ValueAnimator? = null

    private var mPaint: Paint

    private val mPath: Path

    init {
        val ta = context.obtainStyledAttributes(attrs, R.styleable.WaveView)
        mWaterLevelHeight = ta.getDimension(R.styleable.WaveView_waterLevelHeight, 0f)
        mWaveCount = ta.getInteger(R.styleable.WaveView_waveCount, 1)
        mCycleDuration = ta.getInteger(R.styleable.WaveView_cycleDuration, 5000)
        mCycleDuration2 = ta.getInteger(R.styleable.WaveView_cycleDuration2, mCycleDuration)
        mWaveColor = ta.getColor(R.styleable.WaveView_waveColor, -0x77e16f01)
        mWaveColor2 = ta.getColor(R.styleable.WaveView_waveColor2, -0x77e16f01)
        mWaveAmplitude = ta.getDimension(R.styleable.WaveView_waveAmplitude, 30f)
        mWaveAmplitude2 = ta.getDimension(R.styleable.WaveView_waveAmplitude2, 30f)
        mWaveLength = ta.getDimension(R.styleable.WaveView_waveLength, 0f)
        mWaveLength2 = ta.getDimension(R.styleable.WaveView_waveLength2, 0f)
        mWaveLengthPercent = ta.getFloat(R.styleable.WaveView_waveLengthPercent, 1f)
        mWaveLengthPercent2 = ta.getFloat(R.styleable.WaveView_waveLengthPercent2, 1f)
        mDefOffset = ta.getDimension(R.styleable.WaveView_waveDefOffset, 0f)
        mDefOffset2 = ta.getDimension(R.styleable.WaveView_waveDefOffset2, 0f)
        mDefOffsetPercent = ta.getFloat(R.styleable.WaveView_waveDefOffsetPercent, 0f)
        mDefOffsetPercent2 = ta.getFloat(R.styleable.WaveView_waveDefOffsetPercent2, 0f)
        mMoveDirection = ta.getInt(R.styleable.WaveView_moveDirection, MOVE_DIRECTION_RIGHT)
        mWaveLocation = ta.getInt(R.styleable.WaveView_waveLocation, WAVE_LOCATION_BOTTOM)
        mDrawMode = ta.getInt(R.styleable.WaveView_drawMode, DRAW_MODE_BEZIER)
        mStartAnim = ta.getBoolean(R.styleable.WaveView_startAnim, true)
        ta.recycle()

        mPaint = Paint()
        mPaint.style = Paint.Style.FILL
        mPaint.isAntiAlias = true
        mPath = Path()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        // 如果控件宽度是 wrap_content，就改成 match_parent
        if (layoutParams.width == ViewGroup.LayoutParams.WRAP_CONTENT) {
            layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT
        }
        if (layoutParams.height == ViewGroup.LayoutParams.WRAP_CONTENT && mWaterLevelHeight > 0) {
            // 如果控件高度是 wrap_content 和设置了水位线高度，则高度设为水位线高度 + 最大振幅
            val h: Int = (mWaterLevelHeight + mWaveAmplitude.coerceAtLeast(mWaveAmplitude2)).toInt()
            setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec), h)
        }
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        mTotalWidth = measuredWidth
        mTotalHeight = measuredHeight

        // 用水位线高度计算水位线纵坐标
        mWaterLevelY = if (mWaterLevelHeight == 0F) {
            // 如果没有设置水位线高度，则水位线纵坐标为 控件高度 - 最大振幅
            if (mWaveLocation == WAVE_LOCATION_TOP) {
                mTotalHeight - mWaveAmplitude.coerceAtLeast(mWaveAmplitude2)
            } else {
                mWaveAmplitude.coerceAtLeast(mWaveAmplitude2)
            }
        } else {
            if (mWaveLocation == WAVE_LOCATION_TOP) {
                mWaterLevelHeight
            } else {
                mTotalHeight - mWaterLevelHeight
            }
        }
        // 波长如果没有设置具体数值，则为 总宽度 * 波长占总宽度的百分比
        if (mWaveLength == 0f) {
            mWaveLength = mTotalWidth * mWaveLengthPercent
        }
        // 默认偏移量如果没有设置具体数值，则为 波长 * 默认偏移量占波长的百分比
        if (mDefOffset == 0f) {
            mDefOffset = mWaveLength * mDefOffsetPercent
        }
        if (mWaveCount > 1) {
            if (mWaveLength2 == 0f) {
                mWaveLength2 = mTotalWidth * mWaveLengthPercent2
            }
            if (mDefOffset2 == 0f) {
                mDefOffset2 = mWaveLength2 * mDefOffsetPercent2
            }
        }
        if (mStartAnim) {
            startAnim()
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (mWaveCount > 1) {
            mPaint.color = mWaveColor2
            canvas.drawPath(getPath(mWaveLength2, mWaveAmplitude2, mDefOffset2, mAnimOffset2), mPaint)
        }
        mPaint.color = mWaveColor
        canvas.drawPath(getPath(mWaveLength, mWaveAmplitude, mDefOffset, mAnimOffset), mPaint)
    }

    /**
     * 获取路径
     *
     * @param waveLength    波长
     * @param waveAmplitude 振幅
     * @param defOffset     默认偏移量
     * @param animOffset    实时偏移量
     * @return 路径
     */
    private fun getPath(waveLength: Float, waveAmplitude: Float, defOffset: Float, animOffset: Float): Path {
        var mAnimOffset = animOffset
        mPath.reset()
        if (mMoveDirection == MOVE_DIRECTION_LEFT) {
            mAnimOffset = -mAnimOffset
        }
        var offset = mAnimOffset + defOffset

        // 计算波浪曲线路径
        if (mDrawMode == DRAW_MODE_BEZIER) { // 用贝塞尔曲线计算波浪曲线路径
            val halfWaveLength = waveLength / 2
            val extraHalfWaveCount = 2 //
            val totalHalfWaveCount = (mTotalWidth / halfWaveLength + 1).toInt() + extraHalfWaveCount

            // 控制偏移量在 0 到波长之间
            offset %= waveLength
            if (offset < 0) {
                offset += waveLength
            }
            // 移动到波浪曲线左端点
            mPath.moveTo(halfWaveLength * -2 + offset, mWaterLevelY)

            // 计算贝塞尔曲线路径
            var controlPointsY: Float
            for (i in 0 until totalHalfWaveCount) {
                controlPointsY = getControlPointsY(waveAmplitude, i)
                mPath.rQuadTo(halfWaveLength / 2, controlPointsY, halfWaveLength, 0f)
            }
        } else { // 用三角函数计算波浪曲线路径
            var x = 0
            var y: Float
            while (x < mTotalWidth) {
                y = getTrigonometricY(waveLength, waveAmplitude, offset, x)
                mPath.lineTo(x.toFloat(), y)
                x++
            }
        }

        // 闭合路径
        if (mWaveLocation == WAVE_LOCATION_TOP) {
            mPath.lineTo(mTotalWidth.toFloat(), 0f)
            mPath.lineTo(0f, 0f)
        } else {
            mPath.lineTo(mTotalWidth.toFloat(), mTotalHeight.toFloat())
            mPath.lineTo(0f, mTotalHeight.toFloat())
        }
        mPath.close()
        return mPath
    }

    /**
     * 获取贝塞尔控制点纵坐标
     *
     * @param waveAmplitude 波浪振幅
     * @param halfWaveIndex 半波浪的索引位置
     * @return 控制点纵坐标
     */
    private fun getControlPointsY(waveAmplitude: Float, halfWaveIndex: Int): Float {
        val indexDirection = if (halfWaveIndex % 2 == 0) 1 else -1
        return waveAmplitude * indexDirection * 2
    }

    /**
     * 获取三角函数纵坐标
     *
     * @param waveLength    波长
     * @param waveAmplitude 振幅
     * @param offset        偏移量
     * @param x             横坐标
     * @return 三角函数纵坐标
     */
    private fun getTrigonometricY(waveLength: Float, waveAmplitude: Float, offset: Float, x: Int): Float {
        if (waveAmplitude == 0f) {
            return mWaterLevelY
        }
        val w = 2 * Math.PI / waveLength // 角速度 ω = 2π/T
        val angle = w * x - offset / waveLength * 2 * Math.PI
        val trigonometricValue: Double = when (mDrawMode) {
            DRAW_MODE_SIN -> {
                sin(angle)
            }
            DRAW_MODE_COS -> {
                cos(angle)
            }
            else -> {
                0.0
            }
        }
        return (waveAmplitude * trigonometricValue + mWaterLevelY).toFloat()
    }

    /**
     * 开始动画
     */
    fun startAnim() {
        if (mWaveLength == 0f || mAnimator != null) {
            return
        }
        mAnimator = ValueAnimator.ofFloat(0f, mWaveLength).apply {
            interpolator = LinearInterpolator()
            addUpdateListener {
                if (mStartAnim) {
                    val offset = it.animatedValue as Float
                    if (mWaveCount > 1) {
                        mAnimOffset2 = mLastAnimOffset2 + offset * mCycleDuration / mCycleDuration2
                        if (mAnimOffset2 >= mWaveLength2) {
                            mAnimOffset2 %= mWaveLength2
                        }
                    }
                    mAnimOffset = mLastAnimOffset + offset
                    if (mAnimOffset >= mWaveLength) {
                        mAnimOffset %= mWaveLength
                    }
                    postInvalidate()
                }
            }
            addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    super.onAnimationEnd(animation)
                    mLastAnimOffset = mAnimOffset
                    mLastAnimOffset2 = mAnimOffset2
                }

                override fun onAnimationRepeat(animation: Animator) {
                    super.onAnimationRepeat(animation)
                    mLastAnimOffset = mAnimOffset
                    mLastAnimOffset2 = mAnimOffset2
                }
            })
            duration = mCycleDuration.toLong()
            repeatCount = ValueAnimator.INFINITE
            start()
        }
        mStartAnim = true
    }

    /**
     * 停止动画
     */
    fun stopAnim() {
        mAnimator?.run {
            this.cancel()
            mAnimator = null
            mStartAnim = false
        }
    }
}