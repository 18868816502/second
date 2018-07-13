package com.beihui.market.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

/**
 * Created by admin on 2018/5/21.
 */

public class MarqueeTextView extends TextView implements View.OnClickListener  {


//    public boolean mIsFloating = false; //是否开始滚动
//    private float mSpeed = 0.5f;
//    private float mStep = 0f;
//    private String mStr = ""; //文本内容
//    private float mTextLength = 0f; //文本长度
//    private float mViewWidth = 0f;
//    private float mY = 0f; //文字的纵坐标
//
//    public Context context;
//    private float textViewWidh;
//
    public MarqueeTextView(Context context) {
        super(context);
//        this.context = context;
//        initView();
    }

    public MarqueeTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
//        this.context = context;
//
//        initView();
    }

    public MarqueeTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
//        this.context = context;
//        initView();
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public boolean isFocused() {
        return true;
    }

    //
//
//    private void initView() {
//        setOnClickListener(this);
//    }
//
//    public void init(String str, float speed, float width, float textViewWidh) {
//        setText(str);
//        mSpeed = speed;
//        mStr = getText().toString();
//        mTextLength = getPaint().measureText(mStr);
//        mViewWidth = width;
//        mStep = mTextLength + mViewWidth;
//        mY = getTextSize() + getPaddingTop();
//        getPaint().setColor(0xff424251);
//        getPaint().setTextAlign(Paint.Align.LEFT);
//        float textWidth = getTextWidth(mStr, getTextSize());
//        this.textViewWidh = textViewWidh;
//        Log.e(" mIsFloating = false;" , " getTextWidth() " +  textWidth);
//    }
//
//    @Override
//    public void onClick(View v) {
////        if (mIsFloating)
////            stopFloating();
////        else
////            startFloating();
//    }
//
//    @Override
//    public void onDraw(Canvas canvas) {
//        canvas.drawText(mStr, 0, mStr.length(), mViewWidth + mTextLength - mStep, mY, getPaint());
//        mStep += mSpeed;
//        if (mStep > mViewWidth + mTextLength * 2) {
//            mStep = mTextLength;
//        }
////        Log.e(" mIsFloating = false;" , " mStep " +  mStep);
//        if (mIsFloating) {
//            invalidate();
//        }
//    }
//
//    @Override
//    public void onRestoreInstanceState(Parcelable state) {
//        if (!(state instanceof SavedState)) {
//            super.onRestoreInstanceState(state);
//            return;
//        }
//        SavedState savedState = (SavedState) state;
//        super.onRestoreInstanceState(savedState.getSuperState());
//
//        mStep = savedState.mStep;
//        mIsFloating = savedState.mIsFloating;
//    }
//
//    @Override
//    public Parcelable onSaveInstanceState() {
//        Parcelable superState = super.onSaveInstanceState();
//        SavedState savedState = new SavedState(superState);
//        savedState.mStep = mStep;
//        savedState.mIsFloating = mIsFloating;
//        return savedState;
//    }
//
//    public void setSpeed(float speed) {
//        mSpeed = speed;
//    }
//
//    public void startFloating() {
//        mIsFloating = mTextLength + mViewWidth > this.textViewWidh;
//        // mIsFloating = true;
//        Log.e(" mIsFloating = false;" , " mTextLength + mViewWidth = ---> " +  mTextLength + mViewWidth);
//        Log.e(" mIsFloating = false;" , " getWidth() " +     this.textViewWidh);
//        Log.e(" mIsFloating = false;" , " mIsFloating = ---> " +  mIsFloating);
//        if (mIsFloating) {
//            invalidate();
//        }
//    }
//
//    public float getTextWidth(String text, float textSize){
//        TextPaint paint = new TextPaint();
//        float scaledDensity = this.context.getResources().getDisplayMetrics().scaledDensity;
//        paint.setTextSize(scaledDensity * textSize);
//        return paint.measureText(text);
//    }
//
//    public void stopFloating() {
//        mIsFloating = false;
//        invalidate();
//    }
//
//
//    public static class SavedState extends BaseSavedState {
//        public static final Parcelable.Creator<SavedState> CREATOR = new Parcelable.Creator<SavedState>() {
//
//            @Override
//            public SavedState createFromParcel(Parcel in) {
//                return new SavedState(in);
//            }
//
//            public SavedState[] newArray(int size) {
//                return new SavedState[size];
//            }
//
//        };
//        public boolean mIsFloating = false;
//        public float mStep = 0.0f;
//
//        private SavedState(Parcel in) {
//            super(in);
//            boolean[] b = new boolean[1];
//            in.readBooleanArray(b);
//            mIsFloating = b[0];
//            mStep = in.readFloat();
//        }
//
//        SavedState(Parcelable superState) {
//            super(superState);
//        }
//
//        @Override
//        public void writeToParcel(Parcel out, int flags) {
//            super.writeToParcel(out, flags);
//            out.writeBooleanArray(new boolean[]{
//                    mIsFloating
//            });
//            out.writeFloat(mStep);
//        }
//    }
}
