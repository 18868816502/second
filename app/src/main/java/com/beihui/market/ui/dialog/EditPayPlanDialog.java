package com.beihui.market.ui.dialog;


import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;

import com.beihui.market.R;
import com.beihui.market.util.CommonUtils;
import com.beihui.market.util.InputMethodUtil;
import com.beihui.market.util.WeakRefToastUtil;

public class EditPayPlanDialog extends DialogFragment {

    private EditText payPlanAmount;

    private PayPlanAmountChangedListener listener;

    private double originalAmount;

    public interface PayPlanAmountChangedListener {
        void onPayPlanAmountChanged(String amount);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, 0);
        setCancelable(false);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_pay_plan_edit, container, false);
        payPlanAmount = view.findViewById(R.id.pay_plan_amount);
        payPlanAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    double amount = Double.parseDouble(payPlanAmount.getText().toString());
                    if (amount >= 10000000) {
                        WeakRefToastUtil.showShort(getContext(), "金额不能超过1000万", null);
                        payPlanAmount.getEditableText().delete(payPlanAmount.length() - 1, payPlanAmount.length());
                    }
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        payPlanAmount.setText(CommonUtils.convertAmount(originalAmount));
        View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId() == R.id.cancel) {
                    InputMethodUtil.closeSoftKeyboard(getContext(), payPlanAmount);

                    payPlanAmount.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            dismiss();
                        }
                    }, 100);
                } else {
                    String amount = payPlanAmount.getText().toString();

                    if (amount.length() > 0) {
                        if (listener != null) {
                            listener.onPayPlanAmountChanged(amount);
                        }

                        InputMethodUtil.closeSoftKeyboard(getContext(), payPlanAmount);
                        payPlanAmount.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                dismiss();
                            }
                        }, 100);
                    } else {
                        WeakRefToastUtil.showShort(getContext(), "请输入金额", null);
                    }
                }
            }
        };
        view.findViewById(R.id.cancel).setOnClickListener(clickListener);
        view.findViewById(R.id.confirm).setOnClickListener(clickListener);

        view.postDelayed(new Runnable() {
            @Override
            public void run() {
                InputMethodUtil.openSoftKeyboard(getContext(), payPlanAmount);
            }
        }, 100);

        payPlanAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        Window window = getDialog().getWindow();
        if (window != null) {
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

            WindowManager.LayoutParams lp = window.getAttributes();
            lp.width = (int) (getResources().getDisplayMetrics().density * 280);
            window.setAttributes(lp);
        }
    }

    public EditPayPlanDialog setPayPlanAmountChangedListener(PayPlanAmountChangedListener listener) {
        this.listener = listener;
        return this;
    }

    public EditPayPlanDialog setOriginalAmount(double amount) {
        this.originalAmount = amount;
        return this;
    }
}
