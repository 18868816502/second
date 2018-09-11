package com.beihui.market.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.beihui.market.R;
import com.beihui.market.util.ToastUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @name loanmarket
 * @class name：com.beihui.market.ui.adapter
 * @class describe 个人中心适配器
 * @anthor chenguoguo
 * @time 2018/9/11 14:38
 */
public class PersonalCenterAdapter extends RecyclerView.Adapter {

    private Context mContext;
    /**
     * 头部
     */
    private static final int HEAD = 1;
    /**
     * 列表项
     */
    private static final int CONTENT = 2;

    /**
     * 头部ViewHolder
     */
    private HeadViewHolder headViewHolder;
    /**
     * 列表ViewHolder
     */
    private ContentViewHolder contentViewHolder;

    public PersonalCenterAdapter(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType){
            case HEAD:
                View headView = LayoutInflater.from(mContext).inflate(R.layout.item_personal_center_head,parent,false);
                return new HeadViewHolder(headView);
            case CONTENT:
                View contentView = LayoutInflater.from(mContext).inflate(R.layout.item_personal_center_article_content,parent,false);
                return new ContentViewHolder(contentView);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(position == 0){
            headViewHolder = (HeadViewHolder) holder;

        }else{
            contentViewHolder = (ContentViewHolder) holder;

        }
    }

    @Override
    public int getItemCount() {
        return 5;
    }

    @Override
    public int getItemViewType(int position) {
        if(position == 0){
            return HEAD;
        }else{
            return CONTENT;
        }
    }

    class HeadViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.avatar)
        ImageView ivAvatar;
        @BindView(R.id.tv_name)
        TextView tvName;
        @BindView(R.id.tv_sex)
        TextView tvSex;
        @BindView(R.id.ll_edit_container)
        LinearLayout llEditContainer;
        @BindView(R.id.tv_produce)
        TextView tvProduce;
        @BindView(R.id.ll_publish_container)
        LinearLayout llPublishContainer;
        @BindView(R.id.ll_attention_container)
        LinearLayout llAttentionContainer;
        @BindView(R.id.ll_fans_container)
        LinearLayout llFansContainer;
        @BindView(R.id.ll_praise_container)
        LinearLayout llPraiseContainer;
        @BindView(R.id.iv_more)
        ImageView ivMore;

        HeadViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
            setOnClick(ivAvatar,llEditContainer,llPraiseContainer,llAttentionContainer,llFansContainer,llPraiseContainer,ivMore);
        }
    }

    private void setOnClick(View ...views){
        for (View view: views) {
            view.setOnClickListener(new OnClickListener());
        }
    }

    class ContentViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.iv_author_avatar)
        ImageView ivAuthorAvatar;
        @BindView(R.id.tv_author_name)
        TextView tvAuthorName;
        @BindView(R.id.tv_author_publish_time)
        TextView tvPublishTime;
        @BindView( R.id.tv_article_content)
        TextView tvArticleContent;
        @BindView(R.id.tv_article_descripe)
        TextView tvArticleDescripe;
        @BindView(R.id.ll_article_praise_container)
        LinearLayout llArticlePraiseContainer;
        @BindView(R.id.iv_priase)
        ImageView ivPraise;
        @BindView(R.id.tv_praise)
        TextView tvPraise;
        @BindView(R.id.ll_article_comment_container)
        LinearLayout llCommentContainer;
        @BindView(R.id.iv_comment)
        ImageView ivComment;
        @BindView(R.id.tv_comment_num)
        TextView tvCommentNum;


        ContentViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
            setOnClick(ivAuthorAvatar,llArticlePraiseContainer,llCommentContainer);
        }
    }

    class OnClickListener implements View.OnClickListener{

        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.avatar:
                    ToastUtil.toast("用户头像");
                    break;
                case R.id.ll_edit_container:
                    ToastUtil.toast("编辑个人资料");
                    break;
                case R.id.ll_publish_container:
                    ToastUtil.toast("发布");
                    break;
                case R.id.ll_attention_container:
                    ToastUtil.toast("关注");
                    break;
                case R.id.ll_fans_container:
                    ToastUtil.toast("粉丝");
                    break;
                case R.id.ll_praise_container:
                    ToastUtil.toast("获赞");
                    break;
                case R.id.iv_more:
                    ToastUtil.toast("我的文章更多按钮");
                    listener.onMoreClick();
                    break;


                    /*content*/
                case R.id.iv_author_avatar:
                    ToastUtil.toast("作者头像");
                    break;
                case R.id.ll_article_praise_container:
                    ToastUtil.toast("点赞");
                    break;
                case R.id.ll_article_comment_container:
                    ToastUtil.toast("评论");
                    break;
            }
        }
    }

    private OnViewClickListener listener;

    public void setOnViewClickListener(OnViewClickListener listener){
        this.listener = listener;
    }

    public interface OnViewClickListener{
        void onMoreClick();
    }
}
