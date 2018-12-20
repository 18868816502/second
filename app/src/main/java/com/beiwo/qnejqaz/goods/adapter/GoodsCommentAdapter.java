package com.beiwo.qnejqaz.goods.adapter;

import android.graphics.Color;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.beiwo.qnejqaz.R;
import com.beiwo.qnejqaz.entity.GoodsComment;
import com.beiwo.qnejqaz.entity.Labels;
import com.beiwo.qnejqaz.goods.TagUtil;
import com.beiwo.qnejqaz.util.DensityUtil;
import com.beiwo.qnejqaz.view.GlideCircleTransform;
import com.beiwo.qnejqaz.view.flowlayout.FlowLayout;
import com.beiwo.qnejqaz.view.flowlayout.TagAdapter;
import com.beiwo.qnejqaz.view.flowlayout.TagFlowLayout;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.ArrayList;
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
                .setText(R.id.comment_tv_type, types[(item.getType() + 1) % 3])
                .setText(R.id.comment_tv_content, item.getContent());
        ImageView comment_iv_type = helper.getView(R.id.comment_iv_type);
        comment_iv_type.setImageResource(imgs[(item.getType() + 1) % 3]);
        //标签
        TagFlowLayout comment_tfl_tag = helper.getView(R.id.comment_tfl_tag);
        List<Labels> labels = item.getLabelList();
        if (labels != null && labels.size() > 0) {
            List<String> tags = new ArrayList<>();
            for (int i = 0; i < labels.size(); i++) {
                String flag = labels.get(i).getFlag();
                if (TextUtils.isEmpty(flag)) continue;
                tags.add(TagUtil.getKeyValue(flag, false));
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
        if (!TextUtils.isEmpty(imageUrl)) {
            LinearLayout comment_pic_container = helper.getView(R.id.comment_pic_container);
            if (imageUrl.contains(",")) {
                String[] urls = imageUrl.split(",");
                if (urls != null && urls.length > 0) {
                    for (String url : urls) comment_pic_container.addView(initImg(url));
                }
            } else {
                comment_pic_container.addView(initImg(imageUrl));
            }
        }
    }

    private ImageView initImg(String item) {
        ImageView imgView = new ImageView(mContext);
        imgView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(DensityUtil.dp2px(mContext, 65), DensityUtil.dp2px(mContext, 65));
        params.rightMargin = DensityUtil.dp2px(mContext, 4);
        imgView.setLayoutParams(params);
        Glide.with(mContext).load(item).into(imgView);
        return imgView;
    }
}