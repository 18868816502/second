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
import com.beihui.market.injection.component.DaggerTabLoanComponent;
import com.beihui.market.injection.module.TabLoanModule;
import com.beihui.market.ui.contract.TabLoanContract;
import com.beihui.market.ui.presenter.TabLoanPresenter;
import com.beihui.market.umeng.Events;
import com.beihui.market.umeng.Statistic;
import com.beihui.market.view.AutoTextView;
import com.beihui.market.view.copytablayout.CopyTabLayout;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;


public class TabLoanFragment extends BaseTabFragment implements TabLoanContract.View {

    @BindView(R.id.tab_layout)
    CopyTabLayout tabLayout;
    @BindView(R.id.view_pager)
    ViewPager viewPager;

    @BindView(R.id.notice_container)
    View noticeContainer;
    @BindView(R.id.notice_content)
    AutoTextView noticeContentView;
    @BindView(R.id.notice_close)
    View noticeCloseView;

    @Inject
    TabLoanPresenter presenter;

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
        noticeContentView.setScrollMode(AutoTextView.SCROLL_NORM);
        viewPager.setAdapter(new RecommendPagerAdapter(getChildFragmentManager()));
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public void initDatas() {
        presenter.loadProductNotice();
    }

    @Override
    protected void configureComponent(AppComponent appComponent) {
        DaggerTabLoanComponent.builder()
                .appComponent(appComponent)
                .tabLoanModule(new TabLoanModule(this))
                .build()
                .inject(this);
    }

    @Override
    public void setPresenter(TabLoanContract.Presenter presenter) {
        //
    }

    @Override
    public void showProductNotice(List<String> notice) {
        noticeContainer.setVisibility(View.VISIBLE);
        noticeCloseView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                noticeContainer.setVisibility(View.GONE);
            }
        });
        StringBuilder sb = new StringBuilder();
        for (String item : notice) {
            sb.append(item);
        }
        noticeContentView.setText(sb);
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
