package com.beiwo.qnejqaz.ui.activity;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.beiwo.qnejqaz.R;
import com.beiwo.qnejqaz.base.BaseComponentActivity;
import com.beiwo.qnejqaz.entity.AdBanner;
import com.beiwo.qnejqaz.helper.ActivityTracker;
import com.beiwo.qnejqaz.helper.SlidePanelHelper;
import com.beiwo.qnejqaz.ui.busevents.AuthNavigationEvent;
import com.beiwo.qnejqaz.ui.busevents.UserLoginEvent;
import com.beiwo.qnejqaz.ui.busevents.UserLoginWithPendingTaskEvent;
import com.beiwo.qnejqaz.ui.fragment.LoginMainFragment;
import com.beiwo.qnejqaz.ui.fragment.UserLoginFragment;
import com.beiwo.qnejqaz.umeng.Events;
import com.beiwo.qnejqaz.umeng.Statistic;
import com.beiwo.qnejqaz.util.InputMethodUtil;
import com.beiwo.qnejqaz.view.drawable.BlurringDrawable;
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
    }

    public static void launchWithPending(Activity context, AdBanner adBanner) {
        Intent intent = new Intent(context, UserAuthorizationActivity.class);
        if (adBanner != null) {
            intent.putExtra("pendingAd", adBanner);
        }
        context.startActivity(intent);
    }

    public static void launch(Activity activity) {
        activity.startActivity(new Intent(activity, UserAuthorizationActivity.class));
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
                .commitAllowingStateLoss();
        SlidePanelHelper.attach(this);
    }

    @Override
    public void initDatas() {
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
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

    @Subscribe
    public void onAuthorizationNavigation(AuthNavigationEvent event) {
        if (event.navigationTag == AuthNavigationEvent.TAG_LOGIN_PSD) {
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.setCustomAnimations(R.anim.slide_from_right, R.anim.hold_still);

            Fragment mainLoginTag = fm.findFragmentByTag(LoginMainFragment.class.getSimpleName());
            ft.detach(mainLoginTag);

            String loginPsdTag = UserLoginFragment.class.getSimpleName();
            Fragment loginPsd = new UserLoginFragment();
            ft.add(R.id.content_container, loginPsd, loginPsdTag);

            ft.attach(loginPsd);
            ft.addToBackStack(loginPsdTag);
            ft.commitAllowingStateLoss();
        } else if (event.navigationTag == AuthNavigationEvent.TAG_LOGIN_FAST) {
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.setCustomAnimations(R.anim.slide_from_right, R.anim.hold_still);

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
            ft.commitAllowingStateLoss();
        } else if (event.navigationTag == AuthNavigationEvent.TAG_HEAD_TO_LOGIN) {
            getSupportFragmentManager().popBackStack();
        }
    }

    @Subscribe
    public void onLogin(UserLoginEvent event) {
        EventBus.getDefault().post("1");
        //成功登录后，检查是否存在后续动作，有则完成该动作
        if (pendingAd != null) {
            EventBus.getDefault().post(new UserLoginWithPendingTaskEvent(pendingAd));
        }
    }

    @Override
    public void finish() {
        InputMethodUtil.closeSoftKeyboard(this);
        override = false;
        super.finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }
}