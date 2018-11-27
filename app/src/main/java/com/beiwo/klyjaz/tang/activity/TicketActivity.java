package com.beiwo.klyjaz.tang.activity;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.beiwo.klyjaz.R;
import com.beiwo.klyjaz.api.Api;
import com.beiwo.klyjaz.base.BaseComponentActivity;
import com.beiwo.klyjaz.entity.Ticket;
import com.beiwo.klyjaz.helper.SlidePanelHelper;
import com.beiwo.klyjaz.helper.UserHelper;
import com.beiwo.klyjaz.tang.adapter.TicketAdapter;
import com.beiwo.klyjaz.tang.rx.RxResponse;
import com.beiwo.klyjaz.tang.rx.observer.ApiObserver;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.gyf.barlibrary.ImmersionBar;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

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
 * @date: 2018/9/4
 */

public class TicketActivity extends BaseComponentActivity {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.recycler)
    RecyclerView recycler;
    @BindView(R.id.refresh_layout)
    SmartRefreshLayout refresh_layout;

    private TicketAdapter adapter = new TicketAdapter();

    @Override
    public int getLayoutId() {
        return R.layout.f_activity_ticket;
    }

    @Override
    public void configViews() {
        setupToolbar(toolbar);
        ImmersionBar.with(this).statusBarDarkFont(true).init();
        SlidePanelHelper.attach(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        request();
    }

    @Override
    public void initDatas() {
        initRecyclerView();
        refresh_layout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@android.support.annotation.NonNull RefreshLayout refreshLayout) {
                request();
            }
        });
    }

    private void request() {
        Api.getInstance().tickets(UserHelper.getInstance(this).id())
                .compose(RxResponse.<List<Ticket>>compatT())
                .subscribe(new ApiObserver<List<Ticket>>() {
                    @Override
                    public void onNext(@NonNull List<Ticket> data) {
                        refresh_layout.finishRefresh();
                        if (data != null && data.size() > 0) {
                            adapter.setNewData(data);
                        } else {
                            adapter.setNewData(null);
                            adapter.setEmptyView(R.layout.f_layout_ticket_none, recycler);
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable t) {
                        super.onError(t);
                        refresh_layout.finishRefresh();
                        adapter.setNewData(null);
                        adapter.setEmptyView(R.layout.f_layout_ticket_none, recycler);
                    }
                });
    }

    private void initRecyclerView() {
        recycler.setLayoutManager(new LinearLayoutManager(this));
        recycler.setAdapter(adapter);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter baseQuickAdapter, View view, int position) {
                Ticket ticket = adapter.getData().get(position);
                Intent intent = new Intent(TicketActivity.this, TicketDetailActivity.class);
                intent.putExtra("ticket", ticket);
                startActivity(intent);
            }
        });
    }

    @OnClick({R.id.add_ticket})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.add_ticket:
                startActivity(new Intent(this, AddTicketActivity.class));
                break;
            default:
                break;
        }
    }
}
