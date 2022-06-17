package cn.chawloo.base.widget.tablayout

import android.animation.TypeEvaluator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.os.Parcelable
import android.util.AttributeSet
import android.util.SparseBooleanArray
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.view.animation.OvershootInterpolator
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import cn.chawloo.base.R
import cn.chawloo.base.ext.dp
import cn.chawloo.base.ext.gone
import cn.chawloo.base.ext.sp
import cn.chawloo.base.widget.tablayout.listener.OnTabSelectListener
import cn.chawloo.base.widget.tablayout.utils.FragmentChangeManager
import cn.chawloo.base.widget.tablayout.utils.UnreadMsgUtils
import java.util.*

/**
 * TODO
 * @author Create by 鲁超 on 2022/6/8 0008 9:43:21
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
class SegmentTabLayout @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : FrameLayout(context, attrs, defStyleAttr),
    ValueAnimator.AnimatorUpdateListener {
    private var mTitles = emptyList<String>()
    private var mTabsContainer: LinearLayout
    private var mCurrentTab = 0
    private var mLastTab = 0
    private var mTabCount = 0

    /**
     * 用于绘制显示器
     */
    private val mIndicatorRect = Rect()
    private val mIndicatorDrawable = GradientDrawable()
    private val mRectDrawable = GradientDrawable()

    private val mDividerPaint = Paint(Paint.ANTI_ALIAS_FLAG)

    private var mTabPadding = 0f
    private var mTabSpaceEqual = false
    private var mTabWidth = 0f

    /**
     * indicator
     */
    private var mIndicatorColor = 0
    private var mIndicatorHeight = 0f
    private var mIndicatorCornerRadius = 0f
    private var mIndicatorMarginLeft = 0f
    private var mIndicatorMarginTop = 0f
    private var mIndicatorMarginRight = 0f
    private var mIndicatorMarginBottom = 0f
    private var mIndicatorAnimDuration: Long = 0
    private var mIndicatorAnimEnable = false
    private var mIndicatorBounceEnable = false

    /**
     * divider
     */
    private var mDividerColor = 0
    private var mDividerWidth = 0f
    private var mDividerPadding = 0f

    companion object {
        private const val TEXT_BOLD_NONE = 0
        private const val TEXT_BOLD_WHEN_SELECT = 1
        private const val TEXT_BOLD_BOTH = 2
    }

    private var mTextSize = 0f
    private var mTextSelectColor = 0
    private var mTextUnselectColor = 0
    private var mTextBold = 0
    private var mTextAllCaps = false

    private var mBarColor = 0
    private var mBarStrokeColor = 0
    private var mBarStrokeWidth = 0f

    private var mHeight = 0

    /**
     * anim
     */
    private var mValueAnimator: ValueAnimator
    private val mInterpolator = OvershootInterpolator(0.8f)

    private var mFragmentChangeManager: FragmentChangeManager? = null
    private val mRadiusArr = FloatArray(8)

    private val mCurrentP: IndicatorPoint = IndicatorPoint()
    private val mLastP: IndicatorPoint = IndicatorPoint()

    init {
        setWillNotDraw(false)
        clipChildren = false
        clipToPadding = false

        mTabsContainer = LinearLayout(context)
        addView(mTabsContainer)

        obtainAttributes(attrs)

        val height: String? = attrs?.getAttributeValue("http://schemas.android.com/apk/res/android", "layout_height")

        if (height != ViewGroup.LayoutParams.MATCH_PARENT.toString() && height != ViewGroup.LayoutParams.WRAP_CONTENT.toString()) {
            val systemAttrs = intArrayOf(android.R.attr.layout_height)
            val ta = context.obtainStyledAttributes(attrs, systemAttrs)
            mHeight = ta.getDimensionPixelSize(0, ViewGroup.LayoutParams.WRAP_CONTENT)
            ta.recycle()
        }

        mValueAnimator = ValueAnimator.ofObject(PointEvaluator(), mLastP, mCurrentP).apply {
            addUpdateListener(this@SegmentTabLayout)
        }
    }

    private fun obtainAttributes(attrs: AttributeSet?) {
        val ta = context.obtainStyledAttributes(attrs, R.styleable.SegmentTabLayout)

        mIndicatorColor = ta.getColor(R.styleable.SegmentTabLayout_tl_indicator_color, Color.parseColor("#222831"))
        mIndicatorHeight = ta.getDimension(R.styleable.SegmentTabLayout_tl_indicator_height, -1f)
        mIndicatorCornerRadius = ta.getDimension(R.styleable.SegmentTabLayout_tl_indicator_corner_radius, -1f)
        mIndicatorMarginLeft = ta.getDimension(R.styleable.SegmentTabLayout_tl_indicator_margin_left, 0f.dp)
        mIndicatorMarginTop = ta.getDimension(R.styleable.SegmentTabLayout_tl_indicator_margin_top, 0f)
        mIndicatorMarginRight = ta.getDimension(R.styleable.SegmentTabLayout_tl_indicator_margin_right, 0f.dp)
        mIndicatorMarginBottom = ta.getDimension(R.styleable.SegmentTabLayout_tl_indicator_margin_bottom, 0f)
        mIndicatorAnimEnable = ta.getBoolean(R.styleable.SegmentTabLayout_tl_indicator_anim_enable, false)
        mIndicatorBounceEnable = ta.getBoolean(R.styleable.SegmentTabLayout_tl_indicator_bounce_enable, true)
        mIndicatorAnimDuration = ta.getInt(R.styleable.SegmentTabLayout_tl_indicator_anim_duration, -1).toLong()

        mDividerColor = ta.getColor(R.styleable.SegmentTabLayout_tl_divider_color, mIndicatorColor)
        mDividerWidth = ta.getDimension(R.styleable.SegmentTabLayout_tl_divider_width, 1f.dp)
        mDividerPadding = ta.getDimension(R.styleable.SegmentTabLayout_tl_divider_padding, 0f)

        mTextSize = ta.getDimension(R.styleable.SegmentTabLayout_tl_textsize, 13f.sp)
        mTextSelectColor = ta.getColor(R.styleable.SegmentTabLayout_tl_textSelectColor, Color.parseColor("#ffffff"))
        mTextUnselectColor = ta.getColor(R.styleable.SegmentTabLayout_tl_textUnselectColor, mIndicatorColor)
        mTextBold = ta.getInt(R.styleable.SegmentTabLayout_tl_textBold, TEXT_BOLD_NONE)
        mTextAllCaps = ta.getBoolean(R.styleable.SegmentTabLayout_tl_textAllCaps, false)

        mTabSpaceEqual = ta.getBoolean(R.styleable.SegmentTabLayout_tl_tab_space_equal, true)
        mTabWidth = ta.getDimension(R.styleable.SegmentTabLayout_tl_tab_width, (-1f).dp)
        mTabPadding = ta.getDimension(R.styleable.SegmentTabLayout_tl_tab_padding, if (mTabSpaceEqual || mTabWidth > 0) 0f.dp else 10f.dp)

        mBarColor = ta.getColor(R.styleable.SegmentTabLayout_tl_bar_color, Color.TRANSPARENT)
        mBarStrokeColor = ta.getColor(R.styleable.SegmentTabLayout_tl_bar_stroke_color, mIndicatorColor)
        mBarStrokeWidth = ta.getDimension(R.styleable.SegmentTabLayout_tl_bar_stroke_width, 1f.dp)

        ta.recycle()
    }

    fun setTabData(titles: List<String>) {
        titles.takeIf { it.isNotEmpty() }?.run {
            mTitles = titles
            notifyDataSetChanged()
        }
    }

    fun setTabData(titles: List<String>, fa: FragmentActivity, containerViewId: Int, fragments: List<Fragment>) {
        mFragmentChangeManager = FragmentChangeManager(fa.supportFragmentManager, containerViewId, fragments)
        setTabData(titles)
    }

    fun notifyDataSetChanged() {
        mTabsContainer.removeAllViews()
        this.mTabCount = mTitles.size
        var tabView: View
        for (i in 0 until mTabCount) {
            tabView = inflate(context, R.layout.layout_tab_segment, null)
            tabView.tag = i
            addTab(i, tabView)
        }
        updateTabStyles()
    }

    private fun addTab(position: Int, tabView: View) {
        val tvTabTitle = tabView.findViewById<TextView>(R.id.tv_tab_title)
        tvTabTitle.text = mTitles[position]
        tabView.setOnClickListener {
            val p = it.tag as Int
            if (mCurrentTab != p) {
                setCurrentTab(p)
                mListener?.onTabSelect(p)
            } else {
                mListener?.onTabReselect(p)
            }
        }

        /** 每一个Tab的布局参数  */
        var lpTab = if (mTabSpaceEqual) {
            LinearLayout.LayoutParams(0, LayoutParams.MATCH_PARENT, 1.0f)
        } else {
            LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT)
        }
        if (mTabWidth > 0) {
            lpTab = LinearLayout.LayoutParams(mTabWidth.toInt(), LayoutParams.MATCH_PARENT)
        }
        mTabsContainer.addView(tabView, position, lpTab)
    }

    private fun updateTabStyles() {
        for (i in 0 until mTabCount) {
            val tabView = mTabsContainer.getChildAt(i)
            tabView.setPadding(mTabPadding.toInt(), 0, mTabPadding.toInt(), 0)
            val tvTabTitle = tabView.findViewById<View>(R.id.tv_tab_title) as TextView
            tvTabTitle.setTextColor(if (i == mCurrentTab) mTextSelectColor else mTextUnselectColor)
            tvTabTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTextSize)
            if (mTextAllCaps) {
                tvTabTitle.text = tvTabTitle.text.toString().uppercase(Locale.getDefault())
            }
            if (mTextBold == TEXT_BOLD_BOTH) {
                tvTabTitle.paint.isFakeBoldText = true
            } else if (mTextBold == TEXT_BOLD_NONE) {
                tvTabTitle.paint.isFakeBoldText = false
            }
        }
    }

    private fun updateTabSelection(position: Int) {
        for (i in 0 until mTabCount) {
            val tabView = mTabsContainer.getChildAt(i)
            val isSelect = i == position
            val tabTitle = tabView.findViewById<TextView>(R.id.tv_tab_title)
            tabTitle.setTextColor(if (isSelect) mTextSelectColor else mTextUnselectColor)
            if (mTextBold == TEXT_BOLD_WHEN_SELECT) {
                tabTitle.paint.isFakeBoldText = isSelect
            }
        }
    }

    private fun calcOffset() {
        val currentTabView = mTabsContainer.getChildAt(mCurrentTab)
        mCurrentP.left = currentTabView.left.toFloat()
        mCurrentP.right = currentTabView.right.toFloat()
        val lastTabView = mTabsContainer.getChildAt(mLastTab)
        mLastP.left = lastTabView.left.toFloat()
        mLastP.right = lastTabView.right.toFloat()
        if (mLastP.left == mCurrentP.left && mLastP.right == mCurrentP.right) {
            invalidate()
        } else {
            mValueAnimator.setObjectValues(mLastP, mCurrentP)
            if (mIndicatorBounceEnable) {
                mValueAnimator.interpolator = mInterpolator
            }
            if (mIndicatorAnimDuration < 0) {
                mIndicatorAnimDuration = if (mIndicatorBounceEnable) 500 else 250.toLong()
            }
            mValueAnimator.duration = mIndicatorAnimDuration
            mValueAnimator.start()
        }
    }

    private fun calcIndicatorRect() {
        val currentTabView = mTabsContainer.getChildAt(mCurrentTab)
        val left = currentTabView.left.toFloat()
        val right = currentTabView.right.toFloat()
        mIndicatorRect.left = left.toInt()
        mIndicatorRect.right = right.toInt()
        if (!mIndicatorAnimEnable) {
            when (mCurrentTab) {
                0 -> {
                    /*The corners are ordered top-left, top-right, bottom-right, bottom-left*/
                    mRadiusArr[0] = mIndicatorCornerRadius
                    mRadiusArr[1] = mIndicatorCornerRadius
                    mRadiusArr[2] = 0F
                    mRadiusArr[3] = 0F
                    mRadiusArr[4] = 0F
                    mRadiusArr[5] = 0F
                    mRadiusArr[6] = mIndicatorCornerRadius
                    mRadiusArr[7] = mIndicatorCornerRadius
                }
                mTabCount - 1 -> {
                    /*The corners are ordered top-left, top-right, bottom-right, bottom-left*/
                    mRadiusArr[0] = 0F
                    mRadiusArr[1] = 0F
                    mRadiusArr[2] = mIndicatorCornerRadius
                    mRadiusArr[3] = mIndicatorCornerRadius
                    mRadiusArr[4] = mIndicatorCornerRadius
                    mRadiusArr[5] = mIndicatorCornerRadius
                    mRadiusArr[6] = 0F
                    mRadiusArr[7] = 0F
                }
                else -> {
                    /*The corners are ordered top-left, top-right, bottom-right, bottom-left*/
                    mRadiusArr[0] = 0F
                    mRadiusArr[1] = 0F
                    mRadiusArr[2] = 0F
                    mRadiusArr[3] = 0F
                    mRadiusArr[4] = 0F
                    mRadiusArr[5] = 0F
                    mRadiusArr[6] = 0F
                    mRadiusArr[7] = 0F
                }
            }
        } else {
            /*The corners are ordered top-left, top-right, bottom-right, bottom-left*/
            mRadiusArr[0] = mIndicatorCornerRadius
            mRadiusArr[1] = mIndicatorCornerRadius
            mRadiusArr[2] = mIndicatorCornerRadius
            mRadiusArr[3] = mIndicatorCornerRadius
            mRadiusArr[4] = mIndicatorCornerRadius
            mRadiusArr[5] = mIndicatorCornerRadius
            mRadiusArr[6] = mIndicatorCornerRadius
            mRadiusArr[7] = mIndicatorCornerRadius
        }
    }

    override fun onAnimationUpdate(p0: ValueAnimator) {
        val p = p0.animatedValue as IndicatorPoint
        mIndicatorRect.left = p.left.toInt()
        mIndicatorRect.right = p.right.toInt()
        invalidate()
    }

    private var mIsFirstDraw = true

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (!isInEditMode && mTabCount > 0) {
            val height = height
            val paddingLeft = paddingLeft

            if (mIndicatorHeight < 0) {
                mIndicatorHeight = height - mIndicatorMarginTop - mIndicatorMarginBottom
            }

            if (mIndicatorCornerRadius < 0 || mIndicatorCornerRadius > mIndicatorHeight / 2) {
                mIndicatorCornerRadius = mIndicatorHeight / 2
            }

            //draw rect
            mRectDrawable.setColor(mBarColor)
            mRectDrawable.setStroke(mBarStrokeWidth.toInt(), mBarStrokeColor)
            mRectDrawable.cornerRadius = mIndicatorCornerRadius
            mRectDrawable.setBounds(getPaddingLeft(), paddingTop, width - paddingRight, getHeight() - paddingBottom)
            mRectDrawable.draw(canvas)

            // draw divider
            if (!mIndicatorAnimEnable && mDividerWidth > 0) {
                mDividerPaint.strokeWidth = mDividerWidth
                mDividerPaint.color = mDividerColor
                for (i in 0 until mTabCount - 1) {
                    val tab = mTabsContainer.getChildAt(i)
                    canvas.drawLine((paddingLeft + tab.right).toFloat(), mDividerPadding, (paddingLeft + tab.right).toFloat(), height - mDividerPadding, mDividerPaint)
                }
            }

            //draw indicator line
            if (mIndicatorAnimEnable) {
                if (mIsFirstDraw) {
                    mIsFirstDraw = false
                    calcIndicatorRect()
                }
            } else {
                calcIndicatorRect()
            }

            mIndicatorDrawable.setColor(mIndicatorColor)
            mIndicatorDrawable.setBounds(
                paddingLeft + mIndicatorMarginLeft.toInt() + mIndicatorRect.left,
                mIndicatorMarginTop.toInt(),
                (paddingLeft + mIndicatorRect.right - mIndicatorMarginRight).toInt(),
                (mIndicatorMarginTop + mIndicatorHeight).toInt()
            )
            mIndicatorDrawable.cornerRadii = mRadiusArr
            mIndicatorDrawable.draw(canvas)
        }
    }


    fun setCurrentTab(currentTab: Int) {
        mLastTab = mCurrentTab
        mCurrentTab = currentTab
        updateTabSelection(currentTab)
        if (mFragmentChangeManager != null) {
            mFragmentChangeManager!!.setFragments(currentTab)
        }
        if (mIndicatorAnimEnable) {
            calcOffset()
        } else {
            invalidate()
        }
    }

    fun setTabPadding(tabPadding: Float) {
        this.mTabPadding = tabPadding.dp
        updateTabStyles()
    }

    fun setTabSpaceEqual(tabSpaceEqual: Boolean) {
        mTabSpaceEqual = tabSpaceEqual
        updateTabStyles()
    }

    fun setTabWidth(tabWidth: Float) {
        mTabWidth = tabWidth.dp
        updateTabStyles()
    }

    fun setIndicatorColor(indicatorColor: Int) {
        mIndicatorColor = indicatorColor
        invalidate()
    }

    fun setIndicatorHeight(indicatorHeight: Float) {
        mIndicatorHeight = indicatorHeight.dp
        invalidate()
    }

    fun setIndicatorCornerRadius(indicatorCornerRadius: Float) {
        mIndicatorCornerRadius = indicatorCornerRadius.dp
        invalidate()
    }

    fun setIndicatorMargin(indicatorMarginLeft: Float, indicatorMarginTop: Float, indicatorMarginRight: Float, indicatorMarginBottom: Float) {
        mIndicatorMarginLeft = indicatorMarginLeft.dp
        mIndicatorMarginTop = indicatorMarginTop.dp
        mIndicatorMarginRight = indicatorMarginRight.dp
        mIndicatorMarginBottom = indicatorMarginBottom.dp
        invalidate()
    }

    fun setDividerColor(dividerColor: Int) {
        mDividerColor = dividerColor
        invalidate()
    }

    fun setDividerWidth(dividerWidth: Float) {
        mDividerWidth = dividerWidth.dp
        invalidate()
    }

    fun setDividerPadding(dividerPadding: Float) {
        mDividerPadding = dividerPadding.dp
        invalidate()
    }

    fun setTextSize(textSize: Float) {
        mTextSize = textSize.sp
        updateTabStyles()
    }

    fun setTextSelectColor(textSelectColor: Int) {
        mTextSelectColor = textSelectColor
        updateTabStyles()
    }

    fun setTextUnselectColor(textUnselectColor: Int) {
        mTextUnselectColor = textUnselectColor
        updateTabStyles()
    }

    fun setTextBold(textBold: Int) {
        mTextBold = textBold
        updateTabStyles()
    }

    fun setTextAllCaps(textAllCaps: Boolean) {
        mTextAllCaps = textAllCaps
        updateTabStyles()
    }

    fun getTitleView(tab: Int): TextView {
        val tabView = mTabsContainer.getChildAt(tab)
        return tabView.findViewById(R.id.tv_tab_title)
    }

    private val mTextPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val mInitSetMap = SparseBooleanArray()

    /**
     * 显示未读消息
     *
     * @param position 显示tab位置
     * @param num      num小于等于0显示红点,num大于0显示数字
     */
    fun showMsg(position: Int, num: Int) {
        var mPosition = position
        if (mPosition >= mTabCount) {
            mPosition = mTabCount - 1
        }
        val tabView = mTabsContainer.getChildAt(mPosition)
        val tipView = tabView.findViewById<MsgView>(R.id.rtv_msg_tip)
        if (tipView != null) {
            UnreadMsgUtils.show(tipView, num)
            if (mInitSetMap[mPosition]) {
                return
            }
            setMsgMargin(mPosition)
            mInitSetMap.put(mPosition, true)
        }
    }

    /**
     * 显示未读红点
     *
     * @param position 显示tab位置
     */
    fun showDot(position: Int) {
        var mPosition = position
        if (mPosition >= mTabCount) {
            mPosition = mTabCount - 1
        }
        showMsg(mPosition, 0)
    }

    fun hideMsg(position: Int) {
        var mPosition = position
        if (mPosition >= mTabCount) {
            mPosition = mTabCount - 1
        }
        val tabView = mTabsContainer.getChildAt(mPosition)
        val tipView = tabView.findViewById<MsgView>(R.id.rtv_msg_tip)
        tipView.gone()
    }

    /**
     * 设置提示红点偏移,注意
     * 1.控件为固定高度:参照点为tab内容的右上角
     * 2.控件高度不固定(WRAP_CONTENT):参照点为tab内容的右上角,此时高度已是红点的最高显示范围,所以这时bottomPadding其实就是topPadding
     */
    private fun setMsgMargin(position: Int) {
        var mPosition = position
        if (mPosition >= mTabCount) {
            mPosition = mTabCount - 1
        }
        val tabView = mTabsContainer.getChildAt(mPosition)
        val tipView = tabView.findViewById<MsgView>(R.id.rtv_msg_tip)
        val tvTabTitle = tabView.findViewById<TextView>(R.id.tv_tab_title)
        mTextPaint.textSize = mTextSize
        mTextPaint.measureText(tvTabTitle.text.toString())
        val textHeight = mTextPaint.descent() - mTextPaint.ascent()
        val lp = tipView.layoutParams as MarginLayoutParams
        lp.leftMargin = 2.dp
        lp.topMargin = if (mHeight > 0) (mHeight - textHeight).toInt() / 2 - 2.dp else 2.dp
        tipView.layoutParams = lp
    }

    /**
     * 当前类只提供了少许设置未读消息属性的方法,可以通过该方法获取MsgView对象从而各种设置
     */
    fun getMsgView(position: Int): MsgView {
        var mPosition = position
        if (mPosition >= mTabCount) {
            mPosition = mTabCount - 1
        }
        val tabView = mTabsContainer.getChildAt(mPosition)
        return tabView.findViewById(R.id.rtv_msg_tip)
    }

    private var mListener: OnTabSelectListener? = null

    fun setOnTabSelectListener(listener: OnTabSelectListener) {
        mListener = listener
    }

    override fun onSaveInstanceState(): Parcelable {
        val bundle = Bundle()
        bundle.putParcelable("instanceState", super.onSaveInstanceState())
        bundle.putInt("mCurrentTab", mCurrentTab)
        return bundle
    }

    override fun onRestoreInstanceState(state: Parcelable?) {
        var mState = state
        if (mState is Bundle) {
            val bundle = mState
            mCurrentTab = bundle.getInt("mCurrentTab")
            mState = bundle.getParcelable("instanceState")
            if (mCurrentTab != 0 && mTabsContainer.childCount > 0) {
                updateTabSelection(mCurrentTab)
            }
        }
        super.onRestoreInstanceState(mState)
    }

    internal class IndicatorPoint {
        var left = 0f
        var right = 0f
    }

    internal class PointEvaluator : TypeEvaluator<IndicatorPoint> {
        override fun evaluate(fraction: Float, startValue: IndicatorPoint, endValue: IndicatorPoint): IndicatorPoint {
            val left: Float = startValue.left + fraction * (endValue.left - startValue.left)
            val right: Float = startValue.right + fraction * (endValue.right - startValue.right)
            val point = IndicatorPoint()
            point.left = left
            point.right = right
            return point
        }
    }
}