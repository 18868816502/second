package com.beihui.market.ui.activity;


import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.beihui.market.R;
import com.beihui.market.base.BaseComponentActivity;
import com.beihui.market.component.AppComponent;
import com.beihui.market.ui.dialog.CommNoneAndroidDialog;

import butterknife.BindView;
import butterknife.OnClick;

public class SettingsActivity extends BaseComponentActivity {

    @BindView(R.id.tool_bar)
    Toolbar toolbar;
    @BindView(R.id.version_name)
    TextView versionNameTv;

    @Override
    public int getLayoutId() {
        return R.layout.activity_settings;
    }

    @Override
    public void configViews() {
        setupToolbar(toolbar);
    }

    @Override
    public void initDatas() {

    }

    @Override
    protected void configureComponent(AppComponent appComponent) {

    }

    @OnClick({R.id.change_psd, R.id.star_me, R.id.about_us, R.id.exit})
    void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.change_psd:
                Intent toChangePsd = new Intent(this, ChangePsdActivity.class);
                startActivity(toChangePsd);
                break;
            case R.id.star_me:
                try {
                    Intent toMarket = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + getApplicationInfo().packageName));
                    startActivity(toMarket);
                } catch (ActivityNotFoundException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.about_us:
                Intent toAboutUs = new Intent(this, AboutUsActivity.class);
                startActivity(toAboutUs);
                break;
            case R.id.exit:
                new CommNoneAndroidDialog().withMessage("确认退出爱信管家")
                        .withPositiveBtn("在看看", null)
                        .withNegativeBtn("退出", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                            }
                        }).show(getSupportFragmentManager(), CommNoneAndroidDialog.class.getSimpleName());
                break;
        }
    }
}

