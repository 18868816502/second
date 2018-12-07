package com.beiwo.klyjaz.tang.adapter;

import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.beiwo.klyjaz.R;
import com.beiwo.klyjaz.helper.UserHelper;
import com.beiwo.klyjaz.social.bean.ForumBean;
import com.beiwo.klyjaz.tang.RoundCornerTransformation;
import com.beiwo.klyjaz.view.GlideCircleTransform;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.lang.ref.WeakReference;
import java.util.List;

/**
 * https://gitee.com/tangbuzhi
 *
 * @author: Tangbuzhi
 * @version:
 * @package:
 * @description:
 * @modify:
 * @date: 2018/12/5
 */
public class RecomMultiAdapter extends BaseMultiItemQuickAdapter<ForumBean, BaseViewHolder> {
    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param data A new list is created out of this one to avoid mutable list
     */
    public RecomMultiAdapter(List<ForumBean> data) {
        super(data);
        addItemType(ForumBean.TYPE_ARTICLE, R.layout.layout_recom);//普通帖子
        addItemType(ForumBean.TYPE_EVENT, R.layout.layout_event_item);//活动
        addItemType(ForumBean.TYPE_GOODS, R.layout.layout_goods_item);//下款推荐
        addItemType(ForumBean.TYPE_TOPIC, R.layout.layout_topic_item);//话题
    }

    @Override
    protected BaseViewHolder createBaseViewHolder(ViewGroup parent, int layoutResId) {
        return createBaseViewHolder(getItemView(layoutResId, parent));
    }

    @Override
    protected void convert(BaseViewHolder helper, ForumBean item) {
        switch (helper.getItemViewType()) {
            case ForumBean.TYPE_ARTICLE:
                article(helper, item);
                break;
            case ForumBean.TYPE_EVENT:
                event(helper, item);
                break;
            case ForumBean.TYPE_GOODS:
                goods(helper, item);
                break;
            case ForumBean.TYPE_TOPIC:
                topic(helper, item);
                break;
            default:
                break;
        }
    }

    private void article(BaseViewHolder helper, final ForumBean item) {
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
                    .bitmapTransform(new CenterCrop(mContext), new RoundCornerTransformation(mContext, 4, RoundCornerTransformation.CornerType.TOP))
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
                        if (itemClickListener != null)
                            itemClickListener.itemClick(item.getForumId(), userId);
                        break;
                    case R.id.iv_account_icon:
                    case R.id.tv_account_name:
                        if (itemClickListener != null)
                            itemClickListener.userClick(item.getUserId());
                        break;
                    case R.id.tv_account_praise:
                        if (itemClickListener != null)
                            itemClickListener.praiseClick(item, new WeakReference(tv_account_praise));
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

    private void event(BaseViewHolder helper, final ForumBean item) {
        Glide.with(mContext)
                .load(item.getImgUrl())
                .crossFade(1000)
                .bitmapTransform(new CenterCrop(mContext), new RoundCornerTransformation(mContext, 4, RoundCornerTransformation.CornerType.TOP))
                .into((ImageView) helper.getView(R.id.iv_event_icon));
        helper.setText(R.id.tv_event_title, item.getActiveName())
                .setText(R.id.tv_event_name, mContext.getString(R.string.app_name))
                .addOnClickListener(R.id.ll_event_wrap);
        Glide.with(mContext)
                .load(R.mipmap.ic_launcher)
                .centerCrop()
                .transform(new GlideCircleTransform(mContext))
                .error(R.mipmap.ic_launcher)
                .into((ImageView) helper.getView(R.id.iv_app_icon));
        helper.getView(R.id.ll_event_wrap).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (itemClickListener != null)
                    itemClickListener.eventClick(item.getActiveUrl(), item.getActiveName());
            }
        });
    }

    private void topic(BaseViewHolder helper, final ForumBean item) {
        List<String> headUrls = item.getTopicUserHeadUrl();
        ImageView iv_topic_icon0 = helper.getView(R.id.iv_topic_icon0);
        ImageView iv_topic_icon1 = helper.getView(R.id.iv_topic_icon1);
        ImageView iv_topic_icon2 = helper.getView(R.id.iv_topic_icon2);
        if (headUrls != null) {
            if (headUrls.size() > 2) {
                iv_topic_icon0.setVisibility(View.VISIBLE);
                iv_topic_icon1.setVisibility(View.VISIBLE);
                iv_topic_icon2.setVisibility(View.VISIBLE);
                Glide.with(mContext)
                        .load(headUrls.get(0))
                        .centerCrop()
                        .transform(new GlideCircleTransform(mContext))
                        .error(R.drawable.mine_icon_head)
                        .into(iv_topic_icon0);
                Glide.with(mContext)
                        .load(headUrls.get(1))
                        .centerCrop()
                        .transform(new GlideCircleTransform(mContext))
                        .error(R.drawable.mine_icon_head)
                        .into(iv_topic_icon1);
                Glide.with(mContext)
                        .load(headUrls.get(2))
                        .centerCrop()
                        .transform(new GlideCircleTransform(mContext))
                        .error(R.drawable.mine_icon_head)
                        .into(iv_topic_icon2);
            } else if (headUrls.size() > 1) {
                iv_topic_icon0.setVisibility(View.VISIBLE);
                iv_topic_icon1.setVisibility(View.VISIBLE);
                iv_topic_icon2.setVisibility(View.GONE);
                Glide.with(mContext)
                        .load(headUrls.get(0))
                        .centerCrop()
                        .transform(new GlideCircleTransform(mContext))
                        .error(R.drawable.mine_icon_head)
                        .into(iv_topic_icon0);
                Glide.with(mContext)
                        .load(headUrls.get(1))
                        .centerCrop()
                        .transform(new GlideCircleTransform(mContext))
                        .error(R.drawable.mine_icon_head)
                        .into(iv_topic_icon1);
            } else if (headUrls.size() > 0) {
                iv_topic_icon0.setVisibility(View.VISIBLE);
                iv_topic_icon1.setVisibility(View.GONE);
                iv_topic_icon2.setVisibility(View.GONE);
                Glide.with(mContext)
                        .load(headUrls.get(0))
                        .centerCrop()
                        .transform(new GlideCircleTransform(mContext))
                        .error(R.drawable.mine_icon_head)
                        .into(iv_topic_icon0);
            } else {
                iv_topic_icon0.setVisibility(View.GONE);
                iv_topic_icon1.setVisibility(View.GONE);
                iv_topic_icon2.setVisibility(View.GONE);
            }
        }
        helper.setText(R.id.tv_topic_title, item.getTopicTitle())
                .setText(R.id.tv_topic_content, item.getTopicContent())
                .setText(R.id.tv_topic_num, item.getOnLookCount() + "人 · 正在围观")
                .addOnClickListener(R.id.ll_topic_wrap);
        helper.getView(R.id.ll_topic_wrap).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (itemClickListener != null)
                    itemClickListener.topicClick(item.getTopicId());
            }
        });
    }

    private void goods(BaseViewHolder helper, ForumBean item) {
        Glide.with(mContext)
                .load(R.mipmap.ic_launcher)
                .centerCrop()
                .transform(new GlideCircleTransform(mContext))
                .into((ImageView) helper.getView(R.id.iv_goods_icon));
        helper.setText(R.id.tv_goods_num, "已帮助" + item.getLoadProductNum() + "人成功下款！")
                .addOnClickListener(R.id.ll_goods_wrap);
        helper.getView(R.id.ll_goods_wrap).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (itemClickListener != null)
                    itemClickListener.goodsClick();
            }
        });
    }

    public void setPraiseState(ForumBean item, TextView tv) {
        Drawable praise = ContextCompat.getDrawable(mContext, R.drawable.ic_praised);
        praise.setBounds(0, 0, praise.getMinimumWidth(), praise.getMinimumHeight());
        Drawable unpraise = ContextCompat.getDrawable(mContext, R.drawable.ic_unpraised);
        unpraise.setBounds(0, 0, unpraise.getMinimumWidth(), unpraise.getMinimumHeight());
        tv.setCompoundDrawables(item.getIsPraise() == 1 ? praise : unpraise, null, null, null);
        tv.setText(item.getPraiseCount() + "");
    }

    private OnSocialItemClickListener itemClickListener;

    public void setOnArticleClickListener(OnSocialItemClickListener clickListener) {
        itemClickListener = clickListener;
    }

    public interface OnSocialItemClickListener {
        void itemClick(String forumId, String userId);

        void userClick(String userId);

        void praiseClick(ForumBean item, WeakReference<TextView> tvRef);

        void eventClick(String url, String name);

        void topicClick(String topicId);

        void goodsClick();
    }
}