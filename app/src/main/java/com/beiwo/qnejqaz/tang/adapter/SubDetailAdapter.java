package com.beiwo.qnejqaz.tang.adapter;

import com.beiwo.qnejqaz.R;
import com.beiwo.qnejqaz.entity.BillDetail;
import com.beiwo.qnejqaz.util.FormatNumberUtils;
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
 * @date: 2018/7/24
 */

public class SubDetailAdapter extends BaseQuickAdapter<BillDetail, BaseViewHolder> {
    public SubDetailAdapter() {
        super(R.layout.f_layout_sub_detail);
        openLoadAnimation(ALPHAIN);
    }

    @Override
    protected void convert(BaseViewHolder helper, BillDetail item) {
        helper.setText(R.id.tv_sub_description, item.getDiscription())
                .setText(R.id.tv_sub_amount, FormatNumberUtils.FormatNumberFor2(item.getAmountMoney()))
                .setText(R.id.tv_sub_time, item.getTransDate().substring(0, 10).replace("-", "."));
    }
}
