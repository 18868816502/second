package com.beihui.market.ui.activity;

import android.content.Context;
import android.content.Intent;
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
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.beihui.market.R;
import com.beihui.market.api.Api;
import com.beihui.market.api.ResultEntity;
import com.beihui.market.base.BaseComponentActivity;
import com.beihui.market.base.BaseComponentFragment;
import com.beihui.market.entity.AccountFlowIconBean;
import com.beihui.market.entity.DebtDetail;
import com.beihui.market.helper.KeyBoardHelper;
import com.beihui.market.helper.SlidePanelHelper;
import com.beihui.market.helper.UserHelper;
import com.beihui.market.injection.component.AppComponent;
import com.beihui.market.ui.adapter.AccountFlowAdapter;
import com.beihui.market.ui.fragment.AccountFlowCreditCardFragment;
import com.beihui.market.ui.fragment.AccountFlowLoanFragment;
import com.beihui.market.ui.fragment.AccountFlowNormalFragment;
import com.beihui.market.ui.fragment.TabAccountFragment;
import com.beihui.market.ui.presenter.DebtDetailPresenter;
import com.beihui.market.util.FastClickUtils;
import com.beihui.market.util.RxUtil;
import com.beihui.market.util.ToastUtils;
import com.beihui.market.view.AutoAdjustSizeEditText;
import com.beihui.market.view.NoScrollViewPager;
import com.beihui.market.view.customekeyboard.CustomBaseKeyboard;
import com.beihui.market.view.customekeyboard.CustomKeyboardManager;
import com.gyf.barlibrary.ImmersionBar;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.functions.Consumer;

/**
 * Created by admin on 2018/6/13.
 * 账单流程页面 通用记账 网贷记账 信用卡记账
 */

public class AccountFlowActivity extends BaseComponentActivity {

    @BindView(R.id.tool_bar)
    Toolbar toolbar;
    @BindView(R.id.tv_ac_account_flow_title)
    TextView mTitleName;
    @BindView(R.id.ll_ac_account_flow_tab_root)
    LinearLayout mTabRoot;
    @BindView(R.id.ll_account_flow_root)
    LinearLayout mRoot;
    @BindView(R.id.tv_normal_account_flow)
    TextView mAccountFlowNormal;
    @BindView(R.id.tv_loan_account_flow)
    TextView mAccountFlowLoan;
    @BindView(R.id.fl_ac_account_flow_container)
    NoScrollViewPager mViewPager;
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

        SlidePanelHelper.attach(this);

    }


    @Override
    public void initDatas() {
        MyFragmentViewPgaerAdapter adapter = new MyFragmentViewPgaerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(adapter);

        /**
         * 是否编辑
         */
        Intent intent = getIntent();
        String debtType = intent.getStringExtra("debt_type");
        if (!TextUtils.isEmpty(debtType)) {
            //隐藏Tab
            mTitleName.setVisibility(View.VISIBLE);
            mTabRoot.setVisibility(View.GONE);
            //关闭滑动
            mViewPager.setNoScroll(true);
            if ("0".equals(debtType)) {
                //通用类型
                mViewPager.setCurrentItem(0);
                mAccountFlowNormal.setSelected(true);
                mNormalFragment.debtNormalDetail = intent.getParcelableExtra("debt_detail");
                selectedFragmentId = R.id.tv_normal_account_flow;
            }
            if ("1".equals(debtType)) {
                //网贷类型
                mViewPager.setCurrentItem(1);
                mAccountFlowLoan.setSelected(true);
                mLoanFragment.debtNormalDetail = intent.getParcelableExtra("debt_detail");
                selectedFragmentId = R.id.tv_loan_account_flow;
            }
        } else {
            mTitleName.setVisibility(View.GONE);
            mTabRoot.setVisibility(View.VISIBLE);
            //开启滑动
            mViewPager.setNoScroll(false);
            mViewPager.setCurrentItem(0);
            mAccountFlowNormal.setSelected(true);
        }


        /**
         * 监听器tab
         */
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

                    selectedFragmentId = R.id.tv_normal_account_flow;
                    mLoanFragment.customKeyboardManager.hideSoftKeyboard(mLoanFragment.etInputPrice, 1);
                    mNormalFragment.customKeyboardManager.showSoftKeyboard(mNormalFragment.etInputPrice);
                    mConFirmOrRefrsh.setVisibility(View.VISIBLE);
                }
                if (position == 1) {
                    mAccountFlowNormal.setSelected(false);
                    mAccountFlowLoan.setSelected(true);
                    mAccountFlowCreditCard.setSelected(false);

                    mNormalFragment.customKeyboardManager.hideSoftKeyboard(mNormalFragment.etInputPrice, 0);
                    selectedFragmentId = R.id.tv_loan_account_flow;

                    mConFirmOrRefrsh.setVisibility(View.VISIBLE);
                }
                if (position == 2) {
                    mAccountFlowNormal.setSelected(false);
                    mAccountFlowLoan.setSelected(false);
                    mAccountFlowCreditCard.setSelected(true);

                    mNormalFragment.customKeyboardManager.hideSoftKeyboard(mNormalFragment.etInputPrice, 0);
                    mLoanFragment.customKeyboardManager.hideSoftKeyboard(mLoanFragment.etInputPrice, 1);
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


    @OnClick({R.id.tv_normal_account_flow, R.id.tv_loan_account_flow, R.id.tv_credit_card_flow, R.id.iv_ac_account_flow_confirm})
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
        if (view.getId() == R.id.iv_ac_account_flow_confirm) {
            if (FastClickUtils.isFastClick()) {
                return;
            }
            if (selectedFragmentId == R.id.tv_normal_account_flow) {
                if (mNormalFragment.debtNormalDetail == null) {
                    //通用记账
                    createAccount(mNormalFragment.map, 0);
                } else {
                    //先删除账单 在创建账单
                    deleteFastDebt(mNormalFragment.debtNormalDetail.getId());
                }
            } else if (selectedFragmentId == R.id.tv_loan_account_flow) {
                if (mLoanFragment.debtNormalDetail == null) {
                    //网贷记账
                    createAccount(mLoanFragment.map, 1);
                } else {
                    //先删除账单 在创建账单
                    deleteLoanDebt(mLoanFragment.debtNormalDetail.getId());
                }
            }
        } else {
            selectedFragmentId = view.getId();
        }
    }

    /**
     * 删除通用账单
     */
    private void deleteFastDebt(String debtId) {
        Api.getInstance().deleteFastDebt(UserHelper.getInstance(this).getProfile().getId(), debtId)
                .compose(RxUtil.<ResultEntity>io2main())
                .subscribe(new Consumer<ResultEntity>() {
                               @Override
                               public void accept(ResultEntity result) throws Exception {
                                   if (result.isSuccess()) {
                                       createAccount(mNormalFragment.map, 0);
                                   } else {
                                       showErrorMsg(result.getMsg());
                                   }
                               }
                           },
                        new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {


                            }
                        });

    }


    /**
     * 删除账单
     */
    public void deleteLoanDebt(String debtId) {
        Api.getInstance().deleteDebt(UserHelper.getInstance(this).getProfile().getId(), debtId)
                .compose(RxUtil.<ResultEntity>io2main())
                .subscribe(new Consumer<ResultEntity>() {
                               @Override
                               public void accept(ResultEntity result) throws Exception {
                                   if (result.isSuccess()) {
                                       createAccount(mLoanFragment.map, 1);
                                   } else {
                                       showErrorMsg(result.getMsg());
                                   }
                               }
                           },
                        new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                            }
                        });
    }


    /**
     * 创建通用 网贷账单
     */
    private void createAccount(Map<String, Object> map, int type) {
        if (map == null) {
            return;
        }
        if (map.get("amount") == null) {
            return;
        }

        double amount = Double.parseDouble(map.get("amount")+"");
        if (amount < 0D) {
            ToastUtils.showToast(this, "每期金额不能小于0");
            return;
        } else if (amount == 0D) {
            ToastUtils.showToast(this, "每期金额不能为0");
            return;
        } else if (amount > 999999999D) {
            ToastUtils.showToast(this, "输入的金额太大啦");
            return;
        }

        if (map.get("projectName") == null && type == 0) {
            ToastUtils.showToast(this, "账单名称不能为空");
            return;
        }
        if (map.get("channelName") == null && type == 1) {
            ToastUtils.showToast(this, "账单名称不能为空");
            return;
        }


        map.put("userId", UserHelper.getInstance(this).getProfile().getId());
        if ( type == 0) {
            createFastAccount(map);
        } else {
            createLoanAccount(map);
        }
    }

    /**
     * 创建快捷记账
     */
    public void createFastAccount(Map<String, Object> map) {
        Api.getInstance().createNormalAccount(map)
                .compose(RxUtil.<ResultEntity>io2main())
                .subscribe(new Consumer<ResultEntity>() {
                               @Override
                               public void accept(ResultEntity result) throws Exception {
                                   Toast.makeText(AccountFlowActivity.this, result.getMsg(), Toast.LENGTH_SHORT).show();
                                   if (result.isSuccess()) {
                                       Intent intent = new Intent(AccountFlowActivity.this, MainActivity.class);
                                       intent.putExtra("account", true);
                                       intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                       startActivity(intent);
                                       finish();
                                   }
                               }
                           },
                        new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                Log.e("exception_custom", throwable.getMessage());
                            }
                        });
    }

    /**
     * 创建借贷记账
     */
    public void createLoanAccount(Map<String, Object> map) {
        Api.getInstance().createLoanAccount(map)
                .compose(RxUtil.<ResultEntity>io2main())
                .subscribe(new Consumer<ResultEntity>() {
                               @Override
                               public void accept(ResultEntity result) throws Exception {
                                   Toast.makeText(AccountFlowActivity.this, result.getMsg(), Toast.LENGTH_SHORT).show();

                                   if (result.isSuccess()) {
                                       Intent intent = new Intent(AccountFlowActivity.this, MainActivity.class);
                                       intent.putExtra("account", true);
                                       intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                       startActivity(intent);
                                       finish();
                                   }
                               }
                           },
                        new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                Log.e("exception_custom", throwable.getMessage());
                            }
                        });
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