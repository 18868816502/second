package com.beiwo.klyjaz.goods.adapter;

import android.widget.ImageView;

import com.beiwo.klyjaz.R;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

/**
 * @author chenguoguo
 * @name loanmarket_social
 * @class name：com.beiwo.klyjaz.social.adapter
 * @descripe
 * @time 2018/12/11 15:57
 */
public class GoodsEvaluateAdapter extends BaseQuickAdapter<String,BaseViewHolder> {


    public GoodsEvaluateAdapter() {
        super(R.layout.item_goods_comment_photo);
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
        Glide.with(mContext).load(item).into((ImageView) helper.getView(R.id.iv_photo));
        helper.addOnClickListener(R.id.iv_delete);
    }
}
