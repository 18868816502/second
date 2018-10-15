package com.beiwo.klyjaz.ui.rvdecoration;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;

public abstract class AccountFlowLoanStickyHeaderItemDeco extends RecyclerView.ItemDecoration {
    private Paint mHeaderTxtPaint;
    private Paint mHeaderContentPaint;

    private int headerHeight;
    private int textPaddingLeft;
    private int textSize;
    private int textColor;
    private int headerContentColor;

    private final float txtYAxis;

    private SparseArray<View> headViewMap = new SparseArray<>();

    public abstract String getHeaderName(int pos);

    public AccountFlowLoanStickyHeaderItemDeco(Context context) {

        float density = context.getResources().getDisplayMetrics().density;
        headerHeight = (int) (density * 35);
        textPaddingLeft = (int) (density * 15);
        textSize = (int) (density * 14);
        textColor = Color.parseColor("#909298");
        /**
         * xhb 修改索引的背景为白色
         */
        //headerContentColor = Color.parseColor("#fafbff");
        headerContentColor = Color.parseColor("#ffffff");


        mHeaderTxtPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mHeaderTxtPaint.setColor(textColor);
        mHeaderTxtPaint.setTextSize(textSize);
        mHeaderTxtPaint.setTextAlign(Paint.Align.LEFT);


        mHeaderContentPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mHeaderContentPaint.setColor(headerContentColor);
        Paint.FontMetrics fontMetrics = mHeaderTxtPaint.getFontMetrics();
        float total = -fontMetrics.ascent + fontMetrics.descent;
        txtYAxis = total / 2 - fontMetrics.descent;
    }


    @Override
    public void getItemOffsets(Rect outRect, View itemView, RecyclerView parent, RecyclerView.State state) {
        //添加了头部，item的范围应该是[1, adapter'itemCount - 1),需要 -1
        int pos = parent.getChildAdapterPosition(itemView);
        if (pos >= 0) {
            String curHeaderName = getHeaderName(pos);
            if (pos == 0) {
                outRect.top = headerHeight;
            } else if (!curHeaderName.equals(getHeaderName(pos - 1))) {
                outRect.top = headerHeight;
            }
        }
    }


    @Override
    public void onDrawOver(Canvas canvas, RecyclerView recyclerView, RecyclerView.State state) {

        int childCount = recyclerView.getChildCount();//获取屏幕上可见的item数量
        int left = recyclerView.getLeft() + recyclerView.getPaddingLeft();
        int right = recyclerView.getRight() - recyclerView.getPaddingRight();

        String firstHeaderName = null;
        //绘制悬浮头部的偏移量
        int translateTop = 0;
        /*for循环里面绘制每个分组的头部*/
        for (int i = 0; i < childCount; i++) {
            View childView = recyclerView.getChildAt(i);
            //添加了头部，item的范围应该是[1, adapter'itemCount - 1),需要 -1
            int pos = recyclerView.getChildAdapterPosition(childView);
            if (pos >= 0) {
                String curHeaderName = getHeaderName(pos);
                if (i == 0) {
                    firstHeaderName = curHeaderName;
                }

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

    public void onDestroy() {
        headViewMap.clear();
    }
}
