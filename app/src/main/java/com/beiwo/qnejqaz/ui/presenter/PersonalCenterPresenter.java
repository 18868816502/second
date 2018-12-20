package com.beiwo.qnejqaz.ui.presenter;

import com.beiwo.qnejqaz.api.Api;
import com.beiwo.qnejqaz.api.ResultEntity;
import com.beiwo.qnejqaz.base.BaseRxPresenter;
import com.beiwo.qnejqaz.entity.UserTopicBean;
import com.beiwo.qnejqaz.entity.UserInfoBean;
import com.beiwo.qnejqaz.ui.contract.PersonalCenterContact;
import com.beiwo.qnejqaz.util.RxUtil;

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
public class PersonalCenterPresenter extends BaseRxPresenter implements PersonalCenterContact.Presenter {

    private Api api;
    private PersonalCenterContact.View view;

    public PersonalCenterPresenter(PersonalCenterContact.View view) {
        this.api = Api.getInstance();
        this.view = view;
    }

    @Override
    public void fetchPersonalInfo(String userId) {
        Disposable dis = api.queryUserInfo(userId)
                .compose(RxUtil.<ResultEntity<UserInfoBean>>io2main())
                .subscribe(new Consumer<ResultEntity<UserInfoBean>>() {
                               @Override
                               public void accept(ResultEntity<UserInfoBean> resultEntity){
                                   if (resultEntity.isSuccess()) {
                                       view.onQueryUserInfoSucceed(resultEntity.getData());
                                   } else {
                                       view.showErrorMsg(resultEntity.getMsg());
                                   }
                               }
                           },
                        new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable){
                                logError(PersonalCenterPresenter.this, throwable);
                                view.showErrorMsg(generateErrorMsg(throwable));
                            }
                        });
        addDisposable(dis);
    }

    @Override
    public void fetchPersonalTopic(String userId,int pageNo,int pageSize) {
        Disposable dis = api.queryUserTopicInfo(userId,pageNo,pageSize)
                .compose(RxUtil.<ResultEntity<List<UserTopicBean>>>io2main())
                .subscribe(new Consumer<ResultEntity<List<UserTopicBean>>>() {
                               @Override
                               public void accept(ResultEntity<List<UserTopicBean>> resultEntity){
                                   if (resultEntity.isSuccess()) {
                                       view.onQueryUserTopicSucceed(resultEntity.getData());
                                   } else {
                                       view.showErrorMsg(resultEntity.getMsg());
                                   }
                               }
                           },
                        new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable){
                                logError(PersonalCenterPresenter.this, throwable);
                                view.showErrorMsg(generateErrorMsg(throwable));
                            }
                        });
        addDisposable(dis);
    }
}
