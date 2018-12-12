package com.beiwo.klyjaz.goods.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.beiwo.klyjaz.R;
import com.beiwo.klyjaz.api.Api;
import com.beiwo.klyjaz.base.BaseComponentActivity;
import com.beiwo.klyjaz.entity.Comments;
import com.beiwo.klyjaz.entity.CommentsTotal;
import com.beiwo.klyjaz.entity.GoodsInfo;
import com.beiwo.klyjaz.goods.adapter.GoodsDetailAdapter;
import com.beiwo.klyjaz.helper.SlidePanelHelper;
import com.beiwo.klyjaz.tang.rx.RxResponse;
import com.beiwo.klyjaz.tang.rx.observer.ApiObserver;
import com.gyf.barlibrary.ImmersionBar;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;

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

    private String cutId, manageId;
    private GoodsDetailAdapter detailAdapter = new GoodsDetailAdapter();

    @Override
    public int getLayoutId() {
        return R.layout.activity_goods_detail;
    }

    @Override
    public void configViews() {
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
                startActivity(new Intent(getApplicationContext(), GoodsCommentActivity.class));
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
}