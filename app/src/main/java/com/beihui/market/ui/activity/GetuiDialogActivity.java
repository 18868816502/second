package com.beihui.market.ui.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.beihui.market.R;
import com.beihui.market.base.BaseComponentActivity;
import com.beihui.market.entity.GetuiBean;
import com.beihui.market.injection.component.AppComponent;
import com.beihui.market.util.CommonUtils;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import butterknife.BindView;

/**
 * Copyright: zhujia (C)2018
 * FileName: GetuiDialogActivity
 * Author: jiang
 * Create on: 2018/7/26 10:35
 * Description: 彈框
 */
public class GetuiDialogActivity extends Activity {
    private TextView okTv;
    private Gson gosn = new Gson();
    private ImageView img;
    private TextView title;
    private TextView total;
    private TextView billname;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tankuang_layout);
        img = findViewById(R.id.bill_img);
        title = findViewById(R.id.title_tv);
        total = findViewById(R.id.dialog_total_tv);
        billname = findViewById(R.id.bill_name);
        if (getIntent() != null) {
            String json = getIntent().getStringExtra("pending_json");
            GetuiBean getuiBean = gosn.fromJson(json, GetuiBean.class);
            if (getuiBean != null && getuiBean.getData() != null) {
                Glide.with(GetuiDialogActivity.this).load(getuiBean.getData().getLogo()).asBitmap().into(img);
                title.setText("有一笔账单" + getuiBean.getData().getTitle());
                billname.setText(getuiBean.getData().getName());
                total.setText(CommonUtils.numToString(getuiBean.getData().getAmount()));
            }
        }
        okTv = findViewById(R.id.submmit_tv);
        okTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }


}
