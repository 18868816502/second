package com.beihui.market.ui.presenter;


import android.content.Context;
import android.text.TextUtils;

import com.beihui.market.api.Api;
import com.beihui.market.api.ResultEntity;
import com.beihui.market.base.BaseRxPresenter;
import com.beihui.market.db.DatabaseHelper;
import com.beihui.market.entity.DebtChannel;
import com.beihui.market.entity.DebtChannelDao;
import com.beihui.market.ui.contract.DebtChannelContract;
import com.beihui.market.util.RxUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class DebtChannelPresenter extends BaseRxPresenter implements DebtChannelContract.Presenter {

    private static final int HISTORY_MAX_SIZE = 6;

    private Api api;
    private DebtChannelContract.View view;
    private Context context;

    private DebtChannelDao dao;

    private DebtChannel customChannel;
    private LinkedHashMap<String, List<DebtChannel>> debtChannelMap;
    private int[] alphabetCountList = new int[27];
    private List<DebtChannel> debtChannels = new ArrayList<>();
    private List<DebtChannel> debtChannelHistory = new ArrayList<>();

    private List<DebtChannel> searchResult = new ArrayList<>();

    @Inject
    DebtChannelPresenter(Context context, Api api, DebtChannelContract.View view) {
        this.context = context;
        this.api = api;
        this.view = view;
        dao = DatabaseHelper.getInstance(context).getDaoSession().getDebtChannelDao();
    }

    @Override
    public void onStart() {
        super.onStart();
        loadDebtChannelHistory();
        loadDebtChannel();
    }

    @Override
    public void loadDebtChannel() {
        Disposable dis = api.queryLoanChannel()
                .compose(RxUtil.<ResultEntity<LinkedHashMap<String, List<DebtChannel>>>>io2main())
                .subscribe(new Consumer<ResultEntity<LinkedHashMap<String, List<DebtChannel>>>>() {
                               @Override
                               public void accept(ResultEntity<LinkedHashMap<String, List<DebtChannel>>> result) throws Exception {
                                   if (result.isSuccess()) {
                                       debtChannelMap = result.getData();
                                       debtChannels.clear();
                                       if (debtChannelMap != null) {
                                           //获取自定义渠道相关信息
                                           if (debtChannelMap.containsKey("custom") && debtChannelMap.get("custom").size() > 0) {
                                               //不显示custom
                                               customChannel = debtChannelMap.remove("custom").get(0);
                                           }
                                           //可能需要更新自定义渠道的logo
                                           compareAndUpdateHistoryChannel();
                                           //重置字母对应得数据个数
                                           resetAlphabetCountList();
                                           //遍历转化channel
                                           for (Map.Entry<String, List<DebtChannel>> entry : debtChannelMap.entrySet()) {
                                               char ch = entry.getKey().toLowerCase().charAt(0);
                                               //记录每个字母下对用数据的个数
                                               if (ch >= 'a' && ch <= 'z') {
                                                   alphabetCountList[ch - 'a' + 1] = entry.getValue().size();
                                               } else {
                                                   //非字母统一归为一类
                                                   alphabetCountList[0] = entry.getValue().size();
                                               }
                                               for (DebtChannel channel : entry.getValue()) {
                                                   channel.setChannelInitials(entry.getKey());
                                               }
                                               debtChannels.addAll(entry.getValue());
                                           }
                                       }
                                       view.showDebtChannel(Collections.unmodifiableList(debtChannels));
                                   } else {
                                       view.showErrorMsg(result.getMsg());
                                   }
                               }
                           },
                        new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                logError(DebtChannelPresenter.this, throwable);
                                view.showErrorMsg(generateErrorMsg(throwable));
                            }
                        });
        addDisposable(dis);
    }

    @Override
    public void loadDebtChannelHistory() {
        Disposable dis = Observable.just(1)
                .observeOn(Schedulers.io())
                .flatMap(new Function<Integer, ObservableSource<List<DebtChannel>>>() {
                    @Override
                    public ObservableSource<List<DebtChannel>> apply(Integer integer) throws Exception {
                        DebtChannelDao dao = DatabaseHelper.getInstance(context).getDaoSession().getDebtChannelDao();
                        return Observable.just(dao.loadAll());
                    }
                })
                .compose(RxUtil.<List<DebtChannel>>io2main())
                .subscribe(new Consumer<List<DebtChannel>>() {
                               @Override
                               public void accept(List<DebtChannel> list) throws Exception {
                                   debtChannelHistory.clear();
                                   if (list != null && list.size() > 0) {
                                       Collections.reverse(list);
                                       debtChannelHistory.addAll(list);
                                   }
                                   view.showDebtChannelHistory(Collections.unmodifiableList(debtChannelHistory));
                               }
                           },
                        new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                logError(DebtChannelPresenter.this, throwable);
                            }
                        });
        addDisposable(dis);
    }

    @Override
    public void search(String key) {
        searchResult.clear();
        if (!TextUtils.isEmpty(key)) {
            //记录已经添加的id，避免重复添加
            List<String> addedIds = new ArrayList<>();
            for (DebtChannel channel : debtChannels) {
                if (channel.getChannelName().contains(key)) {
                    searchResult.add(channel);
                    addedIds.add(channel.getId());
                }
            }
            for (DebtChannel channel : debtChannelHistory) {
                if (channel.getChannelName().contains(key)) {
                    //保证唯一
                    if (!addedIds.contains(channel.getId())) {
                        searchResult.add(channel);
                    }
                }
            }
        }
        if (searchResult.size() > 0) {
            view.showSearchResult(Collections.unmodifiableList(searchResult));
        } else {
            view.showNoSearchResult();
        }
    }

    @Override
    public void addDebtChannel(String channelName) {
        if (customChannel != null) {
            final DebtChannel newChannel = new DebtChannel();
            newChannel.setChannelName(channelName);
            newChannel.setId(java.util.UUID.randomUUID().toString());
            newChannel.setType("custom");
            newChannel.setLogo(customChannel.getLogo());
            newChannel.setCustomId(customChannel.getId());

            Disposable dis = Observable.just(newChannel)
                    .observeOn(Schedulers.io())
                    .map(new Function<DebtChannel, DebtChannel>() {
                        @Override
                        public DebtChannel apply(DebtChannel channel) throws Exception {
                            dao.insert(channel);
                            //历史记录达到最大值
                            if (debtChannelHistory.size() >= HISTORY_MAX_SIZE) {
                                dao.delete(debtChannelHistory.get(debtChannelHistory.size() - 1));
                            }
                            return channel;
                        }
                    })
                    .compose(RxUtil.<DebtChannel>io2main())
                    .subscribe(new Consumer<DebtChannel>() {
                                   @Override
                                   public void accept(DebtChannel channel) throws Exception {
                                       view.showSearchChannelSelected(channel);
                                   }
                               },
                            new Consumer<Throwable>() {
                                @Override
                                public void accept(Throwable throwable) throws Exception {
                                    logError(DebtChannelPresenter.this, throwable);
                                }
                            });
            addDisposable(dis);
        }
    }

    @Override
    public void selectDebtChannel(int index, final boolean history) {
        final DebtChannel selected = history ? debtChannelHistory.get(index) : debtChannels.get(index);
        Disposable dis = Observable.just(selected)
                .observeOn(Schedulers.io())
                .map(new Function<DebtChannel, DebtChannel>() {
                    @Override
                    public DebtChannel apply(DebtChannel channel) throws Exception {
                        if (history) {
                            //点击历史记录，只需要换位置
                            dao.delete(channel);
                            dao.insert(channel);
                        } else {
                            boolean inHistory = false;
                            //点击服务器获取的渠道，
                            // 1.判断历史记录中是否已存在，存在则直接返回
                            // 2.不存在，添加记录，并判断历史记录是否达到最大值
                            for (DebtChannel history : debtChannelHistory) {
                                //已经存在
                                if (history.getId().equals(channel.getId())) {
                                    inHistory = true;
                                }
                            }
                            if (!inHistory) {
                                //历史记录中不存在
                                dao.insert(channel);
                                //历史记录达到最大值，需要删除
                                if (debtChannelHistory.size() >= HISTORY_MAX_SIZE) {
                                    dao.delete(debtChannelHistory.get(debtChannelHistory.size() - 1));
                                }
                            } else {
                                //历史记录中存在，更新位置
                                dao.delete(channel);
                                dao.insert(channel);
                            }
                        }
                        return selected;
                    }
                })
                .compose(RxUtil.<DebtChannel>io2main())
                .subscribe(new Consumer<DebtChannel>() {
                               @Override
                               public void accept(DebtChannel channel) throws Exception {
                                   view.showSearchChannelSelected(channel);
                               }
                           },
                        new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                logError(DebtChannelPresenter.this, throwable);
                            }
                        });
        addDisposable(dis);
    }

    @Override
    public void selectSearchDebtChannel(int index) {
        DebtChannel selected = searchResult.get(index);
        if (debtChannels.size() > 0 && debtChannels.contains(selected)) {
            selectDebtChannel(debtChannels.indexOf(selected), false);
        } else if (debtChannelHistory.size() > 0 && debtChannelHistory.contains(selected)) {
            selectDebtChannel(debtChannelHistory.indexOf(selected), true);
        }
    }

    @Override
    public void selectedAlphabet(int index, String alphabet) {
        //如果选中的位置下没有数据，则不响应
        if (alphabetCountList[index] != 0) {
            int size = 0;
            for (int i = 0; i < index; ++i) {
                size += alphabetCountList[i];
            }
            view.scrollToPosition(size);
        }
    }

    private void compareAndUpdateHistoryChannel() {
        if (customChannel != null && debtChannelHistory.size() > 0) {
            for (DebtChannel channel : debtChannelHistory) {
                if (TextUtils.isEmpty(channel.getType()) || channel.isCustom()) {
                    channel.setLogo(customChannel.getLogo());
                    channel.setCustomId(customChannel.getId());
                }
            }
            view.showDebtChannelHistory(Collections.unmodifiableList(debtChannelHistory));
        }
    }

    private void resetAlphabetCountList() {
        for (int i = 0; i < alphabetCountList.length; ++i) {
            alphabetCountList[i] = 0;
        }
    }
}
