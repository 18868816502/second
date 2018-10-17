package com.beiwo.klyjaz.loan;

import android.widget.ImageView;

import com.beiwo.klyjaz.R;
import com.beiwo.klyjaz.entity.GroupProductBean;
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
 * @date: 2018/10/11
 */

public class RecomProAdapter extends BaseQuickAdapter<GroupProductBean, BaseViewHolder> {
    public RecomProAdapter() {
        super(R.layout.layout_recom_pro);
        openLoadAnimation(ALPHAIN);
    }

    @Override
    protected void convert(BaseViewHolder helper, GroupProductBean item) {
        ImageView iv_icon = helper.getView(R.id.iv_icon);
        Glide.with(iv_icon.getContext()).load(item.logoUrl).error(R.color.white).into(iv_icon);
        helper.setText(R.id.tv_name, item.getProductName())
                .setText(R.id.tv_content, item.borrowingLowText.substring(0, item.borrowingLowText.length() - 1) + "-" + item.borrowingHighText);
    }
}