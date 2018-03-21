package com.beihui.market.ui.presenter;


import android.content.Context;
import android.text.TextUtils;

import com.beihui.market.api.Api;
import com.beihui.market.api.ResultEntity;
import com.beihui.market.base.BaseRxPresenter;
import com.beihui.market.entity.DebtChannel;
import com.beihui.market.helper.UserHelper;
import com.beihui.market.ui.contract.DebtNewContract;
import com.beihui.market.util.RxUtil;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import javax.inject.Inject;

import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class DebtNewPresenter extends BaseRxPresenter implements DebtNewContract.Presenter {
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);

    private final int TEMPLATE_COMP = 1;
    private final int TEMPLATE_SAMPLE = 2;

    private Api api;
    private DebtNewContract.View view;
    private DebtChannel debtChannel;

    private UserHelper userHelper;

    @Inject
    DebtNewPresenter(Context context, Api api, DebtNewContract.View view, DebtChannel debtChannel) {
        this.api = api;
        this.view = view;
        this.debtChannel = debtChannel;

        this.userHelper = UserHelper.getInstance(context);
    }


    @Override
    public void saveOneTimeDebt(@NonNull Date payDate, String debtAmount, String capital, String timeLimit, String remark) {
        if (TextUtils.isEmpty(debtAmount)) {
            view.showErrorMsg("请输入到期还款金额");
            return;
        }

        String channelId = (TextUtils.isEmpty(debtChannel.getType()) || debtChannel.isCustom()) ? debtChannel.getCustomId() : debtChannel.getId();
        HashMap<String, Object> params = new HashMap<>();
        params.put("userId", userHelper.getProfile().getId());
        params.put("channelId", channelId);
        params.put("channelName", debtChannel.getChannelName());
        params.put("repayType", DebtNewPresenter.METHOD_ONE_TIME);
        params.put("termType", 1);

        params.put("deuRepaymentDate", dateFormat.format(payDate));
        try {
            params.put("payableAmount", Double.parseDouble(debtAmount));
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        if (!TextUtils.isEmpty(remark)) {
            params.put("remark", remark);
        }

        //用户填写完整信息
        boolean hasExtraInfo = !TextUtils.isEmpty(capital);
        if (hasExtraInfo) {
            params.put("capital", Double.parseDouble(capital));
            try {
                params.put("term", Integer.parseInt(timeLimit));
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
        params.put("template", hasExtraInfo ? TEMPLATE_COMP : TEMPLATE_SAMPLE);

        Disposable dis = api.saveDebtImmediately(params)
                .compose(RxUtil.<ResultEntity>io2main())
                .subscribe(new Consumer<ResultEntity>() {
                               @Override
                               public void accept(ResultEntity result) throws Exception {
                                   if (result.isSuccess()) {
                                       view.saveDebtSuccess();
                                   } else {
                                       view.showErrorMsg(result.getMsg());
                                   }
                               }
                           },
                        new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                logError(DebtNewPresenter.this, throwable);
                                view.showErrorMsg(generateErrorMsg(throwable));
                            }
                        });
        addDisposable(dis);
    }

    @Override
    public void saveEvenDebt(@NonNull Date payDate, String timeLimit, String termAmount, String capital, String remark) {
        if (TextUtils.isEmpty(termAmount)) {
            view.showErrorMsg("请输入每月还款金额");
            return;
        }

        String channelId = (TextUtils.isEmpty(debtChannel.getType()) || debtChannel.isCustom()) ? debtChannel.getCustomId() : debtChannel.getId();
        HashMap<String, Object> params = new HashMap<>();
        params.put("userId", userHelper.getProfile().getId());
        params.put("channelId", channelId);
        params.put("channelName", debtChannel.getChannelName());
        params.put("repayType", DebtNewPresenter.METHOD_EVEN_DEBT);
        params.put("termType", 2);

        params.put("firstRepaymentDate", dateFormat.format(payDate));
        try {
            params.put("term", Integer.parseInt(timeLimit));
            params.put("everyTermAmount", Double.parseDouble(termAmount));
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        if (!TextUtils.isEmpty(remark)) {
            params.put("remark", remark);
        }

        //用户填写完整信息
        boolean hasExtraInfo = !TextUtils.isEmpty(capital);
        if (hasExtraInfo) {
            try {
                params.put("capital", Double.parseDouble(capital));
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
        params.put("template", hasExtraInfo ? TEMPLATE_COMP : TEMPLATE_SAMPLE);

        Disposable dis = api.saveDebtImmediately(params)
                .compose(RxUtil.<ResultEntity>io2main())
                .subscribe(new Consumer<ResultEntity>() {
                               @Override
                               public void accept(ResultEntity result) throws Exception {
                                   if (result.isSuccess()) {
                                       view.saveDebtSuccess();
                                   } else {
                                       view.showErrorMsg(result.getMsg());
                                   }
                               }
                           },
                        new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                logError(DebtNewPresenter.this, throwable);
                                view.showErrorMsg(generateErrorMsg(throwable));
                            }
                        });
        addDisposable(dis);
    }
}
