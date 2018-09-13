package com.beihui.market.ui.activity;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.Toolbar;
import android.view.View;
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
import com.beihui.market.ui.presenter.CreditCardDebtNewPresenter;
import com.beihui.market.util.AndroidBug5497Fix;
import com.beihui.market.util.WeakRefToastUtil;
import com.beihui.market.view.pickerview.OptionsPickerView;
import com.gyf.barlibrary.ImmersionBar;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author xhb
 * 手动记账 编辑详情页面 信用卡
 *
 * TODO
 */
public class CreditCardDebtNewActivity extends BaseComponentActivity implements CreditCardDebtNewContract.View {

    private final int REQUEST_CODE_BANK_LIST = 1;

    @BindView(R.id.tb_credit_card_add_title_bar)
    Toolbar toolbar;
    //账单日
    @BindView(R.id.tv_credit_card_hand_account_date)
    TextView mAccountDate;
   //还款日
    @BindView(R.id.tv_credit_card_hand_pay_date)
    TextView mPayDate;

//    @BindView(R.id.credit_card_number)
//    EditText etCreditCardNumber;
//    @BindView(R.id.card_bank_block)
//    FrameLayout flCardBankBlock;
//    @BindView(R.id.bank_text)
//    TextView tvBankText;
//    @BindView(R.id.bank)
//    TextView tvBank;
//    @BindView(R.id.card_owner)
//    EditText etCardOwner;
//    @BindView(R.id.debt_day)
//    TextView tvDebtDay;
//    @BindView(R.id.debt_pay_day)
//    TextView tvDebtPayDay;
//    @BindView(R.id.debt_amount)
//    EditText etDebtAmount;

    @Inject
    CreditCardDebtNewPresenter presenter;

    private CreditCardBank creditCardBank;
    /**
     * 是否处于编辑模式
     */
    private boolean editMode;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE_BANK_LIST) {
            if (data != null && data.getParcelableExtra("bank") != null) {
                creditCardBank = data.getParcelableExtra("bank");

//                if (!TextUtils.isEmpty(creditCardBank.getBankName())) {
//                    tvBank.setText(creditCardBank.getBankName());
//                    tvBank.setTextColor(getResources().getColor(R.color.black_1));
//                }
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

//        etCardOwner.addTextChangedListener(new EtTextLengthWatcher(etCardOwner, 10 * 2));
//        etDebtAmount.addTextChangedListener(new EtAmountWatcher(etDebtAmount));
//
//        etCreditCardNumber.post(new Runnable() {
//            @Override
//            public void run() {
//                etCreditCardNumber.requestFocus();
//            }
//        });
//        //限制两位小数
//        EditTextUtils.addDecimalDigitsInputFilter(etDebtAmount);
//        //禁止输入emoji
//        EditTextUtils.addDisableEmojiInputFilter(etCardOwner);

        AndroidBug5497Fix.assistActivity(this);
        SlidePanelHelper.attach(this);
    }

    @Override
    public void initDatas() {
        CreditCardDebtDetail debtDetail = getIntent().getParcelableExtra("credit_card_debt_detail");
        if (debtDetail != null) {
            editMode = true;
            presenter.attachCreditCardDebt(debtDetail);
        }
    }

    @Override
    protected void configureComponent(AppComponent appComponent) {
        DaggerCreditCardDebtNewComponent.builder()
                .appComponent(appComponent)
                .creditCardDebtNewModule(new CreditCardDebtNewModule(this))
                .build()
                .inject(this);
    }


    /**
     * 点击事件
     */
    @OnClick({R.id.tv_credit_card_hand_account_date, R.id.tv_credit_card_hand_pay_date, R.id.cet_credit_card_hand_for_bank})
    public void onClick(View view) {
       switch (view.getId()) {
           //所属银行
           case R.id.cet_credit_card_hand_for_bank:
               startActivityForResult(new Intent(this, CreditCardBankActivity.class), REQUEST_CODE_BANK_LIST);
               break;
           //账单日
           case R.id.tv_credit_card_hand_account_date:
               showDatePicker(mAccountDate, "账单日");
               break;
           //还款日
           case R.id.tv_credit_card_hand_pay_date:
               showDatePicker(mPayDate, "还款日");
               break;
       }
    }

//    @OnClick({R.id.help_feedback, R.id.card_bank_block, R.id.debt_day_block, R.id.debt_pay_day_block, R.id.confirm})
//    void onItemClicked(View view) {
//        InputMethodUtil.closeSoftKeyboard(this);
//        switch (view.getId()) {
//            case R.id.help_feedback:
//                startActivity(new Intent(this, HelpAndFeedActivity.class));
//                break;
//            case R.id.card_bank_block:
//                startActivityForResult(new Intent(this, CreditCardBankActivity.class), REQUEST_CODE_BANK_LIST);
//                break;
//            case R.id.debt_day_block:
//                showDatePicker(tvDebtDay);
//                break;
//            case R.id.debt_pay_day_block:
//                showDatePicker(tvDebtPayDay);
//                break;
//            case R.id.confirm:
//                int billDay = tvDebtDay.getTag() != null ? (int) tvDebtDay.getTag() : 0;
//                int dueDay = tvDebtPayDay.getTag() != null ? (int) tvDebtPayDay.getTag() : 0;
//                if (editMode) {
//                    //编辑模式
//                    presenter.updateCreditCardDebt(billDay, dueDay, etDebtAmount.getText().toString());
//                } else {
//                    //新增模式
//                    presenter.saveCreditCardDebt(etCreditCardNumber.getText().toString(), creditCardBank != null ? creditCardBank.getId() + "" : "",
//                            etCardOwner.getText().toString(), billDay, dueDay, etDebtAmount.getText().toString());
//                }
//                break;
//        }
//    }

    @Override
    public void setPresenter(CreditCardDebtNewContract.Presenter presenter) {
        //
    }

    @Override
    public void showSaveCreditCardDebtSuccess(String msg) {
//        WeakRefToastUtil.showShort(this, msg, R.mipmap.white_success);
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

    @Override
    public void showUpdateCreditCardDebtSuccess(String msg) {
        WeakRefToastUtil.showShort(this, msg, R.mipmap.white_success);
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

    @Override
    public void bindOldCreditCardDebt(CreditCardDebtDetail debtDetail) {
//        //卡号
//        etCreditCardNumber.setText(debtDetail.getCardNums());
//        //姓名
//        etCardOwner.setText(debtDetail.getCardUserName());
//        //银行
//        creditCardBank = new CreditCardBank();
//        creditCardBank.setId(debtDetail.getBankId());
//        creditCardBank.setBankName(debtDetail.getBankName());
//        tvBank.setTextColor(getResources().getColor(R.color.black_1));
//        tvBank.setText(debtDetail.getBankName());
//        //账单日
//        tvDebtDay.setTextColor(getResources().getColor(R.color.black_1));
//        tvDebtDay.setText(String.format(Locale.CHINA, "每月%d日", debtDetail.getBillDay()));
//        tvDebtDay.setTag(debtDetail.getBillDay());
//        //还款日
//        tvDebtPayDay.setTextColor(getResources().getColor(R.color.black_1));
//        tvDebtPayDay.setText(String.format(Locale.CHINA, "每月%d日", debtDetail.getDueDay()));
//        tvDebtPayDay.setTag(debtDetail.getDueDay());
//        //金额
//        String amount = CommonUtils.keep2digitsWithoutZero(debtDetail.getShowBill().getNewBalance());
//        if (amount.contains(",")) {
//            amount = amount.replace(",", "");
//        }
//        etDebtAmount.setText(amount);
//        //编辑模式下卡号，银行，姓名都不可编辑
//        etCreditCardNumber.setFocusable(false);
//        etCreditCardNumber.setTextColor(getResources().getColor(R.color.black_2));
//
//        etCardOwner.setFocusable(false);
//        etCardOwner.setTextColor(getResources().getColor(R.color.black_2));
//
//        flCardBankBlock.setEnabled(false);
//        tvBankText.setCompoundDrawables(null, null, null, null);
//
//        tvBank.setTextColor(getResources().getColor(R.color.black_2));
//        tvBank.setPadding(0, 0, 0, 0);

    }

    /**
     * 账单日 还款日 选择时间的wheelView
     * @param textView
     */
    private void showDatePicker(TextView textView, String titleName) {
        final TextView target = textView;
        OptionsPickerView pickerView = new OptionsPickerView.Builder(this, new OptionsPickerView.OnOptionsSelectListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                target.setTextColor(getResources().getColor(R.color.black_1));
                int day = options1 + 1;
                target.setText(String.format(Locale.CHINA, "每月%d日", day));
                target.setTag(day);
            }
        }).setTitleBgColor(Color.parseColor("#ffffff"))
                .setCancelText("取消")
                .setCancelColor(Color.parseColor("#ff5240"))
                .setSubmitText("确认")
                .setSubmitColor(Color.parseColor("#ff5240"))
                .setTitleText(titleName)
                .setTitleColor(getResources().getColor(R.color.black_1))
                .build();

        List<String> list = new ArrayList<>();
        for (int i = 1; i <= 31; ++i) {
            list.add(String.format(Locale.CHINA, "每月%d日", i));
        }
        pickerView.setPicker(list);
        if (target.getTag() != null) {
            pickerView.setSelectOptions((Integer) target.getTag() - 1);
        }
        pickerView.show();
    }
}
