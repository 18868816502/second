package com.beiwo.klyjaz.goods.adapter;

import android.graphics.Color;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.beiwo.klyjaz.R;
import com.beiwo.klyjaz.entity.GoodsComment;
import com.beiwo.klyjaz.entity.Labels;
import com.beiwo.klyjaz.goods.TagUtil;
import com.beiwo.klyjaz.util.DensityUtil;
import com.beiwo.klyjaz.view.GlideCircleTransform;
import com.beiwo.klyjaz.view.flowlayout.FlowLayout;
import com.beiwo.klyjaz.view.flowlayout.TagAdapter;
import com.beiwo.klyjaz.view.flowlayout.TagFlowLayout;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * https://gitee.com/tangbuzhi
 *
 * @author: Tangbuzhi
 * @version:
 * @package:
 * @description:
 * @modify:
 * @date: 2018/12/13
 */
public class GoodsCommentAdapter extends BaseQuickAdapter<GoodsComment, BaseViewHolder> {
    private CommentImgAdapter imgAdapter = new CommentImgAdapter();

    public GoodsCommentAdapter() {
        super(R.layout.layout_goods_comment_item);
        openLoadAnimation(ALPHAIN);
    }

    @Override
    protected void convert(BaseViewHolder helper, GoodsComment item) {
        String[] types = {"差评", "中评", "好评"};
        int[] imgs = {R.mipmap.ic_comment_bad, R.mipmap.ic_comment_mid, R.mipmap.ic_comment_good};
        Glide.with(mContext).load(item.getHeadUrl())
                .transform(new GlideCircleTransform(mContext))
                .into((ImageView) helper.getView(R.id.comment_iv_avator));
        helper.setText(R.id.comment_tv_name, item.getUserName())
                .setText(R.id.comment_tv_time, item.getShowText())
                .setText(R.id.comment_tv_type, types[(item.getStatus() + 1) % 3])
                .setText(R.id.comment_tv_content, item.getContent());
        ImageView comment_iv_type = helper.getView(R.id.comment_iv_type);
        comment_iv_type.setImageResource(imgs[(item.getStatus() + 1) % 3]);
        //标签
        TagFlowLayout comment_tfl_tag = helper.getView(R.id.comment_tfl_tag);
        List<Labels> labels = item.getLabelList();
        if (labels != null && labels.size() > 0) {
            final List<String> tags = new ArrayList<>();
            for (int i = 0; i < labels.size(); i++) {
                tags.add(TagUtil.getKeyValue(labels.get(i).getFlag(), false));
            }
            comment_tfl_tag.setAdapter(new TagAdapter<String>(tags) {
                @Override
                public View getView(FlowLayout parent, int position, String str) {
                    TextView textView = new TextView(mContext);
                    textView.setPadding(DensityUtil.dp2px(mContext, 5), DensityUtil.dp2px(mContext, 2), DensityUtil.dp2px(mContext, 5), DensityUtil.dp2px(mContext, 2));
                    textView.setGravity(Gravity.CENTER);
                    textView.setBackgroundResource(R.drawable.bg_comment_tag);
                    textView.setTextColor(Color.parseColor("#f08175"));
                    textView.setTextSize(11);
                    textView.setText(str);
                    return textView;
                }
            });
        }
        //评价图片
        String imageUrl = item.getImageUrl();
        System.out.println("imageUrl = " + imageUrl);
        if (!TextUtils.isEmpty(imageUrl)) {
            String[] urls = imageUrl.split(",");
            if (urls != null && urls.length > 0) {
                List<String> images = Arrays.asList(urls);
                RecyclerView comment_pic_recycler = helper.getView(R.id.comment_pic_recycler);
                LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
                layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
                comment_pic_recycler.setLayoutManager(layoutManager);
                comment_pic_recycler.setItemAnimator(new DefaultItemAnimator());
                comment_pic_recycler.setHasFixedSize(true);
                comment_pic_recycler.setAdapter(imgAdapter);
                imgAdapter.setNewData(images);
            }
        }
    }
}