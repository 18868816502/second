package com.beihui.market.tang.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.beihui.market.App;
import com.beihui.market.BuildConfig;
import com.beihui.market.R;
import com.beihui.market.api.Api;
import com.beihui.market.base.BaseComponentActivity;
import com.beihui.market.entity.BlackList;
import com.beihui.market.entity.Phone;
import com.beihui.market.helper.SlidePanelHelper;
import com.beihui.market.helper.UserHelper;
import com.beihui.market.injection.component.AppComponent;
import com.beihui.market.tang.StringUtil;
import com.beihui.market.tang.rx.RxResponse;
import com.beihui.market.tang.rx.observer.ApiObserver;
import com.beihui.market.ui.activity.UserProtocolActivity;
import com.beihui.market.util.CommonUtils;
import com.beihui.market.util.ToastUtil;
import com.beihui.market.view.ClearEditText;
import com.gyf.barlibrary.ImmersionBar;
import com.jakewharton.rxbinding2.InitialValueObservable;
import com.jakewharton.rxbinding2.view.RxView;
import com.jakewharton.rxbinding2.widget.RxTextView;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
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
 * @description:
 * @modify:
 * @date: 2018/9/6
 */

public class CreditQueryActivity extends BaseComponentActivity {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.hold_view)
    View hold_view;
    @BindView(R.id.cet_query_name)
    ClearEditText cet_query_name;
    @BindView(R.id.cet_query_id)
    ClearEditText cet_query_id;
    @BindView(R.id.cet_query_phone)
    ClearEditText cet_query_phone;
    @BindView(R.id.cet_query_authcode)
    ClearEditText cet_query_authcode;
    @BindView(R.id.tv_query_auth)
    TextView tv_query_auth;
    @BindView(R.id.tv_query_start)
    TextView tv_query_start;
    @BindView(R.id.tv_user_protocal)
    TextView tv_user_protocal;
    @BindView(R.id.iv_agree_protocal)
    ImageView iv_agree_protocal;
    @BindView(R.id.ll_protocal_wrap)
    LinearLayout ll_protocal_wrap;

    private boolean checked = true;
    private String realName, idNo, phoneNo, authCode;
    private Map<String, Object> map = new HashMap<>();
    private TimeCounter timeCounter = new TimeCounter(60 * 1000, 1000);
    private String webViewUrl = "";

    @Override
    public int getLayoutId() {
        return R.layout.f_activity_credit_query;
    }

    @Override
    public void configViews() {
        int statusHeight = CommonUtils.getStatusBarHeight(this);
        ViewGroup.LayoutParams params = hold_view.getLayoutParams();
        params.height = statusHeight;
        hold_view.setLayoutParams(params);
        hold_view.setBackgroundResource(R.color.refresh_one);

        setupToolbarBackNavigation(toolbar, R.drawable.back_white);
        ImmersionBar.with(this).statusBarDarkFont(false).init();
        SlidePanelHelper.attach(this);

        cet_query_name.setMaxLenght(20);
        cet_query_id.setMaxLenght(18);
        cet_query_phone.setMaxLenght(11);
        cet_query_authcode.setMaxLenght(4);
    }

    @Override
    public void initDatas() {
        tv_user_protocal.setText(String.format(getString(R.string.user_protocal), getString(R.string.app_name)));
        Observable<CharSequence> obName = RxTextView.textChanges(cet_query_name);
        Observable<CharSequence> obId = RxTextView.textChanges(cet_query_id);
        Observable<CharSequence> obPhone = RxTextView.textChanges(cet_query_phone);
        Observable<CharSequence> obCode = RxTextView.textChanges(cet_query_authcode);
        Observable.combineLatest(obName, obId, obPhone, obCode, new Function4<CharSequence, CharSequence, CharSequence, CharSequence, Boolean>() {
            @Override
            public Boolean apply(@NonNull CharSequence name, @NonNull CharSequence id,
                                 @NonNull CharSequence phone, @NonNull CharSequence code) throws Exception {
                realName = name.toString().trim();
                idNo = id.toString().trim();
                phoneNo = phone.toString().trim();
                authCode = code.toString().trim();
                return right();
            }
        }).subscribe(new Consumer<Boolean>() {
            @Override
            public void accept(Boolean aBoolean) throws Exception {
                RxView.enabled(tv_query_start).accept(aBoolean);
            }
        });
        map.put("userId", UserHelper.getInstance(this).id());
    }

    private boolean right() {
        boolean isName = !TextUtils.isEmpty(realName);
        boolean isID = !TextUtils.isEmpty(idNo) && idNo.length() == 18;
        boolean isPhone = StringUtil.isPhone(phoneNo);
        boolean isCode = !TextUtils.isEmpty(authCode) && authCode.length() == 4;
        return isName && isID && isPhone && isCode && checked;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (timeCounter != null){
            timeCounter.onFinish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
    }

    @Override
    protected void configureComponent(AppComponent appComponent) {
    }

    @OnClick({R.id.tv_query_auth, R.id.tv_query_start, R.id.iv_agree_protocal, R.id.ll_protocal_wrap})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_query_auth:
                Api.getInstance().requestSms(phoneNo, "5")
                        .compose(RxResponse.<Phone>compatT())
                        .subscribe(new ApiObserver<Phone>() {
                            @Override
                            public void onNext(@NonNull Phone data) {
                                timeCounter.start();
                                ToastUtil.toast("验证码已发送至" + data.getPhone().replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2"));
                            }
                        });
                break;
            case R.id.tv_query_start:
                map.put("realName", realName);
                map.put("idCard", idNo);
                map.put("phone", phoneNo);
                map.put("verifyCode", authCode);

//                Intent intent = new Intent(CreditQueryActivity.this, CreditResultActivity.class);
//                intent.putExtra("webViewUrl", generateUrl(1));
//                intent.putExtra("title", "信用查询");
//                startActivity(intent);

                Api.getInstance().queryCredit(map)
                        .compose(RxResponse.<BlackList>compatT())
                        .subscribe(new ApiObserver<BlackList>() {
                            @Override
                            public void onNext(@NonNull BlackList data) {
                                Intent intent = new Intent(CreditQueryActivity.this, CreditResultActivity.class);
                                intent.putExtra("webViewUrl", generateUrl(data.getBlackList()));
                                intent.putExtra("title", "信用查询");
                                startActivity(intent);
                                //ToastUtil.toast("是否黑名单：" + (data.getBlackList() == 1));//黑名单用户 0-否 1-是
                            }
                        });
                break;
            case R.id.iv_agree_protocal:
                iv_agree_protocal.setImageResource(!checked ? R.drawable.btn_open_rb : R.drawable.btn_close_rb);
                checked = !checked;
                tv_query_start.setEnabled(right());
                break;
            case R.id.ll_protocal_wrap:
                startActivity(new Intent(this, UserProtocolActivity.class));
                break;
            default:
                break;
        }
    }

    private String generateUrl(int black) {
        webViewUrl = "http://116.62.148.52/activity/page/activity-credit-query.html?userId="
                + UserHelper.getInstance(this).id() + "&packageId=" + App.sChannelId + "&version=" + BuildConfig.VERSION_NAME + "&title=信用查询&black=" + black;
        System.out.println(webViewUrl);
        return webViewUrl;
    }

    private class TimeCounter extends CountDownTimer {
        public TimeCounter(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            tv_query_auth.setText(millisUntilFinished / 1000 + "s");
            tv_query_auth.setEnabled(false);
        }

        @Override
        public void onFinish() {
            tv_query_auth.setText("重新获取");
            tv_query_auth.setEnabled(true);
        }
    }
}