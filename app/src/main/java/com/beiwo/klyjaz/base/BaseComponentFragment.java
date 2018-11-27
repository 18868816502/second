package com.beiwo.klyjaz.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.beiwo.klyjaz.ui.dialog.CommNoneAndroidLoading;
import com.beiwo.klyjaz.umeng.Statistic;
import com.beiwo.klyjaz.util.InputMethodUtil;
import com.beiwo.klyjaz.util.WeakRefToastUtil;

import butterknife.ButterKnife;
import butterknife.Unbinder;

public abstract class BaseComponentFragment extends Fragment {
    private Unbinder unbinder;
    private CommNoneAndroidLoading loading;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
//        configureComponent(App.getInstance().getAppComponent());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle state) {
        return inflater.inflate(getLayoutResId(), container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        unbinder = ButterKnife.bind(this, view);
        configViews();
        initDatas();
    }

    @Override
    public void onResume() {
        super.onResume();
        Statistic.onPageStart(getClass().getSimpleName());
    }

    @Override
    public void onPause() {
        super.onPause();
        Statistic.onPageEnd(getClass().getSimpleName());
        InputMethodUtil.closeSoftKeyboard(getActivity());
    }

    @Override
    public void onDestroyView() {
        if (unbinder != null) {
            unbinder.unbind();
        }
        super.onDestroyView();
    }

    @LayoutRes
    public abstract int getLayoutResId();

    /**
     * set view init state
     */
    public abstract void configViews();

    /**
     * set view init data or fetch init data
     */
    public abstract void initDatas();

    /**
     * set up injection
     */
//    protected abstract void configureComponent(AppComponent appComponent);

    protected void showProgress(String msg) {
        if (loading == null) {
            loading = new CommNoneAndroidLoading(getContext(), msg);
        }
        loading.show();
    }

    /**
     * hook BaseView.dismissProgress().
     */
    public void dismissProgress() {
        if (loading != null) {
            loading.dismiss();
        }
    }

    /**
     * hook BaseView.showProgress().
     */
    public void showProgress() {
        showProgress(null);
    }

    /**
     * hook BaseView.showErrorMsg(String).
     */
    public void showErrorMsg(String msg) {
        dismissProgress();
        WeakRefToastUtil.showShort(getContext(), msg, null);
    }
}