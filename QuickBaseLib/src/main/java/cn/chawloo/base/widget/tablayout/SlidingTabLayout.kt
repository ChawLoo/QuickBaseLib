package cn.chawloo.base.widget.tablayout

import android.content.Context
import android.graphics.*
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import android.util.SparseBooleanArray
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.HorizontalScrollView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import cn.chawloo.base.R
import cn.chawloo.base.ext.doClick
import cn.chawloo.base.ext.dp
import cn.chawloo.base.ext.gone
import cn.chawloo.base.ext.sp
import cn.chawloo.base.widget.tablayout.listener.OnTabSelectListener
import cn.chawloo.base.widget.tablayout.utils.UnreadMsgUtils
import java.util.*

/**
 * 滑动标签布局
 *
 * @author Create by 鲁超 on 2020/8/19 0019 17:12
 */
class SlidingTabLayout @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyle: Int = 0) : HorizontalScrollView(context, attrs, defStyle) {
    companion object {
        private const val STYLE_NORMAL = 0
        private const val STYLE_TRIANGLE = 1
        private const val STYLE_BLOCK = 2

        private const val TEXT_BOLD_NONE = 0
        private const val TEXT_BOLD_WHEN_SELECT = 1
        private const val TEXT_BOLD_BOTH = 2
    }

    private var mContext: Context
    private lateinit var mViewPager: ViewPager2
    private var mTitles: ArrayList<String>? = null
    private val mTabsContainer: LinearLayout
    private var mCurrentTab = 0
    private var mCurrentPositionOffset = 0f
    private var mTabCount = 0

    /**
     * 用于绘制显示器
     */
    private val mIndicatorRect = Rect()

    /**`
     * 用于实现滚动居中
     */
    private val mTabRect = Rect()
    private val mIndicatorDrawable = GradientDrawable()
    private val mRectPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val mDividerPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val mTrianglePaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val mTrianglePath = Path()

    var mIndicatorStyle = STYLE_NORMAL
        set(value) {
            field = value
            invalidate()
        }

    private var mTabPadding = 0f
    fun setTabPadding(value: Float) {
        mTabPadding = value.dp
        updateTabStyles()
    }

    private var mTabSpaceEqual = false
        set(value) {
            field = value
            updateTabStyles()
        }

    private var mTabWidth = 0f
    fun setTabWidth(value: Float) {
        mTabWidth = value.dp
        updateTabStyles()
    }

    /**
     * indicator
     */
    var mIndicatorColor = 0
        set(value) {
            field = value
            invalidate()
        }

    private var mIndicatorHeight = 0f
    fun setIndicatorHeight(value: Float) {
        mIndicatorHeight = value.dp
        invalidate()
    }

    private var mIndicatorWidth = 0f
    fun mIndicatorWidth(value: Float) {
        mIndicatorHeight = value.dp
        invalidate()
    }

    private var mIndicatorCornerRadius = 0f
    fun setIndicatorCornerRadius(value: Float) {
        mIndicatorCornerRadius = value.dp
        invalidate()
    }

    private var mIndicatorMarginLeft = 0f
    fun setIndicatorMarginLeft(value: Float) {
        mIndicatorMarginLeft = value.dp
        invalidate()
    }

    private var mIndicatorMarginTop = 0f
    fun setIndicatorMarginTop(value: Float) {
        mIndicatorMarginTop = value.dp
        invalidate()
    }

    private var mIndicatorMarginRight = 0f
    fun setIndicatorMarginRight(value: Float) {
        mIndicatorMarginRight = value.dp
        invalidate()
    }

    private var mIndicatorMarginBottom = 0f
    fun setIndicatorMarginBottom(value: Float) {
        mIndicatorMarginBottom = value.dp
        invalidate()
    }

    var mIndicatorGravity = 0
        set(value) {
            field = value
            invalidate()
        }
    var mIndicatorWidthEqualTitle = false
        set(value) {
            field = value
            invalidate()
        }

    /**
     * underline
     */
    var mUnderlineColor = 0
        set(value) {
            field = value
            invalidate()
        }

    private var mUnderlineHeight = 0f
    fun setUnderlineHeight(value: Float) {
        mUnderlineHeight = value.dp
        invalidate()
    }

    var mUnderlineGravity = 0
        set(value) {
            field = value
            invalidate()
        }

    /**
     * divider
     */
    var mDividerColor = 0
        set(value) {
            field = value
            invalidate()
        }

    private var mDividerWidth = 0f
    fun setDividerWidth(value: Float) {
        mDividerWidth = value.dp
        invalidate()
    }

    private var mDividerPadding = 0f
    fun setDividerPadding(value: Float) {
        mDividerPadding = value.dp
        invalidate()
    }

    private var mTextSize = 0f
    fun setTextSize(value: Float) {
        mTextSize = value.sp
        invalidate()
    }

    var mTextSelectColor = 0
        set(value) {
            field = value
            updateTabStyles()
        }
    var mTextUnselectColor = 0
        set(value) {
            field = value
            updateTabStyles()
        }
    var mTextBold = 0
        set(value) {
            field = value
            updateTabStyles()
        }
    var mTextAllCaps = false
        set(value) {
            field = value
            updateTabStyles()
        }
    private var mLastScrollX = 0
    private var mHeight = 0
    private var mSnapOnTabClick = false

    init {
        isFillViewport = true //设置滚动视图是否可以伸缩其内容以填充视口
        setWillNotDraw(false) //重写onDraw方法,需要调用这个方法来清除flag
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
    }

    private fun obtainAttributes(context: Context, attrs: AttributeSet?) {
        val ta = context.obtainStyledAttributes(attrs, R.styleable.SlidingTabLayout)
        mIndicatorStyle = ta.getInt(R.styleable.SlidingTabLayout_tl_indicator_style, STYLE_NORMAL)
        mIndicatorColor = ta.getColor(R.styleable.SlidingTabLayout_tl_indicator_color, Color.parseColor(if (mIndicatorStyle == STYLE_BLOCK) "#4B6A87" else "#ffffff"))
        mIndicatorHeight = ta.getDimension(
            R.styleable.SlidingTabLayout_tl_indicator_height, (if (mIndicatorStyle == STYLE_TRIANGLE) 4F else if (mIndicatorStyle == STYLE_BLOCK) -1F else 2F).dp
        )
        mIndicatorWidth = ta.getDimension(R.styleable.SlidingTabLayout_tl_indicator_width, (if (mIndicatorStyle == STYLE_TRIANGLE) 10F else -1F).dp)
        mIndicatorCornerRadius = ta.getDimension(R.styleable.SlidingTabLayout_tl_indicator_corner_radius, (if (mIndicatorStyle == STYLE_BLOCK) -1F else 0F).dp)
        mIndicatorMarginLeft = ta.getDimension(R.styleable.SlidingTabLayout_tl_indicator_margin_left, 0F.dp)
        mIndicatorMarginTop = ta.getDimension(R.styleable.SlidingTabLayout_tl_indicator_margin_top, (if (mIndicatorStyle == STYLE_BLOCK) 7F else 0F).dp)
        mIndicatorMarginRight = ta.getDimension(R.styleable.SlidingTabLayout_tl_indicator_margin_right, 0F.dp)
        mIndicatorMarginBottom = ta.getDimension(R.styleable.SlidingTabLayout_tl_indicator_margin_bottom, (if (mIndicatorStyle == STYLE_BLOCK) 7F else 0F).dp)
        mIndicatorGravity = ta.getInt(R.styleable.SlidingTabLayout_tl_indicator_gravity, Gravity.BOTTOM)
        mIndicatorWidthEqualTitle = ta.getBoolean(R.styleable.SlidingTabLayout_tl_indicator_width_equal_title, false)
        mUnderlineColor = ta.getColor(R.styleable.SlidingTabLayout_tl_underline_color, Color.parseColor("#ffffff"))
        mUnderlineHeight = ta.getDimension(R.styleable.SlidingTabLayout_tl_underline_height, 0F.dp)
        mUnderlineGravity = ta.getInt(R.styleable.SlidingTabLayout_tl_underline_gravity, Gravity.BOTTOM)
        mDividerColor = ta.getColor(R.styleable.SlidingTabLayout_tl_divider_color, Color.parseColor("#ffffff"))
        mDividerWidth = ta.getDimension(R.styleable.SlidingTabLayout_tl_divider_width, 0F.dp)
        mDividerPadding = ta.getDimension(R.styleable.SlidingTabLayout_tl_divider_padding, 12F.dp)
        mTextSize = ta.getDimension(R.styleable.SlidingTabLayout_tl_textsize, 14F.sp)
        mTextSelectColor = ta.getColor(R.styleable.SlidingTabLayout_tl_textSelectColor, Color.parseColor("#ffffff"))
        mTextUnselectColor = ta.getColor(R.styleable.SlidingTabLayout_tl_textUnselectColor, Color.parseColor("#AAffffff"))
        mTextBold = ta.getInt(R.styleable.SlidingTabLayout_tl_textBold, TEXT_BOLD_NONE)
        mTextAllCaps = ta.getBoolean(R.styleable.SlidingTabLayout_tl_textAllCaps, false)
        mTabSpaceEqual = ta.getBoolean(R.styleable.SlidingTabLayout_tl_tab_space_equal, false)
        mTabWidth = ta.getDimension(R.styleable.SlidingTabLayout_tl_tab_width, (-1F).dp)
        mTabPadding = ta.getDimension(R.styleable.SlidingTabLayout_tl_tab_padding, if (mTabSpaceEqual || mTabWidth > 0) 0F.dp else 20F.dp)
        ta.recycle()
    }

    /**
     * 关联ViewPager
     */
    fun setViewPager(vp: ViewPager2) {
        check(vp.adapter != null) { "ViewPager or ViewPager adapter can not be NULL !" }
        mViewPager = vp
        mViewPager.run {
            unregisterOnPageChangeCallback(onPageChange)
            registerOnPageChangeCallback(onPageChange)
        }
        notifyDataSetChanged()
    }

    /**
     * 关联ViewPager,用于不想在ViewPager适配器中设置titles数据的情况
     */
    fun setViewPager(vp: ViewPager2, titles: ArrayList<String>) {
        check(vp.adapter != null) { "ViewPager or ViewPager adapter can not be NULL !" }
        check(titles.isNotEmpty()) { "Titles can not be EMPTY !" }
        check(titles.size == vp.adapter!!.itemCount) { "Titles length must be the same as the page count !" }
        mViewPager = vp
        mTitles = ArrayList()
        mTitles!!.addAll(titles)
        mViewPager.run {
            unregisterOnPageChangeCallback(onPageChange)
            registerOnPageChangeCallback(onPageChange)
        }
        notifyDataSetChanged()
    }

    /**
     * 关联ViewPager,用于连适配器都不想自己实例化的情况
     */
    fun setViewPager(vp: ViewPager2, titles: ArrayList<String>, fa: FragmentActivity, fragments: ArrayList<Fragment>) {
        check(titles.isNotEmpty()) { "Titles can not be EMPTY !" }
        mViewPager = vp
        mTitles = titles
        mViewPager.apply {
            adapter = InnerPagerAdapter(fa, fragments)
            offscreenPageLimit = titles.size
            unregisterOnPageChangeCallback(onPageChange)
            registerOnPageChangeCallback(onPageChange)
        }
        notifyDataSetChanged()
    }

    /**
     * 关联ViewPager,用于连适配器都不想自己实例化的情况
     */
    fun setViewPager(vp: ViewPager2, titles: ArrayList<String>, fragment: Fragment, fragments: ArrayList<Fragment>) {
        check(titles.isNotEmpty()) { "Titles can not be EMPTY !" }
        mViewPager = vp
        mTitles = titles
        mViewPager.apply {
            adapter = InnerPagerChildAdapter(fragment, fragments)
            offscreenPageLimit = titles.size
            unregisterOnPageChangeCallback(onPageChange)
            registerOnPageChangeCallback(onPageChange)
        }
        notifyDataSetChanged()
    }

    /**
     * 更新数据
     */
    fun notifyDataSetChanged() {
        mTabsContainer.removeAllViews()
        mTabCount = mTitles?.size ?: mViewPager.adapter?.itemCount ?: 0
        var tabView: View
        for (i in 0 until mTabCount) {
            tabView = inflate(mContext, R.layout.layout_tab, null)
            val pageTitle = mTitles?.get(i)
            addTab(i, pageTitle.toString(), tabView)
        }
        updateTabStyles()
    }

    fun addNewTab(title: String) {
        val tabView = inflate(mContext, R.layout.layout_tab, null)
        if (mTitles != null) {
            mTitles!!.add(title)
        }
        val pageTitle = mTitles?.get(mTabCount)
        addTab(mTabCount, pageTitle.toString(), tabView)
        mTabCount = mTitles?.size ?: mViewPager.adapter?.itemCount ?: 0
        updateTabStyles()
    }

    /**
     * 创建并添加tab
     */
    private fun addTab(position: Int, title: String, tabView: View) {
        val tvTabTitle = tabView.findViewById<TextView>(R.id.tv_tab_title)
        tvTabTitle.text = title
        tabView.doClick {
            val position1 = mTabsContainer.indexOfChild(this)
            if (position1 != -1) {
                if (mViewPager.currentItem != position1) {
                    if (mSnapOnTabClick) {
                        mViewPager.setCurrentItem(position1, false)
                    } else {
                        mViewPager.currentItem = position1
                    }
                    mListener?.onTabSelect(position1)
                } else {
                    mListener?.onTabReselect(position1)
                }
            }
        }
        /** 每一个Tab的布局参数  */
        var lpTab =
            if (mTabSpaceEqual) LinearLayout.LayoutParams(0, LayoutParams.MATCH_PARENT, 1.0f) else LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT)
        if (mTabWidth > 0) {
            lpTab = LinearLayout.LayoutParams(mTabWidth.toInt(), LayoutParams.MATCH_PARENT)
        }
        mTabsContainer.addView(tabView, position, lpTab)
    }

    private fun updateTabStyles() {
        for (i in 0 until mTabCount) {
            val v = mTabsContainer.getChildAt(i)
            val tvTabTitle = v.findViewById<TextView>(R.id.tv_tab_title)
            tvTabTitle.setTextColor(if (i == mCurrentTab) mTextSelectColor else mTextUnselectColor)
            tvTabTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTextSize)
            tvTabTitle.setPadding(mTabPadding.toInt(), 0, mTabPadding.toInt(), 0)
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

    private val onPageChange = object : ViewPager2.OnPageChangeCallback() {
        override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            super.onPageScrolled(position, positionOffset, positionOffsetPixels)
            /*
             * position:当前View的位置
             * mCurrentPositionOffset:当前View的偏移量比例.[0,1)
             */
            mCurrentTab = position
            mCurrentPositionOffset = positionOffset
            scrollToCurrentTab()
            invalidate()
        }

        override fun onPageSelected(position: Int) {
            super.onPageSelected(position)
            updateTabSelection(position)
        }
    }

    /**
     * HorizontalScrollView滚到当前tab,并且居中显示
     */
    private fun scrollToCurrentTab() {
        if (mTabCount <= 0) {
            return
        }
        val offset = (mCurrentPositionOffset * mTabsContainer.getChildAt(mCurrentTab).width).toInt()

        /**当前Tab的left+当前Tab的Width乘以positionOffset */
        var newScrollX = mTabsContainer.getChildAt(mCurrentTab).left + offset
        if (mCurrentTab > 0 || offset > 0) {
            /**HorizontalScrollView移动到当前tab,并居中 */
            newScrollX -= width / 2 - paddingLeft
            calcIndicatorRect()
            newScrollX += (mTabRect.right - mTabRect.left) / 2
        }
        if (newScrollX != mLastScrollX) {
            mLastScrollX = newScrollX
            /** scrollTo（int x,int y）:x,y代表的不是坐标点,而是偏移量
             * x:表示离起始位置的x水平方向的偏移量
             * y:表示离起始位置的y垂直方向的偏移量
             */
            scrollTo(newScrollX, 0)
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

    private var margin = 0f

    private fun calcIndicatorRect() {
        val currentTabView = mTabsContainer.getChildAt(mCurrentTab)
        var left = currentTabView.left.toFloat()
        var right = currentTabView.right.toFloat()

        //for mIndicatorWidthEqualTitle
        if (mIndicatorStyle == STYLE_NORMAL && mIndicatorWidthEqualTitle) {
            val tabTitle = currentTabView.findViewById<TextView>(R.id.tv_tab_title)
            mTextPaint.textSize = mTextSize
            val textWidth = mTextPaint.measureText(tabTitle.text.toString())
            margin = (right - left - textWidth) / 2
        }
        if (mCurrentTab < mTabCount - 1) {
            val nextTabView = mTabsContainer.getChildAt(mCurrentTab + 1)
            val nextTabLeft = nextTabView.left.toFloat()
            val nextTabRight = nextTabView.right.toFloat()
            left += mCurrentPositionOffset * (nextTabLeft - left)
            right += mCurrentPositionOffset * (nextTabRight - right)

            //for mIndicatorWidthEqualTitle
            if (mIndicatorStyle == STYLE_NORMAL && mIndicatorWidthEqualTitle) {
                val nextTabTitle = nextTabView.findViewById<TextView>(R.id.tv_tab_title)
                mTextPaint.textSize = mTextSize
                val nextTextWidth = mTextPaint.measureText(nextTabTitle.text.toString())
                val nextMargin = (nextTabRight - nextTabLeft - nextTextWidth) / 2
                margin += mCurrentPositionOffset * (nextMargin - margin)
            }
        }
        mIndicatorRect.left = left.toInt()
        mIndicatorRect.right = right.toInt()
        //for mIndicatorWidthEqualTitle
        if (mIndicatorStyle == STYLE_NORMAL && mIndicatorWidthEqualTitle) {
            mIndicatorRect.left = (left + margin - 1).toInt()
            mIndicatorRect.right = (right - margin - 1).toInt()
        }
        mTabRect.left = left.toInt()
        mTabRect.right = right.toInt()
        if (mIndicatorWidth >= 0) {
            var indicatorLeft = currentTabView.left + (currentTabView.width - mIndicatorWidth) / 2
            if (mCurrentTab < mTabCount - 1) {
                val nextTab = mTabsContainer.getChildAt(mCurrentTab + 1)
                indicatorLeft += mCurrentPositionOffset * (currentTabView.width / 2 + nextTab.width / 2)
            }
            mIndicatorRect.left = indicatorLeft.toInt()
            mIndicatorRect.right = (mIndicatorRect.left + mIndicatorWidth).toInt()
        }
    }

    override fun onDraw(canvas: Canvas) {
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
                canvas.drawLine((paddingLeft + tab.right).toFloat(), mDividerPadding, (paddingLeft + tab.right).toFloat(), height - mDividerPadding, mDividerPaint)
            }
        }

        // draw underline
        if (mUnderlineHeight > 0) {
            mRectPaint.color = mUnderlineColor
            if (mUnderlineGravity == Gravity.BOTTOM) {
                canvas.drawRect(paddingLeft.toFloat(), height - mUnderlineHeight, (mTabsContainer.width + paddingLeft).toFloat(), height.toFloat(), mRectPaint)
            } else {
                canvas.drawRect(paddingLeft.toFloat(), 0f, (mTabsContainer.width + paddingLeft).toFloat(), mUnderlineHeight, mRectPaint)
            }
        }

        //draw indicator line
        calcIndicatorRect()
        if (mIndicatorStyle == STYLE_TRIANGLE) {
            if (mIndicatorHeight > 0) {
                mTrianglePaint.color = mIndicatorColor
                mTrianglePath.reset()
                mTrianglePath.moveTo((paddingLeft + mIndicatorRect.left).toFloat(), height.toFloat())
                mTrianglePath.lineTo((paddingLeft + mIndicatorRect.left / 2 + mIndicatorRect.right / 2).toFloat(), height - mIndicatorHeight)
                mTrianglePath.lineTo((paddingLeft + mIndicatorRect.right).toFloat(), height.toFloat())
                mTrianglePath.close()
                canvas.drawPath(mTrianglePath, mTrianglePaint)
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
                mIndicatorDrawable.draw(canvas)
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
                mIndicatorDrawable.draw(canvas)
            }
        }
    }

    fun getCurrentTab(): Int {
        return mCurrentTab
    }

    fun setCurrentTab(currentTab: Int) {
        this.mCurrentTab = currentTab
        mViewPager.currentItem = currentTab
    }

    fun setCurrentTab(currentTab: Int, smoothScroll: Boolean? = null) {
        mCurrentTab = currentTab
        if (smoothScroll == null) {
            mViewPager.currentItem = currentTab
        } else {
            mViewPager.setCurrentItem(currentTab, smoothScroll)
        }
    }

    fun setIndicatorMargin(indicatorMarginLeft: Float, indicatorMarginTop: Float, indicatorMarginRight: Float, indicatorMarginBottom: Float) {
        this.mIndicatorMarginLeft = indicatorMarginLeft.dp
        this.mIndicatorMarginTop = indicatorMarginTop.dp
        this.mIndicatorMarginRight = indicatorMarginRight.dp
        this.mIndicatorMarginBottom = indicatorMarginBottom.dp
        invalidate()
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
        var index = position
        if (index >= mTabCount) {
            index = mTabCount - 1
        }
        val tabView = mTabsContainer.getChildAt(index)
        val tipView = tabView.findViewById<MsgView>(R.id.rtv_msg_tip)
        UnreadMsgUtils.show(tipView, num)
        if (mInitSetMap[index]) {
            return
        }
        setMsgMargin(index, 4, 2)
        mInitSetMap.put(index, true)
    }

    /**
     * 显示未读红点
     *
     * @param position 显示tab位置
     */
    fun showDot(position: Int) {
        var index = position
        if (index >= mTabCount) {
            index = mTabCount - 1
        }
        showMsg(index, 0)
    }

    /**
     * 隐藏未读消息
     */
    fun hideMsg(position: Int) {
        var index = position
        if (index >= mTabCount) {
            index = mTabCount - 1
        }
        val tabView = mTabsContainer.getChildAt(index)
        val tipView = tabView.findViewById<MsgView>(R.id.rtv_msg_tip)
        tipView.gone()
    }

    /**
     * 设置未读消息偏移,原点为文字的右上角.当控件高度固定,消息提示位置易控制,显示效果佳
     */
    fun setMsgMargin(position: Int, leftPadding: Int, bottomPadding: Int) {
        var index = position
        if (index >= mTabCount) {
            index = mTabCount - 1
        }
        val tabView = mTabsContainer.getChildAt(index)
        val tipView = tabView.findViewById<MsgView>(R.id.rtv_msg_tip)
        val tvTabTitle = tabView.findViewById<TextView>(R.id.tv_tab_title)
        mTextPaint.textSize = mTextSize
        val textWidth = mTextPaint.measureText(tvTabTitle.text.toString())
        val textHeight = mTextPaint.descent() - mTextPaint.ascent()
        val lp = tipView.layoutParams as MarginLayoutParams
        lp.leftMargin = if (mTabWidth >= 0) (mTabWidth / 2 + textWidth / 2 + leftPadding.dp).toInt() else (mTabPadding + textWidth + leftPadding.dp).toInt()
        lp.topMargin = if (mHeight > 0) ((mHeight - textHeight).toInt() / 2 - bottomPadding.dp) else 0
        tipView.layoutParams = lp
    }

    /**
     * 当前类只提供了少许设置未读消息属性的方法,可以通过该方法获取MsgView对象从而各种设置
     */
    fun getMsgView(position: Int): MsgView {
        var index = position
        if (index >= mTabCount) {
            index = mTabCount - 1
        }
        val tabView = mTabsContainer.getChildAt(index)
        return tabView.findViewById(R.id.rtv_msg_tip)
    }

    var mListener: OnTabSelectListener? = null

    internal class InnerPagerAdapter(fm: FragmentActivity, private val fragments: ArrayList<Fragment>) : FragmentStateAdapter(fm) {
        override fun getItemCount(): Int = fragments.size

        override fun createFragment(position: Int): Fragment = fragments[position]
    }

    internal class InnerPagerChildAdapter(fm: Fragment, private val fragments: ArrayList<Fragment>) : FragmentStateAdapter(fm) {
        override fun getItemCount(): Int = fragments.size

        override fun createFragment(position: Int): Fragment = fragments[position]
    }
}