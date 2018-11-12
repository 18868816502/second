package com.beiwo.klyjaz.jjd.activity;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.beiwo.klyjaz.R;
import com.beiwo.klyjaz.api.Api;
import com.beiwo.klyjaz.base.BaseComponentActivity;
import com.beiwo.klyjaz.helper.SlidePanelHelper;
import com.beiwo.klyjaz.helper.UserHelper;
import com.beiwo.klyjaz.injection.component.AppComponent;
import com.beiwo.klyjaz.jjd.CardAdapter;
import com.beiwo.klyjaz.jjd.bean.BankCard;
import com.beiwo.klyjaz.tang.rx.RxResponse;
import com.beiwo.klyjaz.tang.rx.observer.ApiObserver;
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
 * @date: 2018/9/14
 */

public class MyBankCardActivity extends BaseComponentActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.refresh_layout)
    SmartRefreshLayout refresh_layout;
    @BindView(R.id.recycler)
    RecyclerView recycler;

    private CardAdapter adapter = new CardAdapter();

    @Override
    public int getLayoutId() {
        return R.layout.vest_activity_mycard;
    }

    @Override
    public void configViews() {
        setupToolbar(toolbar);
        ImmersionBar.with(this).statusBarDarkFont(true).init();
        SlidePanelHelper.attach(this);
    }

    @Override
    public void initDatas() {
        initRecycler();
        refresh_layout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@android.support.annotation.NonNull RefreshLayout refreshLayout) {
                request();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        request();
    }

    private void initRecycler() {
        recycler.setLayoutManager(new LinearLayoutManager(this));
        recycler.setAdapter(adapter);
        /*adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter baseQuickAdapter, View view, int position) {
                try {
                    Intent intent = new Intent(getApplicationContext(), AddCardActivity.class);
                    intent.putExtra("cardId", adapter.getData().get(position).getCardId());
                    startActivity(intent);
                } catch (Exception e) {
                }
            }
        });*/
    }

    private void request() {
        Api.getInstance().cardList(UserHelper.getInstance(this).id())
                .compose(RxResponse.<List<BankCard>>compatT())
                .subscribe(new ApiObserver<List<BankCard>>() {
                    @Override
                    public void onNext(@NonNull List<BankCard> data) {
                        refresh_layout.finishRefresh();
                        if (data == null || data.isEmpty()) {
                            empty();
                        } else {
                            adapter.setNewData(data);
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable t) {
                        super.onError(t);
                        refresh_layout.finishRefresh();
                        empty();
                    }
                });
    }

    private void empty() {
        adapter.setEmptyView(R.layout.vest_empty_layout, recycler);
        View emptyView = adapter.getEmptyView();
        TextView tv_error_msg = emptyView.findViewById(R.id.tv_error_msg);
        TextView tv_retry_btn = emptyView.findViewById(R.id.tv_retry_btn);
        ImageView iv_empty_img = emptyView.findViewById(R.id.iv_empty_img);
        iv_empty_img.setImageResource(R.mipmap.no_data_card);
        tv_error_msg.setText("您还没有添加银行卡");
        tv_retry_btn.setText("立即添加");
        tv_retry_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), AddCardActivity.class));
            }
        });
    }

    @Override
    protected void configureComponent(AppComponent appComponent) {
    }

    @OnClick({R.id.iv_add_card})
    public void onClick(View view) {
        startActivity(new Intent(getApplicationContext(), AddCardActivity.class));
    }
}