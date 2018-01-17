package com.beihui.market.ui.rvdecoration;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.beihui.market.ui.adapter.AllDebtRVAdapter;

public abstract class AllDebtStickyHeaderItemDeco extends RecyclerView.ItemDecoration {

    private Paint mHeaderTxtPaint;
    private Paint mHeaderContentPaint;

    private int headerHeight;
    private int textPaddingLeft;

    private final float txtYAxis;

    public abstract String getHeaderName(int pos);

    protected AllDebtStickyHeaderItemDeco(Context context) {
        float density = context.getResources().getDisplayMetrics().density;
        headerHeight = (int) (density * 35);
        textPaddingLeft = (int) (density * 15);

        mHeaderTxtPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mHeaderTxtPaint.setColor(Color.parseColor("#909298"));
        mHeaderTxtPaint.setTextSize((int) (density * 14));
        mHeaderTxtPaint.setTextAlign(Paint.Align.LEFT);


        mHeaderContentPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mHeaderContentPaint.setColor(Color.parseColor("#fafbff"));
        Paint.FontMetrics fontMetrics = mHeaderTxtPaint.getFontMetrics();
        float total = -fontMetrics.ascent + fontMetrics.descent;
        txtYAxis = total / 2 - fontMetrics.descent;
    }


    @Override
    public void getItemOffsets(Rect outRect, View itemView, RecyclerView parent, RecyclerView.State state) {
        int pos = parent.getChildAdapterPosition(itemView);

        AllDebtRVAdapter adapter = (AllDebtRVAdapter) parent.getAdapter();
        if (pos == 0) {
            outRect.top = headerHeight;
        } else if (pos < adapter.getDataSetCount()) {
            String curHeaderName = getHeaderName(pos);
            if (!curHeaderName.equals(getHeaderName(pos - 1))) {
                outRect.top = headerHeight;
            }
        }

    }


    @Override
    public void onDrawOver(Canvas canvas, RecyclerView recyclerView, RecyclerView.State state) {
        int childCount = recyclerView.getChildCount();
        int left = recyclerView.getLeft() + recyclerView.getPaddingLeft();
        int right = recyclerView.getRight() - recyclerView.getPaddingRight();

        AllDebtRVAdapter adapter = (AllDebtRVAdapter) recyclerView.getAdapter();

        String firstHeaderName = null;
        int translateTop = 0;//绘制悬浮头部的偏移量
        /*for循环里面绘制每个分组的头部*/
        for (int i = 0; i < childCount; i++) {
            View childView = recyclerView.getChildAt(i);
            int pos = recyclerView.getChildAdapterPosition(childView);
            if (pos < adapter.getDataSetCount()) {
                String curHeaderName = getHeaderName(pos);
                if (i == 0) {
                    firstHeaderName = curHeaderName;
                }
                if (curHeaderName == null)
                    continue;

                int viewTop = childView.getTop() + recyclerView.getPaddingTop();
                if (pos == 0 || !curHeaderName.equals(getHeaderName(pos - 1))) {
                    canvas.drawRect(left, viewTop - headerHeight, right, viewTop, mHeaderContentPaint);
                    canvas.drawText(curHeaderName, left + textPaddingLeft, viewTop - headerHeight / 2 + txtYAxis, mHeaderTxtPaint);
                    if (headerHeight < viewTop && viewTop <= 2 * headerHeight) { //此判断是刚好2个头部碰撞，悬浮头部就要偏移
                        translateTop = viewTop - 2 * headerHeight;
                    }
                }
            }
        }
        if (firstHeaderName == null)
            return;

        canvas.save();
        canvas.translate(0, translateTop);

        /*绘制悬浮的头部*/
        canvas.drawRect(left, 0, right, headerHeight, mHeaderContentPaint);
        canvas.drawText(firstHeaderName, left + textPaddingLeft, headerHeight / 2 + txtYAxis, mHeaderTxtPaint);
        canvas.restore();
    }
}
