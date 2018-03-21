package com.beihui.market.ui.activity;


import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import com.beihui.market.R;
import com.beihui.market.base.BaseComponentActivity;
import com.beihui.market.entity.DebtChannel;
import com.beihui.market.helper.SlidePanelHelper;
import com.beihui.market.injection.component.AppComponent;
import com.beihui.market.injection.component.DaggerDebtChannelComponent;
import com.beihui.market.injection.module.DebtChannelModule;
import com.beihui.market.ui.adapter.DebtChannelRVAdapter;
import com.beihui.market.ui.contract.DebtChannelContract;
import com.beihui.market.ui.dialog.DebtChannelNewDialog;
import com.beihui.market.ui.fragment.DebtChannelSearchFragment;
import com.beihui.market.ui.presenter.DebtChannelPresenter;
import com.beihui.market.ui.rvdecoration.DebtChannelItemDeco;
import com.beihui.market.ui.rvdecoration.DebtChannelStickyHeaderItemDeco;
import com.beihui.market.view.AlphabetIndexBar;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.gyf.barlibrary.ImmersionBar;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

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
        historyAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                presenter.selectDebtChannel(position, true);
            }
        });
        header.recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        header.recyclerView.setAdapter(historyAdapter);
        header.debtChannelNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DebtChannelNewDialog().setDebtChannelAddListener(new DebtChannelNewDialog.DebtAddChannelListener() {
                    @Override
                    public void onChannelAdded(String channelName) {
                        presenter.addDebtChannel(channelName);
                    }
                }).show(getSupportFragmentManager(), "NewChannel");
            }
        });

        alphabetIndexBar.setAlphabetSelectedListener(new AlphabetIndexBar.AlphabetSelectedListener() {
            @Override
            public void onAlphabetSelected(int index, String alphabet) {
                presenter.selectedAlphabet(index, alphabet);
            }
        });

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fragment == null) {
                    fragment = new DebtChannelSearchFragment();
                    fragment.setPresenter(presenter);
                    getSupportFragmentManager().beginTransaction()
                            .add(R.id.debt_channel_search_container, fragment, fragment.toString())
                            .commit();
                } else {
                    getSupportFragmentManager().beginTransaction()
                            .attach(fragment)
                            .commit();
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

    @Override
    public void showSearchChannelSelected(DebtChannel channel) {
        Intent intent = new Intent(this, DebtNewActivity.class);
        intent.putExtra("debt_channel", channel);
        startActivity(intent);
        toolbar.postDelayed(new Runnable() {
            @Override
            public void run() {
                finish();
            }
        }, 100);
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
                    .commit();
        }
    }
}
