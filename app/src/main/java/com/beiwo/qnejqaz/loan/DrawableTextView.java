package com.beiwo.qnejqaz.loan;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.beiwo.qnejqaz.R;

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

public class DrawableTextView extends RelativeLayout {

    private TextView textView;
    private ImageView imageView;

    public DrawableTextView(Context context) {
        this(context, null);
    }

    public DrawableTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DrawableTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        View view = LayoutInflater.from(context).inflate(R.layout.layout_drawable_tv, this);
        textView = view.findViewById(R.id.tv_text_txt);
        imageView = view.findViewById(R.id.tv_text_img);
    }

    public void setText(String text) {
        textView.setText(text);
    }

    public void setTextHighLight(boolean highLight) {
        if (highLight) {
            textView.setTypeface(Typeface.DEFAULT_BOLD);
            textView.setTextSize(15f);
        } else {
            textView.setTypeface(Typeface.DEFAULT);
            textView.setTextSize(14f);
        }
    }

    public void setTextColor(int color) {
        textView.setTextColor(ContextCompat.getColor(getContext(), color));
    }

    public void setImg(int res) {
        imageView.setImageResource(res);
    }
}