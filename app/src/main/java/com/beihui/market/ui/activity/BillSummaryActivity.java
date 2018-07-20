package com.beihui.market.ui.activity;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.beihui.market.R;
import com.beihui.market.api.Api;
import com.beihui.market.api.ResultEntity;
import com.beihui.market.base.BaseComponentActivity;
import com.beihui.market.entity.LoanAccountIconBean;
import com.beihui.market.helper.SlidePanelHelper;
import com.beihui.market.helper.UserHelper;
import com.beihui.market.injection.component.AppComponent;
import com.beihui.market.tang.rx.RxResponse;
import com.beihui.market.tang.rx.observer.ApiObserver;
import com.beihui.market.ui.adapter.BillSummaryAdapter;
import com.beihui.market.util.RxUtil;
import com.gyf.barlibrary.ImmersionBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import io.reactivex.Observer;
import io.reactivex.functions.Consumer;

public class BillSummaryActivity extends BaseComponentActivity {
    @BindView(R.id.tool_bar_sunmary)
    Toolbar toolbar;
    private BillSummaryAdapter adapter;
    @BindView(R.id.bill_summary_recycler)
    RecyclerView recyclerView;

    @Override
    public int getLayoutId() {
        return R.layout.activity_bill_summary;
    }

    @Override
    public void configViews() {
        ImmersionBar.with(this).statusBarDarkFont(true).init();
        setupToolbar(toolbar);
        SlidePanelHelper.attach(this);

    }

    @Override
    public void initDatas() {
        List<String> s = new ArrayList<>();
        s.add("123");
        s.add("123");
        s.add("123");
        s.add("123");
        s.add("123");
        s.add("123");
        getBillSummaryData();
        adapter = new BillSummaryAdapter(R.layout.item_bill_summary_layout, s);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void configureComponent(AppComponent appComponent) {

    }

    private void getBillSummaryData() {
        String userId = UserHelper.getInstance(this).getProfile().getId();
        Api.getInstance().onBillSummary(userId).compose(RxResponse.<List<LoanAccountIconBean>>compatT()).subscribe(new ApiObserver<List<LoanAccountIconBean>>() {
            @Override
            public void onNext(List<LoanAccountIconBean> data) {
                System.out.println("12313");

            }
        });
    }

}
