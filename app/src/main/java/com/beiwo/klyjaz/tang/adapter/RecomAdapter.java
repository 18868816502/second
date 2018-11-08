package com.beiwo.klyjaz.tang.adapter;

import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.beiwo.klyjaz.R;
import com.beiwo.klyjaz.social.bean.SocialTopicBean;
import com.beiwo.klyjaz.tang.GlideRoundTransform;
import com.beiwo.klyjaz.tang.RoundCornerTransformation;
import com.beiwo.klyjaz.util.DensityUtil;
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
    protected void convert(BaseViewHolder helper, SocialTopicBean.ForumBean item) {
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
                    .skipMemoryCache(true)
                    .crossFade(1000)
                    .transform(new CenterCrop(mContext), new GlideRoundTransform(mContext, 4))
                    .bitmapTransform(new CenterCrop(mContext), new RoundCornerTransformation(mContext, 4, RoundCornerTransformation.CornerType.TOP))
                    .into(iv_article_icon);
            tv_article_content.setVisibility(View.GONE);
        }
        Drawable praise = mContext.getResources().getDrawable(R.drawable.icon_social_topic_praise_select);
        praise.setBounds(0, 0, praise.getMinimumWidth(), praise.getMinimumHeight());
        Drawable unpraise = mContext.getResources().getDrawable(R.drawable.icon_social_topic_praise_unselect);
        unpraise.setBounds(0, 0, unpraise.getMinimumWidth(), unpraise.getMinimumHeight());

        ImageView iv_account_icon = helper.getView(R.id.iv_account_icon);
        Glide.with(mContext)
                .load(item.getUserHeadUrl())
                .centerCrop()
                .transform(new GlideCircleTransform(mContext))
                .error(R.drawable.mine_icon_head)
                .into(iv_account_icon);
        TextView tv_account_praise = helper.getView(R.id.tv_account_praise);
        tv_account_praise.setCompoundDrawables(item.getIsPraise() == 1 ? praise : unpraise, null, null, null);
        helper.setText(R.id.tv_article_title, item.getTitle())
                .setText(R.id.tv_article_content, item.getContent())
                .setText(R.id.tv_account_name, item.getUserName())
                .setText(R.id.tv_account_praise, item.getPraiseCount() + "");
    }
}