package com.beihui.market.ui.activity;

import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;

import com.beihui.market.R;
import com.beihui.market.api.NetConstants;
import com.beihui.market.base.BaseComponentActivity;
import com.beihui.market.injection.component.AppComponent;
import com.beihui.market.ui.dialog.ShareDialog;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;

import butterknife.BindView;


public class WorthTestActivity extends BaseComponentActivity {

    @BindView(R.id.tool_bar)
    Toolbar toolbar;
    @BindView(R.id.web_view)
    WebView webView;

    @Override
    protected void onDestroy() {
        webView.getSettings().setJavaScriptEnabled(false);
        webView.destroy();
        webView = null;
        super.onDestroy();
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_worth_test;
    }

    @Override
    public void configViews() {
        setupToolbar(toolbar);
        webView.getSettings().setJavaScriptEnabled(true);
    }

    @Override
    public void initDatas() {
        webView.loadUrl(NetConstants.H5_TEST);
    }

    @Override
    protected void configureComponent(AppComponent appComponent) {
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.worth_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        UMWeb web = new UMWeb(NetConstants.H5_TEST);
        UMImage image = new UMImage(this, R.mipmap.ic_launcher);
        web.setTitle("测测应急时你能凑到多少钱？");
        web.setDescription("快来测试一下，看看自己应急的时候能贷多少钱。");
        web.setThumb(image);
        new ShareDialog()
                .setUmWeb(web)
                .show(getSupportFragmentManager(), ShareDialog.class.getSimpleName());
        return true;
    }
}
