package com.beiwo.klyjaz.loan;

import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.RelativeSizeSpan;
import android.widget.ImageView;

import com.beiwo.klyjaz.R;
import com.beiwo.klyjaz.entity.GroupProductBean;
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
 * @date: 2018/10/10
 */

public class ProductAdapter extends BaseQuickAdapter<GroupProductBean, BaseViewHolder> {
    public ProductAdapter() {
        super(R.layout.layout_product_item);
        openLoadAnimation(ALPHAIN);
    }

    @Override
    protected void convert(BaseViewHolder helper, GroupProductBean item) {
        helper.setText(R.id.tv_product_name, item.getProductName())
                .setText(R.id.tv_product_money, span(item.borrowingLowText.substring(0, item.borrowingLowText.length() - 1) + "-" + item.borrowingHighText))
                .setText(R.id.tv_product_rate, span(item.getInterestLowText()))
                .setText(R.id.tv_product_count, span(item.getSuccessCount() + "人"))
                .setText(R.id.tv_product_rate_type, item.interestTimeText);
        ImageView iv_product_icon = helper.getView(R.id.iv_product_icon);
        Glide.with(mContext)
                .load(item.getLogoUrl())
                .centerCrop()
                .transform(new GlideCircleTransform(mContext))
                .error(R.color.white_7)
                .into(iv_product_icon);
    }

    private CharSequence span(String value) {
        SpannableString string = new SpannableString(value);
        RelativeSizeSpan sizeSpan = new RelativeSizeSpan(0.65f);
        string.setSpan(sizeSpan, value.length() - 1, value.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return string;
    }
}