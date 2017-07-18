package com.beihui.market.ui.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.beihui.market.R;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class NewsRVAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

    public NewsRVAdapter() {
        super(R.layout.rv_item_news);
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return super.onCreateViewHolder(parent, viewType);
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
        Context context = helper.itemView.getContext();
        Glide.with(context).load("").placeholder(R.mipmap.borrow_student_icon).into((ImageView) helper.getView(R.id.news_image));
        helper.setText(R.id.news_title, "NewsTitle");
        helper.setText(R.id.news_source, "NewsSource");
        helper.setText(R.id.news_publish_time, "NewsPublishTime");
        helper.setText(R.id.news_read_times, "NewsReadTimes");
    }

    public void notifyNewsSetChanged(List<String> list) {
        setNewData(list);
    }
}
