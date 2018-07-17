package com.beihui.market.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.beihui.market.R;
import com.beihui.market.base.BaseTabFragment;
import com.beihui.market.helper.DataStatisticsHelper;
import com.beihui.market.injection.component.AppComponent;
import com.beihui.market.umeng.NewVersionEvents;
import com.beihui.market.util.CommonUtils;
import com.gyf.barlibrary.ImmersionBar;

import butterknife.BindView;

/**
 * https://gitee.com/tangbuzhi
 *
 * @author: Tangbuzhi
 * @version:
 * @package:
 * @description:
 * @modify:
 * @date: 2018/7/17
 */

public class HomeFragment extends BaseTabFragment {

    @BindView(R.id.tool_bar)
    Toolbar mToolBar;


    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    @Override
    public int getLayoutResId() {
        return R.layout.f_fragment_home;
    }

    /**
     * 统计点击tab事件
     */
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //pv，uv统计
        DataStatisticsHelper.getInstance().onCountUv(DataStatisticsHelper.ID_CLICK_TAB_ACCOUNT);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void configViews() {
        ImmersionBar.with(this).titleBar(mToolBar).statusBarDarkFont(false).init();
        int statusHeight = CommonUtils.getStatusBarHeight(getActivity());
        //设置toolbar的高度为状态栏相同高度
        mToolBar.setPadding(mToolBar.getPaddingLeft(), statusHeight, mToolBar.getPaddingRight(), 0);
        ViewGroup.LayoutParams lp = mToolBar.getLayoutParams();
        lp.height = 0;
        mToolBar.setLayoutParams(lp);
    }

    @Override
    public void initDatas() {

    }

    @Override
    protected void configureComponent(AppComponent appComponent) {
    }

    @Override
    protected boolean needFakeStatusBar() {
        return false;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //pv，uv统计
        DataStatisticsHelper.getInstance().onCountUv(NewVersionEvents.HP);
    }
}
