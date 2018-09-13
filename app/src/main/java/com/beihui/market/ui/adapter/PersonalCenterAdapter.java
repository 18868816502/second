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
import com.beihui.market.entity.UserArticleBean;
import com.beihui.market.entity.UserInfoBean;
import com.beihui.market.util.ToastUtil;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @name loanmarket
 * @class name：com.beihui.market.ui.adapter
 * @class describe 个人中心适配器
 * @author chenguoguo
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

    /**
     * 用户数据
     */
    private UserInfoBean userInfo;
    /**
     * 用户文章列表
     */
    private List<UserArticleBean> mList;

    /**
     * 装载头部用户数据
     * @param userInfo 用户bean
     */
    public void setHeadData(UserInfoBean userInfo){
        this.userInfo = userInfo;
    }

    /**
     * 装载刷新的文章数据
     * @param list 用户文章列表
     */
    public void setContentData(List<UserArticleBean> list){
        mList.clear();
        mList.addAll(list);
        notifyDataSetChanged();
    }

    /**
     * 加载更多文章数据
     * @param list 文章列表
     */
    public void appendArticleData(List<UserArticleBean> list){
        mList.addAll(list);
        notifyDataSetChanged();
    }

    public PersonalCenterAdapter(Context mContext) {
        this.mContext = mContext;
        mList = new ArrayList<>();
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
                default:
                    break;
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(position == 0){
            headViewHolder = (HeadViewHolder) holder;
//            onBindHeadData(headViewHolder);
        }else{
            contentViewHolder = (ContentViewHolder) holder;
//            onBindArticleData(contentViewHolder,position-1);
        }
    }

    /**
     * 绑定头部用户数据
     * @param holder headViewHolder
     */
    private void onBindHeadData(HeadViewHolder holder) {
        if(userInfo == null){ return; }
        Glide.with(mContext).load(userInfo.getHeadPortrait()).asBitmap().into(holder.ivAvatar);
        holder.tvName.setText(userInfo.getUserName());
        holder.tvSex.setText(1 == userInfo.getSex() ? "男":"女");
        holder.tvProduce.setText(String.format("简介:",userInfo.getIntroduce()));
        holder.tvPublishNum.setText(String.valueOf(userInfo.getForumCount()));
        holder.tvAttentionNum.setText(String.valueOf(userInfo.getFollowerCount()));
        holder.tvFansNum.setText(String.valueOf(userInfo.getFansCount()));
        holder.tvPraiseNum.setText(String.valueOf(userInfo.getPraiseCount()));

    }

    /**
     * 绑定用户文章列表数据
     * @param holder contentViewHolder
     */
    private void onBindArticleData(ContentViewHolder holder,int position) {
        UserArticleBean bean = mList.get(position);
        Glide.with(mContext).load(bean.getUserHeadUrl()).asBitmap().into(holder.ivAuthorAvatar);
        holder.tvAuthorName.setText(bean.getUserName());
        holder.tvArticleContent.setText(bean.getGmtCreate());
        holder.tvArticleDescripe.setText(bean.getTitle());
        Glide.with(mContext).load(bean.getPicUrl().get(0)).asBitmap().into(holder.ivAuthorContent);
        holder.tvPraise.setText(String.valueOf(bean.getPraiseCount()));
        holder.tvCommentNum.setText(String.valueOf(bean.getCommentCount()));
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
        @BindView(R.id.tv_publish_num)
        TextView tvPublishNum;
        @BindView(R.id.ll_attention_container)
        LinearLayout llAttentionContainer;
        @BindView(R.id.tv_attention_num)
        TextView tvAttentionNum;
        @BindView(R.id.ll_fans_container)
        LinearLayout llFansContainer;
        @BindView(R.id.tv_fans_num)
        TextView tvFansNum;
        @BindView(R.id.ll_praise_container)
        LinearLayout llPraiseContainer;
        @BindView(R.id.tv_praise_num)
        TextView tvPraiseNum;
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
        @BindView(R.id.iv_author_content)
        ImageView ivAuthorContent;
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
                    listener.onAvatarClick();
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
                    listener.onMoreClick(headViewHolder.ivMore);
                    break;


                    /*content*/
                case R.id.iv_author_avatar:
                    ToastUtil.toast("作者头像");
//                    listener.onAvatarClick();
                    break;
                case R.id.ll_article_praise_container:
                    ToastUtil.toast("点赞");
                    break;
                case R.id.ll_article_comment_container:
                    ToastUtil.toast("评论");
                    break;
                    default:
                        break;
            }
        }
    }

    private OnViewClickListener listener;

    public void setOnViewClickListener(OnViewClickListener listener){
        this.listener = listener;
    }

    public interface OnViewClickListener{
        /**
         * 更多按钮点击事件
         * @param ivMore 更多按钮
         */
        void onMoreClick(ImageView ivMore);

        /**
         * 用户头像点击
         */
        void onAvatarClick();
    }
}