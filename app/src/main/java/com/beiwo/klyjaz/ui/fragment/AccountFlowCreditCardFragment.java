package com.beiwo.klyjaz.ui.fragment;

import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.beiwo.klyjaz.R;
import com.beiwo.klyjaz.api.Api;
import com.beiwo.klyjaz.base.BaseComponentFragment;
import com.beiwo.klyjaz.entity.CreditCardBean;
import com.beiwo.klyjaz.helper.DataStatisticsHelper;
import com.beiwo.klyjaz.injection.component.AppComponent;
import com.beiwo.klyjaz.tang.rx.RxResponse;
import com.beiwo.klyjaz.tang.rx.observer.ApiObserver;
import com.beiwo.klyjaz.ui.adapter.AccountFlowCreditCardAdapter;
import com.beiwo.klyjaz.umeng.NewVersionEvents;

import java.util.List;

import butterknife.BindView;
import io.reactivex.annotations.NonNull;



public class AccountFlowCreditCardFragment extends BaseComponentFragment {

    @BindView(R.id.rv_fg_account_flow_credit_card)
    RecyclerView recyclerView;

    public AccountFlowCreditCardAdapter mAdapter;
    private FragmentActivity activity;

    @Override
    public int getLayoutResId() {
        return R.layout.x_fragment_account_flow_credit_card;
    }

    @Override
    public void configViews() {
        activity = getActivity();
        mAdapter = new AccountFlowCreditCardAdapter(activity);
        LinearLayoutManager manager = new LinearLayoutManager(activity);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(mAdapter);
        //pv，uv统计
        DataStatisticsHelper.getInstance().onCountUv(NewVersionEvents.TALLYCREDITCARD);
    }

    @Override
    public void initDatas() {
        Api.getInstance().queryBankList()
                .compose(RxResponse.<List<CreditCardBean>>compatT())
                .subscribe(new ApiObserver<List<CreditCardBean>>() {
                    @Override
                    public void onNext(@NonNull List<CreditCardBean> data) {
                        if (mAdapter != null) {
                            mAdapter.addData(data);
                        }
                    }
                });
    }

    @Override
    protected void configureComponent(AppComponent appComponent) {
    }
}