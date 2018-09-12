package com.beihui.market.ui.activity;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.beihui.market.R;
import com.beihui.market.base.BaseComponentActivity;
import com.beihui.market.helper.SlidePanelHelper;
import com.beihui.market.injection.component.AppComponent;
import com.beihui.market.ui.fragment.MyCreditCardDebtListFragment;
import com.beihui.market.ui.fragment.MyFastDebtListFragment;
import com.beihui.market.ui.fragment.MyLoanDebtListFragment;
import com.gyf.barlibrary.ImmersionBar;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author xhb
 * 我的账单页面
 */
public class MyDebtActivity extends BaseComponentActivity {

    @BindView(R.id.tool_bar)
    Toolbar toolbar;
    @BindView(R.id.view_pager)
    ViewPager viewPager;

    @BindView(R.id.ll_ac_my_debt_tab_root)
    LinearLayout mRoot;
    @BindView(R.id.tv_normal_account_flow)
    TextView mAccountFlowNormal;
    @BindView(R.id.tv_loan_account_flow)
    TextView mAccountFlowLoan;
    @BindView(R.id.tv_credit_card_flow)
    TextView mAccountFlowCreditCard;


    public MyLoanDebtListFragment loan = new MyLoanDebtListFragment();
    public MyFastDebtListFragment fast = new MyFastDebtListFragment();
    public MyCreditCardDebtListFragment creditCard = new MyCreditCardDebtListFragment();

    public ArrayList<Fragment> fragments = new ArrayList<>();

    @Override
    public int getLayoutId() {
        return R.layout.activity_my_debt;
    }

    private int selectedFragmentId = R.id.tv_normal_account_flow;

    @Override
    public void configViews() {
        setupToolbar(toolbar);
        ImmersionBar.with(this).titleBar(toolbar).statusBarDarkFont(true).init();

        fragments.add(fast);
        fragments.add(loan);
        fragments.add(creditCard);

        viewPager.setAdapter(new MyDebtPager(getSupportFragmentManager()));

        SlidePanelHelper.attach(this);
    }

    @Override
    public void initDatas() {
        mAccountFlowNormal.setSelected(true);
    }

    @OnClick({R.id.tv_normal_account_flow, R.id.tv_loan_account_flow, R.id.tv_credit_card_flow})
    public void onItemClicked(View view) {
        if (view.getId() != selectedFragmentId) {
            if (view.getId() == R.id.tv_normal_account_flow) {
                viewPager.setCurrentItem(0);
            }
            if (view.getId() == R.id.tv_loan_account_flow) {
                viewPager.setCurrentItem(1);
            }
            if (view.getId() == R.id.tv_credit_card_flow) {
                viewPager.setCurrentItem(2);
            }
        }
        selectedFragmentId = view.getId();
    }

    @Override
    protected void onResume() {
        super.onResume();
        /**
         * 监听器tab
         */
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                if (position == 0) {
                    mAccountFlowNormal.setSelected(true);
                    mAccountFlowLoan.setSelected(false);
                    mAccountFlowCreditCard.setSelected(false);

                    selectedFragmentId = R.id.tv_normal_account_flow;
                }
                if (position == 1) {
                    mAccountFlowNormal.setSelected(false);
                    mAccountFlowLoan.setSelected(true);
                    mAccountFlowCreditCard.setSelected(false);

                    selectedFragmentId = R.id.tv_loan_account_flow;
                }
                if (position == 2) {
                    mAccountFlowNormal.setSelected(false);
                    mAccountFlowLoan.setSelected(false);
                    mAccountFlowCreditCard.setSelected(true);

                    selectedFragmentId = R.id.tv_credit_card_flow;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    protected void configureComponent(AppComponent appComponent) {

    }

    public int mPosition = 0;

    class MyDebtPager extends FragmentPagerAdapter {

        MyDebtPager(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            mPosition = position;
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return 3;
        }
    }
}