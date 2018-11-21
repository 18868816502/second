package com.beiwo.klyjaz.social.presenter;

import android.content.Context;

import com.beiwo.klyjaz.api.Api;
import com.beiwo.klyjaz.api.ResultEntity;
import com.beiwo.klyjaz.base.BaseRxPresenter;
import com.beiwo.klyjaz.helper.UserHelper;
import com.beiwo.klyjaz.social.bean.CommentReplyBean;
import com.beiwo.klyjaz.social.bean.ForumInfoBean;
import com.beiwo.klyjaz.social.bean.PraiseBean;
import com.beiwo.klyjaz.social.contract.ForumDetailContact;
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
public class ForumDetailPresenter extends BaseRxPresenter implements ForumDetailContact.Presenter {

    private Api api;
    private ForumDetailContact.View view;
    private UserHelper userHelper;
    private Context mContext;


    public ForumDetailPresenter(Context context, ForumDetailContact.View view) {
        this.api = Api.getInstance();
        this.view = view;
        userHelper = UserHelper.getInstance(context);
        this.mContext = context;
    }


    @Override
    public void queryForumInfo(String forumId, int pageNo, int pageSize) {
        String userId = "";
        if (UserHelper.getInstance(mContext).isLogin()) {
            userId = UserHelper.getInstance(mContext).getProfile().getId();
        }
        Disposable dis = api.queryForumInfo(ParamsUtils.generateForumParams(userId,forumId,pageNo,pageSize))
                .compose(RxUtil.<ResultEntity<ForumInfoBean>>io2main())
                .subscribe(new Consumer<ResultEntity<ForumInfoBean>>() {
                               @Override
                               public void accept(ResultEntity<ForumInfoBean> result) throws Exception {
                                   if (result.isSuccess()) {
                                       view.onQueryForumInfoSucceed(result.getData());
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
    public void fetchSaveReport(String linkId, String reportType, String reportContent) {
        Disposable dis = api.fetchSaveReport(userHelper.id(),linkId, reportType, reportContent)
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
