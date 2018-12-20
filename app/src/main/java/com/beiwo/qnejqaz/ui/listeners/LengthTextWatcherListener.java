package com.beiwo.qnejqaz.ui.listeners;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import com.beiwo.qnejqaz.util.ToastUtil;


/**
 * @author chenguoguo
 * @name loanmarket_social
 * @class name：com.beiwo.klyjaz.ui.listeners
 * @descripe
 * @time 2018/10/15 16:16
 */
public class LengthTextWatcherListener implements TextWatcher {
    private int length;
    private EditText editText;

    public LengthTextWatcherListener(EditText editText, int length) {
        this.length = length;
        this.editText = editText;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        //判断EditText中输入的字符数是不是已经大于6
        if (s.length() > length) {
            //设置EditText只显示前面6位字符
            editText.setText(s.toString().substring(0, length));
            //让光标移至末端
            editText.setSelection(length);
            ToastUtil.toast("输入字数已达上限");
            return;
        }
    }

    @Override
    public void afterTextChanged(Editable s) {
    }
}