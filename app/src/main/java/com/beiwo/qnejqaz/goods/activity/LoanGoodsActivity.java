package com.beiwo.qnejqaz.goods.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.beiwo.qnejqaz.R;
import com.beiwo.qnejqaz.api.Api;
import com.beiwo.qnejqaz.api.NetConstants;
import com.beiwo.qnejqaz.base.BaseComponentActivity;
import com.beiwo.qnejqaz.entity.Goods;
import com.beiwo.qnejqaz.entity.HotTop;
import com.beiwo.qnejqaz.entity.Product;
import com.beiwo.qnejqaz.goods.adapter.LoanGoodsAdapter;
import com.beiwo.qnejqaz.helper.DataHelper;
import com.beiwo.qnejqaz.helper.SlidePanelHelper;
import com.beiwo.qnejqaz.helper.UserHelper;
import com.beiwo.qnejqaz.tang.DlgUtil;
import com.beiwo.qnejqaz.tang.rx.RxResponse;
import com.beiwo.qnejqaz.tang.rx.observer.ApiObserver;
import com.beiwo.qnejqaz.util.DensityUtil;
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
    @BindView(R.id.view_bg_toolbar)
    View view_bg_toolbar;

    private int pageNo = 1;
    private int pageSize = 20;
    private int dyTranslate;
    private LoanGoodsAdapter loanGoodsAdapter = new LoanGoodsAdapter();
    private long nao;

    @Override
    public int getLayoutId() {
        return R.layout.activity_loan_goods;
    }

    @Override
    public void configViews() {
        nao = System.currentTimeMillis();
        setupToolbar(toolbar, true);
        setupToolbarBackNavigation(toolbar, R.drawable.back_white);
        ImmersionBar.with(this).statusBarDarkFont(false).init();
        SlidePanelHelper.attach(this);
    }

    @Override
    public void initDatas() {
        initRecycler();
        request(pageNo);
        recycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                dyTranslate += dy;
                float dyRate = dyTranslate * 1.0f / DensityUtil.dp2px(getApplicationContext(), 50);
                if (dyTranslate >= DensityUtil.dp2px(getApplicationContext(), 35f)) {
                    view_bg_toolbar.setAlpha(dyRate < 1f ? dyRate : 1f);
                } else {
                    view_bg_toolbar.setAlpha(dyRate > 0f && dyRate < 1f ? dyRate : 0f);
                }
            }
        });
        refresh_layout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                pageNo = 1;
                request(pageNo);
            }

            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                pageNo++;
                goods(pageNo);
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
                        refresh_layout.finishRefresh();
                        loanGoodsAdapter.setTodayRecom(data);
                    }
                });
        //下款热门
        Api.getInstance().hotLoan(NetConstants.SECOND_PRODUCT_HOT_TOP)
                .compose(RxResponse.<List<HotTop>>compatT())
                .subscribe(new ApiObserver<List<HotTop>>() {
                    @Override
                    public void onNext(List<HotTop> data) {
                        refresh_layout.finishRefresh();
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
                        refresh_layout.finishLoadMore();
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
                if (!UserHelper.getInstance(this).isLogin()) {
                    DlgUtil.loginDlg(this, null);
                    return;
                }
                startActivity(new Intent(this, GoodsListActivity.class));
                break;
            default:
                break;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (nao != 0 && System.currentTimeMillis() - nao > 500 && System.currentTimeMillis() - nao < DataHelper.MAX_SECOND) {
            DataHelper.getInstance(this).event(DataHelper.EVENT_TYPE_STAY, DataHelper.EVENT_VIEWID_LOANRECOMMENDDIVISION, "", System.currentTimeMillis() - nao);
        }
    }
}