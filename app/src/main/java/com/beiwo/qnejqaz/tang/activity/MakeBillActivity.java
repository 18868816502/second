package com.beiwo.qnejqaz.tang.activity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.beiwo.qnejqaz.R;
import com.beiwo.qnejqaz.api.Api;
import com.beiwo.qnejqaz.api.ResultEntity;
import com.beiwo.qnejqaz.base.BaseComponentActivity;
import com.beiwo.qnejqaz.entity.CreateAccountReturnIDsBean;
import com.beiwo.qnejqaz.entity.RemindBean;
import com.beiwo.qnejqaz.helper.SlidePanelHelper;
import com.beiwo.qnejqaz.helper.UserHelper;
import com.beiwo.qnejqaz.loan.BillListActivity;
import com.beiwo.qnejqaz.tang.DlgUtil;
import com.beiwo.qnejqaz.tang.rx.RxResponse;
import com.beiwo.qnejqaz.tang.rx.observer.ApiObserver;
import com.beiwo.qnejqaz.util.InputMethodUtil;
import com.beiwo.qnejqaz.util.RxUtil;
import com.beiwo.qnejqaz.util.ToastUtil;
import com.beiwo.qnejqaz.view.ClearEditText;
import com.bigkoo.pickerview.OptionsPickerView;
import com.bigkoo.pickerview.TimePickerView;
import com.gyf.barlibrary.ImmersionBar;
import com.jakewharton.rxbinding2.view.RxView;

import org.greenrobot.eventbus.EventBus;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;

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
    ClearEditText etInputMoney;
    @BindView(R.id.tv_repay_date)
    TextView tvRepayDate;
    @BindView(R.id.fl_repay_date_wrap)
    FrameLayout flRepayDateWrap;
    @BindView(R.id.tv_repay_cycle)
    TextView tv_repay_cycle;
    @BindView(R.id.ll_cycle_times_wrap)
    LinearLayout ll_cycle_times_wrap;
    @BindView(R.id.tv_repay_times)
    TextView tvRepayTimes;
    @BindView(R.id.et_remark)
    ClearEditText etRemark;
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
    @BindView(R.id.fl_custom_wrap)
    FrameLayout flCustomWrap;
    @BindView(R.id.et_custom_name)
    ClearEditText etCustomName;
    @BindView(R.id.fl_repay_times_wrap)
    FrameLayout repay_times_wrap;
    @BindView(R.id.tv_alert_dlg)
    View alertDlg;

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
    private int repayCycle = 0;
    private int repayTimes = 0;
    private List<String> timeList = new ArrayList<>();
    private List<String> houseYearList = new ArrayList<>();
    private String numText;
    private int remind_day = 3;//默认提前3天

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
        tvToolbarTitle.setText(title);
        if (type == TYPE_NET_LOAN) {
            InputMethodUtil.openSoftKeyboard(this, etInputMoney);
        }
        if (type == TYPE_HOUSE_LOAN) {//房贷
            repay_times_wrap.setEnabled(false);
            tv_repay_cycle.setText("每月一次");
            repayCycle = 1;
            tv_repay_cycle.setCompoundDrawables(null, null, null, null);
            ll_cycle_times_wrap.setVisibility(View.VISIBLE);
            InputMethodUtil.openSoftKeyboard(this, etInputMoney);
        }
        if (type == TYPE_CAR_LOAN) {//车贷
            repay_times_wrap.setEnabled(false);
            tv_repay_cycle.setText("每月一次");
            repayCycle = 1;
            tv_repay_cycle.setCompoundDrawables(null, null, null, null);
            ll_cycle_times_wrap.setVisibility(View.VISIBLE);
            InputMethodUtil.openSoftKeyboard(this, etInputMoney);
        }
        if (type == TYPE_USER_DIFINE) {//自定义
            flCustomWrap.setVisibility(View.VISIBLE);
            InputMethodUtil.openSoftKeyboard(this, etCustomName);
        }
        iconId = getIntent().getStringExtra("iconId");
        tallyId = getIntent().getStringExtra("tallyId");

        etCustomName.setMaxLenght(8);
        etRemark.setMaxLenght(20);

        opCycleTimes = Arrays.asList(getResources().getStringArray(R.array.repay_cycle_times));
        opBeforeDate = Arrays.asList(getResources().getStringArray(R.array.tip_before_date));
        timeList.clear();
        for (int i = 0; i < 36; i++) {
            timeList.add((i + 1) + "期");
        }
        for (int i = 0; i < 6; i++) {
            houseYearList.add((i + 1) * 5 + "年");
        }

        Api.getInstance().onRemindInfo(UserHelper.getInstance(this).id())
                .compose(RxResponse.<RemindBean>compatT())
                .subscribe(new ApiObserver<RemindBean>() {
                    @Override
                    public void onNext(@NonNull RemindBean data) {
                        remind_day = data.getDay();
                        tvNotice.setText(opBeforeDate.get(remind_day - 1));
                    }
                });

        map.put("userId", UserHelper.getInstance(this).id());
        map.put("iconId", iconId);
        map.put("channelId", tallyId);
        map.put("tallyType", 1);//图标类型 1-公共 2-个性(isPrivate=0时tallyType=1/isPrivate=1时tallyType=2)
        map.put("channelName", title);
        map.put("termType", 2);//周期类型 1-日, 2-月, 3-年,全部按月计算

        etInputMoney.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (TextUtils.isEmpty(s.toString()) || "0".equals(s.toString())) {
                    numText = "";
                    return;
                }
                if (s.toString().startsWith("00")) {
                    String txt = s.toString().replaceFirst("^00*", "0");
                    etInputMoney.setText(txt);
                    etInputMoney.setSelection(txt.length());
                }
                String temp = s.toString();
                int dotPosition = temp.indexOf(".");
                if (dotPosition >= 0) {
                    if (temp.length() - dotPosition - 1 > 2) {
                        s.delete(dotPosition + 3, temp.length());
                    }
                } else {
                    if (temp.length() >= 8) {
                        s.delete(8, temp.length());
                    }
                }
                numText = s.toString().trim();
            }
        });

        RxView.clicks(tvSaveBill)
                .throttleFirst(1500, TimeUnit.MILLISECONDS)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {
                        createBill();
                    }
                });
    }

    @OnClick({R.id.tv_save_bill, R.id.fl_repay_date_wrap, R.id.fl_repay_times_wrap, R.id.tv_alert_dlg,
            R.id.fl_notice_wrap, R.id.rl_expand_shrink, R.id.ll_cycle_times_wrap})
    public void onViewClicked(View view) {
        InputMethodUtil.closeSoftKeyboard(this);//收起软键盘
        switch (view.getId()) {
            case R.id.tv_alert_dlg:
                DlgUtil.createDlg(MakeBillActivity.this, R.layout.dlg_info_dout, new DlgUtil.OnDlgViewClickListener() {
                    @Override
                    public void onViewClick(final Dialog dialog, View dlgView) {
                        View.OnClickListener listener = new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (R.id.confirm == v.getId()) {
                                    dialog.dismiss();
                                }
                            }
                        };
                        dlgView.findViewById(R.id.confirm).setOnClickListener(listener);
                    }
                });
                break;
            case R.id.fl_repay_date_wrap://首期应还款日
                firstRepayDatePicker();
                break;
            case R.id.fl_repay_times_wrap://还款周期
                repayPicker(opCycleTimes, getString(R.string.repay_cycle), 1, 0);
                break;
            case R.id.ll_cycle_times_wrap://还款期数
                if (type == TYPE_NET_LOAN || type == TYPE_CAR_LOAN || type == TYPE_USER_DIFINE)
                    repayPicker(timeList, getString(R.string.repay_times), 3, 0);
                if (type == TYPE_HOUSE_LOAN)
                    repayPicker(houseYearList, getString(R.string.repay_times), 3, 0);
                break;
            case R.id.fl_notice_wrap://还款提醒
                repayPicker(opBeforeDate, getString(R.string.repay_tip), 2, remind_day - 1);
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
        tvSaveBill.requestFocus();
        tvSaveBill.setFocusable(true);
        tvSaveBill.setFocusableInTouchMode(true);
    }

    private void createBill() {
        if (type == TYPE_USER_DIFINE) {
            String customName = etCustomName.getText().toString().trim();
            if (customName == null || customName.isEmpty()) {
                ToastUtil.toast("账单名称不能为空");
                return;
            }
            map.put("channelName", customName);
        }
        //金额
        if (numText == null || numText.isEmpty()) {
            ToastUtil.toast("请输入正确金额");
            return;
        }
        double num = 0;
        if (numText != null && !numText.isEmpty()) {
            num = Double.valueOf(numText);
            if (num <= 0) {
                ToastUtil.toast("每期金额不可为0");
                return;
            }
            if (num > 1e7) {
                ToastUtil.toast("每期金额过大");
                return;
            }
        }
        map.put("amount", num);
        //首期还款时间
        if (firstRepayDate == null || firstRepayDate.isEmpty()) {
            ToastUtil.toast("请选择首期应还款日");
            return;
        }
        map.put("firstRepaymentDate", firstRepayDate);
        //周期 + 期数
        if (repayCycle == 0) {//未配置还款周期，使用默认仅一次
            repayCycle = 1;
            if (repayTimes == 0) repayTimes = 1;
        } else {//使用还款周期配置
            if (repayTimes == 0) {
                ToastUtil.toast("请选择还款期数");
                return;
            }
        }
        map.put("cycle", repayCycle);
        map.put("term", repayTimes);
        //高级配置
        String remark = etRemark.getText().toString().trim();
        if (remark != null && !remark.isEmpty()) map.put("remark", remark);
        //提前x天提醒
        map.put("remind", remind_day);
        Api.getInstance().createLoanAccount(map)
                .compose(RxUtil.<ResultEntity<CreateAccountReturnIDsBean>>io2main())
                .subscribe(new Consumer<ResultEntity<CreateAccountReturnIDsBean>>() {
                    @Override
                    public void accept(ResultEntity<CreateAccountReturnIDsBean> resultEntity) throws Exception {
                        if (resultEntity.isSuccess()) {
                            ToastUtil.toast(resultEntity.getMsg());
                            Intent intent = new Intent(MakeBillActivity.this, BillListActivity.class);
                            startActivity(intent);
                            EventBus.getDefault().post("1");
                            finish();
                        }
                    }
                });
    }

    private void repayPicker(final List<String> op, String title, final int item, int position) {
        OptionsPickerView pickerView = new OptionsPickerView.Builder(this, new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                if (item == 1) {//还款周期
                    tv_repay_cycle.setText(op.get(options1));
                    if (options1 == 0) repayCycle = 0;
                    if (options1 == 1) repayCycle = 1;
                    if (options1 == 2) repayCycle = 2;
                    if (options1 == 3) repayCycle = 3;
                    if (options1 == 4) repayCycle = 6;
                    if (options1 == 5) repayCycle = 12;
                    if (options1 == 0) {//仅一次时不显示还款期数
                        ll_cycle_times_wrap.setVisibility(View.GONE);
                    } else {
                        ll_cycle_times_wrap.setVisibility(View.VISIBLE);
                    }
                }
                if (item == 2) {//还款提醒
                    tvNotice.setText(op.get(options1));
                    remind_day = options1 + 1;
                }
                if (item == 3) {//还款期数
                    tvRepayTimes.setText(op.get(options1));
                    if (type == TYPE_NET_LOAN || type == TYPE_CAR_LOAN || type == TYPE_USER_DIFINE) {
                        repayTimes = options1 + 1;
                    }
                    if (type == TYPE_HOUSE_LOAN) {
                        repayTimes = (options1 + 1) * 5 * 12;
                    }
                }
            }
        }).setCancelText(getString(R.string.cancel))
                .setCancelColor(cancelColor)
                .setSubmitText(getString(R.string.confirm))
                .setSubmitColor(confirmColor)
                .setTitleText(title)
                .setTitleColor(titleColor)
                .setTitleBgColor(Color.WHITE)
                .setSelectOptions(position)
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