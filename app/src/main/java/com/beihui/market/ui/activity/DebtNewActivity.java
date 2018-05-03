package com.beihui.market.ui.activity;


import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.beihui.market.R;
import com.beihui.market.base.BaseComponentActivity;
import com.beihui.market.entity.DebtChannel;
import com.beihui.market.entity.DebtDetail;
import com.beihui.market.helper.DataStatisticsHelper;
import com.beihui.market.helper.SlidePanelHelper;
import com.beihui.market.injection.component.AppComponent;
import com.beihui.market.ui.contract.DebtNewContract;
import com.beihui.market.ui.fragment.DebtNewEvenFragment;
import com.beihui.market.ui.fragment.DebtNewOneTimeFragment;
import com.beihui.market.util.AndroidBug5497Fix;
import com.beihui.market.util.InputMethodUtil;
import com.beihui.market.view.PagerSlidingTab;
import com.beihui.market.view.copytablayout.CopyTabLayout;
import com.gyf.barlibrary.ImmersionBar;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author xhb
 * 网贷账单 详情编辑页面
 * 一次性还款
 * 分期还款
 */
public class DebtNewActivity extends BaseComponentActivity {

    @BindView(R.id.tool_bar)
    Toolbar toolbar;
    @BindView(R.id.channel_name)
    TextView channelName;
    @BindView(R.id.copy_tab_layout)
    PagerSlidingTab copyTabLayout;
    @BindView(R.id.view_pager)
    ViewPager viewPager;

    /**
     * 该字段不为空，则为新增账单模式
     */
    private DebtChannel debtChannel;
    /**
     * 该字段不为空，则为编辑账单模式
     */
    private DebtDetail debtDetail;

    //Fragment的标题
    private final String[] pageTitles = {"一次性还款", "分期还款"};

    /**
     * @return 返回布局
     */
    @Override
    public int getLayoutId() {
        return R.layout.activity_debt_new;
    }

    /**
     * 失去焦点 关闭软键盘
     */
    @Override
    protected void onPause() {
        InputMethodUtil.closeSoftKeyboard(this);
        super.onPause();
    }



    /**
     * ViewPager的适配器
     */
    class DebtNewPagerAdapter extends FragmentPagerAdapter {

        DebtNewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        /**
         * 切换Fragment类型
         */
        @Override
        public Fragment getItem(int position) {
            if (position == 0) {
                DebtDetail pending = null;
                if (debtDetail != null && debtDetail.getRepayType() == DebtNewContract.Presenter.METHOD_ONE_TIME) {
                    pending = debtDetail;
                }
                return DebtNewOneTimeFragment.newInstance(debtChannel, pending);
            } else {
                DebtDetail pending = null;
                if (debtDetail != null && debtDetail.getRepayType() == DebtNewContract.Presenter.METHOD_EVEN_DEBT) {
                    pending = debtDetail;
                }
                return DebtNewEvenFragment.newInstance(debtChannel, pending);
            }
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return pageTitles[position];
        }
    }

    /**
     * 空事件
     */
    @Override
    public void initDatas() {
        copyTabLayout.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 1) {
                    /**
                     * 埋点 	网贷记账自定义
                     */
                    //pv，uv统计
                    DataStatisticsHelper.getInstance().onCountUv(DataStatisticsHelper.ID_BILL_NET_LOAN_TAB_BY_STAGES);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    /**
     * 空事件
     */
    @Override
    protected void configureComponent(AppComponent appComponent) {}



    @Override
    public void configViews() {
        ImmersionBar.with(this).titleBar(toolbar).statusBarDarkFont(true).init();
        setupToolbarBackNavigation(toolbar, R.mipmap.left_arrow_black);

        viewPager.setAdapter(new DebtNewPagerAdapter(getSupportFragmentManager()));
        copyTabLayout.setViewPager(viewPager);
        debtChannel = getIntent().getParcelableExtra("debt_channel");
        if (debtChannel != null) {
            channelName.setText(debtChannel.getChannelName());
        } else {
            debtDetail = getIntent().getParcelableExtra("debt_detail");
            channelName.setText(debtDetail.getChannelName());

            debtChannel = new DebtChannel();
            debtChannel.setType("whatever");
            debtChannel.setChannelName(debtDetail.getChannelName());
            debtChannel.setId(debtDetail.getChannelId());
        }

        if (debtDetail != null) {
            viewPager.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (debtDetail.getRepayType() == DebtNewContract.Presenter.METHOD_EVEN_DEBT) {
                        viewPager.setCurrentItem(1);
                    } else {
                        viewPager.setCurrentItem(0);
                    }
                }
            }, 100);

        }

        SlidePanelHelper.attach(this);
        AndroidBug5497Fix.assistActivity(this);
    }

}
