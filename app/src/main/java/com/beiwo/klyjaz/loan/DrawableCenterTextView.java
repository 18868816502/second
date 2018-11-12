package com.beiwo.klyjaz.loan;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

/**
 * https://gitee.com/tangbuzhi
 *
 * @author: Tangbuzhi
 * @version:
 * @package:
 * @description:
 * @modify:
 * @date: 2018/10/16
 */

public class DrawableCenterTextView extends AppCompatTextView {
    public DrawableCenterTextView(Context context) {
        super(context);
    }

    public DrawableCenterTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public DrawableCenterTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Drawable[] drawables = getCompoundDrawables();
        if (drawables != null) {
            Drawable left = drawables[0];
            /*Drawable top = drawables[1];
            Drawable right = drawables[2];
            Drawable bottom = drawables[3];*/
            if (left != null) {
                float textWidth = getPaint().measureText(getText().toString());
                int drawablePadding = getCompoundDrawablePadding();
                int drawableWidth = left.getIntrinsicWidth();
                float bodyWidth = textWidth + drawableWidth + drawablePadding;
                canvas.translate((getWidth() - bodyWidth) / 2, 0);
            }
        }
        super.onDraw(canvas);
    }
}