package com.beihui.market.ui.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.beihui.market.R;
import com.beihui.market.entity.AccountFlowIconBean;
import com.beihui.market.ui.activity.AccountFlowTypeActivity;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2018/6/13.
 */

public class AccountFlowAdapter extends RecyclerView.Adapter<AccountFlowAdapter.ViewHolder> {

    public Activity mActivity;

    public int addNum = 0;

    private List<AccountFlowIconBean> dataSet = new ArrayList<>();

    public static final int VIEW_NORMAL = R.layout.x_item_account_flow;

    public AccountFlowAdapter(Activity mActivity, int addNum) {
        this.mActivity = mActivity;
        this.addNum = addNum;
    }

    @Override
    public AccountFlowAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder viewHolder = new ViewHolder(LayoutInflater.from(mActivity).inflate(VIEW_NORMAL, parent, false));
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        if (position < dataSet.size()) {
            AccountFlowIconBean item = dataSet.get(position);
            Glide.with(mActivity)
                    .load(item.logo)
                    .asBitmap()
                    .into(holder.mIcon);

            if (!TextUtils.isEmpty(item.iconName) && addNum > 0) {
                holder.mName.setText(item.iconName);
                holder.mName.setVisibility(View.VISIBLE);
            } else {
                holder.mName.setVisibility(View.GONE);
            }
        } else {
            holder.mIcon.setImageDrawable(mActivity.getResources().getDrawable(R.drawable.x_add_account_flow_more_type));
            holder.mName.setText("手动添加");
        }


        holder.mRoot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ("手动添加".equals(holder.mName.getText().toString())) {
                    mActivity.startActivity(new Intent(mActivity, AccountFlowTypeActivity.class));
                }
                if (onItemClickListener != null && (position < dataSet.size())) {
                    onItemClickListener.onItemClick(dataSet.get(position));
                }
            }
        });
    }

    public void notifyDebtChannelChanged(List<AccountFlowIconBean> list) {
        dataSet.clear();
        if (list != null && list.size() > 0) {
            dataSet.addAll(list);
        }
        notifyDataSetChanged();
    }

    public interface OnItemClickListener{
        void onItemClick(AccountFlowIconBean bean);
    }

    public OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public int getItemCount() {
        return dataSet.size() + addNum;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public LinearLayout mRoot;
        public ImageView mIcon;
        public TextView mName;

        public ViewHolder(View itemView) {
            super(itemView);
            mRoot = (LinearLayout)itemView.findViewById(R.id.ll_item_account_flow_root);
            mIcon = (ImageView)itemView.findViewById(R.id.iv_item_account_flow_icon);
            mName = (TextView)itemView.findViewById(R.id.tv_item_account_flow_name);
        }
    }

}
