package com.beiwo.klyjaz.ui.dialog;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.beiwo.klyjaz.R;
import com.beiwo.klyjaz.view.EditTextUtils;
import com.beiwo.klyjaz.view.flowlayout.FlowLayout;
import com.beiwo.klyjaz.view.flowlayout.TagAdapter;
import com.beiwo.klyjaz.view.flowlayout.TagFlowLayout;

import java.util.HashMap;

/**
 * Created by admin on 2018/6/14.
 */

public class AccountFlowRemarkDialog extends DialogFragment implements View.OnClickListener {

    //依附的Activity
    private Activity activity;

    public String saveContent;

    //XML
    public View mView;

    public TagFlowLayout mFlowLayout;
    public LinearLayout mRoot;
    public EditText mEditText;
    public TextView mNum;
    public TextView mConfirm;
    private String content = null;


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
        mRoot = (LinearLayout)mView.findViewById(R.id.ll_dialog_acount_remark_root);
        mFlowLayout = (TagFlowLayout)mView.findViewById(R.id.id_flowlayout);
        mEditText = (EditText)mView.findViewById(R.id.et_dialog_account_remark);
        mConfirm = (TextView)mView.findViewById(R.id.tv_content_confirm);
        mNum = (TextView)mView.findViewById(R.id.tv_content_num);

        if (content == null) {
            mEditText.setText("");
            mConfirm.setEnabled(false);
            mConfirm.setBackground(activity.getResources().getDrawable(R.drawable.xshape_dialog_remark_black_bg));
            mNum.setText("0/20");
        } else {
            mEditText.setText(content);
            mEditText.setSelection(content.length());
            mConfirm.setEnabled(true);
            mConfirm.setBackground(activity.getResources().getDrawable(R.drawable.xshape_dialog_remark_red_bg));
            mNum.setText(content.length()+"/20");
        }


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


        mFlowLayout.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
            @Override
            public boolean onTagClick(View view, int position, FlowLayout parent) {
                if (copyVals.containsKey(position+"")) {
                    copyVals.remove(position+"");
                    if (!TextUtils.isEmpty(mEditText.getText().toString())) {
                        mEditText.setText(mEditText.getText().toString() + mVals[position]);
                    } else {
                        mEditText.setText(mVals[position]);
                    }
                    mEditText.setSelection(mEditText.getText().length());
                } else {
                    copyVals.put(position+"", mVals[position]);
                    mEditText.setText(mEditText.getText().toString().replace(mVals[position], ""));
                    mEditText.setSelection(mEditText.getText().length());
                }
                return true;
            }
        });

        mEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mNum.setText(s.length()+"/20");
                if (TextUtils.isEmpty(s.toString())) {
                    mConfirm.setEnabled(false);
                    mConfirm.setBackground(activity.getResources().getDrawable(R.drawable.xshape_dialog_remark_black_bg));
                } else {
                    mConfirm.setEnabled(true);
                    mConfirm.setBackground(activity.getResources().getDrawable(R.drawable.xshape_dialog_remark_red_bg));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mConfirm.setOnClickListener(this);


        if (!TextUtils.isEmpty(saveContent)) {
            mEditText.setText(saveContent);
            mEditText.setSelection(saveContent.length());
        }

        EditTextUtils.addDisableEmojiInputFilter(mEditText);
        return mView;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.tv_content_confirm) {
            if (onTextChangeListener!=null) {
                onTextChangeListener.textChange(mEditText.getText().toString());
                saveContent = mEditText.getText().toString();
            }
            HideKeyboard(mEditText);
        }
    }

    public interface OnTextChangeListener{
        void textChange(String text);
    }

    public OnTextChangeListener onTextChangeListener;

    public void setOnTextChangeListener(OnTextChangeListener onTextChangeListener) {
        this.onTextChangeListener = onTextChangeListener;
    }

    @Override
    public void onStart() {
        super.onStart();
        showKeyBoard(mEditText);
    }

    public static void HideKeyboard(View v) {
        InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService( Context.INPUT_METHOD_SERVICE );
        if (imm.isActive()) {
            imm.hideSoftInputFromWindow( v.getApplicationWindowToken() , 0);
        }
    }

    /**
     * 显示软键盘
     */
    public void showKeyBoard(final View editText) {
        editText.requestFocus();
        InputMethodManager manager = (InputMethodManager)activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        manager.showSoftInput(editText, 0);
    }


    private String[] mVals = new String[]{};

    public HashMap<String, String> copyVals = new HashMap<>();

    public void setTagList(String[] remarks) {
        if (remarks.length > 0) {
            mVals = remarks;
        }
        for(int i = 0; i < remarks.length; i++) {
            copyVals.put(i+"", remarks[i]);
        }
    }
}