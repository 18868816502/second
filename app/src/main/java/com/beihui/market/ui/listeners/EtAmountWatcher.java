package com.beihui.market.ui.listeners;


import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import com.beihui.market.util.viewutils.ToastUtils;

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
        try {
            double amount = Double.parseDouble(editText.getText().toString());
            if (amount >= 10000000) {
                ToastUtils.showShort(editText.getContext(), "金额不能超过1000万", null);
                editText.getEditableText().delete(editText.length() - 1, editText.length());
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}
