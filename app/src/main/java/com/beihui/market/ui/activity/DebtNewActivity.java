package com.beihui.market.ui.activity;


import android.content.Intent;
import android.os.Bundle;
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
import com.beihui.market.helper.SlidePanelHelper;
import com.beihui.market.injection.component.AppComponent;
import com.beihui.market.ui.contract.DebtNewContract;
import com.beihui.market.ui.fragment.DebtNewEvenFragment;
import com.beihui.market.ui.fragment.DebtNewOneTimeFragment;
import com.beihui.market.util.AndroidBug5497Fix;
import com.beihui.market.view.copytablayout.CopyTabLayout;
import com.gyf.barlibrary.ImmersionBar;

import butterknife.BindView;
import butterknife.OnClick;

public class DebtNewActivity extends BaseComponentActivity {

    private final String[] pageTitles = {"一次性还款", "分期还款"};

    @BindView(R.id.tool_bar)
    Toolbar toolbar;
    @BindView(R.id.channel_name)
    TextView channelName;
    @BindView(R.id.copy_tab_layout)
    CopyTabLayout copyTabLayout;
    @BindView(R.id.view_pager)
    ViewPager viewPager;

    private DebtChannel debtChannel;
    /**
     * 该字段不为空，则为编辑账单模式
     */
    private DebtDetail debtDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_debt_new;
    }

    @Override
    public void configViews() {
        ImmersionBar.with(this).titleBar(toolbar).statusBarDarkFont(true).init();
        setupToolbarBackNavigation(toolbar, R.mipmap.left_arrow_black);

        viewPager.setAdapter(new DebtNewPagerAdapter(getSupportFragmentManager()));
        copyTabLayout.setupWithViewPager(viewPager);
        debtChannel = getIntent().getParcelableExtra("debt_channel");
        if (debtChannel != null) {
            channelName.setText(debtChannel.getChannelName());
        } else {
            debtDetail = getIntent().getParcelableExtra("debt_detail");
            channelName.setText(debtDetail.getChannelName());

            debtChannel = new DebtChannel();
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

    @Override
    public void initDatas() {
    }

    @Override
    protected void configureComponent(AppComponent appComponent) {
    }

    @OnClick(R.id.help_feedback)
    void onItemClicked() {
        startActivity(new Intent(this, DebtHelpAndFeedActivity.class));
    }

    class DebtNewPagerAdapter extends FragmentPagerAdapter {


        DebtNewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

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
}
