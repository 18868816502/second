package com.beihui.market.ui.activity;


import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.beihui.market.R;
import com.beihui.market.api.Api;
import com.beihui.market.api.ResultEntity;
import com.beihui.market.base.BaseComponentActivity;
import com.beihui.market.entity.EBank;
import com.beihui.market.helper.SlidePanelHelper;
import com.beihui.market.helper.UserHelper;
import com.beihui.market.injection.component.AppComponent;
import com.beihui.market.util.RxUtil;
import com.beihui.market.util.viewutils.ToastUtils;

import butterknife.BindView;
import io.reactivex.functions.Consumer;

public class EBankActivity extends BaseComponentActivity {

    @BindView(R.id.web_view)
    WebView webView;

    @Override
    public int getLayoutId() {
        return R.layout.activity_ebank;
    }

    @Override
    public void configViews() {
        webView.setWebViewClient(new WebViewClient());
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
                                       webView.loadUrl(result.getData().getUrl());
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
    }

    @Override
    protected void configureComponent(AppComponent appComponent) {

    }
}
