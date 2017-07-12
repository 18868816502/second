package com.beihui.market.ui.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.beihui.market.R;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class LoanRVAdapter extends BaseQuickAdapter<String, LoanRVAdapter.LoanViewHolder> {

    public LoanRVAdapter() {
        super(R.layout.adapter_borrow);
    }

    @Override
    protected void convert(LoanViewHolder helper, String item) {

    }

    public void notifyDataSetChanged(List<String> dataSet) {
        setNewData(dataSet);
    }

    class LoanViewHolder extends BaseViewHolder {

        @BindView(R.id.tv_name)
        TextView tvName;
        @BindView(R.id.tv_solg)
        TextView tvSolg;
        @BindView(R.id.tv_money)
        TextView tvMoney;
        @BindView(R.id.tv_lixi)
        TextView tvLixi;
        @BindView(R.id.tv_time)
        TextView tvTime;
        @BindView(R.id.tv_detail)
        TextView tvDetail;
        @BindView(R.id.iv_sing)
        ImageView ivSing;

        public LoanViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
