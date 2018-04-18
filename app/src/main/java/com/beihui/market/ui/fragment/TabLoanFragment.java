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
import com.beihui.market.entity.CreditCard;
import com.beihui.market.entity.HotNews;
import com.beihui.market.entity.LoanProduct;
import com.beihui.market.entity.NoticeAbstract;
import com.beihui.market.helper.DataStatisticsHelper;
import com.beihui.market.helper.UserHelper;
import com.beihui.market.injection.component.AppComponent;
import com.beihui.market.injection.component.DaggerTabLoanComponent;
import com.beihui.market.injection.module.TabLoanModule;
import com.beihui.market.ui.activity.ChoiceProductActivity;
import com.beihui.market.ui.activity.ComWebViewActivity;
import com.beihui.market.ui.activity.CreditCardWebActivity;
import com.beihui.market.ui.activity.LoanDetailActivity;
import com.beihui.market.ui.activity.LoanProductActivity;
import com.beihui.market.ui.activity.NewsDetailActivity;
import com.beihui.market.ui.activity.NoticeDetailActivity;
import com.beihui.market.ui.activity.ThirdAuthorizationActivity;
import com.beihui.market.ui.activity.UserAuthorizationActivity;
import com.beihui.market.ui.activity.WorthTestActivity;
import com.beihui.market.ui.adapter.CreditCardRVAdapter;
import com.beihui.market.ui.adapter.HotChoiceRVAdapter;
import com.beihui.market.ui.adapter.HotNewsAdapter;
import com.beihui.market.ui.busevents.NavigateNews;
import com.beihui.market.ui.contract.TabLoanContract;
import com.beihui.market.ui.dialog.AdDialog;
import com.beihui.market.ui.presenter.TabLoanPresenter;
import com.beihui.market.ui.rvdecoration.HomeCreditCardItemDeco;
import com.beihui.market.ui.rvdecoration.HotNewsItemDeco;
import com.beihui.market.umeng.Events;
import com.beihui.market.umeng.Statistic;
import com.beihui.market.util.CommonUtils;
import com.beihui.market.util.SPUtils;
import com.beihui.market.view.AutoTextView;
import com.beihui.market.view.StateLayout;
import com.beihui.market.view.WatchableScrollView;
import com.beihui.market.view.banner.Banner;
import com.beihui.market.view.banner.BannerConfig;
import com.beihui.market.view.banner.listener.OnBannerListener;
import com.beihui.market.view.banner.loader.ImageLoader;
import com.beihui.market.view.stateprovider.HomeStateViewProvider;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.loadmore.LoadMoreView;
import com.sunfusheng.marqueeview.MarqueeView;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

/**
 * 借款模块
 */
public class TabLoanFragment extends BaseTabFragment implements TabLoanContract.View {
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

    @BindView(R.id.module_personal_loan)
    View modulePersonalLoan;
    @BindView(R.id.module_smart_loan)
    View moduleSmartLoan;
    @BindView(R.id.module_credit_card)
    View moduleCreditCard;

    @BindView(R.id.refresh_hot)
    View refreshHot;
    @BindView(R.id.refresh_icon)
    ImageView refreshIcon;
    /**
     * 热门产品列表
     */
    @BindView(R.id.hot_recycler_view)
    RecyclerView hotRecyclerView;

    @BindView(R.id.choice_more)
    View choiceMore;
    @BindView(R.id.choice_recycler_view)
    RecyclerView choiceRecyclerView;

    @BindView(R.id.credit_card_head)
    View creditCardHead;
    @BindView(R.id.credit_card_more)
    View creditCardMore;
    @BindView(R.id.credit_card_recycler_view)
    RecyclerView creditCardRecyclerView;

    @BindView(R.id.loan_news_more)
    View loanNewsMore;
    @BindView(R.id.loan_news_recycler_view)
    RecyclerView loanNewsRecyclerView;

    @BindView(R.id.quality_test)
    View qualityTestView;

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
    TabLoanPresenter presenter;

    private boolean oneKeyLoanHintClosed;

    private Animation animation;
    //热门产品适配器
    private HotChoiceRVAdapter hotAdapter;
    private HotChoiceRVAdapter choiceAdapter;
    private CreditCardRVAdapter creditCardAdapter;
    private HotNewsAdapter newsAdapter;

    //status and tool bar render
    public float toolBarBgAlpha;

    private List<AdBanner> bannerAds = new ArrayList<>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //umeng统计
        Statistic.onEvent(Events.ENTER_HOME_PAGE);

        //pv，uv统计
        DataStatisticsHelper.getInstance().onCountUv(DataStatisticsHelper.ID_ENTER_HOME);
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

    public static TabLoanFragment newInstance() {
        return new TabLoanFragment();
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
                //umeng统计
                Statistic.onEvent(Events.CLICK_BANNER);

                //统计点击
                AdBanner ad = bannerAds.get(position);
                DataStatisticsHelper.getInstance().onAdClicked(ad.getId(), 2);

                //pv，uv统计
                DataStatisticsHelper.getInstance().onCountUv(DataStatisticsHelper.ID_CLICK_BANNER);

                presenter.clickBanner(position);

            }
        });

        /*精选好借，智能推荐，办信用卡*/
        final View.OnClickListener moduleClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.module_personal_loan:
                        //pv，uv统计
                        DataStatisticsHelper.getInstance().onCountUv(DataStatisticsHelper.ID_HOME_MODULE_PERSONAL_PRODUCT);

                        Intent toPersonalLoan = new Intent(getContext(), LoanProductActivity.class);
                        toPersonalLoan.putExtra("module_index", 0);
                        startActivity(toPersonalLoan);
                        break;
                    case R.id.module_smart_loan:
                        //pv，uv统计
                        DataStatisticsHelper.getInstance().onCountUv(DataStatisticsHelper.ID_HOME_MODULE_SMART);

                        Intent toSmartLoan = new Intent(getContext(), LoanProductActivity.class);
                        toSmartLoan.putExtra("module_index", 1);
                        startActivity(toSmartLoan);
                        break;
                    case R.id.module_credit_card:
                        //pv，uv统计
                        DataStatisticsHelper.getInstance().onCountUv(DataStatisticsHelper.ID_HOME_MODULE_CREDIT_CARD);

                        startActivity(new Intent(getContext(), CreditCardWebActivity.class));
                        break;
                    default:
                        break;
                }
            }
        };
        modulePersonalLoan.setOnClickListener(moduleClickListener);
        moduleSmartLoan.setOnClickListener(moduleClickListener);
        moduleCreditCard.setOnClickListener(moduleClickListener);

        /*热门产品*/
        hotAdapter = new HotChoiceRVAdapter(R.layout.list_item_hot_product);
        hotAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                //umeng统计
                Statistic.onEvent(Events.CLICK_HOT_PRODUCT);

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
                //umeng统计
                Statistic.onEvent(Events.CLICK_REFRESH_HOT);

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
                //umeng统计
                Statistic.onEvent(Events.CLICK_ONE_KEY_LOAN);

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
                //umeng统计
                Statistic.onEvent(Events.CLICK_CHOICE_PRODUCT);

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
        choiceMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), LoanProductActivity.class));
            }
        });

        /*推荐信用卡*/
        creditCardAdapter = new CreditCardRVAdapter();
        creditCardAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                presenter.clickCreditCard(position);
            }
        });
        creditCardRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        creditCardRecyclerView.setAdapter(creditCardAdapter);
        creditCardRecyclerView.addItemDecoration(new HomeCreditCardItemDeco());
        creditCardMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), CreditCardWebActivity.class));
            }
        });

        /*借款攻略*/
        newsAdapter = new HotNewsAdapter();
        newsAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                //umeng统计
                Statistic.onEvent(Events.CLICK_HOT_NEWS);

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
        loanNewsMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new NavigateNews());
            }
        });

        /*测试身价*/
        qualityTestView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //umeng统计
                Statistic.onEvent(Events.HOME_CLICK_TEST);

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
        DaggerTabLoanComponent.builder()
                .appComponent(appComponent)
                .tabLoanModule(new TabLoanModule(this))
                .build()
                .inject(this);
    }

    @Override
    protected boolean needFakeStatusBar() {
        //fake status bar is unexpected.
        return false;
    }

    @Override
    public void setPresenter(TabLoanContract.Presenter presenter) {
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

        if (visible) {
            if (!oneKeyLoanHintClosed) {
                oneKeyLoanHintHolder.setVisibility(visibility);
            }
        } else {
            oneKeyLoanHintHolder.setVisibility(View.GONE);
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
    public void showCreditCards(List<CreditCard.Row> creditCards) {
        if (creditCards.size() == 0) {
            creditCardHead.setVisibility(View.GONE);
            creditCardRecyclerView.setVisibility(View.GONE);
        } else {
            creditCardHead.setVisibility(View.VISIBLE);
            creditCardRecyclerView.setVisibility(View.VISIBLE);
            creditCardAdapter.notifyCreditCardChanged(creditCards);
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
        //umeng统计
        Statistic.onEvent(Events.RESUME_AD_DIALOG);

        //pv，uv统计
        DataStatisticsHelper.getInstance().onCountUv(DataStatisticsHelper.ID_SHOW_HOME_AD_DIALOG);

        new AdDialog().setAd(ad).setListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //umeng统计
                Statistic.onEvent(Events.CLICK_AD_DIALOG);

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
                    String url = ad.getUrl();
                    if (url.contains("USERID") && UserHelper.getInstance(getContext()).getProfile() != null) {
                        url = url.replace("USERID", UserHelper.getInstance(getContext()).getProfile().getId());
                    }
                    Intent intent = new Intent(getContext(), ComWebViewActivity.class);
                    intent.putExtra("title", ad.getTitle());
                    intent.putExtra("url", url);
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

    @Override
    public void navigateCreditCard(String title, String url) {
        Intent intent = new Intent(getContext(), CreditCardWebActivity.class);
        intent.putExtra("url", url);
        intent.putExtra("title", title);
        startActivity(intent);
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
