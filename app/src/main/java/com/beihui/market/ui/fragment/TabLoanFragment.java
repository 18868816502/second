package com.beihui.market.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.beihui.market.R;
import com.beihui.market.base.BaseTabFragment;
import com.beihui.market.injection.component.AppComponent;
import com.beihui.market.umeng.Events;
import com.beihui.market.umeng.Statistic;
import com.beihui.market.view.copytablayout.CopyTabLayout;

import butterknife.BindView;


public class TabLoanFragment extends BaseTabFragment {

    @BindView(R.id.tab_layout)
    CopyTabLayout tabLayout;
    @BindView(R.id.view_pager)
    ViewPager viewPager;

    public static TabLoanFragment newInstance() {
        return new TabLoanFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //umeng统计
        Statistic.onEvent(Events.ENTER_LOAN_PAGE);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public int getLayoutResId() {
        return R.layout.fragment_tab_loan;
    }

    @Override
    public void configViews() {
        viewPager.setAdapter(new RecommendPagerAdapter(getChildFragmentManager()));
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public void initDatas() {
    }

    @Override
    protected void configureComponent(AppComponent appComponent) {
    }

    class RecommendPagerAdapter extends FragmentPagerAdapter {

        private String[] recommendTitle = {"个性化推荐", "智能推荐"};


        public RecommendPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return position == 0 ? new PagePersonalFragment() : new PageSmartFragment();
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return recommendTitle[position];
        }
    }

}
