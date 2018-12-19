package com.beiwo.klyjaz.social.fragment;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.widget.TextView;

import com.beiwo.klyjaz.R;
import com.beiwo.klyjaz.api.Api;
import com.beiwo.klyjaz.base.BaseComponentFragment;
import com.beiwo.klyjaz.helper.UserHelper;
import com.beiwo.klyjaz.social.activity.ForumDetailActivity;
import com.beiwo.klyjaz.social.adapter.TopicAdapter;
import com.beiwo.klyjaz.social.bean.ForumBean;
import com.beiwo.klyjaz.tang.DlgUtil;
import com.beiwo.klyjaz.tang.rx.RxResponse;
import com.beiwo.klyjaz.tang.rx.observer.ApiObserver;
import com.beiwo.klyjaz.ui.activity.PersonalCenterActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.lang.ref.WeakReference;
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
    private int pageSize = 20;
    private Map<String, Object> map = new HashMap<>();
    private TopicAdapter topicAdapter = new TopicAdapter();
    private UserHelper userHelper;
    private boolean praise;//标记item点赞状态

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
        topicAdapter.setOnTopicItemClickListener(new TopicAdapter.OnTopicItemClickListener() {
            @Override
            public void itemClick(String forumId, String userId) {
                Intent intent = new Intent(getActivity(), ForumDetailActivity.class);
                intent.putExtra("forumId", forumId);
                intent.putExtra("userId", userId);
                startActivity(intent);
            }

            @Override
            public void userClick(String userId) {
                if (!userHelper.isLogin()) {
                    DlgUtil.loginDlg(getActivity(), null);
                    return;
                }
                Intent intent = new Intent(getActivity(), PersonalCenterActivity.class);
                intent.putExtra("userId", userId);
                startActivity(intent);
            }

            @Override
            public void praiseClick(final ForumBean item, final WeakReference<TextView> tvRef) {
                if (!UserHelper.getInstance(getActivity()).isLogin()) {
                    DlgUtil.loginDlg(getActivity(), null);
                    return;
                }
                String userId = userHelper.id();
                praise = item.getIsPraise() == 1;
                if (praise) {
                    Api.getInstance().fetchCancelPraise(0, item.getForumId(), userId)
                            .compose(RxResponse.compatO())
                            .subscribe(new ApiObserver<Object>() {
                                @Override
                                public void onNext(@NonNull Object data) {
                                    praise = !praise;
                                    item.setIsPraise(0);
                                    item.setPraiseCount(item.getPraiseCount() - 1);
                                    topicAdapter.setPraiseState(item, tvRef.get());
                                }
                            });
                } else {
                    Api.getInstance().fetchClickPraise(0, item.getForumId(), userId)
                            .compose(RxResponse.<Object>compatO())
                            .subscribe(new ApiObserver<Object>() {
                                @Override
                                public void onNext(@NonNull Object data) {
                                    praise = !praise;
                                    item.setIsPraise(1);
                                    item.setPraiseCount(item.getPraiseCount() + 1);
                                    topicAdapter.setPraiseState(item, tvRef.get());
                                }
                            });
                }
            }
        });
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

    public void refresh() {
        pageNo = 1;
        request(pageNo);
    }

    public void loadMore() {
        pageNo++;
        request(pageNo);
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
        /*DividerItemDecoration decoration = new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL);
        recycler.addItemDecoration(n);*/
        recycler.setAdapter(topicAdapter);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                pageNo = 1;
                request(pageNo);
            }
        }, new IntentFilter("refresh_layout"));
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void recieveMsg(String msg) {
        if (TextUtils.equals("1", msg)) {
            pageNo = 1;
            request(pageNo);
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!EventBus.getDefault().isRegistered(this)) EventBus.getDefault().register(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) EventBus.getDefault().unregister(this);
    }

    public static TopicFragment getInstance() {
        return new TopicFragment();
    }
}