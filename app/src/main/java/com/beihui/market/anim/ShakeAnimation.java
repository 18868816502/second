package com.beihui.market.anim;

import android.util.DisplayMetrics;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;

/**
 * Created by admin on 2018/7/10.
 */

public class ShakeAnimation {

    public boolean mNeedShake = true; //是否要播放抖动动画，默认否
    private static final int ICON_WIDTH = 80;
    private static final int ICON_HEIGHT = 94;
    private static final float DEGREE_0 = 1.8f;
    private static final float DEGREE_1 = - 2.0f;
    private static final float DEGREE_2 = 2.0f;
    private static final float DEGREE_3 = - 1.5f;
    private static final float DEGREE_4 = 1.5f;
    private static final int ANIMATION_DURATION = 100;
    private int mCount = 0;
    private static final ShakeAnimation instance = new ShakeAnimation();
    private ShakeAnimation(){}
    public static final ShakeAnimation getInstance()
    {
        return instance;
    }

    // 晃动动画
    public void shakeAnimation(final View v)
    {
        DisplayMetrics dm = new DisplayMetrics();
        float mDensity = dm.density;
        float rotate;
        int c = mCount++ % 5;
        if (c == 0)
        {
            rotate = DEGREE_0;
        }
        else if (c == 1)
        {
            rotate = DEGREE_1;
        }
        else if (c == 2)
        {
            rotate = DEGREE_2;
        }
        else if (c == 3)
        {
            rotate = DEGREE_3;
        }
        else
        {
            rotate = DEGREE_4;
        }
        final RotateAnimation ra1 = new RotateAnimation(rotate, - rotate,
                ICON_WIDTH * mDensity / 2, ICON_HEIGHT * mDensity / 2);
        final RotateAnimation ra2 = new RotateAnimation(-rotate, rotate,
                ICON_WIDTH * mDensity / 2, ICON_HEIGHT * mDensity / 2);

        ra1.setDuration(ANIMATION_DURATION);
        ra2.setDuration(ANIMATION_DURATION);

        // 设置旋转动画的监听
        ra1.setAnimationListener(new Animation.AnimationListener()
        {
            @Override
            public void onAnimationEnd(Animation animation)
            {
                if (mNeedShake)
                {
                    ra1.reset(); // 重置动画
                    v.startAnimation(ra2); // 第一个动画结束开始第二个旋转动画
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation)
            {
            }

            @Override
            public void onAnimationStart(Animation animation)
            {
            }
        });

        ra2.setAnimationListener(new Animation.AnimationListener()
        {
            @Override
            public void onAnimationEnd(Animation animation)
            {
                if (mNeedShake)
                {
                    ra2.reset();
                    v.startAnimation(ra1);// 第二个动画结束开始第一个旋转动画
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation)
            {
            }

            @Override
            public void onAnimationStart(Animation animation)
            {
            }
        });
        v.startAnimation(ra1);
    }

}
