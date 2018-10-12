package com.beihui.market.ui.presenter;

import com.beihui.market.api.Api;
import com.beihui.market.api.ResultEntity;
import com.beihui.market.base.BaseRxPresenter;
import com.beihui.market.entity.UserTopicBean;
import com.beihui.market.entity.UserInfoBean;
import com.beihui.market.ui.contract.PersonalCenterContact;
import com.beihui.market.util.RxUtil;

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
public class PersonalCenterPresenter extends BaseRxPresenter implements PersonalCenterContact.Presenter {

    private Api api;
    private PersonalCenterContact.View view;

    @Inject
    PersonalCenterPresenter(Api api, PersonalCenterContact.View view) {
        this.api = api;
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
