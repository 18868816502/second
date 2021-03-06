package com.beiwo.qnejqaz.ui.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;

import com.beiwo.qnejqaz.R;
import com.beiwo.qnejqaz.base.BaseComponentActivity;
import com.beiwo.qnejqaz.helper.SlidePanelHelper;
import com.beiwo.qnejqaz.helper.UserHelper;
import com.beiwo.qnejqaz.ui.busevents.ResetPsdNavigationEvent;
import com.beiwo.qnejqaz.ui.fragment.RequireVerifyCodeFragment;
import com.beiwo.qnejqaz.ui.fragment.SetPsdFragment;
import com.beiwo.qnejqaz.util.InputMethodUtil;
import com.gyf.barlibrary.ImmersionBar;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.BindView;

/**
 * 忘记密码的页面
 */
public class ResetPsdActivity extends BaseComponentActivity {
    @BindView(R.id.tool_bar)
    Toolbar toolbar;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_reset_psd;
    }

    @Override
    public void configViews() {
        EventBus.getDefault().register(this);
        ImmersionBar.with(this)
                .statusBarDarkFont(true)
                .keyboardEnable(true)
                .init();

        setupToolbar(toolbar);
        if (TextUtils.isEmpty(getIntent().getStringExtra("tileName"))) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.content_container, new RequireVerifyCodeFragment(), RequireVerifyCodeFragment.class.getSimpleName())
                    .commitAllowingStateLoss();
        } else {
            Fragment setPsd = new SetPsdFragment();
            FragmentTransaction ft = getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.content_container, setPsd, SetPsdFragment.class.getSimpleName());
            Bundle bundle = new Bundle();
            bundle.putString("requestPhone", UserHelper.getInstance(this).getProfile().getAccount());
            setPsd.setArguments(bundle);
            ft.commitAllowingStateLoss();
        }
        SlidePanelHelper.attach(this);
    }

    @Override
    public void initDatas() {
    }

    /**
     * EventBus 事件 计入忘记密码填写密码页面
     *
     * @param event
     */
    @Subscribe
    public void navigation(ResetPsdNavigationEvent event) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();

        ft.setCustomAnimations(R.anim.slide_from_right, R.anim.hold_still);
        Fragment verifyCode = fm.findFragmentByTag(RequireVerifyCodeFragment.class.getSimpleName());
        ft.detach(verifyCode);

        String setPsdTag = SetPsdFragment.class.getSimpleName();
        Fragment setPsd = fm.findFragmentByTag(setPsdTag);
        if (setPsd == null) {
            setPsd = new SetPsdFragment();
            ft.add(R.id.content_container, setPsd, setPsdTag);
        } else {
            ft.attach(setPsd);
        }
        Bundle bundle = new Bundle();
        bundle.putString("requestPhone", event.requestPhone);
        setPsd.setArguments(bundle);
        ft.addToBackStack(setPsdTag);
        ft.commitAllowingStateLoss();
    }


    @Override
    public void finish() {
        InputMethodUtil.closeSoftKeyboard(this);
        super.finish();
    }
}
