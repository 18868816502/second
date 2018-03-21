package com.beihui.market.ui.activity;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;

import com.beihui.market.R;
import com.beihui.market.base.BaseComponentActivity;
import com.beihui.market.entity.DebtChannel;
import com.beihui.market.helper.SlidePanelHelper;
import com.beihui.market.injection.component.AppComponent;
import com.beihui.market.ui.fragment.DebtNewEvenFragment;
import com.beihui.market.ui.fragment.DebtNewOneTimeFragment;
import com.beihui.market.util.AndroidBug5497Fix;
import com.beihui.market.util.viewutils.ToastUtils;
import com.beihui.market.view.copytablayout.CopyTabLayout;
import com.gyf.barlibrary.ImmersionBar;

import butterknife.BindView;

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

        debtChannel = getIntent().getParcelableExtra("debt_channel");

        viewPager.setAdapter(new DebtNewPagerAdapter(getSupportFragmentManager()));
        copyTabLayout.setupWithViewPager(viewPager);
        channelName.setText(debtChannel.getChannelName());

        SlidePanelHelper.attach(this);
        AndroidBug5497Fix.assistActivity(this);
    }

    @Override
    public void initDatas() {
    }

    @Override
    protected void configureComponent(AppComponent appComponent) {
    }

    class DebtNewPagerAdapter extends FragmentPagerAdapter {


        DebtNewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0) {
                return DebtNewOneTimeFragment.newInstance(debtChannel);
            } else {
                return DebtNewEvenFragment.newInstance(debtChannel);
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
