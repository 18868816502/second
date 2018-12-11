package com.beiwo.klyjaz.goods.activity;

import android.support.annotation.NonNull;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.beiwo.klyjaz.R;
import com.beiwo.klyjaz.api.Api;
import com.beiwo.klyjaz.api.NetConstants;
import com.beiwo.klyjaz.base.BaseComponentActivity;
import com.beiwo.klyjaz.entity.Goods;
import com.beiwo.klyjaz.entity.Product;
import com.beiwo.klyjaz.goods.adapter.LoanGoodsAdapter;
import com.beiwo.klyjaz.helper.SlidePanelHelper;
import com.beiwo.klyjaz.tang.rx.RxResponse;
import com.beiwo.klyjaz.tang.rx.observer.ApiObserver;
import com.gyf.barlibrary.ImmersionBar;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * https://gitee.com/tangbuzhi
 *
 * @author: Tangbuzhi
 * @version:
 * @package:
 * @description:下款推荐
 * @modify:
 * @date: 2018/12/11
 */
public class LoanGoodsActivity extends BaseComponentActivity {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.refresh_layout)
    SmartRefreshLayout refresh_layout;
    @BindView(R.id.recycler)
    RecyclerView recycler;

    private int pageNo = 1;
    private int pageSize = 20;

    private LoanGoodsAdapter loanGoodsAdapter = new LoanGoodsAdapter();

    @Override
    public int getLayoutId() {
        return R.layout.activity_loan_goods;
    }

    @Override
    public void configViews() {
        setupToolbar(toolbar, true);
        setupToolbarBackNavigation(toolbar, R.drawable.back_white);
        ImmersionBar.with(this).statusBarDarkFont(false).init();
        SlidePanelHelper.attach(this);
    }

    @Override
    public void initDatas() {
        initRecycler();
        request(pageNo);
        refresh_layout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                pageNo = 1;
                request(pageNo);
            }

            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                pageNo++;
                request(pageNo);
            }
        });
    }

    private void initRecycler() {
        recycler.setLayoutManager(new LinearLayoutManager(this));
        recycler.setItemAnimator(new DefaultItemAnimator());
        recycler.setHasFixedSize(true);
        recycler.setAdapter(loanGoodsAdapter);
    }

    private void request(int pageNo) {
        //今日推荐
        Api.getInstance().groupProducts(NetConstants.SECOND_PRODUCT_TODAY)
                .compose(RxResponse.<List<Product>>compatT())
                .subscribe(new ApiObserver<List<Product>>() {
                    @Override
                    public void onNext(List<Product> data) {
                        loanGoodsAdapter.setTodayRecom(data);
                    }
                });
        //下款热门
        Api.getInstance().hotLoan(NetConstants.SECOND_PRODUCT_HOT_TOP)
                .compose(RxResponse.<List<Goods>>compatT())
                .subscribe(new ApiObserver<List<Goods>>() {
                    @Override
                    public void onNext(List<Goods> data) {
                        loanGoodsAdapter.setHotTop(data);
                    }
                });
        //好评口子
        goods(pageNo);
    }

    private void goods(final int pageNo) {
        Api.getInstance().hotGoods(pageNo, pageSize)
                .compose(RxResponse.<List<Goods>>compatT())
                .subscribe(new ApiObserver<List<Goods>>() {
                    @Override
                    public void onNext(List<Goods> data) {
                        if (pageNo == 1) {
                            loanGoodsAdapter.setGood(data);
                        } else {
                            loanGoodsAdapter.appendGood(data);
                        }
                    }
                });
    }

    @OnClick({R.id.ll_comment_wrap})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_comment_wrap:

                break;
            default:
                break;
        }
    }
}