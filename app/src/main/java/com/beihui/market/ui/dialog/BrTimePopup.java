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

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/3/7.
 */

public class BrTimePopup extends PopupWindow {


    @BindView(R.id.tv_time1)
    TextView tvTime1;
    @BindView(R.id.tv_time2)
    TextView tvTime2;
    @BindView(R.id.tv_time3)
    TextView tvTime3;
    @BindView(R.id.tv_time4)
    TextView tvTime4;
    @BindView(R.id.tv_time5)
    TextView tvTime5;
    @BindView(R.id.tv_time6)
    TextView tvTime6;
    @BindView(R.id.tv_time7)
    TextView tvTime7;

    private TextView tv_time;
    //记录选择的是什么范围的，一个月，三个月还是不限,从1 ~ 7
    private int selectTimeIndex;

    private String money;
    private Activity context;

    private View mMenuView;
    private View shadowView;
    private TextView tv;
    private ImageView iv;

    public BrTimePopup(final Activity context, int selectTimeIndex, View shadowView,TextView tv, ImageView iv) {
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


        switch (selectTimeIndex){
            case 1:
                tv_time = tvTime1;
                break;
            case 2:
                tv_time = tvTime2;
                break;
            case 3:
                tv_time = tvTime3;
                break;
            case 4:
                tv_time = tvTime4;
                break;
            case 5:
                tv_time = tvTime5;
                break;
            case 6:
                tv_time = tvTime6;
                break;
            case 7:
                tv_time = tvTime7;
                break;
        }

        onTimeMenuCheck(selectTimeIndex);

    }


    @Override
    public void dismiss() {
        super.dismiss();
        shadowView.setVisibility(View.GONE);
        tv.setTextColor(Color.parseColor("#424251"));
        iv.setImageResource(R.mipmap.daosanjiao);
        rotateArrow(180, 0, iv);
    }





    @OnClick({R.id.tv_time1, R.id.tv_time2,R.id.tv_time3,R.id.tv_time4,R.id.tv_time5,R.id.tv_time6,R.id.tv_time7})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_time1:
                onTimeMenuCheck(1);
                break;
            case R.id.tv_time2:
                onTimeMenuCheck(2);
                break;
            case R.id.tv_time3:
                onTimeMenuCheck(3);
                break;
            case R.id.tv_time4:
                onTimeMenuCheck(4);
                break;
            case R.id.tv_time5:
                onTimeMenuCheck(5);
                break;
            case R.id.tv_time6:
                onTimeMenuCheck(6);
                break;
            case R.id.tv_time7:
                onTimeMenuCheck(7);
                break;
        }
        if (listener != null)
            listener.onTimeItemClick(selectTimeIndex);

        dismiss();
    }




    /**
     * 点击选择时间改变状态
     *
     * @param index
     */
    private void onTimeMenuCheck(int index) {
        tv_time.setSelected(false);
        selectTimeIndex = index;
        switch (index) {
            case 1:
                tv_time = tvTime1;
                break;
            case 2:
                tv_time = tvTime2;
                break;
            case 3:
                tv_time = tvTime3;
                break;
            case 4:
                tv_time = tvTime4;
                break;
            case 5:
                tv_time = tvTime5;
                break;
            case 6:
                tv_time = tvTime6;
                break;
            case 7:
                tv_time = tvTime7;
                break;
        }
        tv_time.setSelected(true);
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
