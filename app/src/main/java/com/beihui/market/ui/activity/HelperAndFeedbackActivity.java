package com.beihui.market.ui.activity;


import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.beihui.market.R;
import com.beihui.market.api.NetConstants;
import com.beihui.market.base.BaseComponentActivity;
import com.beihui.market.injection.component.AppComponent;

import butterknife.BindView;

public class HelperAndFeedbackActivity extends BaseComponentActivity implements View.OnClickListener {
    @BindView(R.id.tool_bar)
    Toolbar toolbar;
    @BindView(R.id.help)
    TextView helpTv;
    @BindView(R.id.feedback)
    TextView feedbackTv;
    @BindView(R.id.web_view)
    WebView webView;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    private View selected;

    @Override
    protected void onDestroy() {
        webView.destroy();
        super.onDestroy();
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_helper_and_feedback;
    }

    @Override
    public void configViews() {
        setupToolbar(toolbar);
        helpTv.setOnClickListener(this);
        feedbackTv.setOnClickListener(this);
        webView.setWebChromeClient(new ChromeClient());
        webView.setWebViewClient(new ViewClient());
        webView.getSettings().setJavaScriptEnabled(true);
    }

    @Override
    public void initDatas() {
        select(helpTv);
    }

    @Override
    protected void configureComponent(AppComponent appComponent) {

    }

    @Override
    public void onClick(View v) {
        if (v != selected) {
            select(v);
        }
    }

    private void select(View view) {
        if (selected != null) {
            selected.setSelected(false);
        }
        selected = view;
        if (selected != null) {
            selected.setSelected(true);
        }

        if (view == helpTv) {
            webView.loadUrl(NetConstants.H5_HELPER);
        } else {
            webView.loadUrl(NetConstants.H5_ADVICE);
        }
    }

    @JavascriptInterface
    void onSubmit() {

    }

    class ChromeClient extends WebChromeClient {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);
            if (progressBar.getVisibility() == View.GONE) {
                progressBar.setVisibility(View.VISIBLE);
            }
            progressBar.setProgress(newProgress);
            if (newProgress == 100) {
                progressBar.setVisibility(View.GONE);
            }
        }
    }

    class ViewClient extends WebViewClient {

    }
}
