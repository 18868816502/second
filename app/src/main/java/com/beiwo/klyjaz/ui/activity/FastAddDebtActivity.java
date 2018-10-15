package com.beiwo.klyjaz.ui.activity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.beiwo.klyjaz.R;
import com.beiwo.klyjaz.base.BaseComponentActivity;
import com.beiwo.klyjaz.entity.FastDebtDetail;
import com.beiwo.klyjaz.helper.SlidePanelHelper;
import com.beiwo.klyjaz.injection.component.AppComponent;
import com.beiwo.klyjaz.ui.fragment.FastAddDebtNewEvenFragment;
import com.beiwo.klyjaz.ui.fragment.FastAddDebtOneTimeFragment;
import com.beiwo.klyjaz.util.AndroidBug5497Fix;
import com.beiwo.klyjaz.view.PagerSlidingTab;
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

    /**
     * 该字段不为空，则为编辑账单模式
     */
    private FastDebtDetail fastDebtDetail;

    //Fragment的标题
    private final String[] pageTitles = {"一次性还款", "分期还款"};

    /**
     * 布局
     */
    @Override
    public int getLayoutId() {
        return R.layout.activity_debt_new;
    }

    @Override
    public void configViews() {
        ImmersionBar.with(this).titleBar(toolbar).statusBarDarkFont(true).init();
        setupToolbarBackNavigation(toolbar, R.mipmap.btn_back_normal_black);

        viewPager.setAdapter(new DebtNewPagerAdapter(getSupportFragmentManager()));
        copyTabLayout.setViewPager(viewPager);


        SlidePanelHelper.attach(this);
        AndroidBug5497Fix.assistActivity(this);
    }

    @Override
    public void initDatas() {
        channelName.setText("快捷记账");
        fastDebtDetail =  getIntent().getParcelableExtra("fast_debt_detail");
        if (fastDebtDetail == null) {
            //新增账单
            mTabRoot.setVisibility(View.VISIBLE);
        } else {
            //编辑账单
            //隐藏tab
            mTabRoot.setVisibility(View.GONE);
            if (fastDebtDetail.getRepayType() == 1) {
                viewPager.setCurrentItem(0);
            } else {
                viewPager.setCurrentItem(1);
            }
        }
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
                if (fastDebtDetail != null && fastDebtDetail.getRepayType() == 1) {
                    return FastAddDebtOneTimeFragment.newInstance(fastDebtDetail);
                } else {
                    return FastAddDebtOneTimeFragment.newInstance(null);
                }
            } else {
                if (fastDebtDetail != null && fastDebtDetail.getRepayType() == 2) {
                    return FastAddDebtNewEvenFragment.newInstance(fastDebtDetail);
                } else {
                    return FastAddDebtNewEvenFragment.newInstance(null);
                }
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
