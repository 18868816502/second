package com.beihui.market.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.MotionEvent;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.beihui.market.R;
import com.beihui.market.base.BaseComponentActivity;
import com.beihui.market.helper.updatehelper.AppUpdateHelper;
import com.beihui.market.injection.component.AppComponent;
import com.beihui.market.ui.busevents.NavigateLoan;
import com.beihui.market.ui.busevents.NavigateNews;
import com.beihui.market.ui.fragment.TabHomeFragment;
import com.beihui.market.ui.fragment.TabLoanFragment;
import com.beihui.market.ui.fragment.TabMineFragment;
import com.beihui.market.ui.fragment.TabNewsFragment;
import com.beihui.market.util.FastClickUtils;
import com.beihui.market.view.BottomNavigationBar;
import com.gyf.barlibrary.ImmersionBar;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;

public class MainActivity extends BaseComponentActivity {

    @BindView(R.id.navigation_bar)
    BottomNavigationBar mNavigationBar;

    private int mSelectedFragmentId = -1;

    /**
     * param need passed to target fragment if navigating to TabLoanFragment.
     */
    private int queryMoney = -1;

    private InputMethodManager inputMethodManager;

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (getIntent().getBooleanExtra("home", false)) {
            if (mNavigationBar != null) {
                mNavigationBar.select(R.id.tab_home);
            }
        }
    }

    @Override
    protected void onDestroy() {
        AppUpdateHelper.getInstance().destroy();
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void configViews() {
        EventBus.getDefault().register(this);
        ImmersionBar.with(this).fitsSystemWindows(false).statusBarColor(R.color.transparent).init();
    }

    @Override
    public void initDatas() {
        mNavigationBar.setOnSelectedChangedListener(new BottomNavigationBar.OnSelectedChangedListener() {
            @Override
            public void onSelected(int selectedId) {
                if (selectedId != mSelectedFragmentId) {
                    selectTab(selectedId);
                }
            }
        });
        mNavigationBar.select(R.id.tab_home);

        AppUpdateHelper.getInstance().checkUpdate(this);
    }

    @Override
    protected void configureComponent(AppComponent appComponent) {

    }

    /**
     * 点击给我推荐，把条件带过去在第二个页面筛选
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void navigateLoan(NavigateLoan event) {
        if (event.queryMoney != -1) {
            queryMoney = event.queryMoney;
        }
        mNavigationBar.select(R.id.tab_loan);
        queryMoney = -1;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void navigateNews(NavigateNews event) {
        mNavigationBar.select(R.id.tab_news);
    }

    private void selectTab(int id) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();

        String oldTag = makeTag(mSelectedFragmentId);
        mSelectedFragmentId = id;
        String newTag = makeTag(mSelectedFragmentId);
        Fragment lastSelected = fm.findFragmentByTag(oldTag);
        if (lastSelected != null) {
            ft.detach(lastSelected);
        }
        Fragment newSelected = fm.findFragmentByTag(newTag);
        if (newSelected == null) {
            switch (id) {
                case R.id.tab_home:
                    newSelected = TabHomeFragment.newInstance();
                    break;
                case R.id.tab_loan:
                    newSelected = TabLoanFragment.newInstance();
                    break;
                case R.id.tab_news:
                    newSelected = TabNewsFragment.newInstance();
                    break;
                case R.id.tab_mine:
                    newSelected = TabMineFragment.newInstance();
                    break;
            }
            ft.add(R.id.tab_fragment, newSelected, newTag);
        } else {
            ft.attach(newSelected);
        }
        if (newSelected != null && newSelected instanceof TabLoanFragment && queryMoney != -1) {
            ((TabLoanFragment) newSelected).setQueryMoney(queryMoney);
        }
        ft.commit();
    }

    private String makeTag(int id) {
        return "TabFragmentId=" + id;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (mSelectedFragmentId == R.id.tab_home) {
            if (ev.getAction() == MotionEvent.ACTION_DOWN) {
                if (inputMethodManager == null) {
                    inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                }
                if (getCurrentFocus() != null && getCurrentFocus().getWindowToken() != null) {
                    if (getCurrentFocus() instanceof EditText) {
                        getCurrentFocus().clearFocus();
                        inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    }
                }
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public void onBackPressed() {
        if (FastClickUtils.isFastBackPress()) {
            super.onBackPressed();
        } else {
            Toast.makeText(this, "再按一次退出", Toast.LENGTH_SHORT).show();
        }
    }
}
