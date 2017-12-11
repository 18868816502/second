package com.beihui.market.ui.activity;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.beihui.market.R;
import com.beihui.market.base.BaseComponentActivity;
import com.beihui.market.entity.LoanProduct;
import com.beihui.market.injection.component.AppComponent;
import com.beihui.market.injection.component.DaggerRecommendComponent;
import com.beihui.market.injection.module.RecommendProductModule;
import com.beihui.market.ui.adapter.HotChoiceRVAdapter;
import com.beihui.market.ui.contract.RecommendProductContract;
import com.beihui.market.ui.presenter.RecommendProductPresenter;
import com.beihui.market.umeng.Events;
import com.beihui.market.umeng.Statistic;
import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

public class RecommendProductActivity extends BaseComponentActivity implements RecommendProductContract.View {
    @BindView(R.id.tool_bar)
    Toolbar toolbar;
    @BindView(R.id.title)
    TextView titleView;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    @Inject
    RecommendProductPresenter presenter;

    private HotChoiceRVAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //umeng统计
        Statistic.onEvent(Events.RESUME_RECOMMEND_PRODUCT);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_recommend_product;
    }

    @Override
    public void configViews() {
        setupToolbar(toolbar);

        adapter = new HotChoiceRVAdapter(R.layout.list_item_hot_product);
        final View view = LayoutInflater.from(this)
                .inflate(R.layout.layout_recommend_product_head, null);
        adapter.setHeaderView(view);

        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                //umeng统计
                Statistic.onEvent(Events.CLICK_RELEVANT_PRODUCT_ITEM);

                Intent intent = new Intent(RecommendProductActivity.this, LoanDetailActivity.class);
                intent.putExtra("loan", (LoanProduct.Row) adapter.getItem(position));
                startActivity(intent);
            }
        });
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerView.setAdapter(adapter);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            int scrollY;

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                scrollY += dy;
                titleView.setVisibility(scrollY >= view.getMeasuredHeight() - 10 ? View.VISIBLE : View.GONE);
            }
        });
    }

    @Override
    public void initDatas() {
        presenter.loadRecommendProduct(getIntent().getIntExtra("amount", 0));
    }

    @Override
    protected void configureComponent(AppComponent appComponent) {
        DaggerRecommendComponent.builder()
                .appComponent(appComponent)
                .recommendProductModule(new RecommendProductModule(this))
                .build()
                .inject(this);
    }

    @Override
    public void setPresenter(RecommendProductContract.Presenter presenter) {
        //
    }

    @Override
    public void showRecommendProduct(List<LoanProduct.Row> list) {
        adapter.notifyHotProductChanged(list);
    }
}
