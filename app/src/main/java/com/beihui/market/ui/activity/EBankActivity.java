package com.beihui.market.ui.activity;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.beihui.market.R;
import com.beihui.market.api.Api;
import com.beihui.market.api.ResultEntity;
import com.beihui.market.base.BaseComponentActivity;
import com.beihui.market.entity.EBank;
import com.beihui.market.helper.DataStatisticsHelper;
import com.beihui.market.helper.SlidePanelHelper;
import com.beihui.market.helper.UserHelper;
import com.beihui.market.injection.component.AppComponent;
import com.beihui.market.util.RxUtil;
import com.beihui.market.util.viewutils.ToastUtils;

import butterknife.BindView;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

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
        Disposable dis = Api.getInstance().fetchEBankUrl(UserHelper.getInstance(this).getProfile().getId())
                .compose(RxUtil.<ResultEntity<EBank>>io2main())
                .subscribe(new Consumer<ResultEntity<EBank>>() {
                               @Override
                               public void accept(ResultEntity<EBank> result) throws Exception {
                                   if (result.isSuccess()) {
                                       eBankUrl = result.getData().getUrl();
                                       webView.loadUrl(eBankUrl);
                                   } else {
                                       ToastUtils.showShort(EBankActivity.this, result.getMsg(), null);
                                   }
                               }
                           },
                        new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                Log.e("EBank", throwable.toString());
                                ToastUtils.showShort(EBankActivity.this, "请求出错", null);
                            }
                        });

        //pv，uv统计
        DataStatisticsHelper.getInstance().onCountUv(DataStatisticsHelper.ID_BILL_ENTER_EBANK_LEAD_IN);
    }

    @Override
    protected void configureComponent(AppComponent appComponent) {

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
