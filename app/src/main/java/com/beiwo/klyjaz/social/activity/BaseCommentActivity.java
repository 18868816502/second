package com.beiwo.klyjaz.social.activity;

import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.beiwo.klyjaz.App;
import com.beiwo.klyjaz.ui.dialog.CommNoneAndroidLoading;
import com.beiwo.klyjaz.util.WeakRefToastUtil;
import com.gyf.barlibrary.ImmersionBar;

import butterknife.ButterKnife;

/**
 * @author chenguoguo
 * @name loanmarket_social
 * @class name：com.beiwo.klyjaz.social.activity
 * @descripe
 * @time 2018/11/5 10:40
 */
public abstract class BaseCommentActivity extends AppCompatActivity  {

    //进度条
    protected CommNoneAndroidLoading loading;

    @Override
    @CallSuper
    protected void onCreate(Bundle savedInstanceState) {
        /**
         * 注入App
         */
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        if (savedInstanceState != null) {
            FragmentManager manager = getSupportFragmentManager();
            manager.popBackStackImmediate(null, 1);
        }
        ButterKnife.bind(this);
        setBackgroundWindow();
        configViews();
        initDatas();
    }

    public abstract int getLayoutId();
    public abstract void configViews();
    public abstract void initDatas();

    /**
     * 设置弹窗背景宽度
     */
    private void setBackgroundWindow() {
        Window window = getWindow();
        WindowManager.LayoutParams wl = window.getAttributes();
        wl.x = 0;
        wl.y = getWindowManager().getDefaultDisplay().getHeight();
        // 以下这两句是为了保证按钮可以水平满屏
        wl.width = ViewGroup.LayoutParams.MATCH_PARENT;
        wl.height = ViewGroup.LayoutParams.MATCH_PARENT;
    }

    /**
     * hook BaseView.showProgress
     */
    public void showProgress() {
        showProgress(null);
    }

    /**
     * hook BaseView.dismissProgress
     */
    public void dismissProgress() {
        if (loading != null) {
            loading.dismiss();
        }
    }

    /**
     * hook BaseView.showErrorMsg(String).
     */
    public void showErrorMsg(String msg) {
        dismissProgress();
        WeakRefToastUtil.showShort(this, msg, null);
    }

    protected void showProgress(String msg) {
        if (loading == null) {
            loading = new CommNoneAndroidLoading(this, msg);
        }
        loading.show();
    }
}