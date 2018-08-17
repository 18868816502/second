package com.beihui.market.tang;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * https://gitee.com/tangbuzhi
 *
 * @author: Tangbuzhi
 * @version:
 * @package:
 * @description:
 * @modify:
 * @date: 2018/8/17
 */

public class Decoration extends RecyclerView.ItemDecoration {
    private int itemSpace;
    private int spanSize;

    public Decoration(int itemSpace, int spanSize) {
        this.itemSpace = itemSpace;
        this.spanSize = spanSize;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        outRect.left = itemSpace;
        outRect.top = itemSpace;
        if ((parent.getChildLayoutPosition(view) + 1) % spanSize == 0) {
            outRect.right = itemSpace;
        }
    }
}
