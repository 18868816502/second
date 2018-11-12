package com.beiwo.klyjaz.base;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.beiwo.klyjaz.R;

public class BaseActivity extends AppCompatActivity {
    protected boolean override = true;

    @Override
    public void startActivityForResult(Intent intent, int requestCode, @Nullable Bundle options) {
        super.startActivityForResult(intent, requestCode, options);
        if (override) {
            overridePendingTransition(R.anim.slide_right_to_left, R.anim.fade_still);
        }
        //override = true;
    }

    @Override
    public void finish() {
        super.finish();
        if (override) {
            overridePendingTransition(R.anim.no_anim, R.anim.slide_left_to_right);
        } else {
            overridePendingTransition(R.anim.no_anim, R.anim.no_anim);
        }
    }

    public void startActivityWithoutOverride(Intent intent) {
        override = false;
        startActivity(intent);
    }
}