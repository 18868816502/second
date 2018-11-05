package com.beiwo.klyjaz.social.adapter;

import android.app.Activity;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;

import com.beiwo.klyjaz.R;
import com.beiwo.klyjaz.social.bean.CommentReplyBean;
import com.beiwo.klyjaz.ui.adapter.ArticleCommentAdapter;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;


/**
 * @author chenguoguo
 * @name loanmarket_social
 * @class name：com.beiwo.klyjaz.social.adapter
 * @descripe
 * @time 2018/10/29 14:06
 */
public class ForumDetailAdapter extends BaseQuickAdapter<CommentReplyBean,BaseViewHolder> {

    public ForumDetailAdapter() {
        super(R.layout.item_article_detail_comment);
    }

    @Override
    protected void convert(BaseViewHolder helper, CommentReplyBean item) {
        Glide.with(mContext).load(item.getUserHeadUrl())
                .placeholder(R.drawable.mine_icon_head)
                .into((ImageView) helper.getView(R.id.iv_commentator_avatar));
        helper.setText(R.id.tv_commentator_name,item.getUserName())
                .setText(R.id.tv_comment_content,item.getContent())
                .setText(R.id.tv_comment_time,item.getGmtCreate());
        helper.addOnClickListener(R.id.iv_commentator_avatar)
                .addOnClickListener(R.id.iv_article_comment)
                .addOnClickListener(R.id.tv_comment_delete);
        bindChildRecycler(helper,item);
    }

    private void bindChildRecycler(BaseViewHolder helper, CommentReplyBean item) {
        ArticleCommentAdapter childAdapter = new ArticleCommentAdapter((Activity) mContext);
        RecyclerView childRecycler = helper.getView(R.id.item_recycler);
        childRecycler.setLayoutManager(new LinearLayoutManager(mContext,LinearLayoutManager.VERTICAL,true));
        childRecycler.setAdapter(childAdapter);
        childAdapter.setDatas(item.getReplyDtoList(),item.getId());
    }
}
