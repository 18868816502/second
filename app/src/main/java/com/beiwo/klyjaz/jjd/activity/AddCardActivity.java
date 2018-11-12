package com.beiwo.klyjaz.jjd.activity;

import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.beiwo.klyjaz.R;
import com.beiwo.klyjaz.api.Api;
import com.beiwo.klyjaz.base.BaseComponentActivity;
import com.beiwo.klyjaz.helper.SlidePanelHelper;
import com.beiwo.klyjaz.helper.UserHelper;
import com.beiwo.klyjaz.injection.component.AppComponent;
import com.beiwo.klyjaz.jjd.BankInfoUtil;
import com.beiwo.klyjaz.jjd.bean.BankCard;
import com.beiwo.klyjaz.jjd.bean.BankName;
import com.beiwo.klyjaz.loan.BankCardTextWatcher;
import com.beiwo.klyjaz.tang.rx.RxResponse;
import com.beiwo.klyjaz.tang.rx.observer.ApiObserver;
import com.beiwo.klyjaz.util.ToastUtil;
import com.beiwo.klyjaz.view.ClearEditText;
import com.gyf.barlibrary.ImmersionBar;
import com.jakewharton.rxbinding2.view.RxView;
import com.jakewharton.rxbinding2.widget.RxTextView;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function3;

/**
 * https://gitee.com/tangbuzhi
 *
 * @author: Tangbuzhi
 * @version:
 * @package:
 * @description: 添加/编辑银行卡
 * @modify:
 * @date: 2018/9/15
 */
public class AddCardActivity extends BaseComponentActivity {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.cet_holder_name)
    ClearEditText cet_holder_name;
    @BindView(R.id.cet_card_no)
    ClearEditText cet_card_no;
    @BindView(R.id.cet_bank_name)
    ClearEditText cet_bank_name;
    @BindView(R.id.tv_add_card)
    TextView tv_add_card;

    @BindView(R.id.ll_card_wrap)
    LinearLayout ll_card_wrap;
    @BindView(R.id.iv_card_icon)
    ImageView iv_card_icon;
    @BindView(R.id.tv_card_name)
    TextView tv_card_name;
    @BindView(R.id.tv_card_num)
    TextView tv_card_num;
    @BindView(R.id.tv_holder_name)
    TextView tv_holder_name;

    private String holderName, cardNo, bankName;
    private Map<String, Object> map = new HashMap<>();
    private String cardId = "";

    @Override
    public int getLayoutId() {
        return R.layout.vest_activity_add_card;
    }

    @Override
    public void configViews() {
        setupToolbar(toolbar);
        ImmersionBar.with(this).statusBarDarkFont(true).init();
        SlidePanelHelper.attach(this);
    }

    @Override
    public void initDatas() {
        map.put("userId", UserHelper.getInstance(this).id());
        cet_holder_name.setMaxLenght(20);
        cet_card_no.setMaxLenght(23);
        cet_bank_name.setMaxLenght(20);

        try {
            cardId = getIntent().getStringExtra("cardId");
        } catch (Exception e) {
        }
        if (!TextUtils.isEmpty(cardId)) {
            Api.getInstance().queryCard(UserHelper.getInstance(this).id(), cardId)
                    .compose(RxResponse.<BankCard>compatT())
                    .subscribe(new ApiObserver<BankCard>() {
                        @Override
                        public void onNext(@NonNull BankCard data) {
                            cet_holder_name.setText(data.getCardholder());
                            cet_card_no.setText(data.getBankCardno());
                            cet_bank_name.setText(data.getBankName());
                        }
                    });
        }

        Observable<CharSequence> obName = RxTextView.textChanges(cet_holder_name);
        Observable<CharSequence> obCardId = RxTextView.textChanges(cet_card_no);
        Observable<CharSequence> obBankName = RxTextView.textChanges(cet_bank_name);
        Observable.combineLatest(obName, obCardId, obBankName, new Function3<CharSequence, CharSequence, CharSequence, Boolean>() {
            @Override
            public Boolean apply(@NonNull CharSequence name, @NonNull CharSequence card, @NonNull CharSequence bank) throws Exception {
                holderName = name.toString().trim();
                cardNo = card.toString().trim().replace(" ", "");
                bankName = bank.toString().trim();
                return right();
            }
        }).subscribe(new Consumer<Boolean>() {
            @Override
            public void accept(Boolean aBoolean) throws Exception {
                RxView.enabled(tv_add_card).accept(aBoolean);
            }
        });
        RxTextView.textChanges(cet_holder_name).subscribe(new Consumer<CharSequence>() {
            @Override
            public void accept(CharSequence holder) throws Exception {
                tv_holder_name.setText(TextUtils.isEmpty(holder) ? "持卡人：xxx" : "持卡人：" + holder);
            }
        });
        BankCardTextWatcher.watch(cet_card_no);
        RxTextView.textChanges(cet_card_no)
                .subscribe(new Consumer<CharSequence>() {
                    @Override
                    public void accept(CharSequence card) throws Exception {
                        if (!TextUtils.isEmpty(card)) {
                            if (card.length() > 20) {
                                tv_card_num.setTextSize(24.3f);
                            } else tv_card_num.setTextSize(29);
                            tv_card_num.setText(card);
                        } else {
                            cardBank();
                            tv_card_num.setText(getString(R.string.card_num_default));
                            tv_card_num.setTextSize(27.5f);
                        }
                        if (isBankCard(cardNo)) {
                            Api.getInstance().bankName(cardNo)
                                    .compose(RxResponse.<BankName>compatT())
                                    .subscribe(new ApiObserver<BankName>() {
                                        @Override
                                        public void onNext(@NonNull BankName data) {
                                            if (!TextUtils.isEmpty(data.getBankName())) {
                                                cet_bank_name.setEnabled(false);
                                                String bankName = data.getBankName();
                                                cet_bank_name.setText(bankName);
                                                tv_card_name.setText(bankName);
                                                Map<String, Integer> nameAndLogo = BankInfoUtil.bankNameAndLogo(bankName);
                                                assert nameAndLogo != null;
                                                Integer bankLogo = nameAndLogo.get("bankLogo");
                                                assert bankLogo != null;
                                                Integer bg = nameAndLogo.get("bg");
                                                iv_card_icon.setImageResource(bankLogo);
                                                iv_card_icon.setBackgroundResource(R.drawable.bg_oval_white);
                                                //ll_card_wrap.setBackgroundResource(BankInfoUtil.bgResource(bg));
                                            } else {
                                                cet_bank_name.setEnabled(true);
                                                cardBank();
                                            }
                                        }

                                        @Override
                                        public void onError(@NonNull Throwable t) {
                                            super.onError(t);
                                            cet_bank_name.setEnabled(true);
                                            cardBank();
                                        }
                                    });
                        }
                    }
                });
        RxView.clicks(tv_add_card)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {
                        if (!TextUtils.isEmpty(cardId)) map.put("cardId", cardId);
                        map.put("bankCardno", cardNo);
                        map.put("bankName", bankName);
                        map.put("reservePhone", "15072442326");
                        map.put("cardholder", holderName);
                        Api.getInstance().saveCard(map)
                                .compose(RxResponse.compatO())
                                .subscribe(new ApiObserver<Object>() {
                                    @Override
                                    public void onNext(@NonNull Object data) {
                                        if (TextUtils.isEmpty(cardId)) {
                                            ToastUtil.toast("添加银行卡成功");
                                        } else {
                                            ToastUtil.toast("更改银行卡成功");
                                        }
                                        finish();
                                    }
                                });
                    }
                });
    }

    /*private void setCardNum(CharSequence cardNum) {
        StringBuffer sb = new StringBuffer();
        CharSequence[] arr = new CharSequence[]{"xxxx", "xxxx", "xxxx", "xxxx", "xxx"};
        int i = cardNum.length() / 4;
        if (cardNum.length() >= 4 * i) {
            for (int k = 0; k < i; k++) {
                arr[k] = cardNum.subSequence(4 * k, 4 * (k + 1));
            }
            CharSequence ccc = cardNum.subSequence(4 * i, cardNum.length());
            arr[i] = ccc;
            *//*if (i == 4) {
                if (ccc.length() == 1) arr[i] = ccc + "xx";
                if (ccc.length() == 2) arr[i] = ccc + "x";
                if (ccc.length() == 3) arr[i] = ccc;
            } else {
                if (ccc.length() == 1) arr[i] = ccc + "xxx";
                if (ccc.length() == 2) arr[i] = ccc + "xx";
                if (ccc.length() == 3) arr[i] = ccc + "x";
                if (ccc.length() == 4) arr[i] = ccc;
            }*//*
        }
        for (int j = 0; j <= i; j++) {
            sb.append(" ").append(arr[j]);
        }
        tv_card_num.setText(sb.toString().trim());
    }*/

    private void cardBank() {
        tv_card_name.setText("");
        iv_card_icon.setImageResource(R.color.transparent);
        iv_card_icon.setBackgroundResource(R.color.transparent);
        ll_card_wrap.setBackgroundResource(R.drawable.bg_card1);
    }

    private boolean right() {
        boolean isName = !TextUtils.isEmpty(holderName);
        boolean isBank = !TextUtils.isEmpty(bankName);
        return isName && isBankCard(cardNo) && isBank;
    }

    private boolean isBankCard(String cardNo) {
        return !TextUtils.isEmpty(cardNo) && cardNo.length() <= 19 && cardNo.length() >= 15;//银行卡号15-19位数字
    }

    @Override
    protected void configureComponent(AppComponent appComponent) {
    }
}