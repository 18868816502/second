package com.beiwo.klyjaz.tang.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.beiwo.klyjaz.App;
import com.beiwo.klyjaz.BuildConfig;
import com.beiwo.klyjaz.R;
import com.beiwo.klyjaz.api.Api;
import com.beiwo.klyjaz.base.BaseComponentActivity;
import com.beiwo.klyjaz.entity.BlackList;
import com.beiwo.klyjaz.entity.Phone;
import com.beiwo.klyjaz.helper.SlidePanelHelper;
import com.beiwo.klyjaz.helper.UserHelper;
import com.beiwo.klyjaz.injection.component.AppComponent;
import com.beiwo.klyjaz.tang.StringUtil;
import com.beiwo.klyjaz.tang.rx.RxResponse;
import com.beiwo.klyjaz.tang.rx.observer.ApiObserver;
import com.beiwo.klyjaz.ui.activity.UserProtocolActivity;
import com.beiwo.klyjaz.util.CommonUtils;
import com.beiwo.klyjaz.util.ToastUtil;
import com.beiwo.klyjaz.view.ClearEditText;
import com.gyf.barlibrary.ImmersionBar;
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

    private boolean checked = true;
    private String realName, idNo, phoneNo, authCode;
    private Map<String, Object> map = new HashMap<>();
    private TimeCounter timeCounter = new TimeCounter(60 * 1000, 1000);
    private String webViewUrl = "";
    private View.OnClickListener listener1 = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getApplicationContext(), UserProtocolActivity.class);
            intent.putExtra("type", 3);
            startActivity(intent);
        }
    };
    private View.OnClickListener listener2 = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getApplicationContext(), UserProtocolActivity.class);
            intent.putExtra("type", 1);
            startActivity(intent);
        }
    };
    private View.OnClickListener listener3 = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getApplicationContext(), UserProtocolActivity.class);
            intent.putExtra("type", 2);
            startActivity(intent);
        }
    };

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
        String appName = getString(R.string.app_name);
        String origin = String.format(getString(R.string.user_protocal), appName, appName, appName);
        CharSequence char1 = protocolSpan(origin, 7, 13 + appName.length(), listener1);
        CharSequence char2 = protocolSpan(char1, 13 + appName.length(), 19 + appName.length() * 2, listener2);
        CharSequence char3 = protocolSpan(char2, 19 + appName.length() * 2, origin.length(), listener3);
        tv_user_protocal.setMovementMethod(LinkMovementMethod.getInstance());
        tv_user_protocal.setText(char3);

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
    protected void onStop() {
        super.onStop();
        if (timeCounter != null) timeCounter.onFinish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
    }

    @Override
    protected void configureComponent(AppComponent appComponent) {
    }

    @OnClick({R.id.tv_query_auth, R.id.tv_query_start, R.id.iv_agree_protocal})
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
                Api.getInstance().queryCredit(map)
                        .compose(RxResponse.<BlackList>compatT())
                        .subscribe(new ApiObserver<BlackList>() {
                            @Override
                            public void onNext(@NonNull BlackList data) {
                                Intent intent = new Intent(CreditQueryActivity.this, CreditResultActivity.class);
                                intent.putExtra("webViewUrl", generateUrl(data.getBlackList()));//黑名单用户 0-否 1-是
                                intent.putExtra("title", "信用查询");
                                startActivity(intent);
                            }
                        });
                break;
            case R.id.iv_agree_protocal:
                iv_agree_protocal.setImageResource(!checked ? R.drawable.btn_open_rb : R.drawable.btn_close_rb);
                checked = !checked;
                tv_query_start.setEnabled(right());
                break;
            default:
                break;
        }
    }

    private CharSequence protocolSpan(CharSequence str, int start, int end, View.OnClickListener clickListener) {
        SpannableString result = new SpannableString(str);
        ForegroundColorSpan colorSpan = new ForegroundColorSpan(ContextCompat.getColor(this, R.color.protocol_txt_color));
        result.setSpan(new SpanClick(clickListener), start, end, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        result.setSpan(colorSpan, start, end, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        return result;
    }

    private String generateUrl(int black) {
        webViewUrl = BuildConfig.H5_DOMAIN_NEW + "/activity/page/activity-credit-query.html" +
                "?userId=" + UserHelper.getInstance(this).id() + "&packageId=" + App.sChannelId + "&version=" + BuildConfig.VERSION_NAME + "&title=信用查询&black=" + black;
        return webViewUrl;
    }

    private class SpanClick extends ClickableSpan implements View.OnClickListener {
        private View.OnClickListener clickListener;

        public SpanClick(View.OnClickListener clickListener) {
            this.clickListener = clickListener;
        }

        @Override
        public void onClick(View view) {
            clickListener.onClick(view);
        }

        @Override
        public void updateDrawState(TextPaint ds) {
            ds.setColor(ds.linkColor);
            ds.setUnderlineText(false);//去除超链接的下划线
        }
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
            tv_query_auth.setText("获取");
            tv_query_auth.setEnabled(true);
        }
    }
}