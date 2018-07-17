package com.beihui.market.ui.presenter;


import android.content.Context;

import com.beihui.market.api.Api;
import com.beihui.market.api.ResultEntity;
import com.beihui.market.base.BaseRxPresenter;
import com.beihui.market.entity.DebtAbstract;
import com.beihui.market.entity.TabAccountNewBean;
import com.beihui.market.helper.UserHelper;
import com.beihui.market.ui.contract.TabAccountContract;
import com.beihui.market.util.RxUtil;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * @author xhb
 *         账单模块 首页
 */
public class TabAccountPresenter extends BaseRxPresenter implements TabAccountContract.Presenter {

    private Context context;
    private Api api;
    private TabAccountContract.View view;
    private UserHelper userHelper;

    private List<TabAccountNewBean> debts = new ArrayList<>();

    //账单 头信息
    private DebtAbstract anAbstract;

    @Inject
    TabAccountPresenter(Context context, Api api, TabAccountContract.View view) {
        this.context = context;
        this.api = api;
        this.view = view;
        userHelper = UserHelper.getInstance(context);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (userHelper.getProfile() != null && userHelper.getProfile().getId() != null) {
            view.showUserLoginBlock();

            //获取头信息
            loadDebtAbstract();
            //获取列表信息
            loadInDebtList();
        } else {
            //获取列表信息
            List<TabAccountNewBean> list = new ArrayList<>();
            view.showInDebtList(list);
        }
    }


    public void onRefresh() {
        if (userHelper.getProfile() != null) {
            //获取头信息
            loadDebtAbstract();
            //获取列表信息
            loadInDebtList();
        } else {
            //获取列表信息
            List<TabAccountNewBean> list = new ArrayList<>();
            view.showInDebtList(list);
        }
    }

    /**
     * 获取头信息
     */
    @Override
    public void loadDebtAbstract() {
        Disposable dis = api.queryTabAccountHeaderInfo(userHelper.getProfile().getId(), 6)//获取网贷+信用卡负债摘要+快捷记账
                .compose(RxUtil.<ResultEntity<DebtAbstract>>io2main())
                .subscribe(new Consumer<ResultEntity<DebtAbstract>>() {
                               @Override
                               public void accept(ResultEntity<DebtAbstract> result) throws Exception {
                                   if (result.isSuccess()) {
                                       if (result.getData() != null) {
                                           anAbstract = result.getData();
                                       }
                                       view.showDebtInfo(result.getData());
                                   } else {
                                       view.showErrorMsg(result.getMsg());
                                   }
                               }
                           },
                        new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                logError(TabAccountPresenter.this, throwable);
                                view.showErrorMsg(generateErrorMsg(throwable));
                            }
                        });
        addDisposable(dis);
    }

    /**
     * 获取待还以及已还第一页数据列表信息
     */
    @Override
    public void loadInDebtList() {
        Disposable dis = api.queryTabAccountList(userHelper.getProfile().getId(), 1)
                .compose(RxUtil.<ResultEntity<List<TabAccountNewBean>>>io2main())
                .subscribe(new Consumer<ResultEntity<List<TabAccountNewBean>>>() {
                               @Override
                               public void accept(ResultEntity<List<TabAccountNewBean>> result) throws Exception {
                                   if (result.isSuccess()) {
                                       view.showInDebtList(result.getData());
                                   } else {
                                       view.showErrorMsg(result.getMsg());
                                   }
                               }
                           },
                        new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                logError(TabAccountPresenter.this, throwable);
                                view.showErrorMsg(generateErrorMsg(throwable));
                            }
                        });
        addDisposable(dis);
    }

    /**
     * 获取列表信息
     */
    @Override
    public void loadInDebtList(int collectType, int pageNo) {
        if (userHelper.getProfile() == null) {
            view.showNoUserLoginBlock();
        } else {
            Disposable dis = api.queryTabAccountList(userHelper.getProfile().getId(), 1, pageNo, 10)
                    .compose(RxUtil.<ResultEntity<List<TabAccountNewBean>>>io2main())
                    .subscribe(new Consumer<ResultEntity<List<TabAccountNewBean>>>() {
                                   @Override
                                   public void accept(ResultEntity<List<TabAccountNewBean>> result) throws Exception {
                                       if (result.isSuccess()) {
                                           view.showPayedInDebtList(result.getData());
                                       } else {
                                           view.showErrorMsg(result.getMsg());
                                       }
                                   }
                               },
                            new Consumer<Throwable>() {
                                @Override
                                public void accept(Throwable throwable) throws Exception {
                                    logError(TabAccountPresenter.this, throwable);
                                    view.showErrorMsg(generateErrorMsg(throwable));
                                }
                            });
            addDisposable(dis);
        }
    }


    @Override
    public void clickDebtSetStatus(int index) {
    }

    @Override
    public void clickDebtHide(int index) {
    }

    @Override
    public void clickDebtSync(int index) {
        view.navigateVisaLeadingIn();
    }

    @Override
    public void refresh() {
        if (userHelper.getProfile() != null) {
            loadDebtAbstract();
        }
    }

    @Override
    public void clickAdd() {
        if (userHelper.getProfile() != null) {
            view.navigateAdd();
        } else {
            view.navigateUserLogin();
        }
    }

    @Override
    public void clickAddCreditCardDebt() {
        if (userHelper.getProfile() != null) {
            view.navigateAddCreditCardDebt();
        } else {
            view.navigateUserLogin();
        }
    }

    @Override
    public void clickAddLoanDebt() {
        if (userHelper.getProfile() != null) {
            view.navigateAddLoanDebt();
        } else {
            view.navigateUserLogin();
        }
    }


    @Override
    public void clickCalendar() {
        if (userHelper.getProfile() != null) {
            view.navigateCalendar();
        } else {
            view.navigateUserLogin();
        }
    }

    @Override
    public void clickAnalyze() {
        if (userHelper.getProfile() != null) {
            view.navigateAnalyze();
        } else {
            view.navigateUserLogin();
        }
    }

    @Override
    public void clickDebt(int index) {
    }

    @Override
    public void clickCreditCard() {
        view.navigateCreditCardCenter();
    }

    @Override
    public void clickEye(boolean checked) {
        if (debts.size() > 0) {
            if (checked) {
                view.hideDebtInfo();
            } else {
                view.showDebtInfo();
            }
        }
    }
}
