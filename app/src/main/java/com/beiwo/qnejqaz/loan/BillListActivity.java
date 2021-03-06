package com.beiwo.qnejqaz.loan;

import android.content.Intent;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.beiwo.qnejqaz.R;
import com.beiwo.qnejqaz.api.Api;
import com.beiwo.qnejqaz.base.BaseComponentActivity;
import com.beiwo.qnejqaz.entity.HomeData;
import com.beiwo.qnejqaz.helper.DataHelper;
import com.beiwo.qnejqaz.helper.SlidePanelHelper;
import com.beiwo.qnejqaz.helper.UserHelper;
import com.beiwo.qnejqaz.tang.activity.LoanBillActivity;
import com.beiwo.qnejqaz.tang.rx.RxResponse;
import com.beiwo.qnejqaz.tang.rx.observer.ApiObserver;
import com.beiwo.qnejqaz.util.DensityUtil;
import com.beiwo.qnejqaz.util.FormatNumberUtils;
import com.gyf.barlibrary.ImmersionBar;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.text.SimpleDateFormat;
import java.util.Locale;

import butterknife.BindView;
import io.reactivex.annotations.NonNull;

/**
 * https://gitee.com/tangbuzhi
 *
 * @author: Tangbuzhi
 * @version:
 * @package:
 * @description:
 * @modify:
 * @date: 2018/10/15
 */

public class BillListActivity extends BaseComponentActivity {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.refresh_layout)
    SmartRefreshLayout refresh_layout;
    @BindView(R.id.recycler)
    RecyclerView recycler;
    @BindView(R.id.tv_add_bill)
    TextView tv_add_bill;
    private View headView;
    private TextView head_month_num, head_bill_num, head_add_bill;

    private BillListAdapter listAdapter = new BillListAdapter();
    private UserHelper userHelper;
    private int pageNo = 1;
    private String x_month;
    private double num;
    private int yOffset;
    private String currentMonth;

    @Override
    public int getLayoutId() {
        return R.layout.activity_bill_list;
    }

    @Override
    public void configViews() {
        nao = System.currentTimeMillis();
        setupToolbar(toolbar, true);
        setupToolbarBackNavigation(toolbar, R.drawable.back_white);
        ImmersionBar.with(this).statusBarDarkFont(false).init();
        SlidePanelHelper.attach(this);

        currentMonth = new SimpleDateFormat("MM", Locale.CHINA).format(System.currentTimeMillis());
    }

    private long nao;

    @Override
    protected void onStop() {
        super.onStop();
        if (nao != 0 &&System.currentTimeMillis() - nao > 500 && System.currentTimeMillis() - nao < DataHelper.MAX_SECOND) {
            DataHelper.getInstance(this).event(DataHelper.EVENT_TYPE_STAY, DataHelper.EVENT_VIEWID_TALLYHOMEPAGE, "", System.currentTimeMillis() - nao);
        }
    }

    @Override
    public void initDatas() {
        userHelper = UserHelper.getInstance(this);
        headView();
        initRecycler();
    }

    @Override
    protected void onResume() {
        super.onResume();
        request();
    }

    private void initRecycler() {
        recycler.setLayoutManager(new LinearLayoutManager(this));
        recycler.setItemAnimator(new DefaultItemAnimator());
        recycler.setAdapter(listAdapter);
        listAdapter.setHeaderView(headView);
        recycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                yOffset += dy;
                if (yOffset >= DensityUtil.dp2px(getApplicationContext(), 120f)) {
                    tv_add_bill.setVisibility(View.VISIBLE);
                } else {
                    tv_add_bill.setVisibility(View.GONE);
                }
            }
        });
        refresh_layout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@android.support.annotation.NonNull RefreshLayout refreshLayout) {
                pageNo = 1;
                request();
            }
        });
        refresh_layout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@android.support.annotation.NonNull RefreshLayout refreshLayout) {
                pageNo++;
                request();
            }
        });
    }

    public void request() {
        if (userHelper.isLogin()) {
            Api.getInstance().home(userHelper.id(), pageNo)
                    .compose(RxResponse.<HomeData>compatT())
                    .subscribe(new ApiObserver<HomeData>() {
                        @Override
                        public void onNext(@NonNull HomeData data) {
                            x_month = String.format(getString(R.string.x_month_repay), data.getXmonth());//x月应还
                            head_month_num.setText(x_month);
                            num = data.getTotalAmount();
                            head_bill_num.setText(FormatNumberUtils.FormatNumberFor2(num));

                            refresh_layout.finishRefresh();
                            refresh_layout.finishLoadMore();
                            if (pageNo == 1) {
                                if (data.getItem() == null || data.getItem().size() < 1) {
                                    empty();
                                } else {
                                    listAdapter.setNewData(data.getItem());
                                }
                            } else {
                                listAdapter.addData(data.getItem());
                            }
                        }

                        @Override
                        public void onError(@NonNull Throwable t) {
                            super.onError(t);
                            refresh_layout.finishRefresh();
                            refresh_layout.finishLoadMore();
                            head_month_num.setText(String.format(getString(R.string.x_month_repay), currentMonth));
                            head_bill_num.setText(FormatNumberUtils.FormatNumberFor2(num));
                            empty();
                        }
                    });
        }
    }

    private void empty() {
        listAdapter.setNewData(null);
        listAdapter.setEmptyView(R.layout.empty_bill_list, recycler);

        View emptyView = listAdapter.getEmptyView();
        TextView head_month_num = emptyView.findViewById(R.id.head_month_num);
        TextView head_bill_num = emptyView.findViewById(R.id.head_bill_num);
        head_month_num.setText(String.format(getString(R.string.x_month_repay), currentMonth));
        head_bill_num.setText(FormatNumberUtils.FormatNumberFor2(num));
        emptyView.findViewById(R.id.head_add_bill).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), LoanBillActivity.class));
            }
        });
    }

    private void headView() {
        headView = LayoutInflater.from(this).inflate(R.layout.head_bill_layout, null);
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, DensityUtil.dp2px(this, 110));
        headView.setLayoutParams(lp);
        head_month_num = headView.findViewById(R.id.head_month_num);
        head_bill_num = headView.findViewById(R.id.head_bill_num);
        head_add_bill = headView.findViewById(R.id.head_add_bill);
        head_add_bill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), LoanBillActivity.class));
            }
        });
        tv_add_bill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), LoanBillActivity.class));
            }
        });
    }
}