package com.beihui.market.ui.activity;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v7.widget.Toolbar;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.beihui.market.R;
import com.beihui.market.api.NetConstants;
import com.beihui.market.base.BaseComponentActivity;
import com.beihui.market.injection.component.AppComponent;
import com.beihui.market.util.viewutils.ToastUtils;
import com.gyf.barlibrary.ImmersionBar;

import java.lang.reflect.Field;

import app.u51.com.newnutsdk.NutSDK;
import app.u51.com.newnutsdk.model.MailSupportConfig;
import butterknife.BindView;

public class EmailLeadingInResultActivity extends BaseComponentActivity {

    @BindView(R.id.tool_bar)
    Toolbar toolbar;
    @BindView(R.id.web_view)
    WebView webView;

    /**
     * 如果导入失败，失败页面点击重新导入则需要改字段
     */
    private String emailSymbol;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //失败页面，点击重新导入，邮箱登录页面验证完成,开启进度界面
        if (requestCode == 1 && data == null) {
            Intent intent = new Intent(EmailLeadingInResultActivity.this, EmailLeadingInProgressActivity.class);
            intent.putExtra("email_symbol", emailSymbol);
            startActivity(intent);
        }
    }

    @Override
    public void onBackPressed() {
        navigateTabAccount();
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_bill_leading_in_result;
    }

    @SuppressLint("AddJavascriptInterface")
    @Override
    public void configViews() {
        ImmersionBar.with(this).titleBar(toolbar).statusBarDarkFont(true).init();
        setupToolbarBackNavigation(toolbar, R.mipmap.left_arrow_black);

        webView.setWebViewClient(new WebViewClient());
        webView.addJavascriptInterface(this, "android");
    }

    @Override
    public void initDatas() {
        emailSymbol = getIntent().getStringExtra("email_symbol");
        webView.loadUrl(getIntent().getBooleanExtra("success", false) ? NetConstants.H5_LEADING_IN_RESULT_SUCCESS : NetConstants.H5_LEADING_IN_RESULT_FAILED);
    }

    @Override
    protected void configureComponent(AppComponent appComponent) {

    }

    @JavascriptInterface
    public void navigateTabAccount() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(EmailLeadingInResultActivity.this, MainActivity.class);
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
                try {
                    Field field = NutSDK.getDefault().getClass().getDeclaredField("mailSupportConfig");
                    field.setAccessible(true);
                    MailSupportConfig config = (MailSupportConfig) field.get(NutSDK.getDefault());
                    if (config != null) {
                        Intent intent = new Intent(EmailLeadingInResultActivity.this, EmailLoginActivity.class);
                        intent.putExtra("extra_mail_config", config.getMailItemConfig(emailSymbol));
                        startActivityForResult(intent, 1);
                    } else {
                        ToastUtils.showShort(EmailLeadingInResultActivity.this, "正在获取配置", null);
                    }
                } catch (NoSuchFieldException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
