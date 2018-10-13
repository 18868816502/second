package com.beihui.market.jjd;

import android.widget.ImageView;

import com.beihui.market.R;
import com.beihui.market.jjd.bean.BankCard;

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
 * @date: 2018/9/14
 */

public class CardAdapter extends BaseQuickAdapter<BankCard, BaseViewHolder> {
    public CardAdapter() {
        super(R.layout.vest_layout_card_item);
        openLoadAnimation(ALPHAIN);
    }

    @Override
    protected void convert(BaseViewHolder helper, BankCard item) {
        helper.setText(R.id.tv_card_name, item.getBankName());
        helper.setText(R.id.tv_card_type, "储蓄卡");
        helper.setText(R.id.tv_card_suffix_num, item.getBankCardno().substring(item.getBankCardno().length() - 4));
        ImageView cardIcon = helper.getView(R.id.iv_card_icon);
        Integer bankLogo = null;
        try {
            bankLogo = BankInfoUtil.bankNameAndLogo(item.getBankName()).get("bankLogo");
        } catch (Exception e) {
        }
        Glide.with(cardIcon.getContext()).load(bankLogo).error(R.mipmap.bank_logo_zhaoshang).transform(new GlideCircleTransform(cardIcon.getContext())).into(cardIcon);
    }
}