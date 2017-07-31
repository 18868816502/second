package com.beihui.market.ui.dialog;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.beihui.market.R;
import com.beihui.market.view.busineesrel.FlowTagLayout;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/3/7.
 */

public class TimeFilterPopup extends PopupWindow {

    @BindView(R.id.flow_layout)
    FlowTagLayout flowTagLayout;

    //记录选择的是什么范围的，一个月，三个月还是不限,从1 ~ 7
    private int selectTimeIndex;

    private String money;
    private Activity context;

    private View mMenuView;
    private View shadowView;
    private TextView tv;
    private ImageView iv;

    private String[] tags;

    public TimeFilterPopup(final Activity context, final int selectTimeIndex, View shadowView, TextView tv, ImageView iv,
                           String[] tags) {
        super(context);
        this.context = context;
        this.shadowView = shadowView;
        this.selectTimeIndex = selectTimeIndex;

        shadowView.setVisibility(View.VISIBLE);
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mMenuView = inflater.inflate(R.layout.dialog_brtime, null);
        ButterKnife.bind(this, mMenuView);

        //设置SelectPicPopupWindow的View
        this.setContentView(mMenuView);
        //设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(LayoutParams.MATCH_PARENT);
        //设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(LayoutParams.WRAP_CONTENT);
        //设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        //实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0xb0000000);
        //设置SelectPicPopupWindow弹出窗体的背景
        this.setBackgroundDrawable(dw);
        //mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
        mMenuView.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {

                int height = mMenuView.findViewById(R.id.pop_layout).getTop();
                int y = (int) event.getY();
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (y < height) {
                        dismiss();
                    }
                }
                return true;
            }
        });

        this.tv = tv;
        this.iv = iv;
        tv.setTextColor(Color.parseColor("#5591FF"));
        iv.setImageResource(R.mipmap.daosanjiao_blue);
        rotateArrow(0, 180, iv);

        flowTagLayout.setTags(tags, 0);
        flowTagLayout.setTagSelectedListener(new FlowTagLayout.TagSelectedListener() {
            @Override
            public void onTagSelected(int selected) {
                if (listener != null) {
                    listener.onTimeItemClick(selected);
                }
                dismiss();
            }
        });

    }


    @Override
    public void dismiss() {
        super.dismiss();
        shadowView.setVisibility(View.GONE);
        tv.setTextColor(Color.parseColor("#424251"));
        iv.setImageResource(R.mipmap.daosanjiao);
        rotateArrow(180, 0, iv);
    }

    public onBrTimeListener listener;

    public interface onBrTimeListener {
        void onTimeItemClick(int selectTimeIndex);
    }

    public void setShareItemListener(onBrTimeListener listener) {
        this.listener = listener;
    }


    /**
     * 旋转指示器
     *
     * @param fromDegrees
     * @param toDegrees
     */
    private void rotateArrow(float fromDegrees, float toDegrees, ImageView imageView) {
        RotateAnimation mRotateAnimation =
                new RotateAnimation(fromDegrees, toDegrees,
                        (int) (imageView.getMeasuredWidth() / 2.0),
                        (int) (imageView.getMeasuredHeight() / 2.0));
        mRotateAnimation.setDuration(150);
        mRotateAnimation.setFillAfter(true);
        imageView.startAnimation(mRotateAnimation);
    }

}
