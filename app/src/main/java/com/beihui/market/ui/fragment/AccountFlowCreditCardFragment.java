package com.beihui.market.ui.fragment;

import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.Toast;

import com.beihui.market.R;
import com.beihui.market.api.Api;
import com.beihui.market.api.ResultEntity;
import com.beihui.market.base.BaseComponentFragment;
import com.beihui.market.entity.CreditCardBean;
import com.beihui.market.helper.DataStatisticsHelper;
import com.beihui.market.injection.component.AppComponent;
import com.beihui.market.ui.activity.AccountFlowActivity;
import com.beihui.market.ui.activity.CreditCardDebtDetailActivity;
import com.beihui.market.ui.adapter.AccountFlowCreditCardAdapter;
import com.beihui.market.umeng.NewVersionEvents;
import com.beihui.market.util.RxUtil;

import java.util.List;

import butterknife.BindView;
import io.reactivex.functions.Consumer;

/**
 * Created by admin on 2018/6/15.
 */

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
        Api.getInstance().queryBankList().compose(RxUtil.<ResultEntity<List<CreditCardBean>>>io2main())
                .subscribe(new Consumer<ResultEntity<List<CreditCardBean>>>() {
                               @Override
                               public void accept(ResultEntity<List<CreditCardBean>> result) throws Exception {
                                   if (result.isSuccess()) {
                                      if (mAdapter != null) {
                                          mAdapter.addData(result.getData());
                                      }
                                   } else {
                                       Toast.makeText(activity, result.getMsg(), Toast.LENGTH_SHORT).show();
                                   }
                               }
                           },
                        new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                //Log.e("exception_custom", throwable.getMessage());
                            }
                        });
    }

    @Override
    protected void configureComponent(AppComponent appComponent) {

    }
}
