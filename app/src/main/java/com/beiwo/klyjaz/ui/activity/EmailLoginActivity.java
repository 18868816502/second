package com.beiwo.klyjaz.ui.activity;

import android.content.Intent;
import android.support.v7.widget.Toolbar;

import com.beiwo.klyjaz.R;
import com.beiwo.klyjaz.base.BaseComponentActivity;
import com.beiwo.klyjaz.helper.SlidePanelHelper;
import com.beiwo.klyjaz.injection.component.AppComponent;
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
