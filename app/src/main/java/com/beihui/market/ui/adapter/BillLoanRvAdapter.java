package com.beihui.market.ui.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.beihui.market.R;

/**
 * Created by admin on 2018/5/22.
 */

public class BillLoanRvAdapter extends RecyclerView.Adapter<BillLoanRvAdapter.ViewHolder> {

    public Activity mActivity;

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
    public void onBindViewHolder(ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 20;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View itemView) {
            super(itemView);
        }
    }

}
