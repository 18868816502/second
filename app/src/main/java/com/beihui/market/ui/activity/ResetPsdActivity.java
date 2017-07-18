package com.beihui.market.ui.activity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;

import com.beihui.market.R;
import com.beihui.market.base.BaseComponentActivity;
import com.beihui.market.component.AppComponent;
import com.beihui.market.ui.busevents.ResetPsdNavigationEvent;
import com.beihui.market.ui.fragment.RequireVerifyCodeComponentFragment;
import com.beihui.market.ui.fragment.SetPsdComponentFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.BindView;


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
        setupToolbar(toolbar);
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.content_container, new RequireVerifyCodeComponentFragment(), RequireVerifyCodeComponentFragment.class.getSimpleName())
                .commit();
    }

    @Override
    public void initDatas() {
    }

    @Override
    protected void configureComponent(AppComponent appComponent) {

    }

    @Subscribe
    void navigation(ResetPsdNavigationEvent event) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();

        ft.setCustomAnimations(R.anim.slide_from_right, R.anim.hold_still);
        Fragment verifyCode = fm.findFragmentByTag(RequireVerifyCodeComponentFragment.class.getSimpleName());
        ft.detach(verifyCode);

        String setPsdTag = SetPsdComponentFragment.class.getSimpleName();
        Fragment setPsd = fm.findFragmentByTag(setPsdTag);
        if (setPsd == null) {
            setPsd = new SetPsdComponentFragment();
            ft.add(R.id.content_container, setPsd, setPsdTag);
        } else {
            ft.attach(setPsd);
        }
        ft.commit();
    }
}
