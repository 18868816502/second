package com.beihui.market.loan;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.beihui.market.R;

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

    public void setImg(int res) {
        imageView.setImageResource(res);
    }
}