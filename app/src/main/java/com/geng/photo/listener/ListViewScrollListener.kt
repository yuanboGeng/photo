package com.geng.photo.listener


import android.util.Log
import android.widget.AbsListView
import android.widget.BaseAdapter

import com.geng.photo.weight.ImageLoader

/**
 * Created by gengyuanbo on 2018/12/3.
 * Desc:
 */
class ListViewScrollListener(private var mBaseAdapter: BaseAdapter?) : AbsListView.OnScrollListener {
    private var mPreviousFirstVisibleItem = 0
    private var mPreviousEventTime: Long = 0
    private var mIndex = 0 //Ignore for the first time

    override fun onScrollStateChanged(view: AbsListView, scrollState: Int) {
        if (mBaseAdapter == null) {
            return
        }
        when (scrollState) {
            AbsListView.OnScrollListener.SCROLL_STATE_FLING -> {
                ImageLoader.lock()
            }
            AbsListView.OnScrollListener.SCROLL_STATE_IDLE -> {
                ImageLoader.unLock()
                mBaseAdapter!!.notifyDataSetChanged()
            }
            AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL -> {
                mIndex = 0
            }
            else -> ImageLoader.unLock()
        }
    }

    override fun onScroll(view: AbsListView, firstVisibleItem: Int, visibleItemCount: Int, totalItemCount: Int) {
        if (mPreviousFirstVisibleItem != firstVisibleItem) {
            val currTime = System.currentTimeMillis()
            val timeToScrollOneElement = currTime - mPreviousEventTime
            val speed = 1.toDouble() / timeToScrollOneElement * 1000
            mPreviousFirstVisibleItem = firstVisibleItem
            mPreviousEventTime = currTime
            //Ignore for the first time
            Log.d(TAG, "speed:  $speed")
            if (speed > MAX_SPEED && mIndex != 0) {
                ImageLoader.lock()
            } else {
                ImageLoader.unLock()
            }
            mIndex++
        }
    }

    companion object {
        val TAG = ListViewScrollListener::class.java.simpleName!!
        const val MAX_SPEED = 10
    }
}
