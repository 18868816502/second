package com.beihui.market.view.jiang;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.RefreshState;
import com.scwang.smartrefresh.layout.constant.SpinnerStyle;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.internal.ArrowDrawable;
import com.scwang.smartrefresh.layout.internal.ProgressDrawable;
import com.scwang.smartrefresh.layout.util.DensityUtil;

/**
 * Copyright: kaola (C)2018
 * FileName: ClassicFooter
 * Author: jiang
 * Create on: 2018/6/29 10:23
 * Description:
 */
public class ClassicFooter extends ClassicsFooter {
    public static String REFRESH_FOOTER_PULLING = null;//"上拉加载更多";
    public static String REFRESH_FOOTER_RELEASE = null;//"释放立即加载";
    public static String REFRESH_FOOTER_LOADING = null;//"正在加载...";
    public static String REFRESH_FOOTER_REFRESHING = null;//"正在刷新...";
    public static String REFRESH_FOOTER_FINISH = null;//"加载完成";
    public static String REFRESH_FOOTER_FAILED = null;//"加载失败";
    public static String REFRESH_FOOTER_NOTHING = null;//"没有更多数据了";

    protected boolean mNoMoreData = false;

    //<editor-fold desc="LinearLayout">
    public ClassicFooter(Context context) {
        this(context, null);
    }

    public ClassicFooter(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ClassicFooter(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        if (REFRESH_FOOTER_PULLING == null) {
            REFRESH_FOOTER_PULLING = context.getString(com.scwang.smartrefresh.layout.R.string.srl_footer_pulling);
        }

        if (REFRESH_FOOTER_RELEASE == null) {
            REFRESH_FOOTER_RELEASE = context.getString(com.scwang.smartrefresh.layout.R.string.srl_footer_release);
        }

        if (REFRESH_FOOTER_LOADING == null) {
            REFRESH_FOOTER_LOADING = context.getString(com.scwang.smartrefresh.layout.R.string.srl_footer_loading);
        }

        if (REFRESH_FOOTER_REFRESHING == null) {
            REFRESH_FOOTER_REFRESHING = context.getString(com.scwang.smartrefresh.layout.R.string.srl_footer_refreshing);
        }

        if (REFRESH_FOOTER_FINISH == null) {
            REFRESH_FOOTER_FINISH = context.getString(com.scwang.smartrefresh.layout.R.string.srl_footer_finish);
        }

        if (REFRESH_FOOTER_FAILED == null) {
            REFRESH_FOOTER_FAILED = context.getString(com.scwang.smartrefresh.layout.R.string.srl_footer_failed);
        }

        if (REFRESH_FOOTER_NOTHING == null) {
            REFRESH_FOOTER_NOTHING = context.getString(com.scwang.smartrefresh.layout.R.string.srl_footer_nothing);
        }

        final View thisView = this;
        final View arrowView = mArrowView;
        final View progressView = mProgressView;
        final DensityUtil density = new DensityUtil();

        mTitleText.setTextColor(0xff666666);
        mTitleText.setText(thisView.isInEditMode() ? REFRESH_FOOTER_LOADING : REFRESH_FOOTER_PULLING);

        TypedArray ta = context.obtainStyledAttributes(attrs, com.scwang.smartrefresh.layout.R.styleable.ClassicsFooter);

        LayoutParams lpArrow = (LayoutParams) arrowView.getLayoutParams();
        LayoutParams lpProgress = (LayoutParams) progressView.getLayoutParams();
        lpProgress.rightMargin = ta.getDimensionPixelSize(com.scwang.smartrefresh.layout.R.styleable.ClassicsFooter_srlDrawableMarginRight, density.dip2px(20));
        lpArrow.rightMargin = lpProgress.rightMargin;

        lpArrow.width = ta.getLayoutDimension(com.scwang.smartrefresh.layout.R.styleable.ClassicsFooter_srlDrawableArrowSize, lpArrow.width);
        lpArrow.height = ta.getLayoutDimension(com.scwang.smartrefresh.layout.R.styleable.ClassicsFooter_srlDrawableArrowSize, lpArrow.height);
        lpProgress.width = ta.getLayoutDimension(com.scwang.smartrefresh.layout.R.styleable.ClassicsFooter_srlDrawableProgressSize, lpProgress.width);
        lpProgress.height = ta.getLayoutDimension(com.scwang.smartrefresh.layout.R.styleable.ClassicsFooter_srlDrawableProgressSize, lpProgress.height);

        lpArrow.width = ta.getLayoutDimension(com.scwang.smartrefresh.layout.R.styleable.ClassicsFooter_srlDrawableSize, lpArrow.width);
        lpArrow.height = ta.getLayoutDimension(com.scwang.smartrefresh.layout.R.styleable.ClassicsFooter_srlDrawableSize, lpArrow.height);
        lpProgress.width = ta.getLayoutDimension(com.scwang.smartrefresh.layout.R.styleable.ClassicsFooter_srlDrawableSize, lpProgress.width);
        lpProgress.height = ta.getLayoutDimension(com.scwang.smartrefresh.layout.R.styleable.ClassicsFooter_srlDrawableSize, lpProgress.height);

        mFinishDuration = ta.getInt(com.scwang.smartrefresh.layout.R.styleable.ClassicsFooter_srlFinishDuration, mFinishDuration);
        mSpinnerStyle = SpinnerStyle.values()[ta.getInt(com.scwang.smartrefresh.layout.R.styleable.ClassicsFooter_srlClassicsSpinnerStyle, mSpinnerStyle.ordinal())];

        if (ta.hasValue(com.scwang.smartrefresh.layout.R.styleable.ClassicsFooter_srlDrawableArrow)) {
            mArrowView.setImageDrawable(ta.getDrawable(com.scwang.smartrefresh.layout.R.styleable.ClassicsFooter_srlDrawableArrow));
        } else {
            mArrowDrawable = new ArrowDrawable();
            mArrowDrawable.setColor(0xff666666);
            mArrowView.setImageDrawable(mArrowDrawable);
        }

        if (ta.hasValue(com.scwang.smartrefresh.layout.R.styleable.ClassicsFooter_srlDrawableProgress)) {
            mProgressView.setImageDrawable(ta.getDrawable(com.scwang.smartrefresh.layout.R.styleable.ClassicsFooter_srlDrawableProgress));
        } else {
            mProgressDrawable = new ProgressDrawable();
            mProgressDrawable.setColor(0xff666666);
            mProgressView.setImageDrawable(mProgressDrawable);
        }

        if (ta.hasValue(com.scwang.smartrefresh.layout.R.styleable.ClassicsFooter_srlTextSizeTitle)) {
            mTitleText.setTextSize(TypedValue.COMPLEX_UNIT_PX, ta.getDimensionPixelSize(com.scwang.smartrefresh.layout.R.styleable.ClassicsFooter_srlTextSizeTitle, DensityUtil.dp2px(12)));
        } else {
            mTitleText.setTextSize(12);
        }

        if (ta.hasValue(com.scwang.smartrefresh.layout.R.styleable.ClassicsFooter_srlPrimaryColor)) {
            setPrimaryColor(ta.getColor(com.scwang.smartrefresh.layout.R.styleable.ClassicsFooter_srlPrimaryColor, 0));
        }
        if (ta.hasValue(com.scwang.smartrefresh.layout.R.styleable.ClassicsFooter_srlAccentColor)) {
            setAccentColor(ta.getColor(com.scwang.smartrefresh.layout.R.styleable.ClassicsFooter_srlAccentColor, 0));
        }
        ta.recycle();
    }

    @Override
    public void onStartAnimator(@NonNull RefreshLayout refreshLayout, int height, int maxDragHeight) {
        if (!mNoMoreData) {
            super.onStartAnimator(refreshLayout, height, maxDragHeight);
        }
    }

    @Override
    public int onFinish(@NonNull RefreshLayout layout, boolean success) {
        if (!mNoMoreData) {
            mTitleText.setText(success ? REFRESH_FOOTER_FINISH : REFRESH_FOOTER_FAILED);
            return super.onFinish(layout, success);
        }
        return 0;
    }

    /**
     * ClassicsFooter 在(SpinnerStyle.FixedBehind)时才有主题色
     */
    @Override
    @Deprecated
    public void setPrimaryColors(@ColorInt int... colors) {
        if (mSpinnerStyle == SpinnerStyle.FixedBehind) {
            super.setPrimaryColors(colors);
        }
    }

    /**
     * 设置数据全部加载完成，将不能再次触发加载功能
     */
    @Override
    public boolean setNoMoreData(boolean noMoreData) {
        if (mNoMoreData != noMoreData) {
            mNoMoreData = noMoreData;
            final View arrowView = mArrowView;
            if (noMoreData) {
                mTitleText.setText(REFRESH_FOOTER_NOTHING);
                arrowView.setVisibility(GONE);
            } else {
                mTitleText.setText(REFRESH_FOOTER_PULLING);
                arrowView.setVisibility(VISIBLE);
            }
        }
        return true;
    }

    @Override
    public void onStateChanged(@NonNull RefreshLayout refreshLayout, @NonNull RefreshState oldState, @NonNull RefreshState newState) {
        final View arrowView = mArrowView;
        if (!mNoMoreData) {
            switch (newState) {
                case None:
                    arrowView.setVisibility(VISIBLE);
                case PullUpToLoad:
                    mTitleText.setText(REFRESH_FOOTER_PULLING);
                    arrowView.animate().rotation(180);
                    break;
                case Loading:
                case LoadReleased:
                    arrowView.setVisibility(GONE);
                    mTitleText.setText(REFRESH_FOOTER_LOADING);
                    break;
                case ReleaseToLoad:
                    mTitleText.setText(REFRESH_FOOTER_RELEASE);
                    arrowView.animate().rotation(0);
                    break;
                case Refreshing:
                    mTitleText.setText(REFRESH_FOOTER_REFRESHING);
                    arrowView.setVisibility(GONE);
                    break;
            }
        }
    }
}