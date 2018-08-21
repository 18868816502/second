package com.beihui.market.tang.adapter;

import android.support.v4.content.ContextCompat;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.view.Gravity;
import android.widget.TextView;

import com.beihui.market.R;
import com.beihui.market.entity.PurseBalance;
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
 * @date: 2018/8/17
 */

public class WalletAdapter extends BaseQuickAdapter<PurseBalance.Amount, BaseViewHolder> {
    public WalletAdapter() {
        super(R.layout.f_layout_wallet_item);
        openLoadAnimation(ALPHAIN);
    }

    @Override
    protected void convert(BaseViewHolder helper, PurseBalance.Amount item) {
        SpannableString amount = new SpannableString(String.format("%.0f元", item.getAmount()));
        AbsoluteSizeSpan span = new AbsoluteSizeSpan(80);
        amount.setSpan(span, 0, amount.length() - 1, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        TextView tv_amount = helper.getView(R.id.tv_amount);
        int color;
        int backgroud;
        if (item.isSelect()) {
            color = ContextCompat.getColor(helper.itemView.getContext(), R.color.white);
            backgroud = R.drawable.bg_item_slelct;
        } else {
            color = ContextCompat.getColor(helper.itemView.getContext(), R.color.black_1);
            backgroud = R.drawable.bg_item_normal;
        }
        tv_amount.setTextColor(color);
        tv_amount.setBackgroundResource(backgroud);
        tv_amount.setGravity(Gravity.CENTER);
        tv_amount.setText(amount);
    }
}