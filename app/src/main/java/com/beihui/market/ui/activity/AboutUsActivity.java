package com.beihui.market.ui.activity;


import android.annotation.SuppressLint;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.beihui.market.R;
import com.beihui.market.base.BaseComponentActivity;
import com.beihui.market.component.AppComponent;

import butterknife.BindView;

public class AboutUsActivity extends BaseComponentActivity {
    @BindView(R.id.tool_bar)
    Toolbar toolbar;
    @BindView(R.id.version_name)
    TextView versionNameTv;


    @Override
    public int getLayoutId() {
        return R.layout.activity_about_us;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void configViews() {
        setupToolbar(toolbar);
        try {
            PackageInfo info = getPackageManager().getPackageInfo(getPackageName(), 0);
            versionNameTv.setText("v" + info.versionName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initDatas() {

    }

    @Override
    protected void configureComponent(AppComponent appComponent) {

    }
}
