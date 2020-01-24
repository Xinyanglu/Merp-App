package com.merp.android

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.viewpager.widget.ViewPager

/**
 * A custom [ViewPager] class for preventing the user from swiping left and right to change tabs.
 *
 * @constructor The default [ViewPager] constructor
 *
 * @param [context] TODO
 * @param attrs TODO
 */
class NonswipeableViewPager(context: Context, attrs: AttributeSet): ViewPager(context, attrs) {

    /**
     * Overridden method prevents user from swiping left and right to change tabs.
     *
     * @param [ev] Object used to report movement events
     */
    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        return false
    }


}