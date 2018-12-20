package com.beiwo.qnejqaz.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.mcxtzhang.swipemenulib.SwipeMenuLayout;


public class CustomSwipeMenuLayout extends SwipeMenuLayout {
    public CustomSwipeMenuLayout(Context context) {
        super(context);
    }

    public CustomSwipeMenuLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomSwipeMenuLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        super.dispatchTouchEvent(event);
        return true;
    }
}