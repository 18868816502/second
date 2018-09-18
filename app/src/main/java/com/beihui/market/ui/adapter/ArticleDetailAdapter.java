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
import com.beihui.market.ui.activity.PersonalCenterActivity;
import com.beihui.market.util.ToastUtil;
import com.beihui.market.view.CircleImageView;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bingoogolapple.bgabanner.BGABanner;
import cn.bingoogolapple.bgabanner.BGALocalImageSize;

/**
 * @author chenguoguo
 * @name loanmarket
 * @class name：com.beihui.market.ui.adapter
 * @class describe
 * @time 2018/9/12 19:27
 */
public class ArticleDetailAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private static final int HEAD = 0;
    private static final int COMMENT = 1;
    private HeadViewHolder headViewHolder;
    private CommmentViewHolder commmentViewHolder;

    public ArticleDetailAdapter(Context mContext) {
        this.mContext = mContext;
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
            default:
                break;

        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(position == 0){
            headViewHolder = (HeadViewHolder) holder;
        }else{
            commmentViewHolder = (CommmentViewHolder) holder;
        }
    }

    @Override
    public int getItemCount() {
        return 5;
    }

    @Override
    public int getItemViewType(int position) {
        if(position == 0){
            return  HEAD;
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
        @BindView(R.id.tv_article_praise)
        TextView tvPraise;
        @BindView(R.id.tv_comment_num)
        TextView tvCommentNum;
        @BindView(R.id.tv_comment)
        TextView tvComment;

        public HeadViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
            BGALocalImageSize localImageSize = new BGALocalImageSize(720, 1280, 320, 640);
            bgaBanner.setData(localImageSize, ImageView.ScaleType.CENTER_CROP,
                    R.drawable.x_account_info_header_bg,
                    R.drawable.x_account_info_header_bg,
                    R.drawable.x_account_info_header_bg);
            setOnClick(ivAuthorAvatar,tvAttention,tvPraise,tvComment);
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
        @BindView(R.id.tv_article_praise)
        TextView tvArticlePraise;
        @BindView(R.id.iv_article_comment)
        ImageView ivArticleComment;
        @BindView(R.id.item_recycler)
        RecyclerView itemRecyclerView;

        public CommmentViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
            LinearLayoutManager manager = new LinearLayoutManager(mContext);
            manager.setOrientation(LinearLayoutManager.VERTICAL);
            ArticleCommentAdapter adapter = new ArticleCommentAdapter(mContext);
            itemRecyclerView.setLayoutManager(manager);
            itemRecyclerView.setAdapter(adapter);
            adapter.notifyDataSetChanged();

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

    public interface OnViewClickListener{

        /**
         * 关注和点赞点击事件
         * @param view 点击的控件
         * @param type 类型，用于判断点击的是哪个控件
         */
        void onViewClick(TextView view,int type);

    }

    class OnClickListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.iv_commentator_avatar:
                    mContext.startActivity(new Intent(mContext,PersonalCenterActivity.class));
                    break;
                case R.id.tv_article_attention:
                    listener.onViewClick(headViewHolder.tvAttention,0);
                    break;
                case R.id.tv_article_praise:
                    listener.onViewClick(headViewHolder.tvAttention,1);
                    break;
                case R.id.tv_comment:
                    listener.onViewClick(headViewHolder.tvComment,2);
                    break;
                default:
                    break;
            }
        }
    }
}
