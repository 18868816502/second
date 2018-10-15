package com.beiwo.klyjaz.ui.activity;


import android.content.Intent;
import android.view.ViewGroup;

import com.beiwo.klyjaz.R;
import com.beiwo.klyjaz.base.BaseComponentActivity;
import com.beiwo.klyjaz.injection.component.AppComponent;

import butterknife.BindView;

public class SplashActivity extends BaseComponentActivity {

    @BindView(R.id.base_container)
    ViewGroup baseContainer;

    @Override
    public int getLayoutId() {
        return R.layout.activity_splash;
    }

    @Override
    public void configViews() {
    }

    @Override
    public void initDatas() {
        baseContainer.postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivityWithoutOverride(new Intent(SplashActivity.this, MainActivity.class));
                finish();
            }
        }, 1000);
    }

    @Override
    protected void configureComponent(AppComponent appComponent) {

    }


    @Override
    public void finish() {
        override = false;
        super.finish();
    }
}
