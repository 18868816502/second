package com.beiwo.klyjaz.goods.adapter;

import android.widget.ImageView;

import com.beiwo.klyjaz.R;
import com.beiwo.klyjaz.entity.Goods;
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
 * @date: 2018/12/11
 */
public class GoodsItemAdapter extends BaseQuickAdapter<Goods, BaseViewHolder> {
    public GoodsItemAdapter() {
        super(R.layout.layout_item_goods);
        openLoadAnimation(ALPHAIN);
    }

    @Override
    protected void convert(BaseViewHolder helper, Goods item) {
        Glide.with(mContext).load(item.getLogo())
                .transform(new GlideCircleTransform(mContext))
                .into((ImageView) helper.getView(R.id.good_iv_icon));
        helper.setText(R.id.good_tv_name, item.getName())
                .setText(R.id.good_tv_comment_num, String.format(mContext.getString(R.string.loan_comment_num), item.getCommentCount()))
                .setText(R.id.good_tv_range_money, String.format(mContext.getString(R.string.limit_loan), item.getMinQuota(), item.getMaxQuota()))
                .setText(R.id.good_tv_period, String.format(mContext.getString(R.string.period_loan), item.getTerm()))
                .setText(R.id.good_tv_comment_rate, String.format("%.0f", item.getGoodCommentRate()));
    }
}