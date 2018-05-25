package com.beihui.market.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.beihui.market.event.InsideViewPagerBus;
import com.mcxtzhang.swipemenulib.SwipeMenuLayout;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by admin on 2018/5/23.
 */

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
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                EventBus.getDefault().postSticky(new InsideViewPagerBus(true));
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                EventBus.getDefault().postSticky(new InsideViewPagerBus(false));
                break;
        }
        super.dispatchTouchEvent(event);
        return true;
    }
}
