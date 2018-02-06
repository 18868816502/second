package com.beihui.market.view;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

public class AlphabetIndexBar extends View {

    private static final String[] ALPHABET = {
            "#",
            "A", "B", "C", "D", "E", "F", "G",
            "H", "I", "J", "K", "L", "M", "N",
            "O", "P", "Q", "R", "S", "T",
            "U", "V", "W", "X", "Y", "Z"};

    public interface AlphabetSelectedListener {
        void onAlphabetSelected(int index, String alphabet);
    }

    private AlphabetSelectedListener listener;

    private Rect[] areas = new Rect[ALPHABET.length + 1];
    private TextPaint alphabetPaint = new TextPaint();
    private TextPaint selectedTextPaint = new TextPaint();
    private Paint selectedBgPaint = new Paint();
    private int alphabetPadding;
    private int alphabetSize;

    private int selectedIndex = -1;

    private GestureDetector gestureDetector;


    public AlphabetIndexBar(Context context) {
        this(context, null);
    }

    public AlphabetIndexBar(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AlphabetIndexBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        alphabetPadding = (int) (getResources().getDisplayMetrics().density * 10);

        alphabetPaint.setColor(Color.parseColor("#5c92ff"));
        alphabetPaint.setTextSize(8 * getResources().getDisplayMetrics().density);
        alphabetPaint.setTextAlign(Paint.Align.CENTER);
        alphabetPaint.setAntiAlias(true);
        alphabetPaint.setTypeface(Typeface.DEFAULT_BOLD);

        selectedTextPaint.setColor(Color.WHITE);
        selectedTextPaint.setTextSize(8 * getResources().getDisplayMetrics().density);
        selectedTextPaint.setTextAlign(Paint.Align.CENTER);
        selectedTextPaint.setAntiAlias(true);
        selectedTextPaint.setTypeface(Typeface.DEFAULT_BOLD);

        selectedBgPaint.setAntiAlias(true);
        selectedBgPaint.setColor(Color.parseColor("#5c92ff"));

        Paint.FontMetricsInt fontMetricsInt = new Paint.FontMetricsInt();
        alphabetPaint.getFontMetricsInt(fontMetricsInt);
        alphabetSize = fontMetricsInt.descent - fontMetricsInt.ascent;


        for (int i = 0; i < areas.length; ++i) {
            areas[i] = new Rect();
        }

        gestureDetector = new GestureDetector(getContext(), new GestureDetector.SimpleOnGestureListener() {

            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                selectedIndex = calSelectedIndex(e);
                if (listener != null) {
                    listener.onAlphabetSelected(selectedIndex, ALPHABET[selectedIndex]);
                }
                invalidate();
                return true;
            }

            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                int selected = calSelectedIndex(e2);
                if (selected != selectedIndex) {
                    selectedIndex = selected;
                    if (listener != null) {
                        listener.onAlphabetSelected(selectedIndex, ALPHABET[selectedIndex]);
                    }
                    invalidate();
                }
                return true;
            }
        });
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        if (widthMode != MeasureSpec.EXACTLY) {
            width = alphabetSize + getPaddingLeft() + getPaddingRight() + alphabetPadding;
        }
        if (heightMode != MeasureSpec.EXACTLY) {
            height = ALPHABET.length * alphabetSize + (ALPHABET.length + 2) * alphabetPadding;
        }
        setMeasuredDimension(width, height);

        width = getMeasuredWidth();
        int paddingLeft = getPaddingLeft();
        int paddingRight = getPaddingRight();
        int top = 0;
        for (int i = 0; i < ALPHABET.length; ++i) {
            int bottom = top + alphabetPadding + alphabetSize;
            areas[i].set(paddingLeft, top, width - paddingRight, bottom);
            top = bottom;
        }
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (int i = 0; i < ALPHABET.length; ++i) {
            final Rect rect = areas[i];
            if (i == selectedIndex) {
                canvas.drawCircle(rect.centerX(), rect.centerY(), rect.height() / 2.f, selectedBgPaint);
                canvas.drawText(ALPHABET[i], rect.centerX(), rect.centerY() + selectedTextPaint.getTextSize() / 2.f, selectedTextPaint);
            } else {
                canvas.drawText(ALPHABET[i], rect.centerX(), rect.centerY() + alphabetPaint.getTextSize() / 2.f, alphabetPaint);
            }

        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        gestureDetector.onTouchEvent(event);
        return true;
    }

    public void setAlphabetSelectedListener(AlphabetSelectedListener listener) {
        this.listener = listener;
    }

    private int calSelectedIndex(MotionEvent event) {
        int index = (int) (event.getY() / areas[0].height());
        if (index < 0) {
            index = 0;
        }
        if (index >= ALPHABET.length) {
            index = ALPHABET.length - 1;
        }
        return index;
    }
}
