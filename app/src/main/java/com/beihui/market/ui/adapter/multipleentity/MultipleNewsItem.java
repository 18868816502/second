package com.beihui.market.ui.adapter.multipleentity;


import com.beihui.market.entity.News;
import com.chad.library.adapter.base.entity.MultiItemEntity;

public class MultipleNewsItem implements MultiItemEntity {

    public static final int ITEM_TYPE_NORMAL_IMAGE = 1;
    public static final int ITEM_TYPE_BIG_IMAGE = 2;
    public static final int ITEM_TYPE_LIST_IMAGE = 3;

    private int itemType;
    private News.Row news;

    public MultipleNewsItem(News.Row news) {
        this.news = news;
        switch (news.getShowType()) {
            case 0:
                itemType = ITEM_TYPE_NORMAL_IMAGE;
                break;
            case 1:
                itemType = ITEM_TYPE_BIG_IMAGE;
                break;
            case 2:
                itemType = ITEM_TYPE_LIST_IMAGE;
                break;
            default:
                itemType = ITEM_TYPE_NORMAL_IMAGE;
        }
    }

    @Override
    public int getItemType() {
        return itemType;
    }

    public News.Row getNews() {
        return news;
    }
}
