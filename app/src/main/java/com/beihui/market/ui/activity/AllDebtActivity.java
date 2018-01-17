package com.beihui.market.ui.activity;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.AbsoluteSizeSpan;
import android.widget.TextView;

import com.beihui.market.R;
import com.beihui.market.base.BaseComponentActivity;
import com.beihui.market.helper.SlidePanelHelper;
import com.beihui.market.injection.component.AppComponent;
import com.beihui.market.ui.fragment.AllDebtFragment;
import com.beihui.market.view.copytablayout.CopyTabLayout;
import com.gyf.barlibrary.ImmersionBar;

import butterknife.BindView;

public class AllDebtActivity extends BaseComponentActivity {

    static int[] status = {AllDebtFragment.DEBT_STATUS_IN, AllDebtFragment.DEBT_STATUS_OFF, AllDebtFragment.DEBT_STATUS_ALL};
    static String[] titles = {"待还", "已还", "全部"};

    @BindView(R.id.tool_bar)
    Toolbar toolbar;
    @BindView(R.id.tab_layout)
    CopyTabLayout tabLayout;
    @BindView(R.id.view_pager)
    ViewPager viewPager;

    @BindView(R.id.debt_count)
    TextView debtCountView;
    @BindView(R.id.debt_amount)
    TextView debtAmountView;
    @BindView(R.id.capital_amount)
    TextView capitalAmountView;
    @BindView(R.id.interest_amount)
    TextView interestAmountView;

    @Override
    public int getLayoutId() {
        return R.layout.activity_all_debt;
    }

    @Override
    public void configViews() {
        ImmersionBar.with(this).titleBar(toolbar).statusBarDarkFont(true).init();
        setupToolbarBackNavigation(toolbar, R.mipmap.left_arrow_black);

        viewPager.setAdapter(new DebtAdapter(getSupportFragmentManager()));
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setLengthAndMarginBottom((int) (getResources().getDisplayMetrics().density * 25), (int) (getResources().getDisplayMetrics().density * 6));
        SlidePanelHelper.attach(this);
    }

    @Override
    public void initDatas() {
    }

    @Override
    protected void configureComponent(AppComponent appComponent) {

    }

    public void updateBottomInfo(String count, String debtAmount, String capitalAmount, String interestAmount) {
        Spannable ss = new SpannableString("共" + count + "项");
        ss.setSpan(new AbsoluteSizeSpan((int) (getResources().getDisplayMetrics().density * 19)), 1, count.length() + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        debtCountView.setText(ss);
        debtAmountView.setText(debtAmount);
        capitalAmountView.setText(capitalAmount);
        interestAmountView.setText(interestAmount);
    }

    class DebtAdapter extends FragmentPagerAdapter {

        DebtAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return AllDebtFragment.newInstance(status[position]);
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
        }
    }
}
