package com.beihui.market.ui.activity;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.beihui.market.R;
import com.beihui.market.api.Api;
import com.beihui.market.api.ResultEntity;
import com.beihui.market.base.BaseComponentActivity;
import com.beihui.market.entity.RemindBean;
import com.beihui.market.helper.SlidePanelHelper;
import com.beihui.market.helper.UserHelper;
import com.beihui.market.injection.component.AppComponent;
import com.beihui.market.injection.component.DaggerRemindComponent;
import com.beihui.market.injection.module.RemindModule;
import com.beihui.market.tang.rx.RxResponse;
import com.beihui.market.tang.rx.observer.ApiObserver;
import com.beihui.market.ui.contract.RemindContract;
import com.beihui.market.ui.presenter.RemindPresenter;
import com.beihui.market.util.CommonUtils;
import com.beihui.market.util.FastClickUtils;
import com.beihui.market.util.RxUtil;
import com.beihui.market.util.ToastUtils;
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

    private int pushRemind = 0;
    private int messageRemind = 1;
    private int remindDay = 1;
    private int type = 0;

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
        getRemindInfo();
        pushSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                type = 0;
                boolean checked = ((Switch) v).isChecked();
                if (checked) {
                    pushRemind = 1;
                } else {
                    pushRemind = 0;
                }
                remindSetting();
            }
        });

        messageSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                type = 1;
                boolean checked = ((Switch) v).isChecked();
                if (checked) {
                    messageRemind = 1;
                } else {
                    messageRemind = 0;
                }
                remindSetting();

            }
        });

    }


    private void getRemindInfo() {
        String userId = UserHelper.getInstance(this).getProfile().getId();
        Api.getInstance().onRemindInfo(userId).compose(RxResponse.<RemindBean>compatT()).subscribe(new ApiObserver<RemindBean>() {
            @Override
            public void onNext(RemindBean data) {
                if (data.getGeTui() == 1) {
                    pushRemind = 1;
                    pushSwitch.setChecked(true);
                } else {
                    pushSwitch.setChecked(false);
                    pushRemind = 0;
                }
                if (data.getSms() == 1) {
                    messageSwitch.setChecked(true);
                    messageRemind = 1;
                } else {
                    messageSwitch.setChecked(false);
                    messageRemind = 0;
                }

                selectTv.setText(CommonUtils.getDay(data.getDay() - 1));
                remindDay = data.getDay();


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
                remindDay = options1 + 1;
                remindSetting();

            }
        }).setTitleText("还款提醒").setCancelColor(getResources().getColor(R.color.pickerview_cancle))
                .setSubmitColor(getResources().getColor(R.color.pickerview_submit))
                .setTitleColor(getResources().getColor(R.color.pickerview_title)).setTitleBgColor(Color.WHITE).setLineSpacingMultiplier(2f)
                .setSelectOptions(0)
                .build();

        optionsPickerView.setPicker(list);
        optionsPickerView.show();


    }

    private void remindSetting() {
        String userId = UserHelper.getInstance(this).getProfile().getId();
        Api.getInstance().onRemindSetting(userId, pushRemind + "", messageRemind + "", null, remindDay + "")
                .compose(RxUtil.<ResultEntity>io2main())
                .subscribe(new ApiObserver<ResultEntity>() {
                    @Override
                    public void onNext(ResultEntity data) {
                        if (type == 0 && pushSwitch.isChecked()) {
                            ToastUtils.showToast(RemindActivity.this, "开启成功,考拉将" + CommonUtils.getDay(remindDay - 1) + "提醒你哦~");
                        } else if (type == 1 && messageSwitch.isChecked()) {
                            ToastUtils.showToast(RemindActivity.this, "开启成功,考拉将" + CommonUtils.getDay(remindDay - 1) + "提醒你哦~");
                        }
                    }
                });
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
