package com.beiwo.klyjaz.social.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.beiwo.klyjaz.R;
import com.beiwo.klyjaz.api.Api;
import com.beiwo.klyjaz.base.BaseComponentActivity;
import com.beiwo.klyjaz.helper.SlidePanelHelper;
import com.beiwo.klyjaz.helper.UserHelper;
import com.beiwo.klyjaz.social.bean.TopicDetail;
import com.beiwo.klyjaz.social.fragment.TopicFragment;
import com.beiwo.klyjaz.tang.DlgUtil;
import com.beiwo.klyjaz.tang.adapter.SocialAdapter;
import com.beiwo.klyjaz.tang.rx.RxResponse;
import com.beiwo.klyjaz.tang.rx.observer.ApiObserver;
import com.beiwo.klyjaz.tang.widget.ExpandableTextView;
import com.beiwo.klyjaz.util.DensityUtil;
import com.bumptech.glide.Glide;
import com.gyf.barlibrary.ImmersionBar;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.ColorTransitionPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.SimplePagerTitleView;

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
 * @description: 话题详情
 * @modify:
 * @date: 2018/12/5
 */
public class TopicDetailActivity extends BaseComponentActivity {
    @BindView(R.id.refresh_layout)
    SmartRefreshLayout refresh_layout;
    @BindView(R.id.ctl_title)
    CollapsingToolbarLayout ctl_title;
    @BindView(R.id.iv_topic_head)
    ImageView iv_topic_head;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.nsv_root)
    ViewGroup nsv_root;
    @BindView(R.id.indicator)
    MagicIndicator indicator;
    @BindView(R.id.viewpager)
    ViewPager viewpager;
    @BindView(R.id.tv_join_num)
    TextView tv_join_num;
    @BindView(R.id.expand_tv)
    ExpandableTextView expand_tv;
    @BindView(R.id.tv_involve_topic)
    TextView tvInvolveTopic;

    private String topicId;
    private Activity context;
    private String topicTitle;
    private String[] mDataList = {"最热", "最新"};
    private List<TopicFragment> fragments = new ArrayList<>();

    @Override
    public int getLayoutId() {
        return R.layout.activity_topic_detail;
    }

    @Override
    public void configViews() {
        context = this;
        setupToolbar(toolbar, true);
        setupToolbarBackNavigation(toolbar, R.drawable.back_white);
        ImmersionBar.with(this).statusBarDarkFont(false).init();
        SlidePanelHelper.attach(this);
        topicId = getIntent().getStringExtra("topicId");
        ctl_title.setExpandedTitleColor(Color.WHITE);
        ctl_title.setCollapsedTitleTextColor(Color.WHITE);
        request();
        initAdapter();
        initIndicator();
    }

    private void request() {
        Api.getInstance().topicDetail(topicId)
                .compose(RxResponse.<TopicDetail>compatT())
                .subscribe(new ApiObserver<TopicDetail>() {
                    @Override
                    public void onNext(TopicDetail data) {
                        topicTitle = data.getTitle();
                        ctl_title.setTitle(data.getTitle());
                        ctl_title.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                ctl_title.setExpandedTitleMarginBottom(expand_tv.getMeasuredHeight() + tv_join_num.getMeasuredHeight() + DensityUtil.dp2px(context, 100f));
                            }
                        }, 150);
                        Glide.with(context).load(data.getImgUrl()).into(iv_topic_head);

                        int count = data.getTopicFourmCount();
                        SpannableString ss = new SpannableString(String.format(getString(R.string.join_topic_num), count));
                        ss.setSpan(new ForegroundColorSpan(ContextCompat.getColor(context, R.color.refresh_one)), 2, String.valueOf(count).length() + 3, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                        tv_join_num.setText(ss);
                        expand_tv.initWidth(getWindowManager().getDefaultDisplay().getWidth());
                        expand_tv.setMaxLines(4);
                        expand_tv.setCloseText(data.getContent());
                    }
                });
    }

    @OnClick({R.id.tv_involve_topic})
    public void onViewClick(View view) {
        switch (view.getId()) {
            case R.id.tv_involve_topic:
                if (!UserHelper.getInstance(this).isLogin()) {
                    DlgUtil.loginDlg(this, null);
                    return;
                }
                Intent intent = new Intent(this, ForumPublishActivity.class);
                intent.putExtra("topicId", topicId);
                intent.putExtra("title", topicTitle);
                startActivity(intent);
                break;
            default:
                break;
        }
    }

    @Override
    public void initDatas() {
        LocalBroadcastManager.getInstance(this).registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(final Context context, Intent intent) {
                ctl_title.post(new Runnable() {
                    @Override
                    public void run() {
                        ctl_title.setExpandedTitleMarginBottom(expand_tv.getMeasuredHeight() + tv_join_num.getMeasuredHeight() + DensityUtil.dp2px(context, 100f));
                    }
                });
            }
        }, new IntentFilter("request_layout"));
        refresh_layout.setEnableLoadMore(false);
        refresh_layout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                refresh_layout.finishRefresh();
                request();
                fragments.get(viewpager.getCurrentItem()).refresh();
            }
        });
    }

    private void initAdapter() {
        TopicFragment fragment1 = TopicFragment.getInstance();
        Bundle args1 = new Bundle();
        args1.putInt("type", 2);
        args1.putString("topicId", topicId);
        fragment1.setArguments(args1);

        TopicFragment fragment2 = TopicFragment.getInstance();
        Bundle args2 = new Bundle();
        args2.putInt("type", 1);
        args2.putString("topicId", topicId);
        fragment2.setArguments(args2);

        fragments.clear();
        fragments.add(fragment1);
        fragments.add(fragment2);

        SocialAdapter adapter = new SocialAdapter(getSupportFragmentManager());
        adapter.setDatas(fragments);
        viewpager.setAdapter(adapter);
        viewpager.setCurrentItem(0);
    }

    private void initIndicator() {
        CommonNavigator commonNavigator = new CommonNavigator(context);
        commonNavigator.setAdapter(new CommonNavigatorAdapter() {
            @Override
            public int getCount() {
                return mDataList.length;
            }

            @Override
            public IPagerTitleView getTitleView(Context context, final int index) {
                SimplePagerTitleView simplePagerTitleView = new ColorTransitionPagerTitleView(context);
                simplePagerTitleView.setNormalColor(ContextCompat.getColor(context, R.color.black_1));
                simplePagerTitleView.setSelectedColor(ContextCompat.getColor(context, R.color.refresh_one));
                simplePagerTitleView.setText(mDataList[index]);
                simplePagerTitleView.setTextSize(16);
                simplePagerTitleView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        viewpager.setCurrentItem(index);
                    }
                });
                return simplePagerTitleView;
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
        LinearLayout titleContainer = commonNavigator.getTitleContainer();
        titleContainer.setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE);
        titleContainer.setDividerDrawable(new ColorDrawable() {
            @Override
            public int getIntrinsicWidth() {
                return DensityUtil.dp2px(context, 55);
            }
        });
        ViewPagerHelper.bind(indicator, viewpager);
    }
}