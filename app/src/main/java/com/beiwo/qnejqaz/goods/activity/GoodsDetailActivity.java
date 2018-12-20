package com.beiwo.qnejqaz.goods.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.beiwo.qnejqaz.App;
import com.beiwo.qnejqaz.BuildConfig;
import com.beiwo.qnejqaz.R;
import com.beiwo.qnejqaz.api.Api;
import com.beiwo.qnejqaz.base.BaseComponentActivity;
import com.beiwo.qnejqaz.entity.Comments;
import com.beiwo.qnejqaz.entity.CommentsTotal;
import com.beiwo.qnejqaz.entity.GoodsInfo;
import com.beiwo.qnejqaz.entity.UserProfileAbstract;
import com.beiwo.qnejqaz.goods.adapter.GoodsDetailAdapter;
import com.beiwo.qnejqaz.helper.DataHelper;
import com.beiwo.qnejqaz.helper.SlidePanelHelper;
import com.beiwo.qnejqaz.helper.UserHelper;
import com.beiwo.qnejqaz.tang.DlgUtil;
import com.beiwo.qnejqaz.tang.rx.RxResponse;
import com.beiwo.qnejqaz.tang.rx.observer.ApiObserver;
import com.beiwo.qnejqaz.ui.activity.WebViewActivity;
import com.beiwo.qnejqaz.util.DensityUtil;
import com.gyf.barlibrary.ImmersionBar;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * https://gitee.com/tangbuzhi
 *
 * @author: Tangbuzhi
 * @version:
 * @package:
 * @description:口子详情
 * @modify:
 * @date: 2018/12/11
 */
public class GoodsDetailActivity extends BaseComponentActivity {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.toolbar_title)
    TextView toolbar_title;
    @BindView(R.id.refresh_layout)
    SmartRefreshLayout refresh_layout;
    @BindView(R.id.recycler)
    RecyclerView recycler;

    private String cutId, manageId, productId;
    private GoodsDetailAdapter detailAdapter = new GoodsDetailAdapter();
    private int dyTranslate;
    private String title;

    @Override
    public int getLayoutId() {
        return R.layout.activity_goods_detail;
    }

    @Override
    public void configViews() {
        nao = System.currentTimeMillis();
        setupToolbar(toolbar);
        ImmersionBar.with(this).statusBarDarkFont(true).init();
        SlidePanelHelper.attach(this);

        Intent intent = getIntent();
        if (intent != null) {
            cutId = intent.getStringExtra("cutId");
            manageId = intent.getStringExtra("manageId");
        }
        toolbar_title.setText("产品详情");
    }

    private long nao;

    @Override
    protected void onStop() {
        super.onStop();
        if (nao != 0 && System.currentTimeMillis() - nao > 500 && System.currentTimeMillis() - nao < DataHelper.MAX_SECOND) {
            DataHelper.getInstance(this).event(DataHelper.EVENT_TYPE_STAY, DataHelper.EVENT_VIEWID_LOANRECOMMENDPRODUCTDETAIL, "", System.currentTimeMillis() - nao);
        }
    }

    @Override
    public void initDatas() {
        initRecycler();
        request();
        refresh_layout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                refresh_layout.finishRefresh();
                request();
            }

            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                refresh_layout.finishLoadMore();
                Intent intent = new Intent(getApplicationContext(), GoodsCommentActivity.class);
                intent.putExtra("type", 0);
                intent.putExtra("tag", "all");
                intent.putExtra("name", title);
                intent.putExtra("cutId", cutId);
                intent.putExtra("manageId", manageId);
                startActivity(intent);
            }
        });
        recycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                dyTranslate += dy;
                if (dyTranslate >= DensityUtil.dp2px(getApplicationContext(), 80f)) {
                    toolbar_title.setText(title);
                } else {
                    toolbar_title.setText("产品详情");
                }
            }
        });
    }

    private void initRecycler() {
        recycler.setLayoutManager(new LinearLayoutManager(this));
        recycler.setItemAnimator(new DefaultItemAnimator());
        recycler.setHasFixedSize(true);
        recycler.setAdapter(detailAdapter);
    }

    private void request() {
        //产品信息
        Api.getInstance().goodsInfo(cutId, manageId)
                .compose(RxResponse.<GoodsInfo>compatT())
                .subscribe(new ApiObserver<GoodsInfo>() {
                    @Override
                    public void onNext(GoodsInfo data) {
                        productId = data.getProductId();
                        title = data.getName();
                        detailAdapter.setDetailInfo(data);
                    }
                });
        //评价汇总
        Api.getInstance().goodsCommentTotal(cutId, manageId)
                .compose(RxResponse.<CommentsTotal>compatT())
                .subscribe(new ApiObserver<CommentsTotal>() {
                    @Override
                    public void onNext(CommentsTotal data) {
                        detailAdapter.setCommentsTotal(data);
                    }
                });
        //评价列表
        Map<String, Object> map = new HashMap<>();
        map.put("cutId", cutId);
        map.put("manageId", manageId);
        map.put("pageNo", 1);
        map.put("pageSize", 3);
        Api.getInstance().goodsComments(map)
                .compose(RxResponse.<Comments>compatT())
                .subscribe(new ApiObserver<Comments>() {
                    @Override
                    public void onNext(Comments data) {
                        detailAdapter.setGoodsComments(data.rows);
                    }
                });
    }

    @OnClick({R.id.tv_quick_loan})
    public void onClick(View view) {
        if (BuildConfig.FORCE_LOGIN && !UserHelper.getInstance(this).isLogin()) {
            DlgUtil.loginDlg(this, new DlgUtil.OnLoginSuccessListener() {
                @Override
                public void success(UserProfileAbstract data) {
                    goProduct(productId, title);
                }
            });
        } else goProduct(productId, title);
    }

    private void goProduct(String goodId, final String name) {
        DataHelper.getInstance(this).onCountUvPv("PraiseCutLoanHit", goodId);
        String id = UserHelper.getInstance(this).isLogin() ? UserHelper.getInstance(this).id() : App.androidId;
        Api.getInstance().queryGroupProductSkip(id, goodId)
                .compose(RxResponse.<String>compatT())
                .subscribe(new ApiObserver<String>() {
                    @Override
                    public void onNext(@NonNull String data) {
                        Intent intent = new Intent(getApplicationContext(), WebViewActivity.class);
                        intent.putExtra("webViewUrl", data);
                        intent.putExtra("webViewTitleName", name);
                        startActivity(intent);
                    }
                });
    }
}