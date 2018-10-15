package com.beiwo.klyjaz.tang.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.beiwo.klyjaz.R;
import com.beiwo.klyjaz.entity.BillDetail;
import com.beiwo.klyjaz.util.FormatNumberUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * https://gitee.com/tangbuzhi
 *
 * @author: Tangbuzhi
 * @version:
 * @package:
 * @description:
 * @modify:
 * @date: 2018/8/2
 */

public class SubAdapter extends RecyclerView.Adapter<SubAdapter.ViewHolder> {

    private final static int VIEW_EMPTY = R.layout.f_layout_sub_empty;
    private final static int VIEW_NORMAL = R.layout.f_layout_sub_detail;
    private List<BillDetail> dataSet = new ArrayList<>();

    public void notify(List<BillDetail> data) {
        if (data != null && data.size() > 0) this.dataSet = data;
        else dataSet.clear();
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false), viewType);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (holder.viewType == VIEW_EMPTY) {
            //nothing
        } else if (holder.viewType == VIEW_NORMAL) {
            if (dataSet.size() == 0) return;
            BillDetail item = dataSet.get(position);
            holder.tv_sub_description.setText(item.getDiscription());
            holder.tv_sub_amount.setText(FormatNumberUtils.FormatNumberFor2(item.getAmountMoney()));
            holder.tv_sub_time.setText(item.getTransDate().substring(0, 10).replace("-", "."));
        }
    }

    @Override
    public int getItemCount() {
        if (dataSet.size() == 0) return 1;
        else return dataSet.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (dataSet.size() == 0) return VIEW_EMPTY;
        else return VIEW_NORMAL;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private int viewType;
        private TextView tv_sub_description;
        private TextView tv_sub_amount;
        private TextView tv_sub_time;

        public ViewHolder(View itemView, int viewType) {
            super(itemView);
            this.viewType = viewType;
            if (viewType == VIEW_NORMAL) {
                tv_sub_description = (TextView) itemView.findViewById(R.id.tv_sub_description);
                tv_sub_amount = (TextView) itemView.findViewById(R.id.tv_sub_amount);
                tv_sub_time = (TextView) itemView.findViewById(R.id.tv_sub_time);
            }
        }
    }
}