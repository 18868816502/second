package com.beiwo.klyjaz.goods.adapter;

import android.app.Activity;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.beiwo.klyjaz.R;
import com.beiwo.klyjaz.entity.Goods;
import com.beiwo.klyjaz.entity.Product;

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
 * @date: 2018/12/11
 */
public class LoanGoodsAdapter extends RecyclerView.Adapter<LoanGoodsAdapter.ViewHolder> {
    private static final int TYPE_TODAY_RECOM = R.layout.layout_goods_today_recom;//0
    private static final int TYPE_TOP_HOT = R.layout.layout_goods_top_hot;//1
    private static final int TYPE_GOOD_GOODS = R.layout.layout_good_goods;//2

    @Override
    public int getItemCount() {
        return 3;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) return TYPE_TODAY_RECOM;
        else if (position == 1) return TYPE_TOP_HOT;
        else if (position == 2) return TYPE_GOOD_GOODS;
        else return -1;
    }

    private Activity context;
    private List<Product> todayRecomData = new ArrayList<>();
    private List<Goods> hotTopData = new ArrayList<>();
    private List<Goods> goodsData = new ArrayList<>();
    private TodayRecomAdapter todayRecomAdapter = new TodayRecomAdapter();
    private HotTopAdapter hotTopAdapter = new HotTopAdapter();
    private GoodsItemAdapter goodsItemAdapter = new GoodsItemAdapter();

    public void setTodayRecom(List<Product> data) {
        todayRecomData = data;
        notifyItemChanged(0);
    }

    public void setHotTop(List<Goods> data) {
        hotTopData = data;
        notifyItemChanged(1);
    }

    public void setGood(List<Goods> data) {
        goodsData = data;
        notifyItemChanged(2);
    }

    public void appendGood(List<Goods> data) {
        goodsData.addAll(data);
        notifyItemChanged(2);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = (Activity) parent.getContext();
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false), viewType);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (holder.viewType == TYPE_TODAY_RECOM) {
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
            linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
            holder.recycler_today_recom.setHasFixedSize(true);
            holder.recycler_today_recom.setItemAnimator(new DefaultItemAnimator());
            holder.recycler_today_recom.setLayoutManager(linearLayoutManager);
            holder.recycler_today_recom.setFocusableInTouchMode(false);
            holder.recycler_today_recom.setAdapter(todayRecomAdapter);
            todayRecomAdapter.setNewData(todayRecomData);
        }
        if (holder.viewType == TYPE_TOP_HOT) {

        }
        if (holder.viewType == TYPE_GOOD_GOODS) {

        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private int viewType;
        private RecyclerView recycler_today_recom;
        private RecyclerView recycler_hot_top;
        private RecyclerView recycler_goods;

        public ViewHolder(View itemView, int viewType) {
            super(itemView);
            this.viewType = viewType;
            if (viewType == TYPE_TODAY_RECOM)
                recycler_today_recom = itemView.findViewById(R.id.recycler_today_recom);
            if (viewType == TYPE_TOP_HOT)
                recycler_hot_top = itemView.findViewById(R.id.recycler_hot_top);
            if (viewType == TYPE_GOOD_GOODS)
                recycler_goods = itemView.findViewById(R.id.recycler_goods);
        }
    }
}