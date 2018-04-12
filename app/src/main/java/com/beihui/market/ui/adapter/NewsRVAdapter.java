package com.beihui.market.ui.adapter;


import android.content.Context;
import android.text.TextUtils;
import android.widget.ImageView;

import com.beihui.market.R;
import com.beihui.market.entity.News;
import com.beihui.market.ui.adapter.multipleentity.MultipleNewsItem;
import com.beihui.market.util.DateFormatUtils;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.ArrayList;
import java.util.List;

public class NewsRVAdapter extends BaseMultiItemQuickAdapter<MultipleNewsItem, BaseViewHolder> {

    private static final int[] image_ids = {R.id.news_image_1, R.id.news_image_2, R.id.news_image_3};

    private List<MultipleNewsItem> dataSet = new ArrayList<>();

    public NewsRVAdapter() {
        super(null);
        addItemType(MultipleNewsItem.ITEM_TYPE_NORMAL_IMAGE, R.layout.list_item_news_normal_image);
        addItemType(MultipleNewsItem.ITEM_TYPE_BIG_IMAGE, R.layout.list_item_news_big_image);
        addItemType(MultipleNewsItem.ITEM_TYPE_LIST_IMAGE, R.layout.list_item_news_list_image);
    }

    @Override
    protected void convert(BaseViewHolder helper, MultipleNewsItem multipleItem) {
        News.Row item = multipleItem.getNews();
        if (item.getTitle() != null) {
            helper.setText(R.id.news_title, item.getTitle());
        }
        if (item.getSource() != null) {
            helper.setText(R.id.news_source, item.getSource());
        }
        helper.setText(R.id.news_publish_time, DateFormatUtils.generateNewsDate(item.getGmtCreate()));
        helper.setText(R.id.news_read_times, item.getPv() + "");

        switch (multipleItem.getItemType()) {
            case MultipleNewsItem.ITEM_TYPE_LIST_IMAGE:
                if (item.getImageList() != null && item.getImageList().size() > 0) {
                    for (int i = 0; i < item.getImageList().size() && i < image_ids.length; ++i) {
                        String url = item.getImageList().get(i);
                        if (!TextUtils.isEmpty(url)) {
                            Glide.with(helper.itemView.getContext())
                                    .load(item.getImageList().get(i))
                                    .asBitmap()
                                    .placeholder(R.drawable.image_place_holder)
                                    .into((ImageView) helper.getView(image_ids[i]));
                        } else {
                            helper.setImageResource(image_ids[i], R.drawable.image_place_holder);
                        }
                    }
                }
                break;
            default:
                //normal image item and big image item
                if (item.getImageList() != null && item.getImageList().size() > 0
                        && !TextUtils.isEmpty(item.getImageList().get(0))) {
                    Context context = helper.itemView.getContext();
                    Glide.with(context)
                            .load(item.getImageList().get(0))
                            .asBitmap()
                            .placeholder(R.drawable.image_place_holder)
                            .into((ImageView) helper.getView(R.id.news_image));
                } else {
                    helper.setImageResource(R.id.news_image, R.drawable.image_place_holder);
                }
                break;
        }
    }

    private List<MultipleNewsItem> buildMultipleItemWithNews(List<News.Row> list) {
        if (list != null && list.size() > 0) {
            List<MultipleNewsItem> news = new ArrayList<>();
            for (News.Row item : list) {
                news.add(new MultipleNewsItem(item));
            }
            return news;
        }
        return null;
    }

    public void notifyNewsDataSetChanged(List<News.Row> list) {
        dataSet.clear();
        List<MultipleNewsItem> itemList = buildMultipleItemWithNews(list);
        if (itemList != null && itemList.size() > 0) {
            dataSet.addAll(itemList);
        }
        setNewData(dataSet);
        disableLoadMoreIfNotFullPage();
    }
}
