package com.beiwo.klyjaz.util;

import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.beiwo.klyjaz.App;
import com.beiwo.klyjaz.R;


public class ToastUtil {
    private static Toast sToast;
    private static Context context = App.getInstance();

    public static void toast(String msg) {
        toast(msg, -1);
    }

    public static void toast(String msg, int resTop) {
        if (sToast == null)
            sToast = Toast.makeText(context, "", Toast.LENGTH_SHORT);
        View view = LayoutInflater.from(context).inflate(R.layout.f_layout_toast, null);
        ImageView iv_image = view.findViewById(R.id.iv_image);
        View view_gap = view.findViewById(R.id.view_gap);
        TextView tv_content = view.findViewById(R.id.tv_content);
        sToast.setGravity(Gravity.CENTER, 0, 0);
        sToast.setView(view);

        if (!TextUtils.isEmpty(msg) && resTop != -1) {
            iv_image.setVisibility(View.VISIBLE);
            view_gap.setVisibility(View.VISIBLE);
            tv_content.setVisibility(View.VISIBLE);
            iv_image.setImageResource(resTop);
            tv_content.setText(msg);
        } else if (!TextUtils.isEmpty(msg)) {
            iv_image.setVisibility(View.GONE);
            view_gap.setVisibility(View.GONE);
            tv_content.setVisibility(View.VISIBLE);
            tv_content.setText(msg);
        } else if (resTop != -1) {
            iv_image.setVisibility(View.VISIBLE);
            view_gap.setVisibility(View.GONE);
            tv_content.setVisibility(View.GONE);
            iv_image.setImageResource(resTop);
        } else {
            iv_image.setVisibility(View.GONE);
            view_gap.setVisibility(View.GONE);
            tv_content.setVisibility(View.GONE);
        }
        sToast.show();
    }
}