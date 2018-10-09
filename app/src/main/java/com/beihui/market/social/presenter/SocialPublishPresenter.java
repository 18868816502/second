package com.beihui.market.social.presenter;

import android.content.Context;

import com.beihui.market.api.Api;
import com.beihui.market.api.ResultEntity;
import com.beihui.market.base.BaseRxPresenter;
import com.beihui.market.entity.Phone;
import com.beihui.market.helper.UserHelper;
import com.beihui.market.social.contract.SocialPublishContract;
import com.beihui.market.ui.contract.ResetPwdSetPwdContract;
import com.beihui.market.umeng.Events;
import com.beihui.market.umeng.Statistic;
import com.beihui.market.util.RxUtil;

import javax.inject.Inject;

import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

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
                                  String forumContent, String status,String topicId) {
        Disposable dis = mApi.publicForumInfo(mUserHelper.getProfile().getId(),imgKey, forumTitle,
                forumContent, status,topicId).compose(RxUtil.<ResultEntity>io2main())
                .subscribe(new Consumer<ResultEntity>() {
                               @Override
                               public void accept(@NonNull ResultEntity result) throws Exception {
                                   if (result.isSuccess()) {
                                       mView.onPublishTopicSucceed();
                                   } else {
                                       mView.showErrorMsg(result.getMsg());
                                   }
                               }
                           },
                        new Consumer<Throwable>() {
                            @Override
                            public void accept(@NonNull Throwable throwable) throws Exception {
                                logError(SocialPublishPresenter.this, throwable);
                                mView.showErrorMsg(generateErrorMsg(throwable));
                            }
                        });
        addDisposable(dis);
    }

    @Override
    public void uploadForumImg(int index,String base64) {
        Disposable dis = mApi.uploadFourmImg(base64).compose(RxUtil.<ResultEntity<String>>io2main())
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
                                logError(SocialPublishPresenter.this, throwable);
                                mView.showErrorMsg(generateErrorMsg(throwable));
                            }
                        });
        addDisposable(dis);
    }
}
