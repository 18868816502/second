package com.beiwo.qnejqaz.social.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.beiwo.qnejqaz.R;
import com.beiwo.qnejqaz.constant.ConstantTag;
import com.beiwo.qnejqaz.helper.UserHelper;
import com.beiwo.qnejqaz.social.bean.CommentReplyBean;
import com.beiwo.qnejqaz.tang.DlgUtil;
import com.beiwo.qnejqaz.ui.activity.PersonalCenterActivity;
import com.beiwo.qnejqaz.ui.listeners.OnViewClickListener;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author chenguoguo
 * @name loanmarket
 * @class name：com.beihui.market.ui.adapter
 * @describe 动态回复列表适配器
 * @time 2018/9/13 14:56
 */
public class ForumChildCommentAdapter extends RecyclerView.Adapter {

    private Activity mContext;
    private boolean isOpen = false;
    private String selfId;

    private List<CommentReplyBean.ReplyDtoListBean> datas;
    private static final int CONTENT = 1;
    private static final int FOOT = 2;

    public ForumChildCommentAdapter(Activity mContext) {
        this.mContext = mContext;
        datas = new ArrayList<>();
    }

    public void setDatas(List<CommentReplyBean.ReplyDtoListBean> datas, String selfId) {
        this.datas.clear();
        this.datas.addAll(datas);
        this.selfId = selfId;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case CONTENT:
                View commentView = LayoutInflater.from(mContext).inflate(R.layout.item_article_detail_comment, parent, false);
                return new ViewHolder(commentView);
            case FOOT:
                View footView = LayoutInflater.from(mContext).inflate(R.layout.item_article_detail_comment_foot, parent, false);
                return new FootViewHolder(footView);
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (position == getItemCount() - 1) {
            FootViewHolder viewHolder = (FootViewHolder) holder;
            if (datas.size() < 2) {
                viewHolder.setVisibility(false);
            } else {
                viewHolder.setVisibility(true);
                if (isOpen) {
                    viewHolder.tvOpenReply.setText("关闭全部回复");
                    viewHolder.ivOpenReply.setImageResource(R.mipmap.ic_up);
                } else {
                    viewHolder.tvOpenReply.setText("展开全部回复");
                    viewHolder.ivOpenReply.setImageResource(R.mipmap.ic_down);
                }
            }
        } else {
            ViewHolder viewHolder = (ViewHolder) holder;
            viewHolder.tvCommentPraise.setTag(position);
            viewHolder.ivCommentatorAcatar.setTag(R.id.user_avatar, position);
            viewHolder.ivArticleComment.setTag(R.id.tag_child_comment, position);
            viewHolder.tvDelete.setTag(position);
            if (!TextUtils.isEmpty(datas.get(position).getUserHeadUrl())) {
                Glide.with(mContext).load(datas.get(position).getUserHeadUrl()).into(viewHolder.ivCommentatorAcatar);
            }

            viewHolder.tvCommentTime.setText(datas.get(position).getGmtCreate());
            StringBuilder sb = new StringBuilder();
            if (TextUtils.equals(selfId, datas.get(position).getReplyId())) {
                viewHolder.tvCommentContent.setText(datas.get(position).getContent());
            } else {
                sb.append("回复<font color='#2a84ff'>")
                        .append(datas.get(position).getToUserName())
                        .append("</font>:")
                        .append((TextUtils.isEmpty(datas.get(position).getContent()) ? "" : datas.get(position).getContent()));
                viewHolder.tvCommentContent.setText(Html.fromHtml(sb.toString()));
            }
            viewHolder.tvCommentatorName.setText(datas.get(position).getUserName());
            if (UserHelper.getInstance(mContext).isLogin()) {
                if (TextUtils.equals(UserHelper.getInstance(mContext).id(), datas.get(position).getUserId())) {
                    viewHolder.tvDelete.setVisibility(View.VISIBLE);
                } else {
                    viewHolder.tvDelete.setVisibility(View.GONE);
                }
            } else {
                viewHolder.tvDelete.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public int getItemCount() {
        if (isOpen) {
            return datas.size() + 1;
        } else {
            if (datas.size() > 0) {
                return 2;
            } else {
                return 0;
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == getItemCount() - 1) {
            return FOOT;
        } else {
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
            ButterKnife.bind(this, itemView);
            itemRecyclerView.setVisibility(View.GONE);
            setOnClick(tvCommentPraise, ivArticleComment, tvDelete, ivCommentatorAcatar);
        }
    }

    class FootViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_open_reply)
        TextView tvOpenReply;
        @BindView(R.id.iv_open_reply)
        ImageView ivOpenReply;

        public FootViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
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
                    if (!UserHelper.getInstance(mContext).isLogin()) {
                        DlgUtil.loginDlg(mContext, null);
                        return;
                    }
                    Intent pIntent = new Intent(mContext, PersonalCenterActivity.class);
                    pIntent.putExtra("userId", datas.get((Integer) v.getTag(R.id.user_avatar)).getUserId());
                    mContext.startActivity(pIntent);
                    break;
                //评论点赞
                case R.id.tv_comment_praise:
                    int position = (int) v.getTag();
                    listener.onViewClick(v, ConstantTag.TAG_CHILD_PARISE_COMMENT, position);
                    break;
                //评论回复
                case R.id.iv_article_comment:
                    int comPosition = (int) v.getTag(R.id.tag_child_comment);
                    listener.onViewClick(v, ConstantTag.TAG_CHILD_REPLY_COMMENT, comPosition);
                    break;
                case R.id.tv_comment_delete:
                    int delPosition = (int) v.getTag();
                    listener.onViewClick(v, ConstantTag.TAG_CHILD_COMMENT_DELETE, delPosition);
                    break;
                case R.id.foot:
                    //展开或者回复
                    if (isOpen) {
                        isOpen = false;
                    } else {
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
