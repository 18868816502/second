package com.beihui.market.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.beihui.market.R;
import com.beihui.market.api.Api;
import com.beihui.market.base.BaseComponentActivity;
import com.beihui.market.entity.BillSummaryBean;
import com.beihui.market.helper.SlidePanelHelper;
import com.beihui.market.helper.UserHelper;
import com.beihui.market.injection.component.AppComponent;
import com.beihui.market.tang.activity.CommonDetailActivity;
import com.beihui.market.tang.activity.CreditDetailActivity;
import com.beihui.market.tang.activity.NetLoanDetailActivity;
import com.beihui.market.tang.rx.RxResponse;
import com.beihui.market.tang.rx.observer.ApiObserver;
import com.beihui.market.ui.adapter.BillSummaryAdapter;
import com.beihui.market.util.CommonUtils;
import com.beihui.market.util.FormatNumberUtils;
import com.beihui.market.util.LogUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.gyf.barlibrary.ImmersionBar;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class BillSummaryActivity extends BaseComponentActivity {
    @BindView(R.id.tool_bar_sunmary)
    Toolbar toolbar;
    private BillSummaryAdapter adapter;
    @BindView(R.id.bill_summary_recycler)
    RecyclerView recyclerView;
    private int pageNo = 1;
    @BindView(R.id.bill_refresh)
    SmartRefreshLayout refreshLayout;
    List<BillSummaryBean.PersonBillItemBean> list = new ArrayList<>();
    private TextView toatlLiMoney;
    private TextView toatlOverMoney;
    private BillSummaryActivity activity;
    private View view;
    private boolean isRefresh = true;
    private View emptyView;
    private int postion;
    private boolean isFirst = true;
    private int first;
    private final static String TAG = BillSummaryActivity.class.getSimpleName();
    private TextView totalTile;
    private TextView liTile;

    @Override
    public int getLayoutId() {
        return R.layout.activity_bill_summary;
    }

    @Override
    public void configViews() {
        ImmersionBar.with(this).statusBarDarkFont(true).init();
        setupToolbar(toolbar);
        SlidePanelHelper.attach(this);
        activity = this;

    }

    @Override
    public void initDatas() {
        getBillSummaryData(false);
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                pageNo = 1;
                isRefresh = true;
                getBillSummaryData(false);
            }
        });

        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                isRefresh = false;
                getBillSummaryData(false);
            }
        });
        view = View.inflate(this, R.layout.bill_summary_header, null);
        emptyView = View.inflate(this, R.layout.empty_bill_summary_layout, null);
        toatlLiMoney = view.findViewById(R.id.totalliamount);
        toatlOverMoney = view.findViewById(R.id.totalover_amount);
        totalTile = view.findViewById(R.id.total_title);
        liTile = view.findViewById(R.id.li_tv);
        totalTile.setTextColor(Color.argb(204, 255, 255, 255));
        liTile.setTextColor(Color.argb(204, 255, 255, 255));
        adapter = new BillSummaryAdapter(R.layout.item_bill_summary_layout, list, this, activity);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = null;
                postion = position;
                if (list.get(position).getType().equals("1")) {//网贷
                    intent = new Intent(activity, NetLoanDetailActivity.class);
                } else if (list.get(position).getType().equals("2")) {//信用卡
                    intent = new Intent(activity, CreditDetailActivity.class);
                } else if (list.get(position).getType().equals("3")) {//通用
                    intent = new Intent(activity, CommonDetailActivity.class);
                }
                if (intent != null) {
                    intent.putExtra("recordId", list.get(position).getRecordId());
                    intent.putExtra("billId", list.get(position).billId);
                    intent.putExtra("title", list.get(position).getTitle());
                    activity.startActivityForResult(intent, 0);
                }
            }
        });
    }

    @Override
    protected void configureComponent(AppComponent appComponent) {
    }

    private void getBillSummaryData(final boolean isDelete) {
        String userId = UserHelper.getInstance(this).getProfile().getId();
        Api.getInstance().onBillSummary(userId, pageNo + "").compose(RxResponse.<BillSummaryBean>compatT()).subscribe(new ApiObserver<BillSummaryBean>() {
            @Override
            public void onNext(BillSummaryBean data) {

                pageNo++;
                refreshLayout.finishRefresh();
                refreshLayout.finishLoadMore();
                if (isFirst && data.getPersonBillItem() != null) {
                    first = data.getPersonBillItem().size();
                    isFirst = false;
                }

                if (!isDelete) {
                    if (isRefresh && data.getPersonBillItem() != null && data.getPersonBillItem().size() == 0) {
                        adapter.setEmptyView(emptyView);
                    } else if (isRefresh && data.getPersonBillItem() != null && data.getPersonBillItem().size() > 0) {
                        adapter.removeAllHeaderView();
                        adapter.addHeaderView(view);
                    }
                    if (isRefresh) {
                        list.clear();
                        adapter.replaceData(data.getPersonBillItem());
                    } else {
                        adapter.addData(data.getPersonBillItem());
                        list.addAll(data.getPersonBillItem());
                    }
                }
                if (data.getTotalLiAmount() != null) {
                    double num = Double.valueOf(data.getTotalLiAmount());
                    toatlLiMoney.setText("¥ " + FormatNumberUtils.FormatNumberFor2(num));
                } else {
                    toatlLiMoney.setText("¥ 0.00");
                }

                if (data.getOverLiAmount() != null) {
                    double num = Double.valueOf(data.getOverLiAmount());
                    toatlOverMoney.setText("¥ " + FormatNumberUtils.FormatNumberFor2(num));
                } else {
                    toatlOverMoney.setText("¥ 0.00");
                }

            }

            @Override
            public void onError(Throwable t) {
                refreshLayout.finishRefresh();
                refreshLayout.finishLoadMore();
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0 && resultCode == 100) {
            adapter.remove(postion);
            if (list.size() == 1 || first == 1) {
                adapter.setEmptyView(emptyView);
                first = 0;
            }
            getBillSummaryData(true);
            LogUtils.i(TAG, "列表大小" + list.size());

        }
    }
}
