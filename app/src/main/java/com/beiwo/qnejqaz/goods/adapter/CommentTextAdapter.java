package com.beiwo.qnejqaz.goods.adapter;

import com.beiwo.qnejqaz.R;
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
 * @date: 2018/12/12
 */
public class CommentTextAdapter extends BaseQuickAdapter<String, BaseViewHolder> {
    public CommentTextAdapter() {
        super(R.layout.layout_comment_box);
        openLoadAnimation(ALPHAIN);
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
        helper.setText(R.id.tv_comment_type, item);
    }
}