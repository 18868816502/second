package com.beihui.market.jjd.activity;

import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.widget.TextView;

import com.beihui.market.R;
import com.beihui.market.api.Api;
import com.beihui.market.base.BaseComponentActivity;
import com.beihui.market.helper.SlidePanelHelper;
import com.beihui.market.helper.UserHelper;
import com.beihui.market.injection.component.AppComponent;
import com.beihui.market.jjd.bean.BankCard;
import com.beihui.market.jjd.bean.BankName;
import com.beihui.market.tang.StringUtil;
import com.beihui.market.tang.rx.RxResponse;
import com.beihui.market.tang.rx.observer.ApiObserver;
import com.beihui.market.util.ToastUtil;
import com.beihui.market.view.ClearEditText;
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
import io.reactivex.functions.Function4;

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
    @BindView(R.id.cet_phone_no)
    ClearEditText cet_phone_no;
    @BindView(R.id.tv_add_card)
    TextView tv_add_card;

    private String holderName, cardNo, bankName, phoneNo;
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
        cet_card_no.setMaxLenght(20);
        cet_bank_name.setMaxLenght(20);
        cet_phone_no.setMaxLenght(11);

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
                            cet_phone_no.setText(data.getReservePhone());
                        }
                    });
        }

        Observable<CharSequence> obName = RxTextView.textChanges(cet_holder_name);
        Observable<CharSequence> obCardId = RxTextView.textChanges(cet_card_no);
        Observable<CharSequence> obBankName = RxTextView.textChanges(cet_bank_name);
        Observable<CharSequence> obPhone = RxTextView.textChanges(cet_phone_no);
        Observable.combineLatest(obName, obCardId, obBankName, obPhone, new Function4<CharSequence, CharSequence, CharSequence, CharSequence, Boolean>() {
            @Override
            public Boolean apply(@NonNull CharSequence name, @NonNull CharSequence card,
                                 @NonNull CharSequence bank, @NonNull CharSequence phone) throws Exception {
                holderName = name.toString().trim();
                cardNo = card.toString().trim();
                bankName = bank.toString().trim();
                phoneNo = phone.toString().trim();
                return right();
            }
        }).subscribe(new Consumer<Boolean>() {
            @Override
            public void accept(Boolean aBoolean) throws Exception {
                RxView.enabled(tv_add_card).accept(aBoolean);
            }
        });
        RxTextView.textChanges(cet_card_no)
                .subscribe(new Consumer<CharSequence>() {
                    @Override
                    public void accept(CharSequence card) throws Exception {
                        if (isBankCard(cardNo)) {
                            Api.getInstance().bankName(cardNo)
                                    .compose(RxResponse.<BankName>compatT())
                                    .subscribe(new ApiObserver<BankName>() {
                                        @Override
                                        public void onNext(@NonNull BankName data) {
                                            if (!TextUtils.isEmpty(data.getBankName())) {
                                                cet_bank_name.setEnabled(false);
                                                cet_bank_name.setText(data.getBankName());
                                            } else cet_bank_name.setEnabled(true);
                                        }

                                        @Override
                                        public void onError(@NonNull Throwable t) {
                                            super.onError(t);
                                            cet_bank_name.setEnabled(true);
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
                        map.put("reservePhone", phoneNo);
                        map.put("bankName", bankName);
                        map.put("cardholder", holderName);
                        Api.getInstance().saveCard(map)
                                .compose(RxResponse.compatO())
                                .subscribe(new ApiObserver<Object>() {
                                    @Override
                                    public void onNext(@NonNull Object data) {
                                        ToastUtil.toast("添加银行卡成功");
                                        finish();
                                    }
                                });
                    }
                });
    }

    private boolean right() {
        boolean isName = !TextUtils.isEmpty(holderName);
        boolean isBank = !TextUtils.isEmpty(bankName);
        boolean isPhone = StringUtil.isPhone(phoneNo);
        return isName && isBankCard(cardNo) && isPhone && isBank;
    }

    private boolean isBankCard(String cardNo) {
        return !TextUtils.isEmpty(cardNo) && cardNo.length() <= 19 && cardNo.length() >= 15;//银行卡号15-19位数字
    }

    @Override
    protected void configureComponent(AppComponent appComponent) {
    }
}