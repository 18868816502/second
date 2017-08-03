package com.beihui.market.ui.activity;


import android.content.Intent;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;

import com.beihui.market.R;
import com.beihui.market.base.BaseComponentActivity;
import com.beihui.market.injection.component.AppComponent;
import com.beihui.market.injection.component.DaggerChangePsdComponent;
import com.beihui.market.injection.module.ChangePsdModule;
import com.beihui.market.ui.busevents.UserLogoutEvent;
import com.beihui.market.ui.contract.ChangePsdContract;
import com.beihui.market.ui.presenter.ChangePsdPresenter;
import com.beihui.market.util.LegalInputUtils;
import com.beihui.market.util.viewutils.ToastUtils;

import org.greenrobot.eventbus.EventBus;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

public class ChangePsdActivity extends BaseComponentActivity implements ChangePsdContract.View {
    @BindView(R.id.tool_bar)
    Toolbar toolbar;
    @BindView(R.id.origin_psd)
    EditText originPsdEt;
    @BindView(R.id.new_psd)
    EditText newPsdEt;
    @BindView(R.id.new_psd_confirm)
    EditText newPsdConfirmEt;
    @BindView(R.id.confirm)
    Button confirmBtn;

    @Inject
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
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };
        originPsdEt.addTextChangedListener(textWatcher);
        newPsdEt.addTextChangedListener(textWatcher);
        newPsdConfirmEt.addTextChangedListener(textWatcher);
    }

    @Override
    public void initDatas() {

    }

    @Override
    protected void configureComponent(AppComponent appComponent) {
        DaggerChangePsdComponent.builder()
                .appComponent(appComponent)
                .changePsdModule(new ChangePsdModule(this))
                .build()
                .inject(this);
    }

    @OnClick(R.id.confirm)
    void onViewClicked() {
        presenter.updatePsd(originPsdEt.getText().toString(), newPsdEt.getText().toString(), newPsdConfirmEt.getText().toString());
    }

    @Override
    public void setPresenter(ChangePsdContract.Presenter presenter) {
        //injected.nothing to do.
    }

    @Override
    public void showErrorMsg(String msg) {
        ToastUtils.showShort(this, msg, null);
    }

    @Override
    public void showUpdateSuccess() {
        ToastUtils.showShort(this, "密码修改成功", null);
        //发送用户退出全局事件，并要求用户重新登录
        UserLogoutEvent event = new UserLogoutEvent();
        event.pendingAction = UserLogoutEvent.ACTION_START_LOGIN;
        EventBus.getDefault().post(event);

        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}
