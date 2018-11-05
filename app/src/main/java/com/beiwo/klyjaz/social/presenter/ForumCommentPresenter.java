package com.beiwo.klyjaz.social.presenter;

import android.content.Context;

import com.beiwo.klyjaz.api.Api;
import com.beiwo.klyjaz.api.ResultEntity;
import com.beiwo.klyjaz.base.BaseRxPresenter;
import com.beiwo.klyjaz.helper.UserHelper;
import com.beiwo.klyjaz.social.bean.CommentReplyBean;
import com.beiwo.klyjaz.social.bean.ForumInfoBean;
import com.beiwo.klyjaz.social.bean.PraiseBean;
import com.beiwo.klyjaz.social.contract.ForumCommentContact;
import com.beiwo.klyjaz.social.contract.ForumDetailContact;
import com.beiwo.klyjaz.util.ParamsUtils;
import com.beiwo.klyjaz.util.RxUtil;

import java.util.List;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;


/**
 * @name loanmarket
 * @class name：com.beihui.market.ui.presenter
 * @class describe
 * @author A
 * @time 2018/9/11 17:19
 */
public class ForumCommentPresenter extends BaseRxPresenter implements ForumCommentContact.Presenter {

    private Api api;
    private ForumCommentContact.View view;
    private UserHelper userHelper;


    public ForumCommentPresenter(Context context, ForumCommentContact.View view) {
        this.api = Api.getInstance();
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
    public void fetchReplyInfo(String userId, String commentType, String commentContent, String forumId, String toUserId, String selfId, String replyId, String replyContent) {
        Disposable dis = api.fetchReplyForumInfo(ParamsUtils.generateCommentParams(userHelper.getProfile().getId(),commentType,
                commentContent, forumId, toUserId, selfId,replyId,replyContent))
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
    public void fetchCancelReply(String replyId) {
        Disposable dis = api.fetchCancelReply(replyId)
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


}
