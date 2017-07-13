package com.beihui.market.ui.activity;

import android.content.Intent;
import android.support.v7.widget.Toolbar;
import android.widget.EditText;

import com.beihui.market.R;
import com.beihui.market.base.BaseActivity;
import com.beihui.market.component.AppComponent;
import com.gyf.barlibrary.ImmersionBar;

import butterknife.BindView;


public class EditNickNameActivity extends BaseActivity {
    @BindView(R.id.tool_bar)
    Toolbar toolbar;
    @BindView(R.id.edit_text)
    EditText editText;

    @Override
    public int getLayoutId() {
        return R.layout.activity_edit_nick_name;
    }

    @Override
    public void configViews() {
        setupToolbar(toolbar);
    }

    @Override
    public void initDatas() {

    }

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {

    }
}
