package com.beihui.market.util;

import android.text.TextUtils;
import android.view.ViewTreeObserver;
import android.widget.TextView;

public class OnGlobalLayoutListenerByEllipSize implements ViewTreeObserver.OnGlobalLayoutListener {
    private TextView mTextView;
    private int mMaxLines;  //最大行数

    public OnGlobalLayoutListenerByEllipSize(TextView textView, int maxLines) {
        if (maxLines <= 0)
            throw new IllegalArgumentException("maxLines不能小于等于0");
        this.mTextView = textView;
        this.mMaxLines = maxLines;
        this.mTextView.setMaxLines(mMaxLines + 1);
        this.mTextView.setSingleLine(false);
    }

    @Override
    public void onGlobalLayout() {
        if (mTextView.getLineCount() > mMaxLines) {
            int line = mTextView.getLayout().getLineEnd(mMaxLines - 1);
            CharSequence truncate = "...";//定义成CharSequence类型，是为了兼容emoji表情，如果使用String类型则会造成emoji无法显示
            CharSequence text = mTextView.getText();
            try {
                text = text.subSequence(0, line - 3);
            } catch (Exception e) {
                truncate = "";
                text = mTextView.getText();
            }
            TextUtils.TruncateAt at = mTextView.getEllipsize();
            if (at == TextUtils.TruncateAt.START) {
                mTextView.setText(truncate);
                mTextView.append(text);
            } else if (at == TextUtils.TruncateAt.MIDDLE) {
                mTextView.setText(text.subSequence(0, text.length() / 2));
                mTextView.append(truncate);
                mTextView.append(text.subSequence(text.length() / 2, text.length()));
            } else {
                mTextView.setText(text);
                mTextView.append(truncate);
            }
        }
    }
}