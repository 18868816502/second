package com.beihui.market.tang.widget;

import android.content.Context;
import android.graphics.Typeface;
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
 * @date: 2018/7/25
 */

public class CustomNumTextView extends AppCompatTextView {

    private final String fontPath = "fonts/din-medium.otf";
    private Typeface typeface;

    public Typeface setFont(Context context) {
        if (typeface == null) {
            typeface = Typeface.createFromAsset(context.getAssets(), fontPath);
        }
        return typeface;
    }

    public CustomNumTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setTypeface(setFont(context));
    }
}
