package com.beihui.market.ui.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.DownloadListener;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.beihui.market.App;
import com.beihui.market.BuildConfig;
import com.beihui.market.R;
import com.beihui.market.api.Api;
import com.beihui.market.api.NetConstants;
import com.beihui.market.api.ResultEntity;
import com.beihui.market.base.BaseComponentFragment;
import com.beihui.market.base.BaseTabFragment;
import com.beihui.market.event.TabNewsWebViewFragmentTitleEvent;
import com.beihui.market.event.TabNewsWebViewFragmentUrlEvent;
import com.beihui.market.helper.DataStatisticsHelper;
import com.beihui.market.helper.UserHelper;
import com.beihui.market.injection.component.AppComponent;
import com.beihui.market.ui.activity.AccountFlowActivity;
import com.beihui.market.ui.activity.MainActivity;
import com.beihui.market.ui.activity.TabMineActivity;
import com.beihui.market.ui.activity.UserAuthorizationActivity;
import com.beihui.market.ui.busevents.UserLoginEvent;
import com.beihui.market.ui.busevents.UserLogoutEvent;
import com.beihui.market.ui.presenter.TabMinePresenter;
import com.beihui.market.umeng.Events;
import com.beihui.market.umeng.NewVersionEvents;
import com.beihui.market.umeng.Statistic;
import com.beihui.market.util.RxUtil;
import com.beihui.market.view.BusinessWebView;
import com.beihui.market.view.CircleImageView;
import com.beihui.market.view.GlideCircleTransform;
import com.beihui.market.view.NoScrollViewPager;
import com.bumptech.glide.Glide;
import com.gyf.barlibrary.ImmersionBar;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.xiaoneng.uiapi.Ntalker;
import io.reactivex.functions.Consumer;

/**
 * @date 20180419
 * @version 2.1.1
 * @author xhb
 * 发现 模块 使用WebView页面 (非资讯详情页)
 */
public class TabNewsWebViewFragment extends BaseTabFragment{

    @BindView(R.id.tl_news_header_tool_bar)
    Toolbar toolbar;
    @BindView(R.id.iv_tab_fg_news_web_back)
    ImageView comeBack;
    @BindView(R.id.iv_tab_fg_news_web_title)
    TextView newsTitleName;
    @BindView(R.id.iv_tab_fg_news_web_activity)
    TextView activityName;
    @BindView(R.id.iv_tab_fg_news_web_user)
    ImageView mUserAvatar;
    @BindView(R.id.iv_tab_news_red_dot)
    ImageView mRedDot;
    @BindView(R.id.fl_tab_news_web_container)
    public NoScrollViewPager viewPager;

    /**
     * 拼接URL
     */
    public static String newsUrl = null;

    //依赖的activity
    public FragmentActivity mActivity;
    //发现
    public TabNewsWebViewOneFragment mFindFragment = new TabNewsWebViewOneFragment();
    //活动
    public TabNewsWebViewTwoFragment mActivityFragment = new TabNewsWebViewTwoFragment();

    public List<BaseComponentFragment> fragmentList = new ArrayList<>();

    private int selectedFragmentId = R.id.iv_tab_fg_news_web_title;


    @Subscribe
    public void onLogin(UserLoginEvent event) {
        if (UserHelper.getInstance(mActivity).getProfile() != null && UserHelper.getInstance(mActivity).getProfile().getId() != null) {
            updateNum();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMainEvent(UserLogoutEvent event){
        Glide.with(mActivity).load(R.mipmap.mine_head).into(mUserAvatar);
        mRedDot.setVisibility(View.GONE);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    /**
     * 标题
     */
    public String mTitleName;
    private TabNewsWebViewFragmentTitleEvent event = null;

    @Override
    public void onStart() {
        super.onStart();
        if (event != null && !TextUtils.isEmpty(event.title) && newsTitleName != null) {
            newsTitleName.setText(event.title);
            mTitleName = event.title;
        }

        //pv，uv统计
//        DataStatisticsHelper.getInstance().onCountUv(NewVersionEvents.DISCOVER);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (UserHelper.getInstance(mActivity).getProfile() != null && UserHelper.getInstance(mActivity).getProfile().getId() != null) {
            Glide.with(mActivity).load(UserHelper.getInstance(mActivity).getProfile().getHeadPortrait()).bitmapTransform(new GlideCircleTransform(mActivity)).placeholder(R.mipmap.mine_head).into(mUserAvatar);
            updateNum();
        }
    }

    public static TabNewsWebViewFragment newInstance() {
        return new TabNewsWebViewFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mActivity = getActivity();
        //pv，uv统计
        DataStatisticsHelper.getInstance().onCountUv(DataStatisticsHelper.ID_CLICK_TAB_NEWS);
        //umeng统计
        Statistic.onEvent(Events.ENTER_NEWS_PAGE);

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //设置状态栏文字为黑色字体
        if (TextUtils.isEmpty(mTitleName)) {
            mTitleName = getActivity().getResources().getString(R.string.tab_news);
        }
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }


    @Override
    public int getLayoutResId() {
        return R.layout.fragment_tab_news_web_view;
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public void configViews() {
        comeBack.setVisibility(View.GONE);
        ImmersionBar.with(this).statusBarDarkFont(true).init();

        fragmentList.add(mFindFragment);
        fragmentList.add(mActivityFragment);

        MyFragmentViewPgaerAdapter adapter = new MyFragmentViewPgaerAdapter(mActivity.getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(0);

        newsTitleName.setSelected(true);


        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                if (position == 0) {
                    newsTitleName.setSelected(true);
                    activityName.setSelected(false);

                    selectedFragmentId = R.id.iv_tab_fg_news_web_title;
                }
                if (position == 1) {
                    newsTitleName.setSelected(false);
                    activityName.setSelected(true);

                    selectedFragmentId = R.id.iv_tab_fg_news_web_activity;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                //state ==1的时辰默示正在滑动，


            }
        });

        viewPager.setNoScroll(true);
    }




    /**
     * 更新消息数量
     */
    private void updateNum() {
        Api.getInstance().queryMessage(UserHelper.getInstance(mActivity).getProfile().getId())
                .compose(RxUtil.<ResultEntity<String>>io2main())
                .subscribe(new Consumer<ResultEntity<String>>() {
                               @Override
                               public void accept(ResultEntity<String> result) throws Exception {
                                   if (result.isSuccess()) {
                                        String data = result.getData();
                                       if (TextUtils.isEmpty(data)) {
                                           mRedDot.setVisibility(View.GONE);
                                       } else if (Integer.parseInt(data) > 0) {
                                           mRedDot.setVisibility(View.VISIBLE);
                                       } else if (Integer.parseInt(data) <= 0) {
                                           mRedDot.setVisibility(View.GONE);
                                       } else {
                                           mRedDot.setVisibility(View.GONE);
                                       }
                                   }
                               }
                           },
                        new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {

                            }
                        });
    }

    /**
     * 点击事件
     */
    @OnClick({R.id.iv_tab_fg_news_web_user, R.id.iv_tab_fg_news_web_title, R.id.iv_tab_fg_news_web_activity})
    public void onItemClick(View view) {
        switch (view.getId()) {
            case R.id.iv_tab_fg_news_web_user:
                //pv，uv统计
                DataStatisticsHelper.getInstance().onCountUv(NewVersionEvents.DISCOVERHEADPORTRAIT);
                mActivity.startActivity(new Intent(mActivity, TabMineActivity.class));
                break;
            case R.id.iv_tab_fg_news_web_title:
                //pv，uv统计
//                DataStatisticsHelper.getInstance().onCountUv(NewVersionEvents.DISCOVERTAB1);
                if (selectedFragmentId != R.id.iv_tab_fg_news_web_title) {
                    viewPager.setCurrentItem(0);
                }
                break;
            case R.id.iv_tab_fg_news_web_activity:
                //pv，uv统计
//                DataStatisticsHelper.getInstance().onCountUv(NewVersionEvents.DISCOVERTAB2);
                if (selectedFragmentId != R.id.iv_tab_fg_news_web_activity) {
                    viewPager.setCurrentItem(1);
                }
                break;
        }
    }

    @Override
    public void initDatas() {}

    @Override
    protected void configureComponent(AppComponent appComponent) {

    }

    class MyFragmentViewPgaerAdapter extends FragmentPagerAdapter {

        public MyFragmentViewPgaerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }
    }
}