package com.beiwo.qnejqaz.social.adapter;

import android.app.Activity;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.beiwo.qnejqaz.R;
import com.beiwo.qnejqaz.helper.UserHelper;
import com.beiwo.qnejqaz.social.bean.CommentReplyBean;
import com.beiwo.qnejqaz.social.inter.OnChildViewClickListener;
import com.beiwo.qnejqaz.ui.listeners.OnViewClickListener;
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
 * @time 2018/11/5 11:04
 */
public class ForumCommentAdapter extends BaseQuickAdapter<CommentReplyBean,BaseViewHolder> {

    private List<CommentReplyBean> datas;

    public ForumCommentAdapter() {
        super(R.layout.item_article_detail_comment);
        datas = new ArrayList<>();
    }

    @Override
    protected void convert(BaseViewHolder helper, CommentReplyBean item) {
        if (!TextUtils.isEmpty(item.getUserHeadUrl())) {
            Glide.with(mContext).load(item.getUserHeadUrl()).into((ImageView) helper.getView(R.id.iv_commentator_avatar));
        }else{
            Glide.with(mContext).load(R.drawable.mine_icon_head).into((ImageView) helper.getView(R.id.iv_commentator_avatar));

        }
        helper.setText(R.id.tv_commentator_name,item.getUserName())
                .setText(R.id.tv_comment_content,item.getContent())
                .setText(R.id.tv_comment_time,String.valueOf(item.getGmtCreate()))
                .setText(R.id.tv_comment_praise,String.valueOf(item.getPraiseCount()))
                .addOnClickListener(R.id.tv_comment_delete)
                .addOnClickListener(R.id.iv_article_comment)
                .addOnClickListener(R.id.iv_commentator_avatar);

        if (UserHelper.getInstance(mContext).isLogin()) {
            if (TextUtils.equals(UserHelper.getInstance(mContext).getProfile().getId(), item.getUserId())) {
                helper.setVisible(R.id.tv_comment_delete,true);
            } else {
                helper.setVisible(R.id.tv_comment_delete,false);
            }
        } else {
            helper.setVisible(R.id.tv_comment_delete,false);
        }

        LinearLayoutManager manager = new LinearLayoutManager(mContext);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        ForumChildCommentAdapter adapter = new ForumChildCommentAdapter((Activity) mContext);
        RecyclerView recyclerView = helper.getView(R.id.item_recycler);
        final int position = helper.getLayoutPosition() - getHeaderLayoutCount();
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
        adapter.setDatas(item.getReplyDtoList(),"");
        adapter.setOnViewClickListener(new OnViewClickListener() {
            @Override
            public void onViewClick(View view, int type, int childPosition) {
                listener.onChildViewClick(view,position,childPosition);
            }
        });
    }

    public void setDatas(List<CommentReplyBean> list){
        this.datas.clear();
        this.datas.addAll(list);
        setNewData(datas);
    }

    private OnChildViewClickListener listener;

    public void setOnChildViewClickListener(OnChildViewClickListener listener){
        this.listener = listener;
    }
}
