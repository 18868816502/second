package com.beihui.market.ui.rvdecoration;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class MessageCenterItemDeco extends RecyclerView.ItemDecoration {

    private int mDividerHeight;
    private int padding;

    private Paint mPaint = new Paint();
    private Rect mDrawRect = new Rect();

    public MessageCenterItemDeco(Context context) {
        float density = context.getResources().getDisplayMetrics().density;
        mDividerHeight = (int) (density * 0.5);
        padding = (int) (density * 7);

        mPaint.setColor(Color.parseColor("#f0f0f0"));
    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        for (int i = 0; i < parent.getChildCount(); ++i) {
            View child = parent.getChildAt(i);
            if (parent.getChildAdapterPosition(child) >= 2) {
                int childTop = child.getTop();
                int childWidth = child.getMeasuredWidth();
                mDrawRect.set(padding, childTop - mDividerHeight, childWidth - padding, childTop);
                c.drawRect(mDrawRect, mPaint);
            }
        }
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        if (parent.getChildAdapterPosition(view) >= 2) {
            outRect.set(0, mDividerHeight, 0, 0);
        }
    }
}
