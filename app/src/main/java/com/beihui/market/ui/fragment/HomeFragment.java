package com.beihui.market.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.beihui.market.App;
import com.beihui.market.R;
import com.beihui.market.base.BaseTabFragment;
import com.beihui.market.entity.DebtAbstract;
import com.beihui.market.entity.TabAccountNewBean;
import com.beihui.market.helper.DataStatisticsHelper;
import com.beihui.market.helper.UserHelper;
import com.beihui.market.injection.component.AppComponent;
import com.beihui.market.tang.activity.AddBillActivity;
import com.beihui.market.tang.rx.RxResponse;
import com.beihui.market.tang.rx.observer.ApiObserver;
import com.beihui.market.umeng.NewVersionEvents;
import com.beihui.market.util.CommonUtils;
import com.beihui.market.util.ToastUtils;
import com.gyf.barlibrary.ImmersionBar;

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
                configureComponent(App.getInstance().getAppComponent());
            }
        });
    }

    @Override
    public void initDatas() {

    }

    @Override
    protected void configureComponent(AppComponent appComponent) {
        UserHelper.Profile profile = UserHelper.getInstance(getActivity()).getProfile();
        if (profile != null && profile.getId() != null) {
            appComponent.getApi().queryTabAccountHeaderInfo(profile.getId(), 6)
                    .compose(RxResponse.<DebtAbstract>compatT())
                    .subscribe(new ApiObserver<DebtAbstract>() {
                        @Override
                        public void onNext(@NonNull DebtAbstract data) {
                            //账单头
                            //x月应还
                            mTvMonthNum.setText(String.format(getString(R.string.x_month_repay), "6"));
                            mTvBillNum.setText(String.format("￥%.2f", data.unRepayAmount));
                        }
                    });
        /*appComponent.getApi().queryTabAccountList(userHelper.getProfile().getId(), 1)
                .compose(RxResponse.<List<TabAccountNewBean>>compatT())
                .subscribe(new ApiObserver<List<TabAccountNewBean>>() {
                    @Override
                    public void onNext(@NonNull List<TabAccountNewBean> data) {
                        //账单列表
                    }
                });*/
            appComponent.getApi().queryTabAccountList(profile.getId(), 1, 1, 10)
                    .compose(RxResponse.<List<TabAccountNewBean>>compatT())
                    .subscribe(new ApiObserver<List<TabAccountNewBean>>() {
                        @Override
                        public void onNext(@NonNull List<TabAccountNewBean> data) {
                            //
                        }
                    });
        }
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
                ToastUtils.showToast(getActivity(), "导入信用卡");
                break;
        }
    }
}
