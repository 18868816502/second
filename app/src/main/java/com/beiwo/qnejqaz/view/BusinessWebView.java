package com.beiwo.qnejqaz.view;


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
    }
}