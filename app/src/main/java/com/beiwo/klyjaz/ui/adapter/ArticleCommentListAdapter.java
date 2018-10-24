package com.beiwo.klyjaz.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.beiwo.klyjaz.R;
import com.beiwo.klyjaz.constant.ConstantTag;
import com.beiwo.klyjaz.helper.UserHelper;
import com.beiwo.klyjaz.social.bean.CommentReplyBean;
import com.beiwo.klyjaz.ui.activity.PersonalCenterActivity;
import com.beiwo.klyjaz.ui.listeners.OnViewClickListener;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

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
    private List<CommentReplyBean> datas;


    public ArticleCommentListAdapter(Context mContext) {
        this.mContext = mContext;
        datas = new ArrayList<>();
    }

    public void setDatas(List<CommentReplyBean> list) {
        this.datas.clear();
        this.datas.addAll(list);
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View commentView = LayoutInflater.from(mContext).inflate(R.layout.item_article_detail_comment, parent, false);
        return new CommmentViewHolder(commentView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        CommmentViewHolder viewHolder = (CommmentViewHolder) holder;
        viewHolder.tvCommentPraise.setTag(position);
        viewHolder.ivArticleComment.setTag(position);
        viewHolder.itemView.setTag(position);
        viewHolder.ivCommentatorAcatar.setTag(R.id.comment_list_avatar,position);

        if(!TextUtils.isEmpty(datas.get(position).getUserHeadUrl())) {
            Glide.with(mContext).load(datas.get(position).getUserHeadUrl()).into(viewHolder.ivCommentatorAcatar);
        }
        viewHolder.tvCommentatorName.setText(datas.get(position).getUserName());
        viewHolder.tvCommentTime.setText(String.valueOf(datas.get(position).getGmtCreate()));
        viewHolder.tvCommentContent.setText(datas.get(position).getContent());
        viewHolder.tvCommentPraise.setText(String.valueOf(datas.get(position).getPraiseCount()));
        if(UserHelper.getInstance(mContext).isLogin()) {
            if (TextUtils.equals(UserHelper.getInstance(mContext).getProfile().getId(), datas.get(position).getUserId())) {
                viewHolder.tvCommentDelete.setVisibility(View.VISIBLE);
            } else {
                viewHolder.tvCommentDelete.setVisibility(View.GONE);
            }
        }else{
            viewHolder.tvCommentDelete.setVisibility(View.GONE);
        }
        if (viewHolder.adapter != null){
            viewHolder.adapter.setDatas(datas.get(position).getReplyDtoList(),datas.get(position).getId());
        }

    }

    @Override
    public int getItemCount() {
        return datas.size();
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
        @BindView(R.id.tv_comment_delete)
        TextView tvCommentDelete;
        @BindView(R.id.tv_comment_praise)
        TextView tvCommentPraise;
        @BindView(R.id.iv_article_comment)
        ImageView ivArticleComment;
        @BindView(R.id.item_recycler)
        RecyclerView itemRecyclerView;
        ArticleCommentAdapter adapter;

        CommmentViewHolder(final View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            LinearLayoutManager manager = new LinearLayoutManager(mContext);
            manager.setOrientation(LinearLayoutManager.VERTICAL);
            adapter = new ArticleCommentAdapter(mContext);
            itemRecyclerView.setLayoutManager(manager);
            itemRecyclerView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
            adapter.setOnViewClickListener(new OnViewClickListener() {
                @Override
                public void onViewClick(View view, int type, int position) {
                    view.setTag(itemView.getTag());
                    listener.onViewClick(view, type, position);
                }
            });
            setOnClick(tvCommentPraise, ivArticleComment, tvCommentDelete,ivCommentatorAcatar);
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
                case R.id.iv_commentator_avatar:
                    Intent pIntent = new Intent(mContext, PersonalCenterActivity.class);
                    pIntent.putExtra("userId",datas.get((Integer) v.getTag(R.id.comment_list_avatar)).getUserId());
                    mContext.startActivity(pIntent);
                    break;
                //点赞
                case R.id.tv_comment_praise:
                    listener.onViewClick(v, ConstantTag.TAG_PRAISE_COMMENT, (Integer) v.getTag());
                    break;
                //评论
                case R.id.iv_article_comment:
                    listener.onViewClick(v, ConstantTag.TAG_REPLY_COMMENT, (Integer) v.getTag());
                    break;
                case R.id.tv_comment_delete:
                    listener.onViewClick(v, ConstantTag.TAG_COMMENT_DELETE, (Integer) v.getTag());
                    break;
                default:
                    break;
            }
        }
    }
}
