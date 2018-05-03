package com.beihui.market.ui.activity;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;

import com.beihui.market.R;
import com.beihui.market.base.BaseComponentActivity;
import com.beihui.market.helper.SlidePanelHelper;
import com.beihui.market.injection.component.AppComponent;
import com.beihui.market.ui.fragment.MyCreditCardDebtListFragment;
import com.beihui.market.ui.fragment.MyLoanDebtListFragment;
import com.beihui.market.view.PagerSlidingTab;
import com.beihui.market.view.copytablayout.CopyTabLayout;
import com.gyf.barlibrary.ImmersionBar;

import butterknife.BindView;

/**
 * @author xhb
 * 我的账单页面
 */
public class MyDebtActivity extends BaseComponentActivity {

    @BindView(R.id.tool_bar)
    Toolbar toolbar;
    @BindView(R.id.copy_tab_layout)
    PagerSlidingTab copyTabLayout;
    @BindView(R.id.view_pager)
    ViewPager viewPager;

    @Override
    public int getLayoutId() {
        return R.layout.activity_my_debt;
    }


    @Override
    public void configViews() {
        setupToolbar(toolbar);
        ImmersionBar.with(this).titleBar(toolbar).statusBarDarkFont(true).init();

        viewPager.setAdapter(new MyDebtPager(getSupportFragmentManager()));
        copyTabLayout.setViewPager(viewPager);

        SlidePanelHelper.attach(this);
    }

    @Override
    public void initDatas() {

    }

    @Override
    protected void configureComponent(AppComponent appComponent) {

    }

    class MyDebtPager extends FragmentPagerAdapter {

        MyDebtPager(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return position == 0 ? new MyCreditCardDebtListFragment() : new MyLoanDebtListFragment();
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return position == 0 ? "信用卡" : "网贷";
        }
    }
}
