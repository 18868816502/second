package com.beiwo.klyjaz.tang.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.beiwo.klyjaz.R;
import com.beiwo.klyjaz.api.Api;
import com.beiwo.klyjaz.base.BaseComponentFragment;
import com.beiwo.klyjaz.entity.UserProfileAbstract;
import com.beiwo.klyjaz.helper.DataStatisticsHelper;
import com.beiwo.klyjaz.helper.UserHelper;
import com.beiwo.klyjaz.injection.component.AppComponent;
import com.beiwo.klyjaz.social.activity.ForumDetailActivity;
import com.beiwo.klyjaz.social.activity.ForumPublishActivity;
import com.beiwo.klyjaz.social.bean.SocialTopicBean;
import com.beiwo.klyjaz.tang.DlgUtil;
import com.beiwo.klyjaz.tang.adapter.RecomAdapter;
import com.beiwo.klyjaz.tang.rx.RxResponse;
import com.beiwo.klyjaz.tang.rx.observer.ApiObserver;
import com.beiwo.klyjaz.ui.activity.PersonalCenterActivity;
import com.beiwo.klyjaz.umeng.NewVersionEvents;
import com.beiwo.klyjaz.util.CommonUtils;
import com.beiwo.klyjaz.util.DensityUtil;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.annotations.NonNull;

/**
 * https://gitee.com/tangbuzhi
 *
 * @author: Tangbuzhi
 * @version:
 * @package:
 * @description:
 * @modify:
 * @date: 2018/11/8
 */

public class SocialRecomFragment extends BaseComponentFragment {
    @BindView(R.id.hold_view)
    View hold_view;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.toolbar_title)
    TextView toolbar_title;
    @BindView(R.id.iv_publish)
    ImageView iv_publish;
    @BindView(R.id.refresh_layout)
    SmartRefreshLayout refresh_layout;
    @BindView(R.id.recycler)
    RecyclerView recycler;

    private RecomAdapter adapter = new RecomAdapter();
    private Map<String, Object> map = new HashMap<>();
    private int pageNo = 1;
    private int pageSize = 30;
    private UserHelper userHelper;
    private int spanSpec;
    private boolean praise;//标记item点赞状态

    public static SocialRecomFragment newInstance() {
        return new SocialRecomFragment();
    }

    @Override
    public int getLayoutResId() {
        return R.layout.fragment_social_recom;
    }

    @Override
    public void configViews() {
        int statusHeight = CommonUtils.getStatusBarHeight(getActivity());
        ViewGroup.LayoutParams params = hold_view.getLayoutParams();
        params.height = statusHeight;
        hold_view.setBackgroundResource(R.color.white);
        hold_view.setLayoutParams(params);
        toolbar_title.setText("社区");

        userHelper = UserHelper.getInstance(getActivity());
        spanSpec = DensityUtil.dp2px(getContext(), 15f);

        map.put("pageSize", pageSize);
    }

    @Override
    public void initDatas() {
        initRecycler();
        request(pageNo);
        refresh_layout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@android.support.annotation.NonNull RefreshLayout refreshLayout) {
                pageNo = 1;
                request(pageNo);
            }
        });
        refresh_layout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@android.support.annotation.NonNull RefreshLayout refreshLayout) {
                pageNo++;
                request(pageNo);
            }
        });
        adapter.setOnArticleClickListener(new RecomAdapter.OnArticleClickListener() {
            @Override
            public void itemClick(String forumId, String userId) {
                DataStatisticsHelper.getInstance().onCountUvPv(NewVersionEvents.COMMUNITY_FORUM_HIT, forumId);
//                Intent intent = new Intent(getActivity(), ArticleDetailActivity.class);
                Intent intent = new Intent(getActivity(), ForumDetailActivity.class);
                intent.putExtra("forumId", forumId);
                intent.putExtra("userId", userId);
                startActivity(intent);
            }

            @Override
            public void userClick(String userId) {
                if (!UserHelper.getInstance(getActivity()).isLogin()) {
                    DlgUtil.loginDlg(getActivity(), null);
                    return;
                }
                Intent intent = new Intent(getActivity(), PersonalCenterActivity.class);
                intent.putExtra("userId", userId);
                startActivity(intent);
            }

            @Override
            public void praiseClick(final SocialTopicBean.ForumBean item, final TextView tv) {
                if (!UserHelper.getInstance(getActivity()).isLogin()) {
                    DlgUtil.loginDlg(getActivity(), null);
                    return;
                }
                String userId = UserHelper.getInstance(getActivity()).id();
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
                                    adapter.setPraiseState(item, tv);
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
                                    adapter.setPraiseState(item, tv);
                                }
                            });
                }
            }
        });
    }

    private void request(final int pageNo) {
        map.put("pageNo", pageNo);
        if (userHelper.isLogin()) map.put("userId", userHelper.id());
        Api.getInstance().queryRecommendTopic(map)
                .compose(RxResponse.<SocialTopicBean>compatT())
                .subscribe(new ApiObserver<SocialTopicBean>() {
                    @Override
                    public void onNext(@NonNull SocialTopicBean data) {
                        refresh_layout.finishRefresh();
                        refresh_layout.finishLoadMore();
                        if (data == null || data.getForum() == null) {
                            empty();
                        } else {
                            if (pageNo == 1) {
                                adapter.getData().clear();
                                adapter.addData(data.getForum());
                            } else {
                                adapter.addData(data.getForum());
                            }
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable t) {
                        super.onError(t);
                        refresh_layout.finishRefresh();
                        refresh_layout.finishLoadMore();
                        empty();
                    }
                });
    }

    private void empty() {
        adapter.setNewData(null);
        adapter.setEmptyView(R.layout.empty_layout, recycler);
        TextView tv_content = adapter.getEmptyView().findViewById(R.id.tv_content);
        tv_content.setText("抱歉，我们还没有文章~");
    }

    private void initRecycler() {
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        layoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_NONE);
        layoutManager.setAutoMeasureEnabled(true);
        recycler.setLayoutManager(layoutManager);
        recycler.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                outRect.top = spanSpec / 3;
                outRect.bottom = spanSpec / 3;
                StaggeredGridLayoutManager.LayoutParams params =
                        (StaggeredGridLayoutManager.LayoutParams) view.getLayoutParams();
                if (params.getSpanIndex() % 2 == 0) {//span left
                    outRect.left = spanSpec;
                    outRect.right = spanSpec / 3;
                } else {//span right
                    outRect.left = spanSpec / 3;
                    outRect.right = spanSpec;
                }
            }
        });
        recycler.setHasFixedSize(true);
        recycler.setItemAnimator(new DefaultItemAnimator());
        recycler.setAdapter(adapter);
        recycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                iv_publish.setVisibility(dy > 0 ? View.VISIBLE : View.GONE);
            }
        });
    }

    @Override
    protected void configureComponent(AppComponent appComponent) {
    }

    @OnClick(R.id.iv_publish)
    public void onViewClick(View view) {
        switch (view.getId()) {
            case R.id.iv_publish:
                DataStatisticsHelper.getInstance().onCountUvPv(NewVersionEvents.COMMUNITY_PUBLISH_PAGE, "");
                if (UserHelper.getInstance(getActivity()).isLogin()) {
//                    startActivity(new Intent(getActivity(), CommunityPublishActivity.class));
                    startActivity(new Intent(getActivity(), ForumPublishActivity.class));
                } else {
                    DlgUtil.loginDlg(getActivity(), new DlgUtil.OnLoginSuccessListener() {
                        @Override
                        public void success(UserProfileAbstract data) {
                            pageNo = 1;
                            request(pageNo);
                        }
                    });
                }
                break;
            default:
                break;
        }
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
    public void recieve(String msg) {
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
}