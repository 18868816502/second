package com.beiwo.klyjaz.tang.widget;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.AppCompatTextView;
import android.text.Layout;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.util.AttributeSet;
import android.view.View;

import com.beiwo.klyjaz.R;
import com.beiwo.klyjaz.util.DensityUtil;

/**
 * https://gitee.com/tangbuzhi
 *
 * @author: Tangbuzhi
 * @version:
 * @package:
 * @description:
 * @modify:
 * @date: 2018/12/6
 */
public class ExpandableTextView extends AppCompatTextView {
    private String originText;// 原始内容文本
    private int initWidth = 0;// TextView可展示宽度
    private int mMaxLines = 3;// TextView最大行数
    private SpannableString SPAN_CLOSE = null;// 收起的文案(颜色处理)
    private SpannableString SPAN_EXPAND = null;// 展开的文案(颜色处理)
    private String TEXT_EXPAND = "  展开";
    private String TEXT_CLOSE = "  收起";

    public ExpandableTextView(Context context) {
        this(context, null);
    }

    public ExpandableTextView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ExpandableTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initCloseEnd();
    }

    private void initCloseEnd() {
        String content = TEXT_EXPAND;
        SPAN_CLOSE = new SpannableString(content);
        ButtonSpan span = new ButtonSpan(getContext(), new OnClickListener() {
            @Override
            public void onClick(View v) {
                ExpandableTextView.super.setMaxLines(Integer.MAX_VALUE);
                setExpandText(originText);
                LocalBroadcastManager.getInstance(getContext()).sendBroadcast(new Intent("request_layout"));
            }
        }, R.color.black_2);
        SPAN_CLOSE.setSpan(span, 0, content.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        SPAN_CLOSE.setSpan(new RelativeSizeSpan(0.9f), 0, content.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
    }

    @Override
    public void setMaxLines(int maxLines) {
        this.mMaxLines = maxLines;
        super.setMaxLines(maxLines);
    }

    public void initWidth(int width) {
        initWidth = width;
    }

    public void setCloseText(CharSequence text) {
        if (SPAN_CLOSE == null) initCloseEnd();
        boolean appendShowAll = false;// true 不需要展开收起功能， false 需要展开收起功能
        originText = text.toString();
        // SDK >= 16 可以直接从xml属性获取最大行数
        int maxLines = Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN ?
                getMaxLines() : mMaxLines;
        String workingText = new StringBuilder(originText).toString();
        if (maxLines != -1) {
            Layout layout = createWorkingLayout(workingText);
            if (layout.getLineCount() > maxLines) {
                //获取一行显示字符个数，然后截取字符串数
                workingText = originText.substring(0, layout.getLineEnd(maxLines - 1)).trim();// 收起状态原始文本截取展示的部分
                String showText = originText.substring(0, layout.getLineEnd(maxLines - 1)).trim() + "..." + SPAN_CLOSE;
                Layout layout2 = createWorkingLayout(showText);
                // 对workingText进行-1截取，直到展示行数==最大行数，并且添加 SPAN_CLOSE 后刚好占满最后一行
                while (layout2.getLineCount() > maxLines) {
                    int lastSpace = workingText.length() - 1;
                    if (lastSpace == -1) break;
                    workingText = workingText.substring(0, lastSpace);
                    layout2 = createWorkingLayout(workingText + "..." + SPAN_CLOSE);
                }
                appendShowAll = true;
                workingText = workingText + "...";
            }
        }

        setText(workingText);
        if (appendShowAll) {// 必须使用append，不能在上面使用+连接，否则spannable会无效
            append(SPAN_CLOSE);
            setMovementMethod(LinkMovementMethod.getInstance());
        }
    }

    public void setExpandText(String text) {
        if (SPAN_EXPAND == null) initExpandEnd();
        Layout layout1 = createWorkingLayout(text);
        Layout layout2 = createWorkingLayout(text + TEXT_CLOSE);
        // 展示全部原始内容时 如果 TEXT_CLOSE 需要换行才能显示完整，则直接将TEXT_CLOSE展示在下一行
        if (layout2.getLineCount() > layout1.getLineCount()) {
            setText(originText + "\n");
        } else {
            setText(originText);
        }
        append(SPAN_EXPAND);
        setMovementMethod(LinkMovementMethod.getInstance());
    }

    private void initExpandEnd() {
        String content = TEXT_CLOSE;
        SPAN_EXPAND = new SpannableString(content);
        ButtonSpan span = new ButtonSpan(getContext(), new OnClickListener() {
            @Override
            public void onClick(View v) {
                ExpandableTextView.super.setMaxLines(mMaxLines);
                setCloseText(originText);
                LocalBroadcastManager.getInstance(getContext()).sendBroadcast(new Intent("request_layout"));
            }
        }, R.color.black_2);
        SPAN_EXPAND.setSpan(span, 0, content.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        SPAN_EXPAND.setSpan(new RelativeSizeSpan(0.9f), 0, content.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
    }

    //返回textview的显示区域的layout，该textview的layout并不会显示出来，只是用其宽度来比较要显示的文字是否过长
    private Layout createWorkingLayout(String workingText) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            return new StaticLayout(workingText, getPaint(), initWidth - getPaddingLeft() - getPaddingRight(),
                    Layout.Alignment.ALIGN_NORMAL, getLineSpacingMultiplier(), getLineSpacingExtra(), false);
        } else {
            return new StaticLayout(workingText, getPaint(), initWidth - getPaddingLeft() - getPaddingRight(),
                    Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
        }
    }

    public class ButtonSpan extends ClickableSpan {
        View.OnClickListener onClickListener;
        private Context context;
        private int colorId;

        public ButtonSpan(Context context, View.OnClickListener onClickListener, int colorId) {
            this.onClickListener = onClickListener;
            this.context = context;
            this.colorId = colorId;
        }

        @Override
        public void updateDrawState(TextPaint ds) {
            ds.setColor(context.getResources().getColor(colorId));
            ds.setTextSize(DensityUtil.dp2px(context, 12));
            ds.setUnderlineText(false);
        }

        @Override
        public void onClick(View widget) {
            if (onClickListener != null) onClickListener.onClick(widget);
        }
    }
}