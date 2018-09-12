package com.beihui.market.view;


import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.webkit.WebView;

public class BusinessWebView extends WebView {

    public BusinessWebView(Context context) {
        super(context);
        init();
    }

    public BusinessWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }


    public BusinessWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void init() {

//        WebSettings settings = getSettings();
//        settings.setJavaScriptEnabled(true);
//        settings.setDomStorageEnabled(true);
//        settings.setSupportZoom(true);
//        settings.setBuiltInZoomControls(true);
//        settings.setDisplayZoomControls(false);
//        settings.setUseWideViewPort(true);
//        settings.setLoadWithOverviewMode(true);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            settings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
//        }
    }
}
