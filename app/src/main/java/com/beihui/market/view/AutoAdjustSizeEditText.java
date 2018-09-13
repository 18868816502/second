package com.beihui.market.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.TypedValue;

import com.beihui.market.util.DensityUtil;

/**
 * Created by admin on 2018/6/13.
 */

public class AutoAdjustSizeEditText extends android.support.v7.widget.AppCompatEditText {

    private Paint mTextPaint;
    private float mTextSize;

    public Context mContext;
    private int maxSize;
    private int minSize;

    public AutoAdjustSizeEditText(Context context) {
        super(context);
        mContext = context;
        maxSize = DensityUtil.sp2px(mContext, 27);
        minSize = DensityUtil.sp2px(mContext, 10);
    }

    public AutoAdjustSizeEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        maxSize = DensityUtil.sp2px(mContext, 27);
        minSize = DensityUtil.sp2px(mContext, 10);
    }

    /**
     * Re size the font so the specified text fits in the text box assuming the
     * text box is the specified width.
     *
     * @param text
     */
    private void refitText(String text, int textViewWidth) {
        if (text == null || textViewWidth <= 0)
            return;
        mTextPaint = new Paint();
        mTextPaint.set(this.getPaint());
        int availableTextViewWidth = getWidth() - getPaddingLeft() - getPaddingRight();
        float[] charsWidthArr = new float[text.length()];
        Rect boundsRect = new Rect();
        Rect boundsCharsRect = new Rect();
        mTextPaint.getTextBounds(text, 0, text.length(), boundsRect);
        mTextPaint.getTextBounds("0", 0, 1, boundsCharsRect);
        int textWidth = boundsRect.width();
        int charWidth = boundsCharsRect.width();
        mTextSize = getTextSize();


        while (Math.abs(textWidth - availableTextViewWidth) > 1) {
            /*Log.e("adfas", "textWidth ---> " + textWidth);
            Log.e("adfas", "availableTextViewWidth ---> " + availableTextViewWidth);
            Log.e("adfas", "textWidth - availableTextViewWidth ---> " + (textWidth - availableTextViewWidth));
            Log.e("adfas", "charWidth---> " + charWidth);
            Log.e("adfas", "DensityUtil.sp2px(mContext, 27) ---> " + maxSize);
            Log.e("adfas", "DensityUtil.sp2px(mContext, 15) ---> " + minSize);
            Log.e("adfas", "mTextSize ---> " + mTextSize);*/

            if (textWidth > availableTextViewWidth) {
                if (mTextSize > minSize) {
                    mTextSize -= 1;

                    mTextPaint.setTextSize(mTextSize);
                    mTextPaint.getTextBounds(text, 0, text.length(), boundsRect);
                    textWidth = boundsRect.width();
                } else {
                    break;
                }
                if (availableTextViewWidth > textWidth) {
                    break;
                }
            } else {
                if (mTextSize < maxSize) {
                    mTextSize += 1;

                    mTextPaint.setTextSize(mTextSize);
                    mTextPaint.getTextBounds(text, 0, text.length(), boundsRect);
                    textWidth = boundsRect.width();
                } else {
                    break;
                }
                if (textWidth > availableTextViewWidth) {
                    break;
                }
            }
        }
        this.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTextSize);
    }

    @Override
    protected void onSelectionChanged(int selStart, int selEnd) {
        super.onSelectionChanged(selStart, selEnd);
        //保证光标始终在最后面
        if (selStart == selEnd) {//防止不能多选
            setSelection(getText().length());
        }
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    @Override
    protected void onTextChanged(CharSequence text, int start, int before, int after) {
        super.onTextChanged(text, start, before, after);
        refitText(this.getText().toString(), this.getWidth());
    }
}