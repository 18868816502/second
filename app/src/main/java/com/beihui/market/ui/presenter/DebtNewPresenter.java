package com.beihui.market.ui.presenter;


import android.content.Context;
import android.text.TextUtils;

import com.beihui.market.api.Api;
import com.beihui.market.api.ResultEntity;
import com.beihui.market.base.BaseRxPresenter;
import com.beihui.market.base.Constant;
import com.beihui.market.entity.DebtChannel;
import com.beihui.market.entity.DebtDetail;
import com.beihui.market.entity.RewardPoint;
import com.beihui.market.helper.UserHelper;
import com.beihui.market.ui.contract.DebtNewContract;
import com.beihui.market.util.LogUtils;
import com.beihui.market.util.RxUtil;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;

public class DebtNewPresenter extends BaseRxPresenter implements DebtNewContract.Presenter {
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);

    private final int TEMPLATE_COMP = 1;
    private final int TEMPLATE_SAMPLE = 2;

    private Api api;
    private DebtNewContract.View view;
    private UserHelper userHelper;
    private DebtChannel debtChannel;
    /**
     * 该字段不为空，则为编辑账单模式
     */
    private DebtDetail debtDetail;

    private boolean hasDebtBeenAdded;

    @Inject
    DebtNewPresenter(Context context, Api api, DebtNewContract.View view, DebtChannel debtChannel) {
        this.api = api;
        this.view = view;
        this.debtChannel = debtChannel;

        this.userHelper = UserHelper.getInstance(context);
    }


    @Override
    public void attachDebtDetail(DebtDetail debtDetail) {
        this.debtDetail = debtDetail;
        if (this.debtDetail != null) {
            view.bindDebtDetail(this.debtDetail);
        }
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

        this.saveDebt(params);
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

        this.saveDebt(params);
    }

    private void saveDebt(final Map<String, Object> params) {
        view.showProgress();

        Observable.just(debtDetail != null ? debtDetail : new DebtDetail())
                .observeOn(Schedulers.io())
                .flatMap(new Function<DebtDetail, ObservableSource<ResultEntity>>() {
                    @Override
                    public ObservableSource<ResultEntity> apply(DebtDetail debtDetail) throws Exception {
                        //如果处于编辑模式，则先删除原有账单
                        if (debtDetail.getId() != null) {
                            return api.deleteDebt(userHelper.getProfile().getId(), debtDetail.getId());
                        }
                        //如果不需要删除原有账单，则发送一个成功的假请求
                        ResultEntity result = new ResultEntity();
                        result.setCode(1000000);
                        return Observable.just(result);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(new Function<ResultEntity, ObservableSource<Boolean>>() {
                    @Override
                    public ObservableSource<Boolean> apply(ResultEntity resultEntity) throws Exception {
                        //如果处于编辑模式，且删除原有账单不成功，则结束流程
                        if (!resultEntity.isSuccess()) {
                            view.dismissProgress();
                            view.showErrorMsg(resultEntity.getMsg());
                        }
                        return Observable.just(resultEntity.isSuccess());
                    }
                })
                .filter(new Predicate<Boolean>() {
                    @Override
                    public boolean test(Boolean aBoolean) throws Exception {
                        //不处于编辑模式或者删除成功则开始添加账单,否者流程结束
                        return aBoolean;
                    }
                })
                .observeOn(Schedulers.io())
                .flatMap(new Function<Boolean, ObservableSource<ResultEntity>>() {
                    @Override
                    public ObservableSource<ResultEntity> apply(Boolean aBoolean) throws Exception {
                        //删除成功，或者无需删除原有账单
                        return api.saveDebtImmediately(params);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(new Function<ResultEntity, ObservableSource<Boolean>>() {
                    @Override
                    public ObservableSource<Boolean> apply(ResultEntity result) throws Exception {
                        //保存账单不成功
                        if (!result.isSuccess()) {
                            view.dismissProgress();
                            view.showErrorMsg(result.getMsg());
                        }
                        //添加账单流程是否已成功走完
                        hasDebtBeenAdded = result.isSuccess();
                        return Observable.just(result.isSuccess());
                    }
                })
                .filter(new Predicate<Boolean>() {
                    @Override
                    public boolean test(Boolean aBoolean) throws Exception {
                        //如果保存账单成功，则查询积分任务，否者结束流程
                        return aBoolean;
                    }
                })
                .observeOn(Schedulers.io())
                .flatMap(new Function<Boolean, ObservableSource<ResultEntity<List<RewardPoint>>>>() {
                    @Override
                    public ObservableSource<ResultEntity<List<RewardPoint>>> apply(Boolean aBoolean) throws Exception {
                        //如果保存成功，则查询积分任务状态
                        return api.queryRewardPoints(userHelper.getProfile().getId(), Constant.REWARD_POINTS_TASK_NAME_ADD_DEBT);
                    }
                })
                .compose(RxUtil.<ResultEntity<List<RewardPoint>>>io2main())
                .subscribe(new Consumer<ResultEntity<List<RewardPoint>>>() {
                               @Override
                               public void accept(ResultEntity<List<RewardPoint>> result) throws Exception {
                                   //还款计划保存成功
                                   String msg = "记账成功";
                                   if (result.isSuccess()) {
                                       //检查积分任务数据
                                       List<RewardPoint> list = result.getData();
                                       int points = 0;
                                       if (list != null && list.size() > 0) {
                                           for (RewardPoint point : list) {
                                               //需要弹框
                                               if (point.getFlag() == 1) {
                                                   points += point.getInteg();
                                                   if (Constant.REWARD_POINTS_TASK_NAME_ADD_DEBT_FIRST.equals(point.getTaskName())) {
                                                       msg = "首次记账";
                                                   }
                                                   //设置已读状态
                                                   sendPoint(point.getRecordId());
                                               }
                                           }
                                       }
                                       if (points > 0) {
                                           msg += " 积分+" + points;
                                       }
                                   }
                                   view.dismissProgress();

                                   view.saveDebtSuccess(msg);
                               }
                           },
                        new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                view.dismissProgress();
                                logError(DebtNewPresenter.this, throwable);
                                //如果添加账单的流程已经走完，则表示账单已添加，做成功提示
                                if (hasDebtBeenAdded) {
                                    view.saveDebtSuccess("记账成功");
                                } else {
                                    view.showErrorMsg(generateErrorMsg(throwable));
                                }
                            }
                        });
    }

    private void sendPoint(String id) {
        Disposable dis = api.sendReadPointRead(id)
                .compose(RxUtil.<ResultEntity>io2main())
                .subscribe(new Consumer<ResultEntity>() {
                               @Override
                               public void accept(ResultEntity resultEntity) throws Exception {

                               }
                           },
                        new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                LogUtils.e(throwable);

                            }
                        });
        addDisposable(dis);
    }
}
