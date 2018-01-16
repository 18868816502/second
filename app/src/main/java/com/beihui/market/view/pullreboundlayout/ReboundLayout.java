package com.beihui.market.view.pullreboundlayout;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.support.v4.view.NestedScrollingChild;
import android.support.v4.view.NestedScrollingChildHelper;
import android.support.v4.view.NestedScrollingParent;
import android.support.v4.view.NestedScrollingParentHelper;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.AbsListView;

public class ReboundLayout extends ViewGroup implements NestedScrollingChild, NestedScrollingParent {


    private NestedScrollingChildHelper mNestedScrollingChildHelper;
    private NestedScrollingParentHelper mNestedScrollingParentHelper;

    private View mReboundView;
    private View mTargetView;

    private int[] mParentOffsetInWindow = new int[2];
    private int[] mParentConsumed = new int[2];

    private int totalConsumed;
    private float offsetRate = 0.8f;

    public ReboundLayout(Context context) {
        this(context, null);
    }

    public ReboundLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ReboundLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        ViewCompat.setChildrenDrawingOrderEnabled(this, true);

        mNestedScrollingChildHelper = new NestedScrollingChildHelper(this);
        mNestedScrollingParentHelper = new NestedScrollingParentHelper(this);

        setNestedScrollingEnabled(true);
    }

    //region NestedScrollingParent
    @Override
    public boolean onStartNestedScroll(View child, View target, int nestedScrollAxes) {
        return isEnabled() && (nestedScrollAxes & ViewCompat.SCROLL_AXIS_VERTICAL) != 0;
    }

    @Override
    public int getNestedScrollAxes() {
        return mNestedScrollingParentHelper.getNestedScrollAxes();
    }

    @Override
    public void onNestedScrollAccepted(View child, View target, int nestedScrollAxes) {
        mNestedScrollingParentHelper.onNestedScrollAccepted(child, target, nestedScrollAxes);
        startNestedScroll(nestedScrollAxes & ViewCompat.SCROLL_AXIS_VERTICAL);
        totalConsumed = 0;
    }

    @Override
    public void onNestedPreScroll(View target, int dx, int dy, int[] consumed) {
        if (dy > 0 && totalConsumed > 0) {
            if (dy > totalConsumed) {
                consumed[1] = dy - totalConsumed;
                totalConsumed = 0;
            } else {
                totalConsumed -= dy;
                consumed[1] = dy;
            }
            scrollTo(0, -(int) (totalConsumed * offsetRate));
        }

        final int[] parentConsumed = mParentConsumed;
        if (dispatchNestedPreScroll(dx, dy, parentConsumed, null)) {
            consumed[0] += parentConsumed[0];
            consumed[1] += parentConsumed[1];
        }
    }

    @Override
    public void onNestedScroll(View target, int dxConsumed, int dyConsumed,
                               int dxUnconsumed, int dyUnconsumed) {
        dispatchNestedScroll(dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, mParentOffsetInWindow);

        final int dy = dyUnconsumed + mParentOffsetInWindow[1];
        if (dy < 0 && !canTargetScrollUp()) {
            totalConsumed += Math.abs(dy);
            scrollTo(0, -(int) (totalConsumed * offsetRate));
        }
    }

    @Override
    public void onStopNestedScroll(View target) {
        mNestedScrollingParentHelper.onStopNestedScroll(target);
        if (totalConsumed > 0) {
            resetContentPosition();
            totalConsumed = 0;
        }
        stopNestedScroll();
    }

    @Override
    public boolean onNestedFling(View target, float velocityX, float velocityY, boolean consumed) {
        return dispatchNestedFling(velocityX, velocityY, consumed);
    }

    @Override
    public boolean onNestedPreFling(View target, float velocityX, float velocityY) {
        return dispatchNestedPreFling(velocityX, velocityY);
    }
    //endregion

    //region nested scrolling child
    @Override
    public void setNestedScrollingEnabled(boolean enabled) {
        mNestedScrollingChildHelper.setNestedScrollingEnabled(enabled);
    }

    @Override
    public boolean isNestedScrollingEnabled() {
        return mNestedScrollingChildHelper.isNestedScrollingEnabled();
    }

    @Override
    public boolean hasNestedScrollingParent() {
        return mNestedScrollingChildHelper.hasNestedScrollingParent();
    }

    @Override
    public boolean startNestedScroll(int axes) {
        return mNestedScrollingChildHelper.startNestedScroll(axes);
    }

    @Override
    public void stopNestedScroll() {
        mNestedScrollingChildHelper.stopNestedScroll();
    }


    @Override
    public boolean dispatchNestedScroll(int dxConsumed, int dyConsumed, int dxUnconsumed,
                                        int dyUnconsumed, int[] offsetInWindow) {
        return mNestedScrollingChildHelper.dispatchNestedScroll(dxConsumed, dyConsumed,
                dxUnconsumed, dyUnconsumed, offsetInWindow);
    }

    @Override
    public boolean dispatchNestedPreScroll(int dx, int dy, int[] consumed, int[] offsetInWindow) {
        return mNestedScrollingChildHelper.dispatchNestedPreScroll(
                dx, dy, consumed, offsetInWindow);
    }

    @Override
    public boolean dispatchNestedFling(float velocityX, float velocityY, boolean consumed) {
        return mNestedScrollingChildHelper.dispatchNestedFling(velocityX, velocityY, consumed);
    }

    @Override
    public boolean dispatchNestedPreFling(float velocityX, float velocityY) {
        return mNestedScrollingChildHelper.dispatchNestedPreFling(velocityX, velocityY);
    }
    //endregion


    private boolean canTargetScrollUp() {
        return ViewCompat.canScrollVertically(mTargetView, -1);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = getMeasuredWidth();
        int height = getMeasuredHeight();

        ensureTargetAndReboundView();
        mReboundView.measure(MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
        mTargetView.measure(MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY));
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        ensureTargetAndReboundView();
        if (mReboundView != null) {
            mReboundView.layout(0, -mReboundView.getMeasuredHeight(), mReboundView.getMeasuredWidth(), 0);
        }
        if (mTargetView != null) {
            mTargetView.layout(0, 0, mTargetView.getMeasuredWidth(), mTargetView.getMeasuredHeight());
        }
    }


    private void ensureTargetAndReboundView() {
        if (getChildCount() > 0) {
            mReboundView = getChildAt(0);
        }
        if (getChildCount() > 1) {
            mTargetView = getChildAt(1);
        }
    }

    @Override
    public void requestDisallowInterceptTouchEvent(boolean disallowIntercept) {
        if ((android.os.Build.VERSION.SDK_INT < 21 && mTargetView instanceof AbsListView)
                || (mTargetView != null && !ViewCompat.isNestedScrollingEnabled(mTargetView))) {
            // Nope, just to keep motion event passing.
        } else {
            super.requestDisallowInterceptTouchEvent(disallowIntercept);
        }
    }

    private void resetContentPosition() {
        ValueAnimator animator = ValueAnimator.ofInt(getScrollY(), 0);
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                scrollTo(0, 0);
            }
        });
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                scrollTo(0, (Integer) animation.getAnimatedValue());
            }
        });
        animator.setDuration(400);
        animator.setInterpolator(new AccelerateDecelerateInterpolator());
        animator.start();
    }
}
