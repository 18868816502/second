package com.beiwo.klyjaz.ui.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.beiwo.klyjaz.R;
import com.beiwo.klyjaz.api.NetConstants;
import com.beiwo.klyjaz.base.BaseComponentFragment;
import com.beiwo.klyjaz.base.BaseTabFragment;
import com.beiwo.klyjaz.helper.DataStatisticsHelper;
import com.beiwo.klyjaz.injection.component.AppComponent;
import com.beiwo.klyjaz.umeng.Events;
import com.beiwo.klyjaz.umeng.Statistic;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author xhb
 *         发现 模块 使用WebView页面 (非资讯详情页)
 * @version 2.1.1
 * @date 20180419
 */
public class DiscoverFragment extends BaseTabFragment {

    @BindView(R.id.tl_news_header_tool_bar)
    Toolbar toolbar;
    @BindView(R.id.fl_container)
    ViewPager viewPager;

    public TextView mTvTitleName;
    public LinearLayout mTabRoot;
    public TextView newsTitleName;
    public TextView activityName;

    //依赖的activity
    public FragmentActivity mActivity;
    //发现
    public TabOneFragment mFindFragment = new TabOneFragment();
    //活动
    public TabTwoFragment mActivityFragment = new TabTwoFragment();

    public List<BaseComponentFragment> fragmentList = new ArrayList<>();
    private int selectedFragmentId = R.id.tv_tab_one_title;

    public String mTitleName;

    public static DiscoverFragment newInstance() {
        return new DiscoverFragment();
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
    }

    @Override
    public int getLayoutResId() {
        return R.layout.fragment_discover;
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public void configViews() {
        mTvTitleName = getActivity().findViewById(R.id.tv_tab_fg_web_title);
        mTabRoot = getActivity().findViewById(R.id.ll_tab_fg_web_root);
        newsTitleName = getActivity().findViewById(R.id.tv_tab_one_title);
        activityName = getActivity().findViewById(R.id.tv_tab_two_title);

        //贷超
        if (TextUtils.equals(NetConstants.H5_FIND_WEVVIEW_DETAIL, NetConstants.H5_FIND_WEVVIEW_DETAIL_COPY)) {
            mTabRoot.setVisibility(View.VISIBLE);
            mTvTitleName.setVisibility(View.GONE);

            fragmentList.clear();
            fragmentList.add(mFindFragment);
            fragmentList.add(mActivityFragment);
        } else {//资讯
            mTabRoot.setVisibility(View.GONE);
            mTvTitleName.setVisibility(View.VISIBLE);

            fragmentList.clear();
            fragmentList.add(mFindFragment);
        }

        PgaerAdapter adapter = new PgaerAdapter(mActivity.getSupportFragmentManager());
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
                    selectedFragmentId = R.id.tv_tab_one_title;
                }
                if (position == 1) {
                    newsTitleName.setSelected(false);
                    activityName.setSelected(true);
                    selectedFragmentId = R.id.tv_tab_two_title;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                //state ==1的时辰默示正在滑动，
            }
        });
    }

    @OnClick({R.id.tv_tab_one_title, R.id.tv_tab_two_title})
    public void onItemClick(View view) {
        switch (view.getId()) {
            case R.id.tv_tab_one_title:
                if (selectedFragmentId != R.id.tv_tab_one_title) {
                    viewPager.setCurrentItem(0);
                }
                break;
            case R.id.tv_tab_two_title:
                if (selectedFragmentId != R.id.tv_tab_two_title) {
                    viewPager.setCurrentItem(1);
                }
                break;
        }
    }

    @Override
    public void initDatas() {
    }

    @Override
    protected void configureComponent(AppComponent appComponent) {
    }

    private class PgaerAdapter extends FragmentPagerAdapter {
        public PgaerAdapter(FragmentManager fm) {
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