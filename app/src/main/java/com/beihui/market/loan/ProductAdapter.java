package com.beihui.market.loan;

import android.widget.ImageView;

import com.beihui.market.R;
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
 * @date: 2018/10/10
 */

public class ProductAdapter extends BaseQuickAdapter<Product, BaseViewHolder> {
    public ProductAdapter() {
        super(R.layout.layout_product_item);
        openLoadAnimation(ALPHAIN);
    }

    @Override
    protected void convert(BaseViewHolder helper, Product item) {
        helper.setText(R.id.tv_product_name, item.getProductName())
                .setText(R.id.tv_product_money, item.getBorrowingHighText())
                .setText(R.id.tv_product_rate, item.getInterestLowText())
                .setText(R.id.tv_product_count, item.getSuccessCount() + "");
        ImageView iv_product_icon = helper.getView(R.id.iv_product_icon);
        Glide.with(iv_product_icon.getContext()).load(item.getLogoUrl()).error(R.color.white).into(iv_product_icon);
    }
}