package com.beihui.market.ui.activity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.beihui.market.R;
import com.beihui.market.base.BaseComponentActivity;
import com.beihui.market.entity.DebtDetail;
import com.beihui.market.helper.SlidePanelHelper;
import com.beihui.market.injection.component.AppComponent;
import com.beihui.market.ui.contract.DebtNewContract;
import com.beihui.market.ui.fragment.DebtNewEvenFragment;
import com.beihui.market.ui.fragment.DebtNewOneTimeFragment;
import com.beihui.market.ui.fragment.FastAddDebtNewEvenFragment;
import com.beihui.market.ui.fragment.FastAddDebtOneTimeFragment;
import com.beihui.market.util.AndroidBug5497Fix;
import com.beihui.market.util.InputMethodUtil;
import com.beihui.market.view.PagerSlidingTab;
import com.gyf.barlibrary.ImmersionBar;

import butterknife.BindView;

/**
 * Created by xhb on 2018/5/10.
 * @version 3.0.1
 * 快速记账的新增 以及 编辑页面
 */

public class FastAddDebtActivity extends BaseComponentActivity {

    @BindView(R.id.tool_bar)
    Toolbar toolbar;
    @BindView(R.id.channel_name)
    TextView channelName;
    @BindView(R.id.copy_tab_layout_root)
    LinearLayout mTabRoot;
    @BindView(R.id.copy_tab_layout)
    PagerSlidingTab copyTabLayout;
    @BindView(R.id.view_pager)
    ViewPager viewPager;


    //Fragment的标题
    private final String[] pageTitles = {"一次性还款", "分期还款"};

    /**
     * 布局
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

    @Override
    public void configViews() {
        ImmersionBar.with(this).titleBar(toolbar).statusBarDarkFont(true).init();
        setupToolbarBackNavigation(toolbar, R.mipmap.left_arrow_black);

        viewPager.setAdapter(new DebtNewPagerAdapter(getSupportFragmentManager()));
        copyTabLayout.setViewPager(viewPager);


        SlidePanelHelper.attach(this);
        AndroidBug5497Fix.assistActivity(this);
    }

    @Override
    public void initDatas() {

    }

    @Override
    protected void configureComponent(AppComponent appComponent) {

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
                return FastAddDebtOneTimeFragment.newInstance();
            } else {
                return FastAddDebtNewEvenFragment.newInstance();
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
