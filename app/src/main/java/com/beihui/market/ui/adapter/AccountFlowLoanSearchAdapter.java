package com.beihui.market.ui.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.beihui.market.R;
import com.beihui.market.entity.AccountFlowIconBean;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2018/6/21.
 */

public class AccountFlowLoanSearchAdapter extends RecyclerView.Adapter<AccountFlowLoanSearchAdapter.ViewHolder> {

    public static final int VIEW_NORMAL = R.layout.x_dialog_account_flow_loan_custom;
    public static final int VIEW_IMAGE_VIEW = R.layout.x_dialog_account_flow_loan_custom_root;

    public Activity mActivity;

    //数据源
    public List<AccountFlowIconBean> list = new ArrayList<>();

    public AccountFlowLoanSearchAdapter(Activity mActivity) {
        this.mActivity = mActivity;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder viewHolder = new ViewHolder(LayoutInflater.from(mActivity).inflate(viewType, parent, false), viewType);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (holder.viewType == VIEW_IMAGE_VIEW) {
            position = position - 1;
            Glide.with(mActivity).load(list.get(position).logo).into(holder.imageView);

            final int finalPosition = position;
            holder.imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onItemClickListener != null) {
                        onItemClickListener.onItemClick(list.get(finalPosition));
                    }
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(AccountFlowIconBean bean);
    }

    public OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public int getItemCount() {
        return list.size()+1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return VIEW_NORMAL;
        } else {
            return VIEW_IMAGE_VIEW;
        }
    }

    public void setData(List<AccountFlowIconBean> data) {
        if (list.size() > 0) {
            list.clear();
        }
        this.list.addAll(data);
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public int viewType;

        public ImageView imageView;

        public ViewHolder(View itemView, int viewType) {
            super(itemView);
            this.viewType = viewType;
            if (viewType == VIEW_IMAGE_VIEW) {
                imageView = (ImageView)itemView.findViewById(R.id.iv_item_image_view);
            }
        }
    }

}
