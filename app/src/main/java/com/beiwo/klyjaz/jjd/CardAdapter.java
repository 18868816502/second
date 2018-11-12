package com.beiwo.klyjaz.jjd;

import android.view.View;
import android.widget.ImageView;

import com.beiwo.klyjaz.R;
import com.beiwo.klyjaz.jjd.bean.BankCard;

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
        int bankBg = -1;
        try {
            bankLogo = BankInfoUtil.bankNameAndLogo(item.getBankName()).get("bankLogo");
            bankBg = BankInfoUtil.bankNameAndLogo(item.getBankName()).get("bg");
        } catch (Exception e) {
        }
        Glide.with(cardIcon.getContext()).load(bankLogo).error(R.mipmap.bank_logo_zhaoshang).transform(new GlideCircleTransform(cardIcon.getContext())).into(cardIcon);
        View ll_card_wrap = helper.getView(R.id.ll_card_wrap);
        switch (bankBg) {
            case 1:
                ll_card_wrap.setBackgroundResource(R.drawable.bg_card1);
                break;
            case 2:
                ll_card_wrap.setBackgroundResource(R.drawable.bg_card2);
                break;
            case 3:
                ll_card_wrap.setBackgroundResource(R.drawable.bg_card3);
                break;
            case 4:
                ll_card_wrap.setBackgroundResource(R.drawable.bg_card4);
                break;
            default:
                ll_card_wrap.setBackgroundResource(R.drawable.bg_card1);
                break;
        }
    }
}