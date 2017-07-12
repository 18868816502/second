package com.beihui.market.ui.fragment;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.beihui.market.R;
import com.beihui.market.base.BaseRVFragment;
import com.beihui.market.base.BaseTabFragment;
import com.beihui.market.component.AppComponent;
import com.beihui.market.component.DaggerMainComponent;
import com.beihui.market.ui.adapter.GonglueAdapter;
import com.beihui.market.ui.adapter.LoanRVAdapter;
import com.beihui.market.ui.bean.BannerData;
import com.beihui.market.ui.bean.GonglueData;
import com.beihui.market.ui.bean.support.ReturnMain2;
import com.beihui.market.ui.contract.Main1Contract;
import com.beihui.market.ui.presenter.Main1Presenter;
import com.beihui.market.util.CommonUtils;
import com.beihui.market.util.InputMethodUtil;
import com.beihui.market.view.yrecycleview.YRecycleview;
import com.bumptech.glide.Glide;
import com.gyf.barlibrary.ImmersionBar;
import com.sunfusheng.marqueeview.MarqueeView;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.loader.ImageLoader;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public class TabHomeFragment extends BaseTabFragment implements Main1Contract.View, View.OnClickListener {

    @BindView(R.id.faked_bar)
    View fakedBar;
    @BindView(R.id.tool_bar_container)
    View toolBarContainer;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.recycle_view)
    RecyclerView mRecycleView;
    @BindView(R.id.center_text)
    TextView center_text;

    private LoanRVAdapter loanRVAdapter;

    //记录顶部banner的高度
    private int bannerHeight;
    //status and tool bar render
    public float alpha;
    public int textAlpha;

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
                if (scrollY < 10) {
                    alpha = 0;
                    toolbar.setVisibility(View.GONE);
                    textAlpha = 0;
                } else if (scrollY >= 10 && scrollY < maxMove) {
                    alpha = (float) scrollY / maxMove;
                    textAlpha = (int) (alpha * 255);
                    toolbar.setVisibility(View.VISIBLE);
                } else {
                    alpha = 1;
                    textAlpha = 255;
                    toolbar.setVisibility(View.VISIBLE);
                }
            } else {
                alpha = 1;
                textAlpha = 255;
                toolbar.setVisibility(View.VISIBLE);
            }

            renderStatusAndToolBar(alpha, textAlpha);
        }

    };


    public static TabHomeFragment newInstance() {
        TabHomeFragment f = new TabHomeFragment();
        Bundle b = new Bundle();
        b.putString("type", "TabLoanFragment");
        f.setArguments(b);
        return f;
    }

    @Override
    public void onDestroyView() {
        headerViewHolder.banner.stopAutoPlay();
        super.onDestroyView();
    }

    void renderStatusAndToolBar(float alpha, int textAlpha) {
        toolBarContainer.setAlpha(alpha);
        center_text.setTextColor(Color.argb(textAlpha, 255, 255, 255));
    }

    @Override
    public void attachView() {

    }

    @Override
    public int getLayoutResId() {
        return R.layout.fragment_tab_home;
    }

    @Override
    public void configViews() {
        loanRVAdapter = new LoanRVAdapter();

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecycleView.setLayoutManager(layoutManager);
        mRecycleView.addOnScrollListener(onScrollListener);
        mRecycleView.setAdapter(loanRVAdapter);

        LayoutInflater inflater =
                (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View headerView =
                inflater.inflate(R.layout.layout_tab_home_rv_header, mRecycleView, false);
        headerViewHolder = new HeaderViewHolder(headerView);


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
        renderStatusAndToolBar(alpha, textAlpha);

    }

    @Override
    public void initDatas() {
        headerViewHolder.setOnClickListener(this);

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

    }

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {
        DaggerMainComponent.builder().appComponent(appComponent).build().inject(this);
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

                EventBus.getDefault().post(new ReturnMain2(0, inputMoney));
                break;
            case R.id.tv_more:
                break;
        }
    }


    @Override
    public void showError(String err) {
        dismissDialog();
    }

    @Override
    public void complete() {
        dismissDialog();
    }

    @Override
    protected boolean needFakeStatusBar() {
        //fake status bar is unexpected.
        return false;
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
        @BindView(R.id.recycle_hor)
        RecyclerView recyclerView;

        Unbinder headerUnbinder;

        public HeaderViewHolder(View itemView) {
            this.itemView = itemView;
            headerUnbinder = ButterKnife.bind(this, itemView);

            banner.setDelayTime(1500);
            banner.setIndicatorGravity(BannerConfig.RIGHT);
            banner.isAutoPlay(true);
        }

        public void destroy() {
            headerUnbinder.unbind();
        }

        public void setOnClickListener(View.OnClickListener listener) {
            lyEtbg.setOnClickListener(listener);
            tvTuiJian.setOnClickListener(listener);
            tvMore.setOnClickListener(listener);

        }

        public void loadBanner(List<BannerData> list) {
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
            System.out.println("display " + path);
            Glide.with(context).load((String) path).into(imageView);
        }
    }
}
