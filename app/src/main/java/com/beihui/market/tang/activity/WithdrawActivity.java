package com.beihui.market.tang.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.beihui.market.R;
import com.beihui.market.api.Api;
import com.beihui.market.base.BaseComponentActivity;
import com.beihui.market.entity.PayAccount;
import com.beihui.market.entity.Withdraw;
import com.beihui.market.helper.SlidePanelHelper;
import com.beihui.market.helper.UserHelper;
import com.beihui.market.injection.component.AppComponent;
import com.beihui.market.tang.rx.RxResponse;
import com.beihui.market.tang.rx.observer.ApiObserver;
import com.beihui.market.util.ToastUtil;
import com.gyf.barlibrary.ImmersionBar;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;
import java.util.List;
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

public class WithdrawActivity extends BaseComponentActivity {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.tv_withdraw_amount)
    TextView tv_withdraw_amount;
    @BindView(R.id.tv_pay_account)
    TextView tv_pay_account;

    private PayAccount payAccount;
    private Map<String, Object> map = new HashMap<>();
    private double amount;

    @Override
    public int getLayoutId() {
        return R.layout.f_activity_withdraw;
    }

    @Override
    public void configViews() {
        setupToolbar(mToolbar);
        ImmersionBar.with(this).statusBarDarkFont(true).init();
        SlidePanelHelper.attach(this);
    }

    @Override
    public void initDatas() {
        try {
            amount = getIntent().getDoubleExtra("amount", 0);
        } catch (Exception e) {
        }
        tv_withdraw_amount.setText(String.format("%.0f", amount));

        Api.getInstance().payAccount(UserHelper.getInstance(this).id())
                .compose(RxResponse.<List<PayAccount>>compatT())
                .subscribe(new ApiObserver<List<PayAccount>>() {
                    @Override
                    public void onNext(@NonNull List<PayAccount> data) {
                        if (data != null && data.size() > 0) {
                            payAccount = data.get(0);
                            tv_pay_account.setText(payAccount.getPayeeName() + "\t" + payAccount.getPayeeAccount());
                        } else {
                            tv_pay_account.setText("您尚未设置支付宝账户");
                        }
                    }
                });
    }

    @Override
    protected void configureComponent(AppComponent appComponent) {
    }

    @OnClick({R.id.ll_alp_wrap, R.id.tv_withdraw})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_alp_wrap:
                Intent intent = new Intent(this, BindAlpActivity.class);
                intent.putExtra("payAccount", payAccount);
                startActivity(intent);
                break;
            case R.id.tv_withdraw:
                map.put("userId", UserHelper.getInstance(this).id());
                if (payAccount == null) {
                    ToastUtil.toast("请绑定支付宝账户");
                    return;
                }
                map.put("tradeAccount", payAccount.getPayeeAccount());
                map.put("tradeName", payAccount.getPayeeName());
                map.put("tradeAmount", amount);
                Api.getInstance().withdraw(map)
                        .compose(RxResponse.<Withdraw>compatT())
                        .subscribe(new ApiObserver<Withdraw>() {
                            @Override
                            public void onNext(@NonNull Withdraw data) {
                                EventBus.getDefault().post(String.format("%.2f", data.getBalance()));
                                ToastUtil.toast("提交审核，请耐心等待");
                                finish();
                            }
                        });
                break;
            default:
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void recieveMsg(PayAccount account) {
        if (account != null) {
            payAccount = account;
            tv_pay_account.setText(payAccount.getPayeeName() + "\t" + payAccount.getPayeeAccount());
        }
    }
}