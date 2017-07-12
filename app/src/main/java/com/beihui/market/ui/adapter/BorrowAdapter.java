package com.beihui.market.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.beihui.market.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by longhy on 2016/5/3.
 */
public class BorrowAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private static final int TYPE_HEADER = 0, TYPE_ITEM = 1, TYPE_FOOT = 2;

    private Context context;
    private List<String> list = new ArrayList<>();

    public BorrowAdapter(Context context) {
        this.context = context;
    }


    public void onReference(List<String> list1) {
        if (list1 != null) {
            list.clear();
            list.addAll(list1);
            notifyDataSetChanged();
        }
    }

    public void addOnReference(List<String> list1) {
        if (list1 != null) {
            list.addAll(list1);
            notifyDataSetChanged();
        }
    }


    @Override
    public int getItemViewType(int position) {
        int type;
        if (position == getItemCount() - 1) {
            type = TYPE_FOOT;
        } else {
            type = TYPE_ITEM;
        }
        return type;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case TYPE_ITEM:
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_borrow, parent, false);
                ViewHolder vh = new ViewHolder(view);
                return vh;
            case TYPE_FOOT:
                View view1 = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_main1_bottom, parent, false);
                ViewBottomHolder vh1 = new ViewBottomHolder(view1);
                return vh1;
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ViewHolder) {
            ViewHolder holder1 = (ViewHolder) holder;


        } else if (holder instanceof ViewBottomHolder) {
            ViewBottomHolder holder2 = (ViewBottomHolder) holder;

        }
    }

    @Override
    public int getItemCount() {
        return 10;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

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

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }


    public class ViewBottomHolder extends RecyclerView.ViewHolder {

        public ViewBottomHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);

        }
    }

}
