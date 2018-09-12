package com.beihui.market.view.pulltoswipe;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;


/**
 * *      ┏┓　　　┏┓
 * *    ┏┛┻━━━┛┻┓
 * *    ┃　　　　　　　┃
 * *    ┃　　　━　　　┃
 * *    ┃　┳┛　┗┳　┃
 * *    ┃　　　　　　　┃
 * *    ┃　　　┻　　　┃
 * *    ┃　　　　　　　┃
 * *    ┗━┓　　　┏━┛
 * *       ┃　　　┃   神兽保佑
 * *       ┃　　　┃   代码无BUG！
 * *       ┃　　　┗━━━┓
 * *       ┃　　　　　　　┣┓
 * *       ┃　　　　　　　┏┛
 * *       ┗┓┓┏━┳┓┏┛━━━━━┛
 * *         ┃┫┫　┃┫┫
 * *         ┗┻┛　┗┻┛
 * *
 * Created by opq on 2017/9/22.
 * 可拉刷新的RecyclerView
 */

public class PulledTabAccountRecyclerView extends RecyclerView implements Pulled {

    public PulledTabAccountRecyclerView(Context context) {
        super(context);
    }

    public PulledTabAccountRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public PulledTabAccountRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean canPullDown() {
        return false;
    }

    @Override
    public boolean canPullUp() {
        if (!canPullUp) {
            return false;
        }

        if (getLayoutManager().getChildCount() == 0) {
            // 没有item的时候也可以上拉加载
            return true;
        }

        // 滑到底部了
        if (computeVerticalScrollExtent() + computeVerticalScrollOffset() >= computeVerticalScrollRange()) {
            return true;
        }

        return false;
    }

    public boolean canPullUp = true;

    public void setCanPullUp(boolean canPullUp) {
        this.canPullUp = canPullUp;
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (onItemScrollChanged != null) {
            onItemScrollChanged.onScrollChanged();
        }
    }

    public OnItemScrollChanged onItemScrollChanged;

    public void setOnItemScrollChanged(OnItemScrollChanged onItemScrollChanged) {
        this.onItemScrollChanged = onItemScrollChanged;
    }

    public interface OnItemScrollChanged{
        void onScrollChanged();
    }
}