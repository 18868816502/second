package com.beihui.market.ui.presenter;


import com.beihui.market.api.Api;
import com.beihui.market.api.ResultEntity;
import com.beihui.market.base.BaseRxPresenter;
import com.beihui.market.entity.Announce;
import com.beihui.market.ui.contract.AnnounceContract;
import com.beihui.market.util.RxUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class AnnouncePresenter extends BaseRxPresenter implements AnnounceContract.Presenter {

    private static final int PAGE_SIZE = 10;

    private Api mApi;
    private AnnounceContract.View mView;
    private int curPage;

    private List<Announce.Row> announceList;

    @Inject
    AnnouncePresenter(Api api, AnnounceContract.View view) {
        mApi = api;
        mView = view;
    }

    @Override
    public void onStart() {
        super.onStart();
        curPage = 1;
        Disposable dis = mApi.queryAnnounceList(curPage, PAGE_SIZE)
                .compose(RxUtil.<ResultEntity<Announce>>io2main())
                .subscribe(new Consumer<ResultEntity<Announce>>() {
                               @Override
                               public void accept(@NonNull ResultEntity<Announce> result) throws Exception {
                                   if (result.isSuccess()) {
                                       if (result.getData() != null && result.getData().getTotal() > 0) {
                                           if (announceList == null) {
                                               announceList = new ArrayList<>();
                                           }
                                           announceList.clear();
                                           announceList.addAll(result.getData().getRows());
                                           mView.showAnnounce(Collections.unmodifiableList(announceList));
                                           //返回的数据少于请求的个数,则视为无更多数据
                                           if (result.getData().getTotal() < PAGE_SIZE) {
                                               mView.showNoMoreAnnounce();
                                           }
                                       } else {
                                           mView.showNoAnnounce();
                                       }
                                   } else {
                                       mView.showErrorMsg(result.getMsg());
                                   }
                               }
                           },
                        new Consumer<Throwable>() {
                            @Override
                            public void accept(@NonNull Throwable throwable) throws Exception {
                                logError(AnnouncePresenter.this, throwable);
                                mView.showErrorMsg(generateErrorMsg(throwable));
                            }
                        });
        addDisposable(dis);
    }

    @Override
    public void loadMore() {
        curPage++;
        Disposable dis = mApi.queryAnnounceList(curPage, PAGE_SIZE)
                .compose(RxUtil.<ResultEntity<Announce>>io2main())
                .subscribe(new Consumer<ResultEntity<Announce>>() {
                               @Override
                               public void accept(@NonNull ResultEntity<Announce> result) throws Exception {
                                   if (result.isSuccess()) {
                                       if (result.getData() != null && result.getData().getTotal() > 0) {
                                           announceList.addAll(result.getData().getRows());
                                           mView.showAnnounce(Collections.unmodifiableList(announceList));
                                           //返回的数据少于请求的个数，则同样视为无更多数据
                                           if (result.getData().getTotal() < PAGE_SIZE) {
                                               mView.showNoMoreAnnounce();
                                           }
                                       } else {
                                           mView.showNoMoreAnnounce();
                                       }
                                   } else {
                                       mView.showErrorMsg(result.getMsg());
                                   }
                               }
                           },
                        new Consumer<Throwable>() {
                            @Override
                            public void accept(@NonNull Throwable throwable) throws Exception {
                                logError(AnnouncePresenter.this, throwable);
                                mView.showErrorMsg(generateErrorMsg(throwable));
                            }
                        });
        addDisposable(dis);
    }
}
