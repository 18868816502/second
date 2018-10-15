package com.beiwo.klyjaz.loan;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.TextSwitcher;
import android.widget.TextView;


import com.beiwo.klyjaz.R;

import java.util.ArrayList;
import java.util.List;

/**
 * https://gitee.com/tangbuzhi
 *
 * @author: Tangbuzhi
 * @version:
 * @package:
 * @description:
 * @modify:
 * @date: 2018/10/9
 */
public class ADTextView extends TextSwitcher {
    private int mInterval = 3000; //文字停留在中间的时长
    private int mSizeCount;//内容数量大小
    private Handler mHandler = new Handler();
    private int mAnimationIn = R.anim.anim_in_default;//进入的动画
    private int mAnimationOut = R.anim.anim_out_default;//出去的动画
    private OnAdChangeListener mChangeListener;//返回Listener
    private Context mContext;
    private int mCurrentIndex = 0;//当前的下表
    private TextView mDefaultTextView;//默认的文字
    private List<String> mTexts = new ArrayList<>();

    public ADTextView(Context context) {
        this(context, null);
    }

    public ADTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
    }

    /*设置进入动画*/
    public ADTextView setAnimationIn(int animationIn) {
        mAnimationIn = animationIn;
        return this;
    }

    /*设置间隔时间*/
    public ADTextView setInterval(int interval) {
        mInterval = interval;
        return this;
    }

    /*设置退出动画*/
    public ADTextView setAnimationOut(int animationOut) {
        mAnimationOut = animationOut;
        return this;
    }

    public void init(List<String> texts, OnAdChangeListener listener) {
        if (texts == null || texts.size() == 0) {
            return;
        }
        mSizeCount = texts.size();
        mTexts.clear();
        mTexts.addAll(texts);
        mChangeListener = listener;

        //设置进入动画
        if (mAnimationIn != -1) {
            setInAnimation(AnimationUtils.loadAnimation(mContext, mAnimationIn));
        }
        //设置出去动画
        if (mAnimationOut != -1) {
            setOutAnimation(AnimationUtils.loadAnimation(mContext, mAnimationOut));
        }
        if (getChildCount() > 0) {
            return;
        }
        //设置Factory
        setFactory(new ViewFactory() {
            @Override
            public View makeView() {
                mDefaultTextView = new TextView(mContext);
                LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                mDefaultTextView.setLayoutParams(layoutParams);
                mDefaultTextView.setTextSize(11);
                mDefaultTextView.setSingleLine();
                mDefaultTextView.setEllipsize(TextUtils.TruncateAt.END);
                mDefaultTextView.setGravity(Gravity.CENTER_VERTICAL);
                mDefaultTextView.setTextColor(Color.BLACK);
                return mDefaultTextView;

            }
        });
        //开始滚动
        //设置文字
        setText("恭喜！" + mTexts.get(mCurrentIndex));
        if (mChangeListener != null)
            mChangeListener.diyTextView((TextView) getCurrentView(), mCurrentIndex);
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //递增
                mCurrentIndex++;
                if (mCurrentIndex >= mSizeCount) {
                    mCurrentIndex = 0;
                }
                //设置i文字
                setText("恭喜！" + mTexts.get(mCurrentIndex));
                if (mChangeListener != null)
                    mChangeListener.diyTextView((TextView) getCurrentView(), mCurrentIndex);
                //进行下一次
                mHandler.postDelayed(this, mInterval);
            }
        }, mInterval);
    }

    public interface OnAdChangeListener {
        void diyTextView(TextView textView, int index);
    }
}