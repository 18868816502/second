package com.beiwo.klyjaz.jjd;


import android.content.Context;


import com.beiwo.klyjaz.api.Api;
import com.beiwo.klyjaz.api.ResultEntity;
import com.beiwo.klyjaz.base.BaseRxPresenter;
import com.beiwo.klyjaz.helper.UserHelper;
import com.beiwo.klyjaz.util.RxUtil;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * @author chenguoguo
 */
public class VertifyIDPresenter extends BaseRxPresenter implements VertifyIDContract.Presenter {
    private Api api;
    private VertifyIDContract.View view;
    private Context context;
    private UserHelper userHelper;

    public VertifyIDPresenter(VertifyIDContract.View view, Context context) {
        this.api = Api.getInstance();
        this.view = view;
        this.context = context;
        this.userHelper = UserHelper.getInstance(context);
    }

    @Override
    public void fetchVertifyIDCard(String idName, String idNo) {
        Disposable dis = api.fetchVertifyIDCard(userHelper.id(), idName, idNo)
                .compose(RxUtil.<ResultEntity>io2main())
                .subscribe(new Consumer<ResultEntity>() {
                               @Override
                               public void accept(ResultEntity resultEntity) {
                                   if (resultEntity.isSuccess()) {
                                       view.onVertifyIDCardSucceed();
                                   } else {
                                       view.showErrorMsg(resultEntity.getMsg());
                                   }
                               }
                           },
                        new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) {
                                view.showErrorMsg(generateErrorMsg(throwable));
                            }
                        });
        addDisposable(dis);
    }

    @Override
    public void fetchSaveContact(String userContact, String userRelate, String mobileNum) {
        Disposable dis = api.fetchSaveContact(userHelper.id(), userContact, userRelate, mobileNum)
                .compose(RxUtil.<ResultEntity>io2main())
                .subscribe(new Consumer<ResultEntity>() {
                               @Override
                               public void accept(ResultEntity resultEntity) {
                                   if (resultEntity.isSuccess()) {
                                       view.onSaveContactSucceed();
                                   } else {
                                       view.showErrorMsg(resultEntity.getMsg());
                                   }
                               }
                           },
                        new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) {
                                view.showErrorMsg(generateErrorMsg(throwable));
                            }
                        });
        addDisposable(dis);
    }
}