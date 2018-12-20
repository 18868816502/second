package com.beiwo.qnejqaz.util;


import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.beiwo.qnejqaz.R;

import java.lang.ref.WeakReference;

public class WeakRefToastUtil {
    private static WeakReference<Toast> toast;
    private static WeakReference<Toast> leadToast;

    public static void showShort(Context context, String msg, @DrawableRes int drawableRes) {
        Drawable drawable = ContextCompat.getDrawable(context, drawableRes);
        showShort(context, msg, drawable);
    }

    public static void showShort(Context context, String msg, Drawable drawable) {
        cancel();
        toast = new WeakReference<>(createToast(context, msg, drawable));
        if (toast.get() != null) toast.get().show();
    }

    public static void cancel() {
        if (toast != null && toast.get() != null)
            toast.get().cancel();
    }

    private static Toast createToast(Context context, String msg, Drawable drawable) {
        if (context == null) return null;
        Toast toast = new Toast(context);
        toast.setGravity(Gravity.CENTER, 0, 0);
        View view = LayoutInflater.from(context).inflate(R.layout.layout_toast_as_alert, null);
        toast.setView(view);

        if (msg != null && drawable != null) {
            view.findViewById(R.id.both).setVisibility(View.VISIBLE);
            ((TextView) view.findViewById(R.id.text)).setText(msg);
            ((ImageView) view.findViewById(R.id.image)).setImageDrawable(drawable);
        } else if (msg != null) {
            view.findViewById(R.id.single_text).setVisibility(View.VISIBLE);
            ((TextView) view.findViewById(R.id.text_single)).setText(msg);
        } else if (drawable != null) {
            view.findViewById(R.id.single_image).setVisibility(View.VISIBLE);
            ((ImageView) view.findViewById(R.id.image_single)).setImageDrawable(drawable);
        }
        return toast;
    }

    public static void showLeadInResultToast(Context context) {
        if (context == null) return;
        Toast toast = new Toast(context);
        toast.setGravity(Gravity.BOTTOM, 0, (int) (context.getResources().getDisplayMetrics().density * 60));
        View view = LayoutInflater.from(context).inflate(R.layout.layout_toast_lead_in_result, null);
        toast.setView(view);
        toast.setDuration(Toast.LENGTH_LONG);

        leadToast = new WeakReference<>(toast);
        if (leadToast.get() != null) {
            leadToast.get().show();
        }
    }
}