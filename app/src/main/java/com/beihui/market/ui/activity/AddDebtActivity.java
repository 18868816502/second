package com.beihui.market.ui.activity;


import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.beihui.market.R;
import com.beihui.market.api.NetConstants;
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
import com.beihui.market.util.viewutils.ToastUtils;
import com.beihui.market.view.pickerview.OptionsPickerView;
import com.beihui.market.view.pickerview.TimePickerView;
import com.gyf.barlibrary.ImmersionBar;

import java.nio.charset.Charset;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
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
import static com.beihui.market.util.CommonUtils.convertAmount;

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
                    InputMethodUtil.closeSoftKeyboard(AddDebtActivity.this);
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
                    int term = 1;
                    if (debtLifeContent.getText().length() > 0) {
                        try {
                            term = Integer.parseInt(debtLifeContent.getText().toString());
                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                        }
                    }
                    for (int i = 1; i <= term; ++i) {
                        list.add(i + "");
                    }
                    pickerView.setPicker(list);
                    pickerView.setSelectOptions(termNum - 1);
                    pickerView.show();
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

    private Date storedStartDate;
    private String storedCapital;
    private String storedDebtName;
    private String storedRemark;

    @Override
    public int getLayoutId() {
        return R.layout.activity_add_debt;
    }

    @Override
    public void configViews() {
        ImmersionBar.with(this).titleBar(toolbar).statusBarDarkFont(true).init();
        setupToolbarBackNavigation(toolbar, R.mipmap.left_arrow_black);

        desView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddDebtActivity.this, ComWebViewActivity.class);
                intent.putExtra("url", NetConstants.H5_ADD_DEBT_EXPLAIN);
                intent.putExtra("title", "记账说明");
                intent.putExtra("style", "light");
                startActivity(intent);
            }
        });

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

    @SuppressLint("SetTextI18n")
    @Override
    public void showAttachData(int payMethod, int termLife, String startDate, double capital, double debtAmount, double termAmount, String projectName, String remark) {
        Date date = null;
        try {
            date = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA).parse(startDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String startDateStr = dateFormat.format(date);
        switch (payMethod) {
            case METHOD_ONE_TIME:
                oneTimeHolder.debtLifeContent.setText(termLife + "");
                oneTimeHolder.debtStartDateContent.setText(startDateStr);
                oneTimeHolder.debtStartDateContent.setTag(date);
                oneTimeHolder.debtCapitalAmount.setText(convertAmount(capital));
                oneTimeHolder.debtAmount.setText(convertAmount(debtAmount));
                if (!TextUtils.isEmpty(remark)) {
                    oneTimeHolder.remark.setText(remark);
                }
                break;
            case METHOD_EVEN_DEBT:
                evenDebtHolder.debtLifeContent.setText(termLife + "");
                evenDebtHolder.debtStartDateContent.setText(startDateStr);
                evenDebtHolder.debtStartDateContent.setTag(date);
                evenDebtHolder.debtCapitalAmount.setText(convertAmount(capital));
                evenDebtHolder.termAmount.setText(convertAmount(termAmount));
                evenDebtHolder.debtName.setText(projectName);
                if (!TextUtils.isEmpty(remark)) {
                    evenDebtHolder.remark.setText(remark);
                }
                break;
            case METHOD_EVEN_CAPITAL:
                evenCapitalHolder.debtLifeContent.setText(termLife + "");
                evenCapitalHolder.debtStartDateContent.setText(startDateStr);
                evenCapitalHolder.debtStartDateContent.setTag(date);
                evenCapitalHolder.debtCapitalAmount.setText(convertAmount(capital));
                evenCapitalHolder.debtAmountTh.setText(convertAmount(termAmount));
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
        pickerView.setSelectOptions(this.method - 1);
        pickerView.show();
    }

    @Override
    public void showSelectedMethod(int method, String methodName, String methodDes) {
        //记录当前输入数据
        storeDebtInfo(this.method);

        this.method = method;
        payMethodContent.setText(methodName);
        payMethodDes.setText(methodDes);
        View contentView;
        switch (method) {
            case METHOD_ONE_TIME:
                if (oneTimeHolder == null) {
                    oneTimeHolder = new OneTimeHolder(LayoutInflater.from(this)
                            .inflate(R.layout.layout_debt_add_method_one_time, methodContainer, false));
                    Date now = new Date(System.currentTimeMillis());
                    oneTimeHolder.debtStartDateContent.setTag(now);
                    oneTimeHolder.debtStartDateContent.setText(dateFormat.format(now));

                    oneTimeHolder.debtLifeContent.addTextChangedListener(new DebtLifeTextWatcher(oneTimeHolder.debtLifeContent, 360 * 50));
                    oneTimeHolder.debtCapitalAmount.addTextChangedListener(new DebtAmountTextWatcher(oneTimeHolder.debtCapitalAmount));
                    oneTimeHolder.debtAmount.addTextChangedListener(new DebtAmountTextWatcher(oneTimeHolder.debtAmount));
                    oneTimeHolder.remark.addTextChangedListener(new DebtNameTextWatcher(oneTimeHolder.remark, 20 * 2));
                }
                restoreDebtInfo(oneTimeHolder.debtStartDateContent, oneTimeHolder.debtCapitalAmount, null, oneTimeHolder.remark);
                contentView = oneTimeHolder.itemView;
                break;
            case METHOD_EVEN_DEBT:
                if (evenDebtHolder == null) {
                    evenDebtHolder = new EvenDebtHolder(LayoutInflater.from(this)
                            .inflate(R.layout.layout_debt_add_method_even_debt, methodContainer, false));
                    Date now = new Date(System.currentTimeMillis());
                    evenDebtHolder.debtStartDateContent.setTag(now);
                    evenDebtHolder.debtStartDateContent.setText(dateFormat.format(now));

                    evenDebtHolder.debtLifeContent.addTextChangedListener(new DebtLifeTextWatcher(evenDebtHolder.debtLifeContent, 12 * 50));
                    evenDebtHolder.debtCapitalAmount.addTextChangedListener(new DebtAmountTextWatcher(evenDebtHolder.debtCapitalAmount));
                    evenDebtHolder.termAmount.addTextChangedListener(new DebtAmountTextWatcher(evenDebtHolder.termAmount));
                    evenDebtHolder.debtName.addTextChangedListener(new DebtNameTextWatcher(evenDebtHolder.debtName, 5 * 2));
                    evenDebtHolder.remark.addTextChangedListener(new DebtNameTextWatcher(evenDebtHolder.remark, 20 * 2));
                }
                restoreDebtInfo(evenDebtHolder.debtStartDateContent, evenDebtHolder.debtCapitalAmount, evenDebtHolder.debtName, evenDebtHolder.remark);
                contentView = evenDebtHolder.itemView;
                break;
            case METHOD_EVEN_CAPITAL:
                if (evenCapitalHolder == null) {
                    evenCapitalHolder = new EvenCapitalHolder(LayoutInflater.from(this)
                            .inflate(R.layout.layout_debt_add_method_even_capital, methodContainer, false));
                    Date now = new Date(System.currentTimeMillis());
                    evenCapitalHolder.debtStartDateContent.setTag(now);
                    evenCapitalHolder.debtStartDateContent.setText(dateFormat.format(now));

                    evenCapitalHolder.debtLifeContent.addTextChangedListener(new DebtLifeTextWatcher(evenCapitalHolder.debtLifeContent, 12 * 50));
                    evenCapitalHolder.debtCapitalAmount.addTextChangedListener(new DebtAmountTextWatcher(evenCapitalHolder.debtCapitalAmount));
                    evenCapitalHolder.debtAmountTh.addTextChangedListener(new DebtAmountTextWatcher(evenCapitalHolder.debtAmountTh));
                    evenCapitalHolder.debtName.addTextChangedListener(new DebtNameTextWatcher(evenCapitalHolder.debtName, 5 * 2));
                    evenCapitalHolder.remark.addTextChangedListener(new DebtNameTextWatcher(evenCapitalHolder.remark, 20 * 2));
                }
                restoreDebtInfo(evenCapitalHolder.debtStartDateContent, evenCapitalHolder.debtCapitalAmount, evenCapitalHolder.debtName, evenCapitalHolder.remark);
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

    @Override
    public void onBackPressed() {
        InputMethodUtil.closeSoftKeyboard(this);
        super.onBackPressed();
    }

    private void showTimePicker(final TextView startDateView) {
        InputMethodUtil.closeSoftKeyboard(this);
        Calendar calendar = Calendar.getInstance(Locale.CHINA);
        calendar.setTime((Date) startDateView.getTag());
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
                .setTitleText("起息日期")
                .setTitleColor(getResources().getColor(R.color.black_1))
                .setTitleBgColor(Color.WHITE)
                .setBgColor(Color.WHITE)
                .setLabel("年", "月", "日", null, null, null)
                .isCenterLabel(false)
                .setDate(calendar)
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

    private void storeDebtInfo(int method) {
        Date startDate = null;
        String capital = null, debtName = null, remark = null;
        switch (method) {
            case METHOD_ONE_TIME: {
                startDate = (Date) oneTimeHolder.debtStartDateContent.getTag();
                capital = oneTimeHolder.debtCapitalAmount.getText().toString();
                remark = oneTimeHolder.remark.getText().toString();
                break;
            }
            case METHOD_EVEN_DEBT: {
                startDate = (Date) evenDebtHolder.debtStartDateContent.getTag();
                capital = evenDebtHolder.debtCapitalAmount.getText().toString();
                debtName = evenDebtHolder.debtName.getText().toString();
                remark = evenDebtHolder.remark.getText().toString();
                break;
            }
            case METHOD_EVEN_CAPITAL: {
                startDate = (Date) evenCapitalHolder.debtStartDateContent.getTag();
                capital = evenCapitalHolder.debtCapitalAmount.getText().toString();
                debtName = evenCapitalHolder.debtName.getText().toString();
                remark = evenCapitalHolder.remark.getText().toString();
                break;
            }
        }
        storedStartDate = startDate;
        storedCapital = capital;
        storedDebtName = debtName != null ? debtName : "";
        storedRemark = remark != null ? remark : "";
    }

    private void restoreDebtInfo(TextView startDateView, TextView capitalView, TextView debtNameView, TextView remarkView) {
        if (storedStartDate != null) {
            startDateView.setText(dateFormat.format(storedStartDate));
            startDateView.setTag(storedStartDate);
        }
        if (storedCapital != null) {
            capitalView.setText(storedCapital);
        }
        if (debtNameView != null && storedDebtName != null) {
            debtNameView.setText(storedDebtName);
        }
        if (storedRemark != null) {
            remarkView.setText(storedRemark);
        }
    }

    class DebtAmountTextWatcher implements TextWatcher {

        private EditText editText;

        DebtAmountTextWatcher(EditText editText) {
            this.editText = editText;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            try {
                double amount = Double.parseDouble(editText.getText().toString());
                if (amount >= 10000000) {
                    ToastUtils.showShort(AddDebtActivity.this, "金额不能超过1000万", null);
                    editText.getEditableText().delete(editText.length() - 1, editText.length());
                }
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    }

    class DebtLifeTextWatcher implements TextWatcher {

        private EditText editText;
        private int edge;

        DebtLifeTextWatcher(EditText editText, int edge) {
            this.editText = editText;
            this.edge = edge;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (editText.getEditableText().length() > 0) {
                int num = 0;
                try {
                    num = Integer.parseInt(editText.getText().toString());
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
                if (num > edge) {
                    ToastUtils.showShort(AddDebtActivity.this, "借款期限不能超过50年", null);
                    editText.getEditableText().delete(editText.length() - 1, editText.length());
                }
            }
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    }

    class DebtNameTextWatcher implements TextWatcher {

        private EditText editText;
        private int edge;

        DebtNameTextWatcher(EditText editText, int edge) {
            this.editText = editText;
            this.edge = edge;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (editText.getEditableText().length() > 0) {
                byte[] bytes = editText.getText().toString().getBytes(Charset.forName("gb2312"));
                if (bytes.length > edge) {
                    editText.getEditableText().delete(editText.length() - 1, editText.length());
                }
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    }
}
