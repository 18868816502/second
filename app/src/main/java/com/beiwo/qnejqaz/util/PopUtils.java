package com.beiwo.qnejqaz.util;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.view.Gravity;
import android.view.View;

import com.beiwo.qnejqaz.R;
import com.beiwo.qnejqaz.view.dialog.PopDialog;

/**
 * @author chenguoguo
 * @name loanmarket
 * @class name：com.beihui.market.util
 * @class describe 弹窗工具类
 * @time 2018/9/17 15:23
 */
public class PopUtils {

    private static PopDialog mPopDialog;
    private static PopDialog commentDialog;

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
     * 显示动态删除弹出框
     * @param fManager
     * @param mContext
     * @param listener
     */
    public static void showForumDeleteDialog(FragmentManager fManager, Context mContext,
                                             final View.OnClickListener listener){
        mPopDialog = new PopDialog.Builder(fManager,mContext)
                .setLayoutId(R.layout.dialog_article_mine_more)
                .setGravity(Gravity.BOTTOM)
                .setCancelableOutside(true)
                .setInitPopListener(new PopDialog.OnInitPopListener() {
                    @Override
                    public void initPop(View view, PopDialog mPopDialog) {
                        view.findViewById(R.id.tv_delete).setOnClickListener(listener);
                        view.findViewById(R.id.tv_cancel).setOnClickListener(listener);
                    }
                })
                .create();
        mPopDialog.show();
    }

    /**
     * 显示动态举报弹出框
     * @param fManager
     * @param mContext
     * @param listener
     */
    public static void showForumReportDialog(FragmentManager fManager, Context mContext,
                                           final View.OnClickListener listener){
        mPopDialog = new PopDialog.Builder(fManager,mContext)
                .setLayoutId(R.layout.dialog_article_other_more)
                .setGravity(Gravity.BOTTOM)
                .setCancelableOutside(true)
                .setInitPopListener(new PopDialog.OnInitPopListener() {
                    @Override
                    public void initPop(View view, PopDialog mPopDialog) {
                        view.findViewById(R.id.report01).setOnClickListener(listener);
                        view.findViewById(R.id.report02).setOnClickListener(listener);
                        view.findViewById(R.id.report03).setOnClickListener(listener);
                        view.findViewById(R.id.report04).setOnClickListener(listener);
                        view.findViewById(R.id.tv_cancel).setOnClickListener(listener);
                    }
                })
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
     * 显示发送评论确认弹出框
     * @param fManager
     * @param mContext
     * @param listener
     */
    public static void showCommentAuditWindow(FragmentManager fManager, Context mContext,
                                           final View.OnClickListener listener){
        mPopDialog = new PopDialog.Builder(fManager,mContext)
                .setLayoutId(R.layout.dialog_article_comment_audit)
                .setWidth(270)
                .setHeight(120)
                .setGravity(Gravity.CENTER)
                .setCancelableOutside(false)
                .setInitPopListener(new PopDialog.OnInitPopListener() {
                    @Override
                    public void initPop(View view, PopDialog mPopDialog) {
                        view.findViewById(R.id.tv_cancel).setOnClickListener(listener);
                        view.findViewById(R.id.tv_save).setOnClickListener(listener);
                    }
                })
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
        commentDialog = new PopDialog.Builder(fManager, mContext)
                    .setLayoutId(layoutId)
                    .setHeight(58)
                    .setDimAmount(0.0f)
                    .setGravity(Gravity.BOTTOM)
                    .setCancelableOutside(true)
                    .setInitPopListener(initListener)
                    .setDismissListener(dismissListener)
                    .create();
        commentDialog.show();
    }

    /**
     * 显示动态发布保存草稿弹出框
     * @param fManager
     * @param mContext
     * @param listener
     */
    public static void showForumDraftDialog(FragmentManager fManager, Context mContext,
                                             final View.OnClickListener listener){
        mPopDialog = new PopDialog.Builder(fManager,mContext)
                .setLayoutId(R.layout.dialog_community_publish_save)
                .setWidth(270)
                .setHeight(120)
                .setGravity(Gravity.CENTER)
                .setCancelableOutside(true)
                .setInitPopListener(new PopDialog.OnInitPopListener() {
                    @Override
                    public void initPop(View view, PopDialog mPopDialog) {
                        view.findViewById(R.id.cancel).setOnClickListener(listener);
                        view.findViewById(R.id.tv_save).setOnClickListener(listener);
                    }
                })
                .create();
        mPopDialog.show();
    }

    /**
     * 显示动态发布保存草稿弹出框
     * @param fManager
     * @param mContext
     * @param listener
     */
    public static void showForumCommitDialog(FragmentManager fManager, Context mContext,
                                            final View.OnClickListener listener){
        mPopDialog = new PopDialog.Builder(fManager,mContext)
                .setLayoutId(R.layout.dialog_community_publish_commit)
                .setWidth(270)
                .setHeight(120)
                .setGravity(Gravity.CENTER)
                .setCancelableOutside(true)
                .setInitPopListener(new PopDialog.OnInitPopListener() {
                    @Override
                    public void initPop(View view, PopDialog mPopDialog) {
                        view.findViewById(R.id.tv_cancel).setOnClickListener(listener);
                        view.findViewById(R.id.tv_commit).setOnClickListener(listener);
                    }
                })
                .create();
        mPopDialog.show();
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
