package com.beihui.market.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.AppCompatEditText;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnFocusChangeListener;

import com.beihui.market.R;
import com.beihui.market.util.ToastUtil;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class ClearEditText extends AppCompatEditText implements OnFocusChangeListener {

    private final Pattern emoji = Pattern.compile("[\ud83c\udc00-\ud83c\udfff]|[\ud83d\udc00-\ud83d\udfff]|[\u2600-\u27ff]",
            Pattern.UNICODE_CASE | Pattern.CASE_INSENSITIVE);
    private Drawable mClearDrawable;
    private InputFilter emojiFilter = new InputFilter() {
        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            Matcher emojiMatcher = emoji.matcher(source);
            if (emojiMatcher.find()) {
                ToastUtil.toast("不支持输入Emoji表情符号");
                return "";
            }
            return null;
        }
    };

    public ClearEditText(Context context) {
        this(context, null);
    }

    public ClearEditText(Context context, AttributeSet attrs) {
        this(context, attrs, android.R.attr.editTextStyle);
    }

    public ClearEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        // 获取EditText的DrawableRight,假如没有设置我们就使用默认的图片
        mClearDrawable = getCompoundDrawables()[2];
        if (mClearDrawable == null) {
            mClearDrawable =
                    getResources().getDrawable(R.mipmap.btn_cleanup);
        }
        mClearDrawable.setBounds(0, 0, mClearDrawable.getIntrinsicWidth(),
                mClearDrawable.getIntrinsicHeight());
        setClearIconVisible(false);
        setOnFocusChangeListener(this);
        setFilters(new InputFilter[]{emojiFilter});
    }

    /**
     * 因为我们不能直接给EditText设置点击事件，所以我们用记住我们按下的位置来模拟点击事件 当我们按下的位置 在 EditText的宽度 -
     * 图标到控件右边的间距 - 图标的宽度 和 EditText的宽度 - 图标到控件右边的间距之间我们就算点击了图标，竖直方向没有考虑
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (getCompoundDrawables()[2] != null) {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                boolean touchable =
                        event.getX() > (getWidth() - getPaddingRight() - mClearDrawable
                                .getIntrinsicWidth())
                                && (event.getX() < ((getWidth() - getPaddingRight())));
                if (touchable) {
                    this.getEditableText().clear();
                }
            }
        }
        return super.onTouchEvent(event);
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus) {
            setClearIconVisible(getText().length() > 0);
        } else {
            setClearIconVisible(false);
        }
    }

    public void setMaxLenght(int max) {
        setFilters(new InputFilter[]{emojiFilter, new InputFilter.LengthFilter(max)});
    }

    public void setClearIconVisible(boolean visible) {
        Drawable right = visible ? mClearDrawable : null;
        setCompoundDrawables(getCompoundDrawables()[0], getCompoundDrawables()[1], right,
                getCompoundDrawables()[3]);
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int count, int after) {
        setClearIconVisible(s.length() > 0 && hasFocus());
    }
}