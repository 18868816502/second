package com.beiwo.qnejqaz.tang.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;

import com.beiwo.qnejqaz.R;
import com.beiwo.qnejqaz.entity.LoanAccountIconBean;
import com.beiwo.qnejqaz.tang.activity.MakeBillActivity;
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
 * @date: 2018/7/20
 */

public class LoanBillAdapter extends BaseQuickAdapter<LoanAccountIconBean, BaseViewHolder> {
    public LoanBillAdapter() {
        super(R.layout.x_item_account_flow_credit_crad);
        openLoadAnimation(ALPHAIN);
    }

    @Override
    protected void convert(BaseViewHolder helper, final LoanAccountIconBean item) {
        final Context context = helper.itemView.getContext();
        ImageView ivIcon = helper.getView(R.id.iv_item_credit_card_avatar);
        Glide.with(context).load(item.logo).centerCrop().transform(new GlideCircleTransform(context)).placeholder(R.drawable.mine_bank_default_icon).into(ivIcon);
        helper.setText(R.id.iv_item_credit_card_name, item.iconName);
        helper.addOnClickListener(R.id.iv_item_credit_card_root);
        helper.getView(R.id.iv_item_credit_card_root).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoanAccountIconBean iconBean = item;
                Intent intent = new Intent(context, MakeBillActivity.class);
                intent.putExtra("type", MakeBillActivity.TYPE_NET_LOAN);
                intent.putExtra("title", iconBean.iconName);
                intent.putExtra("iconId", iconBean.iconId);
                intent.putExtra("tallyId", iconBean.tallyId);
                context.startActivity(intent);
            }
        });
    }
}