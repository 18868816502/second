package com.beiwo.qnejqaz.util;

import android.animation.ObjectAnimator;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;

/**
 * @author chenguoguo
 * @name loanmarket
 * @class name：com.beiwo.klyjaz.utils
 * @descripe
 * @time 2018/12/11 9:39
 */
public class AnimationUtil {
    private static AnimationUtil mInstance;

    public static AnimationUtil with() {
        if (mInstance == null) {
            synchronized (AnimationUtil.class) {
                if (mInstance == null) {
                    mInstance = new AnimationUtil();
                }
            }
        }
        return mInstance;
    }

    /**
     * 从控件所在位置移动到控件的底部
     *
     * @param v
     * @param duration 动画时间
     */
    public void moveToViewBottom(final View v, long duration) {
        ObjectAnimator.ofFloat(v, "translationX", 0f, v.getMeasuredWidth()).setDuration(duration).start();
    }

    /**
     * 从控件的底部移动到控件所在位置
     *
     * @param v
     * @param duration 动画时间
     */
    public void bottomMoveToViewLocation(View v, long duration) {
        ObjectAnimator.ofFloat(v, "translationX", v.getMeasuredWidth(), 0f).setDuration(duration).start();
    }
}