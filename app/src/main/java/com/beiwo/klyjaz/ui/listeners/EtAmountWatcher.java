package com.beiwo.klyjaz.ui.listeners;


import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import com.beiwo.klyjaz.util.WeakRefToastUtil;

/**
 * @author xhb 应还金额监听器
 */
public class EtAmountWatcher implements TextWatcher {

    private EditText editText;

    public EtAmountWatcher(EditText editText) {
        this.editText = editText;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (editText.getText().length() > 0) {
            try {
                double amount = Double.parseDouble(editText.getText().toString());
                if (editText.getText().toString().contains(".")) {
                    String[] split = editText.getText().toString().split("\\.");
                    if (split.length > 1 && split[1].length() > 2) {
                        editText.getEditableText().delete(editText.length() - 1, editText.length());
                    }
                }
                if (amount > 10000000) {
                    WeakRefToastUtil.showShort(editText.getContext(), "金额不能超过1000万", null);
                    editText.getEditableText().delete(editText.length() - 1, editText.length());
                }
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void afterTextChanged(Editable s) {
    }
}