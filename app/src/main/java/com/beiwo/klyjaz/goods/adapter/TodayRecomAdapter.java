package com.beiwo.klyjaz.goods.adapter;

import android.widget.ImageView;

import com.beiwo.klyjaz.R;
import com.beiwo.klyjaz.entity.Product;
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
public class TodayRecomAdapter extends BaseQuickAdapter<Product, BaseViewHolder> {
    public TodayRecomAdapter() {
        super(R.layout.layout_item_today_recom);
        openLoadAnimation(ALPHAIN);
    }

    @Override
    protected void convert(BaseViewHolder helper, Product item) {
        Glide.with(mContext).load(item.getLogoUrl())
                .transform(new GlideCircleTransform(mContext))
                .into((ImageView) helper.getView(R.id.iv_today_recom_icon));
        helper.setText(R.id.tv_today_recom_name, item.getProductName())
                .setText(R.id.tv_today_recom_range, item.borrowingLowText.substring(0, item.borrowingLowText.length() - 1) + "-" + item.borrowingHighText);
    }
}