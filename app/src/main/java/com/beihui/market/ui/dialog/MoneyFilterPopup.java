package com.beihui.market.ui.dialog;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.RotateAnimation;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.beihui.market.R;
import com.beihui.market.util.InputMethodUtil;
import com.beihui.market.util.viewutils.ToastUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MoneyFilterPopup extends PopupWindow {

    @BindView(R.id.et_money)
    EditText etMoney;

    private String money;
    private Activity context;

    private View mMenuView;
    private View shadowView;
    private TextView tv;
    private ImageView iv;

    public MoneyFilterPopup(final Activity context, String money, View shadowView, TextView tv, ImageView iv) {
        super(context);
        this.context = context;
        this.shadowView = shadowView;


        shadowView.setVisibility(View.VISIBLE);
        shadowView.invalidate();
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mMenuView = inflater.inflate(R.layout.dialog_brmoney, null);
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


        etMoney.setText(money);
        InputMethodUtil.setEditTextSelectionToEnd(etMoney);
    }


    @Override
    public void dismiss() {
        super.dismiss();
        shadowView.setVisibility(View.GONE);
        tv.setTextColor(Color.parseColor("#424251"));
        iv.setImageResource(R.mipmap.daosanjiao);
        rotateArrow(180, 0, iv);
    }


    public onBrMoneyListener listener;


    @OnClick({R.id.tv_cancle, R.id.tv_ok})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_cancle:
                etMoney.setText("");
                break;
            case R.id.tv_ok:
                money = etMoney.getText().toString();
                if (TextUtils.isEmpty(money)) {
                    ToastUtils.showShort(context, "请输入金额", null);
                    return;
                }
                dismiss();
                if (listener != null)
                    listener.onMoneyItemClick(money);
                break;
        }
    }

    public interface onBrMoneyListener {
        void onMoneyItemClick(String money);
    }

    public void setShareItemListener(onBrMoneyListener listener) {
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
