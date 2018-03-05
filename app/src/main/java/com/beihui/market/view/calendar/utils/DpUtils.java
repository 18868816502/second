package com.beihui.market.view.calendar.utils;


import android.content.Context;
import android.util.TypedValue;

public class DpUtils {
    public static float dp2px(Context context, int dip) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dip, context.getResources().getDisplayMetrics());
    }
}
