package com.beiwo.klyjaz.goods.fragment;


import android.support.annotation.NonNull;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.beiwo.klyjaz.R;
import com.beiwo.klyjaz.api.Api;
import com.beiwo.klyjaz.base.BaseComponentFragment;
import com.beiwo.klyjaz.entity.Comments;
import com.beiwo.klyjaz.entity.CommentsTotal;
import com.beiwo.klyjaz.goods.adapter.GoodsCommentAdapter;
import com.beiwo.klyjaz.tang.rx.RxResponse;
import com.beiwo.klyjaz.tang.rx.observer.ApiObserver;
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
 * @description:
 * @modify:
 * @date: 2018/12/13
 */
public class GoodsCommentFragment extends BaseComponentFragment {
    @BindView(R.id.refresh_layout)
    SmartRefreshLayout refresh_layout;
    @BindView(R.id.recycler)
    RecyclerView recycler;

    private int type;
    private String tag, cutId, manageId;
    private Map<String, Object> map = new HashMap<>();
    private int pageNo = 1;
    private int pageSize = 20;
    private GoodsCommentAdapter commentAdapter = new GoodsCommentAdapter();

    @Override
    public int getLayoutResId() {
        return R.layout.template_refresh_recycler;
    }

    @Override
    public void configViews() {
        try {
            type = getArguments().getInt("type");
            tag = getArguments().getString("tag");
            cutId = getArguments().getString("cutId");
            manageId = getArguments().getString("manageId");
        } catch (Exception e) {
        }
    }

    @Override
    public void initDatas() {
        initRecycler();
        request(pageNo);
        refresh_layout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                refresh_layout.finishRefresh();
                pageNo = 1;
                request(pageNo);
            }

            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                refresh_layout.finishLoadMore();
                pageNo++;
                request(pageNo);
            }
        });
    }

    private void initRecycler() {
        recycler.setFocusableInTouchMode(false);
        recycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        recycler.setHasFixedSize(true);
        recycler.setItemAnimator(new DefaultItemAnimator());
        recycler.setAdapter(commentAdapter);
    }

    private void request(final int pageNo) {
        //评价汇总
        Api.getInstance().goodsCommentTotal(cutId, manageId)
                .compose(RxResponse.<CommentsTotal>compatT())
                .subscribe(new ApiObserver<CommentsTotal>() {
                    @Override
                    public void onNext(CommentsTotal data) {
                        //detailAdapter.setCommentsTotal(data);
                    }
                });
        //评价列表
        map.put("cutId", cutId);
        map.put("manageId", manageId);
        if (type != 0) map.put("type", type);
        else map.remove("type");
        map.put("pageNo", pageNo);
        map.put("pageSize", pageSize);
        Api.getInstance().goodsComments(map)
                .compose(RxResponse.<Comments>compatT())
                .subscribe(new ApiObserver<Comments>() {
                    @Override
                    public void onNext(Comments data) {
                        if (pageNo == 1) {
                            commentAdapter.getData().clear();
                            commentAdapter.addData(data.rows);
                        } else {
                            commentAdapter.addData(data.rows);
                        }
                    }
                });
    }

    public static GoodsCommentFragment newInstance() {
        return new GoodsCommentFragment();
    }
}