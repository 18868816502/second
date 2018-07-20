package com.beihui.market.tang.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.beihui.market.R;
import com.beihui.market.entity.LoanAccountIconBean;
import com.beihui.market.view.GlideCircleTransform;
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
 * @date: 2018/7/20
 */

public class LoanBillAdapter extends BaseQuickAdapter<LoanAccountIconBean, BaseViewHolder> {
    public LoanBillAdapter() {
        super(R.layout.x_item_account_flow_credit_crad);
        openLoadAnimation(ALPHAIN);
    }

    @Override
    protected void convert(BaseViewHolder helper, LoanAccountIconBean item) {
        Context context = helper.itemView.getContext();
        ImageView ivIcon = helper.getView(R.id.iv_item_credit_card_avatar);
        Glide.with(context).load(item.logo).centerCrop().transform(new GlideCircleTransform(context)).placeholder(R.drawable.mine_bank_default_icon).into(ivIcon);
        helper.setText(R.id.iv_item_credit_card_name, item.iconName);
        helper.addOnClickListener(R.id.iv_item_credit_card_root);
    }
}
