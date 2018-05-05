package com.beihui.market.ui.activity;


import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Interpolator;
import android.widget.TextView;

import com.beihui.market.R;
import com.beihui.market.base.BaseComponentActivity;
import com.beihui.market.entity.AdBanner;
import com.beihui.market.helper.ActivityTracker;
import com.beihui.market.injection.component.AppComponent;
import com.beihui.market.ui.busevents.AuthNavigationEvent;
import com.beihui.market.ui.busevents.UserLoginEvent;
import com.beihui.market.ui.busevents.UserLoginWithPendingTaskEvent;
import com.beihui.market.ui.fragment.LoginMainFragment;
import com.beihui.market.ui.fragment.UserLoginFragment;
import com.beihui.market.umeng.Events;
import com.beihui.market.umeng.Statistic;
import com.beihui.market.util.InputMethodUtil;
import com.beihui.market.view.drawable.BlurringDrawable;
import com.gyf.barlibrary.ImmersionBar;
import com.umeng.socialize.UMShareAPI;

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


    @BindView(R.id.tv_change)
    TextView tvChange;


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
        context.overridePendingTransition(R.anim.anim_bottom_in, R.anim.anim_bottom_out);
    }

    public static void launchWithPending(Activity context, AdBanner adBanner) {
        Intent intent = new Intent(context, UserAuthorizationActivity.class);
        if (adBanner != null) {
            intent.putExtra("pendingAd", adBanner);
        }
        context.startActivity(intent);
        context.overridePendingTransition(R.anim.anim_bottom_in, R.anim.anim_bottom_out);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //umeng统计
        Statistic.onEvent(Events.ENTER_LOGIN);

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
        ImmersionBar.with(this)
                .statusBarView(R.id.top_view)
                .statusBarDarkFont(true)
                .keyboardEnable(true)
                .init();

        Bundle bundle = null;
        if (getIntent() != null && getIntent().getStringExtra("phone") != null) {
            bundle = new Bundle();
            bundle.putString("phone", getIntent().getStringExtra("phone"));
        }
        LoginMainFragment fragment = new LoginMainFragment();
        if (bundle != null) {
            fragment.setArguments(bundle);
        }
        getSupportFragmentManager().beginTransaction()
                .add(R.id.content_container, fragment, LoginMainFragment.class.getSimpleName())
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


    @OnClick({R.id.cancel, R.id.tv_change})
    void OnViewClicked(View view) {
        switch (view.getId()) {
            case R.id.cancel:
                Statistic.onEvent(Events.LOGIN_CANCEL);
                finish();
                break;
            case R.id.tv_change:
                String changeText = tvChange.getText().toString();
                if ("密码登录".equals(changeText)) {
                    tvChange.setText("免密码登录");
                    EventBus.getDefault().post(new AuthNavigationEvent(AuthNavigationEvent.TAG_LOGIN_PSD));
                } else {
                    tvChange.setText("密码登录");
                    EventBus.getDefault().post(new AuthNavigationEvent(AuthNavigationEvent.TAG_LOGIN_FAST));
                }
                break;
        }
    }


    /**
     * EventBus事件
     * @param event
     */
    @Subscribe
    public void onAuthorizationNavigation(AuthNavigationEvent event) {
        if (event.navigationTag == AuthNavigationEvent.TAG_LOGIN_PSD) {
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.setCustomAnimations(R.anim.auth_enter, R.anim.auth_exit, R.anim.auth_enter, R.anim.auth_exit);

            Fragment mainLoginTag = fm.findFragmentByTag(LoginMainFragment.class.getSimpleName());
            ft.detach(mainLoginTag);

            String loginPsdTag = UserLoginFragment.class.getSimpleName();
            Fragment loginPsd = new UserLoginFragment();
            ft.add(R.id.content_container, loginPsd, loginPsdTag);
            ft.attach(loginPsd);
            ft.addToBackStack(loginPsdTag);
            ft.commit();

        } else if (event.navigationTag == AuthNavigationEvent.TAG_LOGIN_FAST) {
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.setCustomAnimations(R.anim.auth_enter, R.anim.auth_exit, R.anim.auth_enter, R.anim.auth_exit);

            Fragment loginPsdTag = fm.findFragmentByTag(UserLoginFragment.class.getSimpleName());
            ft.detach(loginPsdTag);

            String setPsdTag = LoginMainFragment.class.getSimpleName();
            Fragment setPsd = fm.findFragmentByTag(setPsdTag);
            if (setPsd == null) {
                setPsd = new LoginMainFragment();
                ft.add(R.id.content_container, setPsd, setPsdTag);
            } else {
                ft.attach(setPsd);
            }
            Bundle bundle = new Bundle();
            bundle.putString("requestPhone", event.requestPhone);
            setPsd.setArguments(bundle);
            ft.addToBackStack(setPsdTag);
            ft.commit();

        } else if (event.navigationTag == AuthNavigationEvent.TAG_HEAD_TO_LOGIN) {
            getSupportFragmentManager().popBackStack();
        }
    }

    @Subscribe
    public void onLogin(UserLoginEvent event) {
        //成功登录后，检查是否存在后续动作，有则完成该动作
        if (pendingAd != null) {
            EventBus.getDefault().post(new UserLoginWithPendingTaskEvent(pendingAd));
        }
    }

    /**
     * 返回键是否放弃注册
     */
//    @Override
//    public void onBackPressed() {
//        if (getSupportFragmentManager().getBackStackEntryCount() == 1) {
//            new CommNoneAndroidDialog()
//                    .withMessage("是否放弃注册？")
//                    .withNegativeBtn("放弃", new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            onAuthorizationNavigation(new AuthNavigationEvent(AuthNavigationEvent.TAG_HEAD_TO_LOGIN));
//                        }
//                    })
//                    .withPositiveBtn("继续注册", null)
//                    .dimBackground(true)
//                    .show(getSupportFragmentManager(), "CancelRegister");
//            return;
//        }
//        super.onBackPressed();
//    }

    @Override
    public void finish() {
        InputMethodUtil.closeSoftKeyboard(this);
        super.finish();
        overridePendingTransition(0, R.anim.slide_to_bottom);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);

    }

}
