package com.beihui.market.ui.dialog;


import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;

import com.beihui.market.R;
import com.beihui.market.ui.listeners.EtAmountWatcher;
import com.beihui.market.util.InputMethodUtil;
import com.beihui.market.util.viewutils.ToastUtils;

/**
 * @author xhb
 * 输入还款金额
 */
public class BillEditAmountDialog extends DialogFragment {
    private EditText billAmount;
    private double pendingAmount;

    private EditAmountConfirmListener confirmListener;

    public interface EditAmountConfirmListener {
        void onEditAmountConfirm(double amount);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, 0);
        setCancelable(false);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_bill_edit_amount, container, false);
        billAmount = view.findViewById(R.id.bill_amount);
        if (pendingAmount > 0) {
            billAmount.setText(pendingAmount + "");
        }
        View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                if (v.getId() == R.id.cancel) {
                    InputMethodUtil.closeSoftKeyboard(getContext(), billAmount);

                    billAmount.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            dismiss();
                        }
                    }, 100);
                } else {
                    final String amount = billAmount.getText().toString();

                    if (TextUtils.isEmpty(amount)) {
                        ToastUtils.showShort(getContext(), "请输入金额", null);
                    } else if (Double.parseDouble(amount) <= 0D) {
                        ToastUtils.showShort(getContext(), "金额必须大于零", null);
                    } else if (amount.length() > 0) {
                        InputMethodUtil.closeSoftKeyboard(getContext(), billAmount);
                        billAmount.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                dismiss();
                                if (confirmListener != null) {
                                    confirmListener.onEditAmountConfirm(Double.parseDouble(amount));
                                }
                            }
                        }, 100);
                    } else {
                        ToastUtils.showShort(getContext(), "请输入金额", null);
                    }
                }
            }
        };
        view.findViewById(R.id.cancel).setOnClickListener(clickListener);
        view.findViewById(R.id.confirm).setOnClickListener(clickListener);

        view.postDelayed(new Runnable() {
            @Override
            public void run() {
                InputMethodUtil.openSoftKeyboard(getContext(), billAmount);
            }
        }, 100);

        billAmount.addTextChangedListener(new EtAmountWatcher(billAmount));
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

    public BillEditAmountDialog attachConfirmListener(EditAmountConfirmListener listener) {
        confirmListener = listener;
        return this;
    }

    public BillEditAmountDialog attachPendingAmount(double amount) {
        this.pendingAmount = amount;
        return this;
    }
}
