package com.beiwo.klyjaz.view.floatbutton;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

import com.beiwo.klyjaz.R;
import com.beiwo.klyjaz.view.GlideCircleTransform;
import com.bumptech.glide.Glide;

/**
 * @author chenguoguo
 * @name loanmarket
 * @class name：com.beiwo.klyjaz.view.floatbutton
 * @descripe 拖动悬浮吸附按钮
 * @time 2018/12/10 18:01
 */
public class DragFloatButton extends BaseDragFloatActionButton {

    private ImageView ivFloat;

    public DragFloatButton(Context context) {
        super(context);
    }

    public DragFloatButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DragFloatButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public int getLayoutId() {
        //自定义的悬浮布局
        return R.layout.layout_drag_float_button;
    }

    @Override
    public void initView(View view) {
        ivFloat = view.findViewById(R.id.iv_float);
    }

    public void setFloatBackground(Context mContext, String url) {
        Glide.with(mContext).load(url).transform(new GlideCircleTransform(mContext)).into(ivFloat);
    }
}