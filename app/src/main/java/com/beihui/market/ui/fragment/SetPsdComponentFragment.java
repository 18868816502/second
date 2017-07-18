package com.beihui.market.ui.fragment;


import android.widget.EditText;

import com.beihui.market.R;
import com.beihui.market.base.BaseComponentFragment;
import com.beihui.market.component.AppComponent;

import butterknife.BindView;

public class SetPsdComponentFragment extends BaseComponentFragment {
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
    protected void configureComponent(AppComponent appComponent) {

    }
}
