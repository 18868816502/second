package com.beiwo.klyjaz.ui.adapter.social;

import android.content.Context;

import com.beiwo.klyjaz.R;
import com.beiwo.klyjaz.social.bean.SocialTopicBean;
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
public class SocialAttentionAdapter extends BaseQuickAdapter<SocialTopicBean,BaseViewHolder> {

    private List<SocialTopicBean> dataSet;
    private Context mContext;

    public SocialAttentionAdapter(Context mContext) {
        super(R.layout.item_personal_center_article_content);
        this.mContext = mContext;
        dataSet = new ArrayList<>();
    }

    @Override
    protected void convert(BaseViewHolder helper, SocialTopicBean item) {
//        helper.setText(R.id.tv_author_name,item.getName())
//                .setText(R.id.tv_author_publish_time,item.getTime())
//                .setText(R.id.tv_article_content,item.getTitle())
//                .setText(R.id.tv_article_descripe,item.getContent())
//                .setText(R.id.tv_praise,item.getPariseNum())
//                .setText(R.id.tv_comment_num,item.getCommentNum());
    }

    public void notifySocialTopicChanged(List<SocialTopicBean> list) {
        dataSet.clear();
        if (list != null && list.size() > 0) {
            dataSet.addAll(list);
        }
        setNewData(dataSet);
    }
}
