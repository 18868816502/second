package com.beihui.market.ui.activity;


import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Interpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.beihui.market.R;
import com.beihui.market.base.BaseComponentActivity;
import com.beihui.market.entity.AdBanner;
import com.beihui.market.helper.ActivityTracker;
import com.beihui.market.injection.component.AppComponent;
import com.beihui.market.ui.busevents.AuthNavigationEvent;
import com.beihui.market.ui.busevents.UserLoginEvent;
import com.beihui.market.ui.busevents.UserLoginWithPendingTaskEvent;
import com.beihui.market.ui.dialog.CommNoneAndroidDialog;
import com.beihui.market.ui.fragment.UserLoginFragment;
import com.beihui.market.ui.fragment.UserRegisterSetPsdFragment;
import com.beihui.market.ui.fragment.UserRegisterVerifyCodeFragment;
import com.beihui.market.umeng.Events;
import com.beihui.market.umeng.Statistic;
import com.beihui.market.util.InputMethodUtil;
import com.beihui.market.view.drawable.BlurringDrawable;
import com.gyf.barlibrary.ImmersionBar;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 用户登录页面 Activity
 */
public class UserAuthorizationActivity extends BaseComponentActivity {

    @BindView(R.id.root_container)
    View rootContainer;
    @BindView(R.id.deco_container)
    View decoContainer;

    @BindView(R.id.cancel)
    TextView cancelTv;
    @BindView(R.id.navigation)
    ImageView navigationIv;

    private BlurringDrawable blurringDrawable;

    /**
     * 点击广告跳转到登录页面，登录完成后需要完成后续跳转动作
     */
    private AdBanner pendingAd;

    public static void launch(Activity context, String phone) {
        Intent intent = new Intent(context, UserAuthorizationActivity.class);
        if (phone != null) {
            intent.putExtra("phone", phone);
        }
        context.startActivity(intent);
        context.overridePendingTransition(0, 0);
    }

    public static void launchWithPending(Activity context, AdBanner adBanner) {
        Intent intent = new Intent(context, UserAuthorizationActivity.class);
        if (adBanner != null) {
            intent.putExtra("pendingAd", adBanner);
        }
        context.startActivity(intent);
        context.overridePendingTransition(0, 0);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);

        pendingAd = getIntent().getParcelableExtra("pendingAd");
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        if (blurringDrawable != null) {
            blurringDrawable.unbindBlurredView();
        }
        super.onDestroy();
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_user_authorization;
    }

    @Override
    public void configViews() {
        ImmersionBar.with(this).fitsSystemWindows(true).init();

        Bundle bundle = null;
        if (getIntent() != null && getIntent().getStringExtra("phone") != null) {
            bundle = new Bundle();
            bundle.putString("phone", getIntent().getStringExtra("phone"));
        }
        UserLoginFragment fragment = new UserLoginFragment();
        if (bundle != null) {
            fragment.setArguments(bundle);
        }
        getSupportFragmentManager().beginTransaction()
                .add(R.id.content_container, fragment, UserLoginFragment.class.getSimpleName())
                .commit();
        decoContainer.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    decoContainer.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                } else {
                    decoContainer.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                }
                decoContainer.setTranslationY(decoContainer.getMeasuredHeight());
                decoContainer.animate()
                        .translationY(0)
                        .setDuration(1000)
                        .setInterpolator(new Interpolator() {
                            @Override
                            public float getInterpolation(float input) {
                                float factor = 0.88F;
                                return (float) (Math.pow(2, -10 * input) * Math.sin((input - factor / 4) * (2 * Math.PI) / factor) + 1);
                            }
                        })
                        .start();
            }
        });
    }

    @Override
    public void initDatas() {

    }

    @Override
    protected void configureComponent(AppComponent appComponent) {
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        //this is a DialogTheme activity, set window size here
        Window window = getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        window.setAttributes(lp);

        if (ActivityTracker.getInstance().getLastActivity() != null) {
            blurringDrawable = new BlurringDrawable(this);
            blurringDrawable.bindBlurredView(ActivityTracker.getInstance().getLastActivity().getWindow().getDecorView());
            window.setBackgroundDrawable(blurringDrawable);
        }
    }


    @OnClick({R.id.cancel, R.id.navigation})
    void OnViewClicked(View view) {
        if (view.getId() == R.id.cancel) {
            //umeng统计
            Statistic.onEvent(Events.LOGIN_CANCEL);
        }
        onBackPressed();
    }

    /**
     *
     */
    @Subscribe
    public void onAuthorizationNavigation(AuthNavigationEvent event) {
        if (event.navigationTag == AuthNavigationEvent.TAG_REGISTER) {
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.setCustomAnimations(R.anim.auth_enter, R.anim.auth_exit, R.anim.auth_enter, R.anim.auth_exit);

            Fragment login = fm.findFragmentByTag(UserLoginFragment.class.getSimpleName());
            ft.detach(login);

            String registerTag = UserRegisterVerifyCodeFragment.class.getSimpleName();
            Fragment verifyCode = new UserRegisterVerifyCodeFragment();
            ft.add(R.id.content_container, verifyCode, registerTag);
            ft.attach(verifyCode);
            ft.addToBackStack(registerTag);
            ft.commit();

            cancelTv.setVisibility(View.GONE);
            navigationIv.setVisibility(View.VISIBLE);
        } else if (event.navigationTag == AuthNavigationEvent.TAG_SET_PSD) {
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.setCustomAnimations(R.anim.auth_enter, R.anim.auth_exit, R.anim.auth_enter, R.anim.auth_exit);

            Fragment verifyCode = fm.findFragmentByTag(UserRegisterVerifyCodeFragment.class.getSimpleName());
            ft.detach(verifyCode);

            String setPsdTag = UserRegisterSetPsdFragment.class.getSimpleName();
            Fragment setPsd = fm.findFragmentByTag(setPsdTag);
            if (setPsd == null) {
                setPsd = new UserRegisterSetPsdFragment();
                ft.add(R.id.content_container, setPsd, setPsdTag);
            } else {
                ft.attach(setPsd);
            }
            Bundle bundle = new Bundle();
            bundle.putString("requestPhone", event.requestPhone);
            setPsd.setArguments(bundle);
            ft.addToBackStack(setPsdTag);
            ft.commit();

            cancelTv.setVisibility(View.GONE);
            navigationIv.setVisibility(View.VISIBLE);
        } else if (event.navigationTag == AuthNavigationEvent.TAG_HEAD_TO_LOGIN) {
            getSupportFragmentManager().popBackStack();

            cancelTv.setVisibility(View.VISIBLE);
            navigationIv.setVisibility(View.GONE);
        }
    }

    @Subscribe
    public void onLogin(UserLoginEvent event) {
        //成功登录后，检查是否存在后续动作，有则完成该动作
        if (pendingAd != null) {
            EventBus.getDefault().post(new UserLoginWithPendingTaskEvent(pendingAd));
        }
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() == 1) {
            new CommNoneAndroidDialog()
                    .withMessage("是否放弃注册？")
                    .withNegativeBtn("放弃", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            onAuthorizationNavigation(new AuthNavigationEvent(AuthNavigationEvent.TAG_HEAD_TO_LOGIN));
                        }
                    })
                    .withPositiveBtn("继续注册", null)
                    .dimBackground(true)
                    .show(getSupportFragmentManager(), "CancelRegister");
            return;
        }
        super.onBackPressed();
    }

    @Override
    public void finish() {
        InputMethodUtil.closeSoftKeyboard(this);
        super.finish();
        overridePendingTransition(0, R.anim.slide_to_bottom);
    }

}
