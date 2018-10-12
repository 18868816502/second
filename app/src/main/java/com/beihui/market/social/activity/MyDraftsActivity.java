package com.beihui.market.social.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.beihui.market.R;
import com.beihui.market.base.BaseComponentActivity;
import com.beihui.market.helper.SlidePanelHelper;
import com.beihui.market.injection.component.AppComponent;
import com.gyf.barlibrary.ImmersionBar;

import butterknife.BindView;

/**
 * 我的草稿箱
 */
public class MyDraftsActivity extends BaseComponentActivity {

    @BindView(R.id.tool_bar)
    Toolbar toolbar;

    @Override
    public int getLayoutId() {
        return R.layout.activity_my_drafts;
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
