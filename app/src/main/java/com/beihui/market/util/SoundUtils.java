package com.beihui.market.util;


import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;

import com.beihui.market.App;
import com.beihui.market.R;

public class SoundUtils {

    private static SoundUtils sInstance;

    private SoundPool soundPool;

    private int addSoundId;
    private int tabSoundId;

    private SoundUtils() {
        soundPool = new SoundPool(2, AudioManager.STREAM_SYSTEM, 0);
        Context context = App.getInstance();
        addSoundId = soundPool.load(context, R.raw.anniu_add_btn, 1);
        tabSoundId = soundPool.load(context, R.raw.anniu_home_icon, 1);
    }

    public static SoundUtils getInstance() {
        if (sInstance == null) {
            synchronized (SoundUtils.class) {
                if (sInstance == null) {
                    sInstance = new SoundUtils();
                }
            }
        }
        return sInstance;
    }

    public void playAdd() {
        soundPool.play(addSoundId, 0.5f, 0.5f, 0, 0, 1);
    }

    public void playTab() {
        soundPool.play(tabSoundId, 0.5f, 0.5f, 0, 0, 1);
    }
}
