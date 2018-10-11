package com.beihui.market.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.beihui.market.R;
import com.beihui.market.constant.ConstantTag;
import com.beihui.market.helper.UserHelper;
import com.beihui.market.social.bean.CommentReplyBean;
import com.beihui.market.ui.listeners.OnViewClickListener;
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
    private boolean isOpen = false;

    private List<CommentReplyBean.ReplyDtoListBean> datas;
    private static final int CONTENT = 1;
    private static final int FOOT = 2;

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
        switch (viewType){
            case CONTENT:
                View commentView = LayoutInflater.from(mContext).inflate(R.layout.item_article_detail_comment,parent,false);
                return new ViewHolder(commentView);
            case FOOT:
                View footView = LayoutInflater.from(mContext).inflate(R.layout.item_article_detail_comment_foot,parent,false);
                return new FootViewHolder(footView);
                default:
                    return null;
        }
//        View commentView = LayoutInflater.from(mContext).inflate(R.layout.item_article_detail_comment,parent,false);
//        return new ViewHolder(commentView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(position == getItemCount() - 1){
            FootViewHolder viewHolder = (FootViewHolder) holder;
            if(datas.size() < 2){
                viewHolder.setVisibility(false);
            }else{
                viewHolder.setVisibility(true);
                if(isOpen){
                    viewHolder.tvOpenReply.setText("关闭全部回复");
//                    viewHolder.ivOpenReply.setBackgroundResource(R.drawable);
                }else{
                    viewHolder.tvOpenReply.setText("展开全部回复");
//                    viewHolder.ivOpenReply.setBackgroundResource(R.drawable);
                }
            }
        }else {
            ViewHolder viewHolder = (ViewHolder) holder;
            viewHolder.tvCommentPraise.setTag(position-1);
            viewHolder.ivArticleComment.setTag(position-1);
            viewHolder.tvDelete.setTag(position-1);

            viewHolder.tvCommentContent.setText(datas.get(position-1).getContent());
            viewHolder.tvCommentatorName.setText(datas.get(position-1).getUserName());
            if (TextUtils.equals(UserHelper.getInstance(mContext).getProfile().getId(), datas.get(position-1).getUserId())) {
                viewHolder.tvDelete.setVisibility(View.VISIBLE);
            } else {
                viewHolder.tvDelete.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public int getItemCount() {
        if(isOpen){
            return datas.size();
        }else{
            if(datas.size() > 1){
                return 2;
            }else{
                return 0;
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        if(position == getItemCount() -1 ){
            return FOOT;
        }else{
            return CONTENT;
        }
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
        @BindView(R.id.tv_comment_delete)
        TextView tvDelete;
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
            setOnClick(tvCommentPraise,ivArticleComment,tvDelete);
        }
    }

    class FootViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_open_reply)
        TextView tvOpenReply;
        @BindView(R.id.iv_open_reply)
        ImageView ivOpenReply;

        public FootViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
            setOnClick(itemView);
        }

        public void setVisibility(boolean isVisible) {
            RecyclerView.LayoutParams param = (RecyclerView.LayoutParams) itemView.getLayoutParams();
            if (isVisible) {
                param.height = LinearLayout.LayoutParams.WRAP_CONTENT;
                param.width = LinearLayout.LayoutParams.MATCH_PARENT;
                itemView.setVisibility(View.VISIBLE);
            } else {
                itemView.setVisibility(View.GONE);
                param.height = 0;
                param.width = 0;
            }
            itemView.setLayoutParams(param);
        }
    }

    private void setOnClick(View... views){
        for(View view:views){
            view.setOnClickListener(new OnClickListener());
        }
    }

    private OnViewClickListener listener;

    public void setOnViewClickListener(OnViewClickListener listener){
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
                case R.id.tv_comment_delete:
                    int delPosition = (int) v.getTag();
                    ToastUtil.toast("删除第"+(delPosition + 1) + "条");
                    listener.onViewClick(v, ConstantTag.TAG_CHILD_COMMENT_DELETE,delPosition);
                    break;
                case R.id.foot:
                    //展开或者回复
                    if(isOpen){
                        isOpen = false;
                    }else{
                        isOpen = true;
                    }
                    notifyDataSetChanged();
                    break;
                default:
                    break;
            }
        }
    }
}
