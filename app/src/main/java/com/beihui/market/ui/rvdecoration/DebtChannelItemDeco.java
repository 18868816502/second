package com.beihui.market.ui.rvdecoration;


import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.beihui.market.ui.adapter.DebtChannelRVAdapter;

public class DebtChannelItemDeco extends RecyclerView.ItemDecoration {

    private int margin = -1;
    private int padding = -1;

    private int gap;

    private Paint bgPaint = new Paint();
    private Paint dividerPaint = new Paint();

    private Rect rect = new Rect();
    private Rect parentRect = new Rect();

    public DebtChannelItemDeco() {
        bgPaint.setColor(Color.WHITE);
        dividerPaint.setColor(Color.parseColor("#e7e7e7"));
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        if (margin == -1) {
            margin = (int) (parent.getResources().getDisplayMetrics().density * 5);
            padding = (int) (parent.getResources().getDisplayMetrics().density * 0.5);

            gap = (int) (parent.getResources().getDisplayMetrics().density * 15);
        }
        //添加了头部，item的范围应该是[1, adapter'itemCount - 1),需要 -1
        int position = parent.getChildAdapterPosition(view) - 1;

        if (position > 0) {
            DebtChannelRVAdapter adapter = (DebtChannelRVAdapter) parent.getAdapter();
            if (adapter.getItem(position).getChannelInitials().equals(adapter.getItem(position - 1).getChannelInitials())) {
                outRect.top = padding;
            }
        }
    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        int childCount = parent.getChildCount();
        for (int i = 1; i < childCount; ++i) {
            View child = parent.getChildAt(i);
            DebtChannelRVAdapter adapter = (DebtChannelRVAdapter) parent.getAdapter();
            //添加了头部，item的范围应该是[1, adapter'itemCount - 1),需要 -1
            int pos = parent.getChildAdapterPosition(child) - 1;
            //noinspection ConstantConditions
            if (pos > 0 && adapter.getItem(pos).getChannelInitials().equals(adapter.getItem(pos - 1).getChannelInitials())) {
                int top = parent.getChildAt(i - 1).getBottom();
                int bottom = child.getTop();
                parentRect.set(0, top, parent.getMeasuredWidth(), bottom);
                rect.set(gap, top, parent.getMeasuredWidth() - gap, bottom);
                c.drawRect(parentRect, bgPaint);
                c.drawRect(rect, dividerPaint);
            }
        }
    }
}
