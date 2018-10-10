package com.beihui.market.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.beihui.market.R;
import com.beihui.market.constant.ConstantTag;
import com.beihui.market.social.bean.CommentReplyBean;
import com.beihui.market.social.bean.SocialTopicBean;
import com.beihui.market.ui.activity.PersonalCenterActivity;
import com.beihui.market.ui.listeners.OnViewClickListener;
import com.beihui.market.util.ToastUtil;
import com.beihui.market.view.CircleImageView;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bingoogolapple.bgabanner.BGABanner;
import cn.bingoogolapple.bgabanner.BGALocalImageSize;

/**
 * @author chenguoguo
 * @name loanmarket
 * @class name：com.beihui.market.ui.adapter
 * @class describe 文章详情页面
 * @time 2018/9/12 19:27
 */
public class ArticleDetailAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private static final int HEAD = 0;
    private static final int COMMENT = 1;
    private static final int FOOT = 2;
    private List<CommentReplyBean> datas;
    private SocialTopicBean.ForumBean forumBean;

    public ArticleDetailAdapter(Context mContext) {
        this.mContext = mContext;
        datas = new ArrayList<>();
    }

    public void setDatas(List<CommentReplyBean> datas, SocialTopicBean.ForumBean forumBean){
        this.datas.clear();
        this.datas.addAll(datas);
        this.forumBean = forumBean;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType){
            case HEAD:
                View headView = LayoutInflater.from(mContext).inflate(R.layout.item_article_detail_head,parent,false);
                return new HeadViewHolder(headView);
            case COMMENT:
                View commentView = LayoutInflater.from(mContext).inflate(R.layout.item_article_detail_comment,parent,false);
                return new CommmentViewHolder(commentView);
            case FOOT:
                View footView = LayoutInflater.from(mContext).inflate(R.layout.item_article_detail_foot,parent,false);
                return new FootViewHolder(footView);
            default:
                break;

        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(position == 0){
//            if(forumBean == null){
//                return;
//            }
            HeadViewHolder headViewHolder = (HeadViewHolder) holder;
            headViewHolder.tvArticleTitle.setText(forumBean.getTitle());
            Glide.with(mContext).load(forumBean.getUserHeadUrl()).into(headViewHolder.ivAuthorAvatar);
            headViewHolder.tvAuthorName.setText(forumBean.getUserName());
            headViewHolder.tvAuthorTime.setText(forumBean.getGmtCreate());
            headViewHolder.bgaBanner.setData(forumBean.getPicUrl(),null);
            headViewHolder.tvArticleContent.setText(forumBean.getContent());
            headViewHolder.tvCommentNum.setText(String.valueOf(datas.size()+""));
        }else if(position == getItemCount() - 1){

        }else{
            CommmentViewHolder commmentViewHolder = (CommmentViewHolder) holder;
            commmentViewHolder.tvCommentPraise.setTag(R.id.tag_praise,position-1);
            commmentViewHolder.ivArticleComment.setTag(R.id.tag_comment,position-1);

            Glide.with(mContext).load(datas.get(position-1).getUserHeadUrl()).into(commmentViewHolder.ivCommentatorAcatar);
            commmentViewHolder.tvCommentatorName.setText(datas.get(position-1).getUserName());
            commmentViewHolder.tvCommentContent.setText(datas.get(position-1).getContent());
            commmentViewHolder.tvCommentTime.setText(String.valueOf(datas.get(position-1).getGmtCreate()+""));
            commmentViewHolder.commentAdapter.setDatas(datas.get(position-1).getReplyDtoList());
        }
    }

    @Override
    public int getItemCount() {
        return datas.size() + 2;
    }

    @Override
    public int getItemViewType(int position) {
        if(position == 0){
            return  HEAD;
        }else if(position == getItemCount() - 1){
            return FOOT;
        }else{
            return COMMENT;
        }
    }

    class HeadViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_article_title)
        TextView tvArticleTitle;
        @BindView(R.id.iv_author_avatar)
        CircleImageView ivAuthorAvatar;
        @BindView(R.id.tv_author_name)
        TextView tvAuthorName;
        @BindView(R.id.tv_author_time)
        TextView tvAuthorTime;
        @BindView(R.id.tv_article_attention)
        TextView tvAttention;
        @BindView(R.id.banner)
        BGABanner bgaBanner;
        @BindView(R.id.tv_article_content)
        TextView tvArticleContent;
        @BindView(R.id.tv_article_praise)
        TextView tvPraise;
        @BindView(R.id.tv_comment_num)
        TextView tvCommentNum;
        @BindView(R.id.tv_comment)
        TextView tvComment;

        HeadViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
//            BGALocalImageSize localImageSize = new BGALocalImageSize(720, 1280, 320, 640);
//            bgaBanner.setData(localImageSize, ImageView.ScaleType.CENTER_CROP,
//                    R.drawable.x_account_info_header_bg,
//                    R.drawable.x_account_info_header_bg,
//                    R.drawable.x_account_info_header_bg);
//            setOnClick(ivAuthorAvatar,tvAttention,tvPraise,tvComment);
            tvArticleTitle.setFocusable(true);
        }
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
        ArticleCommentAdapter commentAdapter;

        CommmentViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
            LinearLayoutManager manager = new LinearLayoutManager(mContext);
            manager.setOrientation(LinearLayoutManager.VERTICAL);
            commentAdapter = new ArticleCommentAdapter(mContext);
            itemRecyclerView.setLayoutManager(manager);
            itemRecyclerView.setAdapter(commentAdapter);
            commentAdapter.notifyDataSetChanged();
            commentAdapter.setOnViewClickListener(new OnViewClickListener() {
                @Override
                public void onViewClick(View view, int type) {
                    listener.onViewClick(view,type);
                    switch (type){
                        //子评论点赞
                        case ConstantTag.TAG_CHILD_PARISE_COMMENT:
                            break;
                        //子评论回复
                        case ConstantTag.TAG_CHILD_REPLY_COMMENT:

                            break;
                        default:
                            break;
                    }
                }
            });
            setOnClick(tvCommentPraise,ivArticleComment);
        }
    }

    class FootViewHolder extends RecyclerView.ViewHolder {


        FootViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
            itemView.setOnClickListener(new OnClickListener());
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
                case R.id.iv_commentator_avatar:
                    mContext.startActivity(new Intent(mContext,PersonalCenterActivity.class));
                    break;
                case R.id.tv_article_attention:
                    listener.onViewClick(v,ConstantTag.TAG_ATTENTION);
                    break;
                case R.id.tv_article_praise:
                    listener.onViewClick(v,ConstantTag.TAG_PARISE_ARTICLE);
                    break;
                //追加文章评论
                case R.id.tv_comment:
                    listener.onViewClick(v,ConstantTag.TAG_COMMENT_ARTICLE);
                    break;

                //评论点赞
                case R.id.tv_comment_praise:
                    int position = (int) v.getTag(R.id.tag_praise);
                    ToastUtil.toast("点赞第"+(position + 1) + "条");
                    listener.onViewClick(v,ConstantTag.TAG_PRAISE_COMMENT);
                    break;
                //评论回复
                case R.id.iv_article_comment:
                    int comPosition = (int) v.getTag(R.id.tag_comment);
                    ToastUtil.toast("回复第"+(comPosition + 1) + "条");
                    listener.onViewClick(v, ConstantTag.TAG_REPLY_COMMENT);
                    break;

                case R.id.foot:
                    listener.onViewClick(v, ConstantTag.TAG_COMMENT_MORE);
                    break;
                default:
                    break;
            }
        }
    }
}
