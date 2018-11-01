package com.beiwo.klyjaz.social.utils;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.beiwo.klyjaz.R;
import com.beiwo.klyjaz.social.bean.CommentReplyBean;
import com.beiwo.klyjaz.ui.adapter.ArticleCommentListAdapter;
import com.beiwo.klyjaz.view.dialog.PopDialog;

import java.util.List;

import static com.umeng.socialize.utils.ContextUtil.getContext;

/**
 * @author chenguoguo
 * @name loanmarket_social
 * @class name：com.beiwo.klyjaz.social.utils
 * @descripe
 * @time 2018/10/31 13:30
 */
public class DialogUtils {

    private static PopDialog commentDialog;
    private static PopDialog inputDialog;

    /**
     * 显示评论列表弹出框
     * @param layoutId
     * @param fManager
     * @param mContext
     * @param listener
     */
    public static void showBottomListWindow(int layoutId, FragmentManager fManager, final Context mContext, final View.OnClickListener listener, final List<CommentReplyBean> list){
        commentDialog = new PopDialog.Builder(fManager,mContext)
                .setLayoutId(layoutId)
                .setGravity(Gravity.BOTTOM)
                .setFlag(2)
                .setCancelableOutside(true)
                .setInitPopListener(new PopDialog.OnInitPopListener() {
                    @Override
                    public void initPop(View view, PopDialog mPopDialog) {
                        view.findViewById(R.id.iv_close).setOnClickListener(listener);
                        view.findViewById(R.id.tv_comment).setOnClickListener(listener);
                        View emptyContainer = view.findViewById(R.id.empty_container);
                        TextView tvCommentTitle = view.findViewById(R.id.tv_comment_title);
                        RecyclerView itemRecycler = view.findViewById(R.id.comment_recycler);
                        LinearLayoutManager manager = new LinearLayoutManager(mContext);
                        manager.setOrientation(LinearLayoutManager.VERTICAL);
                        itemRecycler.setLayoutManager(manager);
                        ArticleCommentListAdapter childAdapter = new ArticleCommentListAdapter((Activity) mContext);
                        itemRecycler.setAdapter(childAdapter);
                        tvCommentTitle.setText(String.valueOf("全部" + list.size() + "条评论"));
                        childAdapter.setDatas(list);
                        childAdapter.notifyDataSetChanged();
//                        childAdapter.setOnViewClickListener(this);
                        if (list.size() == 0) {
                            emptyContainer.setVisibility(View.VISIBLE);
                            itemRecycler.setVisibility(View.GONE);
                        } else {
                            emptyContainer.setVisibility(View.GONE);
                            itemRecycler.setVisibility(View.VISIBLE);
                        }
                    }
                })
                .create();
        commentDialog.show();
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
        inputDialog = new PopDialog.Builder(fManager,mContext)
                .setLayoutId(layoutId)
                .setWidth(270)
                .setHeight(120)
                .setGravity(Gravity.CENTER)
                .setCancelableOutside(false)
                .setInitPopListener(listener)
                .create();
        inputDialog.show();
    }


    /**
     * 显示评论输入框
     * @param layoutId
     * @param fManager
     * @param mContext
     * @param listener
     */
    public static void showCommentPopWindow(int layoutId, FragmentManager fManager, Context mContext,View.OnClickListener listener){
        inputDialog = new PopDialog.Builder(fManager, mContext)
                .setLayoutId(layoutId)
                .setHeight(58)
                .setDimAmount(0.0f)
                .setGravity(Gravity.BOTTOM)
                .setCancelableOutside(true)
                .setInitPopListener(new PopDialog.OnInitPopListener() {
                    @Override
                    public void initPop(View view, PopDialog mPopDialog) {

                    }
                })
                .setDismissListener(new PopDialog.OnDismissListener() {
                    @Override
                    public void onDismiss(PopDialog mPopDialog) {
                        View view = mPopDialog.getDialog().getCurrentFocus();
                        if(view instanceof TextView){
                            InputMethodManager mInputMethodManager = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                            mInputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.RESULT_UNCHANGED_SHOWN);
                        }

//                        super.dismiss();
                    }
                })
                .create();
        inputDialog.show();
    }


}
