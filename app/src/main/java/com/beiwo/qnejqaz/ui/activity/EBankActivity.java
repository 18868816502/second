package com.beiwo.qnejqaz.ui.activity;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.webkit.JavascriptInterface;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.beiwo.qnejqaz.R;
import com.beiwo.qnejqaz.api.Api;
import com.beiwo.qnejqaz.api.ResultEntity;
import com.beiwo.qnejqaz.base.BaseComponentActivity;
import com.beiwo.qnejqaz.entity.EBank;
import com.beiwo.qnejqaz.helper.SlidePanelHelper;
import com.beiwo.qnejqaz.helper.UserHelper;
import com.beiwo.qnejqaz.util.RxUtil;
import com.beiwo.qnejqaz.util.WeakRefToastUtil;
import com.gyf.barlibrary.ImmersionBar;

import butterknife.BindView;
import io.reactivex.functions.Consumer;

/**
 * @author xhb
 * 网银导入页面 webView页面
 */
public class EBankActivity extends BaseComponentActivity {

    @BindView(R.id.web_view)
    WebView webView;

    @Override
    public int getLayoutId() {
        return R.layout.activity_ebank;
    }

    private String eBankUrl;

    @SuppressLint({"JavascriptInterface", "AddJavascriptInterface"})
    @Override
    public void configViews() {
        ImmersionBar.with(this).statusBarDarkFont(true).init();

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                return super.shouldOverrideUrlLoading(view, request);
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                if (url.contains("status=back")) {
                    onBackPressed();
                } else {
                    super.onPageStarted(view, url, favicon);
                }
            }
        });
        webView.addJavascriptInterface(this, "android");


        SlidePanelHelper.attach(this);
    }

    @Override
    public void initDatas() {
        Api.getInstance().fetchEBankUrl(UserHelper.getInstance(this).getProfile().getId())
                .compose(RxUtil.<ResultEntity<EBank>>io2main())
                .subscribe(new Consumer<ResultEntity<EBank>>() {
                               @Override
                               public void accept(ResultEntity<EBank> result) throws Exception {
                                   if (result.isSuccess()) {
                                       eBankUrl = result.getData().getUrl();
                                       webView.loadUrl(eBankUrl);
                                   } else {
                                       WeakRefToastUtil.showShort(EBankActivity.this, result.getMsg(), null);
                                   }
                               }
                           });
    }

    @JavascriptInterface
    public void navigateTabAccount() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(EBankActivity.this, MainActivity.class);
                intent.putExtra("account", true);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);

                finish();
            }
        });
    }

    @JavascriptInterface
    public void reLeadingIn() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                webView.clearHistory();
                webView.loadUrl(eBankUrl);
            }
        });
    }
}
