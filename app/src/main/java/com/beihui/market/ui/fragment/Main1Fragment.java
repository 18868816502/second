package com.beihui.market.ui.fragment;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.beihui.market.R;
import com.beihui.market.base.BaseRVFragment;
import com.beihui.market.component.AppComponent;
import com.beihui.market.component.DaggerMainComponent;
import com.beihui.market.ui.adapter.BorrowAdapter;
import com.beihui.market.ui.adapter.GonglueAdapter;
import com.beihui.market.ui.bean.BannerData;
import com.beihui.market.ui.bean.GonglueData;
import com.beihui.market.ui.bean.support.ReturnMain2;
import com.beihui.market.ui.contract.Main1Contract;
import com.beihui.market.ui.presenter.Main1Presenter;
import com.beihui.market.util.CommonUtils;
import com.beihui.market.util.InputMethodUtil;
import com.beihui.market.view.cycleviewpager.CycleViewPager;
import com.beihui.market.view.yrecycleview.YRecycleview;
import com.bumptech.glide.Glide;
import com.gyf.barlibrary.ImmersionBar;
import com.sunfusheng.marqueeview.MarqueeView;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/1/22.
 * 新闻通知页面
 */

public class Main1Fragment extends BaseRVFragment<Main1Presenter> implements Main1Contract.View, YRecycleview.OnYRecycleScrolling,
        View.OnClickListener {


    @BindView(R.id.yrecycle_view)
    YRecycleview yrecycleView;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.center_text)
    TextView center_text;
    @BindView(R.id.top_view)
    View top_view;


    //记录顶部banner的高度
    private int bannerHeight;


    //顶部的空间
    private ImageView ivBannerOne;
    private LinearLayout ly_banner;
    private MarqueeView marqueeView;
    private LinearLayout ly_etbg;
    private EditText et_money;
    private TextView tv_time, tv_time1, tv_time2, tv_time3, tv_time4, tv_time5, tv_time6, tv_time7;
    private TextView tv_tuijian;
    private LinearLayout ly_zhengxin, ly_daikuan;
    private RecyclerView recycle_hor;
    private TextView tv_more;


    private CycleViewPager cycleViewPager;
    private List<ImageView> views = new ArrayList<>();
    private List<BannerData> infos = new ArrayList<>();


    private BorrowAdapter adapter;
    private GonglueAdapter gonglueAdapter;

    //记录选择的是什么范围的，一个月，三个月还是不限,从1 ~ 7
    private int selectTimeIndex;
    //记录输入的钱数
    private String inputMoney;


    public static Main1Fragment newInstance() {
        Main1Fragment f = new Main1Fragment();
        Bundle b = new Bundle();
        b.putString("type", "Main2Fragment");
        f.setArguments(b);
        return f;
    }

    @Override
    public int getLayoutResId() {
        return R.layout.fragment_main1;
    }

    @Override
    protected void immersionInit() {

        final int statusHeight = CommonUtils.getStatusBarHeight(getActivity());
        top_view.post(new Runnable() {
            @Override
            public void run() {
                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) top_view.getLayoutParams();
                params.height = statusHeight;
                top_view.setLayoutParams(params);
            }
        });
    }

    @Override
    public void configViews() {

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        yrecycleView.setLayoutManager(layoutManager);
        yrecycleView.setOnYRecycleScrolling(this);
        adapter = new BorrowAdapter(getActivity());
        yrecycleView.setAdapter(adapter);
        yrecycleView.setLoadMoreEnabled(false);

        LayoutInflater inflator =
                (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        LinearLayout header1 =
                (LinearLayout) inflator.inflate(R.layout.layout_main1_top, null);
        cycleViewPager = (CycleViewPager) getActivity().getFragmentManager()
                .findFragmentById(R.id.fm_banner);
        ivBannerOne = (ImageView) header1.findViewById(R.id.iv_banner_one);
        ly_banner = (LinearLayout) header1.findViewById(R.id.ly_banner);
        marqueeView = (MarqueeView) header1.findViewById(R.id.marqueeView);
        ly_etbg = (LinearLayout) header1.findViewById(R.id.ly_etbg);
        et_money = (EditText) header1.findViewById(R.id.et_money);
        tv_time1 = (TextView) header1.findViewById(R.id.tv_time1);
        tv_time2 = (TextView) header1.findViewById(R.id.tv_time2);
        tv_time3 = (TextView) header1.findViewById(R.id.tv_time3);
        tv_time4 = (TextView) header1.findViewById(R.id.tv_time4);
        tv_time5 = (TextView) header1.findViewById(R.id.tv_time5);
        tv_time6 = (TextView) header1.findViewById(R.id.tv_time6);
        tv_time7 = (TextView) header1.findViewById(R.id.tv_time7);
        tv_tuijian = (TextView) header1.findViewById(R.id.tv_tuijian);
        tv_more = (TextView) header1.findViewById(R.id.tv_more);
        ly_zhengxin = (LinearLayout) header1.findViewById(R.id.ly_zhengxin);
        ly_daikuan = (LinearLayout) header1.findViewById(R.id.ly_daikuan);
        recycle_hor = (RecyclerView) header1.findViewById(R.id.recycle_hor);


        LinearLayoutManager layoutHorManager = new LinearLayoutManager(getActivity());
        layoutHorManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recycle_hor.setLayoutManager(layoutHorManager);
        gonglueAdapter = new GonglueAdapter(getActivity());
        recycle_hor.setAdapter(gonglueAdapter);


        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) ly_banner.getLayoutParams();
        params.width = CommonUtils.getScreenMaxWidth(getActivity(), 0);
        bannerHeight = (params.width * 530) / 975;
        params.height = bannerHeight;

        ly_banner.setLayoutParams(params);

        yrecycleView.addHeadView(header1);
        yrecycleView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                InputMethodUtil.closeSoftKeyboard(getActivity());
                return false;
            }
        });

        yrecycleView.setRefreshAndLoadMoreListener(new YRecycleview.OnRefreshAndLoadMoreListener() {
            @Override
            public void onRefresh() {
                yrecycleView.setReFreshComplete();
            }

            @Override
            public void onLoadMore() {
                yrecycleView.setloadMoreComplete();
            }
        });

    }

    @Override
    public void initDatas() {
        ly_etbg.setOnClickListener(this);
        tv_time1.setOnClickListener(this);
        tv_time2.setOnClickListener(this);
        tv_time3.setOnClickListener(this);
        tv_time4.setOnClickListener(this);
        tv_time5.setOnClickListener(this);
        tv_time6.setOnClickListener(this);
        tv_time7.setOnClickListener(this);
        tv_tuijian.setOnClickListener(this);
        tv_more.setOnClickListener(this);
        ly_zhengxin.setOnClickListener(this);
        ly_daikuan.setOnClickListener(this);

        //默认选择第一项
        tv_time1.setSelected(true);
        tv_time = tv_time1;
        selectTimeIndex = 1;


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
        initBanner(list);

        List<String> info = new ArrayList<>();
        info.add("187****0421在闪贷侠成功借款1000元");
        info.add("150****7450在爱信钱包成功借款1000元");
        info.add("176****2105在点融网成功借款700元");
        info.add("150****1122在贷款王成功借款500元");
        info.add("134****0123在爱信钱包成功借款2000元");
        info.add("138****4422在闪贷侠成功借款800元");
        marqueeView.startWithList(info);

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

    }

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {
        DaggerMainComponent.builder().appComponent(appComponent).build().inject(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ly_etbg:
                InputMethodUtil.openSoftKeyboard(getActivity(), et_money);
                break;
            case R.id.tv_time1:
                onTimeMenuCheck(1);
                break;
            case R.id.tv_time2:
                onTimeMenuCheck(2);
                break;
            case R.id.tv_time3:
                onTimeMenuCheck(3);
                break;
            case R.id.tv_time4:
                onTimeMenuCheck(4);
                break;
            case R.id.tv_time5:
                onTimeMenuCheck(5);
                break;
            case R.id.tv_time6:
                onTimeMenuCheck(6);
                break;
            case R.id.tv_time7:
                onTimeMenuCheck(7);
                break;
            case R.id.tv_tuijian:
                if (TextUtils.isEmpty(et_money.getText().toString()))
                    inputMoney = "0";
                else
                    inputMoney = et_money.getText().toString();

                EventBus.getDefault().post(new ReturnMain2(selectTimeIndex,inputMoney));

                break;
            case R.id.ly_zhengxin:
                break;
            case R.id.ly_daikuan:
                break;
            case R.id.tv_more:
                break;
        }
    }


    /**
     * 点击选择时间改变状态
     *
     * @param index
     */
    private void onTimeMenuCheck(int index) {
        tv_time.setSelected(false);
        selectTimeIndex = index;
        switch (index) {
            case 1:
                tv_time = tv_time1;
                break;
            case 2:
                tv_time = tv_time2;
                break;
            case 3:
                tv_time = tv_time3;
                break;
            case 4:
                tv_time = tv_time4;
                break;
            case 5:
                tv_time = tv_time5;
                break;
            case 6:
                tv_time = tv_time6;
                break;
            case 7:
                tv_time = tv_time7;
                break;
        }
        tv_time.setSelected(true);
    }


    @Override
    public void showError(String err) {
        dismissDialog();
    }

    @Override
    public void complete() {
        dismissDialog();
    }

    public void initBanner(final List<BannerData> data) {
        infos.clear();
        views.clear();
        infos.addAll(data);
        // 将最后一个ImageView添加进来
        views.add(getImageView(getActivity(), infos.get(infos.size() - 1)));
        for (int i = 0; i < infos.size(); i++) {
            views.add(getImageView(getActivity(), infos.get(i)));
        }
        // 将第一个ImageView添加进来
        views.add(getImageView(getActivity(), infos.get(0)));
        cycleViewPager.setScrollable(true);
        // 设置循环，在调用setData方法前调用
        cycleViewPager.setCycle(true);
        // 在加载数据前设置是否循环
        cycleViewPager.setData(views, infos, mAdCycleViewListener);
        //设置轮播
        cycleViewPager.setWheel(true);

        // 设置轮播时间，默认5000ms
        cycleViewPager.setTime(2000);
        //设置圆点指示图标组居中显示，默认靠右
//        cycleViewPager.setIndicatorCenter();
    }

    public ImageView getImageView(Context context, final BannerData data) {
        ImageView imageView = (ImageView) LayoutInflater.from(context).inflate(
                R.layout.view_banner, null);
        Glide.with(getActivity()).load(data.getImgUrl()).into(imageView);
        return imageView;
    }

    private CycleViewPager.ImageCycleViewListener mAdCycleViewListener = new CycleViewPager.ImageCycleViewListener() {

        @Override
        public void onImageClick(BannerData info, int position, View imageView) {
            if (cycleViewPager.isCycle()) {
                position = position - 1;

            }
        }
    };


    @Override
    public void OnYRecycleScrolled(int moveY) {

        float alpha;
        final int textAlpha;

        int maxMove = bannerHeight / 2;
        if (moveY <= maxMove) {
            if (moveY < 10) {
                alpha = 0;
                toolbar.setVisibility(View.GONE);
                textAlpha = 0;
            } else if (moveY >= 10 && moveY < maxMove) {
                alpha = (float) moveY / maxMove;
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

        ImmersionBar.with(getActivity())
                .barColorTransform(R.color.colorPrimary)
                .navigationBarColorTransform(R.color.colorPrimary)
                .addViewSupportTransformColor(toolbar)
                .barAlpha(alpha)
                .init();


        center_text.post(new Runnable() {
            @Override
            public void run() {
                center_text.setTextColor(Color.argb(textAlpha, 255, 255, 255));   //文字透明度
            }
        });
    }
}
