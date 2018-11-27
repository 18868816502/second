package com.beiwo.klyjaz.ui.activity;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.beiwo.klyjaz.R;
import com.beiwo.klyjaz.base.BaseComponentActivity;
import com.beiwo.klyjaz.entity.LoanProduct;
import com.beiwo.klyjaz.helper.SlidePanelHelper;
import com.beiwo.klyjaz.ui.adapter.HotChoiceRVAdapter;
import com.beiwo.klyjaz.ui.contract.RecommendProductContract;
import com.beiwo.klyjaz.ui.presenter.RecommendProductPresenter;
import com.beiwo.klyjaz.umeng.Events;
import com.beiwo.klyjaz.umeng.Statistic;
import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.List;

import butterknife.BindView;

public class RecommendProductActivity extends BaseComponentActivity implements RecommendProductContract.View {
    @BindView(R.id.tool_bar)
    Toolbar toolbar;
    @BindView(R.id.title)
    TextView titleView;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

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
        presenter = new RecommendProductPresenter(this);
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

        SlidePanelHelper.attach(this);
    }

    @Override
    public void initDatas() {
        presenter.loadRecommendProduct(getIntent().getIntExtra("amount", 0));
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
