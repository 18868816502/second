package com.beiwo.qnejqaz.tang;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.beiwo.qnejqaz.BuildConfig;
import com.beiwo.qnejqaz.R;
import com.beiwo.qnejqaz.api.Api;
import com.beiwo.qnejqaz.entity.Phone;
import com.beiwo.qnejqaz.entity.UserProfileAbstract;
import com.beiwo.qnejqaz.getui.GeTuiClient;
import com.beiwo.qnejqaz.helper.DataHelper;
import com.beiwo.qnejqaz.helper.UserHelper;
import com.beiwo.qnejqaz.tang.rx.RxResponse;
import com.beiwo.qnejqaz.tang.rx.observer.ApiObserver;
import com.beiwo.qnejqaz.ui.activity.UserProtocolActivity;
import com.beiwo.qnejqaz.util.SPUtils;
import com.beiwo.qnejqaz.view.ClearEditText;
import com.jakewharton.rxbinding2.view.RxView;
import com.jakewharton.rxbinding2.widget.RxTextView;

import org.greenrobot.eventbus.EventBus;

import java.lang.ref.WeakReference;

import io.reactivex.Observable;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;

/**
 * https://gitee.com/tangbuzhi
 *
 * @author: Tangbuzhi
 * @version:
 * @package:
 * @description:
 * @modify:
 * @date: 2018/7/22
 */

public class DlgUtil {
    public static void createDlg(Context context, int layoutId, OnDlgViewClickListener clickListener) {
        createDlg(context, layoutId, DlgLocation.CENTER, clickListener);
    }

    public static void createDlg(Context context, int layoutId, DlgLocation location, OnDlgViewClickListener clickListener) {
        Dialog dialog = new Dialog(context, 0) {
            @Override
            public void dismiss() {
                View view = getCurrentFocus();
                if (view instanceof TextView) {
                    InputMethodManager mInputMethodManager = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    mInputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.RESULT_UNCHANGED_SHOWN);
                }
                super.dismiss();
            }
        };
        View dlgView = LayoutInflater.from(context).inflate(layoutId, null);
        if (clickListener != null) clickListener.onViewClick(dialog, dlgView);
        dialog.setContentView(dlgView);
        dialog.setCanceledOnTouchOutside(true);
        Window window = dialog.getWindow();
        if (window != null) {
            WindowManager.LayoutParams lp = window.getAttributes();
            switch (location) {
                case CENTER:
                    window.setGravity(Gravity.CENTER);
                    break;
                case TOP:
                    window.setGravity(Gravity.TOP);
                    window.setWindowAnimations(R.style.anim_style_top2bottom);
                    break;
                case BOTTOM:
                    window.setGravity(Gravity.BOTTOM);
                    window.setWindowAnimations(R.style.anim_style_bottom2top);
                    break;
                default:
                    break;
            }
            window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
            window.setAttributes(lp);
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
        dialog.show();
    }

    public interface OnDlgViewClickListener {
        void onViewClick(Dialog dialog, View dlgView);
    }

    /*取消按钮*/
    public static void cancelClick(final Dialog dialog, View dlgView) {
        dlgView.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    public enum DlgLocation {
        TOP, CENTER, BOTTOM
    }

    /*窗口登陆模式*/
    private static String account, authcode, password;
    private static TimeCounter counter;
    private static boolean agree = true;

    public static void loginDlg(final Activity context, final OnLoginSuccessListener listener) {
        if (false) {//密码登陆弃用
            DlgUtil.createDlg(context, R.layout.dlg_login_pwd, new DlgUtil.OnDlgViewClickListener() {
                @SuppressLint("CheckResult")
                @Override
                public void onViewClick(final Dialog dialog, View dlgView) {
                    final ClearEditText cet_phone = dlgView.findViewById(R.id.cet_phone);
                    final ClearEditText cet_password = dlgView.findViewById(R.id.cet_password);
                    final TextView tv_login = dlgView.findViewById(R.id.tv_login);
                    cancelClick(dialog, dlgView);
                    cet_phone.setMaxLenght(11);
                    cet_password.setMaxLenght(16);
                    Observable<CharSequence> obPhone = RxTextView.textChanges(cet_phone);
                    Observable<CharSequence> obCode = RxTextView.textChanges(cet_password);
                    Observable.combineLatest(obPhone, obCode, new BiFunction<CharSequence, CharSequence, Boolean>() {
                        @Override
                        public Boolean apply(@Nullable CharSequence phone, @NonNull CharSequence code) throws Exception {
                            account = cet_phone.getText().toString().trim();
                            password = cet_password.getText().toString().trim();
                            return StringUtil.isPhone(account) && password != null && password.length() >= 6 && password.length() <= 16;
                        }
                    }).subscribe(new Consumer<Boolean>() {
                        @Override
                        public void accept(Boolean aBoolean) throws Exception {
                            RxView.enabled(tv_login).accept(aBoolean);
                        }
                    });
                    tv_login.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                            Api.getInstance().login(account, password)
                                    .compose(RxResponse.<UserProfileAbstract>compatT())
                                    .subscribe(new ApiObserver<UserProfileAbstract>() {
                                        @Override
                                        public void onNext(UserProfileAbstract data) {
                                            //登录之后，将用户信息注册到本地
                                            UserHelper.getInstance(context).update(data, account, context);
                                            //保存用户id,缓存
                                            SPUtils.setCacheUserId(data.getId());
                                            GeTuiClient.install();
                                            EventBus.getDefault().post("1");
                                            if (listener != null) listener.success(data);
                                        }
                                    });
                        }
                    });
                }
            });
        } else {
            DlgUtil.createDlg(context, R.layout.dlg_login, new DlgUtil.OnDlgViewClickListener() {
                @SuppressLint("CheckResult")
                @Override
                public void onViewClick(final Dialog dialog, View dlgView) {
                    final ClearEditText cet_phone = dlgView.findViewById(R.id.cet_phone);
                    final ClearEditText cet_ver_code = dlgView.findViewById(R.id.cet_ver_code);
                    TextView tv_get_ver_code = dlgView.findViewById(R.id.tv_get_ver_code);
                    final ImageView iv_agree = dlgView.findViewById(R.id.iv_agree);
                    TextView tv_protocol = dlgView.findViewById(R.id.tv_protocol);
                    final TextView tv_login = dlgView.findViewById(R.id.tv_login);
                    cancelClick(dialog, dlgView);
                    cet_phone.setMaxLenght(11);
                    cet_ver_code.setMaxLenght(4);
                    counter = new TimeCounter(60 * 1000, 1000, tv_get_ver_code);
                    Observable<CharSequence> obPhone = RxTextView.textChanges(cet_phone);
                    Observable<CharSequence> obCode = RxTextView.textChanges(cet_ver_code);
                    Observable.combineLatest(obPhone, obCode, new BiFunction<CharSequence, CharSequence, Boolean>() {
                        @Override
                        public Boolean apply(@Nullable CharSequence phone, @NonNull CharSequence code) throws Exception {
                            account = cet_phone.getText().toString().trim();
                            authcode = cet_ver_code.getText().toString().trim();
                            return StringUtil.isPhone(account) && authcode != null && authcode.length() == 4 && agree;
                        }
                    }).subscribe(new Consumer<Boolean>() {
                        @Override
                        public void accept(Boolean aBoolean) throws Exception {
                            RxView.enabled(tv_login).accept(aBoolean);
                        }
                    });
                    tv_get_ver_code.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            DataHelper.getInstance(context).event(DataHelper.EVENT_TYPE_CLICK, DataHelper.EVENT_VIEWID_LOGINPAGE, DataHelper.EVENT_EVENTID_GETCODE, 0);
                            Api.getInstance().requestSms(account, "6")
                                    .compose(RxResponse.<Phone>compatT())
                                    .subscribe(new ApiObserver<Phone>() {
                                        @Override
                                        public void onNext(@NonNull Phone data) {
                                            counter.start();
                                        }
                                    });
                        }
                    });
                    iv_agree.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            iv_agree.setImageResource(agree ? R.drawable.btn_off : R.drawable.btn_on);
                            agree = !agree;
                            tv_login.setEnabled(StringUtil.isPhone(account) && authcode != null && authcode.length() == 4 && agree);
                        }
                    });
                    tv_protocol.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            context.startActivity(new Intent(context, UserProtocolActivity.class));
                            dialog.dismiss();
                        }
                    });
                    tv_login.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            DataHelper.getInstance(context).event(DataHelper.EVENT_TYPE_CLICK, DataHelper.EVENT_VIEWID_LOGINPAGE, DataHelper.EVENT_EVENTID_LOGINIMMEDIATELY, 0);
                            dialog.dismiss();
                            Api.getInstance().loginByCode(account, authcode)
                                    .compose(RxResponse.<UserProfileAbstract>compatT())
                                    .subscribe(new ApiObserver<UserProfileAbstract>() {
                                        @Override
                                        public void onNext(@NonNull UserProfileAbstract data) {
                                            counter.onFinish();
                                            //登录之后，将用户信息注册到本地
                                            UserHelper.getInstance(context).update(data, account, context);
                                            //保存用户id,缓存
                                            SPUtils.setCacheUserId(data.getId());
                                            GeTuiClient.install();
                                            EventBus.getDefault().post("1");
                                            if (listener != null) listener.success(data);
                                        }
                                    });
                        }
                    });
                }
            });
        }
    }

    public interface OnLoginSuccessListener {
        void success(UserProfileAbstract data);
    }

    private static class TimeCounter extends CountDownTimer {
        WeakReference<TextView> reference;

        public TimeCounter(long millisInFuture, long countDownInterval, TextView tv_ver_code) {
            super(millisInFuture, countDownInterval);
            reference = new WeakReference(tv_ver_code);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            TextView tv = reference.get();
            if (tv != null) {
                tv.setEnabled(false);
                tv.setText(millisUntilFinished / 1000 + "s后获取");
            }
        }

        @Override
        public void onFinish() {
            TextView tv = reference.get();
            if (tv != null) {
                tv.setEnabled(true);
                tv.setText("获取验证码");
            }
        }
    }
}