package com.beiwo.klyjaz.social.fragment;


import android.graphics.Rect;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.beiwo.klyjaz.R;
import com.beiwo.klyjaz.api.Api;
import com.beiwo.klyjaz.base.BaseComponentFragment;
import com.beiwo.klyjaz.helper.UserHelper;
import com.beiwo.klyjaz.social.adapter.TopicAdapter;
import com.beiwo.klyjaz.social.bean.ForumBean;
import com.beiwo.klyjaz.tang.rx.RxResponse;
import com.beiwo.klyjaz.tang.rx.observer.ApiObserver;

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
 * @date: 2018/12/6
 */
public class TopicFragment extends BaseComponentFragment {
    @BindView(R.id.recycler)
    RecyclerView recycler;

    private int orderType;
    private String topicId;
    private int pageNo = 1;
    private int pageSize = 10;
    private Map<String, Object> map = new HashMap<>();
    private TopicAdapter topicAdapter = new TopicAdapter();
    private UserHelper userHelper;

    @Override
    public int getLayoutResId() {
        return R.layout.temlapte_recycler;
    }

    @Override
    public void configViews() {
        orderType = getArguments().getInt("type");
        topicId = getArguments().getString("topicId");
        userHelper = UserHelper.getInstance(getActivity());
    }

    @Override
    public void initDatas() {
        initRecycler();
        request(pageNo);
    }

    private void request(final int pageNo) {
        map.put("pageNo", pageNo);
        map.put("pageSize", pageSize);
        map.put("orderType", orderType);
        map.put("topicId", topicId);
        if (userHelper.isLogin()) map.put("userId", userHelper.id());
        else map.remove("userId");
        Api.getInstance().topicList(map)
                .compose(RxResponse.<List<ForumBean>>compatT())
                .subscribe(new ApiObserver<List<ForumBean>>() {
                    @Override
                    public void onNext(List<ForumBean> data) {
                        if (pageNo == 1) {
                            topicAdapter.getData().clear();
                            topicAdapter.addData(data);
                        } else {
                            topicAdapter.addData(data);
                        }
                    }

                    @Override
                    public void onError(Throwable t) {
                        super.onError(t);
                        empty();
                    }
                });
    }

    private void empty() {
        topicAdapter.setNewData(null);
        topicAdapter.setEmptyView(R.layout.empty_layout, recycler);
        TextView tv_content = topicAdapter.getEmptyView().findViewById(R.id.tv_content);
        tv_content.setText("话题暂无参与");
    }

    private void initRecycler() {
        recycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        recycler.setHasFixedSize(true);
        recycler.setItemAnimator(new DefaultItemAnimator());
        recycler.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        recycler.setAdapter(topicAdapter);
    }

    public static TopicFragment getInstance() {
        return new TopicFragment();
    }
}