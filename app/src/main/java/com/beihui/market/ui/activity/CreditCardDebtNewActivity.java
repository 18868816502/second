package com.beihui.market.ui.activity;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.beihui.market.R;
import com.beihui.market.base.BaseComponentActivity;
import com.beihui.market.entity.CreditCardBank;
import com.beihui.market.entity.CreditCardDebtDetail;
import com.beihui.market.helper.SlidePanelHelper;
import com.beihui.market.injection.component.AppComponent;
import com.beihui.market.injection.component.DaggerCreditCardDebtNewComponent;
import com.beihui.market.injection.module.CreditCardDebtNewModule;
import com.beihui.market.ui.contract.CreditCardDebtNewContract;
import com.beihui.market.ui.listeners.EtAmountWatcher;
import com.beihui.market.ui.listeners.EtTextLengthWatcher;
import com.beihui.market.ui.presenter.CreditCardDebtNewPresenter;
import com.beihui.market.util.InputMethodUtil;
import com.beihui.market.util.viewutils.ToastUtils;
import com.beihui.market.view.pickerview.OptionsPickerView;
import com.gyf.barlibrary.ImmersionBar;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

public class CreditCardDebtNewActivity extends BaseComponentActivity implements CreditCardDebtNewContract.View {

    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
    private final int REQUEST_CODE_BANK_LIST = 1;

    @BindView(R.id.tool_bar)
    Toolbar toolbar;
    @BindView(R.id.credit_card_number)
    EditText etCreditCardNumber;
    @BindView(R.id.bank)
    TextView tvBank;
    @BindView(R.id.card_owner)
    EditText etCardOwner;
    @BindView(R.id.debt_day)
    TextView tvDebtDay;
    @BindView(R.id.debt_pay_day)
    TextView tvDebtPayDay;
    @BindView(R.id.debt_amount)
    EditText etDebtAmount;

    @Inject
    CreditCardDebtNewPresenter presenter;

    private CreditCardBank creditCardBank;
    /**
     * 如果是编辑模式，则该字段不为空
     */
    private CreditCardDebtDetail debtDetail;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE_BANK_LIST) {
            if (data != null && data.getParcelableExtra("bank") != null) {
                creditCardBank = data.getParcelableExtra("bank");

                if (!TextUtils.isEmpty(creditCardBank.getBankName())) {
                    tvBank.setText(creditCardBank.getBankName());
                    tvBank.setTextColor(getResources().getColor(R.color.black_1));
                }
            }
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_credit_card_debt_new;
    }

    @Override
    public void configViews() {
        ImmersionBar.with(this).titleBar(toolbar).statusBarDarkFont(true).init();
        setupToolbarBackNavigation(toolbar, R.mipmap.left_arrow_black);

        etCardOwner.addTextChangedListener(new EtTextLengthWatcher(etCardOwner, 10 * 2));
        etDebtAmount.addTextChangedListener(new EtAmountWatcher(etDebtAmount));

        SlidePanelHelper.attach(this);
    }

    @Override
    public void initDatas() {

    }

    @Override
    protected void configureComponent(AppComponent appComponent) {
        DaggerCreditCardDebtNewComponent.builder()
                .appComponent(appComponent)
                .creditCardDebtNewModule(new CreditCardDebtNewModule(this))
                .build()
                .inject(this);
    }

    @OnClick({R.id.card_bank_block, R.id.debt_day_block, R.id.debt_pay_day_block, R.id.confirm})
    void onItemClicked(View view) {
        InputMethodUtil.closeSoftKeyboard(this);
        switch (view.getId()) {
            case R.id.card_bank_block:
                startActivityForResult(new Intent(this, CreditCardBankActivity.class), REQUEST_CODE_BANK_LIST);
                break;
            case R.id.debt_day_block:
                showDatePicker(tvDebtDay);
                break;
            case R.id.debt_pay_day_block:
                showDatePicker(tvDebtPayDay);
                break;
            case R.id.confirm:
                int billDay = tvDebtDay.getTag() != null ? (int) tvDebtDay.getTag() : 0;
                int dueDay = tvDebtPayDay.getTag() != null ? (int) tvDebtPayDay.getTag() : 0;
                presenter.saveCreditCardDebt(etCreditCardNumber.getText().toString(), creditCardBank != null ? creditCardBank.getId() + "" : "",
                        etCardOwner.getText().toString(), billDay, dueDay, etDebtAmount.getText().toString());
                break;
        }
    }

    @Override
    public void setPresenter(CreditCardDebtNewContract.Presenter presenter) {
        //
    }

    @Override
    public void showSaveCreditCardDebtSuccess() {
        ToastUtils.showShort(this, "提交成功", null);
        toolbar.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(CreditCardDebtNewActivity.this, MainActivity.class);
                intent.putExtra("account", true);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        }, 200);
    }

    private void showDatePicker(TextView textView) {
        final TextView target = textView;
        OptionsPickerView pickerView = new OptionsPickerView.Builder(this, new OptionsPickerView.OnOptionsSelectListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                target.setTextColor(getResources().getColor(R.color.black_1));
                int day = options1 + 1;
                target.setText(day + "日");
                target.setTag(day);
            }
        }).setCancelText("取消")
                .setCancelColor(Color.parseColor("#5591ff"))
                .setSubmitText("确认")
                .setSubmitColor(Color.parseColor("#5591ff"))
                .setTitleText("")
                .setTitleColor(getResources().getColor(R.color.black_1))
                .build();

        List<String> list = new ArrayList<>();
        for (int i = 1; i <= 31; ++i) {
            list.add(i + "");
        }
        pickerView.setPicker(list);
        if (target.getTag() != null) {
            pickerView.setSelectOptions((Integer) target.getTag() - 1);
        }
        pickerView.show();
    }
}
