package com.beihui.market.ui.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.beihui.market.R;
import com.beihui.market.base.BaseComponentActivity;
import com.beihui.market.injection.component.AppComponent;

import butterknife.BindView;

/**
 * Copyright: zhujia (C)2018
 * FileName: GetuiDialogActivity
 * Author: jiang
 * Create on: 2018/7/26 10:35
 * Description: 彈框
 */
public class GetuiDialogActivity extends Activity {
    @BindView(R.id.submmit_tv)
    TextView okTv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tankuang_layout);
    }


}
