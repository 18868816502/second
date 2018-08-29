package com.beihui.market.tang.activity;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.beihui.market.R;
import com.beihui.market.api.Api;
import com.beihui.market.base.BaseComponentActivity;
import com.beihui.market.entity.WithdrawRecord;
import com.beihui.market.helper.SlidePanelHelper;
import com.beihui.market.helper.UserHelper;
import com.beihui.market.injection.component.AppComponent;
import com.beihui.market.tang.adapter.WithdrawRecordAdapter;
import com.beihui.market.tang.rx.RxResponse;
import com.beihui.market.tang.rx.observer.ApiObserver;
import com.gyf.barlibrary.ImmersionBar;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import io.reactivex.annotations.NonNull;

/**
 * https://gitee.com/tangbuzhi
 *
 * @author: Tangbuzhi
 * @version:
 * @package:
 * @description:
 * @modify:
 * @date: 2018/8/16
 */

public class WithdrawRecordActivity extends BaseComponentActivity {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.recycler)
    RecyclerView recycler;
    @BindView(R.id.fl_item_wrap)
    View fl_item_wrap;
    @BindView(R.id.refresh_layout)
    SmartRefreshLayout refresh_layout;

    private int pageNo = 1;
    private int pageSize = 500;
    private Map<String, Object> map = new HashMap<>();
    private WithdrawRecordAdapter adapter = new WithdrawRecordAdapter();

    @Override
    public int getLayoutId() {
        return R.layout.f_activity_withdraw_record;
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
        request();
        refresh_layout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@android.support.annotation.NonNull RefreshLayout refreshLayout) {
                request();
            }
        });
    }

    private void request() {
        map.put("userId", UserHelper.getInstance(this).id());
        map.put("pageNo", pageNo);
        map.put("pageSize", pageSize);
        Api.getInstance().withdrawRecord(map)
                .compose(RxResponse.<WithdrawRecord>compatT())
                .subscribe(new ApiObserver<WithdrawRecord>() {
                    @Override
                    public void onNext(@NonNull WithdrawRecord data) {
                        refresh_layout.finishRefresh();
                        List<WithdrawRecord.Rows> rows = data.getRows();
                        if (rows != null && rows.size() > 0) {
                            fl_item_wrap.setVisibility(View.VISIBLE);
                            adapter.setNewData(rows);
                        } else {
                            fl_item_wrap.setVisibility(View.GONE);
                            adapter.setNewData(null);
                            adapter.setEmptyView(R.layout.f_layout_withdraw_none, recycler);
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable t) {
                        super.onError(t);
                        fl_item_wrap.setVisibility(View.GONE);
                        adapter.setNewData(null);
                        adapter.setEmptyView(R.layout.f_layout_withdraw_none, recycler);
                    }
                });
    }

    private void initRecyclerView() {
        recycler.setLayoutManager(new LinearLayoutManager(this));
        recycler.setAdapter(adapter);
    }

    @Override
    protected void configureComponent(AppComponent appComponent) {
    }
}