package com.beiwo.klyjaz.ui.adapter.social;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.beiwo.klyjaz.R;
import com.beiwo.klyjaz.helper.DataStatisticsHelper;
import com.beiwo.klyjaz.helper.UserHelper;
import com.beiwo.klyjaz.social.bean.SocialTopicBean;
import com.beiwo.klyjaz.tang.DlgUtil;
import com.beiwo.klyjaz.ui.activity.ArticleDetailActivity;
import com.beiwo.klyjaz.ui.activity.PersonalCenterActivity;
import com.beiwo.klyjaz.ui.activity.UserAuthorizationActivity;
import com.beiwo.klyjaz.umeng.NewVersionEvents;
import com.beiwo.klyjaz.view.CircleImageView;
import com.beiwo.klyjaz.view.GlideCircleTransform;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author chenguoguo
 * @name loanmarket
 * @class name：com.beihui.market.ui.adapter.social
 * @descripe
 * @time 2018/9/19 10:10
 */
public class SocialRecommendAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<SocialTopicBean.ForumBean> datas;
    private Activity mContext;

    private int mLastPosition = 0;

    public SocialRecommendAdapter(Activity mContext) {
        this.mContext = mContext;
        datas = new ArrayList<>();
    }

    public void setDatas(List<SocialTopicBean.ForumBean> mList) {
        datas.clear();
        datas.addAll(mList);
        notifyDataSetChanged();
    }

    public void appendDatas(List<SocialTopicBean.ForumBean> mList) {
        mLastPosition = datas.size();
        datas.addAll(mList);
        notifyItemInserted(mLastPosition);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View operateView = LayoutInflater.from(mContext).inflate(R.layout.item_social_recommend_operate_topic, parent, false);
        return new OperateTopicViewHolder(operateView);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        OperateTopicViewHolder viewHoler = (OperateTopicViewHolder) holder;

        if (datas.get(position).getPicUrl() != null && datas.get(position).getPicUrl().size() != 0) {
            viewHoler.ivTopic.setVisibility(View.VISIBLE);
            viewHoler.tvTopicContent.setVisibility(View.GONE);
            Glide.with(mContext).load(datas.get(position).getPicUrl().get(0)).into(viewHoler.ivTopic);
        } else {
            viewHoler.ivTopic.setVisibility(View.GONE);
            viewHoler.tvTopicContent.setVisibility(View.VISIBLE);
            viewHoler.tvTopicContent.setText(datas.get(position).getContent());
        }
        viewHoler.tvTopicTitle.setText(datas.get(position).getTitle());
        viewHoler.tvTopicContent.setText(datas.get(position).getContent());
        Glide.with(mContext).load(datas.get(position).getUserHeadUrl()).into(viewHoler.ivAvatar);
        viewHoler.tvName.setText(datas.get(position).getUserName());
        viewHoler.tvPraise.setText(String.valueOf(datas.get(position).getPraiseCount() + ""));
        if (datas.get(position).getIsPraise() == 0) {
            Drawable dwLeft = mContext.getResources().getDrawable(R.drawable.icon_social_topic_praise_unselect);
            dwLeft.setBounds(0, 0, dwLeft.getMinimumWidth(), dwLeft.getMinimumHeight());
            viewHoler.tvPraise.setCompoundDrawables(dwLeft, null, null, null);
            viewHoler.tvPraise.setTextColor(mContext.getColor(R.color.black_2));
        } else {
            Drawable dwLeft = mContext.getResources().getDrawable(R.drawable.icon_social_topic_praise_select);
            dwLeft.setBounds(0, 0, dwLeft.getMinimumWidth(), dwLeft.getMinimumHeight());
            viewHoler.tvPraise.setCompoundDrawables(dwLeft, null, null, null);
            viewHoler.tvPraise.setTextColor(mContext.getColor(R.color.c_ff5240));
        }

        viewHoler.itemView.setTag(position);
        viewHoler.tvPraise.setTag(position);
        viewHoler.ivAvatar.setTag(R.id.tag_user_avatar,position);
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    class OperateTopicViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.iv_topic)
        ImageView ivTopic;
        @BindView(R.id.tv_topic_title)
        TextView tvTopicTitle;
        @BindView(R.id.tv_topic_content)
        TextView tvTopicContent;
        @BindView(R.id.iv_avatar)
        CircleImageView ivAvatar;
        @BindView(R.id.tv_name)
        TextView tvName;
        @BindView(R.id.tv_praise)
        TextView tvPraise;

        public OperateTopicViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DataStatisticsHelper.getInstance().onCountUvPv(NewVersionEvents.COMMUNITY_FORUM_HIT, datas.get((Integer) v.getTag()).getForumId());
                    boolean isLogin = UserHelper.getInstance(v.getContext()).isLogin();
                    Intent intent = new Intent(mContext, ArticleDetailActivity.class);
                    if (isLogin) {
                        intent.putExtra("userId", datas.get((Integer) v.getTag()).getUserId());
                    } else {
                        intent.putExtra("userId", "");
                    }
                    intent.putExtra("forumId", datas.get((Integer) v.getTag()).getForumId());
                    mContext.startActivity(intent);
                }
            });
            ivAvatar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!UserHelper.getInstance(mContext).isLogin()) {
                        DlgUtil.loginDlg(mContext, null);
                        return;
                    }
                    Intent intent = new Intent(mContext, PersonalCenterActivity.class);
                    intent.putExtra("userId",datas.get((Integer) v.getTag(R.id.tag_user_avatar)).getUserId());
                    mContext.startActivity(intent);
                }
            });
        }
    }
}
