package com.beihui.market.view.refreshlayout.manager;

import android.animation.ValueAnimator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.beihui.market.R;
import com.beihui.market.util.CommonUtils;


public class ComRefreshManager extends ComRefreshManagerBase {
    private View refreshContainer;
    private ImageView refreshImage;
    private TextView refreshText;

    private boolean mWaitingRelease;

    private int mTriggerOffset = -1;

    @Override
    protected View createRefreshView(ViewGroup container) {
        View view = LayoutInflater.from(container.getContext())
                .inflate(R.layout.refresh_header, container, false);
        refreshContainer = view.findViewById(R.id.refresh_container);
        refreshImage = view.findViewById(R.id.refresh_image);
        refreshText = view.findViewById(R.id.refresh_text);

        int barHeight = CommonUtils.getStatusBarHeight(container.getContext());
        ViewGroup.LayoutParams lp = refreshContainer.getLayoutParams();
        lp.height += barHeight;
        refreshContainer.setLayoutParams(lp);
        refreshContainer.setPadding(0, barHeight, 0, 0);

        refreshText.setText("下拉刷新");
        return view;
    }

    @Override
    protected void onScroll(int offset) {
        if (mTriggerOffset == -1) {
            mTriggerOffset = getRefreshTriggerDistance();
        }
        if (Math.abs(offset) > mTriggerOffset) {
            if (!mWaitingRelease) {
                refreshText.setText("松手刷新");
                mWaitingRelease = true;
            }
        } else {
            if (mWaitingRelease) {
                refreshText.setText("下拉刷新");
                mWaitingRelease = false;
            }
        }
    }

    private ValueAnimator valueAnimator;

    @Override
    protected void onRefreshing() {
        refreshText.setText("正在刷新");
        valueAnimator = ValueAnimator.ofFloat(0.0f, 360.0f);
        valueAnimator.setDuration(200);
        valueAnimator.setRepeatCount(Integer.MAX_VALUE);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                refreshImage.setRotation((Float) animation.getAnimatedValue());
            }
        });
        valueAnimator.start();
    }

    @Override
    protected void onReset() {
        mWaitingRelease = false;
        refreshText.setText("下拉刷新");
        if (valueAnimator != null && valueAnimator.isRunning()) {
            valueAnimator.cancel();
            valueAnimator = null;
        }
    }

    @Override
    protected int getRefreshTriggerDistance() {
        return refreshContainer.getMeasuredHeight();
    }

    public void updateRefreshContainer(boolean visible) {
        refreshContainer.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

}
