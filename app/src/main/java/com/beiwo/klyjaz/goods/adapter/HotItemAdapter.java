package com.beiwo.klyjaz.goods.adapter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;

import com.beiwo.klyjaz.R;
import com.beiwo.klyjaz.entity.Goods;
import com.beiwo.klyjaz.view.GlideCircleTransform;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

/**
 * https://gitee.com/tangbuzhi
 *
 * @author: Tangbuzhi
 * @version:
 * @package:
 * @description:
 * @modify:
 * @date: 2018/12/11
 */
public class HotItemAdapter extends BaseQuickAdapter<Goods, BaseViewHolder> {
    public HotItemAdapter() {
        super(R.layout.layout_hot_goods_item);
        openLoadAnimation(ALPHAIN);
    }

    @Override
    protected void convert(BaseViewHolder helper, Goods item) {
        Glide.with(mContext).load(item.getLogo())
                .transform(new GlideCircleTransform(mContext))
                .into((ImageView) helper.getView(R.id.hot_item_iv_icon));
        helper.setText(R.id.hot_item_tv_name, item.getName())
                .setText(R.id.hot_item_tv_rate, String.format("%.1f", item.getGoodCommentRate() / 10))
                .setText(R.id.hot_item_tv_comment_num, item.getGoodCommentCount() + "条好评");
        RatingBar hot_item_rb_progress = helper.getView(R.id.hot_item_rb_progress);

        try {//解决ratingbar流泪问题
            Bitmap bmp = BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.ic_stared);
            int scroeHeight = bmp.getHeight();
            ViewGroup.LayoutParams params = hot_item_rb_progress.getLayoutParams();
            params.width = -2;
            params.height = scroeHeight;
            hot_item_rb_progress.setLayoutParams(params);
            hot_item_rb_progress.setRating(item.getGoodCommentRate() / 20);
        } catch (Exception e) {
            Bitmap bmp = BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.ic_unstar);
            int scroeHeight = bmp.getHeight();
            ViewGroup.LayoutParams params = hot_item_rb_progress.getLayoutParams();
            params.width = -2;
            params.height = scroeHeight;
            hot_item_rb_progress.setLayoutParams(params);
            hot_item_rb_progress.setRating(0);
        }
    }
}