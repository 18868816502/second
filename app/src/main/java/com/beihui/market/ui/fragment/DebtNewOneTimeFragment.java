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
import com.beihui.market.view.pickerview.OptionsPickerView;
import com.beihui.market.view.pickerview.TimePickerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

public class DebtNewOneTimeFragment extends BaseComponentFragment implements DebtNewContract.View {

    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);

    @BindView(R.id.debt_pay_day)
    TextView tvDebtPayDay;
    @BindView(R.id.debt_amount)
    EditText etDebtAmount;
    @BindView(R.id.debt_extra_info_block)
    FrameLayout flDebtExtraInfoBlock;
    @BindView(R.id.debt_capital_amount)
    EditText etDebtCapitalAmount;
    @BindView(R.id.debt_time_limit)
    TextView tvDebtTimeLimit;
    @BindView(R.id.remark)
    EditText etRemark;
    @BindView(R.id.debt_info_expand_or_collapse)
    TextView tvDebtInfoExpandCollapse;

    @Inject
    DebtNewPresenter presenter;

    private DebtChannel debtChannel;

    public static DebtNewOneTimeFragment newInstance(DebtChannel debtChannel) {
        DebtNewOneTimeFragment fragment = new DebtNewOneTimeFragment();
        fragment.debtChannel = debtChannel;
        return fragment;
    }

    @Override
    public int getLayoutResId() {
        return R.layout.fragment_debt_new_one_time;
    }

    @Override
    public void configViews() {
        Date date = new Date();
        tvDebtPayDay.setText(dateFormat.format(date));
        tvDebtPayDay.setTag(date);

        tvDebtTimeLimit.setText("30天");
        tvDebtTimeLimit.setTag(30);

        etRemark.addTextChangedListener(new EtTextLengthWatcher(etRemark, 20 * 2));
        etDebtAmount.addTextChangedListener(new EtAmountWatcher(etDebtAmount));
        etDebtCapitalAmount.addTextChangedListener(new EtAmountWatcher(etDebtCapitalAmount));

        tvDebtInfoExpandCollapse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.setSelected(!v.isSelected());
                tvDebtInfoExpandCollapse.setText(v.isSelected() ? "隐藏更多信息" : "添加更多信息");
                flDebtExtraInfoBlock.setVisibility(v.isSelected() ? View.VISIBLE : View.GONE);
            }
        });
    }

    @Override
    public void initDatas() {

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

    @OnClick({R.id.debt_pay_day_block, R.id.debt_time_limit_block, R.id.confirm})
    void onItemClicked(View view) {
        InputMethodUtil.closeSoftKeyboard(getActivity());
        switch (view.getId()) {
            case R.id.debt_pay_day_block: {
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
            case R.id.debt_time_limit_block: {
                OptionsPickerView pickerView = new OptionsPickerView.Builder(getContext(), new OptionsPickerView.OnOptionsSelectListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onOptionsSelect(int options1, int options2, int options3, View v) {
                        int dayLimit = options1 + 1;
                        tvDebtTimeLimit.setText(dayLimit + "天");
                        tvDebtTimeLimit.setTag(dayLimit);
                    }
                }).setCancelText("取消")
                        .setCancelColor(Color.parseColor("#5591ff"))
                        .setSubmitText("确认")
                        .setSubmitColor(Color.parseColor("#5591ff"))
                        .setTitleText("")
                        .setTitleColor(getResources().getColor(R.color.black_1))
                        .build();

                List<String> list = new ArrayList<>();
                for (int i = 1; i <= 360; ++i) {
                    list.add(i + "");
                }
                pickerView.setPicker(list);
                pickerView.setSelectOptions((Integer) tvDebtTimeLimit.getTag() - 1);
                pickerView.show();
                break;
            }
            case R.id.confirm:
                boolean hasExtraInfo = tvDebtInfoExpandCollapse.isSelected() && !TextUtils.isEmpty(etDebtCapitalAmount.getText().toString());
                presenter.saveOneTimeDebt((Date) tvDebtPayDay.getTag(), etDebtAmount.getText().toString(), hasExtraInfo ? etDebtCapitalAmount.getText().toString() : "",
                        hasExtraInfo ? tvDebtTimeLimit.getTag() + "" : "", etRemark.getText().toString());
                break;
        }
    }

    @Override
    public void setPresenter(DebtNewContract.Presenter presenter) {
        //
    }

    @Override
    public void saveDebtSuccess() {
        ToastUtils.showShort(getContext(), "提交成功", null);
        tvDebtInfoExpandCollapse.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(getContext(), MainActivity.class);
                intent.putExtra("account", true);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                if (getActivity() != null) {
                    getActivity().finish();
                }
            }
        }, 200);
    }
}
