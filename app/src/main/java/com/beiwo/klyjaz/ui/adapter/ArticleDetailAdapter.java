package com.beiwo.klyjaz.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.LinearLayoutManager;
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
import com.beiwo.klyjaz.helper.UserHelper;
import com.beiwo.klyjaz.social.activity.PhotoDetailActivity;
import com.beiwo.klyjaz.social.bean.CommentReplyBean;
import com.beiwo.klyjaz.social.bean.ForumInfoBean;
import com.beiwo.klyjaz.social.bean.SocialTopicBean;
import com.beiwo.klyjaz.ui.activity.PersonalCenterActivity;
import com.beiwo.klyjaz.ui.listeners.OnViewClickListener;
import com.beiwo.klyjaz.util.ToastUtil;
import com.beiwo.klyjaz.view.CircleImageView;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bingoogolapple.bgabanner.BGABanner;

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
    private ForumInfoBean.ForumBean forumBean;

    public ArticleDetailAdapter(Context mContext) {
        this.mContext = mContext;
        datas = new ArrayList<>();
    }

    public void setDatas(List<CommentReplyBean> datas, ForumInfoBean.ForumBean forumBean) {
        this.datas.clear();
        this.datas.addAll(datas);
        this.forumBean = forumBean;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case HEAD:
                View headView = LayoutInflater.from(mContext).inflate(R.layout.item_article_detail_head, parent, false);
                return new HeadViewHolder(headView);
            case COMMENT:
                View commentView = LayoutInflater.from(mContext).inflate(R.layout.item_article_detail_comment, parent, false);
                return new CommmentViewHolder(commentView);
            case FOOT:
                View footView = LayoutInflater.from(mContext).inflate(R.layout.item_article_detail_foot, parent, false);
                return new FootViewHolder(footView);
            default:
                break;

        }
        return null;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (position == 0) {
            if (forumBean == null) {
                return;
            }
            HeadViewHolder headViewHolder = (HeadViewHolder) holder;
            headViewHolder.tvArticleTitle.setText(forumBean.getTitle());
            if(!TextUtils.isEmpty(forumBean.getUserHeadUrl())) {
                Glide.with(mContext).load(forumBean.getUserHeadUrl()).into(headViewHolder.ivAuthorAvatar);
            }
            headViewHolder.tvAuthorName.setText(TextUtils.isEmpty(forumBean.getUserName())?"未知":forumBean.getUserName());
            headViewHolder.tvAuthorTime.setText(forumBean.getGmtCreate());
            if (forumBean.getPicUrl() != null && forumBean.getPicUrl().size() != 0) {
                headViewHolder.bgaBanner.setVisibility(View.VISIBLE);
                headViewHolder.bgaBanner.setData(forumBean.getPicUrl(), null);
                headViewHolder.bgaBanner.setAdapter(new BGABanner.Adapter<ImageView, String>() {
                    @Override
                    public void fillBannerItem(BGABanner banner, ImageView itemView, @Nullable String model, int position) {
                        Glide.with(mContext).load(model).into(itemView);
                    }
                });
                headViewHolder.bgaBanner.setDelegate(new BGABanner.Delegate<ImageView, String>() {
                    @Override
                    public void onBannerItemClick(BGABanner banner, ImageView itemView, @Nullable String model, int position) {
                        Intent intent = new Intent(mContext, PhotoDetailActivity.class);
                        intent.putStringArrayListExtra("datas", (ArrayList<String>) forumBean.getPicUrl());
                        intent.putExtra("position",position);
                        mContext.startActivity(intent);
                    }
                });
            } else {
                headViewHolder.bgaBanner.setVisibility(View.GONE);
            }
            headViewHolder.tvArticleContent.setText(forumBean.getContent());
            headViewHolder.tvCommentNum.setText(String.valueOf("评论 " + datas.size()));
            if (forumBean.getIsPraise() == 0) {
                Drawable dwLeft = mContext.getResources().getDrawable(R.drawable.icon_social_topic_praise_unselect);
                dwLeft.setBounds(0, 0, dwLeft.getMinimumWidth(), dwLeft.getMinimumHeight());
                headViewHolder.tvPraise.setCompoundDrawables(dwLeft, null, null, null);
                headViewHolder.tvPraise.setText("赞");
                headViewHolder.tvPraise.setTextColor(mContext.getColor(R.color.black_2));
            } else {
                Drawable dwLeft = mContext.getResources().getDrawable(R.drawable.icon_social_topic_praise_select);
                dwLeft.setBounds(0, 0, dwLeft.getMinimumWidth(), dwLeft.getMinimumHeight());
                headViewHolder.tvPraise.setCompoundDrawables(dwLeft, null, null, null);
                headViewHolder.tvPraise.setText(String.valueOf(forumBean.getPraiseCount()));
                headViewHolder.tvPraise.setTextColor(mContext.getColor(R.color.c_ff5240));
            }

        } else if (position == getItemCount() - 1) {
            FootViewHolder footViewHolder = (FootViewHolder) holder;
            if (datas.size() > 3) {
                footViewHolder.setVisibility(true);
                footViewHolder.tvCommentNum.setText(String.format(mContext.getString(R.string.article_comment_total_num), datas.size()));
            } else {
                footViewHolder.setVisibility(false);
            }
        } else {
            CommmentViewHolder commmentViewHolder = (CommmentViewHolder) holder;
            commmentViewHolder.tvCommentPraise.setTag(R.id.tag_praise, position - 1);
            commmentViewHolder.ivArticleComment.setTag(R.id.tag_comment, position - 1);
            commmentViewHolder.tvCommentDelete.setTag(R.id.tag_delete, position - 1);
            commmentViewHolder.itemView.setTag(position - 1);
            commmentViewHolder.ivCommentatorAcatar.setTag(R.id.comment_user_avatar,position - 1);

            if(!TextUtils.isEmpty(datas.get(position - 1).getUserHeadUrl())) {
                Glide.with(mContext).load(datas.get(position - 1).getUserHeadUrl()).into(commmentViewHolder.ivCommentatorAcatar);
            }
            commmentViewHolder.tvCommentatorName.setText(datas.get(position - 1).getUserName());
            commmentViewHolder.tvCommentContent.setText(datas.get(position - 1).getContent());
            commmentViewHolder.tvCommentTime.setText(String.valueOf(datas.get(position - 1).getGmtCreate() + ""));
            commmentViewHolder.commentAdapter.setDatas(datas.get(position - 1).getReplyDtoList(),datas.get(position - 1).getId());

            if (TextUtils.equals(UserHelper.getInstance(mContext).getProfile().getId(), datas.get(position - 1).getUserId())) {
                commmentViewHolder.tvCommentDelete.setVisibility(View.VISIBLE);
            } else {
                commmentViewHolder.tvCommentDelete.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public int getItemCount() {
        if (datas.size() > 3) {
            return 5;
        } else {
            return datas.size() + 2;
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return HEAD;
        } else if (position == getItemCount() - 1) {
            return FOOT;
        } else {
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
            ButterKnife.bind(this, itemView);
            tvArticleTitle.setFocusable(true);
            setOnClick(ivAuthorAvatar, tvComment, tvPraise);
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
        @BindView(R.id.tv_comment_delete)
        TextView tvCommentDelete;
        @BindView(R.id.tv_comment_praise)
        TextView tvCommentPraise;
        @BindView(R.id.iv_article_comment)
        ImageView ivArticleComment;
        @BindView(R.id.item_recycler)
        RecyclerView itemRecyclerView;
        ArticleCommentAdapter commentAdapter;

        CommmentViewHolder(final View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            LinearLayoutManager manager = new LinearLayoutManager(mContext);
            manager.setOrientation(LinearLayoutManager.VERTICAL);
            commentAdapter = new ArticleCommentAdapter(mContext);
            itemRecyclerView.setLayoutManager(manager);
            itemRecyclerView.setAdapter(commentAdapter);
            commentAdapter.notifyDataSetChanged();
            commentAdapter.setOnViewClickListener(new OnViewClickListener() {
                @Override
                public void onViewClick(View view, int type, int position) {
                    view.setTag(itemView.getTag());
                    switch (type){
                        case ConstantTag.TAG_CHILD_REPLY_COMMENT:
                            listener.onViewClick(view, ConstantTag.TAG_COMMENT_MORE, position);
                            break;

                            default:
                                break;
                    }
//                    listener.onViewClick(view, type, position);
                }
            });
            setOnClick(tvCommentPraise, ivArticleComment, tvCommentDelete,ivCommentatorAcatar);
        }
    }

    class FootViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_comment_num_title)
        TextView tvCommentNum;

        FootViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(new OnClickListener());
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
                case R.id.iv_author_avatar:
                    Intent intent = new Intent(mContext, PersonalCenterActivity.class);
                    intent.putExtra("userId", forumBean.getUserId());
                    mContext.startActivity(intent);
                    break;
                case R.id.iv_commentator_avatar:
                    Intent pIntent = new Intent(mContext, PersonalCenterActivity.class);
                    pIntent.putExtra("userId",datas.get((Integer) v.getTag(R.id.comment_user_avatar)).getUserId());
                    mContext.startActivity(pIntent);
                    break;
                case R.id.tv_article_attention:
//                    listener.onViewClick(v,ConstantTag.TAG_ATTENTION);
                    break;
                case R.id.tv_article_praise:
                    listener.onViewClick(v, ConstantTag.TAG_PARISE_ARTICLE, 0);
                    break;
                //追加文章评论
                case R.id.tv_comment:
                    listener.onViewClick(v, ConstantTag.TAG_COMMENT_ARTICLE, 0);
                    break;


                //评论点赞
                case R.id.tv_comment_praise:
                    int position = (int) v.getTag(R.id.tag_praise);
                    ToastUtil.toast("点赞第" + (position + 1) + "条");
                    listener.onViewClick(v, ConstantTag.TAG_PRAISE_COMMENT, position);
                    break;
                //评论回复
                case R.id.iv_article_comment:
                    int comPosition = (int) v.getTag(R.id.tag_comment);
//                    ToastUtil.toast("回复第"+(comPosition + 1) + "条");
//                    listener.onViewClick(v, ConstantTag.TAG_REPLY_COMMENT, comPosition);
                    listener.onViewClick(v, ConstantTag.TAG_COMMENT_MORE, comPosition);
                    break;
                //评论删除
                case R.id.tv_comment_delete:
                    int delPosition = (int) v.getTag(R.id.tag_delete);
                    ToastUtil.toast("删除第" + (delPosition + 1) + "条");
                    listener.onViewClick(v, ConstantTag.TAG_COMMENT_DELETE, delPosition);
                    break;

                case R.id.foot:
                    listener.onViewClick(v, ConstantTag.TAG_COMMENT_MORE, 0);
                    break;
                default:
                    break;
            }
        }
    }
}
