package com.beiwo.klyjaz.view.floatbutton;

/**
 * @author chenguoguo
 * @name ClipPhotoDemo
 * @class name：com.beiwo.klyjaz.view.floatbutton
 * @descripe
 * @time 2018/12/10 18:00
 */

import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.RelativeLayout;

/**
 * 可拖动悬浮吸附按钮抽象类
 */
public abstract class BaseDragFloatActionButton extends RelativeLayout {
    /**
     * 悬浮的父布局高度
     */
    private int parentHeight;
    /**
     * 悬浮的父布局宽度
     */
    private int parentWidth;

    public BaseDragFloatActionButton(Context context) {
        this(context, null, 0);
    }

    public BaseDragFloatActionButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);

    }

    /**
     * 获取布局文件
     * @return
     */
    public abstract int getLayoutId();

    /**
     * 初始化那些布局
     * @param view
     */
    public abstract void initView(View view);

    public BaseDragFloatActionButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        View view = LayoutInflater.from(context).inflate(getLayoutId(), this);
        initView(view);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        measureChildren(widthMeasureSpec, heightMeasureSpec);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean b, int i, int i1, int i2, int i3) {
        View view = getChildAt(0);
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
    }

    private int lastX;
    private int lastY;
    private boolean isDrag;
    private long downTime;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int rawX = (int) event.getRawX();
        int rawY = (int) event.getRawY();
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                downTime=System.currentTimeMillis();
                //默认是点击事件
                setPressed(true);
                //默认是非拖动而是点击事件
                isDrag = false;
                //父布局不要拦截子布局的监听
                getParent().requestDisallowInterceptTouchEvent(true);
                lastX = rawX;
                lastY = rawY;
                ViewGroup parent;
                if (getParent() != null) {
                    parent = (ViewGroup) getParent();
                    parentHeight = parent.getHeight();
                    parentWidth = parent.getWidth();
                }
                break;
            case MotionEvent.ACTION_MOVE:
                //只有父布局存在你才可以拖动
                isDrag = (parentHeight > 0 && parentWidth > 0);
                if (!isDrag) break;

                int dx = rawX - lastX;
                int dy = rawY - lastY;
                //这里修复一些华为手机无法触发点击事件
                int distance = (int) Math.sqrt(dx * dx + dy * dy);
                //当位移大于0说明拖动了
                isDrag = distance > 0;
                if (!isDrag) break;

                float x = getX() + dx;
                float y = getY() + dy;
                //检测是否到达边缘 左上右下
                x = x < 0 ? 0 : x > parentWidth - getWidth() ? parentWidth - getWidth() : x;
                y = y < 0 ? 0 : y > parentHeight - getHeight() ? parentHeight - getHeight() : y;
//                setX(x);
                setY(y);
                lastX = rawX;
                lastY = rawY;
                break;
            case MotionEvent.ACTION_UP:
                //如果是拖动状态下即非点击按压事件
                if(System.currentTimeMillis()-downTime<600){
                    setPressed(true);
                }else {
                    setPressed(!isDrag);
                }
//                if (rawX >= parentWidth / 2) {
//                    //靠右吸附
//                    animate().setInterpolator(new DecelerateInterpolator())
//                            .setDuration(500)
//                            .xBy(parentWidth - getWidth() - getX())
//                            .start();
//                } else {
//                    //靠左吸附
//                    ObjectAnimator oa = ObjectAnimator.ofFloat(this, "x", getX(), 0);
//                    oa.setInterpolator(new DecelerateInterpolator());
//                    oa.setDuration(500);
//                    oa.start();
//                }

                break;
            default:
                break;
        }

        //如果不是拖拽，那么就不消费这个事件，以免影响点击事件的处理
        //拖拽事件要自己消费
        return isDrag || super.onTouchEvent(event);
    }

}
