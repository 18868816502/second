package com.beihui.market.ui.activity;


import android.graphics.Color;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.beihui.market.R;
import com.beihui.market.base.BaseComponentActivity;
import com.beihui.market.injection.component.AppComponent;
import com.beihui.market.ui.adapter.BillLoanAnalysisRvAdapter;
import com.beihui.market.view.multiChildHistogram.MultiGroupHistogramChildData;
import com.beihui.market.view.multiChildHistogram.MultiGroupHistogramGroupData;
import com.beihui.market.view.multiChildHistogram.MultiGroupHistogramView;
import com.gyf.barlibrary.ImmersionBar;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import butterknife.BindView;

/**
 * Created by admin on 2018/5/22.
 * 网贷分析
 */

public class BillLoanAnalysisActivity extends BaseComponentActivity {

    @BindView(R.id.tool_bar)
    Toolbar toolbar;
    @BindView(R.id.rv_ac_bill_loan_analysis)
    RecyclerView mRecyclerView;




    //适配器
    public BillLoanAnalysisRvAdapter mAdapter;

    @Override
    public int getLayoutId() {
        return R.layout.x_activity_bill_loan_analysis;
    }

    @Override
    public void configViews() {
        setupToolbar(toolbar);
        ImmersionBar.with(this).titleBar(toolbar).statusBarDarkFont(true).init();

        //初始化适配器
        mAdapter = new BillLoanAnalysisRvAdapter(this);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.setAdapter(mAdapter);



    }

    @Override
    public void initDatas() {

    }

    @Override
    protected void configureComponent(AppComponent appComponent) {

    }



}
