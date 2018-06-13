package com.beihui.market.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by admin on 2018/6/13.
 */

public class AutoAdjustSizeEditText extends android.support.v7.widget.AppCompatEditText {

    private Paint mTextPaint;
    private float mTextSize;

    public AutoAdjustSizeEditText(Context context) {
        super(context);
    }

    public AutoAdjustSizeEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
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
//        mTextPaint.getTextBounds(text, 0, 1, boundsCharsRect);
        int textWidth = boundsRect.width();
        int charWidth = boundsCharsRect.width();
        mTextSize = getTextSize();
        while (textWidth > availableTextViewWidth) {
//            if ((textWidth - availableTextViewWidth) > charWidth) {
//                mTextSize += 1;
//            } else {
                mTextSize -= 1;
//            }
            mTextPaint.setTextSize(mTextSize);
            textWidth = mTextPaint.getTextWidths(text, charsWidthArr);
        }

        this.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTextSize);
    }

//    @Override
//    protected void onSelectionChanged(int selStart, int selEnd) {
//        super.onSelectionChanged(selStart, selEnd);
//        //保证光标始终在最后面
//        if(selStart==selEnd){//防止不能多选
//            setSelection(getText().length());
//        }
//
//    }
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        refitText(this.getText().toString(), this.getWidth());
    }

}