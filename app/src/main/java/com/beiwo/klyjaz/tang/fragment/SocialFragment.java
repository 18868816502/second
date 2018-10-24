package com.beiwo.klyjaz.tang.fragment;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.beiwo.klyjaz.R;
import com.beiwo.klyjaz.base.BaseComponentFragment;
import com.beiwo.klyjaz.injection.component.AppComponent;
import com.beiwo.klyjaz.tang.adapter.SocialAdapter;
import com.beiwo.klyjaz.ui.activity.CommunityPublishActivity;
import com.beiwo.klyjaz.ui.fragment.SocialAttentionFragment;
import com.beiwo.klyjaz.ui.fragment.SocialRecommendFragment;
import com.beiwo.klyjaz.util.CommonUtils;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.ColorTransitionPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.SimplePagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.badge.BadgeAnchor;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.badge.BadgePagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.badge.BadgeRule;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;


/**
 * https://gitee.com/tangbuzhi
 *
 * @author: Tangbuzhi
 * @version:
 * @package:
 * @description:
 * @modify:
 * @date: 2018/9/11
 */

public class SocialFragment extends BaseComponentFragment {
    @BindView(R.id.root)
    RelativeLayout root;
    @BindView(R.id.hold_view)
    View hold_view;
    @BindView(R.id.indicator)
    MagicIndicator indicator;
    @BindView(R.id.viewpager)
    ViewPager viewpager;
    @BindView(R.id.iv_publish)
    ImageView ivPublish;

    private String[] mDataList = {"关注", "推荐"};

    public static SocialFragment newInstance() {
        return new SocialFragment();
    }

    @Override
    public int getLayoutResId() {
        return R.layout.f_layout_social;
    }

    @Override
    public void configViews() {
        List<Fragment> fragments = new ArrayList<>();
        Fragment attFragment = SocialAttentionFragment.getInstance();
        Fragment recFragment = SocialRecommendFragment.getInstance();
        fragments.add(attFragment);
        fragments.add(recFragment);
        SocialAdapter adapter = new SocialAdapter(getActivity().getSupportFragmentManager());
        adapter.setDatas(fragments);
        int statusHeight = CommonUtils.getStatusBarHeight(getActivity());
        ViewGroup.LayoutParams params = hold_view.getLayoutParams();
        params.height = statusHeight;
        hold_view.setLayoutParams(params);
        initIndicator();

        viewpager.setAdapter(adapter);
        viewpager.setCurrentItem(1);
    }

    @Override
    public void initDatas() {

    }

    @Override
    protected void configureComponent(AppComponent appComponent) {
    }

    private void initIndicator() {
        CommonNavigator commonNavigator = new CommonNavigator(getActivity());
        commonNavigator.setAdapter(new CommonNavigatorAdapter() {
            @Override
            public int getCount() {
                return mDataList.length;
            }

            @Override
            public IPagerTitleView getTitleView(Context context, final int index) {
                final BadgePagerTitleView badgePagerTitleView = new BadgePagerTitleView(context);
                SimplePagerTitleView simplePagerTitleView = new ColorTransitionPagerTitleView(context);
                simplePagerTitleView.setNormalColor(ContextCompat.getColor(context, R.color.black_2));
                simplePagerTitleView.setSelectedColor(ContextCompat.getColor(context, R.color.refresh_one));
                simplePagerTitleView.setText(mDataList[index]);
                simplePagerTitleView.setTextSize(16);
                simplePagerTitleView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        viewpager.setCurrentItem(index);
                        badgePagerTitleView.setBadgeView(null);
                    }
                });
                badgePagerTitleView.setInnerPagerTitleView(simplePagerTitleView);

                View badgeImageView = LayoutInflater.from(context).inflate(R.layout.layout_red_dot, root, false);
                badgePagerTitleView.setBadgeView(badgeImageView);

                badgePagerTitleView.setXBadgeRule(new BadgeRule(BadgeAnchor.CONTENT_RIGHT, -6));
                badgePagerTitleView.setYBadgeRule(new BadgeRule(BadgeAnchor.CONTENT_TOP, -2));

                badgePagerTitleView.setAutoCancelBadge(true);
                return badgePagerTitleView;
            }

            @Override
            public IPagerIndicator getIndicator(Context context) {
                LinePagerIndicator linePagerIndicator = new LinePagerIndicator(context);
                linePagerIndicator.setMode(LinePagerIndicator.MODE_WRAP_CONTENT);
                linePagerIndicator.setColors(ContextCompat.getColor(context, R.color.refresh_one));
                return linePagerIndicator;
            }
        });
        indicator.setNavigator(commonNavigator);
        ViewPagerHelper.bind(indicator, viewpager);
    }

    @OnClick(R.id.iv_publish)
    public void onViewClick(View view) {
        switch (view.getId()) {
            case R.id.iv_publish:
                startActivity(new Intent(getActivity(), CommunityPublishActivity.class));
                break;
            default:
                break;
        }
    }
}