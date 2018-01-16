package com.beihui.market.ui.activity;


import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.beihui.market.R;
import com.beihui.market.base.BaseComponentActivity;
import com.beihui.market.entity.DebtChannel;
import com.beihui.market.entity.DebtDetail;
import com.beihui.market.entity.PayPlan;
import com.beihui.market.helper.SlidePanelHelper;
import com.beihui.market.injection.component.AppComponent;
import com.beihui.market.injection.component.DaggerDebtAddComponent;
import com.beihui.market.injection.module.DebtAddModule;
import com.beihui.market.ui.contract.DebtAddContract;
import com.beihui.market.ui.presenter.DebtAddPresenter;
import com.beihui.market.util.AndroidBug5497Fix;
import com.beihui.market.util.InputMethodUtil;
import com.bigkoo.pickerview.OptionsPickerView;
import com.bigkoo.pickerview.TimePickerView;
import com.gyf.barlibrary.ImmersionBar;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.beihui.market.ui.contract.DebtAddContract.Presenter.METHOD_EVEN_CAPITAL;
import static com.beihui.market.ui.contract.DebtAddContract.Presenter.METHOD_EVEN_DEBT;
import static com.beihui.market.ui.contract.DebtAddContract.Presenter.METHOD_ONE_TIME;
import static com.beihui.market.util.CommonUtils.keep2digits;

public class AddDebtActivity extends BaseComponentActivity implements DebtAddContract.View {

    private static final int REQUEST_CODE_DEBT_CHANNEL = 1;
    private static final int REQUEST_CODE_DEBT_CONFIRM = 2;

    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy年MM月dd日", Locale.CHINA);

    @BindView(R.id.tool_bar)
    Toolbar toolbar;
    @BindView(R.id.des)
    View desView;

    @BindView(R.id.debt_channel_content)
    TextView debtChannelContent;
    @BindView(R.id.pay_method_content)
    TextView payMethodContent;
    @BindView(R.id.pay_method_des)
    TextView payMethodDes;

    @BindView(R.id.method_container)
    FrameLayout methodContainer;

    class OneTimeHolder {
        View itemView;
        @BindView(R.id.debt_life_content)
        EditText debtLifeContent;
        @BindView(R.id.debt_start_date_content)
        TextView debtStartDateContent;
        @BindView(R.id.debt_capital_amount)
        EditText debtCapitalAmount;
        @BindView(R.id.debt_amount)
        EditText debtAmount;
        @BindView(R.id.remark)
        EditText remark;

        OneTimeHolder(View itemView) {
            this.itemView = itemView;
            ButterKnife.bind(this, itemView);
        }

        @OnClick(R.id.debt_start_date)
        void onItemClicked() {
            showTimePicker(debtStartDateContent);
        }
    }

    class EvenCapitalHolder {
        View itemView;
        @BindView(R.id.debt_life_content)
        EditText debtLifeContent;
        @BindView(R.id.debt_start_date_content)
        TextView debtStartDateContent;
        @BindView(R.id.debt_capital_amount)
        EditText debtCapitalAmount;
        @BindView(R.id.debt_th)
        TextView debtTh;
        @BindView(R.id.debt_amount_th)
        EditText debtAmountTh;
        @BindView(R.id.debt_name)
        EditText debtName;
        @BindView(R.id.remark)
        EditText remark;

        int termNum;

        EvenCapitalHolder(View itemView) {
            this.itemView = itemView;
            ButterKnife.bind(this, itemView);

            selectTh(0);
        }

        @OnClick({R.id.debt_start_date, R.id.debt_th, R.id.debt_name_question})
        void onItemClicked(View view) {
            switch (view.getId()) {
                case R.id.debt_start_date:
                    showTimePicker(debtStartDateContent);
                    break;
                case R.id.debt_th:
                    if (debtLifeContent.getText().length() > 0) {
                        int term = 1;
                        try {
                            term = Integer.parseInt(debtLifeContent.getText().toString());
                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                        }
                        OptionsPickerView pickerView = new OptionsPickerView.Builder(AddDebtActivity.this, new OptionsPickerView.OnOptionsSelectListener() {
                            @Override
                            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                                selectTh(options1);
                            }
                        }).setCancelText("取消")
                                .setCancelColor(Color.parseColor("#5591ff"))
                                .setSubmitText("确认")
                                .setSubmitColor(Color.parseColor("#5591ff"))
                                .setTitleText("")
                                .setTitleColor(getResources().getColor(R.color.black_1))
                                .build();
                        List<String> list = new ArrayList<>();
                        for (int i = 1; i <= term; ++i) {
                            list.add(i + "");
                        }
                        pickerView.setPicker(list);
                        pickerView.show();
                    }
                    break;
                case R.id.debt_name_question:
                    showDebtNameQuestion();
                    break;
            }
        }

        void selectTh(int selected) {
            termNum = selected + 1;
            if (debtTh != null) {
                debtTh.setText("第" + termNum + "期");
            }
        }
    }

    class EvenDebtHolder {
        View itemView;
        @BindView(R.id.debt_life_content)
        EditText debtLifeContent;
        @BindView(R.id.debt_start_date_content)
        TextView debtStartDateContent;
        @BindView(R.id.debt_capital_amount)
        EditText debtCapitalAmount;
        @BindView(R.id.term_amount)
        EditText termAmount;
        @BindView(R.id.debt_name)
        EditText debtName;
        @BindView(R.id.remark)
        EditText remark;

        EvenDebtHolder(View itemView) {
            this.itemView = itemView;
            ButterKnife.bind(this, itemView);
        }

        @OnClick({R.id.debt_start_date, R.id.debt_name_question})
        void onItemClicked(View view) {
            switch (view.getId()) {
                case R.id.debt_start_date:
                    showTimePicker(debtStartDateContent);
                    break;
                case R.id.debt_name_question:
                    showDebtNameQuestion();
                    break;
            }
        }
    }

    @Inject
    DebtAddPresenter presenter;

    private OneTimeHolder oneTimeHolder;
    private EvenCapitalHolder evenCapitalHolder;
    private EvenDebtHolder evenDebtHolder;

    private int method;

    /**
     * 如果是借款详情页面进入编辑页面，则自动填充部分数据，并且在完成编辑后需要删除旧的借款
     */
    private DebtDetail pendingDebt;

    @Override
    public int getLayoutId() {
        return R.layout.activity_add_debt;
    }

    @Override
    public void configViews() {
        ImmersionBar.with(this).titleBar(toolbar).statusBarDarkFont(true).init();
        setupToolbarBackNavigation(toolbar, R.mipmap.left_arrow_black);

        SlidePanelHelper.attach(this);
        AndroidBug5497Fix.assistActivity(this);
    }

    @Override
    public void initDatas() {
        pendingDebt = getIntent().getParcelableExtra("pending_debt");
        //如果是编辑模式，则填充旧数据
        if (pendingDebt != null) {
            presenter.attachDebtDetail(pendingDebt);
        } else {
            presenter.onStart();
        }

    }

    @Override
    protected void configureComponent(AppComponent appComponent) {
        DaggerDebtAddComponent.builder()
                .appComponent(appComponent)
                .debtAddModule(new DebtAddModule(this))
                .build()
                .inject(this);
    }

    @Override
    public void setPresenter(DebtAddContract.Presenter presenter) {
        //
    }

    @Override
    public void showAttachData(int payMethod, int termLife, String startDate, double capital, double debtAmount, String projectName, String remark) {
        Date date = null;
        try {
            date = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA).parse(startDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        switch (payMethod) {
            case METHOD_ONE_TIME:
                oneTimeHolder.debtLifeContent.setText(termLife + "");
                oneTimeHolder.debtStartDateContent.setText(startDate);
                oneTimeHolder.debtStartDateContent.setTag(date);
                oneTimeHolder.debtCapitalAmount.setText(keep2digits(capital));
                oneTimeHolder.debtAmount.setText(keep2digits(debtAmount));
                if (!TextUtils.isEmpty(remark)) {
                    oneTimeHolder.remark.setText(remark);
                }
                break;
            case METHOD_EVEN_DEBT:
                evenDebtHolder.debtLifeContent.setText(termLife + "");
                evenDebtHolder.debtStartDateContent.setText(startDate);
                evenDebtHolder.debtStartDateContent.setTag(date);
                evenDebtHolder.debtCapitalAmount.setText(keep2digits(capital));
                evenDebtHolder.debtName.setText(projectName);
                if (!TextUtils.isEmpty(remark)) {
                    evenDebtHolder.remark.setText(remark);
                }
                break;
            case METHOD_EVEN_CAPITAL:
                evenCapitalHolder.debtLifeContent.setText(termLife + "");
                evenCapitalHolder.debtStartDateContent.setTag(startDate);
                evenCapitalHolder.debtStartDateContent.setTag(date);
                evenCapitalHolder.debtCapitalAmount.setText(keep2digits(capital));
                evenCapitalHolder.debtName.setText(projectName);
                if (!TextUtils.isEmpty(remark)) {
                    evenCapitalHolder.remark.setText(remark);
                }
                break;
        }
    }

    @Override
    public void showMethod(String[] methods) {
        InputMethodUtil.closeSoftKeyboard(this);
        OptionsPickerView pickerView = new OptionsPickerView.Builder(this, new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                presenter.clickPayMethod(options1);
            }
        }).setCancelText("取消")
                .setCancelColor(Color.parseColor("#5591ff"))
                .setSubmitText("确认")
                .setSubmitColor(Color.parseColor("#5591ff"))
                .setTitleText("还款方式")
                .setTitleColor(getResources().getColor(R.color.black_1))
                .build();
        pickerView.setPicker(Arrays.asList(methods));
        pickerView.show();
    }

    @Override
    public void showSelectedMethod(int method, String methodName, String methodDes) {
        this.method = method;
        payMethodContent.setText(methodName);
        payMethodDes.setText(methodDes);
        View contentView;
        switch (method) {
            case METHOD_ONE_TIME:
                if (oneTimeHolder == null) {
                    oneTimeHolder = new OneTimeHolder(LayoutInflater.from(this)
                            .inflate(R.layout.layout_debt_add_method_one_time, methodContainer, false));
                }
                contentView = oneTimeHolder.itemView;
                break;
            case METHOD_EVEN_DEBT:
                if (evenDebtHolder == null) {
                    evenDebtHolder = new EvenDebtHolder(LayoutInflater.from(this)
                            .inflate(R.layout.layout_debt_add_method_even_debt, methodContainer, false));
                }
                contentView = evenDebtHolder.itemView;
                break;
            case METHOD_EVEN_CAPITAL:
                if (evenCapitalHolder == null) {
                    evenCapitalHolder = new EvenCapitalHolder(LayoutInflater.from(this)
                            .inflate(R.layout.layout_debt_add_method_even_capital, methodContainer, false));
                }
                contentView = evenCapitalHolder.itemView;
                break;
            default:
                contentView = null;
        }
        methodContainer.removeAllViews();
        if (contentView != null) {
            methodContainer.addView(contentView);
        }
    }

    @Override
    public void showDebtChannel(DebtChannel channel) {
        debtChannelContent.setTextColor(getResources().getColor(R.color.black_1));
        debtChannelContent.setText(channel.getChannelName());
    }

    @Override
    public void navigatePayPlan(PayPlan payPlan) {
        Intent intent = new Intent(this, ConfirmPayPlanActivity.class);
        intent.putExtra("pay_plan", payPlan);
        if (pendingDebt != null) {
            intent.putExtra("pending_debt", pendingDebt);
        }
        startActivityForResult(intent, REQUEST_CODE_DEBT_CONFIRM);
    }

    @OnClick({R.id.debt_channel, R.id.pay_method, R.id.pay_method_question, R.id.confirm})
    void OnItemClicked(View view) {
        switch (view.getId()) {
            case R.id.debt_channel:
                startActivityForResult(new Intent(this, DebtChannelActivity.class), REQUEST_CODE_DEBT_CHANNEL);
                break;

            case R.id.pay_method:
                presenter.clickPayMethod();
                break;

            case R.id.pay_method_question:
                final Dialog dialog = new Dialog(this, 0);
                View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_pay_method_question, null);
                dialogView.findViewById(R.id.confirm).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog.setContentView(dialogView);
                dialog.setCanceledOnTouchOutside(false);
                Window window = dialog.getWindow();
                if (window != null) {
                    WindowManager.LayoutParams lp = window.getAttributes();
                    window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
                    window.setAttributes(lp);
                    window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                }
                dialog.show();
                break;

            case R.id.confirm:
                switch (method) {
                    case METHOD_ONE_TIME: {
                        presenter.saveDebt(null,
                                oneTimeHolder.debtCapitalAmount.getText().toString(),
                                oneTimeHolder.debtLifeContent.getText().toString(),
                                (Date) oneTimeHolder.debtStartDateContent.getTag(),
                                oneTimeHolder.debtAmount.getText().toString(), null, null, null,
                                oneTimeHolder.remark.getText().toString());
                        break;
                    }
                    case METHOD_EVEN_DEBT: {
                        presenter.saveDebt(evenDebtHolder.debtName.getText().toString(),
                                evenDebtHolder.debtCapitalAmount.getText().toString(),
                                evenDebtHolder.debtLifeContent.getText().toString(),
                                (Date) evenDebtHolder.debtStartDateContent.getTag(), null,
                                evenDebtHolder.termAmount.getText().toString(), null, null,
                                evenDebtHolder.remark.getText().toString());
                        break;
                    }
                    case METHOD_EVEN_CAPITAL:
                        presenter.saveDebt(evenCapitalHolder.debtName.getText().toString(),
                                evenCapitalHolder.debtCapitalAmount.getText().toString(),
                                evenCapitalHolder.debtLifeContent.getText().toString(),
                                (Date) evenCapitalHolder.debtStartDateContent.getTag(), null, null,
                                evenCapitalHolder.debtAmountTh.getText().toString(), evenCapitalHolder.termNum + "",
                                evenCapitalHolder.remark.getText().toString());
                        break;
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CODE_DEBT_CHANNEL) {
                presenter.selectDebtChannel((DebtChannel) data.getParcelableExtra("debt_channel"));
            } else if (requestCode == REQUEST_CODE_DEBT_CONFIRM) {
                setResult(RESULT_OK);
                finish();
            }

        }
    }

    private void showTimePicker(final TextView startDateView) {
        InputMethodUtil.closeSoftKeyboard(this);
        TimePickerView pickerView = new TimePickerView.Builder(this, new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                startDateView.setTag(date);
                startDateView.setText(dateFormat.format(date));
            }
        }).setType(new boolean[]{true, true, true, false, false, false})
                .setCancelText("取消")
                .setCancelColor(Color.parseColor("#5591ff"))
                .setSubmitText("确认")
                .setSubmitColor(Color.parseColor("#5591ff"))
                .setTitleText("还款日期")
                .setTitleColor(getResources().getColor(R.color.black_1))
                .setTitleBgColor(Color.WHITE)
                .setBgColor(Color.WHITE)
                .setLabel("年", "月", "日", null, null, null)
                .isCenterLabel(false)
                .build();
        pickerView.show();
    }

    private void showDebtNameQuestion() {
        final Dialog dialog = new Dialog(this, 0);
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_debt_name_question, null);
        dialogView.findViewById(R.id.confirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.setContentView(dialogView);
        dialog.setCanceledOnTouchOutside(false);
        Window window = dialog.getWindow();
        if (window != null) {
            WindowManager.LayoutParams lp = window.getAttributes();
            window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
            window.setAttributes(lp);
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
        dialog.show();
    }
}