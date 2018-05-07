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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.beihui.market.R;
import com.beihui.market.base.BaseComponentFragment;
import com.beihui.market.entity.DebtChannel;
import com.beihui.market.entity.DebtDetail;
import com.beihui.market.helper.KeyBoardHelper;
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
import com.beihui.market.util.LogUtils;
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
    @BindView(R.id.ll_one_time_account_name_root)
    LinearLayout llAccountNameRoot;
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
     * 该字段不为空，则为新增账单模式
     */
    private DebtChannel debtChannel;
    /**
     * 该字段不为空，则为编辑账单模式
     */
    private DebtDetail debtDetail;

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

        activity = getActivity();

        if (debtDetail != null) {
            presenter.attachDebtDetail(debtDetail);
        }
        if (debtChannel != null && "custom".equals(debtChannel.getType())) {
            llAccountNameRoot.setVisibility(View.VISIBLE);
        } else {
            llAccountNameRoot.setVisibility(View.GONE);
        }
    }


    @Override
    public void configViews() {
        Date date = new Date();
        //设置到期还款日 为今天
        tvDebtPayDay.setText(dateFormat.format(date));
        tvDebtPayDay.setTag(date);

        //账单名称
        etAccountName.addTextChangedListener(new EtTextLengthWatcher(debtChannel, etAccountName, 16 * 2));
        //备注监听器
        etRemark.addTextChangedListener(new EtTextLengthWatcher(etRemark, 20 * 2));
        //应还金额监听器
        etDebtAmount.addTextChangedListener(new EtAmountWatcher(etDebtAmount));

        //限制小数位
        EditTextUtils.addDecimalDigitsInputFilter(etDebtAmount);
        //限制emoji输入
        EditTextUtils.addDisableEmojiInputFilter(etRemark);
        EditTextUtils.addDisableEmojiInputFilter(etAccountName);
    }



    @OnClick({R.id.tv_one_time_dead_time, R.id.tv_one_time_confirm_button})
    public void onClick(View view){
        InputMethodUtil.closeSoftKeyboard(getActivity());

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
                        .setCancelColor(Color.parseColor("#ff5200"))
                        .setSubmitText("确认")
                        .setSubmitColor(Color.parseColor("#ff5200"))
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
            //确认按钮
            case R.id.tv_one_time_confirm_button:
                presenter.saveOneTimeDebt((Date) tvDebtPayDay.getTag(), etDebtAmount.getText().toString(),  "",
                         "", etRemark.getText().toString());

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
     * 获取Fragment
     */
    public static DebtNewOneTimeFragment newInstance(DebtChannel debtChannel, DebtDetail debtDetail) {
        DebtNewOneTimeFragment fragment = new DebtNewOneTimeFragment();
        fragment.debtChannel = debtChannel;
        fragment.debtDetail = debtDetail;
        return fragment;
    }

    /**
     * xhb 猜测是在编辑的时候用的
     * @param debtDetail 原有账单信息
     */
    @Override
    public void bindDebtDetail(DebtDetail debtDetail) {
//        //到期还款日
//        try {
//            tvDebtPayDay.setTag(dateFormat.parse(debtDetail.getFirstRepayDate()));
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//        tvDebtPayDay.setText(debtDetail.getFirstRepayDate());
//        //借款金额
//        String amount = keep2digitsWithoutZero(debtDetail.getPayableAmount());
//        if (amount.contains(",")) {
//            amount = amount.replace(",", "");
//        }
//        etDebtAmount.setText(amount);
//        //备注
//        etRemark.setText(TextUtils.isEmpty(debtDetail.getRemark()) ? "" : debtDetail.getRemark());
//        //高级模板
//        if (debtDetail.getCapital() > 0) {
//            //展开高级模板
////            tvDebtInfoExpandCollapse.setSelected(true);
////            tvDebtInfoExpandCollapse.setText(tvDebtInfoExpandCollapse.isSelected() ? "隐藏更多信息" : "添加更多信息");
////            flDebtExtraInfoBlock.setVisibility(tvDebtInfoExpandCollapse.isSelected() ? View.VISIBLE : View.GONE);
//            //借款本金
//            String capital = keep2digitsWithoutZero(debtDetail.getCapital());
//            if (capital.contains(",")) {
//                capital = capital.replace(",", "");
//            }
////            etDebtCapitalAmount.setText(capital);
////            //借款期限
////            int dayLimit = debtDetail.getTerm();
////            tvDebtTimeLimit.setText(dayLimit + "天");
////            tvDebtTimeLimit.setTag(dayLimit);
//        }
    }

    /**
     * 保存账单成功的回调
     */
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
