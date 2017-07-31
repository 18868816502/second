package com.beihui.market.ui.adapter;


import android.util.Log;
import android.widget.ImageView;

import com.beihui.market.R;
import com.beihui.market.entity.News;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.ArrayList;
import java.util.List;

public class HotNewsAdapter extends BaseQuickAdapter<News.Row, BaseViewHolder> {
    private List<News.Row> dataSet;

    public HotNewsAdapter() {
        super(R.layout.rv_item_hot_news);
    }

    @Override
    protected void convert(BaseViewHolder helper, News.Row item) {
        if (item.getTitle() != null) {
            helper.setText(R.id.news_title, item.getTitle());
        }
        if (item.getImage() != null) {
            Glide.with(helper.itemView.getContext())
                    .load(item.getImage())
                    .placeholder(R.drawable.image_place_holder)
                    .into((ImageView) helper.getView(R.id.news_image));
        } else {
            helper.setImageResource(R.id.news_image, R.drawable.image_place_holder);
        }
    }

    public void notifyHotNewsChanged(List<News.Row> news) {
        if (dataSet == null) {
            dataSet = new ArrayList<>();
        }
        dataSet.clear();
        if (news != null) {
            dataSet.addAll(news);
        }
        setNewData(dataSet);
    }
}
