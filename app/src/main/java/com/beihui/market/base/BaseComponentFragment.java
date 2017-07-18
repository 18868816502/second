package com.beihui.market.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.beihui.market.App;
import com.beihui.market.component.AppComponent;

import java.util.regex.Pattern;

import butterknife.ButterKnife;
import butterknife.Unbinder;

public abstract class BaseComponentFragment extends Fragment {

    private Unbinder unbinder;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        configureComponent(App.getInstance().getAppComponent());
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
    protected abstract void configureComponent(AppComponent appComponent);


    public boolean matchPhone(String text) {
        if (Pattern.compile("(\\d{11})|(\\+\\d{3,})").matcher(text).matches()) {
            return true;
        }
        return false;
    }
}
