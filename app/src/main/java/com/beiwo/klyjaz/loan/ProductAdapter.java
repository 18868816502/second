package com.beiwo.klyjaz.loan;

import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.RelativeSizeSpan;
import android.widget.ImageView;

import com.beiwo.klyjaz.R;
import com.beiwo.klyjaz.entity.GroupProductBean;
import com.beiwo.klyjaz.util.FormatNumberUtils;
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
        String min = item.borrowingLowText.substring(0, item.borrowingLowText.length() - 1);
        String max = item.borrowingHighText.substring(0, item.borrowingHighText.length() - 1);
        String lowText, highText;
        int low = 0, high = 0;
        if (strIsNum(min)) low = Integer.valueOf(min);
        if (strIsNum(max)) high = Integer.valueOf(max);
        if (low != 0) {
            lowText = FormatNumberUtils.FormatNumberFor0(low);
        } else {
            lowText = min;
        }
        if (high != 0) {
            highText = FormatNumberUtils.FormatNumberFor0(high);
        } else {
            highText = max;
        }
        helper.setText(R.id.tv_product_name, item.getProductName())
                .setText(R.id.tv_product_money, span(lowText + "-" + highText + "元"))
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

    private boolean strIsNum(String str) {
        for (int i = 0; i < str.length(); i++) {
            if (!Character.isDigit(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }
}