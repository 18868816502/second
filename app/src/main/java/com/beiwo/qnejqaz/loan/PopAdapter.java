package com.beiwo.qnejqaz.loan;

import android.widget.ImageView;

import com.beiwo.qnejqaz.R;
import com.beiwo.qnejqaz.entity.Product;
import com.beiwo.qnejqaz.view.GlideCircleTransform;
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
 * @date: 2018/10/15
 */

public class PopAdapter extends BaseQuickAdapter<Product, BaseViewHolder> {
    public PopAdapter() {
        super(R.layout.layout_pop);
        openLoadAnimation(ALPHAIN);
    }

    @Override
    protected void convert(BaseViewHolder helper, Product item) {
        ImageView iv_icon = helper.getView(R.id.iv_icon);
        Glide.with(mContext)
                .load(item.getLogoUrl())
                .centerCrop()
                .transform(new GlideCircleTransform(mContext))
                .error(R.color.white_7)
                .into(iv_icon);
        helper.setText(R.id.tv_name, item.getProductName());
    }
}