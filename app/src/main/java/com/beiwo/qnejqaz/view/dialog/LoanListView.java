package com.beiwo.qnejqaz.view.dialog;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ListView;


public class LoanListView extends ListView {
    public LoanListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LoanListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public LoanListView(Context context) {
        super(context);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        getParent().requestDisallowInterceptTouchEvent(false);
        return super.onInterceptTouchEvent(ev);
    }
}