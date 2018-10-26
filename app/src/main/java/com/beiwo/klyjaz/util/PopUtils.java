package com.beiwo.klyjaz.util;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.view.Gravity;
import android.view.WindowManager;

import com.beiwo.klyjaz.view.dialog.PopDialog;

/**
 * @author chenguoguo
 * @name loanmarket
 * @class name：com.beihui.market.util
 * @class describe 弹窗工具类
 * @time 2018/9/17 15:23
 */
public class PopUtils {

    private static PopDialog mPopDialog;
    private static  PopDialog commentDialog;

    private PopUtils() {

    }

    /**
     * 显示底部弹出框
     * @param layoutId
     * @param fManager
     * @param mContext
     * @param listener
     */
    public static void showBottomPopWindow(int layoutId, FragmentManager fManager, Context mContext,
                                           PopDialog.OnInitPopListener listener){
        mPopDialog = new PopDialog.Builder(fManager,mContext)
                .setLayoutId(layoutId)
                .setGravity(Gravity.BOTTOM)
                .setCancelableOutside(true)
                .setInitPopListener(listener)
                .create();
        mPopDialog.show();
    }

    /**
     * 显示评论列表弹出框
     * @param layoutId
     * @param fManager
     * @param mContext
     * @param listener
     */
    public static void showBottomListWindow(int layoutId, FragmentManager fManager, Context mContext,
                                           PopDialog.OnInitPopListener listener){
        mPopDialog = new PopDialog.Builder(fManager,mContext)
                .setLayoutId(layoutId)
                .setGravity(Gravity.BOTTOM)
                .setFlag(2)
                .setCancelableOutside(true)
                .setInitPopListener(listener)
                .create();
        mPopDialog.show();
    }

    /**
     * 显示中心弹出框
     * @param layoutId
     * @param fManager
     * @param mContext
     * @param listener
     */
    public static void showCenterPopWindow(int layoutId, FragmentManager fManager, Context mContext,
                                           PopDialog.OnInitPopListener listener){
        mPopDialog = new PopDialog.Builder(fManager,mContext)
                .setLayoutId(layoutId)
                .setWidth(270)
                .setHeight(120)
                .setGravity(Gravity.CENTER)
                .setCancelableOutside(false)
                .setInitPopListener(listener)
                .create();
        mPopDialog.show();
    }


    /**
     * 显示评论输入框
     * @param layoutId
     * @param fManager
     * @param mContext
     * @param initListener
     * @param dismissListener
     */
    public static void showCommentPopWindow(int layoutId, FragmentManager fManager, Context mContext,
                                            PopDialog.OnInitPopListener initListener,PopDialog.OnDismissListener dismissListener){
//        if(commentDialog == null) {
            commentDialog = new PopDialog.Builder(fManager, mContext)
                    .setLayoutId(layoutId)
                    .setHeight(58)
                    .setDimAmount(0.0f)
                    .setGravity(Gravity.BOTTOM)
                    .setCancelableOutside(true)
                    .setInitPopListener(initListener)
                    .setDismissListener(dismissListener)
                    .create();
//        }
        commentDialog.show();
//        commentDialog.getDialog().show();
    }

    /**
     * 隐藏弹出框
     */
    public static void dismiss(){
        if(mPopDialog != null){
            mPopDialog.dismiss();
        }
    }

    public static void dismissComment(){
        if(commentDialog != null){
            commentDialog.dismiss();
        }
    }

}
