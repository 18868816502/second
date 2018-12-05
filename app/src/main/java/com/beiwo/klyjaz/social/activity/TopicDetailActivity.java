package com.beiwo.klyjaz.social.activity;

import android.graphics.Color;
import android.graphics.Typeface;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;

import com.beiwo.klyjaz.R;
import com.beiwo.klyjaz.base.BaseComponentActivity;
import com.beiwo.klyjaz.helper.SlidePanelHelper;
import com.gyf.barlibrary.ImmersionBar;

import butterknife.BindView;

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
    @BindView(R.id.ctl_title)
    CollapsingToolbarLayout ctl_title;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    private String topicId;

    @Override
    public int getLayoutId() {
        return R.layout.activity_topic_detail;
    }

    @Override
    public void configViews() {
        setupToolbar(toolbar, true);
        setupToolbarBackNavigation(toolbar, R.drawable.back_white);
        ImmersionBar.with(this).statusBarDarkFont(false).init();
        SlidePanelHelper.attach(this);
        topicId = getIntent().getStringExtra("topicId");

        ctl_title.setTitle("手机安全卫士");
        ctl_title.setCollapsedTitleTypeface(Typeface.SANS_SERIF);
        ctl_title.setExpandedTitleColor(Color.WHITE);
        ctl_title.setCollapsedTitleTextColor(Color.WHITE);
    }

    @Override
    public void initDatas() {

    }
}