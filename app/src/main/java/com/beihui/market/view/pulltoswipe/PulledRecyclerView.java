package com.beihui.market.view.pulltoswipe;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;

import butterknife.OnTouch;


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

public class PulledRecyclerView extends RecyclerView implements Pulled {


    public PulledRecyclerView(Context context) {
        super(context);
    }

    public PulledRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public PulledRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean canPullDown() {
        if (getLayoutManager().getChildCount()== 0) {
            // 没有item的时候也可以下拉刷新
            return true;
        } else if (((LinearLayoutManager)getLayoutManager()).findFirstVisibleItemPosition() == 0 && getChildAt(0).getTop() >= 0) {
            // 滑到ListView的顶部了
            return true;
        } else
            return false;
    }

    @Override
    public boolean canPullUp() {
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
