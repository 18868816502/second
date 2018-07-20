package com.beihui.market.ui.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.beihui.market.App;
import com.beihui.market.BuildConfig;
import com.beihui.market.R;
import com.beihui.market.api.Api;
import com.beihui.market.api.ResultEntity;
import com.beihui.market.base.BaseTabFragment;
import com.beihui.market.entity.Bill;
import com.beihui.market.entity.DebtAbstract;
import com.beihui.market.entity.HomeData;
import com.beihui.market.entity.TabAccountNewBean;
import com.beihui.market.helper.DataStatisticsHelper;
import com.beihui.market.helper.UserHelper;
import com.beihui.market.injection.component.AppComponent;
import com.beihui.market.injection.component.DaggerLoanDetailComponent;
import com.beihui.market.tang.activity.AddBillActivity;
import com.beihui.market.tang.activity.CreditBillActivity;
import com.beihui.market.tang.adapter.HomeBillAdapter;
import com.beihui.market.tang.rx.RxResponse;
import com.beihui.market.tang.rx.observer.ApiObserver;
import com.beihui.market.ui.rvdecoration.AccountFlowLoanItemDeco;
import com.beihui.market.umeng.NewVersionEvents;
import com.beihui.market.util.CommonUtils;
import com.beihui.market.util.ToastUtils;
import com.gyf.barlibrary.ImmersionBar;

import java.math.BigInteger;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.annotations.NonNull;

/**
 * https://gitee.com/tangbuzhi
 *
 * @author: Tangbuzhi
 * @version:
 * @package:
 * @description:
 * @modify:
 * @date: 2018/7/17
 */

public class HomeFragment extends BaseTabFragment {

    @BindView(R.id.refresh_layout)
    SwipeRefreshLayout mRefreshLayout;
    @BindView(R.id.toolbar)
    Toolbar mToolBar;
    @BindView(R.id.tv_event_entry)
    TextView mTvEvnentEntry;
    @BindView(R.id.tv_month_num)
    TextView mTvMonthNum;
    @BindView(R.id.tv_bill_num)
    TextView mTvBillNum;
    @BindView(R.id.iv_bill_visible)
    ImageView mIvBillVisible;
    @BindView(R.id.recycler)
    RecyclerView mRecycler;

    private List<Bill> bills = null;
    private HomeBillAdapter billAdapter;
    private UserHelper userHelper;

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    @Override
    public int getLayoutResId() {
        return R.layout.f_fragment_home;
    }

    /**
     * 统计点击tab事件
     */
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //pv，uv统计
        DataStatisticsHelper.getInstance().onCountUv(DataStatisticsHelper.ID_CLICK_TAB_ACCOUNT);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void configViews() {
        int statusHeight = CommonUtils.getStatusBarHeight(getActivity());
        //设置toolbar的高度为状态栏相同高度
        mToolBar.setPadding(mToolBar.getPaddingLeft(), statusHeight, mToolBar.getPaddingRight(), 0);
        ViewGroup.LayoutParams lp = mToolBar.getLayoutParams();
        lp.height = 0;
        mToolBar.setLayoutParams(lp);
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mRefreshLayout.setRefreshing(false);
                request();
            }
        });
    }

    @Override
    public void initDatas() {
        initRecyclerView();
        request();
    }

    private void request() {
        userHelper = UserHelper.getInstance(getActivity());
        if (userHelper.isLogin()) {
            //活动入口
            Api.getInstance().homeEvent("3", 1)
                    .compose(RxResponse.compatO())
                    .subscribe(new ApiObserver<Object>() {
                        @Override
                        public void onNext(@NonNull Object data) {

                        }
                    });
            //首页数据
            Api.getInstance().home(userHelper.id(), "1")
                    .compose(RxResponse.<HomeData>compatT())
                    .subscribe(new ApiObserver<HomeData>() {
                        @Override
                        public void onNext(@NonNull HomeData data) {
                            //账单头
                            //x月应还
                            mTvMonthNum.setText(String.format(getString(R.string.x_month_repay), data.getXmonth()));
                            //应还金额
                            mTvBillNum.setText(String.format("￥%.2f", data.getTotalAmount()));
                            bills = data.getItem();
                            billAdapter.setNewData(bills);
                        }
                    });
        }
    }

    private void initRecyclerView() {
        mRecycler.setItemAnimator(new DefaultItemAnimator());
        mRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        billAdapter = new HomeBillAdapter();
        mRecycler.setAdapter(billAdapter);
        billAdapter.setPayAllAndItemClickListener(new HomeBillAdapter.OnPayAllAndItemClickListener() {
            @Override
            public void payAllClick(int type, String billId, String recordId, double amount) {
                showDialog(type, billId, recordId, amount);
            }

            @Override
            public void itemClick(String recordId, String billId) {

            }
        });
    }

    @Override
    protected void configureComponent(AppComponent appComponent) {
    }

    @Override
    protected boolean needFakeStatusBar() {
        return false;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //pv，uv统计
        DataStatisticsHelper.getInstance().onCountUv(NewVersionEvents.HP);
    }

    @OnClick({R.id.tv_event_entry, R.id.iv_bill_visible, R.id.tv_add_account_bill, R.id.tv_credit_in})
    public void onClick(View view) {
        switch (view.getId()) {
            //记账分钱
            case R.id.tv_event_entry:
                ToastUtils.showToast(getActivity(), "event entry");
                break;
            //还款金额是否可见
            case R.id.iv_bill_visible:
                ToastUtils.showToast(getActivity(), "bill num visible");
                break;
            //添加账单
            case R.id.tv_add_account_bill:
                startActivity(new Intent(getActivity(), AddBillActivity.class));
                break;
            //导入信用卡
            case R.id.tv_credit_in:
                startActivity(new Intent(getActivity(), CreditBillActivity.class));
                break;
            default:
                break;
        }
    }

    private void showDialog(final int type, final String billId, final String recordId, final double amount) {
        final Dialog dialog = new Dialog(getActivity(), 0);
        View dialogView = LayoutInflater.from(getActivity()).inflate(R.layout.dlg_pay_over_bill, null);
        View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.confirm:
                        //pv，uv统计
                        DataStatisticsHelper.getInstance().onCountUv(DataStatisticsHelper.ID_SET_STATUS_PAID);
                        if (type == 2) {//信用卡记账
                            Api.getInstance().updateCreditCardBillStatus(userHelper.id(), billId, 2)
                                    .compose(RxResponse.compatO())
                                    .subscribe(new ApiObserver<Object>() {
                                        @Override
                                        public void onNext(@NonNull Object data) {
                                            request();
                                        }
                                    });
                        } else if (type == 1) {//网贷记账
                            Api.getInstance().updateDebtStatus(userHelper.id(), billId, 2)
                                    .compose(RxResponse.compatO())
                                    .subscribe(new ApiObserver<Object>() {
                                        @Override
                                        public void onNext(@NonNull Object data) {
                                            request();
                                        }
                                    });
                        } else if (type == 3) {//快捷记账
                            Api.getInstance().updateFastDebtBillStatus(userHelper.id(), billId, recordId, 2, amount)
                                    .compose(RxResponse.compatO())
                                    .subscribe(new ApiObserver<Object>() {
                                        @Override
                                        public void onNext(@NonNull Object data) {
                                            request();
                                        }
                                    });
                        }
                        dialog.dismiss();
                        break;
                    case R.id.cancel:
                        dialog.dismiss();
                        break;
                    default:
                        break;
                }
            }
        };
        dialogView.findViewById(R.id.cancel).setOnClickListener(clickListener);
        dialogView.findViewById(R.id.confirm).setOnClickListener(clickListener);
        dialog.setContentView(dialogView);
        dialog.setCanceledOnTouchOutside(true);
        Window window = dialog.getWindow();
        if (window != null) {
            WindowManager.LayoutParams lp = window.getAttributes();
            window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
            window.setAttributes(lp);
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
        dialog.show();
    }
}
