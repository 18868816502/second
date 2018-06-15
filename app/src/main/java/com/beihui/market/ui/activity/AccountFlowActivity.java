package com.beihui.market.ui.activity;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.beihui.market.R;
import com.beihui.market.base.BaseComponentActivity;
import com.beihui.market.base.BaseComponentFragment;
import com.beihui.market.helper.KeyBoardHelper;
import com.beihui.market.helper.SlidePanelHelper;
import com.beihui.market.injection.component.AppComponent;
import com.beihui.market.ui.adapter.AccountFlowAdapter;
import com.beihui.market.ui.fragment.AccountFlowCreditCardFragment;
import com.beihui.market.ui.fragment.AccountFlowLoanFragment;
import com.beihui.market.ui.fragment.AccountFlowNormalFragment;
import com.beihui.market.ui.fragment.TabAccountFragment;
import com.beihui.market.view.AutoAdjustSizeEditText;
import com.beihui.market.view.customekeyboard.CustomBaseKeyboard;
import com.beihui.market.view.customekeyboard.CustomKeyboardManager;
import com.gyf.barlibrary.ImmersionBar;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by admin on 2018/6/13.
 * 账单流程页面 通用记账 网贷记账 信用卡记账
 */

public class AccountFlowActivity extends BaseComponentActivity {

    @BindView(R.id.tool_bar)
    Toolbar toolbar;
    @BindView(R.id.ll_account_flow_root)
    LinearLayout mRoot;
    @BindView(R.id.tv_normal_account_flow)
    TextView mAccountFlowNormal;
    @BindView(R.id.tv_loan_account_flow)
    TextView mAccountFlowLoan;
    @BindView(R.id.fl_ac_account_flow_container)
    ViewPager mViewPager;
    @BindView(R.id.tv_credit_card_flow)
    TextView mAccountFlowCreditCard;
    @BindView(R.id.iv_ac_account_flow_confirm)
    ImageView mConFirmOrRefrsh;

    private int selectedFragmentId = R.id.tv_normal_account_flow;

    //通用
    public AccountFlowNormalFragment mNormalFragment = new AccountFlowNormalFragment();
    //网贷
    public AccountFlowLoanFragment mLoanFragment = new AccountFlowLoanFragment();
    //信用卡
    public AccountFlowCreditCardFragment mCreditCardFragment = new AccountFlowCreditCardFragment();

    public List<BaseComponentFragment> fragmentList = new ArrayList<>();

    @Override
    public int getLayoutId() {
        return R.layout.x_activity_account_flow;
    }

    @Override
    public void configViews() {
        setupToolbarBackNavigation(toolbar,R.drawable.x_delete);
        ImmersionBar.with(this).titleBar(toolbar).statusBarDarkFont(true).init();

        fragmentList.add(mNormalFragment);
        fragmentList.add(mLoanFragment);
        fragmentList.add(mCreditCardFragment);
    }


    @Override
    public void initDatas() {
        MyFragmentViewPgaerAdapter adapter = new MyFragmentViewPgaerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(adapter);
        mViewPager.setCurrentItem(0);
        mAccountFlowNormal.setSelected(true);

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0) {
                    mAccountFlowNormal.setSelected(true);
                    mAccountFlowLoan.setSelected(false);
                    mAccountFlowCreditCard.setSelected(false);

                    mLoanFragment.customKeyboardManager.hideSoftKeyboard(mLoanFragment.etInputPrice);
                    selectedFragmentId = R.id.tv_normal_account_flow;

                    mConFirmOrRefrsh.setVisibility(View.VISIBLE);
                }
                if (position == 1) {
                    mAccountFlowNormal.setSelected(false);
                    mAccountFlowLoan.setSelected(true);
                    mAccountFlowCreditCard.setSelected(false);

                    mNormalFragment.customKeyboardManager.hideSoftKeyboard(mNormalFragment.etInputPrice);
                    selectedFragmentId = R.id.tv_loan_account_flow;

                    mConFirmOrRefrsh.setVisibility(View.VISIBLE);
                }
                if (position == 2) {
                    mAccountFlowNormal.setSelected(false);
                    mAccountFlowLoan.setSelected(false);
                    mAccountFlowCreditCard.setSelected(true);

                    mNormalFragment.customKeyboardManager.hideSoftKeyboard(mNormalFragment.etInputPrice);
                    mLoanFragment.customKeyboardManager.hideSoftKeyboard(mLoanFragment.etInputPrice);
                    selectedFragmentId = R.id.tv_credit_card_flow;

                    mConFirmOrRefrsh.setVisibility(View.GONE);
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


    @OnClick({R.id.tv_normal_account_flow, R.id.tv_loan_account_flow, R.id.tv_credit_card_flow})
    public void onItemClicked(View view) {
        if (view.getId() != selectedFragmentId) {
            if (view.getId() == R.id.tv_normal_account_flow) {
                mViewPager.setCurrentItem(0);
            }
            if (view.getId() == R.id.tv_loan_account_flow) {
                mViewPager.setCurrentItem(1);
            }
            if (view.getId() == R.id.tv_credit_card_flow) {
                mViewPager.setCurrentItem(2);
            }
        }
        selectedFragmentId = view.getId();
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