package com.beiwo.qnejqaz.ui.activity;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.beiwo.qnejqaz.R;
import com.beiwo.qnejqaz.api.NetConstants;
import com.beiwo.qnejqaz.base.BaseComponentActivity;
import com.beiwo.qnejqaz.helper.SlidePanelHelper;
import com.beiwo.qnejqaz.tang.RoundCornerTransformation;
import com.bumptech.glide.Glide;
import com.gyf.barlibrary.ImmersionBar;

import butterknife.BindView;
import butterknife.OnClick;


public class AboutUsActivity extends BaseComponentActivity {
    @BindView(R.id.tool_bar)
    Toolbar toolbar;
    //版本名称
    @BindView(R.id.version_name)
    TextView versionNameTv;
    @BindView(R.id.tv_about_us)
    TextView tv_about_us;
    @BindView(R.id.iv_logo)
    ImageView iv_logo;

    @Override
    public int getLayoutId() {
        return R.layout.activity_about_us;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void configViews() {
        setupToolbar(toolbar);
        ImmersionBar.with(this).statusBarDarkFont(true).init();
        try {
            PackageInfo info = getPackageManager().getPackageInfo(getPackageName(), 0);
            versionNameTv.setText("v" + info.versionName);
        } catch (PackageManager.NameNotFoundException e) {
        }
        SlidePanelHelper.attach(this);
        tv_about_us.setText(String.format(getString(R.string.about_app), getString(R.string.app_name)));
        Glide.with(this).load(R.mipmap.ic_launcher)
                .bitmapTransform(new RoundCornerTransformation(this, 8, RoundCornerTransformation.CornerType.ALL)).into(iv_logo);
    }

    @Override
    public void initDatas() {
    }

    @OnClick({R.id.welcome, R.id.get_know_us, R.id.user_agreement})
    void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.welcome:
                Intent toWelCome = new Intent(this, WelcomeActivity.class);
                toWelCome.putExtra("fromAboutUs", true);
                startActivity(toWelCome);
                break;
            case R.id.get_know_us:
            case R.id.user_agreement:
                Intent intent = new Intent(this, ComWebViewActivity.class);
                String title = view.getId() == R.id.get_know_us ? "了解" + getString(R.string.app_name) : "用户协议";
                String url = view.getId() == R.id.get_know_us ? NetConstants.H5_ABOUT_US : NetConstants.H5_USER_AGREEMENT;
                intent.putExtra("title", title);
                intent.putExtra("url", url);
                startActivity(intent);
                break;
        }
    }
}
