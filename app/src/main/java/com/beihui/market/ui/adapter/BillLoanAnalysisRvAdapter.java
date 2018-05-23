package com.beihui.market.ui.adapter;

import android.app.Activity;
import android.graphics.Color;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.beihui.market.R;
import com.beihui.market.entity.BillLoanAnalysisBean;
import com.beihui.market.view.multiChildHistogram.MultiGroupHistogramChildData;
import com.beihui.market.view.multiChildHistogram.MultiGroupHistogramGroupData;
import com.beihui.market.view.multiChildHistogram.MultiGroupHistogramView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by admin on 2018/5/22.
 * 网贷分析适配器
 */

public class BillLoanAnalysisRvAdapter extends RecyclerView.Adapter<BillLoanAnalysisRvAdapter.ViewHolder> {

    public Activity mActivity;

    public BillLoanRvAdapter mAdapter;

    //数据源 列表数据以及底部数据
    public BillLoanAnalysisBean mBean = new BillLoanAnalysisBean();

    //头布局
    public static final int VIEW_HEADER = R.layout.x_item_bill_loan_analysis_header;
    //列表布局
    public static final int VIEW_NORMAL = R.layout.x_item_bill_loan_analysis_normal;
    //脚布局
    public static final int VIEW_FOOTER = R.layout.x_item_bill_loan_analysis_footer;

    //没有数据的布局
    public static final int VIEW_NODATA = R.layout.x_item_bill_loan_analysis_nodata;
    public BillLoanAnalysisRvAdapter(Activity mActivity) {
        this.mActivity = mActivity;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder viewHolder = new ViewHolder(LayoutInflater.from(mActivity).inflate(viewType, parent, false), viewType);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (holder.viewType == VIEW_HEADER) {
            mAdapter = new BillLoanRvAdapter(mActivity);
            LinearLayoutManager manager = new LinearLayoutManager(mActivity);
            manager.setOrientation(LinearLayoutManager.HORIZONTAL);
            holder.multiGroupHistogramView.setLayoutManager(manager);
            holder.multiGroupHistogramView.setAdapter(mAdapter);

//            Random random = new Random();
//            int groupSize = random.nextInt(10) + 10;
//            List<MultiGroupHistogramGroupData> groupDataList = new ArrayList<>();
//            for (int i = 0; i < groupSize; i++) {
//                List<MultiGroupHistogramChildData> childDataList = new ArrayList<>();
//                MultiGroupHistogramGroupData groupData = new MultiGroupHistogramGroupData();
//                groupData.setGroupName(+ (i + 1) + "周");
//                MultiGroupHistogramChildData childData1 = new MultiGroupHistogramChildData();
//                childData1.setSuffix("");
//                childData1.setValue(300.00f);
//                childDataList.add(childData1);
//
//                groupData.setChildDataList(childDataList);
//                groupDataList.add(groupData);
//            }
//            holder.multiGroupHistogramView.setDataList(groupDataList);
//            int[] color1 = new int[]{Color.parseColor("#ff9f9fac"), Color.parseColor("#ff9f9fac")};
//            holder.multiGroupHistogramView.setHistogramColor(color1);
//
//            holder.multiGroupHistogramView.setOnItemClickListener(new MultiGroupHistogramView.OnItemClickListener() {
//                @Override
//                public void onItemClick(int posisiton) {
//                    Log.e("xhbxhbxhb" , "posisiton--->  " + posisiton);
//                }
//            });

        }

    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return VIEW_HEADER;
        } else if (mBean.getList().size() > 0 && position <= mBean.getList().size()) {
            return VIEW_NORMAL;
        } else {
            if (mBean.getList().size() == 0 && position == 1) {
                return VIEW_NODATA;
            } else {
                return VIEW_FOOTER;
            }
        }
    }

    @Override
    public int getItemCount() {
        return mBean.getList().size() == 0 ? 3 : mBean.getList().size()+2;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public int viewType;

//        MultiGroupHistogramView multiGroupHistogramView;
RecyclerView multiGroupHistogramView;

        public ViewHolder(View itemView, int viewType) {
            super(itemView);
            this.viewType = viewType;
            if (viewType == VIEW_HEADER) {
//                multiGroupHistogramView = (MultiGroupHistogramView)itemView.findViewById(R.id.lcl_ac_bill_loan_chart);
                multiGroupHistogramView = (RecyclerView)itemView.findViewById(R.id.rc_chart_view);
            }
        }
    }

}
