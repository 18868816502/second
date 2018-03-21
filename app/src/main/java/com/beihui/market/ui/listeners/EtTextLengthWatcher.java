package com.beihui.market.ui.listeners;


import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import java.nio.charset.Charset;

public class EtTextLengthWatcher implements TextWatcher {

    private EditText editText;
    private int edge;

    public EtTextLengthWatcher(EditText editText, int edge) {
        this.editText = editText;
        this.edge = edge;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (editText.getEditableText().length() > 0) {
            byte[] bytes = editText.getText().toString().getBytes(Charset.forName("gb2312"));
            if (bytes.length > edge) {
                editText.getEditableText().delete(editText.length() - 1, editText.length());
            }
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}
