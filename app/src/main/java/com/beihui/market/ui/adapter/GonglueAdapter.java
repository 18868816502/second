package com.beihui.market.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.beihui.market.R;
import com.beihui.market.ui.bean.GonglueData;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by longhy on 2016/5/3.
 */
public class GonglueAdapter extends RecyclerView.Adapter<GonglueAdapter.ViewHolder> {


    private Context context;
    private List<GonglueData> list = new ArrayList<>();

    public GonglueAdapter(Context context) {
        this.context = context;
    }


    public void onReference(List<GonglueData> list1) {
        if (list1 != null) {
            list.clear();
            list.addAll(list1);
            notifyDataSetChanged();
        }
    }

    public void addOnReference(List<GonglueData> list1) {
        if (list1 != null) {
            list.addAll(list1);
            notifyDataSetChanged();
        }
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_gonglue_hor, parent, false);
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        GonglueData data = list.get(position);

        Glide.with(context).load(data.getImageUrl()).into(holder.ivGonglue);
        holder.tvGonglue.setText(data.getTitle());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {


        @BindView(R.id.iv_gonglue)
        ImageView ivGonglue;
        @BindView(R.id.tv_gonglue)
        TextView tvGonglue;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
