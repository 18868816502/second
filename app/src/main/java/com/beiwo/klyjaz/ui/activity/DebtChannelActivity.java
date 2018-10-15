package com.beiwo.klyjaz.ui.activity;


import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import com.beiwo.klyjaz.R;
import com.beiwo.klyjaz.base.BaseComponentActivity;
import com.beiwo.klyjaz.entity.DebtChannel;
import com.beiwo.klyjaz.helper.DataStatisticsHelper;
import com.beiwo.klyjaz.helper.SlidePanelHelper;
import com.beiwo.klyjaz.injection.component.AppComponent;
import com.beiwo.klyjaz.injection.component.DaggerDebtChannelComponent;
import com.beiwo.klyjaz.injection.module.DebtChannelModule;
import com.beiwo.klyjaz.ui.adapter.DebtChannelRVAdapter;
import com.beiwo.klyjaz.ui.contract.DebtChannelContract;
import com.beiwo.klyjaz.ui.fragment.DebtChannelSearchFragment;
import com.beiwo.klyjaz.ui.presenter.DebtChannelPresenter;
import com.beiwo.klyjaz.ui.rvdecoration.DebtChannelItemDeco;
import com.beiwo.klyjaz.ui.rvdecoration.DebtChannelStickyHeaderItemDeco;
import com.beiwo.klyjaz.view.AlphabetIndexBar;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.gyf.barlibrary.ImmersionBar;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author xhb
 * 网贷记账
 */
public class DebtChannelActivity extends BaseComponentActivity implements DebtChannelContract.View {

    @BindView(R.id.tool_bar)
    FrameLayout toolbar;
    @BindView(R.id.search)
    View search;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.alphabet_index_bar)
    AlphabetIndexBar alphabetIndexBar;

    class Header {
        View itemView;
        @BindView(R.id.history_header)
        View historyHeader;
        @BindView(R.id.debt_channel_new)
        View debtChannelNew;
        @BindView(R.id.recycler_view)
        RecyclerView recyclerView;

        Header(View itemView) {
            this.itemView = itemView;
            ButterKnife.bind(this, itemView);
        }
    }

    @Inject
    DebtChannelPresenter presenter;

    private DebtChannelRVAdapter adapter;
    private DebtChannelRVAdapter historyAdapter;
    private Header header;

    private DebtChannelSearchFragment fragment;

    @Override
    public int getLayoutId() {
        return R.layout.activity_debt_channel;
    }

    @Override
    public void configViews() {
        ImmersionBar.with(this).titleBar(toolbar).statusBarDarkFont(true).init();

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        View view = LayoutInflater.from(this)
                .inflate(R.layout.layout_debt_channel_header, recyclerView, false);
        header = new Header(view);

        adapter = new DebtChannelRVAdapter(R.layout.list_item_debt_channel);

        /**
         * 点击显示的列表平台
         */
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                presenter.selectDebtChannel(position, false);
            }
        });
        adapter.setHeaderView(view);
        recyclerView.addItemDecoration(new DebtChannelItemDeco());
        recyclerView.addItemDecoration(new DebtChannelStickyHeaderItemDeco(this) {
            @Override
            public String getHeaderName(int pos) {
                return adapter.getItem(pos).getChannelInitials();
            }
        });
        recyclerView.setAdapter(adapter);

        historyAdapter = new DebtChannelRVAdapter(R.layout.list_item_debt_channel_history);
        /**
         * 点击历史Item
         */
        historyAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                presenter.selectDebtChannel(position, true);
            }
        });
        header.recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        header.recyclerView.setAdapter(historyAdapter);
        /**
         * 点击添加新的借款平台
         */
        header.debtChannelNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /**
                 * 埋点 	网贷记账自定义
                 */
                //pv，uv统计
                DataStatisticsHelper.getInstance().onCountUv(DataStatisticsHelper.ID_BILL_NET_LOAN_CUSTOM_ACCOUNT);

                presenter.addDebtChannel();
            }
        });

        alphabetIndexBar.setAlphabetSelectedListener(new AlphabetIndexBar.AlphabetSelectedListener() {
            @Override
            public void onAlphabetSelected(int index, String alphabet) {
                presenter.selectedAlphabet(index, alphabet);
            }
        });

        /**
         * 搜索页面
         */
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fragment == null) {
                    fragment = new DebtChannelSearchFragment();
                    fragment.setPresenter(presenter);
                    getSupportFragmentManager().beginTransaction()
                            .add(R.id.debt_channel_search_container, fragment, fragment.toString())
                            .commitAllowingStateLoss();
                } else {
                    getSupportFragmentManager().beginTransaction()
                            .attach(fragment)
                            .commitAllowingStateLoss();
                }
            }
        });

        SlidePanelHelper.attach(this);
    }

    @Override
    public void initDatas() {
        presenter.onStart();
    }

    @Override
    protected void configureComponent(AppComponent appComponent) {
        DaggerDebtChannelComponent.builder()
                .appComponent(appComponent)
                .debtChannelModule(new DebtChannelModule(this))
                .build()
                .inject(this);
    }

    @Override
    public void setPresenter(DebtChannelContract.Presenter presenter) {
        //
    }

    @Override
    public void showDebtChannel(List<DebtChannel> list) {
        adapter.notifyDebtChannelChanged(list);
    }

    @Override
    public void showDebtChannelHistory(List<DebtChannel> list) {
        if (list.size() > 0) {
            header.historyHeader.setVisibility(View.VISIBLE);
            historyAdapter.notifyDebtChannelChanged(list);
        } else {
            header.historyHeader.setVisibility(View.GONE);
        }
    }

    @Override
    public void showSearchResult(List<DebtChannel> list) {
        if (fragment != null) {
            fragment.showSearchResult(list);
        }
    }

    @Override
    public void showNoSearchResult() {
        if (fragment != null) {
            fragment.showNoSearchResult();
        }
    }

    /**
     * 网贷账单编辑详情 进入此页面
     */
    @Override
    public void showSearchChannelSelected(DebtChannel channel) {
        //pv，uv统计
        DataStatisticsHelper.getInstance().onCountUv(DataStatisticsHelper.ID_BILL_CLICK_LOAN_CHANNEL);

        Intent intent = new Intent(this, DebtNewActivity.class);
        intent.putExtra("debt_channel", channel);
        startActivity(intent);
    }

    @Override
    public void scrollToPosition(int position) {
        //平滑滚动top，简单粗暴处理
//        final int oldTop = recyclerView.computeVerticalScrollOffset();
//        ((LinearLayoutManager) recyclerView.getLayoutManager()).scrollToPositionWithOffset(position + 1, 0);
//        recyclerView.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                final int newTop = recyclerView.computeVerticalScrollOffset();
//                recyclerView.scrollBy(0, oldTop - newTop);
//                recyclerView.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        recyclerView.smoothScrollBy(0, newTop - oldTop, new DecelerateInterpolator(2));
//                    }
//                }, 10);
//            }
//        }, 10);

        ((LinearLayoutManager) recyclerView.getLayoutManager()).scrollToPositionWithOffset(position + 1, 0);
    }

    public void dismissSearchFragment() {
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .detach(fragment)
                    .commitAllowingStateLoss();
        }
    }
}