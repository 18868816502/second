package com.beihui.market.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.beihui.market.R;
import com.beihui.market.api.Api;
import com.beihui.market.api.ResultEntity;
import com.beihui.market.base.BaseComponentActivity;
import com.beihui.market.base.BaseComponentFragment;
import com.beihui.market.entity.CreateAccountReturnIDsBean;
import com.beihui.market.event.MyLoanDebtListFragmentEvent;
import com.beihui.market.helper.DataStatisticsHelper;
import com.beihui.market.helper.SlidePanelHelper;
import com.beihui.market.helper.UserHelper;
import com.beihui.market.injection.component.AppComponent;
import com.beihui.market.ui.fragment.AccountFlowCreditCardFragment;
import com.beihui.market.ui.fragment.AccountFlowLoanFragment;
import com.beihui.market.ui.fragment.AccountFlowNormalFragment;
import com.beihui.market.umeng.NewVersionEvents;
import com.beihui.market.util.FastClickUtils;
import com.beihui.market.util.InputMethodUtil;
import com.beihui.market.util.RxUtil;
import com.beihui.market.util.ToastUtil;
import com.beihui.market.view.NoScrollViewPager;
import com.gyf.barlibrary.ImmersionBar;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
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
    @BindView(R.id.tv_credit_card_flow)
    TextView mAccountFlowCreditCard;
    @BindView(R.id.fl_ac_account_flow_container)
    NoScrollViewPager mViewPager;
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
        setupToolbarBackNavigation(toolbar, R.drawable.x_delete);
        ImmersionBar.with(this).titleBar(toolbar).statusBarDarkFont(true).init();

        fragmentList.add(mNormalFragment);
        fragmentList.add(mLoanFragment);
        fragmentList.add(mCreditCardFragment);

        SlidePanelHelper.attach(this);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        //pv，uv统计
//        DataStatisticsHelper.getInstance().onCountUv(NewVersionEvents.TALLY);
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
                    mLoanFragment.customKeyboardManager.hideSoftKeyboard(mLoanFragment.etInputPrice, 1);
                    InputMethodUtil.openSoftKeyboard(AccountFlowActivity.this, mLoanFragment.etLoan);
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
            createAccount();
            if (FastClickUtils.isFastClick()) {
                return;
            }
            //pv，uv统计
            DataStatisticsHelper.getInstance().onCountUv(NewVersionEvents.TALLYTOPRIGHTCORNETHOOK);
        } else {
            selectedFragmentId = view.getId();
        }
    }

    public void createAccount() {
        if (FastClickUtils.isFastClick()) {
            return;
        }
        if (selectedFragmentId == R.id.tv_normal_account_flow) {
            //判断参数问题是否正确
            if (isCommit(mNormalFragment.map, 0)) {
                return;
            }

            if (mNormalFragment.debtNormalDetail == null) {
                //通用记账
                createAccount(mNormalFragment.map, 0);
            } else {
                //先删除账单 在创建账单
                deleteFastDebt(mNormalFragment.debtNormalDetail.getId());
            }
        } else if (selectedFragmentId == R.id.tv_loan_account_flow) {
            //判断参数问题是否正确
            if (isCommit(mLoanFragment.map, 1)) {
                return;
            }

            if (mLoanFragment.debtNormalDetail == null) {
                //网贷记账
                createAccount(mLoanFragment.map, 1);
            } else {
                //先删除账单 在创建账单
                deleteLoanDebt(mLoanFragment.debtNormalDetail.getId());
            }
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

        map.put("userId", UserHelper.getInstance(this).getProfile().getId());
        if (type == 0) {
            createFastAccount(map);
        } else {
            createLoanAccount(map);
        }
    }

    private boolean isCommit(Map<String, Object> map, int type) {
        if (map == null) {
            return true;
        }
        if (map.get("amount") == null) {
            return true;
        }
        if (!TextUtils.isEmpty((String) map.get("amount")) && (((String) map.get("amount")).substring(1).contains("+") || ((String) map.get("amount")).substring(1).contains("-"))) {
            //ToastUtils.showToast(this, "请输入正确的金额");
            ToastUtil.toast("请输入正确的金额");
            return true;
        }

        double amount = Double.parseDouble(map.get("amount") + "");
        if (amount < 0D) {
            //ToastUtils.showToast(this, "每期金额不能小于0");
            ToastUtil.toast("每期金额不能小于0");
            return true;
        } else if (amount == 0D) {
            //ToastUtils.showToast(this, "每期金额不能为0");
            ToastUtil.toast("每期金额不能为0");
            return true;
        } else if (amount > 999999999D) {
            //ToastUtils.showToast(this, "输入的金额太大啦");
            ToastUtil.toast("输入的金额太大啦");
            return true;
        }

        if (map.get("projectName") == null && type == 0) {
            //ToastUtils.showToast(this, "账单名称不能为空");
            ToastUtil.toast("账单名称不能为空");
            return true;
        }
        if (map.get("channelName") == null && type == 1) {
            //ToastUtils.showToast(this, "账单名称不能为空");
            ToastUtil.toast("账单名称不能为空");
            return true;
        }
        return false;
    }

    /**
     * 创建快捷记账
     */
    public void createFastAccount(Map<String, Object> map) {
        Api.getInstance().createNormalAccount(map)
                .compose(RxUtil.<ResultEntity<CreateAccountReturnIDsBean>>io2main())
                .subscribe(new Consumer<ResultEntity<CreateAccountReturnIDsBean>>() {
                               @Override
                               public void accept(ResultEntity<CreateAccountReturnIDsBean> result) throws Exception {
//                                   Toast.makeText(AccountFlowActivity.this, result.getMsg(), Toast.LENGTH_SHORT).show();

                                   /**
                                    * 返回详情页
                                    */
                                   if (result.isSuccess()) {
                                       EventBus.getDefault().postSticky(new MyLoanDebtListFragmentEvent(0));

                                       if (mNormalFragment.debtNormalDetail != null) {
                                           //ToastUtils.showToast(AccountFlowActivity.this, "更新成功，已默认初始还款状态");
                                           ToastUtil.toast("更新成功，已默认初始还款状态");
                                       }

                                       Intent intent = new Intent();
                                       intent.putExtra("recordId", result.getData().recordId);
                                       intent.putExtra("billId", result.getData().billId);
                                       setResult(1, intent);
                                       finish();
                                   } else {
                                       //ToastUtils.showToast(AccountFlowActivity.this, result.getMsg());
                                       ToastUtil.toast(result.getMsg());
                                   }
                               }
                           },
                        new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                //Log.e("exception_custom", throwable.getMessage());
                            }
                        });
    }

    /**
     * 创建借贷记账
     */
    public void createLoanAccount(Map<String, Object> map) {
        Api.getInstance().createLoanAccount(map)
                .compose(RxUtil.<ResultEntity<CreateAccountReturnIDsBean>>io2main())
                .subscribe(new Consumer<ResultEntity<CreateAccountReturnIDsBean>>() {
                               @Override
                               public void accept(ResultEntity<CreateAccountReturnIDsBean> result) throws Exception {
//                                   Toast.makeText(AccountFlowActivity.this, result.getMsg(), Toast.LENGTH_SHORT).show();

                                   if (result.isSuccess()) {
                                       EventBus.getDefault().postSticky(new MyLoanDebtListFragmentEvent(1));

                                       if (mLoanFragment.debtNormalDetail != null) {
                                           //ToastUtils.showToast(AccountFlowActivity.this, "更新成功，已默认初始还款状态");
                                           ToastUtil.toast("更新成功，已默认初始还款状态");
                                       }

                                       /**
                                        * 返回详情页
                                        */
                                       Intent intent = new Intent();
                                       intent.putExtra("recordId", result.getData().recordId);
                                       intent.putExtra("billId", result.getData().billId);
                                       setResult(1, intent);
                                       finish();
                                   } else {
                                       //ToastUtils.showToast(AccountFlowActivity.this, result.getMsg());
                                       ToastUtil.toast(result.getMsg());
                                   }
                               }
                           },
                        new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                //Log.e("exception_custom", throwable.getMessage());
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