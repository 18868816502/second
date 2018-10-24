package com.beiwo.klyjaz.ui.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.widget.ImageView;

import com.beiwo.klyjaz.R;
import com.beiwo.klyjaz.entity.EventBean;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * Copyright: dondo (C)2018
 * FileName: DeployAdapter
 * Author: jiang
 * Create on: 2018/8/30 10:10
 * Description:显示活动的列表
 */
public class DeployAdapter extends BaseQuickAdapter<EventBean, BaseViewHolder> {

    private Context context;

    public DeployAdapter(int layoutResId, @Nullable List<EventBean> data, Context context) {
        super(layoutResId, data);
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, EventBean item) {
        String title = "";
        if (item.getTitle() != null && !TextUtils.isEmpty(item.getTitle())) {
            String[] str = item.getTitle().split("#");
            helper.setText(R.id.activity_event_tv, str[0]);
            title = str[0];
            if (str.length == 2) {
                helper.setText(R.id.subTitle_tv, str[1]);
            }
        }
        Glide.with(context).load(item.getImgUrl()).into((ImageView) helper.getView(R.id.activity_event_img));
    }
}