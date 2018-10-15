package com.beiwo.klyjaz.jjd;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;


import com.beiwo.klyjaz.R;
import com.beiwo.klyjaz.util.DensityUtil;

import static android.graphics.Paint.Style.STROKE;

/**
 * @author chenguoguo
 * @name loanmarket
 * @class name：com.beihui.market.view
 * @class describe 自定义圆环进度条
 * @time
 */
public class CircleProgressBar extends View {

    /**
     * 进度颜色
     */
    private int outsideColor;
    /**
     * 外圆半径大小
     */
    private float outsideRadius;
    /**
     * 背景颜色
     */
    private int insideColor;
    /**
     * 圆环内文字颜色
     */
    private int progressTextColor;
    /**
     * 圆环内文字大小
     */
    private float progressTextSize;
    /**
     * 圆环的宽度
     */
    private float progressWidth;
    /**
     * 最大进度
     */
    private int maxProgress;
    /**
     * 当前进度
     */
    private float progress;
    /**
     * 进度从哪里开始(设置了4个值,上左下右)
     */
    private int direction;
    /**
     * 圆环内文字
     */
    private String progressText;

    private Paint paint;
    private Rect rect;

    private ValueAnimator animator;

    enum DirectionEnum {
        /**
         * 进度条开始的地方（左侧）
         */
        LEFT(0, 180.0f),
        /**
         * 顶部
         */
        TOP(1, 270.0f),
        /**
         * 右侧
         */
        RIGHT(2, 0.0f),
        /**
         * 底部
         */
        BOTTOM(3, 90.0f);

        private final int direction;
        private final float degree;

        DirectionEnum(int direction, float degree) {
            this.direction = direction;
            this.degree = degree;
        }

        public int getDirection() {
            return direction;
        }

        public float getDegree() {
            return degree;
        }

        public boolean equalsDescription(int direction) {
            return this.direction == direction;
        }

        public static DirectionEnum getDirection(int direction) {
            for (DirectionEnum enumObject : values()) {
                if (enumObject.equalsDescription(direction)) {
                    return enumObject;
                }
            }
            return RIGHT;
        }

        public static float getDegree(int direction) {
            DirectionEnum enumObject = getDirection(direction);
            if (enumObject == null) {
                return 0;
            }
            return enumObject.getDegree();
        }
    }

    public CircleProgressBar(Context context) {
        this(context, null);
    }

    public CircleProgressBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.CircleProgressBar, defStyleAttr, 0);
        outsideColor = a.getColor(R.styleable.CircleProgressBar_outside_color, ContextCompat.getColor(getContext(), R.color.c_3586ff));
        outsideRadius = a.getDimension(R.styleable.CircleProgressBar_outside_radius, DensityUtil.dp2px(getContext(), 60.0f));
        insideColor = a.getColor(R.styleable.CircleProgressBar_inside_color, ContextCompat.getColor(getContext(), R.color.colorBackground));
        progressTextColor = a.getColor(R.styleable.CircleProgressBar_progress_text_color, ContextCompat.getColor(getContext(), R.color.colorPrimary));
        progressTextSize = a.getDimension(R.styleable.CircleProgressBar_progress_text_size, DensityUtil.dp2px(getContext(), 14.0f));
        progressWidth = a.getDimension(R.styleable.CircleProgressBar_progress_width, DensityUtil.dp2px(getContext(), 10.0f));
        progress = a.getFloat(R.styleable.CircleProgressBar_progress, 50.0f);
        maxProgress = a.getInt(R.styleable.CircleProgressBar_max_progress, 100);
        direction = a.getInt(R.styleable.CircleProgressBar_direction, 3);

        a.recycle();

        paint = new Paint();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int circlePoint = getWidth() / 2;
        //第一步:画背景(即内层圆)
        //设置圆的颜色
        paint.setColor(insideColor);
        //设置空心
        paint.setStyle(STROKE);
        //设置圆的宽度
        paint.setStrokeWidth(progressWidth);
        //消除锯齿
        paint.setAntiAlias(true);
        //画出圆
        canvas.drawCircle(circlePoint, circlePoint, outsideRadius, paint);

        //第二步:画进度(圆弧)
        //设置进度的颜色
        paint.setColor(outsideColor);
        //用于定义的圆弧的形状和大小的界限
        RectF oval = new RectF(circlePoint - outsideRadius, circlePoint - outsideRadius, circlePoint + outsideRadius, circlePoint + outsideRadius);
        //根据进度画圆弧
        canvas.drawArc(oval, DirectionEnum.getDegree(direction), 360 * (progress / maxProgress), false, paint);

        //第三步:画圆环内百分比文字
        rect = new Rect();
        paint.setColor(progressTextColor);
        paint.setTextSize(progressTextSize);
        paint.setStrokeWidth(0);
        progressText = getProgressText();
        paint.getTextBounds(progressText, 0, progressText.length(), rect);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width;
        int height;
        int size = MeasureSpec.getSize(widthMeasureSpec);
        int mode = MeasureSpec.getMode(widthMeasureSpec);

        if (mode == MeasureSpec.EXACTLY) {
            width = size;
        } else {
            width = (int) ((2 * outsideRadius) + progressWidth);
        }
        size = MeasureSpec.getSize(heightMeasureSpec);
        mode = MeasureSpec.getMode(heightMeasureSpec);
        if (mode == MeasureSpec.EXACTLY) {
            height = size;
        } else {
            height = (int) ((2 * outsideRadius) + progressWidth);
        }
        setMeasuredDimension(width, height);
    }

    /**
     * 中间的进度百分比
     *
     * @return 百分比string
     */
    private String getProgressText() {
        return (int) ((progress / maxProgress) * 100) + "%";
    }

    public int getOutsideColor() {
        return outsideColor;
    }

    public void setOutsideColor(int outsideColor) {
        this.outsideColor = outsideColor;
    }

    public float getOutsideRadius() {
        return outsideRadius;
    }

    public void setOutsideRadius(float outsideRadius) {
        this.outsideRadius = outsideRadius;
    }

    public int getInsideColor() {
        return insideColor;
    }

    public void setInsideColor(int insideColor) {
        this.insideColor = insideColor;
    }

    public int getProgressTextColor() {
        return progressTextColor;
    }

    public void setProgressTextColor(int progressTextColor) {
        this.progressTextColor = progressTextColor;
    }

    public float getProgressTextSize() {
        return progressTextSize;
    }

    public void setProgressTextSize(float progressTextSize) {
        this.progressTextSize = progressTextSize;
    }

    public float getProgressWidth() {
        return progressWidth;
    }

    public void setProgressWidth(float progressWidth) {
        this.progressWidth = progressWidth;
    }

    public synchronized int getMaxProgress() {
        return maxProgress;
    }

    public synchronized void setMaxProgress(int maxProgress) {
        if (maxProgress < 0) {
            //此为传递非法参数异常
            throw new IllegalArgumentException("maxProgress should not be less than 0");
        }
        this.maxProgress = maxProgress;
    }

    public synchronized float getProgress() {
        return progress;
    }

    //加锁保证线程安全,能在线程中使用
    public synchronized void setProgress(int progress) {
        if (progress < 0) {
            throw new IllegalArgumentException("progress should not be less than 0");
        }
        if (progress > maxProgress) {
            progress = maxProgress;
        }
        startAnim(progress);
    }

    /**
     * 启动动画，设置3秒时长
     *
     * @param startProgress
     */
    private void startAnim(float startProgress) {
        animator = ObjectAnimator.ofFloat(0, startProgress);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                CircleProgressBar.this.progress = (float) animation.getAnimatedValue();
                postInvalidate();
            }
        });
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                listener.onFinished();
            }

            @Override
            public void onAnimationCancel(Animator animation) {
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
            }
        });
        animator.setStartDelay(500);
        animator.setDuration(3000);
        animator.setInterpolator(new LinearInterpolator());
        animator.start();
    }

    private OnAnimatorFinishedListener listener;

    public void setOnAnimatorFinishedListener(OnAnimatorFinishedListener listener) {
        this.listener = listener;
    }

    public interface OnAnimatorFinishedListener {
        /**
         * 动画播放完成回调
         */
        void onFinished();
    }
}