package com.beihui.market.ui.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toolbar;

import com.beihui.market.R;
import com.beihui.market.view.flowlayout.FlowLayout;
import com.beihui.market.view.flowlayout.TagAdapter;
import com.beihui.market.view.flowlayout.TagFlowLayout;
import com.gyf.barlibrary.ImmersionBar;

import butterknife.BindView;

/**
 * Created by admin on 2018/6/14.
 */

public class AccountFlowRemarkDialog extends DialogFragment {


    //依附的Activity
    private Activity activity;

    //XML
    public View mView;

    public TagFlowLayout mFlowLayout;
    public EditText mEditText;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Holo_Light_DialogWhenLarge_NoActionBar);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //XML
        mView= LayoutInflater.from(getActivity()).inflate(R.layout.x_dialog_account_flow_remark, null);
        mFlowLayout = (TagFlowLayout)mView.findViewById(R.id.id_flowlayout);
        mEditText = (EditText)mView.findViewById(R.id.et_dialog_account_remark);
        getDialog().getWindow().setBackgroundDrawable(
                new ColorDrawable(ContextCompat.getColor(mView.getContext(), R.color.transparent_half)));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getDialog().getWindow().setStatusBarColor(getActivity().getResources().getColor(R.color.transparent_half));
        }
        mFlowLayout.setAdapter(new TagAdapter<String>(mVals) {

            @Override
            public View getView(FlowLayout parent, int position, String s) {
                TextView tv = (TextView) LayoutInflater.from(getActivity()).inflate(R.layout.x_tag_account_flow_remark,
                        mFlowLayout, false);
                tv.setText(s);
                return tv;
            }

            @Override
            public boolean setSelected(int position, String s) {
                return s.equals("Android");
            }
        });

        return mView;
    }

    @Override
    public void onStart() {
        super.onStart();
        showKeyBoard(mEditText);
    }

    /**
     * 显示软键盘
     */
    public void showKeyBoard(final View editText) {
        editText.requestFocus();
        InputMethodManager manager = (InputMethodManager)activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        manager.showSoftInput(editText, 0);
    }

    private String[] mVals = new String[]
            {"吃饭", "交钱", "购物", "这是什么", "你在干嘛呢", "走",
                    "哈哈哈哈哈", "你说呢", "不要这样子", "嘿嘿嘿", "aaaaaaaaaaaaaaaa"};
}
