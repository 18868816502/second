package com.beiwo.klyjaz.loan;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import com.beiwo.klyjaz.R;


/**
 * https://gitee.com/tangbuzhi
 *
 * @author: Tangbuzhi
 * @version:
 * @package:
 * @description:
 * @modify:
 * @date: 2018/10/9
 */

public class PopUtil {
    public static void pop(Context context, int layoutRes, View anchor, PopViewClickListener listener) {
        View contentView = LayoutInflater.from(context).inflate(layoutRes, null);
        PopupWindow popup = new PopupWindow(contentView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true);
        listener.popClick(popup);
        popup.setAnimationStyle(R.style.anim_style_top2bottom);
        popup.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        popup.setOutsideTouchable(true);
        popup.setTouchable(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {//7.0
            Rect visibleFrame = new Rect();
            anchor.getGlobalVisibleRect(visibleFrame);
            int height = anchor.getResources().getDisplayMetrics().heightPixels - visibleFrame.bottom;
            popup.setHeight(height);
            popup.showAsDropDown(anchor, 0, 15);
        } else {
            popup.showAsDropDown(anchor, 0, 15);
        }
    }

    public interface PopViewClickListener {
        void popClick(PopupWindow popup);
    }
}