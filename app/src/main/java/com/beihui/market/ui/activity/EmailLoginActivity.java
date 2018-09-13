package com.beihui.market.ui.activity;

import android.content.Intent;
import android.support.v7.widget.Toolbar;

import com.beihui.market.R;
import com.beihui.market.base.BaseComponentActivity;
import com.beihui.market.helper.SlidePanelHelper;
import com.beihui.market.injection.component.AppComponent;
import com.gyf.barlibrary.ImmersionBar;

import app.u51.com.newnutsdk.NutWebViewFragment;
import butterknife.BindView;


public class EmailLoginActivity extends BaseComponentActivity {

    @BindView(R.id.tool_bar)
    Toolbar toolbar;

    @Override
    public int getLayoutId() {
        return R.layout.activity_email_login;
    }

    @Override
    public void configViews() {
        ImmersionBar.with(this).titleBar(toolbar).statusBarDarkFont(true).init();
        setupToolbarBackNavigation(toolbar, R.mipmap.left_arrow_black);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.content_container, NutWebViewFragment.a(getIntent().getExtras()))
                .commitAllowingStateLoss();

        SlidePanelHelper.attach(this);
    }

    @Override
    public void initDatas() {

    }

    @Override
    protected void configureComponent(AppComponent appComponent) {

    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_OK, new Intent());
        super.onBackPressed();
    }
}
