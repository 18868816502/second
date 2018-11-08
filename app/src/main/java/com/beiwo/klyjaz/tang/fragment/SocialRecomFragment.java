package com.beiwo.klyjaz.tang.fragment;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.beiwo.klyjaz.R;
import com.beiwo.klyjaz.api.Api;
import com.beiwo.klyjaz.base.BaseComponentFragment;
import com.beiwo.klyjaz.helper.UserHelper;
import com.beiwo.klyjaz.injection.component.AppComponent;
import com.beiwo.klyjaz.social.bean.SocialTopicBean;
import com.beiwo.klyjaz.tang.adapter.RecomAdapter;
import com.beiwo.klyjaz.tang.rx.RxResponse;
import com.beiwo.klyjaz.tang.rx.observer.ApiObserver;
import com.beiwo.klyjaz.util.CommonUtils;
import com.beiwo.klyjaz.util.DensityUtil;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
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
        spanSpec = DensityUtil.dp2px(getContext(), 10.5f);
        map.put("pageNo", pageNo);
        map.put("pageSize", pageSize);
    }

    @Override
    public void initDatas() {
        initRecycler();
        request();
    }

    private void request() {
        if (userHelper.isLogin()) map.put("userId", userHelper.id());
        Api.getInstance().queryRecommendTopic(map)
                .compose(RxResponse.<SocialTopicBean>compatT())
                .subscribe(new ApiObserver<SocialTopicBean>() {
                    @Override
                    public void onNext(@NonNull SocialTopicBean data) {
                        if (data == null || data.getForum() == null) {
                            adapter.setNewData(null);
                            adapter.setEmptyView(R.layout.empty_layout, recycler);
                        } else {
                            if (pageNo == 1) {
                                adapter.setNewData(data.getForum());
                            } else {
                                adapter.addData(data.getForum());
                            }
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable t) {
                        super.onError(t);
                        adapter.setNewData(null);
                        adapter.setEmptyView(R.layout.empty_layout, recycler);
                    }
                });
    }

    private void initRecycler() {
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        layoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_NONE);
        recycler.setLayoutManager(layoutManager);
        recycler.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                outRect.top = spanSpec;
                StaggeredGridLayoutManager.LayoutParams params =
                        (StaggeredGridLayoutManager.LayoutParams) view.getLayoutParams();
                if (params.getSpanIndex() % 2 == 0) {
                    outRect.left = spanSpec;
                    outRect.right = spanSpec / 2;
                } else {
                    outRect.left = spanSpec / 2;
                    outRect.right = spanSpec;
                }
                if (parent.getChildLayoutPosition(view) == parent.getAdapter().getItemCount() - 1) {
                    outRect.bottom = spanSpec;
                }
            }
        });
        recycler.setHasFixedSize(true);
        recycler.setItemAnimator(new DefaultItemAnimator());
        recycler.setAdapter(adapter);
    }

    @Override
    protected void configureComponent(AppComponent appComponent) {
    }
}