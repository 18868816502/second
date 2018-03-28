package com.beihui.market.ui.activity;


import android.content.Intent;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.beihui.market.R;
import com.beihui.market.base.BaseComponentActivity;
import com.beihui.market.helper.SlidePanelHelper;
import com.beihui.market.injection.component.AppComponent;
import com.beihui.market.view.RoundProgressBar;
import com.gyf.barlibrary.ImmersionBar;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import app.u51.com.newnutsdk.net.msg.CrawlerStatusMessage;
import butterknife.BindView;
import butterknife.OnClick;

public class EmailLeadingInProgressActivity extends BaseComponentActivity {

    @BindView(R.id.tool_bar)
    Toolbar toolbar;
    @BindView(R.id.round_progress_bar)
    RoundProgressBar roundProgressBar;
    @BindView(R.id.loading_text)
    TextView loadingText;

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("account", true);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);

        finish();
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_email_leading_in_progress;
    }

    @Override
    public void configViews() {
        ImmersionBar.with(this).titleBar(toolbar).statusBarDarkFont(true).init();
        setupToolbarBackNavigation(toolbar, R.mipmap.left_arrow_black);

        SlidePanelHelper.attach(this);

        EventBus.getDefault().register(this);
    }

    @Override
    public void initDatas() {

    }

    @Override
    protected void configureComponent(AppComponent appComponent) {

    }

    @OnClick(R.id.leave)
    void onItemClicked() {
        onBackPressed();
    }


    @Subscribe
    public void onLeadingInEvent(CrawlerStatusMessage msg) {
        roundProgressBar.setProgress(msg.progress);
        if (msg.progress == 100) {
            loadingText.setVisibility(View.GONE);
        }
    }
}
