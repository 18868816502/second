package com.beihui.market.social.presenter;

import android.content.Context;
import android.graphics.Bitmap;

import com.beihui.market.api.Api;
import com.beihui.market.api.ResultEntity;
import com.beihui.market.base.BaseRxPresenter;
import com.beihui.market.entity.Avatar;
import com.beihui.market.entity.Phone;
import com.beihui.market.entity.request.RequestConstants;
import com.beihui.market.helper.UserHelper;
import com.beihui.market.social.contract.SocialPublishContract;
import com.beihui.market.tang.rx.RxResponse;
import com.beihui.market.tang.rx.observer.ApiObserver;
import com.beihui.market.ui.contract.ResetPwdSetPwdContract;
import com.beihui.market.ui.presenter.UserProfilePresenter;
import com.beihui.market.umeng.Events;
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

public class SocialPublishPresenter extends BaseRxPresenter implements SocialPublishContract.Presenter {

    private Api mApi;
    private SocialPublishContract.View mView;
    private Context mContext;
    private UserHelper mUserHelper;

    private boolean isRequestingVerification;

    @Inject
    public SocialPublishPresenter(Api api, SocialPublishContract.View view, Context context) {
        mApi = api;
        mView = view;
        mContext = context;
        mUserHelper = UserHelper.getInstance(context);
    }


    @Override
    public void saveDraft(String userId, String title, String content) {

    }

    @Override
    public void fetchPublishTopic(String imgKey, String forumTitle,
                                  String forumContent, String status, String topicId, String forumId) {
        /*Disposable dis = mApi.publicForumInfo(mUserHelper.getProfile().getId(), imgKey, forumTitle, forumContent, status, topicId, forumId)
                .compose(RxUtil.<ResultEntity>io2main())
                .subscribe(new Consumer<ResultEntity>() {
                               @Override
                               public void accept(@NonNull ResultEntity result) {
                                   if (result.isSuccess()) {
                                       mView.onPublishTopicSucceed();
                                   } else {
                                       mView.showErrorMsg(result.getMsg());
                                   }
                               }
                           },
                        new Consumer<Throwable>() {
                            @Override
                            public void accept(@NonNull Throwable throwable) {
                                logError(SocialPublishPresenter.this, throwable);
                                mView.showErrorMsg(generateErrorMsg(throwable));
                            }
                        });
        addDisposable(dis);*/
        mApi.publicForumInfo(mUserHelper.getProfile().getId(), imgKey, forumTitle, forumContent, status, topicId, forumId)
                .compose(RxResponse.compatO())
                .subscribe(new ApiObserver<Object>() {
                    @Override
                    public void onNext(@NonNull Object data) {
                        mView.onPublishTopicSucceed();
                    }

                    @Override
                    public void onError(@NonNull Throwable t) {
                        super.onError(t);
                        logError(SocialPublishPresenter.this, t);
                        mView.showErrorMsg(generateErrorMsg(t));
                    }
                });
    }

    @Override
    public void uploadForumImg(Bitmap bitmap) {
//        Disposable dis = mApi.uploadFourmImg(base64).compose(RxUtil.<ResultEntity<String>>io2main())
//                .subscribe(new Consumer<ResultEntity<String>>() {
//                               @Override
//                               public void accept(@NonNull ResultEntity<String> result) throws Exception {
//                                   if (result.isSuccess()) {
//                                       mView.onUploadImgSucceed(result.getData());
//                                   } else {
//                                       mView.showErrorMsg(result.getMsg());
//                                   }
//                               }
//                           },
//                        new Consumer<Throwable>() {
//                            @Override
//                            public void accept(@NonNull Throwable throwable) throws Exception {
//                                logError(SocialPublishPresenter.this, throwable);
//                                mView.showErrorMsg(generateErrorMsg(throwable));
//                            }
//                        });
//        addDisposable(dis);

        Disposable dis = Observable.just(bitmap)
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
                .flatMap(new Function<byte[], ObservableSource<ResultEntity<String>>>() {
                    @Override
                    public ObservableSource<ResultEntity<String>> apply(@NonNull byte[] bytes) throws Exception {
                        return mApi.uploadFourmImg(bytes);
                    }
                })
                .compose(RxUtil.<ResultEntity<String>>io2main())
                .subscribe(new Consumer<ResultEntity<String>>() {
                               @Override
                               public void accept(@NonNull ResultEntity<String> result) throws Exception {
                                   if (result.isSuccess()) {
                                       mView.onUploadImgSucceed(result.getData());
                                   } else {
                                       mView.showErrorMsg(result.getMsg());
                                   }
                               }
                           },
                        new Consumer<Throwable>() {
                            @Override
                            public void accept(@NonNull Throwable throwable) throws Exception {
//                                logError(UserProfilePresenter.this, throwable);
                                mView.showErrorMsg(generateErrorMsg(throwable));
                            }
                        });
        addDisposable(dis);
    }
}
