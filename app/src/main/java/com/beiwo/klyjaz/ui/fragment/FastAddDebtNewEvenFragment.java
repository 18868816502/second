package com.beiwo.klyjaz.ui.fragment;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.beiwo.klyjaz.R;
import com.beiwo.klyjaz.api.Api;
import com.beiwo.klyjaz.api.ResultEntity;
import com.beiwo.klyjaz.base.BaseComponentFragment;
import com.beiwo.klyjaz.entity.FastDebtDetail;
import com.beiwo.klyjaz.helper.UserHelper;
import com.beiwo.klyjaz.injection.component.AppComponent;
import com.beiwo.klyjaz.ui.activity.MainActivity;
import com.beiwo.klyjaz.ui.listeners.EtAmountWatcher;
import com.beiwo.klyjaz.ui.listeners.EtTextLengthWatcher;
import com.beiwo.klyjaz.util.FastClickUtils;
import com.beiwo.klyjaz.util.InputMethodUtil;
import com.beiwo.klyjaz.util.RxUtil;
import com.beiwo.klyjaz.util.WeakRefToastUtil;
import com.beiwo.klyjaz.view.ClearEditText;
import com.beiwo.klyjaz.view.EditTextUtils;
import com.bigkoo.pickerview.OptionsPickerView;
import com.bigkoo.pickerview.TimePickerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.functions.Consumer;

import static com.beiwo.klyjaz.util.CommonUtils.keep2digitsWithoutZero;

/**
 * @author xhb
 * @version 3.0.1
 * 快速记账 分期还款
 */
public class FastAddDebtNewEvenFragment extends BaseComponentFragment{

    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);

    private final SimpleDateFormat saveDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);

    //账单名称根布局
    @BindView(R.id.ll_even_debt_account_name_root)
    LinearLayout accountNameRoot;
    //账单名称
    @BindView(R.id.cet_even_debt_account_name)
    ClearEditText accountName;
    //应还金额
    @BindView(R.id.cet_even_debt_pay_money)
    ClearEditText payMoney;
    //首次还款日
    @BindView(R.id.tv_even_debt_date)
    TextView firstDate;
    //期数
    @BindView(R.id.tv_even_debt_times)
    TextView times;
    //备注
    @BindView(R.id.cet_even_debt_remark)
    ClearEditText remark;
    //保存账单按钮
    @BindView(R.id.tv_debt_new_even_debt_save)
    TextView saveAccount;

    //依附的Activity
    private FragmentActivity activity;

    public Handler mHandler = new Handler();

    /**
     * 该字段不为空，则为编辑账单模式
     */
    private FastDebtDetail fastDebtDetail;

    public static FastAddDebtNewEvenFragment newInstance(FastDebtDetail fastDebtDetail) {
        FastAddDebtNewEvenFragment fragment = new FastAddDebtNewEvenFragment();
        fragment.fastDebtDetail = fastDebtDetail;
        return fragment;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    /**
     * 布局
     */
    @Override
    public int getLayoutResId() {
        return R.layout.fragment_debt_new_even_debt;
    }

    @Override
    public void initDatas() {
        activity = getActivity();
        //隐藏账单名称
        accountNameRoot.setVisibility(View.GONE);
    }

    @Override
    public void configViews() {

        if (fastDebtDetail == null) {
            Date date = new Date();
            firstDate.setText(dateFormat.format(date));
            firstDate.setTag(date);

            times.setText("6个月");
            times.setTag(6);
        } else {
            //每月应还
            payMoney.setText((keep2digitsWithoutZero(fastDebtDetail.termPayableAmount).replace(",", "")));
            //首次还款日
            SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
            try {
                firstDate.setText(fastDebtDetail.firstRepayDate);
                firstDate.setTag(sdf.parse(fastDebtDetail.firstRepayDate));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            //还款期数
            times.setText(fastDebtDetail.term + "个月");
            times.setTag(fastDebtDetail.term);
            //备注
            remark.setText((TextUtils.isEmpty(fastDebtDetail.getProjectName()) || "快捷记账".equals(fastDebtDetail.getProjectName())) ? "" : fastDebtDetail.getProjectName());
        }

        //账单名称
        accountName.addTextChangedListener(new EtTextLengthWatcher(accountName, 16 * 2));

        remark.addTextChangedListener(new EtTextLengthWatcher(remark, 20 * 2));
        payMoney.addTextChangedListener(new EtAmountWatcher(payMoney));

        //限制小数位
        EditTextUtils.addDecimalDigitsInputFilter(payMoney);
        //限制emoji输入
        EditTextUtils.addDisableEmojiInputFilter(remark);
        EditTextUtils.addDisableEmojiInputFilter(accountName);

        /**
         * 开启软键盘
         */
        payMoney.requestFocus();
        if (!TextUtils.isEmpty(payMoney.getText().toString())) {
            payMoney.setSelection(payMoney.getText().toString().length());
        }
        mHandler.postDelayed(new Runnable(){
            public void run() {
                InputMethodUtil.openSoftKeyboard(activity, payMoney);
            }
        }, 500);
    }



    @Override
    protected void configureComponent(AppComponent appComponent) {

    }


    /**
     * 控件的点击事件
     */
    @OnClick({R.id.tv_even_debt_date, R.id.tv_even_debt_times, R.id.tv_debt_new_even_debt_save})
    void onItemClicked(View view) {
        InputMethodUtil.closeSoftKeyboard(getActivity());
        switch (view.getId()) {
            //首次还款日
            case R.id.tv_even_debt_date: {

                Calendar calendar = Calendar.getInstance(Locale.CHINA);
                calendar.setTime((Date) firstDate.getTag());

                TimePickerView pickerView = new TimePickerView.Builder(getContext(), new TimePickerView.OnTimeSelectListener() {
                    @Override
                    public void onTimeSelect(Date date, View v) {
                        firstDate.setTag(date);
                        firstDate.setText(dateFormat.format(date));
                    }
                }).setType(new boolean[]{true, true, true, false, false, false})
                        .setCancelText("取消")
                        .setCancelColor(Color.parseColor("#ff5200"))
                        .setSubmitText("确认")
                        .setSubmitColor(Color.parseColor("#ff5200"))
                        .setTitleText("首次还款日")
                        .setTitleColor(getResources().getColor(R.color.black_1))
                        .setTitleBgColor(Color.WHITE)
                        .setBgColor(Color.WHITE)
                        .setLabel("年", "月", "日", null, null, null)
                        .isCenterLabel(false)
                        .setDate(calendar)
                        .build();
                pickerView.show();
                break;
            }

            //分期期数
            case R.id.tv_even_debt_times: {
                OptionsPickerView pickerView = new OptionsPickerView.Builder(getContext(), new OptionsPickerView.OnOptionsSelectListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onOptionsSelect(int options1, int options2, int options3, View v) {
                        int monthLimit = options1 + 1;
                        times.setText(monthLimit + "个月");
                        times.setTag(monthLimit);
                    }
                }).setCancelText("取消")
                        .setCancelColor(Color.parseColor("#ff5200"))
                        .setSubmitText("确认")
                        .setSubmitColor(Color.parseColor("#ff5200"))
                        .setTitleText("还款期数")
                        .setTitleColor(getResources().getColor(R.color.black_1))
                        .setTitleBgColor(Color.WHITE)
                        .setBgColor(Color.WHITE)
                        .build();

                List<String> list = new ArrayList<>();
                for (int i = 1; i <= 36; ++i) {
                    list.add(i + "个月");
                }
                pickerView.setPicker(list);
                pickerView.setSelectOptions((Integer) times.getTag() - 1);
                pickerView.show();
                break;
            }
            //保存按钮
            case R.id.tv_debt_new_even_debt_save:
                if (!FastClickUtils.isFastClick()) {
                    if (fastDebtDetail == null) {
                        saveDebt();
                    } else {
                        deleteDebt();
                    }
                }
                break;
        }
    }

    private void deleteDebt() {
        Api.getInstance().deleteFastDebt(UserHelper.getInstance(activity).getProfile().getId(), fastDebtDetail.getId())
                .compose(RxUtil.<ResultEntity>io2main())
                .subscribe(new Consumer<ResultEntity>() {
                               @Override
                               public void accept(ResultEntity result) throws Exception {
                                   if (result.isSuccess()) {
                                       saveDebt();
                                   } else {
                                       showErrorMsg(result.getMsg());
                                   }
                               }
                           },
                        new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {


                            }
                        });
    }

    /**
     * 保存账单
     * @userId 用户ID
     * @projectName 快速记账 把备注当成账单名称 （默认为“快速记账”）
     * @repayType 还款方式 1-一次性还本付息, 2-分期
     * @term     期限
     * @payableAmount 到期还款 (repayType =1 必须)
     * @everyTermAmount 每期应还金额(repayType =2 必须)
     * @remark  不传
     * @deuRepaymentDate 	最终还款时间 (repayType =1 必须)
     * @firstRepaymentDate  首期还款时间 (repayType =2 必须)
     */
    private void saveDebt() {
        /**
         * 需要每月应还金额
         */
        if (TextUtils.isEmpty(payMoney.getText().toString())) {
            WeakRefToastUtil.showShort(getContext(), "请输入每月还款金额", null);
            return;
        } else {
            if (Double.parseDouble(payMoney.getText().toString()) <= 0D) {
                WeakRefToastUtil.showShort(getContext(), "账单金额必须大于0", null);
                return;
            }
        }
        HashMap<String, Object> params = new HashMap<>();
        params.put("userId", UserHelper.getInstance(activity).getProfile().getId());
        params.put("projectName", TextUtils.isEmpty(remark.getText().toString()) ? "快捷记账" : remark.getText().toString());
        params.put("repayType","2");
        params.put("term",times.getTag());
        params.put("everyTermAmount",payMoney.getText().toString());
        params.put("firstRepaymentDate",saveDateFormat.format((Date) firstDate.getTag()));

        Api.getInstance().saveFastDebt(params)
                .compose(RxUtil.<ResultEntity>io2main())
                .subscribe(new Consumer<ResultEntity>() {

                    @Override
                    public void accept(ResultEntity resultEntity) throws Exception {
                        if (resultEntity.isSuccess()) {
                            Intent intent = new Intent(getContext(), MainActivity.class);
                            intent.putExtra("account", true);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            if (activity != null) {
                                activity.finish();
                            }
                        } else {
                            WeakRefToastUtil.showShort(getContext(), resultEntity.getMsg(), null);
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                    }
                });
    }
}
