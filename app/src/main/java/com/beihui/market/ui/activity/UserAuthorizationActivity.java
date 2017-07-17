package com.beihui.market.ui.activity;


import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;

import com.beihui.market.R;
import com.beihui.market.base.BaseActivity;
import com.beihui.market.component.AppComponent;
import com.beihui.market.ui.busevents.AuthNavigationEvent;
import com.beihui.market.ui.fragment.UserLoginFragment;
import com.beihui.market.ui.fragment.UserRegisterSetPsdFragment;
import com.beihui.market.ui.fragment.UserRegisterVerifyCodeFragment;
import com.gyf.barlibrary.ImmersionBar;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.ByteArrayOutputStream;

import butterknife.BindView;
import butterknife.OnClick;

public class UserAuthorizationActivity extends BaseActivity {

    @BindView(R.id.root_container)
    FrameLayout rootContainer;

    public static void launch(Context context, View blurView) {
        blurView.setDrawingCacheEnabled(true);
        Bitmap bg = blurView.getDrawingCache();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bg.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] bytes = baos.toByteArray();
        bg.recycle();
        blurView.setDrawingCacheEnabled(false);

        Intent intent = new Intent(context, UserAuthorizationActivity.class);
        intent.putExtra("bytes", bytes);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_user_authorization;
    }

    @Override
    public void configViews() {
        getSupportFragmentManager().beginTransaction()
                .add(R.id.content_container, new UserLoginFragment(), UserLoginFragment.class.getSimpleName())
                .commit();
    }

    @Override
    public void initDatas() {
        ImmersionBar.with(this).fitsSystemWindows(true).init();
    }

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {

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

        byte[] bytes = getIntent().getByteArrayExtra("bytes");
        Bitmap bg = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        window.setBackgroundDrawable(new BitmapDrawable(getResources(), bg));
    }


    @OnClick(R.id.tv_back)
    void OnViewClicked() {
        finish();
    }

    @Subscribe
    void onAuthorizationNavigation(AuthNavigationEvent event) {
        if (event.navigationTag == AuthNavigationEvent.TAG_REGISTER) {
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.setCustomAnimations(R.anim.auth_enter, R.anim.auth_exit, R.anim.auth_enter, R.anim.auth_exit);

            Fragment login = fm.findFragmentByTag(UserLoginFragment.class.getSimpleName());
            ft.detach(login);

            String registerTag = UserRegisterVerifyCodeFragment.class.getSimpleName();
            Fragment verifyCode = fm.findFragmentByTag(registerTag);
            if (verifyCode == null) {
                verifyCode = new UserRegisterVerifyCodeFragment();
                ft.add(R.id.content_container, verifyCode, registerTag);
            } else {
                ft.attach(verifyCode);
            }
            ft.addToBackStack(registerTag);
            ft.commit();
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
            ft.addToBackStack(setPsdTag);
            ft.commit();
        } else if (event.navigationTag == AuthNavigationEvent.TAG_HEAD_TO_LOGIN) {
            onBackPressed();
        }
    }
}
