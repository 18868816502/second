package com.beiwo.klyjaz.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by admin on 2018/5/21.
 */

public class MarqueeTextView extends android.support.v7.widget.AppCompatTextView implements View.OnClickListener  {

    public MarqueeTextView(Context context) {
        super(context);
    }

    public MarqueeTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MarqueeTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public boolean isFocused() {
        return true;
    }
}
