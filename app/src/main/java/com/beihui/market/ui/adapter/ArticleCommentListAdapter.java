package com.beihui.market.ui.adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.beihui.market.R;
import com.beihui.market.constant.ConstantTag;
import com.beihui.market.ui.listeners.OnViewClickListener;
import com.beihui.market.util.ToastUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author chenguoguo
 * @name loanmarket
 * @class name：com.beihui.market.ui.adapter
 * @class describe
 * @time 2018/9/12 19:27
 */
public class ArticleCommentListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;

    public ArticleCommentListAdapter(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View commentView = LayoutInflater.from(mContext).inflate(R.layout.item_article_detail_comment, parent, false);
        return new CommmentViewHolder(commentView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        CommmentViewHolder commmentViewHolder = (CommmentViewHolder) holder;
        commmentViewHolder.tvCommentPraise.setTag(position);
        commmentViewHolder.ivArticleComment.setTag(position);
    }

    @Override
    public int getItemCount() {
        return 6;
    }


    class CommmentViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.iv_commentator_avatar)
        ImageView ivCommentatorAcatar;
        @BindView(R.id.tv_commentator_name)
        TextView tvCommentatorName;
        @BindView(R.id.tv_comment_content)
        TextView tvCommentContent;
        @BindView(R.id.tv_comment_time)
        TextView tvCommentTime;
        @BindView(R.id.tv_comment_praise)
        TextView tvCommentPraise;
        @BindView(R.id.iv_article_comment)
        ImageView ivArticleComment;
        @BindView(R.id.item_recycler)
        RecyclerView itemRecyclerView;

        CommmentViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            LinearLayoutManager manager = new LinearLayoutManager(mContext);
            manager.setOrientation(LinearLayoutManager.VERTICAL);
            ArticleCommentAdapter adapter = new ArticleCommentAdapter(mContext);
            itemRecyclerView.setLayoutManager(manager);
            itemRecyclerView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
            adapter.setOnViewClickListener(new OnViewClickListener() {
                @Override
                public void onViewClick(View view, int type,int position) {
                    listener.onViewClick(view,type,position);
                }
            });
            setOnClick(tvCommentPraise,ivArticleComment);
        }
    }

    private void setOnClick(View... views) {
        for (View view : views) {
            view.setOnClickListener(new OnClickListener());
        }
    }

    private OnViewClickListener listener;

    public void setOnViewClickListener(OnViewClickListener listener) {
        this.listener = listener;
    }

    class OnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                //点赞
                case R.id.tv_comment_praise:
//                    ToastUtil.toast("点赞第"+ v.getTag()+"条");
                    listener.onViewClick(v, ConstantTag.TAG_PRAISE_COMMENT, (Integer) v.getTag());
                    break;
                //评论
                case R.id.iv_article_comment:
//                    ToastUtil.toast("评论第"+ v.getTag()+"条");
                    listener.onViewClick(v,ConstantTag.TAG_REPLY_COMMENT, (Integer) v.getTag());
                    break;
                default:
                    break;
            }
        }
    }
}
