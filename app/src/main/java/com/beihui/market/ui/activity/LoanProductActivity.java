package com.beihui.market.ui.activity;


import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.beihui.market.R;
import com.beihui.market.base.BaseComponentActivity;
import com.beihui.market.helper.DataStatisticsHelper;
import com.beihui.market.helper.SlidePanelHelper;
import com.beihui.market.injection.component.AppComponent;
import com.beihui.market.injection.component.DaggerLoanProductComponent;
import com.beihui.market.injection.module.LoanProductModule;
import com.beihui.market.ui.contract.LoanProductContract;
import com.beihui.market.ui.fragment.PagePersonalFragment;
import com.beihui.market.ui.fragment.PageSmartFragment;
import com.beihui.market.ui.presenter.LoanProductPresenter;
import com.beihui.market.umeng.Events;
import com.beihui.market.umeng.Statistic;
import com.beihui.market.view.AutoTextView;
import com.beihui.market.view.copytablayout.CopyTabLayout;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

public class LoanProductActivity extends BaseComponentActivity implements LoanProductContract.View {

    @BindView(R.id.appBarLayout)
    AppBarLayout appBarLayout;
    @BindView(R.id.tool_bar)
    Toolbar toolbar;
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
    LoanProductPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //pv，uv统计
        DataStatisticsHelper.getInstance().onCountUv(DataStatisticsHelper.ID_CLICK_TAB_LOAN);

        //umeng统计
        Statistic.onEvent(Events.CLICK_TAB_LOAN);
        super.onCreate(savedInstanceState);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_loan_product;
    }


    @Override
    public void configViews() {
//        ImmersionBar.with(this).titleBar(toolbar).init();
        setupToolbar(toolbar);
        viewPager.setAdapter(new RecommendPagerAdapter(getSupportFragmentManager()));
        tabLayout.setupWithViewPager(viewPager);

        viewPager.setCurrentItem(getIntent().getIntExtra("module_index", 0));

        SlidePanelHelper.attach(this);
    }

    @Override
    public void initDatas() {
        presenter.loadProductNotice();
    }

    @Override
    protected void configureComponent(AppComponent appComponent) {
        DaggerLoanProductComponent.builder()
                .appComponent(appComponent)
                .loanProductModule(new LoanProductModule(this))
                .build()
                .inject(this);
    }

    @Override
    public void setPresenter(LoanProductContract.Presenter presenter) {
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
        if (sb.length() > 25) {
            noticeContentView.setScrollMode(AutoTextView.SCROLL_NORM);
        } else {
            noticeContentView.setScrollMode(AutoTextView.SCROLL_STILL);
        }
        noticeContentView.setText(sb);
    }

    class RecommendPagerAdapter extends FragmentPagerAdapter {

        private String[] recommendTitle = {"精选好借", "智能推荐"};


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
