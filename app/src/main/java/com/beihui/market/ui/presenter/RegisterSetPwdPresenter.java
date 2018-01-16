package com.beihui.market.ui.presenter;


import android.content.Context;
import android.text.TextUtils;

import com.beihui.market.api.Api;
import com.beihui.market.api.ResultEntity;
import com.beihui.market.base.BaseRxPresenter;
import com.beihui.market.base.Constant;
import com.beihui.market.entity.RewardPoint;
import com.beihui.market.entity.UserProfileAbstract;
import com.beihui.market.helper.UserHelper;
import com.beihui.market.ui.contract.RegisterSetPwdContract;
import com.beihui.market.umeng.Events;
import com.beihui.market.umeng.Statistic;
import com.beihui.market.util.LogUtils;
import com.beihui.market.util.RxUtil;
import com.beihui.market.util.SPUtils;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;

public class RegisterSetPwdPresenter extends BaseRxPresenter implements RegisterSetPwdContract.Presenter {
    private Api api;
    private RegisterSetPwdContract.View view;
    private Context context;
    private UserHelper userHelper;

    @Inject
    RegisterSetPwdPresenter(Api api, RegisterSetPwdContract.View view, Context context) {
        this.api = api;
        this.view = view;
        this.context = context;
        this.userHelper = UserHelper.getInstance(context);
    }

    @Override
    public void register(String phone, String pwd, String inviteCode) {
        final String account = phone;
        final String password = pwd;
        //do not pass empty str to retrofit query annotation, if empty then set null
        if (TextUtils.isEmpty(inviteCode)) {
            inviteCode = null;
        }
        view.showLoading();

        Disposable dis = api.register(phone, pwd, inviteCode)
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(new Function<ResultEntity, ObservableSource<ResultEntity>>() {
                    @Override
                    public ObservableSource<ResultEntity> apply(ResultEntity resultEntity) throws Exception {
                        if (resultEntity.isSuccess()) {
                            //umeng统计
                            Statistic.onEvent(Events.REGISTER_SUCCESS);
                        } else {
                            //umeng统计
                            Statistic.onEvent(Events.REGISTER_FAILED);
                            view.showErrorMsg(resultEntity.getMsg());
                        }
                        return Observable.just(resultEntity);
                    }
                })
                .filter(new Predicate<ResultEntity>() {
                    @Override
                    public boolean test(ResultEntity resultEntity) throws Exception {
                        //注册失败则停止流程
                        return resultEntity.isSuccess();
                    }
                })
                .observeOn(Schedulers.io())
                .flatMap(new Function<ResultEntity, ObservableSource<ResultEntity<UserProfileAbstract>>>() {
                    @Override
                    public ObservableSource<ResultEntity<UserProfileAbstract>> apply(ResultEntity resultEntity) throws Exception {
                        //注册成功后，直接登录
                        return api.login(account, password).subscribeOn(Schedulers.io());
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(new Function<ResultEntity<UserProfileAbstract>, ObservableSource<ResultEntity<String>>>() {
                    @Override
                    public ObservableSource<ResultEntity<String>> apply(ResultEntity<UserProfileAbstract> result) throws Exception {
                        if (result.isSuccess()) {
                            //umeng统计
                            Statistic.login(result.getData().getId());

                            //登录成功后，将用户信息注册到本地
                            userHelper.update(result.getData(), account, context);
                            //保存用户id,缓存
                            SPUtils.setCacheUserId(context, result.getData().getId());
                        } else {
                            view.showErrorMsg(result.getMsg());
                        }
                        ResultEntity<String> newRes = new ResultEntity<>();
                        newRes.setCode(result.getCode());
                        newRes.setMsg(result.getMsg());
                        newRes.setData(result.getData().getId());
                        return Observable.just(newRes);
                    }
                })
                .filter(new Predicate<ResultEntity<String>>() {
                    @Override
                    public boolean test(ResultEntity<String> result) throws Exception {
                        //如果登录失败，则结束流程
                        return result.isSuccess();
                    }
                })
                .observeOn(Schedulers.io())
                .flatMap(new Function<ResultEntity<String>, ObservableSource<ResultEntity<List<RewardPoint>>>>() {
                    @Override
                    public ObservableSource<ResultEntity<List<RewardPoint>>> apply(ResultEntity<String> resultEntity) throws Exception {
                        //登录成功，查询积分任务
                        return api.queryRewardPoints(resultEntity.getData(), Constant.REWARD_POINTS_TASK_NAME_REGISTER);
                    }
                })
                .compose(RxUtil.<ResultEntity<List<RewardPoint>>>io2main())
                .subscribe(new Consumer<ResultEntity<List<RewardPoint>>>() {
                               @Override
                               public void accept(ResultEntity<List<RewardPoint>> result) throws Exception {
                                   String msg = null;
                                   if (result.isSuccess()) {
                                       int points = 0;
                                       if (result.getData() != null && result.getData().size() > 0) {
                                           for (RewardPoint point : result.getData()) {
                                               if (point.getStatus() == 1) {
                                                   points += point.getInteg();
                                                   //设置积分任务状态
                                                   sendPoint(point.getRecordId());
                                               }
                                           }
                                       }
                                       if (points > 0) {
                                           msg = "注册成功 积分+" + points;
                                       }
                                   }
                                   view.showRegisterSuccess(msg);
                               }
                           },
                        new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                logError(RegisterSetPwdPresenter.this, throwable);
                                view.showErrorMsg(generateErrorMsg(throwable));
                            }
                        });
        addDisposable(dis);
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
