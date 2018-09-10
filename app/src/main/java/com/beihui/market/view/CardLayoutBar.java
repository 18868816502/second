package com.beihui.market.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.beihui.market.R;
import com.beihui.market.util.Px2DpUtils;

/**
 * @author wubo on 2018/6/5 10:51
 *         类说明：选项栏
 */
public class CardLayoutBar extends RelativeLayout {
    /**
     * leftImage 左边图片
     * rightImage 右边图片
     */
    private ImageView leftImage, rightImage;


    private Context context;

    /**
     * leftTextView 左边第一个文字
     * rightTextView1 右边第二个文字
     * rightTextView2 右边第一个文字
     */
    private TextView leftTextView,tvLeftRemark, leftTextDownView, rightTextView1, rightTextView2;

    /**
     * view 底部横线
     */
    private View view;

    /**
     * left_image 左边资源图片地址
     * right_image 右边资源图片地址
     * left_textView_color 左边文字颜色
     * right_textView1_color 右边第二个文字颜色
     * right_textView2_color 右边第一个文字颜色
     * view_margin_right 线条相对与右边距离
     */
    private int left_image, right_image, left_textView_color, left_down_color, right_textView1_color,
            right_textView2_color, view_margin_right;

    /**
     * left_textView_size 左边文字大小
     * right_textView1_size 右边第二个文字大小
     * right_textView2_size 右边第一个文字大小
     */
    private int left_textView_size, left_down_size, right_textView1_size, right_textView2_size;

    /**
     * left_textView_text 左边文字内容
     * right_textView1_text 右边第二个文字内容
     * right_textView2_text 右边第一个文字内容
     */
    private String left_textView_text, left_down_text, right_textView1_text, right_textView2_text;

    /**
     * left_image_Visible 左边图片是否显示
     * right_image_Visible 右边图片是否显示
     * left_textView_Visible 左边文字是否显示
     * right_textView1_Visible 右边第二个文字是否显示
     * right_textView2_Visible 右边第一个文字是否显示
     * view_Visible 底部横线是否显示
     */
    private boolean left_image_Visible,left_remark_Visible, right_image_Visible, left_textView_Visible, left_down_Visible,
            right_textView1_Visible, right_textView2_Visible, view_Visible;

    private boolean left_textView_style, left_down_style;

    public CardLayoutBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        LayoutInflater.from(context).inflate(R.layout.public_cardlayout_bar, this, true);

        leftImage = findViewById(R.id.left_image);
        rightImage = findViewById(R.id.right_image);
        leftTextView = findViewById(R.id.left_text);
        tvLeftRemark = findViewById(R.id.left_remark);

        leftTextDownView = findViewById(R.id.left_text_down);
        rightTextView1 = findViewById(R.id.right_text1);
        rightTextView2 = findViewById(R.id.right_text2);
        view = findViewById(R.id.view);

        /**获取属性值*/
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.RelativeLayoutBar);

        left_image = typedArray.getResourceId(R.styleable.RelativeLayoutBar_left_image, R.color.transparent);
        right_image = typedArray.getResourceId(R.styleable.RelativeLayoutBar_right_image, R.drawable.icon_come);

        left_textView_color = typedArray.getColor(R.styleable.RelativeLayoutBar_left_textView_color, Color.WHITE);
//        left_textView_remark_color = typedArray.getColor(R.styleable.RelativeLayoutBar_left_textView_color, Color.WHITE);
        left_down_color = typedArray.getColor(R.styleable.RelativeLayoutBar_left_down_color, Color.WHITE);
        right_textView1_color = typedArray.getColor(R.styleable.RelativeLayoutBar_right_textView1_color, Color.WHITE);
        right_textView2_color = typedArray.getColor(R.styleable.RelativeLayoutBar_right_textView2_color, Color.WHITE);

        left_textView_size = typedArray.getDimensionPixelSize(R.styleable.RelativeLayoutBar_left_textView_textSize, 14);
        left_down_size = typedArray.getDimensionPixelSize(R.styleable.RelativeLayoutBar_left_down_textSize, 14);
        right_textView1_size = typedArray.getDimensionPixelSize(R.styleable.RelativeLayoutBar_right_textView1_textSize, 14);
        right_textView2_size = typedArray.getDimensionPixelSize(R.styleable.RelativeLayoutBar_right_textView2_textSize, 14);

        left_textView_text = typedArray.getString(R.styleable.RelativeLayoutBar_left_textView_text);
        left_down_text = typedArray.getString(R.styleable.RelativeLayoutBar_left_down_text);
        right_textView1_text = typedArray.getString(R.styleable.RelativeLayoutBar_right_textView1_text);
        right_textView2_text = typedArray.getString(R.styleable.RelativeLayoutBar_right_textView2_text);

        left_image_Visible = typedArray.getBoolean(R.styleable.RelativeLayoutBar_left_image_Visible, false);
        left_remark_Visible = typedArray.getBoolean(R.styleable.RelativeLayoutBar_left_textView_remark_Visible, false);
        right_image_Visible = typedArray.getBoolean(R.styleable.RelativeLayoutBar_right_image_Visible, true);
        left_textView_Visible = typedArray.getBoolean(R.styleable.RelativeLayoutBar_left_textView_Visible, false);
        left_down_Visible = typedArray.getBoolean(R.styleable.RelativeLayoutBar_left_down_Visible, false);
        right_textView1_Visible = typedArray.getBoolean(R.styleable.RelativeLayoutBar_right_textView1_Visible, false);
        right_textView2_Visible = typedArray.getBoolean(R.styleable.RelativeLayoutBar_right_textView2_Visible, false);
        view_Visible = typedArray.getBoolean(R.styleable.RelativeLayoutBar_view_Visible, false);
        view_margin_right = typedArray.getInteger(R.styleable.RelativeLayoutBar_view_margin_right, 0);
        left_textView_style = typedArray.getBoolean(R.styleable.RelativeLayoutBar_left_textView_style, false);
        left_down_style = typedArray.getBoolean(R.styleable.RelativeLayoutBar_left_down_style, false);

        /**设置值**/
        setLeftImage(left_image);
        setRightImage(right_image);
        setLeftTextViewColor(left_textView_color);
        setRightTextView1Color(right_textView1_color);
        setRightTextView2Color(right_textView2_color);
        setLeftTextViewTextSize(left_textView_size);
        setRightTextView1TextSize(right_textView1_size);
        setRightTextView2TextSize(right_textView2_size);
        setLeftTextViewText(left_textView_text);
        setRightTextView1Text(right_textView1_text);
        setRightTextView2Text(right_textView2_text);
        setLeftImageVisible(left_image_Visible);
        setLeftRemarkVisible(left_remark_Visible);
        setRightImageVisible(right_image_Visible);
        setLeftTextViewVisible(left_textView_Visible);
        setRightTextView1Visible(right_textView1_Visible);
        setRightTextView2Visible(right_textView2_Visible);
        setViewVisible(view_Visible, view_margin_right);
        setTextViewIsBold(left_textView_style);

        leftTextDownView.setText(left_down_text);
        leftTextDownView.setTextColor(left_down_color);
        leftTextDownView.setTextSize(Px2DpUtils.px2dp(context, left_down_size));
        leftTextDownView.setVisibility(left_down_Visible ? VISIBLE : GONE);
        leftTextDownView.setTypeface(left_down_style ? Typeface.DEFAULT_BOLD : Typeface.DEFAULT);
    }

    public void setTextViewIsBold(boolean isBold) {
        if (isBold) {
            leftTextView.setTypeface(Typeface.DEFAULT_BOLD);
        }
    }

    /**
     * 设置左边图片边距
     *
     * @param l 左边距
     * @param t 上边距
     * @param r 右边距
     * @param b 底部边距
     */
    public void setLeftImageMargins(int l, int t, int r, int b) {
        if (leftImage.getLayoutParams() instanceof MarginLayoutParams) {
            MarginLayoutParams p = (MarginLayoutParams) leftImage.getLayoutParams();
            p.setMargins(l, t, r, b);
            leftImage.requestLayout();
        }
    }

    /**
     * 设置右边图片边距
     *
     * @param l 左边距
     * @param t 上边距
     * @param r 右边距
     * @param b 底部边距
     */
    public void setRightImageMargins(int l, int t, int r, int b) {
        if (rightImage.getLayoutParams() instanceof MarginLayoutParams) {
            MarginLayoutParams p = (MarginLayoutParams) rightImage.getLayoutParams();
            p.setMargins(l, t, r, b);
            rightImage.requestLayout();
        }
    }

    /**
     * 设置左边文字边距
     *
     * @param l 左边距
     * @param t 上边距
     * @param r 右边距
     * @param b 底部边距
     */
    public void setLeftTextViewMargins(int l, int t, int r, int b) {
        if (leftTextView.getLayoutParams() instanceof MarginLayoutParams) {
            MarginLayoutParams p = (MarginLayoutParams) leftTextView.getLayoutParams();
            p.setMargins(l, t, r, b);
            leftTextView.requestLayout();
        }
    }

    /**
     * 设置右边第二个文字边距
     *
     * @param l 左边距
     * @param t 上边距
     * @param r 右边距
     * @param b 底部边距
     */
    public void setRightTextView1Margins(int l, int t, int r, int b) {
        if (rightTextView1.getLayoutParams() instanceof MarginLayoutParams) {
            MarginLayoutParams p = (MarginLayoutParams) rightTextView1.getLayoutParams();
            p.setMargins(l, t, r, b);
            rightTextView1.requestLayout();
        }
    }

    /**
     * 设置右边第一个文字边距
     *
     * @param l 左边距
     * @param t 上边距
     * @param r 右边距
     * @param b 底部边距
     */
    public void setRightTextView2Margins(int l, int t, int r, int b) {
        if (rightTextView2.getLayoutParams() instanceof MarginLayoutParams) {
            MarginLayoutParams p = (MarginLayoutParams) rightTextView2.getLayoutParams();
            p.setMargins(l, t, r, b);
            rightTextView2.requestLayout();
        }
    }

    public ImageView getLeftImage() {
        return leftImage;
    }

    /**
     * 设置左边图片
     *
     * @param left_image
     */
    public void setLeftImage(int left_image) {
        leftImage.setImageResource(left_image);
    }

    /**
     * 设置右边图片
     *
     * @param right_image
     */
    public void setRightImage(int right_image) {
        rightImage.setImageResource(right_image);
    }

    /**
     * 设置左边文字颜色
     *
     * @param left_textView_color
     */
    public void setLeftTextViewColor(int left_textView_color) {
        leftTextView.setTextColor(left_textView_color);
    }

    public void setLeftImageSize(int w, int h) {
        LayoutParams layout = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layout.width = w;
        layout.height = h;
        leftImage.setLayoutParams(layout);
    }

    /**
     * 设置右边第二个文字颜色
     *
     * @param right_textView1_color
     */
    public void setRightTextView1Color(int right_textView1_color) {
        rightTextView1.setTextColor(right_textView1_color);
    }

    /**
     * 设置右边第一个文字颜色
     *
     * @param right_textView2_color
     */
    public void setRightTextView2Color(int right_textView2_color) {
        rightTextView2.setTextColor(right_textView2_color);
    }

    /**
     * 设置左边文字大小
     *
     * @param left_textView_size
     */
    public void setLeftTextViewTextSize(int left_textView_size) {
        leftTextView.setTextSize(Px2DpUtils.px2dp(context, left_textView_size));
    }

    /**
     * 设置右边第二个文字大小
     *
     * @param right_textView1_size
     */
    public void setRightTextView1TextSize(int right_textView1_size) {
        rightTextView1.setTextSize(Px2DpUtils.px2dp(context, right_textView1_size));
    }

    /**
     * 设置右边第一个文字大小
     *
     * @param right_textView2_size
     */
    public void setRightTextView2TextSize(int right_textView2_size) {
        rightTextView2.setTextSize(Px2DpUtils.px2dp(context, right_textView2_size));
    }

    /**
     * 设置左边文字内容
     *
     * @param left_textView_text
     */
    public void setLeftTextViewText(String left_textView_text) {
        leftTextView.setText(left_textView_text);
    }

    /**
     * 设置右边第二个文字内容
     *
     * @param right_textView1_text
     */
    public void setRightTextView1Text(String right_textView1_text) {
        rightTextView1.setText(right_textView1_text);
    }

    /**
     * 设置右边第一个文字内容
     *
     * @param right_textView2_text
     */
    public void setRightTextView2Text(String right_textView2_text) {
        rightTextView2.setText(right_textView2_text);
    }

    /**
     * 设置左边图片是否显示
     *
     * @param left_image_Visible
     */
    public void setLeftImageVisible(boolean left_image_Visible) {
        leftImage.setVisibility(left_image_Visible ? View.VISIBLE : View.GONE);
    }

    /**
     * 设置左边标记是否显示
     * @param left_remard_Visible
     */
    public void setLeftRemarkVisible(boolean left_remard_Visible){
        tvLeftRemark.setVisibility(left_remard_Visible ? View.VISIBLE : View.GONE);
    }

    /**
     * 设置右边图片是否显示
     *
     * @param right_image_Visible
     */
    public void setRightImageVisible(boolean right_image_Visible) {
        rightImage.setVisibility(right_image_Visible ? View.VISIBLE : View.GONE);
    }

    /**
     * 设置左边文字是否显示
     *
     * @param left_textView_Visible
     */
    public void setLeftTextViewVisible(boolean left_textView_Visible) {
        if (!TextUtils.isEmpty(left_textView_text)) {
            leftTextView.setVisibility(View.VISIBLE);
        } else {
            leftTextView.setVisibility(left_textView_Visible ? View.VISIBLE : View.GONE);
        }
    }

    /**
     * 设置右边第二个文字是否显示
     *
     * @param right_textView1_Visible
     */
    public void setRightTextView1Visible(boolean right_textView1_Visible) {
        if (!TextUtils.isEmpty(right_textView1_text)) {
            rightTextView1.setVisibility(View.VISIBLE);
        } else {
            rightTextView1.setVisibility(right_textView1_Visible ? View.VISIBLE : View.GONE);
        }
    }

    /**
     * 设置右边第一个文字是否显示
     *
     * @param right_textView2_Visible
     */
    public void setRightTextView2Visible(boolean right_textView2_Visible) {
        if (!TextUtils.isEmpty(right_textView2_text)) {
            rightTextView2.setVisibility(View.VISIBLE);
        } else {
            rightTextView2.setVisibility(right_textView2_Visible ? View.VISIBLE : View.GONE);
        }
    }

    /**
     * 设置底部横线是否显示
     *
     * @param view_Visible
     */
    public void setViewVisible(boolean view_Visible, int view_margin_right) {
        setViewMarginRight(view_margin_right);
        view.setVisibility(view_Visible ? View.VISIBLE : View.GONE);
    }

    /**
     * 设置底部横线相对左边距离
     *
     * @param view_margin_right
     */
    public void setViewMarginRight(int view_margin_right) {
        LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, 1);
        layoutParams.setMargins(Px2DpUtils.dp2px(context, view_margin_right), 0, Px2DpUtils.dp2px(context, view_margin_right), 0);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        view.setLayoutParams(layoutParams);
    }
}
