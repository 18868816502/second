package com.beiwo.klyjaz.social.dialog;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.beiwo.klyjaz.R;


public class CommentDialog extends DialogFragment implements TextWatcher, View.OnClickListener {

    private TextView sendView;
    private String hintText;
    private Dialog dialog;
    private EditText inputText;
//    private ImageView emojiView;
    //点击发表，内容不为空时的回调
    public SendListener mSendListener;

    public CommentDialog() {

    }

    @SuppressLint("ValidFragment")
    public CommentDialog(String hintText, SendListener sendBackListener) {//提示文字
        this.hintText = hintText;
        this.mSendListener = sendBackListener;
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View contentView = initDialog();
        setLayoutParams();
        initView(contentView);
        return dialog;
    }

    private void initView(View contentView) {
//        emojiView = (ImageView) contentView.findViewById(R.id.iv_emoji);
        inputText = (EditText) contentView.findViewById(R.id.dialog_comment_content);
        inputText.setHint(hintText);
        sendView = (TextView) contentView.findViewById(R.id.dialog_comment_send);

        inputText.addTextChangedListener(this);
        sendView.setOnClickListener(this);
//        emojiView.setOnClickListener(this);
        inputText.setFocusable(true);
        inputText.setFocusableInTouchMode(true);
        inputText.requestFocus();

        final Handler handler = new Handler();
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {

            @Override
            public void onDismiss(DialogInterface dialog) {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        hideSoftKeyBoard(getActivity());
                    }
                }, 200);

            }
        });
    }

    private void hideSoftKeyBoard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputMethodManager != null) {
            inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    @NonNull
    private View initDialog() {
        // 使用不带Theme的构造器, 获得的dialog边框距离屏幕仍有几毫米的缝隙。
        dialog = new Dialog(getActivity(), R.style.Comment_Dialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // 设置Content前设定
        View contentView = View.inflate(getActivity(), R.layout.dialog_comment1111, null);
        dialog.setContentView(contentView);
        dialog.setCanceledOnTouchOutside(true); // 外部点击取消
        return contentView;
    }

    private void setLayoutParams() {
        // 设置宽度为屏宽, 靠近屏幕底部。
        Window window = dialog.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.gravity = Gravity.BOTTOM; // 紧贴底部
        lp.alpha = 1;
        lp.dimAmount = 0.0f;
        lp.width = WindowManager.LayoutParams.MATCH_PARENT; // 宽度持平
        window.setAttributes(lp);
        window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
    }

    public void cleanText() {
        inputText.setText("");
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if (s.length() > 0) {
            sendView.setEnabled(true);
            sendView.setTextColor(Color.BLACK);
        } else {
            sendView.setEnabled(false);
            sendView.setTextColor(Color.GRAY);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.dialog_comment_send:
                checkContent();
                break;
//            case R.id.iv_emoji:
//                inputText.setText("");
//                break;
        }
    }

    private void checkContent() {
        String content = inputText.getText().toString().trim();
        if (TextUtils.isEmpty(content)) {
            Toast.makeText(getContext(), "评论内容不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        if (mSendListener != null) mSendListener.sendComment(content);
        dismiss();
    }

    public interface SendListener {
        void sendComment(String inputText);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mSendListener != null) mSendListener = null;
    }
}
