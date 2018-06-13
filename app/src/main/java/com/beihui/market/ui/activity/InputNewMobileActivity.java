package com.beihui.market.ui.activity;

import android.graphics.Color;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.TextView;

import com.beihui.market.R;
import com.beihui.market.base.BaseComponentActivity;
import com.beihui.market.injection.component.AppComponent;
import com.beihui.market.util.LegalInputUtils;
import com.beihui.market.view.ClearEditText;
import com.gyf.barlibrary.ImmersionBar;

import butterknife.BindView;

/**
 * Created by admin on 2018/6/12.
 * 输入新手机号码
 */

public class InputNewMobileActivity extends BaseComponentActivity {

    @BindView(R.id.tool_bar)
    Toolbar toolbar;
    @BindView(R.id.cel_new_phone_number)
    ClearEditText phoneNumber;
    @BindView(R.id.tv_new_phone_update)
    TextView next;


    @Override
    public int getLayoutId() {
        return R.layout.x_activity_input_new_mobile;
    }

    @Override
    public void configViews() {
        ImmersionBar.with(this).statusBarDarkFont(true).init();
        setupToolbar(toolbar);
    }

    @Override
    public void initDatas() {
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
                    UserCertificationCodeActivity.launch(InputNewMobileActivity.this, phoneNumber.getText().toString());
                }
            }
        });
    }

    /**
     * 验证按钮是否可以点击
     */
    private void validation(){
        String phone = phoneNumber.getText().toString();
        if (LegalInputUtils.validatePhone(phone)) {
            next.setClickable(true);
            next.setTextColor(Color.WHITE);
        } else {
            next.setClickable(false);
            next.setTextColor(Color.parseColor("#50ffffff"));
        }
    }

    @Override
    protected void configureComponent(AppComponent appComponent) {

    }
}
