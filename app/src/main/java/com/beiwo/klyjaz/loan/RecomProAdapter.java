package com.beiwo.klyjaz.loan;

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
 * @date: 2018/10/11
 */

public class RecomProAdapter extends BaseQuickAdapter<Product, BaseViewHolder> {
    public RecomProAdapter() {
        super(R.layout.layout_recom_pro);
        openLoadAnimation(ALPHAIN);
    }

    @Override
    protected void convert(BaseViewHolder helper, Product item) {
        ImageView iv_icon = helper.getView(R.id.iv_icon);
        Glide.with(mContext)
                .load(item.logoUrl)
                .centerCrop()
                .transform(new GlideCircleTransform(mContext))
                .error(R.color.white_7)
                .into(iv_icon);
        helper.setText(R.id.tv_name, item.getProductName())
                .setText(R.id.tv_content, item.borrowingLowText.substring(0, item.borrowingLowText.length() - 1) + "-" + item.borrowingHighText);
    }
}