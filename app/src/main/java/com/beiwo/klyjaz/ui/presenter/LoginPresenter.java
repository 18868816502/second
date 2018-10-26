package com.beiwo.klyjaz.ui.presenter;


import android.content.Context;

import com.beiwo.klyjaz.api.Api;
import com.beiwo.klyjaz.api.ResultEntity;
import com.beiwo.klyjaz.base.BaseRxPresenter;
import com.beiwo.klyjaz.base.Constant;
import com.beiwo.klyjaz.entity.RewardPoint;
import com.beiwo.klyjaz.entity.UserProfileAbstract;
import com.beiwo.klyjaz.helper.UserHelper;
import com.beiwo.klyjaz.ui.contract.LoginContract;
import com.beiwo.klyjaz.umeng.Events;
import com.beiwo.klyjaz.umeng.Statistic;
import com.beiwo.klyjaz.util.LogUtils;
import com.beiwo.klyjaz.util.RxUtil;
import com.beiwo.klyjaz.util.SPUtils;

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

public class LoginPresenter extends BaseRxPresenter implements LoginContract.Presenter {
    private Api api;
    private LoginContract.View view;
    private Context context;
    private UserHelper userHelper;

    @Inject
    LoginPresenter(Api api, LoginContract.View view, Context context) {
        this.api = api;
        this.view = view;
        this.context = context;
        this.userHelper = UserHelper.getInstance(context);
    }

    @Override
    public void login(String account, String pwd) {
        final String phone = account;
        view.showProgress();

        Disposable dis = api.login(account, pwd)
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(new Function<ResultEntity<UserProfileAbstract>, ObservableSource<ResultEntity<String>>>() {
                    @Override
                    public ObservableSource<ResultEntity<String>> apply(ResultEntity<UserProfileAbstract> result) throws Exception {
                        ResultEntity<String> newRes = new ResultEntity<>();
                        newRes.setCode(result.getCode());
                        newRes.setMsg(result.getMsg());

                        if (result.isSuccess()) {
                            //umeng统计
                            Statistic.onEvent(Events.LOGIN_SUCCESS);
                            Statistic.login(result.getData().getId());

                            //登录之后，将用户信息注册到本地
                            userHelper.update(result.getData(), phone, context);
                            //保存用户id,缓存
                            SPUtils.setCacheUserId(result.getData().getId());

                            //向下传递id
                            newRes.setData(result.getData().getId());
                        } else {
                            view.dismissProgress();

                            //umeng统计
                            Statistic.onEvent(Events.LOGIN_FAILED);
                            view.showErrorMsg(result.getMsg());
                        }

                        return Observable.just(newRes);
                    }
                })
                .filter(new Predicate<ResultEntity<String>>() {
                    @Override
                    public boolean test(ResultEntity<String> resultEntity) throws Exception {
                        //如果登录失败，则结束
                        return resultEntity.isSuccess();
                    }
                })
                .observeOn(Schedulers.io())
                .flatMap(new Function<ResultEntity<String>, ObservableSource<ResultEntity<List<RewardPoint>>>>() {
                    @Override
                    public ObservableSource<ResultEntity<List<RewardPoint>>> apply(ResultEntity<String> result) throws Exception {
                        //登录成功，查询积分任务
                        return api.queryRewardPoints(result.getData(), Constant.REWARD_POINTS_TASK_NAME_LOGIN);
                    }
                })
                .compose(RxUtil.<ResultEntity<List<RewardPoint>>>io2main())
                .subscribe(new Consumer<ResultEntity<List<RewardPoint>>>() {
                               @Override
                               public void accept(ResultEntity<List<RewardPoint>> result) throws Exception {
                                   view.dismissProgress();
                                   //查询积分任何无论结果如何，都要通知登录操作成功
                                   String msg = "登录成功";
                                   if (result.isSuccess()) {
                                       int points = 0;
                                       if (result.getData() != null && result.getData().size() > 0) {
                                           for (RewardPoint point : result.getData()) {
                                               if (point.getFlag() == 1) {
                                                   //显示
                                                   points += point.getInteg();
                                                   //设置积分任务状态
                                                   sendPoint(point.getRecordId());
                                               }
                                           }
                                       }

                                       if (points > 0) {
                                           msg += " 积分+" + points;
                                       }
                                   }
                                   view.showLoginSuccess(msg);
                               }
                           },
                        new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                view.dismissProgress();
                                logError(LoginPresenter.this, throwable);
                                view.showErrorMsg(generateErrorMsg(throwable));
                            }
                        });
        addDisposable(dis);
    }

    @Override
    public void loginWithWeChat(String openId) {
        view.showProgress();
        Disposable dis = api.loginWithWechat(openId)
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(new Function<ResultEntity<UserProfileAbstract>, ObservableSource<ResultEntity<String>>>() {
                    @Override
                    public ObservableSource<ResultEntity<String>> apply(ResultEntity<UserProfileAbstract> result) throws Exception {
                        ResultEntity<String> newRes = new ResultEntity<>();
                        newRes.setCode(result.getCode());
                        newRes.setMsg(result.getMsg());

                        if (result.isSuccess()) {
                            //umeng统计
                            Statistic.onEvent(Events.LOGIN_SUCCESS);
                            Statistic.login(result.getData().getId());

                            //登录之后，将用户信息注册到本地
                            userHelper.update(result.getData(), result.getData().getAccount(), context);
                            //保存用户id,缓存
                            SPUtils.setCacheUserId(result.getData().getId());

                            newRes.setData(result.getData().getId());

                        } else {
                            //umeng统计
                            Statistic.onEvent(Events.LOGIN_FAILED);

                            view.dismissProgress();
                            if (result.getCode() == 100008) {
                                //未绑定手机号
                                view.navigateWechatBindAccount();
                            } else {
                                //未知错误
                                view.showErrorMsg(result.getMsg());
                            }
                        }
                        //向下传递id
                        return Observable.just(newRes);
                    }
                })
                .filter(new Predicate<ResultEntity<String>>() {
                    @Override
                    public boolean test(ResultEntity<String> stringResultEntity) throws Exception {
                        //如果登录失败则结束流程
                        return stringResultEntity.isSuccess();
                    }
                })
                .observeOn(Schedulers.io())
                .flatMap(new Function<ResultEntity<String>, ObservableSource<ResultEntity<List<RewardPoint>>>>() {
                    @Override
                    public ObservableSource<ResultEntity<List<RewardPoint>>> apply(ResultEntity<String> result) throws Exception {
                        //登录成功，查询积分任务
                        return api.queryRewardPoints(result.getData(), Constant.REWARD_POINTS_TASK_NAME_LOGIN);
                    }
                })
                .compose(RxUtil.<ResultEntity<List<RewardPoint>>>io2main())
                .subscribe(new Consumer<ResultEntity<List<RewardPoint>>>() {
                               @Override
                               public void accept(ResultEntity<List<RewardPoint>> result) throws Exception {
                                   view.dismissProgress();
                                   //查询积分任何无论结果如何，都要通知登录操作成功
                                   String msg = "登录成功";
                                   if (result.isSuccess()) {
                                       int points = 0;
                                       if (result.getData() != null && result.getData().size() > 0) {
                                           for (RewardPoint point : result.getData()) {
                                               if (point.getFlag() == 1) {
                                                   //显示
                                                   points += point.getInteg();
                                                   //设置积分任务状态
                                                   sendPoint(point.getRecordId());
                                               }
                                           }
                                       }

                                       if (points > 0) {
                                           msg += " 积分+" + points;
                                       }
                                   }
                                   view.showLoginSuccess(msg);
                               }
                           },
                        new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                view.dismissProgress();
                                logError(LoginPresenter.this, throwable);
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
                                LogUtils.w(throwable);

                            }
                        });
        addDisposable(dis);
    }
}
