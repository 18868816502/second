package com.beiwo.klyjaz.ui.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.beiwo.klyjaz.R;
import com.beiwo.klyjaz.constant.ConstantTag;
import com.beiwo.klyjaz.entity.UserTopicBean;
import com.beiwo.klyjaz.entity.UserInfoBean;
import com.beiwo.klyjaz.helper.UserHelper;
import com.beiwo.klyjaz.ui.listeners.OnItemClickListener;
import com.beiwo.klyjaz.ui.listeners.OnViewClickListener;
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
    private List<UserTopicBean> mList;

    /**
     * 装载头部用户数据
     * @param userInfo 用户bean
     */
    public void setHeadData(UserInfoBean userInfo){
        this.userInfo = userInfo;
        notifyDataSetChanged();
    }

    /**
     * 装载刷新的文章数据
     * @param userInfoBean 用户bean
     * @param list 用户话题列表
     */
    public void setDatas(UserInfoBean userInfoBean,List<UserTopicBean> list){
        mList.clear();
        mList.addAll(list);
        this.userInfo = userInfoBean;
        notifyDataSetChanged();
    }

    /**
     * 加载更多文章数据
     * @param list 文章列表
     */
    public void appendTopicData(List<UserTopicBean> list){
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
            onBindHeadData(headViewHolder);
        }else{
            contentViewHolder = (ContentViewHolder) holder;
            contentViewHolder.itemView.setTag(position-1);
            onBindArticleData(contentViewHolder,position-1);
        }
    }

    /**
     * 绑定头部用户数据
     * @param holder headViewHolder
     */
    private void onBindHeadData(HeadViewHolder holder) {
        if(userInfo == null){ return; }
        if(!TextUtils.isEmpty(userInfo.getHeadPortrait())) {
            Glide.with(mContext).load(userInfo.getHeadPortrait()).asBitmap().into(holder.ivAvatar);
        }else{
            holder.ivAvatar.setBackgroundResource(R.drawable.mine_icon_head);
        }
        holder.tvName.setText(userInfo.getUserName());
        if(1 == userInfo.getSex()){
            holder.ivSex.setBackgroundResource(R.drawable.icon_social_boy);
        }else{
            holder.ivSex.setBackgroundResource(R.drawable.icon_social_girl);
        }
        if(!TextUtils.isEmpty(userInfo.getIntroduce())){
            holder.tvProduce.setText("简介:"+userInfo.getIntroduce());
        }else{
            holder.tvProduce.setText("简介:");
        }
        holder.tvPublishNum.setText(String.valueOf(userInfo.getForumCount()));
        holder.tvPraiseNum.setText(String.valueOf(userInfo.getPraiseCount()));

        if(UserHelper.getInstance(mContext).isLogin()) {
            if (TextUtils.equals(userInfo.getUserId(), UserHelper.getInstance(mContext).getProfile().getId())) {
                holder.ivMore.setVisibility(View.VISIBLE);
                holder.ivAvatar.setClickable(true);
                holder.llEditContainer.setVisibility(View.VISIBLE);
            } else {
                holder.ivMore.setVisibility(View.GONE);
                holder.ivAvatar.setClickable(false);
                holder.llEditContainer.setVisibility(View.GONE);
            }
        }

    }

    /**
     * 绑定用户文章列表数据
     * @param holder contentViewHolder
     */
    private void onBindArticleData(ContentViewHolder holder,int position) {
        UserTopicBean bean = mList.get(position);
        if(!TextUtils.isEmpty(bean.getUserHeadUrl())) {
            Glide.with(mContext).load(bean.getUserHeadUrl()).asBitmap().into(holder.ivAuthorAvatar);
        }
        holder.tvAuthorName.setText(bean.getUserName());
        holder.tvPublishTime.setText(String.valueOf(bean.getCreateText()));
        holder.tvArticleContent.setText(bean.getTitle());
        holder.tvArticleDescripe.setText(bean.getContent());
        holder.tvTitle.setText(bean.getTitle());
        if(bean.getPicUrl()!=null&&bean.getPicUrl().size()!=0) {
            Glide.with(mContext).load(bean.getPicUrl().get(0)).asBitmap().into(holder.ivAuthorContent);
            holder.llContainer.setVisibility(View.VISIBLE);
            holder.tvTitle.setVisibility(View.GONE);
        }else{
            holder.ivAuthorContent.setVisibility(View.GONE);
            holder.tvTitle.setVisibility(View.VISIBLE);
            holder.llContainer.setVisibility(View.GONE);
        }
        holder.tvPraise.setText(String.valueOf(bean.getPraiseCount()));
        holder.tvCommentNum.setText(String.valueOf(bean.getCommentCount()));
        if (mList.get(position).getIsPraise() == 0) {
            holder.ivPraise.setBackgroundResource(R.drawable.icon_social_personal_praise_unselected);
        } else {
            holder.ivPraise.setBackgroundResource(R.drawable.icon_social_personal_praise_selected);
        }
    }

    @Override
    public int getItemCount() {
        return mList.size() + 1;
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
        @BindView(R.id.iv_sex)
        ImageView ivSex;
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
        @BindView(R.id.ll_article_content)
        LinearLayout llContainer;
        @BindView(R.id.tv_title)
        TextView tvTitle;


        ContentViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
            setOnClick(itemView);
        }
    }

    class OnClickListener implements View.OnClickListener{

        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.avatar:
//                    ToastUtil.toast("用户头像");
                    listener.onViewClick(view, ConstantTag.TAG_PERSONAL_AVATAR,0);
                    break;
                case R.id.ll_edit_container:
//                    ToastUtil.toast("编辑个人资料");
                    listener.onViewClick(view, ConstantTag.TAG_PERSONAL_INFO_EDIT,0);
                    break;
                case R.id.ll_publish_container:
//                    ToastUtil.toast("发布");
                    listener.onViewClick(view, ConstantTag.TAG_PERSONAL_PUBLISH,0);
                    break;
                case R.id.ll_attention_container:
//                    ToastUtil.toast("关注");
//                    listener.onViewClick(view, ConstantTag.TAG_PERSONAL_INFO_EDIT,0);
                    break;
                case R.id.ll_fans_container:
//                    ToastUtil.toast("粉丝");
//                    listener.onViewClick(view, ConstantTag.TAG_PERSONAL_INFO_EDIT,0);
                    break;
                case R.id.ll_praise_container:
//                    ToastUtil.toast("获赞");
                    listener.onViewClick(view, ConstantTag.TAG_PERSONAL_PARISE,0);
                    break;
                case R.id.iv_more:
                    listener.onViewClick(view,ConstantTag.TAG_PERSONAL_MORE,0);
                    break;
                    /*content*/
                case R.id.iv_author_avatar:
//                    ToastUtil.toast("作者头像");
//                    listener.onAvatarClick();
                    break;
                case R.id.ll_article_praise_container:
//                    ToastUtil.toast("点赞");
                    break;
                case R.id.ll_article_comment_container:
//                    ToastUtil.toast("评论");
                    break;
                case R.id.article_item:
                    itemListener.onItemClick((Integer) view.getTag());
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

    private OnItemClickListener itemListener;

    public void setOnItemClickListener(OnItemClickListener itemListener){
        this.itemListener = itemListener;
    }
}