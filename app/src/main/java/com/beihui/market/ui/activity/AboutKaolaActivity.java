package com.beihui.market.ui.activity;

import android.support.v7.widget.Toolbar;

import com.beihui.market.R;
import com.beihui.market.base.BaseComponentActivity;
import com.beihui.market.helper.SlidePanelHelper;
import com.beihui.market.injection.component.AppComponent;
import com.gyf.barlibrary.ImmersionBar;

import butterknife.BindView;

public class AboutKaolaActivity extends BaseComponentActivity {

    @BindView(R.id.tool_bar)
    Toolbar toolbar;


    @Override
    public int getLayoutId() {
        return R.layout.activity_about_kaola;
    }

    @Override
    public void configViews() {
        setupToolbar(toolbar);
        ImmersionBar.with(this).statusBarDarkFont(true).init();

        SlidePanelHelper.attach(this);

    }

    @Override
    public void initDatas() {

    }

    @Override
    protected void configureComponent(AppComponent appComponent) {

    }
}
