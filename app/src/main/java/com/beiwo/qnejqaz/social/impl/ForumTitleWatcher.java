package com.beiwo.qnejqaz.social.impl;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.TextView;

import com.beiwo.qnejqaz.R;

/**
 * @author chenguoguo
 * @name loanmarket_social
 * @class name：com.beiwo.klyjaz.social.impl
 * @descripe
 * @time 2018/11/20 14:07
 */
public class ForumTitleWatcher implements TextWatcher {

    private TextView tvNumber;
    private Context mContext;

    public ForumTitleWatcher(Context mContext, TextView tvNumber) {
        this.tvNumber = tvNumber;
        this.mContext = mContext;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        tvNumber.setText(String.format(mContext.getString(R.string.social_forum_title_tips), 30 - s.length()));
    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}
