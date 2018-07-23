package com.beihui.market.ui.activity;

import android.graphics.Color;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.beihui.market.R;
import com.beihui.market.base.BaseComponentActivity;
import com.beihui.market.helper.SlidePanelHelper;
import com.beihui.market.injection.component.AppComponent;
import com.beihui.market.injection.component.DaggerRemindComponent;
import com.beihui.market.injection.module.RemindModule;
import com.beihui.market.ui.contract.RemindContract;
import com.beihui.market.ui.presenter.RemindPresenter;
import com.beihui.market.util.CommonUtils;
import com.beihui.market.util.FastClickUtils;
import com.beihui.market.view.pickerview.OptionsPickerView;
import com.gyf.barlibrary.ImmersionBar;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;

/**
 * Copyright: kaola (C)2018
 * FileName: RemindActivity
 * Author: jiang
 * Create on: 2018/7/18 14:50
 * Description: 提醒界面
 */
public class RemindActivity extends BaseComponentActivity implements RemindContract.View {

    @BindView(R.id.tool_bar)
    Toolbar toolbar;

    @BindView(R.id.select_time_tv)
    TextView selectTv;

    @BindView(R.id.push_switch)
    Switch pushSwitch;
    @BindView(R.id.message_switch)
    Switch messageSwitch;

    @Inject
    RemindPresenter presenter;

    private int pushRemind;
    private int messageRemind;

    @Override
    public int getLayoutId() {
        return R.layout.activity_remind_layout;
    }

    @Override
    public void configViews() {
        ImmersionBar.with(this).statusBarDarkFont(true).init();
        setupToolbar(toolbar);
        SlidePanelHelper.attach(this);

    }

    @Override
    public void initDatas() {
        presenter.onStart();
        pushSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    pushRemind = 1;
                    Toast.makeText(RemindActivity.this, "个推打开", Toast.LENGTH_SHORT).show();
                } else {
                    pushRemind = 0;
                }

            }
        });

        messageSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    messageRemind = 1;
                    Toast.makeText(RemindActivity.this, "信息推打开", Toast.LENGTH_SHORT).show();
                } else {
                    messageRemind = 0;
                }

            }
        });

    }

    @Override
    protected void configureComponent(AppComponent appComponent) {
        DaggerRemindComponent.builder()
                .appComponent(appComponent)
                .remindModule(new RemindModule(this))
                .build()
                .inject(this);

    }

    @Override
    public void showRepaymentTime() {
        final List<String> list = new ArrayList<>();
        String day;
        for (int i = 0; i < 10; i++) {
            day = CommonUtils.getDay(i);
            list.add(day);
        }

        OptionsPickerView optionsPickerView = new OptionsPickerView.Builder(this, new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                selectTv.setText(list.get(options1));

            }
        }).setTitleText("还款提醒").setCancelColor(getResources().getColor(R.color.pickerview_cancle))
                .setSubmitColor(getResources().getColor(R.color.pickerview_submit))
                .setTitleColor(getResources().getColor(R.color.pickerview_title)).setTitleBgColor(Color.WHITE).setLineSpacingMultiplier(2f)
                .setSelectOptions(5)
                .build();

        optionsPickerView.setPicker(list);
        optionsPickerView.show();


    }

    @Override
    public void selectPushRemindType() {

    }

    @Override
    public void selectmessageRemindType() {

    }

    @Override
    public void setPresenter(RemindContract.Presenter presenter) {

    }

    @OnClick({R.id.time_layout})
    void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.time_layout:
                if (!FastClickUtils.isFastClick()) {
                    presenter.repaymentTime();
                }

                break;

        }
    }


}
