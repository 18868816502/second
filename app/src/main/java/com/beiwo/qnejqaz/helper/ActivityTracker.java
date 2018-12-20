package com.beiwo.qnejqaz.helper;


import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.text.TextUtils;

import com.beiwo.qnejqaz.loan.ProTypeActivity;
import com.beiwo.qnejqaz.ui.activity.MainActivity;
import com.beiwo.qnejqaz.util.SPUtils;

import java.util.LinkedList;

public class ActivityTracker implements Application.ActivityLifecycleCallbacks {
    private static ActivityTracker sInstance;
    private LinkedList<Activity> activities;
    private int actSize;

    private ActivityTracker() {
    }

    public static ActivityTracker getInstance() {
        if (sInstance == null) {
            synchronized (ActivityTracker.class) {
                if (sInstance == null) {
                    sInstance = new ActivityTracker();
                }
            }
        }
        return sInstance;
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        if (activities == null) activities = new LinkedList<>();
        activities.add(activity);
    }

    @Override
    public void onActivityStarted(Activity activity) {
        actSize++;
    }

    @Override
    public void onActivityResumed(Activity activity) {
        //use this lifecycle callback to maintain activity active link since activity'destroy may not response
        //immediately, producing wrong last activity exception as it id not removed as quickly as we excepted
        int index = activities.indexOf(activity);
        for (int i = activities.size() - 1; i > index; --i) {
            activities.remove(i);
        }
    }

    @Override
    public void onActivityPaused(Activity activity) {
    }

    @Override
    public void onActivityStopped(Activity activity) {
        --actSize;
        if (actSize <= 0) saveLastName();
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        if (activities != null && activities.contains(activity)) {
            activities.remove(activity);
        }
    }

    public Activity getLastActivity() {
        if (activities != null && activities.size() >= 2) {
            return activities.get(activities.size() - 2);
        }
        return null;
    }

    public Activity getTopActivity() {
        if (activities != null && activities.size() > 0) {
            return activities.get(activities.size() - 1);
        }
        return null;
    }

    public void removeTrackImmediately(Activity activity) {
        if (activities != null && activities.contains(activity)) {
            activities.remove(activity);
        }
    }

    private void saveLastName() {
        try {
            String name = getTopActivity().getClass().getSimpleName();
            if (TextUtils.equals("MainActivity", name)) {
                MainActivity activity = (MainActivity) ActivityTracker.getInstance().getTopActivity();
                String fragmentName = activity.currentFragment.getClass().getSimpleName();
                //System.out.println("frg = " + fragmentName);
                name = fragmentName;
            } else if (TextUtils.equals("ProTypeActivity", name)) {
                ProTypeActivity activity = (ProTypeActivity) ActivityTracker.getInstance().getTopActivity();
                int type = activity.productType;
                name = "ProTypeActivity" + type;
            }
            SPUtils.setLastActName(name);
            //System.out.println("last = " + name);
        } catch (Exception e) {
        }
    }
}