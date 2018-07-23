package com.beihui.market.ui.activity;

import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.beihui.market.R;
import com.beihui.market.api.Api;
import com.beihui.market.api.ResultEntity;
import com.beihui.market.base.BaseComponentActivity;
import com.beihui.market.entity.BillSummaryBean;
import com.beihui.market.entity.LoanAccountIconBean;
import com.beihui.market.helper.SlidePanelHelper;
import com.beihui.market.helper.UserHelper;
import com.beihui.market.injection.component.AppComponent;
import com.beihui.market.tang.rx.RxResponse;
import com.beihui.market.tang.rx.observer.ApiObserver;
import com.beihui.market.ui.adapter.BillSummaryAdapter;
import com.beihui.market.util.CommonUtils;
import com.beihui.market.util.RxUtil;
import com.gyf.barlibrary.ImmersionBar;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import io.reactivex.Observer;
import io.reactivex.functions.Consumer;

public class BillSummaryActivity extends BaseComponentActivity {
    @BindView(R.id.tool_bar_sunmary)
    Toolbar toolbar;
    private BillSummaryAdapter adapter;
    @BindView(R.id.bill_summary_recycler)
    RecyclerView recyclerView;
    private int pageNo = 1;
    @BindView(R.id.bill_refresh)
    SmartRefreshLayout refreshLayout;

    private TextView toatlLiMoney;
    private TextView toatlOverMoney;

    @Override
    public int getLayoutId() {
        return R.layout.activity_bill_summary;
    }

    @Override
    public void configViews() {
        ImmersionBar.with(this).statusBarDarkFont(true).init();
        setupToolbar(toolbar);
        SlidePanelHelper.attach(this);

    }

    @Override
    public void initDatas() {
        List<BillSummaryBean.PersonBillItemBean> list = new ArrayList<>();
        getBillSummaryData();
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                pageNo = 1;
                getBillSummaryData();

            }
        });

//        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
//            @Override
//            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
//                getBillSummaryData();
//            }
//        });
        View view = View.inflate(this, R.layout.bill_summary_header, null);
        toatlLiMoney = view.findViewById(R.id.totalliamount);
        toatlOverMoney = view.findViewById(R.id.totalover_amount);
        adapter = new BillSummaryAdapter(R.layout.item_bill_summary_layout, list, this);
        adapter.addHeaderView(view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void configureComponent(AppComponent appComponent) {

    }

    private void getBillSummaryData() {
        String userId = UserHelper.getInstance(this).getProfile().getId();
        Api.getInstance().onBillSummary(userId, pageNo + "").compose(RxResponse.<BillSummaryBean>compatT()).subscribe(new ApiObserver<BillSummaryBean>() {
            @Override
            public void onNext(BillSummaryBean data) {
                refreshLayout.finishRefresh();
                pageNo++;
                adapter.setNewData(data.getPersonBillItem());
                toatlLiMoney.setText("¥ " + CommonUtils.numToString(data.getTotalLiAmount()));
                toatlOverMoney.setText("¥ " + CommonUtils.numToString(data.getOverLiAmount()));
            }

            @Override
            public void onError(Throwable t) {
                refreshLayout.finishLoadMore();
            }
        });
    }

}
