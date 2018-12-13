package com.beiwo.klyjaz.goods.adapter;

import android.widget.ImageView;

import com.beiwo.klyjaz.R;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

/**
 * https://gitee.com/tangbuzhi
 *
 * @author: Tangbuzhi
 * @version:
 * @package:
 * @description:口子详情的评价里图片组
 * @modify:
 * @date: 2018/12/13
 */
public class CommentImgAdapter extends BaseQuickAdapter<String, BaseViewHolder> {
    public CommentImgAdapter() {
        super(R.layout.layout_comment_img);
        openLoadAnimation(ALPHAIN);
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
        Glide.with(mContext).load(item).into((ImageView) helper.getView(R.id.comment_iv_pic));
    }
}