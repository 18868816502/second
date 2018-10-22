package com.beiwo.klyjaz.scdk.activity;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.beiwo.klyjaz.App;
import com.beiwo.klyjaz.BuildConfig;
import com.beiwo.klyjaz.R;
import com.beiwo.klyjaz.api.NetConstants;
import com.beiwo.klyjaz.base.BaseComponentActivity;
import com.beiwo.klyjaz.helper.DataStatisticsHelper;
import com.beiwo.klyjaz.helper.SlidePanelHelper;
import com.beiwo.klyjaz.helper.UserHelper;
import com.beiwo.klyjaz.injection.component.AppComponent;
import com.beiwo.klyjaz.tang.StringUtil;
import com.beiwo.klyjaz.ui.activity.UserProtocolActivity;
import com.beiwo.klyjaz.ui.activity.VestMainActivity;
import com.beiwo.klyjaz.util.SPUtils;
import com.gyf.barlibrary.ImmersionBar;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * https://gitee.com/tangbuzhi
 *
 * @author: Tangbuzhi
 * @version:
 * @package:
 * @description:
 * @modify:
 * @date: 2018/9/14
 */

public class ScdkLoanActivity extends BaseComponentActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tv_loan_money)
    TextView tv_loan_money;
    @BindView(R.id.tv_loan_period)
    TextView tv_loan_period;
    @BindView(R.id.tv_service_charge)
    TextView tv_service_charge;
    @BindView(R.id.tv_real_money)
    TextView tv_real_money;
    @BindView(R.id.tv_pay_date)
    TextView tv_pay_date;
    @BindView(R.id.tv_pay_money)
    TextView tv_pay_money;
    @BindView(R.id.tv_loan_protocol)
    TextView tv_loan_protocol;
    @BindView(R.id.iv_agree_protocal)
    ImageView iv_agree_protocal;
    @BindView(R.id.tv_confirm_loan)
    TextView tv_confirm_loan;

    private float money;
    private float charge;
    private boolean checked = true;
    private Activity activity;
    private Map<String, Object> map = new HashMap<>();

    @Override
    public int getLayoutId() {
        return R.layout.vest_activity_loan;
    }

    @Override
    public void configViews() {
        activity = this;
        setupToolbar(toolbar);
        ImmersionBar.with(this).statusBarDarkFont(true).init();
        SlidePanelHelper.attach(this);
        Intent intent = getIntent();
        if (intent != null) {
            money = intent.getFloatExtra("money", 0f);
            charge = intent.getFloatExtra("charge", 0f);
        }
        tv_loan_money.setText(String.format("%.0f元", money));
        tv_loan_period.setText("10天");
        tv_service_charge.setText(String.format("%.2f元", charge));
        tv_real_money.setText(String.format("%.2f元", money - charge));
        tv_pay_date.setText(StringUtil.date2Now(9));
        tv_pay_money.setText(String.format("%.0f元", money));
        tv_loan_protocol.setText(String.format(getString(R.string.loan_protocol), getString(R.string.app_name)));
    }

    @Override
    public void initDatas() {
    }

    @Override
    protected void configureComponent(AppComponent appComponent) {
    }

    @OnClick({R.id.iv_agree_protocal, R.id.tv_loan_protocol, R.id.tv_confirm_loan})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_agree_protocal:
                iv_agree_protocal.setImageResource(!checked ? R.drawable.btn_open_rb : R.drawable.btn_close_rb);
                checked = !checked;
                tv_confirm_loan.setEnabled(checked);
                break;
            case R.id.tv_loan_protocol:
                Intent intent = new Intent(this, UserProtocolActivity.class);
                intent.putExtra("type", 4);
                intent.putExtra("url", sufUrl());
                startActivity(intent);
                break;
            case R.id.tv_confirm_loan:
                DataStatisticsHelper.getInstance().onCountUv("JjdSureLoanPage");
                map.put("userId", UserHelper.getInstance(this).id());
                map.put("orderAmount", money);
                map.put("limitDay", 10);
                map.put("serviceCharge", charge);
                map.put("accountAmount", money - charge);
                map.put("returnTime", StringUtil.date2Now(9));
                map.put("returnAmount", money);
                main();
//                Api.getInstance().saveCashOrder(map)
//                        .compose(RxResponse.<CashOrder>compatT())
//                        .subscribe(new ApiObserver<CashOrder>() {
//                            @Override
//                            public void onNext(@NonNull CashOrder data) {
//                                url = generateUrl(data.getOrderStatus(), data.getOverDate(), data.getAuditDate());
//                                main();
//                            }
//                        });
                break;
            default:
                break;
        }
    }

    private String url;

    private void main() {
        SPUtils.setVertifyState(this,5, UserHelper.getInstance(this).getProfile().getAccount());
        Intent intent = new Intent(activity, VestMainActivity.class);
        intent.putExtra("home", true);
        intent.putExtra("webViewUrl", url);
        startActivity(intent);
        finish();
    }

    private String generateUrl(String status, String overDate, String auditDate) {
        return BuildConfig.H5_DOMAIN_NEW + "/activity/page/activity-loan-review.html?status=" + status
                + "&overDate=" + overDate + "&auditDate=" + auditDate + "&audit=" + App.audit + NetConstants.sufPublicParam(UserHelper.getInstance(this).id());
    }

    private String sufUrl() {
        return "&amount=" + money + "&handlingCharge=" + charge + "&startTime="
                + StringUtil.stamp2Str(System.currentTimeMillis(), StringUtil.FORMAT_Y_M_D) + "&endTime=" + StringUtil.date2Now(9).substring(0, 10);
    }
}