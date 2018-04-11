package com.beihui.market.ui.activity;


import android.content.Intent;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.beihui.market.R;
import com.beihui.market.base.BaseComponentActivity;
import com.beihui.market.helper.NutEmailLeadInListener;
import com.beihui.market.helper.SlidePanelHelper;
import com.beihui.market.injection.component.AppComponent;
import com.beihui.market.view.RoundProgressBar;
import com.gyf.barlibrary.ImmersionBar;

import butterknife.BindView;
import butterknife.OnClick;

public class EmailLeadingInProgressActivity extends BaseComponentActivity implements NutEmailLeadInListener.OnLeadInProgressListener {

    @BindView(R.id.tool_bar)
    Toolbar toolbar;
    @BindView(R.id.round_progress_bar)
    RoundProgressBar roundProgressBar;
    @BindView(R.id.loading_text)
    TextView loadingText;


    @Override
    protected void onDestroy() {
        NutEmailLeadInListener.getInstance().removeInLeadInProgressListener(this);
        super.onDestroy();
    }

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

        NutEmailLeadInListener.getInstance().addOnLeadInProgressListener(this);
        NutEmailLeadInListener.getInstance().syncCurrentTask();
    }

    @Override
    public void initDatas() {

    }

    @Override
    protected void configureComponent(AppComponent appComponent) {

    }

    @OnClick({R.id.leave, R.id.help_feedback})
    void onItemClicked(View view) {
        switch (view.getId()) {
            case R.id.leave:
                onBackPressed();
                break;
            case R.id.help_feedback:
                startActivity(new Intent(this, HelpAndFeedActivity.class));
                break;
        }
    }


    @Override
    public void onProgressChanged(int progress) {
        roundProgressBar.setProgress(progress);
    }

    @Override
    public void onLeadInFinished(boolean success) {
        Intent intent = new Intent(this, EmailLeadingInResultActivity.class);
        intent.putExtra("success", success);
        intent.putExtra("email_symbol", getIntent().getStringExtra("email_symbol"));
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);

        finish();
    }
}
