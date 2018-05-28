package com.beihui.market.ui.activity;


import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.beihui.market.R;
import com.beihui.market.api.Api;
import com.beihui.market.api.ResultEntity;
import com.beihui.market.base.BaseComponentActivity;
import com.beihui.market.entity.AnalysisChartBean;
import com.beihui.market.entity.AnalysisOverviewBean;
import com.beihui.market.entity.BillLoanAnalysisBean;
import com.beihui.market.entity.TabAccountBean;
import com.beihui.market.event.BillLoanRvAdapterEvent;
import com.beihui.market.helper.UserHelper;
import com.beihui.market.injection.component.AppComponent;
import com.beihui.market.ui.adapter.BillLoanAnalysisRvAdapter;
import com.beihui.market.ui.adapter.BillLoanRvAdapter;
import com.beihui.market.ui.presenter.TabAccountPresenter;
import com.beihui.market.util.RxUtil;
import com.beihui.market.view.multiChildHistogram.MultiGroupHistogramChildData;
import com.beihui.market.view.multiChildHistogram.MultiGroupHistogramGroupData;
import com.beihui.market.view.multiChildHistogram.MultiGroupHistogramView;
import com.gyf.barlibrary.ImmersionBar;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.functions.Consumer;


/**
 * Created by admin on 2018/5/22.
 * 网贷分析
 */

public class BillLoanAnalysisActivity extends BaseComponentActivity {

    @BindView(R.id.tool_bar)
    Toolbar toolbar;
    @BindView(R.id.tv_ac_bill_loan_analysis_week)
    TextView mWeek;
    @BindView(R.id.tv_ac_bill_loan_analysis_month)
    TextView mMonth;
    @BindView(R.id.rv_ac_bill_loan_analysis)
    RecyclerView mRecyclerView;

    //	类型 1-日期, 2-周 3-月
    public int type = 3;

    public int selectId = R.id.tv_ac_bill_loan_analysis_month;

    //适配器
    public BillLoanAnalysisRvAdapter mAdapter;

    @Override
    public int getLayoutId() {
        return R.layout.x_activity_bill_loan_analysis;
    }


    /**
     * 响应柱状图的Item的点击 请求列表数据
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMainEvent(BillLoanRvAdapterEvent event) {

        mAdapter.mShowFirstItemPosition = true;

        int position = event.position;
        Log.e("adapter", "posistion-->" + position);
        if (type == 2) {
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.WEEK_OF_YEAR, -11 + position);
            //请求列表数据
            requestListData(calendar);
        }
        if (type == 3) {
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.MONTH, -5 + position);
            //请求列表数据
            requestListData(calendar);
        }
    }

    @Override
    public void configViews() {
        setupToolbar(toolbar);
        ImmersionBar.with(this).titleBar(toolbar).statusBarDarkFont(true).init();

        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }

        //初始化适配器
        mAdapter = new BillLoanAnalysisRvAdapter(this);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.setAdapter(mAdapter);

        //默认选中月
        mMonth.setSelected(true);
    }

    /**
     * 切换月、周
     */
    @OnClick({R.id.tv_ac_bill_loan_analysis_week, R.id.tv_ac_bill_loan_analysis_month})
    public void onViewClicked(View view) {

        switch (view.getId()) {
            case R.id.tv_ac_bill_loan_analysis_week:
                type = 2;
                if (selectId != R.id.tv_ac_bill_loan_analysis_week) {
                    mWeek.setSelected(true);
                    mMonth.setSelected(false);
                    mAdapter.mShowFirstItemPosition = false;
                    //柱状图数据
                    requestChartData(Calendar.getInstance());
                    //请求列表数据
                    requestListData(Calendar.getInstance());
                }
                selectId = view.getId();
                if (mAdapter != null) {
                    mAdapter.setType(2);
                }
                break;
            case R.id.tv_ac_bill_loan_analysis_month:
                type = 3;
                if (selectId != R.id.tv_ac_bill_loan_analysis_month) {
                    mWeek.setSelected(false);
                    mMonth.setSelected(true);
                    mAdapter.mShowFirstItemPosition = false;
                    //柱状图数据
                    requestChartData(Calendar.getInstance());
                    //请求列表数据
                    requestListData(Calendar.getInstance());
                }
                selectId = view.getId();
                if (mAdapter != null) {
                    mAdapter.setType(3);
                }
                break;
        }
    }


    /**
     * 列表的Item的事件监听
     */
    @Override
    public void initDatas() {
        if (mAdapter != null) {
            mAdapter.setOnItemClickListener(new BillLoanAnalysisRvAdapter.OnItemClickListener() {
                @Override
                public void onItemclick(BillLoanAnalysisBean.ListBean listBean) {
                    if (listBean.getType() == 1) {
                        Intent intent = new Intent(BillLoanAnalysisActivity.this, LoanDebtDetailActivity.class);
                        intent.putExtra("debt_id", listBean.getRecordId());
                        intent.putExtra("bill_id", listBean.getBillId());
                        startActivity(intent);
                    } else if (listBean.getType() == 3) {
                        //快速记账详情
                        Intent intent = new Intent(BillLoanAnalysisActivity.this, FastDebtDetailActivity.class);
                        intent.putExtra("debt_id", listBean.getRecordId());
                        intent.putExtra("bill_id", listBean.getBillId());
                        startActivity(intent);
                    }else {
                        Intent intent = new Intent(BillLoanAnalysisActivity.this, CreditCardDebtDetailActivity.class);
                        intent.putExtra("debt_id", listBean.getRecordId());
                        intent.putExtra("bill_id", listBean.getBillId());
                        intent.putExtra("logo", "");
                        intent.putExtra("bank_name", listBean.getTitle());
                        intent.putExtra("card_num", "");
                        intent.putExtra("by_hand", false);//是否是手动记账
                        startActivity(intent);
                    }
                }
            });
        }
    }

    /**
     * 进入详情页 返回 需要刷新数据
     */
    @Override
    protected void onResume() {
        super.onResume();
        mAdapter.mShowFirstItemPosition = false;
        //柱状图数据
        requestChartData(Calendar.getInstance());
        //请求列表数据
        requestListData(Calendar.getInstance());


        //底部数据
        Api.getInstance().queryAnalysisOverview(UserHelper.getInstance(this).getProfile().getId())
                .compose(RxUtil.<ResultEntity<AnalysisOverviewBean>>io2main())
                .subscribe(new Consumer<ResultEntity<AnalysisOverviewBean>>() {
                               @Override
                               public void accept(ResultEntity<AnalysisOverviewBean> result) throws Exception {
                                   if (result.isSuccess()) {
                                       mAdapter.notifyBottomData(result.getData());
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    /**
     * 柱状图数据
     */
    private void requestChartData(Calendar calendar) {
        //获取时间区间
        String start;
        String end;
        if (type == 2) {
            calendar.add(Calendar.WEEK_OF_YEAR, -11);
            int startWeek = calendar.get(Calendar.WEEK_OF_YEAR);
            int startYear = calendar.get(Calendar.YEAR);
            calendar.add(Calendar.WEEK_OF_YEAR, 23);
            int endWeek = calendar.get(Calendar.WEEK_OF_YEAR);
            int endYear = calendar.get(Calendar.YEAR);

            start = startYear+"-"+startWeek;
            end = endYear+"-"+endWeek;

            Log.e("calendar" , "calendar---> " + startYear+"-"+startWeek);
            Log.e("calendar" , "calendar---> " + endYear+"-"+endWeek);
        } else {
            calendar.add(Calendar.MONTH, -5);
            int startMonth = calendar.get(Calendar.MONTH)+1;
            int startYear = calendar.get(Calendar.YEAR);
            calendar.add(Calendar.MONTH, 11);
            int endMonth = calendar.get(Calendar.MONTH)+1;
            int endYear = calendar.get(Calendar.YEAR);

            start = startYear+"-"+startMonth;
            end = endYear+"-"+endMonth;
        }

        Api.getInstance().queryAnalysisOverviewChart(UserHelper.getInstance(this).getProfile().getId(), type, start, end)
                .compose(RxUtil.<ResultEntity<List<AnalysisChartBean>>>io2main())
                .subscribe(new Consumer<ResultEntity<List<AnalysisChartBean>>>() {
                               @Override
                               public void accept(ResultEntity<List<AnalysisChartBean>> result) throws Exception {
                                   if (result.isSuccess()) {
                                       mAdapter.notifyChartData(result.getData());
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
     * 请求列表数据
     */
    private void requestListData(Calendar calendar) {
        //获取时间区间
        String time;
        final String timeTitleTop;
        final String timeTitleBottom;
        if (type == 2) {
            int week = calendar.get(Calendar.WEEK_OF_YEAR);
            int year = calendar.get(Calendar.YEAR);
            time = year+"-"+week;


            timeTitleTop = getMonthDay(calendar)+"-"+getSunDay(calendar);
            timeTitleBottom = week+"";

            Log.e("calendar" , "calendar  time---> " + time);
        } else {
            int month = calendar.get(Calendar.MONTH)+1;
            int year = calendar.get(Calendar.YEAR);
            time = year+"-"+month;

            timeTitleTop = year+"年";
            timeTitleBottom = month+"";
            Log.e("calendar" , "calendar  time---> " + time);
        }

        //列表数据
        Api.getInstance().queryAnalysisOverviewList(UserHelper.getInstance(this).getProfile().getId(), type, time)
                .compose(RxUtil.<ResultEntity<BillLoanAnalysisBean>>io2main())
                .subscribe(new Consumer<ResultEntity<BillLoanAnalysisBean>>() {
                               @Override
                               public void accept(ResultEntity<BillLoanAnalysisBean> result) throws Exception {
                                   Log.e("calendar" , "result.isSuccess()---> " + result.isSuccess());
                                   if (result.isSuccess()) {
                                       mAdapter.notifyListData(result.getData(), timeTitleTop, timeTitleBottom);
                                   } else {
                                       showErrorMsg(result.getMsg());
                                   }
                               }
                           },
                        new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                Log.e("calendar" , "throwable.getMessage()---> " + throwable.getMessage());
                            }
                        });
    }

    /**
     * 获取周日
     */
    private String getSunDay(Calendar calendar) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("M.d");
        int day_of_week = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        if (day_of_week == 0) {
            day_of_week = 7;
        }
        calendar.add(Calendar.DATE, -day_of_week + 7);
        return simpleDateFormat.format(calendar.getTime());
    }

    /**
     * 获取周一
     */
    private String getMonthDay(Calendar calendar) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("M.d");
        int day_of_week = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        if (day_of_week == 0) {
            day_of_week = 7;
        }
        calendar.add(Calendar.DATE, -day_of_week + 1);
        simpleDateFormat.format(calendar.getTime());
        return simpleDateFormat.format(calendar.getTime());
    }

    @Override
    protected void configureComponent(AppComponent appComponent) {

    }
}

