package com.beihui.market.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.beihui.market.R;
import com.beihui.market.base.BaseTabFragment;
import com.beihui.market.entity.AdBanner;
import com.beihui.market.entity.HotNews;
import com.beihui.market.entity.LoanProduct;
import com.beihui.market.entity.NoticeAbstract;
import com.beihui.market.helper.DataStatisticsHelper;
import com.beihui.market.helper.UserHelper;
import com.beihui.market.injection.component.AppComponent;
import com.beihui.market.injection.component.DaggerTabHomeComponent;
import com.beihui.market.injection.module.TabHomeModule;
import com.beihui.market.ui.activity.ChoiceProductActivity;
import com.beihui.market.ui.activity.ComWebViewActivity;
import com.beihui.market.ui.activity.LoanDetailActivity;
import com.beihui.market.ui.activity.NewsDetailActivity;
import com.beihui.market.ui.activity.NoticeDetailActivity;
import com.beihui.market.ui.activity.ThirdAuthorizationActivity;
import com.beihui.market.ui.activity.UserAuthorizationActivity;
import com.beihui.market.ui.activity.WorthTestActivity;
import com.beihui.market.ui.adapter.HotChoiceRVAdapter;
import com.beihui.market.ui.adapter.HotNewsAdapter;
import com.beihui.market.ui.contract.TabHomeContract;
import com.beihui.market.ui.dialog.AdDialog;
import com.beihui.market.ui.presenter.TabHomePresenter;
import com.beihui.market.ui.rvdecoration.HotNewsItemDeco;
import com.beihui.market.umeng.Events;
import com.beihui.market.umeng.Statistic;
import com.beihui.market.util.CommonUtils;
import com.beihui.market.util.SPUtils;
import com.beihui.market.view.AutoTextView;
import com.beihui.market.view.StateLayout;
import com.beihui.market.view.WatchableScrollView;
import com.beihui.market.view.stateprovider.HomeStateViewProvider;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.loadmore.LoadMoreView;
import com.sunfusheng.marqueeview.MarqueeView;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.listener.OnBannerListener;
import com.youth.banner.loader.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;


public class TabHomeFragment extends BaseTabFragment implements TabHomeContract.View {
    @BindView(R.id.root_container)
    FrameLayout rootContainer;
    @BindView(R.id.faked_bar)
    View fakedBar;
    @BindView(R.id.tool_bar_container)
    View toolBarContainer;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.state_layout)
    StateLayout stateLayout;
    @BindView(R.id.refresh_layout)
    SwipeRefreshLayout refreshLayout;
    @BindView(R.id.center_text)
    TextView center_text;

    @BindView(R.id.scroll_view)
    WatchableScrollView scrollView;
    @BindView(R.id.banner)
    Banner banner;
    @BindView(R.id.marquee_view)
    MarqueeView marqueeView;
    @BindView(R.id.refresh_hot)
    View refreshHot;
    @BindView(R.id.refresh_icon)
    ImageView refreshIcon;
    @BindView(R.id.hot_recycler_view)
    RecyclerView hotRecyclerView;
    @BindView(R.id.choice_recycler_view)
    RecyclerView choiceRecyclerView;
    @BindView(R.id.quality_test)
    View qualityTestView;
    @BindView(R.id.loan_news_recycler_view)
    RecyclerView loanNewsRecyclerView;

    @BindView(R.id.notice_container)
    FrameLayout noticeContainer;
    @BindView(R.id.notice_close)
    ImageView noticeCloseIv;
    @BindView(R.id.notice_text)
    AutoTextView noticeTv;

    @BindView(R.id.one_key_loan_holder)
    View oneKeyLoanHolder;
    @BindView(R.id.one_key_loan)
    View oneKeyLoan;
    @BindView(R.id.one_key_loan_hint_holder)
    View oneKeyLoanHintHolder;
    @BindView(R.id.one_key_loan_hint_close)
    View oneKeyLoanHintClose;

    @Inject
    TabHomePresenter presenter;

    private boolean oneKeyLoanHintClosed;

    private Animation animation;

    private HotChoiceRVAdapter hotAdapter;
    private HotChoiceRVAdapter choiceAdapter;
    private HotNewsAdapter newsAdapter;

    //status and tool bar render
    public float toolBarBgAlpha;

    private List<AdBanner> bannerAds = new ArrayList<>();

    public static TabHomeFragment newInstance() {
        return new TabHomeFragment();
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        //pv，uv统计
        DataStatisticsHelper.getInstance().onCountUv(DataStatisticsHelper.ID_ENTER_HOME);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //umeng统计
        Statistic.onEvent(Events.ENTER_HOME_PAGE);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        banner.startAutoPlay();
    }

    @Override
    public void onPause() {
        super.onPause();
        banner.stopAutoPlay();
    }

    @Override
    public void onDestroyView() {
        marqueeView.stopFlipping();
        presenter.onDestroy();
        super.onDestroyView();
    }

    void renderStatusAndToolBar(float alpha) {
        toolBarBgAlpha = alpha;
        int alphaInt = (int) (alpha * 255);
        alphaInt = alphaInt < 10 ? 0 : alphaInt;
        alphaInt = alphaInt > 255 ? 255 : alphaInt;
        toolBarContainer.setBackgroundColor(Color.argb(alphaInt, 85, 145, 255));
        center_text.setTextColor(Color.argb(alphaInt, 255, 255, 255));
    }

    @Override
    public int getLayoutResId() {
        return R.layout.fragment_tab_home;
    }

    @Override
    public void configViews() {
        stateLayout.setStateViewProvider(new HomeStateViewProvider(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.onStart();
            }
        }));

        int statusHeight = CommonUtils.getStatusBarHeight(getActivity());
        ViewGroup.LayoutParams lp = fakedBar.getLayoutParams();
        lp.height = statusHeight;
        fakedBar.setLayoutParams(lp);
        renderStatusAndToolBar(toolBarBgAlpha);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        //noinspection ConstantConditions
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);

        refreshLayout.setColorSchemeResources(R.color.colorPrimary);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                presenter.refresh();
            }
        });

        scrollView.setOnScrollListener(new WatchableScrollView.OnScrollListener() {
            @Override
            public void onScrolled(int dy) {
                int maxMove = banner.getHeight() / 2;
                renderStatusAndToolBar(dy / (float) maxMove);
            }
        });

        /*轮播*/
        banner.setDelayTime(5000);
        banner.setIndicatorGravity(BannerConfig.RIGHT);
        banner.isAutoPlay(true);
        banner.setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int position) {
                //统计点击
                AdBanner ad = bannerAds.get(position);
                DataStatisticsHelper.getInstance().onAdClicked(ad.getId(), 2);

                //pv，uv统计
                DataStatisticsHelper.getInstance().onCountUv(DataStatisticsHelper.ID_CLICK_BANNER);

                presenter.clickBanner(position);

            }
        });

        /*热门产品*/
        hotAdapter = new HotChoiceRVAdapter(R.layout.list_item_hot_product);
        hotAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                //pv，uv统计
                DataStatisticsHelper.getInstance().onCountUv(DataStatisticsHelper.ID_CLICK_HOT_PRODUCT);

                presenter.clickHotProduct(position);
            }
        });
        hotRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });
        hotRecyclerView.setAdapter(hotAdapter);

         /*热门产品刷新*/
        refreshHot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //pv，uv统计
                DataStatisticsHelper.getInstance().onCountUv(DataStatisticsHelper.ID_REFRESH_HOT_PRODUCT);

                animation = AnimationUtils.loadAnimation(getContext(), R.anim.refresh_animation);
                refreshIcon.startAnimation(animation);
                presenter.refreshHotProducts();
            }
        });
        /*一键借款*/
        oneKeyLoan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //pv，uv统计
                DataStatisticsHelper.getInstance().onCountUv(DataStatisticsHelper.ID_ONE_KEY_LOAN);

                presenter.clickOneKeyLoan();
            }
        });
        /*关闭一键借款提示*/
        oneKeyLoanHintClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //记录关闭状态
                oneKeyLoanHintClosed = true;
                oneKeyLoanHintHolder.setVisibility(View.GONE);
            }
        });

        /*精选产品*/
        choiceAdapter = new HotChoiceRVAdapter(R.layout.list_item_product_choice);
        choiceAdapter.setLoadMoreView(new LoadMoreView() {
            @Override
            public int getLayoutId() {
                return R.layout.layout_choice_load_more;
            }

            @Override
            protected int getLoadingViewId() {
                return R.id.loading;
            }

            @Override
            protected int getLoadFailViewId() {
                return R.id.failed;
            }

            @Override
            protected int getLoadEndViewId() {
                return R.id.complete;
            }
        });
        choiceAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                //pv，uv统计
                DataStatisticsHelper.getInstance().onCountUv(DataStatisticsHelper.ID_CLICK_CHOICE_PRODUCT);

                presenter.clickChoiceProduct(position);
            }
        });
        choiceAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                presenter.loadMoreChoiceProducts();
            }
        }, choiceRecyclerView);
        choiceRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        choiceRecyclerView.setAdapter(choiceAdapter);

        /*借款攻略*/
        newsAdapter = new HotNewsAdapter();
        newsAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                //pv，uv统计
                DataStatisticsHelper.getInstance().onCountUv(DataStatisticsHelper.ID_CLICK_HOT_NEWS);

                Intent intent = new Intent(getContext(), NewsDetailActivity.class);
                intent.putExtra("hotNews", (HotNews) adapter.getData().get(position));
                startActivity(intent);
            }
        });
        loanNewsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        loanNewsRecyclerView.addItemDecoration(new HotNewsItemDeco());
        loanNewsRecyclerView.setAdapter(newsAdapter);


        qualityTestView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //pv，uv统计
                DataStatisticsHelper.getInstance().onCountUv(DataStatisticsHelper.ID_CLICK_QUALITY_TEST);

                presenter.clickQualityTest();
            }
        });

        noticeCloseIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                noticeContainer.setVisibility(View.GONE);
                SPUtils.setNoticeClosed(getContext(), true);
            }
        });

    }

    @Override
    public void initDatas() {
        refreshLayout.setRefreshing(true);
        presenter.onStart();
    }

    @Override
    protected void configureComponent(AppComponent appComponent) {
        DaggerTabHomeComponent.builder()
                .appComponent(appComponent)
                .tabHomeModule(new TabHomeModule(this))
                .build()
                .inject(this);
    }

    @Override
    protected boolean needFakeStatusBar() {
        //fake status bar is unexpected.
        return false;
    }

    @Override
    public void setPresenter(TabHomeContract.Presenter presenter) {
        //injected.nothing to do.
    }

    @Override
    public void showBanner(List<AdBanner> list) {
        handleShowContent();
        if (list != null) {
            bannerAds.addAll(list);
            List<String> images = new ArrayList<>();
            for (int i = 0; i < list.size(); ++i) {
                images.add(list.get(i).getImgUrl());
            }
            banner.setImages(images).setImageLoader(new BannerImageLoader()).start();
        }
    }

    @Override
    public void showHeadline(List<String> list) {
        handleShowContent();
        marqueeView.startWithList(list);
    }

    @Override
    public void showHotProducts(List<LoanProduct.Row> products) {
        handleShowContent();
        if (animation != null && animation.hasStarted()) {
            animation.cancel();
            animation = null;
        }
        if (hotAdapter != null) {
            hotAdapter.notifyHotProductChanged(products);
        }
    }

    @Override
    public void updateOneKeyLoanVisibility(boolean visible) {
        int visibility = visible ? View.VISIBLE : View.GONE;
        oneKeyLoanHolder.setVisibility(visibility);
        oneKeyLoan.setVisibility(visibility);

        if (visible && !oneKeyLoanHintClosed) {
            oneKeyLoanHintHolder.setVisibility(visibility);
        }
    }

    @Override
    public void showChoiceProducts(List<LoanProduct.Row> products, boolean canLoadMore) {
        handleShowContent();
        if (choiceAdapter != null) {
            if (choiceAdapter.isLoading()) {
                if (canLoadMore) {
                    choiceAdapter.loadMoreComplete();
                } else {
                    choiceAdapter.loadMoreEnd(true);
                }
            }
            choiceAdapter.notifyHotProductChanged(products);
        }
    }

    @Override
    public void showHotNews(List<HotNews> news) {
        handleShowContent();
        if (newsAdapter != null) {
            newsAdapter.notifyHotNewsChanged(news);
        }
    }


    @Override
    public void showAdDialog(final AdBanner ad) {
        //pv，uv统计
        DataStatisticsHelper.getInstance().onCountUv(DataStatisticsHelper.ID_SHOW_HOME_AD_DIALOG);

        new AdDialog().setAd(ad).setListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //统计点击
                DataStatisticsHelper.getInstance().onAdClicked(ad.getId(), 3);

                //pv，uv统计
                DataStatisticsHelper.getInstance().onCountUv(DataStatisticsHelper.ID_CLICK_HONE_AD_DIALOG);

                //是否需要登录
                if (ad.needLogin()) {
                    if (UserHelper.getInstance(getContext()).getProfile() == null) {
                        UserAuthorizationActivity.launchWithPending(getActivity(), ad);
                        return;
                    }
                }

                //跳原生还是跳Web
                if (ad.isNative()) {
                    Intent intent = new Intent(getContext(), LoanDetailActivity.class);
                    intent.putExtra("loanId", ad.getLocalId());
                    startActivity(intent);
                } else if (!TextUtils.isEmpty(ad.getUrl())) {
                    Intent intent = new Intent(getContext(), ComWebViewActivity.class);
                    intent.putExtra("title", ad.getTitle());
                    intent.putExtra("url", ad.getUrl());
                    startActivity(intent);
                }
            }
        }).show(getChildFragmentManager(), AdDialog.class.getSimpleName());
    }

    @Override
    public void showNotice(NoticeAbstract notice) {
        final NoticeAbstract noticeAbstract = notice;
        noticeContainer.setVisibility(View.VISIBLE);
        noticeTv.setScrollMode(AutoTextView.SCROLL_FAST);
        noticeTv.setText(notice.getTitle());
        noticeContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), NoticeDetailActivity.class);
                intent.putExtra("noticeId", noticeAbstract.getId());
                startActivity(intent);

                noticeContainer.setVisibility(View.GONE);

                SPUtils.setNoticeClosed(getContext(), true);
            }
        });
    }

    @Override
    public void showErrorMsg(String msg) {
        super.showErrorMsg(msg);
        if (refreshLayout.isRefreshing()) {
            refreshLayout.setRefreshing(false);
        }
        if (animation != null && animation.hasStarted()) {
            animation.cancel();
            animation = null;
        }
    }

    @Override
    public void showError() {
        if (refreshLayout.isRefreshing()) {
            refreshLayout.setRefreshing(false);
        }
        if (animation != null && animation.hasStarted()) {
            animation.cancel();
            animation = null;
        }
        stateLayout.switchState(StateLayout.STATE_NET_ERROR);
    }

    @Override
    public void navigateLogin() {
        UserAuthorizationActivity.launch(getActivity(), null);
    }

    @Override
    public void navigateLoginWithPending(AdBanner adBanner) {
        UserAuthorizationActivity.launchWithPending(getActivity(), adBanner);
    }

    @Override
    public void navigateWorthTest() {
        Intent intent = new Intent(getContext(), WorthTestActivity.class);
        startActivity(intent);
    }

    @Override
    public void navigateProductDetail(LoanProduct.Row loan, String loanId) {
        Intent toDetail = new Intent(getContext(), LoanDetailActivity.class);
        if (loan != null) {
            toDetail.putExtra("loan", loan);
        } else {
            toDetail.putExtra("loanId", loanId);
        }
        startActivity(toDetail);
    }

    @Override
    public void navigateThirdAuthorization(List<String> ids) {
        Intent intent = new Intent(getContext(), ThirdAuthorizationActivity.class);
        intent.putStringArrayListExtra("ids", (ArrayList<String>) ids);
        startActivity(intent);
    }

    @Override
    public void navigateChoiceProduct() {
        startActivity(new Intent(getContext(), ChoiceProductActivity.class));
    }

    @Override
    public void navigateWeb(String title, String url) {
        Intent toWeb = new Intent(getContext(), ComWebViewActivity.class);
        toWeb.putExtra("title", title);
        toWeb.putExtra("url", url);
        startActivity(toWeb);
    }

    private void handleShowContent() {
        if (stateLayout != null) {
            stateLayout.switchState(StateLayout.STATE_CONTENT);
        }
        if (refreshLayout != null && refreshLayout.isRefreshing()) {
            refreshLayout.setRefreshing(false);
        }
    }


    /**
     * Banner Image Loader Class
     */
    class BannerImageLoader extends ImageLoader {

        @Override
        public void displayImage(Context context, Object path, ImageView imageView) {
            Glide.with(context)
                    .load((String) path)
                    .asBitmap()
                    .centerCrop()
                    .placeholder(R.mipmap.banner_place_holder)
                    .into(imageView);
        }

    }
}
