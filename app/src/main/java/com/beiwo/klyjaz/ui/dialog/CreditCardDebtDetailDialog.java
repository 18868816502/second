package com.beiwo.klyjaz.ui.dialog;


import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.beiwo.klyjaz.R;

public class CreditCardDebtDetailDialog extends DialogFragment {

    private Switch switchView;

    private View.OnClickListener onClickListener;
    private CompoundButton.OnCheckedChangeListener onCheckedChangeListener;

    private boolean remind;

    private boolean editable;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.CommonBottomDialogStyle);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_credit_card_debt_detail, container, false);
        switchView = view.findViewById(R.id.remind_switch);
        switchView.setChecked(remind);
        switchView.setOnCheckedChangeListener(onCheckedChangeListener);
        view.findViewById(R.id.edit).setOnClickListener(onClickListener);
        view.findViewById(R.id.delete).setOnClickListener(onClickListener);
        view.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        if (!editable) {
            view.findViewById(R.id.edit).setVisibility(View.GONE);
        }
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        getDialog().setCanceledOnTouchOutside(true);
        Window window = getDialog().getWindow();
        if (window != null) {
            WindowManager.LayoutParams lp = window.getAttributes();
            window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
            window.setAttributes(lp);
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            window.setGravity(Gravity.BOTTOM);
        }
    }

    public CreditCardDebtDetailDialog attachListeners(View.OnClickListener clickListener, CompoundButton.OnCheckedChangeListener checkedChangeListener) {
        onClickListener = clickListener;
        onCheckedChangeListener = checkedChangeListener;

        return this;
    }

    public CreditCardDebtDetailDialog attachInitStatus(boolean remind) {
        this.remind = remind;
        return this;
    }

    public CreditCardDebtDetailDialog attachEditable(boolean editable) {
        this.editable = editable;
        return this;
    }

    public void updateRemind(boolean remind) {
        this.remind = remind;
        if (switchView != null) {
            switchView.setChecked(remind);
        }
    }

}
