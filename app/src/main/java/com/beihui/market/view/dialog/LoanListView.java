package com.beihui.market.view.dialog;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ListView;

/**
 * Created by MichaelLee826 on 2016-10-10-0010.
 */
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

    private float mDownX;
    private float mDownY;

//    @Override
//    public boolean onInterceptTouchEvent(MotionEvent ev) {
////        getParent().requestDisallowInterceptTouchEvent(false);
//        switch (ev.getAction()){
//            case MotionEvent.ACTION_DOWN:
//                mDownX = ev.getX();
//                mDownY = ev.getY();
//                getParent().requestDisallowInterceptTouchEvent(true);
//                break;
//            case MotionEvent.ACTION_MOVE:
//                if(Math.abs(ev.getX() - mDownX) > Math.abs(ev.getY() - mDownY)){
//                    getParent().requestDisallowInterceptTouchEvent(true);
//                }else{
//                    getParent().requestDisallowInterceptTouchEvent(false);
//                }
//                break;
//            case MotionEvent.ACTION_UP:
//            case MotionEvent.ACTION_CANCEL:
//                getParent().requestDisallowInterceptTouchEvent(false);
//                break;
//        }
//        return super.onInterceptTouchEvent(ev);
//    }

}
