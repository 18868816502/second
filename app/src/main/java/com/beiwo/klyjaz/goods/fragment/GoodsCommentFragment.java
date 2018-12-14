package com.beiwo.klyjaz.goods.fragment;


import android.support.annotation.NonNull;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.beiwo.klyjaz.R;
import com.beiwo.klyjaz.api.Api;
import com.beiwo.klyjaz.base.BaseComponentFragment;
import com.beiwo.klyjaz.entity.Comments;
import com.beiwo.klyjaz.entity.CommentsTotal;
import com.beiwo.klyjaz.entity.Labels;
import com.beiwo.klyjaz.goods.TagUtil;
import com.beiwo.klyjaz.goods.activity.GoodsCommentActivity;
import com.beiwo.klyjaz.goods.adapter.GoodsCommentAdapter;
import com.beiwo.klyjaz.tang.rx.RxResponse;
import com.beiwo.klyjaz.tang.rx.observer.ApiObserver;
import com.beiwo.klyjaz.util.DensityUtil;
import com.beiwo.klyjaz.util.ToastUtil;
import com.beiwo.klyjaz.view.flowlayout.FlowLayout;
import com.beiwo.klyjaz.view.flowlayout.TagAdapter;
import com.beiwo.klyjaz.view.flowlayout.TagFlowLayout;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
        head();
        recycler.setFocusableInTouchMode(false);
        recycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        recycler.setHasFixedSize(true);
        recycler.setItemAnimator(new DefaultItemAnimator());
        recycler.setAdapter(commentAdapter);
    }

    private void request(final int pageNo) {
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

        Api.getInstance().goodsComments(map)
                .compose(RxResponse.<Comments>compatT())
                .subscribe(new ApiObserver<Comments>() {
                    @Override
                    public void onNext(Comments data) {
                        if (pageNo == 1) {
                            if (data == null || data.rows == null || data.rows.size() == 0) {
                                empty();
                            } else commentAdapter.setNewData(data.rows);
                        } else {
                            commentAdapter.addData(data.rows);
                        }
                    }

                    @Override
                    public void onError(Throwable t) {
                        super.onError(t);
                        empty();
                    }
                });
    }

    private void head() {
        if (context.labels != null && context.labels.size() > 0) {
            final TagFlowLayout flowLayout = new TagFlowLayout(getActivity());
            ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            flowLayout.setLayoutParams(layoutParams);
            flowLayout.setPadding(DensityUtil.dp2px(context, 15f), 0, DensityUtil.dp2px(context, 15f), 0);
            commentAdapter.setHeaderView(flowLayout);
            List<String> tags = new ArrayList<>();
            int tagCountSum = 0;
            for (int i = 0; i < context.labels.size(); i++) {
                Labels label = context.labels.get(i);
                String value = TagUtil.getKeyValue(label.getFlag(), true);
                if (value == null) continue;
                int count = label.getCount();
                tagCountSum += count;
                tags.add(String.format(value, count));
            }
            if (tags.size() <= 0) return;
            tags.add(0, String.format(context.getString(R.string.all_comment), tagCountSum));//全部标签
            flowLayout.setAdapter(new TagAdapter<String>(tags) {
                @Override
                public View getView(FlowLayout parent, int position, String str) {
                    TextView textView = (TextView) LayoutInflater.from(context).inflate(R.layout.layout_tag, flowLayout, false);
                    textView.setText(str);
                    return textView;
                }
            });
            flowLayout.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
                @Override
                public boolean onTagClick(View view, int position, FlowLayout parent) {

                    return true;
                }
            });
            flowLayout.getAdapter().setSelectedList(0);
            /*if (tag.contains(",")) {
                String[] t = tag.split(",");
                flowLayout.getAdapter().setSelected(0, "all");
            } else {
                boolean contains = tags.contains(tag);
                if (contains) {
                    flowLayout.getAdapter().setSelected(tags.indexOf(tag), tag);
                }
            }*/
        }
    }

    private void empty() {
        commentAdapter.setNewData(null);
        commentAdapter.setEmptyView(R.layout.empty_layout, recycler);
        TextView tv_content = commentAdapter.getEmptyView().findViewById(R.id.tv_content);
        tv_content.setText("暂无评价");
    }

    public static GoodsCommentFragment newInstance() {
        return new GoodsCommentFragment();
    }
}