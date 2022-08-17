package cn.chawloo.base.widget.tablayout

import android.animation.TypeEvaluator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.animation.OvershootInterpolator
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import cn.chawloo.base.R
import cn.chawloo.base.ext.dp
import cn.chawloo.base.ext.gone
import cn.chawloo.base.ext.sp
import cn.chawloo.base.ext.visible
import cn.chawloo.base.widget.tablayout.listener.CustomTabEntity
import cn.chawloo.base.widget.tablayout.listener.OnTabSelectListener
import cn.chawloo.base.widget.tablayout.utils.FragmentChangeManager

/**
 * 自定义TabLayout
 * @author Create by 鲁超 on 2020/12/28 0028 8:59
 */
class CommonTabLayout @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyle: Int = 0) : FrameLayout(context, attrs, defStyle),
    ValueAnimator.AnimatorUpdateListener {
    companion object {

        private const val STYLE_NORMAL = 0
        private const val STYLE_TRIANGLE = 1
        private const val STYLE_BLOCK = 2

        private const val TEXT_BOLD_NONE = 0
        private const val TEXT_BOLD_WHEN_SELECT = 1
        private const val TEXT_BOLD_BOTH = 2
    }

    private var mContext: Context
    private var mTabEntities = ArrayList<CustomTabEntity>()
    private var mTabsContainer: LinearLayout
    var mCurrentTab = 0
        set(value) {
            mLastTab = field
            field = value
            updateTabSelection(value)
            mFragmentChangeManager?.setFragments(value)
            if (mIndicatorAnimEnable) {
                calcOffset()
            } else {
                invalidate()
            }
        }
    private var mLastTab = 0
    private var mTabCount = 0

    /**
     * 用于绘制显示器
     */
    private val mIndicatorRect = Rect()
    private val mIndicatorDrawable = GradientDrawable()

    private val mRectPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val mDividerPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val mTrianglePaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val mTrianglePath = Path()
    private var mIndicatorStyle = STYLE_NORMAL


    private var mTabPadding = 0f
    private var mTabSpaceEqual = false
    private var mTabWidth = 0f

    /**
     * indicator
     */
    private var mIndicatorColor = 0
    private var mIndicatorHeight = 0f
    private var mIndicatorWidth = 0f
    private var mIndicatorCornerRadius = 0f
    private var mIndicatorMarginLeft = 0f
    private var mIndicatorMarginTop = 0f
    private var mIndicatorMarginRight = 0f
    private var mIndicatorMarginBottom = 0f
    private var mIndicatorAnimDuration: Long = 0
    private var mIndicatorAnimEnable = false
    private var mIndicatorBounceEnable = false
    private var mIndicatorGravity = 0

    /**
     * underline
     */
    private var mUnderlineColor = 0
    private var mUnderlineHeight = 0f
    private var mUnderlineGravity = 0

    /**
     * divider
     */
    private var mDividerColor = 0
    private var mDividerWidth = 0f
    private var mDividerPadding = 0f

    /**
     * title
     */
    private var mTextSize = 0f
    private var mTextSelectColor = 0
    private var mTextUnSelectColor = 0
    private var mTextBold = 0
    private var mTextAllCaps = false

    /**
     * icon
     */
    private var mIconVisible = false
    private var mIconGravity = 0
    private var mIconWidth = 0f
    private var mIconHeight = 0f
    private var mIconMargin = 0f

    private var mHeight = 0

    /**
     * anim
     */
    private var mValueAnimator: ValueAnimator
    private val mInterpolator = OvershootInterpolator(1.5f)

    private val mCurrentP = IndicatorPoint()
    private val mLastP = IndicatorPoint()

    private var mFragmentChangeManager: FragmentChangeManager? = null
    var mListener: OnTabSelectListener? = null

    init {
        setWillNotDraw(false)//重写onDraw方法，需要调用这个方法清除Flag
        clipChildren = false
        clipToPadding = false
        mContext = context
        mTabsContainer = LinearLayout(context)
        addView(mTabsContainer)
        obtainAttributes(context, attrs)

        val height = attrs?.getAttributeValue("http://schemas.android.com/apk/res/android", "layout_height")
        if (height != ViewGroup.LayoutParams.MATCH_PARENT.toString() && height != ViewGroup.LayoutParams.WRAP_CONTENT.toString()) {
            val systemAttrs = intArrayOf(android.R.attr.layout_height)
            val a = context.obtainStyledAttributes(attrs, systemAttrs)
            mHeight = a.getDimensionPixelSize(0, ViewGroup.LayoutParams.WRAP_CONTENT)
            a.recycle()
        }

        mValueAnimator = ValueAnimator.ofObject(PointEvaluator(), mLastP, mCurrentP)
        mValueAnimator.addUpdateListener(this)
    }

    private fun obtainAttributes(context: Context, attrs: AttributeSet?) {
        val ta = context.obtainStyledAttributes(attrs, R.styleable.CommonTabLayout)
        ta.run {
            mIndicatorStyle = getInt(R.styleable.CommonTabLayout_tl_indicator_style, 0)
            mIndicatorColor = getColor(R.styleable.CommonTabLayout_tl_indicator_color, Color.parseColor(if (mIndicatorStyle == STYLE_BLOCK) "#4B6A87" else "#ffffff"))
            mIndicatorHeight = getDimension(
                R.styleable.CommonTabLayout_tl_indicator_height, (if (mIndicatorStyle == STYLE_TRIANGLE) 4F else if (mIndicatorStyle == STYLE_BLOCK) -1F else 2F).dp
            )
            mIndicatorWidth = getDimension(R.styleable.CommonTabLayout_tl_indicator_width, (if (mIndicatorStyle == STYLE_TRIANGLE) 10F else -1F).dp)
            mIndicatorCornerRadius = getDimension(R.styleable.CommonTabLayout_tl_indicator_corner_radius, if (mIndicatorStyle == STYLE_BLOCK) -1F else 0F.dp)
            mIndicatorMarginLeft = getDimension(R.styleable.CommonTabLayout_tl_indicator_margin_left, 0F.dp)
            mIndicatorMarginTop = getDimension(R.styleable.CommonTabLayout_tl_indicator_margin_top, (if (mIndicatorStyle == STYLE_BLOCK) 7F else 0F).dp)
            mIndicatorMarginRight = getDimension(R.styleable.CommonTabLayout_tl_indicator_margin_right, 0F.dp)
            mIndicatorMarginBottom = getDimension(R.styleable.CommonTabLayout_tl_indicator_margin_bottom, (if (mIndicatorStyle == STYLE_BLOCK) 7F else 0F).dp)
            mIndicatorAnimEnable = getBoolean(R.styleable.CommonTabLayout_tl_indicator_anim_enable, true)
            mIndicatorBounceEnable = getBoolean(R.styleable.CommonTabLayout_tl_indicator_bounce_enable, true)
            mIndicatorAnimDuration = getInt(R.styleable.CommonTabLayout_tl_indicator_anim_duration, -1).toLong()
            mIndicatorGravity = getInt(R.styleable.CommonTabLayout_tl_indicator_gravity, Gravity.BOTTOM)
            mUnderlineColor = getColor(R.styleable.CommonTabLayout_tl_underline_color, Color.parseColor("#ffffff"))
            mUnderlineHeight = getDimension(R.styleable.CommonTabLayout_tl_underline_height, 0F.dp)
            mUnderlineGravity = getInt(R.styleable.CommonTabLayout_tl_underline_gravity, Gravity.BOTTOM)
            mDividerColor = getColor(R.styleable.CommonTabLayout_tl_divider_color, Color.parseColor("#ffffff"))
            mDividerWidth = getDimension(R.styleable.CommonTabLayout_tl_divider_width, 0F.dp)
            mDividerPadding = getDimension(R.styleable.CommonTabLayout_tl_divider_padding, 12F.dp)
            mTextSize = getDimension(R.styleable.CommonTabLayout_tl_textsize, 13F.sp)
            mTextSelectColor = getColor(R.styleable.CommonTabLayout_tl_textSelectColor, Color.parseColor("#ffffff"))
            mTextUnSelectColor = getColor(R.styleable.CommonTabLayout_tl_textUnselectColor, Color.parseColor("#AAffffff"))
            mTextBold = getInt(R.styleable.CommonTabLayout_tl_textBold, TEXT_BOLD_NONE)
            mTextAllCaps = getBoolean(R.styleable.CommonTabLayout_tl_textAllCaps, false)
            mIconVisible = getBoolean(R.styleable.CommonTabLayout_tl_iconVisible, true)
            mIconGravity = getInt(R.styleable.CommonTabLayout_tl_iconGravity, Gravity.TOP)
            mIconWidth = getDimension(R.styleable.CommonTabLayout_tl_iconWidth, 0F.dp)
            mIconHeight = getDimension(R.styleable.CommonTabLayout_tl_iconHeight, 0F.dp)
            mIconMargin = getDimension(R.styleable.CommonTabLayout_tl_iconMargin, 2.5f.dp)
            mTabSpaceEqual = getBoolean(R.styleable.CommonTabLayout_tl_tab_space_equal, true)
            mTabWidth = getDimension(R.styleable.CommonTabLayout_tl_tab_width, (-1F).dp)
            mTabPadding = getDimension(R.styleable.CommonTabLayout_tl_tab_padding, (if (mTabSpaceEqual || mTabWidth > 0) 0 else 10).toFloat().dp)
        }
        ta.recycle()
    }

    fun setTabData(tabEntities: ArrayList<CustomTabEntity>?) {
        if (tabEntities.isNullOrEmpty()) {
            throw IllegalStateException("TabEntities can not be NULL or EMPTY !")
        }
        this.mTabEntities.clear()
        mTabEntities.addAll(tabEntities)
        notifyDataSetChanged()
    }

    /**
     * 关联数据支持同时切换fragments
     */
    fun setTabData(tabEntities: ArrayList<CustomTabEntity>?, fa: FragmentActivity, containerViewId: Int, fragments: List<Fragment>) {
        mFragmentChangeManager = FragmentChangeManager(fa.supportFragmentManager, containerViewId, fragments)
        setTabData(tabEntities)
    }

    /**
     * 更新数据
     */
    private fun notifyDataSetChanged() {
        mTabsContainer.removeAllViews()
        mTabCount = mTabEntities.size
        var tabView: View
        for (i in 0 until mTabCount) {
            tabView = when (mIconGravity) {
                Gravity.START -> {
                    inflate(mContext, R.layout.layout_tab_left, null)
                }
                Gravity.END -> {
                    inflate(mContext, R.layout.layout_tab_right, null)
                }
                Gravity.BOTTOM -> {
                    inflate(mContext, R.layout.layout_tab_bottom, null)
                }
                else -> {
                    inflate(mContext, R.layout.layout_tab_top, null)
                }
            }
            tabView.tag = i
            addTab(i, tabView)
        }

        updateTabStyles()
    }

    /**
     * 创建并添加tab
     */
    private fun addTab(position: Int, tabView: View) {
        val tvTabTitle = tabView.findViewById<TextView>(R.id.tv_tab_title)
        tvTabTitle.text = mTabEntities[position].tabTitle
        val ivTabIcon = tabView.findViewById<AppCompatImageView>(R.id.iv_tab_icon)
        ivTabIcon.setImageResource(mTabEntities[position].tabUnselectedIcon)

        tabView.setOnClickListener {
            val clickPosition = it.tag as Int
            if (mCurrentTab != clickPosition) {
                mCurrentTab = clickPosition
                mListener?.onTabSelect(clickPosition)
            } else {
                mListener?.onTabReselect(clickPosition)
            }
        }
        var lp = if (mTabSpaceEqual) LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1.0f)
        else LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT)
        if (mTabWidth > 0) {
            lp = LinearLayout.LayoutParams(mTabWidth.toInt(), LinearLayout.LayoutParams.MATCH_PARENT)
        }
        mTabsContainer.addView(tabView, position, lp)
    }

    private fun updateTabStyles() {
        for (i in 0 until mTabCount) {
            val tabView = mTabsContainer.getChildAt(i)
            tabView.setPadding(mTabPadding.toInt(), 0, mTabPadding.toInt(), 0)
            val tvTabTitle = tabView.findViewById<TextView>(R.id.tv_tab_title)
            tvTabTitle.setTextColor(if (i == mCurrentTab) mTextSelectColor else mTextUnSelectColor)
            tvTabTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTextSize)
            if (mTextAllCaps) {
                tvTabTitle.text = tvTabTitle.text.toString().uppercase()
            }
            if (mTextBold == TEXT_BOLD_BOTH) {
                tvTabTitle.paint.isFakeBoldText = true
            } else if (mTextBold == TEXT_BOLD_NONE) {
                tvTabTitle.paint.isFakeBoldText = false
            }

            val ivTabIcon = tabView.findViewById<AppCompatImageView>(R.id.iv_tab_icon)
            if (mIconVisible) {
                ivTabIcon.visible()
                val tabEntity = mTabEntities[i]
                ivTabIcon.setImageResource(if (i == mCurrentTab) tabEntity.tabSelectedIcon else tabEntity.tabUnselectedIcon)
                val lp = LinearLayout.LayoutParams(
                    if (mIconWidth <= 0) LinearLayout.LayoutParams.WRAP_CONTENT else mIconWidth.toInt(),
                    if (mIconHeight <= 0) LinearLayout.LayoutParams.WRAP_CONTENT else mIconHeight.toInt()
                )
                when (mIconGravity) {
                    Gravity.START -> {
                        lp.rightMargin = mIconMargin.toInt()
                    }
                    Gravity.END -> {
                        lp.leftMargin = mIconMargin.toInt()
                    }
                    Gravity.BOTTOM -> {
                        lp.topMargin = mIconMargin.toInt()
                    }
                    else -> {
                        lp.bottomMargin = mIconMargin.toInt()
                    }
                }
                ivTabIcon.layoutParams = lp
            } else {
                ivTabIcon.gone()
            }
        }
    }

    private fun updateTabSelection(position: Int) {
        for (i in 0 until mTabCount) {
            val tabView = mTabsContainer.getChildAt(i)
            val isSelect = i == position
            val tabTitle = tabView.findViewById<TextView>(R.id.tv_tab_title)
            tabTitle.setTextColor(if (isSelect) mTextSelectColor else mTextUnSelectColor)
            val ivTabIcon = tabView.findViewById<AppCompatImageView>(R.id.iv_tab_icon)
            val tabEntity = mTabEntities[i]
            ivTabIcon.setImageResource(if (isSelect) tabEntity.tabSelectedIcon else tabEntity.tabUnselectedIcon)
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
        if (mIndicatorWidth < 0) {   //indicatorWidth小于0时,原 PagerSlidingTabStrip
        } else { //indicatorWidth大于0时,圆角矩形以及三角形
            val indicatorLeft = currentTabView.left + (currentTabView.width - mIndicatorWidth) / 2
            mIndicatorRect.left = indicatorLeft.toInt()
            mIndicatorRect.right = (mIndicatorRect.left + mIndicatorWidth).toInt()
        }
    }

    override fun onAnimationUpdate(animation: ValueAnimator?) {
        val currentTabView = mTabsContainer.getChildAt(mCurrentTab)
        val (left, right) = animation!!.animatedValue as IndicatorPoint
        mIndicatorRect.left = left.toInt()
        mIndicatorRect.right = right.toInt()

        if (mIndicatorWidth < 0) {   //indicatorWidth小于0时,原jpardogo's PagerSlidingTabStrip
        } else { //indicatorWidth大于0时,圆角矩形以及三角形
            val indicatorLeft = left + (currentTabView.width - mIndicatorWidth) / 2
            mIndicatorRect.left = indicatorLeft.toInt()
            mIndicatorRect.right = (mIndicatorRect.left + mIndicatorWidth).toInt()
        }
        invalidate()
    }

    private var mIsFirstDraw = true

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        if (isInEditMode || mTabCount <= 0) {
            return
        }
        val height = height
        val paddingLeft = paddingLeft
        // draw divider
        if (mDividerWidth > 0) {
            mDividerPaint.strokeWidth = mDividerWidth
            mDividerPaint.color = mDividerColor
            for (i in 0 until mTabCount - 1) {
                val tab = mTabsContainer.getChildAt(i)
                canvas!!.drawLine(
                    (paddingLeft + tab.right).toFloat(), mDividerPadding, (paddingLeft + tab.right).toFloat(), height - mDividerPadding, mDividerPaint
                )
            }
        }

        if (mUnderlineHeight > 0) {
            mRectPaint.color = mUnderlineColor
            if (mUnderlineGravity == Gravity.BOTTOM) {
                canvas!!.drawRect(
                    paddingLeft.toFloat(), height - mUnderlineHeight, (mTabsContainer.width + paddingLeft).toFloat(), height.toFloat(), mRectPaint
                )
            } else {
                canvas!!.drawRect(paddingLeft.toFloat(), 0f, (mTabsContainer.width + paddingLeft).toFloat(), mUnderlineHeight, mRectPaint)
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

        if (mIndicatorStyle == STYLE_TRIANGLE) {
            if (mIndicatorHeight > 0) {
                mTrianglePaint.color = mIndicatorColor
                mTrianglePath.reset()
                mTrianglePath.moveTo((paddingLeft + mIndicatorRect.left).toFloat(), height.toFloat())
                mTrianglePath.lineTo(paddingLeft + mIndicatorRect.left / 2f + mIndicatorRect.right / 2f, height - mIndicatorHeight)
                mTrianglePath.lineTo((paddingLeft + mIndicatorRect.right).toFloat(), height.toFloat())
                mTrianglePath.close()
                canvas!!.drawPath(mTrianglePath, mTrianglePaint)
            }
        } else if (mIndicatorStyle == STYLE_BLOCK) {
            if (mIndicatorHeight < 0) {
                mIndicatorHeight = height - mIndicatorMarginTop - mIndicatorMarginBottom
            }
            if (mIndicatorHeight > 0) {
                if (mIndicatorCornerRadius < 0 || mIndicatorCornerRadius > mIndicatorHeight / 2) {
                    mIndicatorCornerRadius = mIndicatorHeight / 2
                }
                mIndicatorDrawable.setColor(mIndicatorColor)
                mIndicatorDrawable.setBounds(
                    paddingLeft + mIndicatorMarginLeft.toInt() + mIndicatorRect.left,
                    mIndicatorMarginTop.toInt(), (paddingLeft + mIndicatorRect.right - mIndicatorMarginRight).toInt(),
                    (mIndicatorMarginTop + mIndicatorHeight).toInt()
                )
                mIndicatorDrawable.cornerRadius = mIndicatorCornerRadius
                mIndicatorDrawable.draw(canvas!!)
            }
        } else {
            if (mIndicatorHeight > 0) {
                mIndicatorDrawable.setColor(mIndicatorColor)
                if (mIndicatorGravity == Gravity.BOTTOM) {
                    mIndicatorDrawable.setBounds(
                        paddingLeft + mIndicatorMarginLeft.toInt() + mIndicatorRect.left,
                        height - mIndicatorHeight.toInt() - mIndicatorMarginBottom.toInt(),
                        paddingLeft + mIndicatorRect.right - mIndicatorMarginRight.toInt(),
                        height - mIndicatorMarginBottom.toInt()
                    )
                } else {
                    mIndicatorDrawable.setBounds(
                        paddingLeft + mIndicatorMarginLeft.toInt() + mIndicatorRect.left,
                        mIndicatorMarginTop.toInt(),
                        paddingLeft + mIndicatorRect.right - mIndicatorMarginRight.toInt(),
                        mIndicatorHeight.toInt() + mIndicatorMarginTop.toInt()
                    )
                }
                mIndicatorDrawable.cornerRadius = mIndicatorCornerRadius
                mIndicatorDrawable.draw(canvas!!)
            }
        }
    }

    data class IndicatorPoint(var left: Float = 0f, var right: Float = 0f)

    class PointEvaluator : TypeEvaluator<IndicatorPoint> {
        override fun evaluate(fraction: Float, startValue: IndicatorPoint, endValue: IndicatorPoint): IndicatorPoint {
            val left = startValue.left + fraction * (endValue.left - startValue.left)
            val right = startValue.right + fraction * (endValue.right - startValue.right)
            return IndicatorPoint(left, right)
        }

    }
}