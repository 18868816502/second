package com.beiwo.klyjaz.view.dialog;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * @name loanmarket
 * @class name：
 * @class describe baseDialog
 * @anthor chenguoguo
 * @time 2018/9/15 14:20
 */
public abstract class BaseDialog extends DialogFragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(getViewLayout(), container, false);
        findView(view);
        return view;
    }

    public abstract int getViewLayout();

    public abstract void findView(View view);
}