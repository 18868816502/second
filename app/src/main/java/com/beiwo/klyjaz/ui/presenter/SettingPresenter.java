package com.beiwo.klyjaz.ui.presenter;


import android.content.Context;
import android.content.pm.PackageManager;

import com.beiwo.klyjaz.api.Api;
import com.beiwo.klyjaz.api.ResultEntity;
import com.beiwo.klyjaz.base.BaseRxPresenter;
import com.beiwo.klyjaz.entity.AppUpdate;
import com.beiwo.klyjaz.helper.UserHelper;
import com.beiwo.klyjaz.ui.contract.SettingContract;
import com.beiwo.klyjaz.umeng.Statistic;
import com.beiwo.klyjaz.util.RxUtil;

import javax.inject.Inject;

import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class SettingPresenter extends BaseRxPresenter implements SettingContract.Presenter {

    private Api mApi;
    private SettingContract.View mView;
    private Context mContext;
    private UserHelper mUserHelper;

    private AppUpdate appUpdate;

    @Inject
    SettingPresenter(Api api, SettingContract.View view, Context context) {
        mApi = api;
        mView = view;
        mContext = context;
        mUserHelper = UserHelper.getInstance(context);
    }

    @Override
    public void onStart() {
        super.onStart();
        Disposable disposable = mApi.queryUpdate()
                .compose(RxUtil.<ResultEntity<AppUpdate>>io2main())
                .subscribe(new Consumer<ResultEntity<AppUpdate>>() {
                               @Override
                               public void accept(@NonNull ResultEntity<AppUpdate> result) throws Exception {
                                   if (result.isSuccess()) {
                                       if (result.getData() != null) {
                                           appUpdate = result.getData();
                                           String version = mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), 0).versionName;
                                           if (version.compareTo(appUpdate.getVersion()) < 0)
                                               mView.showLatestVersion("可更新");
                                       }
                                   }
                               }
                           },
                        new Consumer<Throwable>() {
                            @Override
                            public void accept(@NonNull Throwable throwable) throws Exception {
                                logError(SettingPresenter.this, throwable);
                            }
                        });
        addDisposable(disposable);
    }

    @Override
    public void checkVersion() {
        if (appUpdate != null) {
            handleAppUpdate(appUpdate);
        } else {
            Disposable disposable = mApi.queryUpdate()
                    .compose(RxUtil.<ResultEntity<AppUpdate>>io2main())
                    .subscribe(new Consumer<ResultEntity<AppUpdate>>() {
                                   @Override
                                   public void accept(@NonNull ResultEntity<AppUpdate> result) throws Exception {
                                       if (result.isSuccess()) {
                                           appUpdate = result.getData();
                                           handleAppUpdate(appUpdate);
                                       } else {
                                           mView.showErrorMsg(result.getMsg());
                                       }
                                   }
                               },
                            new Consumer<Throwable>() {
                                @Override
                                public void accept(@NonNull Throwable throwable) throws Exception {
                                    logError(SettingPresenter.this, throwable);
                                    mView.showErrorMsg(generateErrorMsg(throwable));
                                }
                            });
            addDisposable(disposable);
        }
    }

    @Override
    public void logout() {
        Disposable dis = mApi.logout(mUserHelper.getProfile().getId())
                .compose(RxUtil.<ResultEntity>io2main())
                .subscribe(new Consumer<ResultEntity>() {
                               @Override
                               public void accept(@NonNull ResultEntity result) throws Exception {
                                   if (result.isSuccess()) {
                                       mUserHelper.clearUser(mContext);
                                       mView.showLogoutSuccess();
                                       //umeng统计
                                       Statistic.logout();
                                   } else {
                                       mView.showErrorMsg(result.getMsg());
                                   }
                               }
                           },
                        new Consumer<Throwable>() {
                            @Override
                            public void accept(@NonNull Throwable throwable) throws Exception {
                                logError(SettingPresenter.this, throwable);
                                mView.showErrorMsg(generateErrorMsg(throwable));
                            }
                        });
        addDisposable(dis);
    }

    private void handleAppUpdate(AppUpdate update) {
        if (update != null) {
            try {
                String version = mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), 0).versionName;
                if (version.compareTo(update.getVersion()) < 0) {
                    mView.showUpdate(appUpdate);
                } else {
                    mView.showHasBeenLatest("当前已是最新版本");
                }
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
        } else {
            mView.showHasBeenLatest("当前已是最新版本");
        }
    }
}
