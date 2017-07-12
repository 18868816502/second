package com.beihui.market.ui.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.beihui.market.R;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class NewsRVAdapter extends BaseQuickAdapter<String, NewsRVAdapter.NewsViewHolder> {

    public NewsRVAdapter() {
        super(R.layout.rv_item_news);
    }

    @Override
    protected void convert(NewsViewHolder helper, String item) {
        Context context = helper.itemView.getContext();
        Glide.with(context).load("").placeholder(R.drawable.animaition01).into(helper.newsIv);
        helper.newsTitleTv.setText("NewsTitle");
        helper.newsSourceTv.setText("NewsSource");
        helper.newsPublishTimeTv.setText("NewsPublishTime");
        helper.newsReadTimesTv.setText("NewsReadTimes");
    }

    public void notifyNewsSetChanged(List<String> list) {
        setNewData(list);
    }

    class NewsViewHolder extends BaseViewHolder {
        @BindView(R.id.news_image)
        ImageView newsIv;
        @BindView(R.id.news_title)
        TextView newsTitleTv;
        @BindView(R.id.news_source)
        TextView newsSourceTv;
        @BindView(R.id.news_publish_time)
        TextView newsPublishTimeTv;
        @BindView(R.id.news_read_times)
        TextView newsReadTimesTv;

        public NewsViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
