package com.beihui.market.ui.fragment;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.beihui.market.R;
import com.beihui.market.base.BaseComponentFragment;
import com.beihui.market.entity.DebtChannel;
import com.beihui.market.entity.DebtDetail;
import com.beihui.market.injection.component.AppComponent;
import com.beihui.market.injection.component.DaggerDebtNewComponent;
import com.beihui.market.injection.module.DebtNewModule;
import com.beihui.market.ui.activity.DebtNewActivity;
import com.beihui.market.ui.activity.MainActivity;
import com.beihui.market.ui.contract.DebtNewContract;
import com.beihui.market.ui.listeners.EtAmountWatcher;
import com.beihui.market.ui.listeners.EtTextLengthWatcher;
import com.beihui.market.ui.presenter.DebtNewPresenter;
import com.beihui.market.util.InputMethodUtil;
import com.beihui.market.util.viewutils.ToastUtils;
import com.beihui.market.view.ClearEditText;
import com.beihui.market.view.EditTextUtils;
import com.beihui.market.view.pickerview.OptionsPickerView;
import com.beihui.market.view.pickerview.TimePickerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

import static com.beihui.market.util.CommonUtils.keep2digitsWithoutZero;

/**
 * @author xhb
 * 网贷账单 详情编辑页面
 * 一次性还款
 *
 * 是新增账单还是编辑账单
 */
public class DebtNewOneTimeFragment extends BaseComponentFragment implements DebtNewContract.View {

    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);

    //账单名称
    @BindView(R.id.cet_one_time_account_name)
    ClearEditText etAccountName;
    //到期还款日
    @BindView(R.id.tv_one_time_dead_time)
    TextView tvDebtPayDay;
    //应还金额
    @BindView(R.id.cet_one_time_money)
    ClearEditText etDebtAmount;
    //备注
    @BindView(R.id.cet_one_time_mark_content)
    ClearEditText etRemark;

    @Inject
    DebtNewPresenter presenter;

    //依附的Activity
    private FragmentActivity activity;

    /**
     * 返回布局
     */
    @Override
    public int getLayoutResId() {
        return R.layout.fragment_debt_new_one_time;
    }

    /**
     * Dagger2注入方法
     */
    @Override
    protected void configureComponent(AppComponent appComponent) {
        DaggerDebtNewComponent.builder()
                .appComponent(appComponent)
                .debtNewModule(new DebtNewModule(this))
                .debtChannel(debtChannel)
                .build()
                .inject(this);
    }

    @Override
    public void initDatas() {

//        if (debtDetail != null) {
//            presenter.attachDebtDetail(debtDetail);
//        }


        activity = getActivity();
        /**
         * 点击保存 点击事件
         */
        ((DebtNewActivity)activity).setOnSaveAccountListener(new DebtNewActivity.OnSaveAccountListener() {
            @Override
            public void save() {
                /**
                 * 保存一次性还款账单
                 *
                 * @param payDate    到期还款日，must
                 * @param debtAmount 到期还款金额， must
                 * @param capital    本金, non-must
                 * @param timeLimit  借款期限, non-must
                 * @param remark     备注，non-must
                 */
//                presenter.saveOneTimeDebt((Date) tvDebtPayDay.getTag(), etDebtAmount.getText().toString(), hasExtraInfo ? etDebtCapitalAmount.getText().toString() : "",
//                        hasExtraInfo ? tvDebtTimeLimit.getTag() + "" : "", etRemark.getText().toString());
            }
        });

    }


    @OnClick({R.id.tv_one_time_dead_time})
    public void onClick(View view){
        switch (view.getId()) {
            //到期还款日
            case R.id.tv_one_time_dead_time:
                Calendar calendar = Calendar.getInstance(Locale.CHINA);
                calendar.setTime((Date) tvDebtPayDay.getTag());

                TimePickerView pickerView = new TimePickerView.Builder(getContext(), new TimePickerView.OnTimeSelectListener() {
                    @Override
                    public void onTimeSelect(Date date, View v) {
                        tvDebtPayDay.setTag(date);
                        tvDebtPayDay.setText(dateFormat.format(date));
                    }
                }).setType(new boolean[]{true, true, true, false, false, false})
                        .setCancelText("取消")
                        .setCancelColor(Color.parseColor("#5591ff"))
                        .setSubmitText("确认")
                        .setSubmitColor(Color.parseColor("#5591ff"))
                        .setTitleText("到期还款日")
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
    }

    //空方法
    @Override
    public void setPresenter(DebtNewContract.Presenter presenter) {}

    /**
     * 注销presenter
     */
    @Override
    public void onDestroyView() {
        presenter.onDestroy();
        super.onDestroyView();
    }











    /**
     * 该字段不为空，则为新增账单模式
     */
    private DebtChannel debtChannel;
    /**
     * 该字段不为空，则为编辑账单模式
     */
    private DebtDetail debtDetail;

    public static DebtNewOneTimeFragment newInstance(DebtChannel debtChannel, DebtDetail debtDetail) {
        DebtNewOneTimeFragment fragment = new DebtNewOneTimeFragment();
        fragment.debtChannel = debtChannel;
        fragment.debtDetail = debtDetail;
        return fragment;
    }





    @Override
    public void configViews() {
        Date date = new Date();
        tvDebtPayDay.setText(dateFormat.format(date));
        tvDebtPayDay.setTag(date);


        etRemark.addTextChangedListener(new EtTextLengthWatcher(etRemark, 20 * 2));
        etDebtAmount.addTextChangedListener(new EtAmountWatcher(etDebtAmount));


//        etDebtAmount.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                etDebtAmount.requestFocus();
//            }
//        }, 100);

        //限制小数位
        EditTextUtils.addDecimalDigitsInputFilter(etDebtAmount);
        //限制emoji输入
        EditTextUtils.addDisableEmojiInputFilter(etRemark);
    }





//    @OnClick({R.id.debt_pay_day_block, R.id.debt_time_limit_block, R.id.confirm})
//    void onItemClicked(View view) {
//        InputMethodUtil.closeSoftKeyboard(getActivity());
//        switch (view.getId()) {
//            case R.id.debt_pay_day_block: {
//                Calendar calendar = Calendar.getInstance(Locale.CHINA);
//                calendar.setTime((Date) tvDebtPayDay.getTag());
//
//                TimePickerView pickerView = new TimePickerView.Builder(getContext(), new TimePickerView.OnTimeSelectListener() {
//                    @Override
//                    public void onTimeSelect(Date date, View v) {
//                        tvDebtPayDay.setTag(date);
//                        tvDebtPayDay.setText(dateFormat.format(date));
//                    }
//                }).setType(new boolean[]{true, true, true, false, false, false})
//                        .setCancelText("取消")
//                        .setCancelColor(Color.parseColor("#5591ff"))
//                        .setSubmitText("确认")
//                        .setSubmitColor(Color.parseColor("#5591ff"))
//                        .setTitleText("到期还款日")
//                        .setTitleColor(getResources().getColor(R.color.black_1))
//                        .setTitleBgColor(Color.WHITE)
//                        .setBgColor(Color.WHITE)
//                        .setLabel("年", "月", "日", null, null, null)
//                        .isCenterLabel(false)
//                        .setDate(calendar)
//                        .build();
//                pickerView.show();
//                break;
//            }
//            case R.id.debt_time_limit_block: {
//                OptionsPickerView pickerView = new OptionsPickerView.Builder(getContext(), new OptionsPickerView.OnOptionsSelectListener() {
//                    @SuppressLint("SetTextI18n")
//                    @Override
//                    public void onOptionsSelect(int options1, int options2, int options3, View v) {
//                        int dayLimit = options1 + 1;
//                    }
//                }).setCancelText("取消")
//                        .setCancelColor(Color.parseColor("#5591ff"))
//                        .setSubmitText("确认")
//                        .setSubmitColor(Color.parseColor("#5591ff"))
//                        .setTitleText("")
//                        .setTitleColor(getResources().getColor(R.color.black_1))
//                        .build();
//
//                List<String> list = new ArrayList<>();
//                for (int i = 1; i <= 360; ++i) {
//                    list.add(i + "天");
//                }
//                pickerView.setPicker(list);
//                pickerView.show();
//                break;
//            }
//            case R.id.confirm:
////                boolean hasExtraInfo = tvDebtInfoExpandCollapse.isSelected() && !TextUtils.isEmpty(etDebtCapitalAmount.getText().toString());
////                presenter.saveOneTimeDebt((Date) tvDebtPayDay.getTag(), etDebtAmount.getText().toString(), hasExtraInfo ? etDebtCapitalAmount.getText().toString() : "",
////                        hasExtraInfo ? tvDebtTimeLimit.getTag() + "" : "", etRemark.getText().toString());
//                break;
//        }
//    }



    @Override
    public void bindDebtDetail(DebtDetail debtDetail) {
        //到期还款日
        try {
            tvDebtPayDay.setTag(dateFormat.parse(debtDetail.getFirstRepayDate()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        tvDebtPayDay.setText(debtDetail.getFirstRepayDate());
        //借款金额
        String amount = keep2digitsWithoutZero(debtDetail.getPayableAmount());
        if (amount.contains(",")) {
            amount = amount.replace(",", "");
        }
        etDebtAmount.setText(amount);
        //备注
        etRemark.setText(TextUtils.isEmpty(debtDetail.getRemark()) ? "" : debtDetail.getRemark());
        //高级模板
        if (debtDetail.getCapital() > 0) {
            //展开高级模板
//            tvDebtInfoExpandCollapse.setSelected(true);
//            tvDebtInfoExpandCollapse.setText(tvDebtInfoExpandCollapse.isSelected() ? "隐藏更多信息" : "添加更多信息");
//            flDebtExtraInfoBlock.setVisibility(tvDebtInfoExpandCollapse.isSelected() ? View.VISIBLE : View.GONE);
            //借款本金
            String capital = keep2digitsWithoutZero(debtDetail.getCapital());
            if (capital.contains(",")) {
                capital = capital.replace(",", "");
            }
//            etDebtCapitalAmount.setText(capital);
//            //借款期限
//            int dayLimit = debtDetail.getTerm();
//            tvDebtTimeLimit.setText(dayLimit + "天");
//            tvDebtTimeLimit.setTag(dayLimit);
        }
    }

    @Override
    public void saveDebtSuccess(String msg) {
        ToastUtils.showShort(getContext(), msg, R.mipmap.white_success);
//        tvDebtInfoExpandCollapse.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                Intent intent = new Intent(getContext(), MainActivity.class);
//                intent.putExtra("account", true);
//                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                startActivity(intent);
//                if (getActivity() != null) {
//                    getActivity().finish();
//                }
//            }
//        }, 200);
    }
}
