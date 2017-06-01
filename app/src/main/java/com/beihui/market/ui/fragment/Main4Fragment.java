package com.beihui.market.ui.fragment;

import android.os.Bundle;

import com.beihui.market.R;
import com.beihui.market.base.BaseRVFragment;
import com.beihui.market.component.AppComponent;
import com.beihui.market.component.DaggerMainComponent;
import com.beihui.market.ui.contract.Main1Contract;
import com.beihui.market.ui.presenter.Main1Presenter;
import com.gyf.barlibrary.ImmersionBar;


/**
 * Created by Administrator on 2017/1/22.
 * 办事中心页面
 */

public class Main4Fragment extends BaseRVFragment<Main1Presenter> implements Main1Contract.View{



    public static Main4Fragment newInstance() {
        Main4Fragment f = new Main4Fragment();
        Bundle b = new Bundle();
        b.putString("type", "Main2Fragment");
        f.setArguments(b);
        return f;
    }

    @Override
    public int getLayoutResId() {
        return R.layout.fragment_main4;
    }

    @Override
    protected void immersionInit() {

    }

    @Override
    public void configViews() {

    }

    @Override
    public void initDatas() {

    }

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {
        DaggerMainComponent.builder().appComponent(appComponent).build().inject(this);
    }

    @Override
    public void showError(String err) {
          dismissDialog();
    }

    @Override
    public void complete() {
       dismissDialog();
    }



}
