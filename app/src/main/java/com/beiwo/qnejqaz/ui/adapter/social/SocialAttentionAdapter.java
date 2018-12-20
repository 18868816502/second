package com.beiwo.qnejqaz.ui.adapter.social;

import android.content.Context;

import com.beiwo.qnejqaz.R;
import com.beiwo.qnejqaz.social.bean.SocialTopicBean;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * @author chenguoguo
 * @name loanmarket
 * @class name：com.beihui.market.ui.adapter.social
 * @descripe
 * @time 2018/9/19 10:10
 */
public class SocialAttentionAdapter extends BaseQuickAdapter<SocialTopicBean, BaseViewHolder> {

    private List<SocialTopicBean> dataSet;

    public SocialAttentionAdapter(Context mContext) {
        super(R.layout.item_personal_center_article_content);
        this.mContext = mContext;
        dataSet = new ArrayList<>();
    }

    @Override
    protected void convert(BaseViewHolder helper, SocialTopicBean item) {
    }

    public void notifySocialTopicChanged(List<SocialTopicBean> list) {
        dataSet.clear();
        if (list != null && list.size() > 0) {
            dataSet.addAll(list);
        }
        setNewData(dataSet);
    }
}