package com.beihui.market.ui.dialog;


import android.annotation.SuppressLint;
import android.content.Intent;
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

import com.beihui.market.R;
import com.beihui.market.entity.DebtChannel;
import com.beihui.market.ui.activity.EBankActivity;
import com.beihui.market.ui.activity.DebtChannelActivity;
import com.beihui.market.util.RxUtil;
import com.umeng.socialize.media.UMWeb;

import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * @author xhb
 * 网贷记账
 * 信用卡账单
 * 快速记账
 */
public class XTabAccountDialog extends DialogFragment {

    private static final String TAG = XTabAccountDialog.class.getSimpleName();

    Unbinder unbinder;

    private UMWeb umWeb;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.CommonBottomDialogStyle);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.xlayout_dialog_tab_account, container, false);
        unbinder = ButterKnife.bind(this, view);
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

    @Override
    public void onDestroyView() {
        unbinder.unbind();
        super.onDestroyView();
    }

    @SuppressLint("InlinedApi")
    @OnClick({R.id.ll_dialog_tab_account_net_loan, R.id.ll_dialog_tab_account_credit_card,  R.id.ll_dialog_tab_account_hand, R.id.ll_dialog_tab_account_cancel})
    void OnViewClicked(View view) {
        switch (view.getId()) {
            //网贷记账
            case R.id.ll_dialog_tab_account_net_loan:
                startActivity(new Intent(getContext(), DebtChannelActivity.class));
                dismiss();
                break;
            //信用卡账单 目前只需要网银导入 进入H5页面
            case R.id.ll_dialog_tab_account_credit_card:
                startActivity(new Intent(getContext(), EBankActivity.class));
                dismiss();
                break;
            //手动记账
            case R.id.ll_dialog_tab_account_hand:

                //TODO 手动记账

                dismiss();
                break;
            case R.id.ll_dialog_tab_account_cancel:
                dismiss();
                break;
        }
    }

    /**
     * 快速记账
     * 不需要渠道ID ChannelID
     * 不需要Logo
     * TODO 跳转到新增网贷账单页面
     */
    public void addDebtChannel() {
        DebtChannel newChannel = new DebtChannel();
        newChannel.setChannelName("自定义");
        newChannel.setId(java.util.UUID.randomUUID().toString());

        Disposable dis = Observable.just(newChannel)
                .observeOn(Schedulers.io())
                .compose(RxUtil.<DebtChannel>io2main())
                .subscribe(new Consumer<DebtChannel>() {
                               @Override
                               public void accept(DebtChannel channel) throws Exception {


                               }
                           },
                        new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                            }
                        });
    }

}
