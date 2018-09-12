package com.beihui.market.ui.adapter;

import android.app.Activity;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.beihui.market.R;
import com.beihui.market.entity.AnalysisChartBean;
import com.beihui.market.event.BillLoanRvAdapterEvent;
import com.beihui.market.view.multiChildHistogram.SingleChartView;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2018/5/22.
 */

public class BillLoanRvAdapter extends RecyclerView.Adapter<BillLoanRvAdapter.ViewHolder> {

    public Activity mActivity;

    //数据源
    public List<AnalysisChartBean> mList = new ArrayList<>();

    //数组最大值
    public Double maxAmout = 1D;


    /**
     * 类型 2-周 3-月
     */
    public int mType = 3;

    public static final int VIEW_NORMAL = R.layout.x_item_chart_bill_loan;

    public BillLoanRvAdapter(Activity mActivity) {
        this.mActivity = mActivity;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder viewHolder = new ViewHolder(LayoutInflater.from(mActivity).inflate(VIEW_NORMAL, parent, false));
        return viewHolder;
}

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final AnalysisChartBean analysisChartBean = mList.get(position);
        //柱形图高度
        final double chartHeidht = analysisChartBean.getAmount()/maxAmout;
        Double leftHeight = null;
        Double rightHeight = null;
        if (position > 0) {
            leftHeight=mList.get(position-1).getAmount()/maxAmout;
        }
        if (mType == 2 && position <= mList.size() - 2) {
            rightHeight=mList.get(position+1).getAmount()/maxAmout;
        }
        if (mType == 3 && position <= mList.size() - 2) {
            rightHeight=mList.get(position+1).getAmount()/maxAmout;
        }



        if (mList.get(position).isSelect) {
            holder.bottomText.setTextColor(Color.parseColor("#424251"));
//            if ((mType == 2 && position < 12) || (mType == 3 && position < 6)){
//                holder.singleChartView.notifyChartColor(chartHeidht,leftHeight, rightHeight, analysisChartBean.showAmount  + "",mList.get(position).isSelect);
//            } else {
//                holder.singleChartView.notifyChartColor(chartHeidht,leftHeight, rightHeight, analysisChartBean.showAmount  + "", mList.get(position).isSelect, 0);
//            }
        } else {
            holder.bottomText.setTextColor(Color.parseColor("#B3B3B3"));
//            if ((mType == 2 && position < 12) || (mType == 3 && position < 6)){
//                holder.singleChartView.notifyChartColor(chartHeidht, leftHeight, rightHeight,analysisChartBean.showAmount + "", mList.get(position).isSelect);
//            } else {
//                holder.singleChartView.notifyChartColor(chartHeidht, leftHeight, rightHeight,analysisChartBean.showAmount + "", mList.get(position).isSelect, 0);
//            }
        }
        int line;
        if ((mType == 2 && position < 11) || (mType == 3 && position < 5)) {
            line = 0;
        } else if ((mType == 2 && position == 11) || (mType == 3 && position == 5)) {
            line = 1;
        } else {
            line = 2;
        }
        holder.singleChartView.notifyChartColor(chartHeidht, leftHeight, rightHeight, analysisChartBean.showAmount, mList.get(position).isSelect, line);

        //底部文字
        String[] split = analysisChartBean.getTime().split("-");
        String timeName = mType==2? "周":"月";
        holder.bottomText.setText(split[1]+timeName);

        holder.singleChartViewRoot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new BillLoanRvAdapterEvent(position));
                for (int i=0; i<mList.size(); i++) {
                    mList.get(i).isSelect = false;
                }
                mList.get(position).isSelect = true;
                notifyDataSetChanged();
            }
        });
    }

    //柱形图高度
    public double chartHeidht;
    //数量
    public String number;
    //底部文字
    public String bottomText;
    //选中状态
    public boolean isSelect = false;
    //颜色值
    public int chartColor;

    @Override
    public int getItemCount() {
        return mList.size();
    }

    /**
     * 刷新柱状图数据
     */
    public void notifyChartData(List<AnalysisChartBean> data, int type) {
        this.mType = type;
        if (mList.size() > 0) {
            mList.clear();
        }
        mList.addAll(data);
        int size = mList.size();
        maxAmout = 0D;
        for (int i = 0; i < size; i++) {
            Double amount = mList.get(i).getAmount();
            if (maxAmout < amount) {
                maxAmout = amount;
            }
        }
        if (maxAmout == 0D) {
          maxAmout = 1D;
        }
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public SingleChartView singleChartView;
        public FrameLayout singleChartViewRoot;
        public TextView bottomText;

        public ViewHolder(View itemView) {
            super(itemView);

            singleChartView = (SingleChartView)itemView.findViewById(R.id.scv_item_chart_bill_loan);
            singleChartViewRoot = (FrameLayout)itemView.findViewById(R.id.fl_item_chart_bill_loan);
            bottomText = (TextView)itemView.findViewById(R.id.tv_item_chart_bill_bottom_text);
        }
    }

}
