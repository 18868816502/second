package com.beiwo.qnejqaz.goods.adapter;

import android.widget.ImageView;

import com.beiwo.qnejqaz.R;
import com.beiwo.qnejqaz.entity.GoodsManageBean;
import com.beiwo.qnejqaz.view.GlideCircleTransform;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * @author chenguoguo
 * @name loanmarket
 * @class name：com.beiwo.klyjaz.goods.adapter
 * @descripe
 * @time 2018/12/12 13:41
 */
public class GoodsListAdapter extends BaseQuickAdapter<GoodsManageBean.RowsBean, BaseViewHolder> {
    public GoodsListAdapter() {
        super(R.layout.item_goods_list);
    }

    @Override
    protected void convert(BaseViewHolder helper, GoodsManageBean.RowsBean item) {
        Glide.with(mContext)
                .load(item.getLogo())
                .transform(new GlideCircleTransform(mContext))
                .error(R.mipmap.ic_kouzi_default)
                .into((ImageView) helper.getView(R.id.iv_logo));
        helper.setText(R.id.tv_goods_name, item.getName());
        helper.addOnClickListener(R.id.tv_go_comment);
    }

    public void notifyGoodsChanged(List<GoodsManageBean.RowsBean> list) {
        setNewData(list);
    }
}