package com.beihui.market.tang.activity;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.beihui.market.R;
import com.beihui.market.api.Api;
import com.beihui.market.base.BaseComponentActivity;
import com.beihui.market.entity.LoanAccountIconBean;
import com.beihui.market.helper.SlidePanelHelper;
import com.beihui.market.helper.UserHelper;
import com.beihui.market.injection.component.AppComponent;
import com.beihui.market.tang.adapter.LoanBillAdapter;
import com.beihui.market.tang.rx.RxResponse;
import com.beihui.market.tang.rx.observer.ApiObserver;
import com.beihui.market.util.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;
import com.gyf.barlibrary.ImmersionBar;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.annotations.NonNull;

/**
 * https://gitee.com/tangbuzhi
 *
 * @author: Tangbuzhi
 * @version:
 * @package:
 * @description:
 * @modify:
 * @date: 2018/7/18
 */

public class LoanBillActivity extends BaseComponentActivity {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.rv_fg_account_flow_loan)
    RecyclerView recyclerView;

    private LoanBillAdapter loanBillAdapter;

    @Override
    public int getLayoutId() {
        return R.layout.f_activity_loan_bill;
    }

    @Override
    public void configViews() {
        setupToolbar(mToolbar);
        ImmersionBar.with(this).statusBarDarkFont(true).init();
        SlidePanelHelper.attach(this);
    }

    @Override
    public void initDatas() {
        initRecyclerView();
        Api.getInstance().queryLoanAccountIcon(UserHelper.getInstance(this).getProfile().getId())
                .compose(RxResponse.<List<LoanAccountIconBean>>compatT())
                .subscribe(new ApiObserver<List<LoanAccountIconBean>>() {
                    @Override
                    public void onNext(@NonNull List<LoanAccountIconBean> data) {
                        loanBillAdapter.setNewData(data);
                    }
                });
    }

    private void initRecyclerView() {
        loanBillAdapter = new LoanBillAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(loanBillAdapter);

        recyclerView.addOnItemTouchListener(new OnItemChildClickListener() {
            @Override
            public void onSimpleItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                LoanAccountIconBean iconBean = loanBillAdapter.getData().get(position);
                Intent intent = new Intent(getApplicationContext(), MakeBillActivity.class);
                intent.putExtra("type", MakeBillActivity.TYPE_NET_LOAN);
                intent.putExtra("title", iconBean.iconName);
                intent.putExtra("iconId", iconBean.iconId);
                intent.putExtra("tallyId", iconBean.tallyId);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void configureComponent(AppComponent appComponent) {
    }

    @OnClick({R.id.tv_custom_loan, R.id.ll_house_loan, R.id.ll_car_loan, R.id.tv_search_others})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_custom_loan:
                Intent customIntent = new Intent(getApplicationContext(), MakeBillActivity.class);
                customIntent.putExtra("type", MakeBillActivity.TYPE_USER_DIFINE);
                /*intent.putExtra("title", iconBean.iconName);
                intent.putExtra("iconId", iconBean.iconId);
                intent.putExtra("tallyId", iconBean.tallyId);*/
                startActivity(customIntent);
                break;
            case R.id.ll_house_loan:
                Intent houseIntent = new Intent(getApplicationContext(), MakeBillActivity.class);
                houseIntent.putExtra("type", MakeBillActivity.TYPE_HOUSE_LOAN);
                /*intent.putExtra("title", iconBean.iconName);
                intent.putExtra("iconId", iconBean.iconId);
                intent.putExtra("tallyId", iconBean.tallyId);*/
                startActivity(houseIntent);
                break;
            case R.id.ll_car_loan:
                Intent carIntent = new Intent(getApplicationContext(), MakeBillActivity.class);
                carIntent.putExtra("type", MakeBillActivity.TYPE_CAR_LOAN);
                /*intent.putExtra("title", iconBean.iconName);
                intent.putExtra("iconId", iconBean.iconId);
                intent.putExtra("tallyId", iconBean.tallyId);*/
                startActivity(carIntent);
                break;
            case R.id.tv_search_others:
                startActivity(new Intent(this, SearchActivity.class));
                break;
            default:
                break;
        }
    }
}
