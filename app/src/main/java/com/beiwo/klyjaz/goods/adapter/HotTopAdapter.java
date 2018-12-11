package com.beiwo.klyjaz.goods.adapter;

import com.beiwo.klyjaz.R;
import com.beiwo.klyjaz.entity.Goods;
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
public class HotTopAdapter extends BaseQuickAdapter<Goods, BaseViewHolder> {
    public HotTopAdapter() {
        super(R.layout.layout_item_hot_top);
        openLoadAnimation(ALPHAIN);
    }

    @Override
    protected void convert(BaseViewHolder helper, Goods item) {

    }
}