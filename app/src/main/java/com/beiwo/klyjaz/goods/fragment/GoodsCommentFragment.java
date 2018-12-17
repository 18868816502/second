package com.beiwo.klyjaz.goods.fragment;


import android.support.annotation.NonNull;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.beiwo.klyjaz.R;
import com.beiwo.klyjaz.api.Api;
import com.beiwo.klyjaz.base.BaseComponentFragment;
import com.beiwo.klyjaz.entity.Comments;
import com.beiwo.klyjaz.goods.activity.GoodsCommentActivity;
import com.beiwo.klyjaz.goods.adapter.CommentListAdapter;
import com.beiwo.klyjaz.tang.rx.RxResponse;
import com.beiwo.klyjaz.tang.rx.observer.ApiObserver;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import io.reactivex.disposables.Disposable;

/**
 * https://gitee.com/tangbuzhi
 *
 * @author: Tangbuzhi
 * @version:
 * @package:
 * @description: 口子评价列表
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
    private CommentListAdapter commentAdapter = new CommentListAdapter(this);
    private GoodsCommentActivity context;

    @Override
    public int getLayoutResId() {
        return R.layout.template_refresh_recycler;
    }

    @Override
    public void configViews() {
        try {
            context = (GoodsCommentActivity) getActivity();
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
        commentAdapter.setHeadFlow(context.labels, tag);
        request(pageNo, tag);
        refresh_layout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                refresh_layout.finishRefresh();
                pageNo = 1;
                request(pageNo, tag);
            }

            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                refresh_layout.finishLoadMore();
                pageNo++;
                request(pageNo, tag);
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

    public void tagRefresh(String tag) {
        pageNo = 1;
        this.tag = tag;
        request(1, tag);
    }

    private void request(final int pageNo, String tag) {
        //评价列表
        map.put("cutId", cutId);
        map.put("manageId", manageId);
        map.put("pageNo", pageNo);
        map.put("pageSize", pageSize);
        //评价种类
        if (type != 2) map.put("type", type);
        else map.remove("type");
        //标签
        if (tag.equals("all")) map.remove("flag");
        else map.put("flag", tag);

        if (ApiObserver.disposables.size() > 0) {
            for (Disposable disposable : ApiObserver.disposables) {
                if (disposable != null && !disposable.isDisposed()) disposable.dispose();
            }
        }
        Api.getInstance().goodsComments(map)
                .compose(RxResponse.<Comments>compatT())
                .subscribe(new ApiObserver<Comments>() {
                    @Override
                    public void onNext(Comments data) {
                        if (pageNo == 1) {
                            if (data == null || data.rows == null || data.rows.size() == 0) {
                                commentAdapter.setCommentList(null);
                            } else commentAdapter.setCommentList(data.rows);
                        } else {
                            commentAdapter.appendComments(data.rows);
                        }
                    }

                    @Override
                    public void onError(Throwable t) {
                        super.onError(t);
                        commentAdapter.setCommentList(null);
                    }
                });
    }

    public static GoodsCommentFragment newInstance() {
        return new GoodsCommentFragment();
    }
}