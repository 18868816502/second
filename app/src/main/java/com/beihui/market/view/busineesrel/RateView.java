package com.beihui.market.view.busineesrel;


import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import com.beihui.market.R;

public class RateView extends LinearLayout {
    public RateView(Context context) {
        this(context, null);
    }

    public RateView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RateView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setOrientation(HORIZONTAL);

        inflate(context, R.layout.layout_rate_view, this);
    }
}
