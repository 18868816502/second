package com.beihui.market.ui.fragment;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
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
import com.beihui.market.ui.activity.MainActivity;
import com.beihui.market.ui.contract.DebtNewContract;
import com.beihui.market.ui.listeners.EtAmountWatcher;
import com.beihui.market.ui.listeners.EtTextLengthWatcher;
import com.beihui.market.ui.presenter.DebtNewPresenter;
import com.beihui.market.util.InputMethodUtil;
import com.beihui.market.util.viewutils.ToastUtils;
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
 * 分期还款
 */
public class DebtNewEvenFragment extends BaseComponentFragment implements DebtNewContract.View {

    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);

//    @BindView(R.id.debt_pay_day)
//    TextView tvDebtPayDay;
//    @BindView(R.id.debt_time_limit)
//    TextView tvDebtTimeLimit;
//    @BindView(R.id.debt_term_amount)
//    EditText etDebtTermAmount;
//    @BindView(R.id.debt_extra_info_block)
//    FrameLayout flDebtExtraInfoBlock;
//    @BindView(R.id.debt_capital_amount)
//    EditText etDebtCapitalAmount;
//    @BindView(R.id.remark)
//    EditText etRemark;
//    @BindView(R.id.debt_info_expand_or_collapse)
//    TextView tvDebtInfoExpandCollapse;

    @Inject
    DebtNewPresenter presenter;

    private DebtChannel debtChannel;
    /**
     * 该字段不为空，则为编辑账单模式
     */
    private DebtDetail debtDetail;

    public static DebtNewEvenFragment newInstance(DebtChannel debtChannel, DebtDetail debtDetail) {
        DebtNewEvenFragment fragment = new DebtNewEvenFragment();
        fragment.debtChannel = debtChannel;
        fragment.debtDetail = debtDetail;
        return fragment;
    }

    @Override
    public void onDestroyView() {
        presenter.onDestroy();
        super.onDestroyView();
    }

    @Override
    public int getLayoutResId() {
        return R.layout.fragment_debt_new_even_debt;
    }

    @Override
    public void configViews() {
        Date date = new Date();
//        tvDebtPayDay.setText(dateFormat.format(date));
//        tvDebtPayDay.setTag(date);
//
//        tvDebtTimeLimit.setText("6个月");
//        tvDebtTimeLimit.setTag(6);
//
//        etRemark.addTextChangedListener(new EtTextLengthWatcher(etRemark, 20 * 2));
//        etDebtTermAmount.addTextChangedListener(new EtAmountWatcher(etDebtTermAmount));
//        etDebtCapitalAmount.addTextChangedListener(new EtAmountWatcher(etDebtCapitalAmount));
//
//        tvDebtInfoExpandCollapse.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                v.setSelected(!v.isSelected());
//                tvDebtInfoExpandCollapse.setText(v.isSelected() ? "隐藏更多信息" : "添加更多信息");
//                flDebtExtraInfoBlock.setVisibility(v.isSelected() ? View.VISIBLE : View.GONE);
//            }
//        });
//
//        etDebtTermAmount.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                etDebtTermAmount.requestFocus();
//            }
//        }, 100);
//
//        //限制小数位
//        EditTextUtils.addDecimalDigitsInputFilter(etDebtTermAmount);
//        EditTextUtils.addDecimalDigitsInputFilter(etDebtCapitalAmount);
//        //限制emoji输入
//        EditTextUtils.addDisableEmojiInputFilter(etRemark);
    }

    @Override
    public void initDatas() {
        if (debtDetail != null) {
            presenter.attachDebtDetail(debtDetail);
        }
    }

    @Override
    protected void configureComponent(AppComponent appComponent) {
        DaggerDebtNewComponent.builder()
                .appComponent(appComponent)
                .debtNewModule(new DebtNewModule(this))
                .debtChannel(debtChannel)
                .build()
                .inject(this);
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
//                        .setTitleText("首次还款日")
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
//                        int monthLimit = options1 + 1;
//                        tvDebtTimeLimit.setText(monthLimit + "个月");
//                        tvDebtTimeLimit.setTag(monthLimit);
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
//                    list.add(i + "个月");
//                }
//                pickerView.setPicker(list);
//                pickerView.setSelectOptions((Integer) tvDebtTimeLimit.getTag() - 1);
//                pickerView.show();
//                break;
//            }
//            case R.id.confirm:
//                boolean hasExtraInfo = tvDebtInfoExpandCollapse.isSelected() && !TextUtils.isEmpty(etDebtCapitalAmount.getText().toString());
//                presenter.saveEvenDebt((Date) tvDebtPayDay.getTag(), tvDebtTimeLimit.getTag() + "", etDebtTermAmount.getText().toString(),
//                        hasExtraInfo ? etDebtCapitalAmount.getText().toString() : "", etRemark.getText().toString());
//                break;
//        }
//    }

    @Override
    public void setPresenter(DebtNewContract.Presenter presenter) {
        //
    }

    @Override
    public void bindDebtDetail(DebtDetail debtDetail) {
//        //首次还款日
//        try {
//            tvDebtPayDay.setTag(dateFormat.parse(debtDetail.getFirstRepayDate()));
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//        tvDebtPayDay.setText(debtDetail.getFirstRepayDate());
//        //借款期限
//        int monthLimit = debtDetail.getTerm();
//        tvDebtTimeLimit.setText(monthLimit + "个月");
//        tvDebtTimeLimit.setTag(monthLimit);
//        //借款金额
//        String amount = keep2digitsWithoutZero(debtDetail.getTermPayableAmount());
//        if (amount.contains(",")) {
//            amount = amount.replace(",", "");
//        }
//        etDebtTermAmount.setText(amount);
//        //备注
//        etRemark.setText(TextUtils.isEmpty(debtDetail.getRemark()) ? "" : debtDetail.getRemark());
//        //高级模板
//        if (debtDetail.getCapital() > 0) {
//            //展开高级模板
//            tvDebtInfoExpandCollapse.setSelected(true);
//            tvDebtInfoExpandCollapse.setText(tvDebtInfoExpandCollapse.isSelected() ? "隐藏更多信息" : "添加更多信息");
//            flDebtExtraInfoBlock.setVisibility(tvDebtInfoExpandCollapse.isSelected() ? View.VISIBLE : View.GONE);
//            //借款本金
//            String capital = keep2digitsWithoutZero(debtDetail.getCapital());
//            if (capital.contains(",")) {
//                capital = capital.replace(",", "");
//            }
//            etDebtCapitalAmount.setText(capital);
//        }
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
