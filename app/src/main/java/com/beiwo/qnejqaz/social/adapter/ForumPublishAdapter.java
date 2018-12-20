package com.beiwo.qnejqaz.social.adapter;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.beiwo.qnejqaz.R;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * @author chenguoguo
 * @name loanmarket_social
 * @class name：com.beiwo.klyjaz.social.adapter
 * @descripe
 * @time 2018/11/13 15:57
 */
public class ForumPublishAdapter extends BaseQuickAdapter<String,BaseViewHolder> {

//    private List<String> datas;

    public ForumPublishAdapter() {
        super(R.layout.item_community_publish_head_photo);
//        this.datas = new ArrayList<>();
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
        Glide.with(mContext).load(item).into((ImageView) helper.getView(R.id.iv_photo));
        helper.addOnClickListener(R.id.iv_delete);
    }

//    public void setPicList(List<String> urls){
//        if(urls != null){
//            this.datas.addAll(urls);
//        }
//        setNewData(datas);
//    }
}
