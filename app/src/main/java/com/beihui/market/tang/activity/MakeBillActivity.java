package com.beihui.market.tang.activity;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.beihui.market.R;
import com.beihui.market.api.Api;
import com.beihui.market.base.BaseComponentActivity;
import com.beihui.market.entity.CreateAccountReturnIDsBean;
import com.beihui.market.helper.SlidePanelHelper;
import com.beihui.market.helper.UserHelper;
import com.beihui.market.injection.component.AppComponent;
import com.beihui.market.tang.rx.RxResponse;
import com.beihui.market.tang.rx.observer.ApiObserver;
import com.beihui.market.ui.activity.MainActivity;
import com.beihui.market.util.InputMethodUtil;
import com.beihui.market.util.ToastUtils;
import com.beihui.market.view.pickerview.OptionsPickerView;
import com.beihui.market.view.pickerview.TimePickerView;
import com.gyf.barlibrary.ImmersionBar;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
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
 * @date: 2018/7/20
 */

public class MakeBillActivity extends BaseComponentActivity {

    public static final int TYPE_USER_DIFINE = 100;
    public static final int TYPE_HOUSE_LOAN = 101;
    public static final int TYPE_CAR_LOAN = 102;
    public static final int TYPE_NET_LOAN = 103;

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.tv_toolbar_title)
    TextView tvToolbarTitle;
    @BindView(R.id.tv_save_bill)
    TextView tvSaveBill;
    @BindView(R.id.et_input_money)
    EditText etInputMoney;
    @BindView(R.id.tv_repay_date)
    TextView tvRepayDate;
    @BindView(R.id.fl_repay_date_wrap)
    FrameLayout flRepayDateWrap;
    @BindView(R.id.tv_repay_cycle)
    TextView tvRepayCycle;
    @BindView(R.id.ll_cycle_times_wrap)
    LinearLayout llCycleTimesWrap;
    @BindView(R.id.tv_repay_times)
    TextView tvRepayTimes;
    @BindView(R.id.fl_repay_times_wrap)
    FrameLayout flRepayTimesWrap;
    @BindView(R.id.et_remark)
    EditText etRemark;
    @BindView(R.id.tv_notice)
    TextView tvNotice;
    @BindView(R.id.fl_notice_wrap)
    FrameLayout flNoticeWrap;
    @BindView(R.id.ll_more_setting_wrap)
    LinearLayout llMoreSettingWrap;
    @BindView(R.id.tv_expand_shrink)
    TextView tvExpandShrink;
    @BindView(R.id.iv_expand_shrink)
    ImageView ivExpandShrink;
    @BindView(R.id.rl_expand_shrink)
    RelativeLayout rlExpandShrink;

    private int type;
    private String iconId;
    private String tallyId;
    private boolean isExpand = false;
    private List<String> opCycleTimes = new ArrayList<>();
    private List<String> opBeforeDate = new ArrayList<>();
    private int cancelColor = Color.parseColor("#909298");
    private int confirmColor = Color.parseColor("#ff5240");
    private int titleColor = Color.parseColor("#424251");
    private Map<String, Object> map = new HashMap<>();
    private String firstRepayDate = null;
    private int repayCycle = 1;
    private int repayTimes = 0;
    private int repayTip = 1;
    private List<String> timeList = new ArrayList<>();

    @Override
    public int getLayoutId() {
        return R.layout.f_activity_make_bill;
    }

    @Override
    public void configViews() {
        setupToolbar(mToolbar);
        ImmersionBar.with(this).statusBarDarkFont(true).init();
        SlidePanelHelper.attach(this);
    }

    @Override
    public void initDatas() {
        type = getIntent().getIntExtra("type", 0);
        String title = getIntent().getStringExtra("title");
        if (type == TYPE_NET_LOAN) tvToolbarTitle.setText(title);
        if (type == TYPE_HOUSE_LOAN) tvToolbarTitle.setText("房贷");
        if (type == TYPE_CAR_LOAN) tvToolbarTitle.setText("车贷");
        if (type == TYPE_USER_DIFINE) tvToolbarTitle.setText("自定义");
        iconId = getIntent().getStringExtra("iconId");
        tallyId = getIntent().getStringExtra("tallyId");

        opCycleTimes = Arrays.asList(getResources().getStringArray(R.array.repay_cycle_times));
        opBeforeDate = Arrays.asList(getResources().getStringArray(R.array.tip_before_date));
        timeList.clear();
        for (int i = 0; i < 36; i++) {
            timeList.add((i + 1) + "期");
        }

        map.put("userId", UserHelper.getInstance(this).id());
        map.put("iconId", iconId);
        map.put("channelId", tallyId);
        map.put("tallyType", 1);//图标类型 1-公共 2-个性(isPrivate=0时tallyType=1/isPrivate=1时tallyType=2)
        map.put("channelName", title);

        map.put("termType", 2);//周期类型 1-日, 2-月, 3-年,都按月计算
    }

    @Override
    protected void configureComponent(AppComponent appComponent) {
    }

    @OnClick({R.id.tv_save_bill, R.id.fl_repay_date_wrap, R.id.fl_repay_times_wrap,
            R.id.fl_notice_wrap, R.id.rl_expand_shrink, R.id.ll_cycle_times_wrap})
    public void onViewClicked(View view) {
        InputMethodUtil.closeSoftKeyboard(this);//收起软键盘
        switch (view.getId()) {
            case R.id.tv_save_bill:
                //金额
                String numText = etInputMoney.getText().toString().trim();
                if (numText == null || numText.isEmpty()) {
                    ToastUtils.showToast(this, "请输入每期金额");
                    return;
                }
                double num = 0;
                if (numText != null && !numText.isEmpty()) {
                    num = Double.valueOf(numText);
                    if (num <= 0) {
                        ToastUtils.showToast(this, "每期金额不可为0");
                        return;
                    }
                }
                map.put("amount", num);
                //首期还款时间
                if (firstRepayDate == null || firstRepayDate.isEmpty()) {
                    ToastUtils.showToast(this, "请选择首期应还款日");
                    return;
                }
                map.put("firstRepaymentDate", firstRepayDate);
                //周期
                map.put("cycle", repayCycle);
                //高级配置
                if (isExpand) {
                    String remark = etRemark.getText().toString().trim();
                    if (remark != null && !remark.isEmpty()) {
                        map.put("remark", remark);
                    } else map.put("remark", "loan");
                    map.put("term", repayTimes);
                } else {
                    map.put("remark", "loan");
                    map.put("term", repayTimes);
                }

                Api.getInstance().createLoanAccount(map)
                        .compose(RxResponse.<CreateAccountReturnIDsBean>compatT())
                        .subscribe(new ApiObserver<CreateAccountReturnIDsBean>() {
                            @Override
                            public void onNext(@NonNull CreateAccountReturnIDsBean data) {
                                startActivity(new Intent(MakeBillActivity.this, MainActivity.class));
                                finish();
                            }
                        });
                break;
            case R.id.fl_repay_date_wrap:
                firstRepayDatePicker();
                break;
            case R.id.fl_repay_times_wrap:
                repayPicker(opCycleTimes, getString(R.string.repay_cycle), 1);
                break;
            case R.id.ll_cycle_times_wrap:
                repayPicker(timeList, getString(R.string.repay_times), 3);
                break;
            case R.id.fl_notice_wrap:
                repayPicker(opBeforeDate, getString(R.string.repay_tip), 2);
                break;
            case R.id.rl_expand_shrink:
                if (isExpand) {
                    llMoreSettingWrap.setVisibility(View.GONE);
                    tvExpandShrink.setText(getString(R.string.expand_more_setting));
                    ivExpandShrink.setImageResource(R.mipmap.ic_orientation_down);
                } else {
                    llMoreSettingWrap.setVisibility(View.VISIBLE);
                    tvExpandShrink.setText(getString(R.string.shrink_more_setting));
                    ivExpandShrink.setImageResource(R.mipmap.ic_orientation_up);
                }
                isExpand = !isExpand;
                break;
            default:
                break;
        }
    }

    private void repayPicker(final List<String> op, String title, final int type) {
        OptionsPickerView pickerView = new OptionsPickerView.Builder(this, new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                if (type == 1) {//还款周期
                    tvRepayCycle.setText(op.get(options1));
                    repayCycle = options1 + 1;
                    if (options1 == 0) repayCycle = 1;
                    if (options1 == 1) repayCycle = 1;
                    if (options1 == 2) repayCycle = 2;
                    if (options1 == 3) repayCycle = 3;
                    if (options1 == 4) repayCycle = 6;
                    if (options1 == 5) repayCycle = 12;
                    if (options1 == 0) {//仅一次时不显示还款期数
                        llCycleTimesWrap.setVisibility(View.GONE);
                    } else {
                        llCycleTimesWrap.setVisibility(View.VISIBLE);
                    }
                }
                if (type == 2) {//还款提醒
                    tvNotice.setText(op.get(options1));
                    repayTip = options1 + 1;
                }
                if (type == 3) {//还款期数
                    tvRepayTimes.setText(op.get(options1));
                    repayTimes = options1 + 1;
                }
            }
        }).setCancelText(getString(R.string.cancel))
                .setCancelColor(cancelColor)
                .setSubmitText(getString(R.string.confirm))
                .setSubmitColor(confirmColor)
                .setTitleText(title)
                .setTitleColor(titleColor)
                .setTitleBgColor(Color.WHITE)
                .setBgColor(Color.WHITE)
                .build();
        pickerView.setPicker(op);
        pickerView.show();
    }

    private void firstRepayDatePicker() {
        TimePickerView pickerView = new TimePickerView.Builder(this, new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                tvRepayDate.setText(new SimpleDateFormat("yyyy年MM月dd日").format(date));
                firstRepayDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
            }
        }).setType(new boolean[]{true, true, true, false, false, false})
                .setCancelText(getString(R.string.cancel))
                .setCancelColor(cancelColor)
                .setSubmitText(getString(R.string.confirm))
                .setSubmitColor(confirmColor)
                .setTitleText(getString(R.string.select_first_repay_date))
                .setTitleColor(titleColor)
                .setTitleBgColor(Color.WHITE)
                .setBgColor(Color.WHITE)
                .setLabel("年", "月", "日", null, null, null)
                .isCenterLabel(false)
                .setDate(Calendar.getInstance(Locale.CHINA))
                .build();
        pickerView.show();
    }
}
