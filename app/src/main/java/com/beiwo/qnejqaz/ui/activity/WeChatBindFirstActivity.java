package com.beiwo.qnejqaz.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.TextView;

import com.beiwo.qnejqaz.R;
import com.beiwo.qnejqaz.base.BaseComponentActivity;
import com.beiwo.qnejqaz.util.CommonUtils;
import com.beiwo.qnejqaz.view.ClearEditText;
import com.gyf.barlibrary.ImmersionBar;

import butterknife.BindView;

/**
 * Copyright: zhujia (C)2018
 * FileName: WeChatBindFirstActivity
 * Author: jiang
 * Create on: 2018/7/31 10:11
 * Description: 微信绑定手机号
 */
public class WeChatBindFirstActivity extends BaseComponentActivity {

    @BindView(R.id.tool_bar)
    Toolbar toolbar;
    @BindView(R.id.wechat_phone_edit)
    ClearEditText phoneNumber;
    @BindView(R.id.tv_wechat_bind_next)
    TextView next;

    private String wxOpenId;
    private String wxName;
    private String wxImage;

    @Override
    public int getLayoutId() {
        return R.layout.activity_wechat_bind_phone_first_layout;
    }

    @Override
    public void configViews() {

        ImmersionBar.with(this).statusBarDarkFont(true).init();
        setupToolbar(toolbar);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            setResult(RESULT_OK);
            finish();
        }
    }

    @Override
    public void initDatas() {
        wxOpenId = getIntent().getStringExtra("openId");
        wxName = getIntent().getStringExtra("name");
        wxImage = getIntent().getStringExtra("profile_image_url");
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                validation();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        };
        phoneNumber.addTextChangedListener(textWatcher);

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(phoneNumber.getText().toString())) {
                    Intent intent = new Intent(WeChatBindFirstActivity.this, WechatBindDoneActivity.class);
                    intent.putExtra("openId", wxOpenId);
                    intent.putExtra("name", wxName);
                    intent.putExtra("profile_image_url", wxImage);
                    intent.putExtra("phone", phoneNumber.getText().toString());
                    startActivityForResult(intent, 1);
                }
            }
        });

    }

    /**
     * 验证按钮是否可以点击
     */
    private void validation() {
        String phone = phoneNumber.getText().toString();
        if (CommonUtils.isMobileNO(phone)) {
            next.setClickable(true);
            next.setTextColor(Color.WHITE);
        } else {
            next.setClickable(false);
            next.setTextColor(Color.parseColor("#50ffffff"));
        }
    }
}
