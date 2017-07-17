package com.beihui.market.ui.fragment;


import android.widget.EditText;

import com.beihui.market.R;
import com.beihui.market.base.BaseFragment;
import com.beihui.market.component.AppComponent;

import butterknife.BindView;

public class SetPsdFragment extends BaseFragment {
    @BindView(R.id.password)
    EditText passwordEt;

    @Override
    public int getLayoutResId() {
        return R.layout.fragment_set_psd;
    }

    @Override
    public void configViews() {

    }

    @Override
    public void initDatas() {

    }

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {

    }
}
