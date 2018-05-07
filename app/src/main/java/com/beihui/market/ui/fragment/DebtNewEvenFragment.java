package com.beihui.market.ui.fragment;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.beihui.market.R;
import com.beihui.market.base.BaseComponentFragment;
import com.beihui.market.entity.DebtChannel;
import com.beihui.market.entity.DebtDetail;
import com.beihui.market.helper.KeyBoardHelper;
import com.beihui.market.injection.component.AppComponent;
import com.beihui.market.injection.component.DaggerDebtNewComponent;
import com.beihui.market.injection.module.DebtNewModule;
import com.beihui.market.ui.activity.MainActivity;
import com.beihui.market.ui.contract.DebtNewContract;
import com.beihui.market.ui.listeners.EtAmountWatcher;
import com.beihui.market.ui.listeners.EtTextLengthWatcher;
import com.beihui.market.ui.presenter.DebtNewPresenter;
import com.beihui.market.util.FastClickUtils;
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
 * 分期还款
 */
public class DebtNewEvenFragment extends BaseComponentFragment implements DebtNewContract.View {

    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);

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

    @Inject
    DebtNewPresenter presenter;

    //依附的Activity
    private FragmentActivity activity;

    /**
     * 该字段不为空，则为新增账单模式
     */
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
        firstDate.setText(dateFormat.format(date));
        firstDate.setTag(date);

        times.setText("6个月");
        times.setTag(6);

        //账单名称
        accountName.addTextChangedListener(new EtTextLengthWatcher(debtChannel, accountName, 16 * 2));

        remark.addTextChangedListener(new EtTextLengthWatcher(remark, 20 * 2));
        payMoney.addTextChangedListener(new EtAmountWatcher(payMoney));

        //限制小数位
        EditTextUtils.addDecimalDigitsInputFilter(payMoney);
        //限制emoji输入
        EditTextUtils.addDisableEmojiInputFilter(remark);
        EditTextUtils.addDisableEmojiInputFilter(accountName);
    }

    @Override
    public void initDatas() {

        activity = getActivity();

        if (debtDetail != null) {
            presenter.attachDebtDetail(debtDetail);
        }

        if (debtChannel != null && "custom".equals(debtChannel.getType())) {
            accountNameRoot.setVisibility(View.VISIBLE);
        } else {
            accountNameRoot.setVisibility(View.GONE);
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
                /**
                 * 防止重复点击
                 */
                if (!FastClickUtils.isFastClick()) {
                    presenter.saveEvenDebt((Date) firstDate.getTag(), times.getTag() + "", payMoney.getText().toString(),
                            "", remark.getText().toString());
                }
                break;
        }
    }


    /**
     * 空事件
     */
    @Override
    public void setPresenter(DebtNewContract.Presenter presenter) {
        //
    }

    /**
     * xhb 猜测是在编辑的时候用的
     * @param debtDetail 原有账单信息
     */
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
//        ToastUtils.showShort(getContext(), msg, R.mipmap.white_success);
        Intent intent = new Intent(getContext(), MainActivity.class);
        intent.putExtra("account", true);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        if (getActivity() != null) {
            getActivity().finish();
        }
    }
}
