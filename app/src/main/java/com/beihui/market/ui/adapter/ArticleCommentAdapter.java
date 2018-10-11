package com.beihui.market.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.beihui.market.R;
import com.beihui.market.constant.ConstantTag;
import com.beihui.market.social.bean.CommentReplyBean;
import com.beihui.market.util.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author chenguoguo
 * @name loanmarket
 * @class name：com.beihui.market.ui.adapter
 * @class describe
 * @time 2018/9/13 14:56
 */
public class ArticleCommentAdapter extends RecyclerView.Adapter {

    private Context mContext;

    private List<CommentReplyBean.ReplyDtoListBean> datas;

    public ArticleCommentAdapter(Context mContext) {
        this.mContext = mContext;
        datas = new ArrayList<>();
    }

    public void setDatas(List<CommentReplyBean.ReplyDtoListBean> datas){
        this.datas.clear();
        this.datas.addAll(datas);
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View commentView = LayoutInflater.from(mContext).inflate(R.layout.item_article_detail_comment,parent,false);
        return new ViewHolder(commentView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        viewHolder.tvCommentPraise.setTag(position);
        viewHolder.ivArticleComment.setTag(position);

        viewHolder.tvCommentContent.setText(datas.get(position).getContent());
        viewHolder.tvCommentatorName.setText(datas.get(position).getUserName());
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

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

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
            itemRecyclerView.setVisibility(View.GONE);
            setOnClick(tvCommentPraise,ivArticleComment);
        }
    }

    private void setOnClick(View... views){
        for(View view:views){
            view.setOnClickListener(new OnClickListener());
        }
    }

    private ArticleDetailAdapter.OnViewClickListener listener;

    public void setOnViewClickListener(ArticleDetailAdapter.OnViewClickListener listener){
        this.listener = listener;
    }


    class OnClickListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                //评论点赞
                case R.id.tv_comment_praise:
                    int position = (int) v.getTag();
                    ToastUtil.toast("子点赞第"+(position + 1) + "条");
                    listener.onViewClick(v, ConstantTag.TAG_CHILD_PARISE_COMMENT,position);
                    break;
                //评论回复
                case R.id.iv_article_comment:
                    int comPosition = (int) v.getTag();
                    ToastUtil.toast("子回复第"+(comPosition + 1) + "条");
                    listener.onViewClick(v,ConstantTag.TAG_CHILD_REPLY_COMMENT,comPosition);
                    break;
                default:
                    break;
            }
        }
    }
}
