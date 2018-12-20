package com.beiwo.qnejqaz.social.adapter;

import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.beiwo.qnejqaz.R;
import com.beiwo.qnejqaz.helper.UserHelper;
import com.beiwo.qnejqaz.social.bean.ForumBean;
import com.beiwo.qnejqaz.tang.RoundCornerTransformation;
import com.beiwo.qnejqaz.tang.StringUtil;
import com.beiwo.qnejqaz.view.GlideCircleTransform;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.lang.ref.WeakReference;

/**
 * https://gitee.com/tangbuzhi
 *
 * @author: Tangbuzhi
 * @version:
 * @package:
 * @description:
 * @modify:
 * @date: 2018/12/6
 */
public class TopicAdapter extends BaseQuickAdapter<ForumBean, BaseViewHolder> {
    public TopicAdapter() {
        super(R.layout.layout_topic_comement_item);
        openLoadAnimation(ALPHAIN);
    }

    @Override
    protected void convert(BaseViewHolder helper, final ForumBean item) {
        Drawable praised = ContextCompat.getDrawable(mContext, R.drawable.ic_praised);
        Drawable unpraise = ContextCompat.getDrawable(mContext, R.drawable.ic_unpraised);

        ImageView iv_topic_avator = helper.getView(R.id.iv_topic_avator);
        TextView tv_topic_title_nopic = helper.getView(R.id.tv_topic_title_nopic);
        LinearLayout ll_topic_pic_wrap = helper.getView(R.id.ll_topic_pic_wrap);
        ImageView iv_topic_content_pic = helper.getView(R.id.iv_topic_content_pic);
        final TextView tv_topic_praise = helper.getView(R.id.tv_topic_praise);

        Glide.with(mContext)
                .load(item.getUserHeadUrl())
                .transform(new GlideCircleTransform(mContext))
                .error(R.drawable.mine_icon_head)
                .into(iv_topic_avator);
        helper.setText(R.id.tv_topic_name, item.getUserName())
                .setText(R.id.tv_topic_time, StringUtil.time2Str(item.getGmtCreate()))
                .setText(R.id.tv_topic_title_nopic, item.getTitle())
                .setText(R.id.tv_topic_title_pic, item.getTitle())
                .setText(R.id.tv_topic_content, item.getContent())
                .setText(R.id.tv_topic_praise, item.getPraiseCount() + "")
                .addOnClickListener(R.id.iv_topic_avator)
                .addOnClickListener(R.id.tv_account_praise)
                .addOnClickListener(R.id.csl_topic_wrap);
        iv_topic_avator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (clickListener != null) clickListener.userClick(item.getUserId());
            }
        });
        tv_topic_praise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (clickListener != null)
                    clickListener.praiseClick(item, new WeakReference(tv_topic_praise));
            }
        });
        helper.getView(R.id.csl_topic_wrap).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (clickListener != null)
                    clickListener.itemClick(item.getForumId(), UserHelper.getInstance(mContext).isLogin() ? UserHelper.getInstance(mContext).id() : "");
            }
        });
        if (item.getPicUrl() == null || item.getPicUrl().size() <= 0) {
            tv_topic_title_nopic.setVisibility(View.VISIBLE);
            ll_topic_pic_wrap.setVisibility(View.GONE);
        } else {
            tv_topic_title_nopic.setVisibility(View.GONE);
            ll_topic_pic_wrap.setVisibility(View.VISIBLE);
            Glide.with(mContext)
                    .load(item.getPicUrl().get(0))
                    .bitmapTransform(new CenterCrop(mContext), new RoundCornerTransformation(mContext, 4, RoundCornerTransformation.CornerType.RIGHT))
                    .error(R.mipmap.ic_launcher)
                    .into(iv_topic_content_pic);
        }
        if (item.getIsPraise() == 1) {
            praised.setBounds(0, 0, praised.getMinimumWidth(), praised.getMinimumHeight());
            tv_topic_praise.setCompoundDrawables(praised, null, null, null);
        } else {
            unpraise.setBounds(0, 0, unpraise.getMinimumWidth(), unpraise.getMinimumHeight());
            tv_topic_praise.setCompoundDrawables(unpraise, null, null, null);
        }
    }

    public void setPraiseState(ForumBean item, TextView tv) {
        Drawable praise = ContextCompat.getDrawable(mContext, R.drawable.ic_praised);
        praise.setBounds(0, 0, praise.getMinimumWidth(), praise.getMinimumHeight());
        Drawable unpraise = ContextCompat.getDrawable(mContext, R.drawable.ic_unpraised);
        unpraise.setBounds(0, 0, unpraise.getMinimumWidth(), unpraise.getMinimumHeight());
        tv.setCompoundDrawables(item.getIsPraise() == 1 ? praise : unpraise, null, null, null);
        tv.setText(item.getPraiseCount() + "");
    }

    private OnTopicItemClickListener clickListener;

    public void setOnTopicItemClickListener(OnTopicItemClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public interface OnTopicItemClickListener {
        void itemClick(String forumId, String userId);

        void userClick(String userId);

        void praiseClick(ForumBean item, WeakReference<TextView> tvRef);
    }
}