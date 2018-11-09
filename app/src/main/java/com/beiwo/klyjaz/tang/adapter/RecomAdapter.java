package com.beiwo.klyjaz.tang.adapter;

import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.beiwo.klyjaz.R;
import com.beiwo.klyjaz.helper.UserHelper;
import com.beiwo.klyjaz.social.bean.SocialTopicBean;
import com.beiwo.klyjaz.tang.RoundCornerTransformation;
import com.beiwo.klyjaz.view.GlideCircleTransform;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * https://gitee.com/tangbuzhi
 *
 * @author: Tangbuzhi
 * @version:
 * @package:
 * @description:
 * @modify:
 * @date: 2018/11/8
 */

public class RecomAdapter extends BaseQuickAdapter<SocialTopicBean.ForumBean, BaseViewHolder> {
    public RecomAdapter() {
        super(R.layout.layout_recom);
        openLoadAnimation(ALPHAIN);
    }

    @Override
    protected void convert(final BaseViewHolder helper, final SocialTopicBean.ForumBean item) {
        List<String> picUrl = item.getPicUrl();
        boolean nonePic = picUrl == null || picUrl.size() == 0;
        ImageView iv_article_icon = helper.getView(R.id.iv_article_icon);
        TextView tv_article_content = helper.getView(R.id.tv_article_content);
        if (nonePic) {
            iv_article_icon.setVisibility(View.GONE);
            tv_article_content.setVisibility(View.VISIBLE);
        } else {
            iv_article_icon.setVisibility(View.VISIBLE);
            Glide.with(mContext)
                    .load(picUrl.get(0))
                    .crossFade(1000)
                    .bitmapTransform(new CenterCrop(mContext), new RoundCornerTransformation(mContext, 4, RoundCornerTransformation.CornerType.LEFT))
                    .into(iv_article_icon);
            tv_article_content.setVisibility(View.GONE);
        }

        ImageView iv_account_icon = helper.getView(R.id.iv_account_icon);
        Glide.with(mContext)
                .load(item.getUserHeadUrl())
                .centerCrop()
                .transform(new GlideCircleTransform(mContext))
                .error(R.drawable.mine_icon_head)
                .into(iv_account_icon);

        Drawable praise = ContextCompat.getDrawable(mContext, R.drawable.ic_praised);
        praise.setBounds(0, 0, praise.getMinimumWidth(), praise.getMinimumHeight());
        Drawable unpraise = ContextCompat.getDrawable(mContext, R.drawable.ic_unpraised);
        unpraise.setBounds(0, 0, unpraise.getMinimumWidth(), unpraise.getMinimumHeight());
        final TextView tv_account_praise = helper.getView(R.id.tv_account_praise);
        tv_account_praise.setCompoundDrawables(item.getIsPraise() == 1 ? praise : unpraise, null, null, null);

        helper.setText(R.id.tv_article_title, item.getTitle())
                .setText(R.id.tv_article_content, item.getContent())
                .setText(R.id.tv_account_name, item.getUserName())
                .setText(R.id.tv_account_praise, item.getPraiseCount() + "")
                .addOnClickListener(R.id.ll_article_wrap)
                .addOnClickListener(R.id.iv_account_icon)
                .addOnClickListener(R.id.tv_account_name)
                .addOnClickListener(R.id.tv_account_praise);

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userId = "";
                if (UserHelper.getInstance(mContext).isLogin())
                    userId = UserHelper.getInstance(mContext).id();
                switch (v.getId()) {
                    case R.id.ll_article_wrap:
                        if (articleClickListener != null)
                            articleClickListener.itemClick(item.getForumId(), userId);
                        break;
                    case R.id.iv_account_icon:
                    case R.id.tv_account_name:
                        if (articleClickListener != null)
                            articleClickListener.userClick(item.getUserId());
                        break;
                    case R.id.tv_account_praise:
                        if (articleClickListener != null)
                            articleClickListener.praiseClick(item, tv_account_praise);
                        break;
                    default:
                        break;
                }
            }
        };

        helper.getView(R.id.ll_article_wrap).setOnClickListener(listener);
        iv_account_icon.setOnClickListener(listener);
        helper.getView(R.id.tv_account_name).setOnClickListener(listener);
        helper.getView(R.id.tv_account_praise).setOnClickListener(listener);
    }

    public void setPraiseState(SocialTopicBean.ForumBean item, TextView tv) {
        Drawable praise = ContextCompat.getDrawable(mContext, R.drawable.ic_praised);
        praise.setBounds(0, 0, praise.getMinimumWidth(), praise.getMinimumHeight());
        Drawable unpraise = ContextCompat.getDrawable(mContext, R.drawable.ic_unpraised);
        unpraise.setBounds(0, 0, unpraise.getMinimumWidth(), unpraise.getMinimumHeight());
        tv.setCompoundDrawables(item.getIsPraise() == 1 ? praise : unpraise, null, null, null);
        tv.setText(item.getPraiseCount() + "");
    }

    private OnArticleClickListener articleClickListener;

    public void setOnArticleClickListener(OnArticleClickListener clickListener) {
        articleClickListener = clickListener;
    }

    public interface OnArticleClickListener {
        void itemClick(String forumId, String userId);

        void userClick(String userId);

        void praiseClick(SocialTopicBean.ForumBean item, TextView tv);
    }
}