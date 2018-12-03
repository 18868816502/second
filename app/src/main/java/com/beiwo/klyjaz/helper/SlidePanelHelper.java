package com.beiwo.klyjaz.helper;


import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewGroup;

import com.beiwo.klyjaz.R;
import com.beiwo.klyjaz.view.slidepanel.SlidePanel;

public class SlidePanelHelper {

    public static SlidePanel attach(@NonNull Activity activity) {
        final Activity curActivity = activity;
        ViewGroup decorView = (ViewGroup) activity.getWindow().getDecorView();
        View contentView = decorView.getChildAt(0);
        contentView.setBackground(ContextCompat.getDrawable(activity, R.color.white));
        decorView.removeView(contentView);

        Activity cacheActivity = ActivityTracker.getInstance().getLastActivity();
        View cacheView = cacheActivity != null ? cacheActivity.getWindow().getDecorView() : null;

        SlidePanel slidePanel = new SlidePanel(activity, contentView, cacheView, new SlidePanel.SlideCallback() {
            @Override
            public void onSlideStart() {
            }

            @Override
            public void onSlide(int left, int dx) {
            }

            @Override
            public void onSlideEnd() {
                curActivity.finish();
                curActivity.overridePendingTransition(R.anim.no_anim, R.anim.no_anim);

                ActivityTracker.getInstance().removeTrackImmediately(curActivity);
            }

            @Override
            public void onSlideReset() {
            }
        });
        decorView.addView(slidePanel);
        return slidePanel;
    }
}