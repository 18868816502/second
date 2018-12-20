package com.beiwo.qnejqaz.ui.activity;


import android.content.Intent;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.beiwo.qnejqaz.App;
import com.beiwo.qnejqaz.R;
import com.beiwo.qnejqaz.base.BaseComponentActivity;
import com.beiwo.qnejqaz.helper.SlidePanelHelper;
import com.beiwo.qnejqaz.ui.busevents.UserLogoutEvent;
import com.beiwo.qnejqaz.ui.contract.ChangePsdContract;
import com.beiwo.qnejqaz.ui.presenter.ChangePsdPresenter;
import com.beiwo.qnejqaz.umeng.Events;
import com.beiwo.qnejqaz.umeng.Statistic;
import com.beiwo.qnejqaz.util.CommonUtils;
import com.beiwo.qnejqaz.util.InputMethodUtil;
import com.beiwo.qnejqaz.util.LegalInputUtils;
import com.beiwo.qnejqaz.util.WeakRefToastUtil;
import com.beiwo.qnejqaz.view.ClearEditText;
import com.gyf.barlibrary.ImmersionBar;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 修改登录密码
 */
public class ChangePsdActivity extends BaseComponentActivity implements ChangePsdContract.View {
    @BindView(R.id.tool_bar)
    Toolbar toolbar;
    //原密码
    @BindView(R.id.origin_psd)
    ClearEditText originPsdEt;
    //新密码
    @BindView(R.id.new_psd)
    ClearEditText newPsdEt;
    //新密码 确认
    @BindView(R.id.new_psd_confirm)
    ClearEditText newPsdConfirmEt;
    //确认修改按钮
    @BindView(R.id.confirm)
    TextView confirmBtn;

    @BindView(R.id.origin_psd_visibility)
    CheckBox originPsdVisibility;
    @BindView(R.id.new_psd_visibility)
    CheckBox newPsdVisibility;
    @BindView(R.id.new_psd_confirm_visibility)
    CheckBox newPsdConfirmVisibility;

    ChangePsdPresenter presenter;

    @Override
    protected void onDestroy() {
        presenter.onDestroy();
        presenter = null;
        super.onDestroy();
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_change_psd;
    }

    @Override
    public void configViews() {
        presenter = new ChangePsdPresenter(this,this);
        ImmersionBar.with(this).statusBarDarkFont(true).init();
        setupToolbar(toolbar);

        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String origin = originPsdEt.getText().toString();
                String newPsd = newPsdEt.getText().toString();
                String confirm = newPsdConfirmEt.getText().toString();

                confirmBtn.setEnabled(LegalInputUtils.validatePassword(origin) && LegalInputUtils.validatePassword(newPsd) &&
                        LegalInputUtils.validatePassword(confirm));
                confirmBtn.setBackground((LegalInputUtils.validatePassword(origin) && LegalInputUtils.validatePassword(newPsd) &&
                        LegalInputUtils.validatePassword(confirm)) ? getResources().getDrawable(R.drawable.round_login_btn) :getResources().getDrawable(R.drawable.round_login_btn_grey));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };
        originPsdEt.addTextChangedListener(textWatcher);
        newPsdEt.addTextChangedListener(textWatcher);
        newPsdConfirmEt.addTextChangedListener(textWatcher);

        SlidePanelHelper.attach(this);
    }

    @Override
    public void initDatas() {
        showOrHideEditContent();
    }

    @OnClick(R.id.confirm)
    void onViewClicked() {
        //umeng统计
        Statistic.onEvent(Events.CHANGE_PASSWORD_CONFIRM);

        presenter.updatePsd(originPsdEt.getText().toString(), newPsdEt.getText().toString(), newPsdConfirmEt.getText().toString());
    }

    @Override
    public void setPresenter(ChangePsdContract.Presenter presenter) {
        //injected.nothing to do.
    }

    @Override
    public void showUpdateSuccess(String msg, String account) {
        dismissProgress();
        WeakRefToastUtil.showShort(this, msg, null);
        //发送用户退出全局事件，并要求用户重新登录
        UserLogoutEvent event = new UserLogoutEvent();
        event.pendingAction = UserLogoutEvent.ACTION_START_LOGIN;
        event.pendingPhone = account;
        EventBus.getDefault().post(event);

        Intent intent = new Intent(this, App.audit == 2 ? MainActivity.class: VestMainActivity.class);
        startActivity(intent);

        finish();
    }

    @Override
    public void finish() {
        InputMethodUtil.closeSoftKeyboard(this);
        super.finish();
    }


    /**
     * 显示或者隐藏输入框的密码
     * 原密码
     * 新密码
     * 确认新密码
     */
    private void showOrHideEditContent() {
        originPsdVisibility.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    originPsdEt.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    originPsdEt.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
                CommonUtils.setEditTextCursorLocation(originPsdEt);
            }
        });

        newPsdVisibility.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    newPsdEt.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    newPsdEt.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
                CommonUtils.setEditTextCursorLocation(newPsdEt);
            }
        });

        newPsdConfirmVisibility.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    newPsdConfirmEt.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    newPsdConfirmEt.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
                CommonUtils.setEditTextCursorLocation(newPsdConfirmEt);
            }
        });
    }
}
