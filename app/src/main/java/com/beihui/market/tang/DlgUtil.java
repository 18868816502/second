package com.beihui.market.tang;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

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
        Dialog dialog = new Dialog(context, 0);
        View dlgView = LayoutInflater.from(context).inflate(layoutId, null);
        if (clickListener != null) clickListener.onViewClick(dialog, dlgView);
        dialog.setContentView(dlgView);
        dialog.setCanceledOnTouchOutside(true);
        Window window = dialog.getWindow();
        if (window != null) {
            WindowManager.LayoutParams lp = window.getAttributes();
            window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
            window.setAttributes(lp);
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
        dialog.show();
    }

    public interface OnDlgViewClickListener {
        void onViewClick(Dialog dialog, View dlgView);
    }
}
