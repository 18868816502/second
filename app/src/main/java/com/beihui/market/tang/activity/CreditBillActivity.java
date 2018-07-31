package com.beihui.market.tang.activity;

import android.app.Activity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.beihui.market.R;
import com.beihui.market.api.Api;
import com.beihui.market.base.BaseComponentActivity;
import com.beihui.market.entity.CreditCardBean;
import com.beihui.market.helper.DataStatisticsHelper;
import com.beihui.market.helper.SlidePanelHelper;
import com.beihui.market.injection.component.AppComponent;
import com.beihui.market.tang.rx.RxResponse;
import com.beihui.market.tang.rx.observer.ApiObserver;
import com.beihui.market.ui.adapter.AccountFlowCreditCardAdapter;
import com.beihui.market.umeng.NewVersionEvents;
import com.gyf.barlibrary.ImmersionBar;

import java.util.List;

import butterknife.BindView;
import io.reactivex.annotations.NonNull;

/**
 * https://gitee.com/tangbuzhi
 *
 * @author: Tangbuzhi
 * @version:
 * @package:
 * @description: 信用卡账单列表
 * @modify:
 * @date: 2018/7/20
 */

public class CreditBillActivity extends BaseComponentActivity {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.rv_fg_account_flow_credit_card)
    RecyclerView recyclerView;
    @BindView(R.id.line)
    View line;

    public AccountFlowCreditCardAdapter mAdapter;
    private Activity activity;

    @Override
    public int getLayoutId() {
        return R.layout.x_fragment_account_flow_credit_card;
    }

    @Override
    public void configViews() {
        setupToolbar(mToolbar);
        ImmersionBar.with(this).statusBarDarkFont(true).init();
        //SlidePanelHelper.attach(this);
        activity = this;
        mAdapter = new AccountFlowCreditCardAdapter(activity);
        LinearLayoutManager manager = new LinearLayoutManager(activity);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(mAdapter);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    line.setVisibility(View.GONE);
                } else {
                    line.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });

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
