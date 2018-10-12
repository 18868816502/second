package com.beihui.market.loan;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import com.beihui.market.R;


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
        popup.setAnimationStyle(R.style.popup_anim_style);
        popup.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        popup.setOutsideTouchable(false);
        popup.setTouchable(true);
        popup.showAsDropDown(anchor, 0, 0);
    }

    public interface PopViewClickListener {
        void popClick(PopupWindow popup);
    }
}