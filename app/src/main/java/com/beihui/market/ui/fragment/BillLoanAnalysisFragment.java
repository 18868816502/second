package com.beihui.market.ui.fragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.beihui.market.R;
import com.beihui.market.base.BaseComponentFragment;
import com.beihui.market.helper.DataStatisticsHelper;
import com.beihui.market.injection.component.AppComponent;
import com.beihui.market.umeng.NewVersionEvents;
import com.gyf.barlibrary.ImmersionBar;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by admin on 2018/6/21.
 */

public class BillLoanAnalysisFragment extends BaseComponentFragment {

    @BindView(R.id.tool_bar)
    Toolbar toolbar;
    @BindView(R.id.tv_ac_bill_loan_analysis_week)
    TextView mWeek;
    @BindView(R.id.tv_ac_bill_loan_analysis_month)
    TextView mMonth;
    @BindView(R.id.vg_fg_bill_loan_analysis)
    ViewPager viewPager;

    private int selectedFragmentId = R.id.tv_ac_bill_loan_analysis_month;

    //网贷
    public BillLoanAnalysisFragmentWeek mWeedFragment = new BillLoanAnalysisFragmentWeek();
    //信用卡
    public BillLoanAnalysisFragmentMonth mMonthFragment = new BillLoanAnalysisFragmentMonth();

    public List<BaseComponentFragment> fragmentList = new ArrayList<>();


    public static BillLoanAnalysisFragment newInstance() {
        return new BillLoanAnalysisFragment();
    }

    @Override
    public int getLayoutResId() {
        return R.layout.x_fragment_bill_loan_analysis;
    }

    @Override
    public void configViews() {
        ImmersionBar.with(this).titleBar(toolbar).statusBarDarkFont(true).init();
        fragmentList.add(mWeedFragment);
        fragmentList.add(mMonthFragment);

        //pv，uv统计
        DataStatisticsHelper.getInstance().onCountUv(NewVersionEvents.REPORT);
    }

    @Override
    public void initDatas() {
        MyFragmentViewPgaerAdapter adapter = new MyFragmentViewPgaerAdapter(getChildFragmentManager());
        viewPager.setAdapter(adapter);

        viewPager.setCurrentItem(1);
        mMonth.setSelected(true);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                if (position == 0) {
                    mWeek.setSelected(true);
                    mMonth.setSelected(false);

                    selectedFragmentId = R.id.tv_ac_bill_loan_analysis_week;

                    //pv，uv统计
//                    DataStatisticsHelper.getInstance().onCountUv(NewVersionEvents.REPORTWEEKICONCLICK);
                }
                if (position == 1) {
                    mWeek.setSelected(false);
                    mMonth.setSelected(true);

                    selectedFragmentId = R.id.tv_ac_bill_loan_analysis_month;

                    //pv，uv统计
//                    DataStatisticsHelper.getInstance().onCountUv(NewVersionEvents.REPORTMONTHICONCLICK);
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    public int type = 3;

    @OnClick({R.id.tv_ac_bill_loan_analysis_week, R.id.tv_ac_bill_loan_analysis_month})
    public void onItemClicked(View view) {
        if (view.getId() != selectedFragmentId) {
            if (view.getId() == R.id.tv_ac_bill_loan_analysis_week) {
                viewPager.setCurrentItem(0);
                mWeek.setSelected(true);
                mMonth.setSelected(false);
                mMonthFragment.mAdapter.mShowFirstItemPosition = false;
                //柱状图数据
                mWeedFragment.requestChartData(Calendar.getInstance());
                //请求列表数据
                mWeedFragment.requestListData(Calendar.getInstance());
            }
            if (view.getId() == R.id.tv_ac_bill_loan_analysis_month) {
                viewPager.setCurrentItem(1);
                mWeek.setSelected(false);
                mMonth.setSelected(true);
                mMonthFragment.mAdapter.mShowFirstItemPosition = false;
                //柱状图数据
                mMonthFragment.requestChartData(Calendar.getInstance());
                //请求列表数据
                mMonthFragment.requestListData(Calendar.getInstance());
            }

        }
        selectedFragmentId = view.getId();
    }

    @Override
    protected void configureComponent(AppComponent appComponent) {

    }

    class MyFragmentViewPgaerAdapter extends FragmentPagerAdapter {

        public MyFragmentViewPgaerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }
    }
}
