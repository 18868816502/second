package com.beihui.market.ui.adapter;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.beihui.market.R;
import com.beihui.market.entity.News;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class NewsRVAdapter extends BaseQuickAdapter<News.Row, BaseViewHolder> {
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("MM月dd日", Locale.CHINA);
    private List<News.Row> dataSet;

    private Date date = new Date();
    private Calendar dateCal = Calendar.getInstance(Locale.CHINA);
    private Calendar nowCal = Calendar.getInstance(Locale.CHINA);

    public NewsRVAdapter() {
        super(R.layout.rv_item_news);
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return super.onCreateViewHolder(parent, viewType);
    }

    @Override
    protected void convert(BaseViewHolder helper, News.Row item) {
        if (item.getImage() != null) {
            Context context = helper.itemView.getContext();
            Glide.with(context).load(item.getImage()).into((ImageView) helper.getView(R.id.news_image));
        }
        if (item.getTitle() != null) {
            helper.setText(R.id.news_title, item.getTitle());
        }
        if (item.getSource() != null) {
            helper.setText(R.id.news_source, item.getSource());
        }
        helper.setText(R.id.news_publish_time, generateDate(item.getGmtCreate()));
        helper.setText(R.id.news_read_times, item.getPv() + "阅读");
    }


    public void notifyNewsSetChanged(List<News.Row> list) {
        if (dataSet == null) {
            dataSet = new ArrayList<>();
        }
        dataSet.clear();
        if (list != null) {
            dataSet.addAll(list);
        }
        setNewData(dataSet);
    }

    private String generateDate(long gmt) {
        long now = System.currentTimeMillis();
        if (gmt >= now - 5 * 1000 * 60) {
            //5分钟之内
            return "刚刚";
        } else if (gmt >= now - 60 * 1000 * 60) {
            //一个小时之内
            return ((now - gmt) / (1000 * 60)) + "分钟前";
        }
        dateCal.setTimeInMillis(gmt);
        nowCal.setTimeInMillis(now);
        int dateDay = dateCal.get(Calendar.DAY_OF_YEAR);
        int nowDay = nowCal.get(Calendar.DAY_OF_YEAR);
        if (dateDay == nowDay) {
            //同一天
            return ((now - gmt) / (1000 * 60 * 60)) + "小时前";
        } else if (dateDay == nowDay - 1) {
            //隔天
            return "昨天";
        }
        date.setTime(gmt);
        return dateFormat.format(date);
    }
}
