package com.beihui.market.view.dialog;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.beihui.market.R;
import com.beihui.market.util.DensityUtil;

/**
 * @name loanmarket
 * @class name：
 * @class describe dialog弹窗
 * @anthor chenguoguo
 * @time 2018/9/15 14:20
 */
public class PopDialog extends BaseDialog {

    public static final int POP_COMMON = 100;//普通对话框
    public static final int POP_LOADING = 101;//loading对话框
    public static final int POP_TIPS = 102;//提示对话框
    public static final int POP_ADVER = 103;//广告对话框
    public static final int POP_PROGRESS = 104;//进度条对话框
    public static final int POP_BOTTOM = 105;//底部相册弹出框
    public static final int POP_LIST = 106;//列表弹出框

    private FragmentManager fManager;
    private int mLayoutId = 0;//自定义弹窗布局
    private int mWidth;//弹窗高度，单位dp
    private int mHeight;//弹窗高度，单位dp

    private String mTitle;
    private String mContent;

    private int mGravity = Gravity.CENTER;
    private boolean mIsCancel;
    private float mDimAmount = 0.6f;

    private int mAnimationAnim;
    private static int mDialogType = 1;//默认为普通弹窗

    private OnInitPopListener mPopListener;
    private int[] ids;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public void onStart() {
        super.onStart();
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.PopDialog);
        setWindowAttr();

    }

    /**
     * 设置弹窗基本属性
     */
    private void setWindowAttr() {
        Window window = getDialog().getWindow();
        getDialog().setCanceledOnTouchOutside(mIsCancel);
        if (window != null) {
            //设置弹窗背景色透明
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            //设置动画
            if (mAnimationAnim > 0) {
                window.setWindowAnimations(mAnimationAnim);
            }
            //设置弹窗宽高
            WindowManager.LayoutParams layoutParams = window.getAttributes();
            if (mWidth > 0) {
                layoutParams.width = mWidth;
            } else {
                layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
            }
            if (mHeight > 0) {
                layoutParams.height = mHeight;
            } else {
                layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
            }
            //透明度
            layoutParams.dimAmount = mDimAmount;
            //位置
            layoutParams.gravity = mGravity;
            window.setAttributes(layoutParams);
        }
    }

    @Override
    public int getViewLayout() {
        return mLayoutId;
//        if(mLayoutId != 0 ) {
//            return mLayoutId;
//        }else{
//            switch (mDialogType){
//                case POP_COMMON:
//                    return R.layout.dialog_version_upgrde;
//                case POP_LOADING:
//                    return R.layout.dialog_tips;
//                case POP_TIPS:
//                    return 102;
//                case POP_ADVER:
//                    return 103;
//                case POP_PROGRESS:
//                    return 104;
//                default:
//                    return R.layout.dialog_version_upgrde;
//            }
//        }
    }

    @Override
    public void findView(View view) {
        mPopListener.initPop(view,this);
    }

    public void show(){
        FragmentTransaction ft = fManager.beginTransaction();
        ft.add(this, "PopDialog");
        ft.commitAllowingStateLoss();
    }

    public static class Builder{

        private PopDialog mDialog;
        private Context mContext;

        public Builder(FragmentManager fManager, Context mContext) {
            mDialog = new PopDialog();
            mDialog.fManager = fManager;
            this.mContext = mContext;
        }

        /**
         * 设置弹窗布局文件
         * @param mLayoutId
         * @return
         */
        public Builder setLayoutId(int mLayoutId){
            mDialog.mLayoutId = mLayoutId;
            return this;
        }

        /**
         * 设置弹窗宽度
         * @param mWidth
         * @return
         */
        public Builder setWidth(int mWidth){
            int px = DensityUtil.dp2px(mContext,mWidth);
            mDialog.mWidth = px;
            return this;
        }

        /**
         * 设置弹窗高度
         * @param mHeight
         * @return
         */
        public Builder setHeight(int mHeight){
            int px = DensityUtil.dp2px(mContext,mHeight);
            mDialog.mHeight = px;
            return this;
        }

        /**
         * 设置弹窗标题内容
         * @param mTitle
         * @return
         */
        public Builder setTitle(String mTitle){
            mDialog.mTitle = mTitle;
            return this;
        }

        /**
         * 设置弹窗内容
         * @param mContent
         * @return
         */
        public Builder setContent(String mContent){
            mDialog.mContent = mContent;
            return this;
        }

        /**
         * 设置弹窗位置
         * @param mGravity
         * @return
         */
        public Builder setGravity(int mGravity){
            mDialog.mGravity = mGravity;
            return this;
        }

        /**
         * 设置弹窗背景透明度(默认 0.6f 半透明)
         * @param dimAmount
         * @return
         */
        public Builder setDimAmount(float dimAmount){
            mDialog.mDimAmount = dimAmount;
            return this;
        }

        /**
         * 设置点击弹窗外是否隐藏弹窗
         * @param mIsCancel
         * @return
         */
        public Builder setCancelableOutside(boolean mIsCancel){
            mDialog.mIsCancel = mIsCancel;
            return this;
        }

        /**
         * 设置弹窗动画
         * @param mAnimationAnim
         * @return
         */
        public Builder setAnimationRes(int mAnimationAnim){
            mDialog.mAnimationAnim = mAnimationAnim;
            return this;
        }

        /**
         * 设置弹窗类型(默认为普通对话框)
         * @param mDialogType 100.普通对话框   101.loading对话框   102.提示对话框   103.广告对话框   104.进度条对话框
         * @return
         */
        public Builder setDialogType(int mDialogType){
            mDialog.mDialogType = mDialogType;
            return this;
        }

        /**
         * 构造对话框
         * @return
         */
        public PopDialog create(){
            return mDialog;
        }

        public Builder setInitPopListener(OnInitPopListener mViewListener){
            mDialog.mPopListener = mViewListener;
            return this;
        }

    }

    public interface OnInitPopListener{
        void initPop(View view, PopDialog mPopDialog);
    }



}
