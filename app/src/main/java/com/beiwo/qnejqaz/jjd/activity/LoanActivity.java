package com.beiwo.qnejqaz.jjd.activity;

import android.app.Dialog;
import android.content.Intent;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.beiwo.qnejqaz.R;
import com.beiwo.qnejqaz.api.Api;
import com.beiwo.qnejqaz.base.BaseComponentActivity;
import com.beiwo.qnejqaz.helper.DataHelper;
import com.beiwo.qnejqaz.helper.SlidePanelHelper;
import com.beiwo.qnejqaz.helper.UserHelper;
import com.beiwo.qnejqaz.jjd.bean.CashOrder;
import com.beiwo.qnejqaz.tang.DlgUtil;
import com.beiwo.qnejqaz.tang.StringUtil;
import com.beiwo.qnejqaz.tang.rx.RxResponse;
import com.beiwo.qnejqaz.tang.rx.observer.ApiObserver;
import com.beiwo.qnejqaz.ui.activity.UserProtocolActivity;
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
 * @date: 2018/9/14
 */

public class LoanActivity extends BaseComponentActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.toolbar_title)
    TextView toolbar_title;
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

    private int money;
    private float charge;
    private boolean checked = true;
    private Map<String, Object> map = new HashMap<>();

    @Override
    public int getLayoutId() {
        return R.layout.vest_activity_loan;
    }

    @Override
    public void configViews() {
        setupToolbar(toolbar);
        ImmersionBar.with(this).statusBarDarkFont(true).init();
        SlidePanelHelper.attach(this);
        toolbar_title.setText("借钱");
        Intent intent = getIntent();
        if (intent != null) {
            money = intent.getIntExtra("money", 0);
            charge = intent.getFloatExtra("charge", 0f);
        }
        //借款金额
        tv_loan_money.setText(money + "元");
        //借款期限
        tv_loan_period.setText("30天");
        //删除
        //tv_service_charge.setText(String.format("%.2f元", charge));
        //到账金额
        tv_real_money.setText(money + "元");
        //还款时间
        tv_pay_date.setText(StringUtil.date2Now(29));
        //还款金额
        tv_pay_money.setText(String.format("%.2f元", money * 0.05 / 100 * 30 + money));
        //借款协议
        tv_loan_protocol.setText(String.format(getString(R.string.loan_protocol), getString(R.string.app_name)));
    }

    @Override
    public void initDatas() {
    }

    @OnClick({R.id.iv_agree_protocal, R.id.tv_loan_protocol, R.id.tv_confirm_loan, R.id.tv_dout})
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
                DataHelper.getInstance(this).onCountUv("HPSureLoanButton");
                map.put("userId", UserHelper.getInstance(this).id());
                map.put("orderAmount", money);
                map.put("limitDay", 10);
                map.put("serviceCharge", charge);
                map.put("accountAmount", money - charge);
                map.put("returnTime", StringUtil.date2Now(9));
                map.put("returnAmount", money);
                Api.getInstance().saveCashOrder(map)
                        .compose(RxResponse.<CashOrder>compatT())
                        .subscribe(new ApiObserver<CashOrder>() {
                            @Override
                            public void onNext(@NonNull CashOrder data) {
                                EventBus.getDefault().post("1");
                                finish();
                            }
                        });
                break;
            case R.id.tv_dout:
                DlgUtil.createDlg(this, R.layout.f_dlg_apl_fail, new DlgUtil.OnDlgViewClickListener() {
                    @Override
                    public void onViewClick(final Dialog dialog, View dlgView) {
                        TextView content = dlgView.findViewById(R.id.content);
                        TextView title = dlgView.findViewById(R.id.dlg_title);
                        title.setText("提示");
                        content.setText("服务费按日息0.1%收取");
                        DlgUtil.cancelClick(dialog, dlgView);
                    }
                });
                break;
            default:
                break;
        }
    }

    private String sufUrl() {
        return "&amount=" + money + "&handlingCharge=" + charge + "&startTime="
                + StringUtil.stamp2Str(System.currentTimeMillis(), StringUtil.FORMAT_Y_M_D) + "&endTime=" + StringUtil.date2Now(9).substring(0, 10);
    }
}