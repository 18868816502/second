package com.beiwo.klyjaz.loan;


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
 * @date: 2018/10/18
 */

public class SubAdatpter extends BaseQuickAdapter<GroupProductBean, BaseViewHolder> {
    public SubAdatpter() {
        super(R.layout.layout_sub2);
        openLoadAnimation(ALPHAIN);
    }

    @Override
    protected void convert(BaseViewHolder helper, GroupProductBean item) {
        ImageView iv_icon = helper.getView(R.id.iv_icon);
        Glide.with(mContext)
                .load(item.getLogoUrl())
                .centerCrop()
                .transform(new GlideCircleTransform(mContext))
                .error(R.color.white_7)
                .into(iv_icon);
        helper.setText(R.id.tv_name, item.getProductName())
                .setText(R.id.tv_num, item.borrowingLowText.substring(0, item.borrowingLowText.length() - 1) + "-" + item.borrowingHighText);
    }
}