package cn.chawloo.base.widget.indicator

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.AdapterDataObserver
import androidx.viewpager2.widget.ViewPager2
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback


class CircleIndicator : BaseCircleIndicator {
    private lateinit var mViewpager: ViewPager2

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    fun setViewPager(viewPager: ViewPager2) {
        mViewpager = viewPager
        if (mViewpager.adapter != null) {
            mLastPosition = -1
            createIndicators()
            mViewpager.unregisterOnPageChangeCallback(mInternalPageChangeCallback)
            mViewpager.registerOnPageChangeCallback(mInternalPageChangeCallback)
            mInternalPageChangeCallback.onPageSelected(mViewpager.currentItem)
        }
    }

    private val mInternalPageChangeCallback: OnPageChangeCallback = object : OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            if (position == mLastPosition || mViewpager.adapter == null || mViewpager.adapter!!.itemCount <= 0) {
                return
            }
            animatePageSelected(position)
        }
    }

    private fun createIndicators() {
        val adapter = mViewpager.adapter
        val count: Int = adapter?.itemCount ?: 0
        createIndicators(count, mViewpager.currentItem)
    }

    private val mAdapterDataObserver: AdapterDataObserver = object : AdapterDataObserver() {
        override fun onChanged() {
            super.onChanged()
            val adapter = mViewpager.adapter
            val newCount = adapter?.itemCount ?: 0
            val currentCount = childCount
            mLastPosition = when {
                newCount == currentCount -> {
                    return
                }
                mLastPosition < newCount -> {
                    mViewpager.currentItem
                }
                else -> {
                    RecyclerView.NO_POSITION
                }
            }
            createIndicators()
        }

        override fun onItemRangeChanged(positionStart: Int, itemCount: Int) {
            super.onItemRangeChanged(positionStart, itemCount)
            onChanged()
        }

        override fun onItemRangeChanged(positionStart: Int, itemCount: Int, payload: Any?) {
            super.onItemRangeChanged(positionStart, itemCount, payload)
            onChanged()
        }

        override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
            super.onItemRangeInserted(positionStart, itemCount)
            onChanged()
        }

        override fun onItemRangeRemoved(positionStart: Int, itemCount: Int) {
            super.onItemRangeRemoved(positionStart, itemCount)
            onChanged()
        }

        override fun onItemRangeMoved(fromPosition: Int, toPosition: Int, itemCount: Int) {
            super.onItemRangeMoved(fromPosition, toPosition, itemCount)
            onChanged()
        }
    }

    fun getAdapterDataObserver(): AdapterDataObserver {
        return mAdapterDataObserver
    }
}