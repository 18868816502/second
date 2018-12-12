package com.beiwo.klyjaz.goods.adapter;

import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.beiwo.klyjaz.R;
import com.beiwo.klyjaz.entity.HotTop;
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
public class HotTopAdapter extends BaseQuickAdapter<HotTop, BaseViewHolder> {
    public HotTopAdapter() {
        super(R.layout.layout_item_hot_top);
        openLoadAnimation(ALPHAIN);
    }

    @Override
    protected void convert(BaseViewHolder helper, HotTop item) {
        helper.setText(R.id.tv_hot_top_name, item.getProductName())
                .setText(R.id.tv_hot_top_apply, item.getApplyCount() + "申请")
                .setText(R.id.tv_hot_top_download, item.getLoanCount() + "下载")
                .setVisible(R.id.gap_hot_top, helper.getAdapterPosition() == 0);
        ImageView iv_hot_top_rank = helper.getView(R.id.iv_hot_top_rank);
        TextView tv_hot_top_rank = helper.getView(R.id.tv_hot_top_rank);
        TextView tv_hot_top_download = helper.getView(R.id.tv_hot_top_download);
        switch (helper.getAdapterPosition()) {
            case 0:
                tv_hot_top_download.setTextColor(ContextCompat.getColor(mContext, R.color.refresh_one));
                iv_hot_top_rank.setVisibility(View.VISIBLE);
                tv_hot_top_rank.setVisibility(View.GONE);
                iv_hot_top_rank.setImageResource(R.mipmap.ic_rank_top);
                break;
            case 1:
                iv_hot_top_rank.setVisibility(View.VISIBLE);
                tv_hot_top_rank.setVisibility(View.GONE);
                iv_hot_top_rank.setImageResource(R.mipmap.ic_rank1);
                break;
            case 2:
                iv_hot_top_rank.setVisibility(View.VISIBLE);
                tv_hot_top_rank.setVisibility(View.GONE);
                iv_hot_top_rank.setImageResource(R.mipmap.ic_rank2);
                break;
            case 3:
                iv_hot_top_rank.setVisibility(View.VISIBLE);
                tv_hot_top_rank.setVisibility(View.GONE);
                iv_hot_top_rank.setImageResource(R.mipmap.ic_rank3);
                break;
            case 4:
            case 5:
                iv_hot_top_rank.setVisibility(View.GONE);
                tv_hot_top_rank.setVisibility(View.VISIBLE);
                tv_hot_top_rank.setText(helper.getAdapterPosition() + ".");
                break;
            default:
                break;
        }
    }
}