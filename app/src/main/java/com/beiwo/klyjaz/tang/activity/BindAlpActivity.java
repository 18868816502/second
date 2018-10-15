package com.beiwo.klyjaz.tang.activity;

import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;

import com.beiwo.klyjaz.R;
import com.beiwo.klyjaz.api.Api;
import com.beiwo.klyjaz.base.BaseComponentActivity;
import com.beiwo.klyjaz.entity.PayAccount;
import com.beiwo.klyjaz.helper.SlidePanelHelper;
import com.beiwo.klyjaz.helper.UserHelper;
import com.beiwo.klyjaz.injection.component.AppComponent;
import com.beiwo.klyjaz.tang.StringUtil;
import com.beiwo.klyjaz.tang.rx.RxResponse;
import com.beiwo.klyjaz.tang.rx.observer.ApiObserver;
import com.beiwo.klyjaz.util.ToastUtil;
import com.beiwo.klyjaz.view.ClearEditText;
import com.gyf.barlibrary.ImmersionBar;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.annotations.NonNull;

/**
 * https://gitee.com/tangbuzhi
 *
 * @author: Tangbuzhi
 * @version:
 * @package:
 * @description:
 * @modify:
 * @date: 2018/8/16
 */

public class BindAlpActivity extends BaseComponentActivity {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.et_alp_account)
    ClearEditText et_alp_account;
    @BindView(R.id.et_alp_name)
    ClearEditText et_alp_name;

    private PayAccount payAccount;
    private Map<String, Object> map = new HashMap<>();
    private String payeeAccount;
    private String payeeName;

    @Override
    public int getLayoutId() {
        return R.layout.f_activity_bind_alp;
    }

    @Override
    public void configViews() {
        setupToolbar(mToolbar);
        ImmersionBar.with(this).statusBarDarkFont(true).init();
        SlidePanelHelper.attach(this);
    }

    @Override
    public void initDatas() {
        et_alp_account.setMaxLenght(50);
        et_alp_name.setMaxLenght(50);
        try {
            payAccount = (PayAccount) getIntent().getSerializableExtra("payAccount");
            if (payAccount != null) {
                et_alp_account.setText(payAccount.getPayeeAccount());
                et_alp_name.setText(payAccount.getPayeeName());
                map.put("accountId", payAccount.getAccountId());
            }
        } catch (Exception e) {
        }
        map.put("userId", UserHelper.getInstance(this).id());
    }

    @Override
    protected void configureComponent(AppComponent appComponent) {
    }

    @OnClick({R.id.tv_alp_save})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_alp_save:
                payeeName = et_alp_name.getText().toString().trim();
                payeeAccount = et_alp_account.getText().toString().trim();
                if (TextUtils.isEmpty(payeeName) || TextUtils.isEmpty(payeeAccount)) {
                    ToastUtil.toast("支付宝账号或姓名不能为空");
                    return;
                }
                if (!(StringUtil.isEmail(payeeAccount) || StringUtil.isPhone(payeeAccount))) {
                    ToastUtil.toast("支付宝账号格式不正确，请检查");
                    return;
                }

                map.put("payeeType", 1);
                map.put("payeeAccount", payeeAccount);
                map.put("payeeName", payeeName);
                Api.getInstance().saveAlpAccount(map)
                        .compose(RxResponse.<PayAccount>compatT())
                        .subscribe(new ApiObserver<PayAccount>() {
                            @Override
                            public void onNext(@NonNull PayAccount data) {
                                ToastUtil.toast("保存成功");
                                EventBus.getDefault().post(data);
                                finish();
                            }
                        });
                break;
            default:
                break;
        }
    }
}