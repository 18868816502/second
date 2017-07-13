package com.beihui.market.ui.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.beihui.market.R;
import com.beihui.market.base.BaseActivity;
import com.beihui.market.component.AppComponent;
import com.beihui.market.ui.busevents.NavigateLoan;
import com.beihui.market.ui.fragment.TabHomeFragment;
import com.beihui.market.ui.fragment.TabLoanFragment;
import com.beihui.market.ui.fragment.TabMineFragment;
import com.beihui.market.ui.fragment.TabNewsFragment;
import com.beihui.market.view.BottomNavigationBar;
import com.gyf.barlibrary.ImmersionBar;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;

public class MainActivity extends BaseActivity {

    @BindView(R.id.navigation_bar)
    BottomNavigationBar mNavigationBar;

    private int mSelectedFragmentId = -1;

    /**
     * param need passed to target fragment if there is.
     */
    private Bundle pendingBundle;


    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
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
    }

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {

    }

    @Override
    public void attachView() {

    }

    /**
     * 点击给我推荐，把条件带过去在第二个页面筛选
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void navigateLoan(NavigateLoan event) {
        pendingBundle = new Bundle();
        pendingBundle.putString("queryMoney", event.queryMoney);
        mNavigationBar.select(R.id.tab_loan);
        pendingBundle = null;
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
        if (pendingBundle != null && newSelected != null) {
            newSelected.setArguments(pendingBundle);
        }
        ft.commit();
    }

    private String makeTag(int id) {
        return "TabFragmentId=" + id;
    }

}
