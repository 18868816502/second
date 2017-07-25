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
import com.beihui.market.injection.component.AppComponent;
import com.beihui.market.injection.component.DaggerTabHomeComponent;
import com.beihui.market.injection.module.TabHomeModule;
import com.beihui.market.ui.activity.LoanDetailActivity;
import com.beihui.market.ui.activity.UserAuthorizationActivity;
import com.beihui.market.ui.activity.WorthTestActivity;
import com.beihui.market.ui.adapter.GonglueAdapter;
import com.beihui.market.ui.adapter.LoanRVAdapter;
import com.beihui.market.ui.bean.BannerData;
import com.beihui.market.ui.bean.GonglueData;
import com.beihui.market.ui.busevents.NavigateLoan;
import com.beihui.market.ui.contract.TabHomeContract;
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

        loanRVAdapter = new LoanRVAdapter();
        loanRVAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(getActivity(), LoanDetailActivity.class);
                startActivity(intent);
            }
        });

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
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

        List<BannerData> list = new ArrayList<>();
        BannerData data1 = new BannerData();
        data1.setImgUrl("http://p4.so.qhimgs1.com/t01769129c2d71c24aa.jpg");
        BannerData data2 = new BannerData();
        data2.setImgUrl("http://p0.so.qhmsg.com/sdr/1728_1080_/t01f9607473316946ef.jpg");
        BannerData data3 = new BannerData();
        data3.setImgUrl("http://p0.so.qhimgs1.com/t01c558f3d2bee4917c.jpg");
        list.add(data1);
        list.add(data2);
        list.add(data3);
        headerViewHolder.loadBanner(list);

        List<String> info = new ArrayList<>();
        info.add("187****0421在闪贷侠成功借款1000元");
        info.add("150****7450在爱信钱包成功借款1000元");
        info.add("176****2105在点融网成功借款700元");
        info.add("150****1122在贷款王成功借款500元");
        info.add("134****0123在爱信钱包成功借款2000元");
        info.add("138****4422在闪贷侠成功借款800元");
        headerViewHolder.marqueeView.startWithList(info);

        List<GonglueData> gonglueDataList = new ArrayList<>();
        GonglueData d1 = new GonglueData();
        d1.setTitle("借款攻略");
        d1.setImageUrl("http://p2.so.qhimgs1.com/sdr/1728_1080_/t01a93405bee16d9592.jpg");
        GonglueData d2 = new GonglueData();
        d2.setTitle("还款缓兵之计");
        d2.setImageUrl("http://p3.so.qhmsg.com/sdr/1728_1080_/t01d81c186009d6aba8.jpg");
        GonglueData d3 = new GonglueData();
        d3.setTitle("如何提升信用");
        d3.setImageUrl("http://p0.so.qhimgs1.com/sdr/1728_1080_/t01c73851a56941d220.jpg");
        GonglueData d4 = new GonglueData();
        d4.setTitle("如何借款");
        d4.setImageUrl("http://p3.so.qhmsg.com/sdr/1728_1080_/t01af7e99f4d3abfc68.jpg");
        GonglueData d5 = new GonglueData();
        d5.setTitle("借款神器");
        d5.setImageUrl("http://p1.so.qhimgs1.com/t01eb20327ed434696d.jpg");
        GonglueData d6 = new GonglueData();
        d6.setTitle("是你的借款");
        d6.setImageUrl("http://p4.so.qhmsg.com/sdr/1728_1080_/t01e9dc7909923b9349.jpg");
        gonglueDataList.add(d1);
        gonglueDataList.add(d2);
        gonglueDataList.add(d3);
        gonglueDataList.add(d4);
        gonglueDataList.add(d5);
        gonglueDataList.add(d6);

        gonglueAdapter.onReference(gonglueDataList);

        List<String> tempList = new ArrayList<>();
        for (int i = 0; i < 10; ++i) {
            tempList.add("" + i);
        }
        loanRVAdapter.notifyDataSetChanged(tempList);

        if (inputMoney != null) {
            headerViewHolder.etMoney.setText(inputMoney);
        }
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
    }

    @Override
    public void showErrorMsg(String msg) {

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

        Unbinder headerUnbinder;

        HeaderViewHolder(View itemView) {
            this.itemView = itemView;
            headerUnbinder = ButterKnife.bind(this, itemView);

            banner.setDelayTime(1500);
            banner.setIndicatorGravity(BannerConfig.RIGHT);
            banner.isAutoPlay(true);
        }

        void destroy() {
            headerUnbinder.unbind();
        }

        void setOnClickListener(View.OnClickListener listener) {
            lyEtbg.setOnClickListener(listener);
            tvTuiJian.setOnClickListener(listener);
            tvMore.setOnClickListener(listener);
        }

        void loadBanner(List<BannerData> list) {
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
