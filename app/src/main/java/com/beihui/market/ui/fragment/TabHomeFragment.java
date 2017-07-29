package com.beihui.market.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.beihui.market.R;
import com.beihui.market.base.BaseTabFragment;
import com.beihui.market.entity.AdBanner;
import com.beihui.market.injection.component.AppComponent;
import com.beihui.market.injection.component.DaggerTabHomeComponent;
import com.beihui.market.injection.module.TabHomeModule;
import com.beihui.market.ui.activity.LoanDetailActivity;
import com.beihui.market.ui.activity.UserAuthorizationActivity;
import com.beihui.market.ui.activity.WorthTestActivity;
import com.beihui.market.ui.adapter.GonglueAdapter;
import com.beihui.market.ui.adapter.LoanRVAdapter;
import com.beihui.market.ui.busevents.NavigateLoan;
import com.beihui.market.ui.contract.TabHomeContract;
import com.beihui.market.ui.dialog.AdDialog;
import com.beihui.market.ui.presenter.TabHomePresenter;
import com.beihui.market.util.CommonUtils;
import com.beihui.market.util.InputMethodUtil;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.sunfusheng.marqueeview.MarqueeView;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.loader.ImageLoader;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public class TabHomeFragment extends BaseTabFragment implements View.OnClickListener, TabHomeContract.View {
    @BindView(R.id.root_container)
    FrameLayout rootContainer;
    @BindView(R.id.faked_bar)
    View fakedBar;
    @BindView(R.id.tool_bar_container)
    View toolBarContainer;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.recycle_view)
    RecyclerView recyclerView;
    @BindView(R.id.center_text)
    TextView center_text;

    @Inject
    TabHomePresenter presenter;

    private LoanRVAdapter loanRVAdapter;

    //记录顶部banner的高度
    private int bannerHeight;
    //status and tool bar render
    public int toolBarBgAlpha;

    private HeaderViewHolder headerViewHolder;

    private GonglueAdapter gonglueAdapter;

    //记录输入的钱数
    private String inputMoney;

    private RecyclerView.OnScrollListener onScrollListener = new RecyclerView.OnScrollListener() {
        int scrollY;

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            scrollY += dy;

            int maxMove = bannerHeight / 2;
            if (scrollY <= maxMove) {
                if (scrollY < 5) {
                    toolBarBgAlpha = 0;
                } else if (scrollY >= 10 && scrollY < maxMove) {
                    float ratio = (float) scrollY / maxMove;
                    toolBarBgAlpha = (int) (ratio * 255);
                } else {
                    toolBarBgAlpha = 255;
                }
            } else {
                toolBarBgAlpha = 255;
            }

            renderStatusAndToolBar(toolBarBgAlpha);
        }

    };

    public static TabHomeFragment newInstance() {
        return new TabHomeFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onDestroyView() {
        headerViewHolder.banner.stopAutoPlay();
        headerViewHolder.destroy();
        super.onDestroyView();
    }

    void renderStatusAndToolBar(int textAlpha) {
        toolBarContainer.setBackgroundColor(Color.argb(textAlpha, 85, 145, 255));
        center_text.setTextColor(Color.argb(textAlpha, 255, 255, 255));
    }

    @Override
    public int getLayoutResId() {
        return R.layout.fragment_tab_home;
    }

    @Override
    public void configViews() {
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        //noinspection ConstantConditions
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);

        if (inputMoney != null) {
            headerViewHolder.etMoney.setText(inputMoney);
        }
        loanRVAdapter = new LoanRVAdapter();
        loanRVAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(getActivity(), LoanDetailActivity.class);
                startActivity(intent);
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.addOnScrollListener(onScrollListener);
        recyclerView.setAdapter(loanRVAdapter);

        LayoutInflater inflater =
                (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View headerView =
                inflater.inflate(R.layout.layout_tab_home_rv_header, recyclerView, false);
        headerViewHolder = new HeaderViewHolder(headerView);

        headerViewHolder.worthTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), WorthTestActivity.class);
                startActivity(intent);
            }
        });


        LinearLayoutManager layoutHorManager = new LinearLayoutManager(getActivity());
        layoutHorManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        headerViewHolder.recyclerView.setLayoutManager(layoutHorManager);
        gonglueAdapter = new GonglueAdapter(getActivity());
        headerViewHolder.recyclerView.setAdapter(gonglueAdapter);

        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) headerViewHolder.lyBanner.getLayoutParams();
        params.width = CommonUtils.getScreenMaxWidth(getActivity(), 0);
        bannerHeight = (params.width * 530) / 975;
        params.height = bannerHeight;

        headerViewHolder.lyBanner.setLayoutParams(params);
        loanRVAdapter.setHeaderView(headerView);

        int statusHeight = CommonUtils.getStatusBarHeight(getActivity());
        ViewGroup.LayoutParams lp = fakedBar.getLayoutParams();
        lp.height = statusHeight;
        fakedBar.setLayoutParams(lp);
        renderStatusAndToolBar(toolBarBgAlpha);

        headerViewHolder.setOnClickListener(this);

    }

    @Override
    public void initDatas() {
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
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ly_etbg:
                InputMethodUtil.openSoftKeyboard(getActivity(), headerViewHolder.etMoney);
                break;
            case R.id.tv_tuijian:
                if (TextUtils.isEmpty(headerViewHolder.etMoney.getText().toString()))
                    inputMoney = "0";
                else
                    inputMoney = headerViewHolder.etMoney.getText().toString();
                EventBus.getDefault().post(new NavigateLoan(inputMoney));
                break;
            case R.id.tv_more:
                break;
        }
    }

    @Override
    protected boolean needFakeStatusBar() {
        //fake status bar is unexpected.
        return false;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_tab_home, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        UserAuthorizationActivity.launch(getActivity(), rootContainer);
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void setPresenter(TabHomeContract.Presenter presenter) {
        //injected.nothing to do.
    }

    @Override
    public void showBanner(List<AdBanner> list) {
        headerViewHolder.loadBanner(list);
    }

    @Override
    public void showBorrowingScroll(List<String> list) {
        headerViewHolder.marqueeView.startWithList(list);
    }

    @Override
    public void showAdDialog(AdBanner ad) {
        new AdDialog().setAd(ad).show(getChildFragmentManager(), AdDialog.class.getSimpleName());
    }


    class HeaderViewHolder {
        View itemView;

        @BindView(R.id.banner)
        Banner banner;
        @BindView(R.id.iv_banner_one)
        ImageView ivBannerOne;
        @BindView(R.id.ly_banner)
        LinearLayout lyBanner;
        @BindView(R.id.marqueeView)
        MarqueeView marqueeView;
        @BindView(R.id.ly_etbg)
        LinearLayout lyEtbg;
        @BindView(R.id.et_money)
        EditText etMoney;
        @BindView(R.id.tv_tuijian)
        TextView tvTuiJian;
        @BindView(R.id.tv_more)
        TextView tvMore;
        @BindView(R.id.worth_test)
        View worthTest;
        @BindView(R.id.recycle_hor)
        RecyclerView recyclerView;

        Unbinder unbinder;

        HeaderViewHolder(View itemView) {
            this.itemView = itemView;
            unbinder = ButterKnife.bind(this, itemView);

            banner.setDelayTime(1500);
            banner.setIndicatorGravity(BannerConfig.RIGHT);
            banner.isAutoPlay(true);
        }

        void destroy() {
            marqueeView.stopFlipping();
            unbinder.unbind();
        }

        void setOnClickListener(View.OnClickListener listener) {
            lyEtbg.setOnClickListener(listener);
            tvTuiJian.setOnClickListener(listener);
            tvMore.setOnClickListener(listener);
        }

        void loadBanner(List<AdBanner> list) {
            List<String> images = new ArrayList<>();
            for (int i = 0; i < list.size(); ++i) {
                images.add(list.get(i).getImgUrl());
            }
            banner.setImages(images).setImageLoader(new BannerImageLoader()).start();
        }
    }

    private class BannerImageLoader extends ImageLoader {

        @Override
        public void displayImage(Context context, Object path, ImageView imageView) {
            Glide.with(context).load((String) path).into(imageView);
        }
    }
}
