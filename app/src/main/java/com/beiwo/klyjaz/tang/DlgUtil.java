package com.beiwo.klyjaz.tang;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.beiwo.klyjaz.R;

/**
 * https://gitee.com/tangbuzhi
 *
 * @author: Tangbuzhi
 * @version:
 * @package:
 * @description:
 * @modify:
 * @date: 2018/7/22
 */

public class DlgUtil {
    public static void createDlg(Context context, int layoutId, OnDlgViewClickListener clickListener) {
        createDlg(context, layoutId, DlgLocation.CENTER, clickListener);
    }

    public static void createDlg(Context context, int layoutId, DlgLocation location, OnDlgViewClickListener clickListener) {
        Dialog dialog = new Dialog(context, 0);
        View dlgView = LayoutInflater.from(context).inflate(layoutId, null);
        if (clickListener != null) clickListener.onViewClick(dialog, dlgView);
        dialog.setContentView(dlgView);
        dialog.setCanceledOnTouchOutside(true);
        Window window = dialog.getWindow();
        if (window != null) {
            WindowManager.LayoutParams lp = window.getAttributes();
            switch (location) {
                case CENTER:
                    window.setGravity(Gravity.CENTER);
                    break;
                case TOP:
                    window.setGravity(Gravity.TOP);
                    window.setWindowAnimations(R.style.anim_style_top2bottom);
                    break;
                case BOTTOM:
                    window.setGravity(Gravity.BOTTOM);
                    window.setWindowAnimations(R.style.anim_style_bottom2top);
                    break;
                default:
                    break;
            }
            window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
            window.setAttributes(lp);
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
        dialog.show();
    }

    public interface OnDlgViewClickListener {
        void onViewClick(Dialog dialog, View dlgView);
    }

    /*取消按钮*/
    public static void cancelClick(final Dialog dialog, View dlgView) {
        dlgView.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    public enum DlgLocation {
        TOP, CENTER, BOTTOM
    }
}