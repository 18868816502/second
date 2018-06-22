package com.beihui.market.ui.presenter;


import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;

import com.beihui.market.api.Api;
import com.beihui.market.api.ResultEntity;
import com.beihui.market.base.BaseRxPresenter;
import com.beihui.market.entity.AppUpdate;
import com.beihui.market.entity.Avatar;
import com.beihui.market.entity.UserProfile;
import com.beihui.market.entity.request.RequestConstants;
import com.beihui.market.helper.UserHelper;
import com.beihui.market.ui.contract.UserProfileContract;
import com.beihui.market.umeng.Statistic;
import com.beihui.market.util.RxUtil;

import java.io.ByteArrayOutputStream;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class UserProfilePresenter extends BaseRxPresenter implements UserProfileContract.Presenter {

    private Api mApi;
    private UserProfileContract.View mView;
    private Context mContext;
    private UserHelper mUserHelper;

    private AppUpdate appUpdate;

    @Inject
    UserProfilePresenter(Api api, UserProfileContract.View view, Context context) {
        mApi = api;
        mView = view;
        mContext = context;
        mUserHelper = UserHelper.getInstance(context);
    }

    @Override
    public void onStart() {
        super.onStart();


        UserHelper.Profile profile = mUserHelper.getProfile();
        if (profile != null) {
            mView.showProfile(profile);

            mView.showUserName(profile.getUserName());

            Disposable dis = mApi.queryUserProfile(profile.getId())
                    .compose(RxUtil.<ResultEntity<UserProfile>>io2main())
                    .subscribe(new Consumer<ResultEntity<UserProfile>>() {
                                   @Override
                                   public void accept(@NonNull ResultEntity<UserProfile> result) throws Exception {
                                       if (result.isSuccess()) {
                                           mUserHelper.update(result.getData(), mContext);
                                           mView.showProfile(mUserHelper.getProfile());
                                       } else {
                                           mView.showErrorMsg(result.getMsg());
                                       }
                                   }
                               },
                            new Consumer<Throwable>() {
                                @Override
                                public void accept(@NonNull Throwable throwable) throws Exception {
                                    logError(UserProfilePresenter.this, throwable);
                                    mView.showErrorMsg(generateErrorMsg(throwable));
                                }
                            });
            addDisposable(dis);
        }

        Disposable disposable = mApi.queryUpdate()
                .compose(RxUtil.<ResultEntity<AppUpdate>>io2main())
                .subscribe(new Consumer<ResultEntity<AppUpdate>>() {
                               @Override
                               public void accept(@NonNull ResultEntity<AppUpdate> result) throws Exception {
                                   if (result.isSuccess()) {
                                       if (result.getData() != null) {
                                           appUpdate = result.getData();
                                           String version = mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), 0).versionName;
                                           if (version.compareTo(appUpdate.getVersion()) < 0) {
                                               mView.showLatestVersion("最新版本"+appUpdate.getVersion());
                                           } else {
                                               mView.showLatestVersion("已是最新版");
                                           }
                                       } else {
                                           mView.showLatestVersion("已是最新版");
                                       }
                                   }
                               }
                           },
                        new Consumer<Throwable>() {
                            @Override
                            public void accept(@NonNull Throwable throwable) throws Exception {
                                logError(UserProfilePresenter.this, throwable);
                            }
                        });
        addDisposable(disposable);

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
                                logError(UserProfilePresenter.this, throwable);
                                mView.showErrorMsg(generateErrorMsg(throwable));
                            }
                        });
        addDisposable(dis);
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
                                    logError(UserProfilePresenter.this, throwable);
                                    mView.showErrorMsg(generateErrorMsg(throwable));
                                }
                            });
            addDisposable(disposable);
        }
    }

    @Override
    public void updateAvatar(final Bitmap avatar) {
        mView.showProgress();
        Disposable dis = Observable.just(avatar)
                .observeOn(Schedulers.io())
                .map(new Function<Bitmap, byte[]>() {
                    @Override
                    public byte[] apply(@io.reactivex.annotations.NonNull Bitmap source) throws Exception {
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        int quality = 100;
                        source.compress(Bitmap.CompressFormat.JPEG, quality, baos);
                        while (baos.size() > RequestConstants.AVATAR_BYTE_SIZE) {
                            quality -= 5;
                            if (quality <= 0) {
                                quality = 0;
                            }
                            baos.reset();
                            source.compress(Bitmap.CompressFormat.JPEG, quality, baos);
                            if (quality == 0) {
                                break;
                            }
                        }
                        return baos.toByteArray();
                    }
                })
                .observeOn(Schedulers.io())
                .flatMap(new Function<byte[], ObservableSource<ResultEntity<Avatar>>>() {
                    @Override
                    public ObservableSource<ResultEntity<Avatar>> apply(@NonNull byte[] bytes) throws Exception {
                        String fileName = System.currentTimeMillis() + ".jpg";
                        return mApi.updateUserAvatar(mUserHelper.getProfile().getId(), fileName, bytes);
                    }
                })
                .compose(RxUtil.<ResultEntity<Avatar>>io2main())
                .subscribe(new Consumer<ResultEntity<Avatar>>() {
                               @Override
                               public void accept(@NonNull ResultEntity<Avatar> result) throws Exception {
                                   if (result.isSuccess()) {
                                       mView.showAvatarUpdateSuccess(result.getData().getFilePath());
                                       mUserHelper.updateAvatar(result.getData().getFilePath(), mContext);
                                   } else {
                                       mView.showErrorMsg(result.getMsg());
                                   }
                               }
                           },
                        new Consumer<Throwable>() {
                            @Override
                            public void accept(@NonNull Throwable throwable) throws Exception {
                                logError(UserProfilePresenter.this, throwable);
                                mView.showErrorMsg(generateErrorMsg(throwable));
                            }
                        });
        addDisposable(dis);
    }

    @Override
    public void updateUserName(String username) {
        final String name = username;
        mView.showProgress();
        Disposable dis = mApi.updateUsername(mUserHelper.getProfile().getId(), username)
                .compose(RxUtil.<ResultEntity>io2main())
                .subscribe(new Consumer<ResultEntity>() {
                               @Override
                               public void accept(@NonNull ResultEntity result) throws Exception {
                                   if (result.isSuccess()) {
                                       mUserHelper.updateUsername(name, mContext);
                                       mView.showUpdateNameSuccess(result.getMsg());
                                   } else {
                                       mView.showErrorMsg(result.getMsg());
                                   }
                               }
                           },
                        new Consumer<Throwable>() {
                            @Override
                            public void accept(@NonNull Throwable throwable) throws Exception {
                                logError(UserProfilePresenter.this, throwable);
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
                    mView.showHasBeenLatest("已经是最新版本了");
                }
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
        } else {
            mView.showHasBeenLatest("已经是最新版了");
        }
    }
}
