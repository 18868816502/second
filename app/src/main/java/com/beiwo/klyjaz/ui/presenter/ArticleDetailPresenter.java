package com.beiwo.klyjaz.ui.presenter;

import android.content.Context;

import com.beiwo.klyjaz.api.Api;
import com.beiwo.klyjaz.api.ResultEntity;
import com.beiwo.klyjaz.base.BaseRxPresenter;
import com.beiwo.klyjaz.helper.UserHelper;
import com.beiwo.klyjaz.social.bean.CommentReplyBean;
import com.beiwo.klyjaz.social.bean.PraiseBean;
import com.beiwo.klyjaz.ui.contract.ArticleDetailContact;
import com.beiwo.klyjaz.util.ParamsUtils;
import com.beiwo.klyjaz.util.RxUtil;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;


/**
 * @name loanmarket
 * @class name：com.beihui.market.ui.presenter
 * @class describe
 * @author A
 * @time 2018/9/11 17:19
 */
public class ArticleDetailPresenter extends BaseRxPresenter implements ArticleDetailContact.Presenter {

    private Api api;
    private ArticleDetailContact.View view;
    private UserHelper userHelper;

    @Inject
    ArticleDetailPresenter(Context context, Api api, ArticleDetailContact.View view) {
        this.api = api;
        this.view = view;
        userHelper = UserHelper.getInstance(context);
    }


    @Override
    public void queryCommentList(String forumId, int pageNo, int pageSize) {
        Disposable dis = api.queryCommentList(forumId,pageNo, pageSize)
                .compose(RxUtil.<ResultEntity<List<CommentReplyBean>>>io2main())
                .subscribe(new Consumer<ResultEntity<List<CommentReplyBean>>>() {
                               @Override
                               public void accept(ResultEntity<List<CommentReplyBean>> result) throws Exception {
                                   if (result.isSuccess()) {
                                       view.onQueryCommentSucceed(result.getData());
                                   } else {
                                       view.showErrorMsg(result.getMsg());
                                   }
                               }
                           },
                        new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                view.showErrorMsg(generateErrorMsg(throwable));
                            }
                        });
        addDisposable(dis);
    }

    @Override
    public void fetchReplyForumInfo(String userId, String commentType, String commentContent, String forumId, String toUserId, String selfId) {

//        Disposable dis = api.fetchReplyForumInfo(userHelper.getProfile().getId(),commentType, commentContent, forumId, toUserId, selfId)
        Disposable dis = api.fetchReplyForumInfo(ParamsUtils.generateCommentParams(userHelper.getProfile().getId(),commentType, commentContent, forumId, toUserId, selfId))
                .compose(RxUtil.<ResultEntity>io2main())
                .subscribe(new Consumer<ResultEntity>() {
                               @Override
                               public void accept(ResultEntity result){
                                   if (result.isSuccess()) {
                                       view.onReplyCommentSucceed();
                                   } else {
                                       view.showErrorMsg(result.getMsg());
                                   }
                               }
                           },
                        new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable){
                                view.showErrorMsg(generateErrorMsg(throwable));
                            }
                        });
        addDisposable(dis);
    }

    @Override
    public void fetchSaveReport(String userId, String linkId, String reportType, String reportContent) {
        Disposable dis = api.fetchSaveReport(userHelper.getProfile().getId(),linkId, reportType, reportContent)
                .compose(RxUtil.<ResultEntity>io2main())
                .subscribe(new Consumer<ResultEntity>() {
                               @Override
                               public void accept(ResultEntity result){
                                   if (result.isSuccess()) {
                                       view.onSaveReportSucceed();
                                   } else {
                                       view.showErrorMsg(result.getMsg());
                                   }
                               }
                           },
                        new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable){
                                view.showErrorMsg(generateErrorMsg(throwable));
                            }
                        });
        addDisposable(dis);
    }

    @Override
    public void fetchCancelForum(String forumId) {
        Disposable dis = api.fetchCancelForum(forumId)
                .compose(RxUtil.<ResultEntity>io2main())
                .subscribe(new Consumer<ResultEntity>() {
                               @Override
                               public void accept(ResultEntity result){
                                   if (result.isSuccess()) {
                                       view.onCancelForumSucceed();
                                   } else {
                                       view.showErrorMsg(result.getMsg());
                                   }
                               }
                           },
                        new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable){
                                view.showErrorMsg(generateErrorMsg(throwable));
                            }
                        });
        addDisposable(dis);
    }

    @Override
    public void fetchCancelReply(String replyId) {
        Disposable dis = api.fetchCancelReply(userHelper.getProfile().getId())
                .compose(RxUtil.<ResultEntity>io2main())
                .subscribe(new Consumer<ResultEntity>() {
                               @Override
                               public void accept(ResultEntity result){
                                   if (result.isSuccess()) {
                                       view.onCancelReplySucceed();
                                   } else {
                                       view.showErrorMsg(result.getMsg());
                                   }
                               }
                           },
                        new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable){
                                view.showErrorMsg(generateErrorMsg(throwable));
                            }
                        });
        addDisposable(dis);
    }

    @Override
    public void fetchClickPraise(int praiseType, String forumReplyId, String userId) {
        Disposable dis = api.fetchClickPraise(praiseType,forumReplyId,userHelper.getProfile().getId())
                .compose(RxUtil.<ResultEntity<PraiseBean>>io2main())
                .subscribe(new Consumer<ResultEntity<PraiseBean>>() {
                               @Override
                               public void accept(ResultEntity<PraiseBean> result){
                                   if (result.isSuccess()) {
                                       view.onPraiseSucceed();
                                   } else {
                                       view.showErrorMsg(result.getMsg());
                                   }
                               }
                           },
                        new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable){
                                view.showErrorMsg(generateErrorMsg(throwable));
                            }
                        });
        addDisposable(dis);
    }

    @Override
    public void fetchCancelPraise(int praiseType, String forumReplyId, String userId) {
        Disposable dis = api.fetchCancelPraise(praiseType,forumReplyId,userHelper.getProfile().getId())
                .compose(RxUtil.<ResultEntity>io2main())
                .subscribe(new Consumer<ResultEntity>() {
                               @Override
                               public void accept(ResultEntity result){
                                   if (result.isSuccess()) {
                                       view.OnCancelPraiseSucceed();
                                   } else {
                                       view.showErrorMsg(result.getMsg());
                                   }
                               }
                           },
                        new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable){
                                view.showErrorMsg(generateErrorMsg(throwable));
                            }
                        });
        addDisposable(dis);
    }
}
