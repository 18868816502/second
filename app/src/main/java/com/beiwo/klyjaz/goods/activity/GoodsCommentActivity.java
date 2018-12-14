package com.beiwo.klyjaz.goods.activity;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.beiwo.klyjaz.R;
import com.beiwo.klyjaz.api.Api;
import com.beiwo.klyjaz.base.BaseComponentActivity;
import com.beiwo.klyjaz.entity.CommentsTotal;
import com.beiwo.klyjaz.entity.Labels;
import com.beiwo.klyjaz.goods.fragment.GoodsCommentFragment;
import com.beiwo.klyjaz.helper.SlidePanelHelper;
import com.beiwo.klyjaz.tang.adapter.FragmentAdapter;
import com.beiwo.klyjaz.tang.rx.RxResponse;
import com.beiwo.klyjaz.tang.rx.observer.ApiObserver;
import com.beiwo.klyjaz.util.DensityUtil;
import com.gyf.barlibrary.ImmersionBar;

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

/**
 * https://gitee.com/tangbuzhi
 *
 * @author: Tangbuzhi
 * @version:
 * @package:
 * @description: 口子评价列表
 * @modify:
 * @date: 2018/12/11
 */
public class GoodsCommentActivity extends BaseComponentActivity {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.toolbar_title)
    TextView toolbar_title;
    @BindView(R.id.indicator)
    MagicIndicator indicator;
    @BindView(R.id.viewpager)
    ViewPager viewpager;

    private String cutId, manageId;
    private String name;
    private String tag;
    private int type;
    private Activity context;
    private List<SpannableString> tabs = new ArrayList<>();
    private List<GoodsCommentFragment> fragments = new ArrayList<>();
    public List<Labels> labels = null;

    @Override
    public int getLayoutId() {
        return R.layout.activity_goods_comments;
    }

    @Override
    public void configViews() {
        context = this;
        setupToolbar(toolbar, true);
        setupToolbarBackNavigation(toolbar, R.drawable.close_white);
        ImmersionBar.with(this).statusBarDarkFont(false).init();
        SlidePanelHelper.attach(this);

        try {
            cutId = getIntent().getStringExtra("cutId");
            manageId = getIntent().getStringExtra("manageId");
            //System.out.println("cutId = " + cutId);
            //System.out.println("manageId = " + manageId);
            name = getIntent().getStringExtra("name");
            type = getIntent().getIntExtra("type", 0);
            tag = getIntent().getStringExtra("tag");
        } catch (Exception e) {
        }
        toolbar.setBackgroundColor(ContextCompat.getColor(this, R.color.refresh_one));
        toolbar_title.setTextColor(Color.WHITE);
    }

    @Override
    public void initDatas() {
        //评价汇总
        Api.getInstance().goodsCommentTotal(cutId, manageId)
                .compose(RxResponse.<CommentsTotal>compatT())
                .subscribe(new ApiObserver<CommentsTotal>() {
                    @Override
                    public void onNext(CommentsTotal data) {
                        if (data != null && data.getLabelList() != null && data.getLabelList().size() > 0) {
                            labels = data.getLabelList();
                        }

                        String title = name + " " + String.format(getString(R.string.comment_all), data.getCommentCount());
                        toolbar_title.setText(convert(title, name.length() + 1));

                        tabs.clear();
                        tabs.add(convert(String.format(getString(R.string.all_comment), data.getCommentCount()), 3));
                        tabs.add(convert(String.format(getString(R.string.good_comment), data.getGoodCommentCount()), 3));
                        tabs.add(convert(String.format(getString(R.string.mid_comment), data.getMinCommentCount()), 3));
                        tabs.add(convert(String.format(getString(R.string.bad_comment), data.getBadCommentCount()), 3));
                        initIndicator();
                        initAdapter();
                    }
                });
    }

    private SpannableString convert(String value, int start) {
        SpannableString ss = new SpannableString(value);
        RelativeSizeSpan sizeSpan = new RelativeSizeSpan(0.78f);
        ForegroundColorSpan colorSpan = new ForegroundColorSpan(Color.parseColor("#50ffffff"));
        ss.setSpan(sizeSpan, start, ss.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss.setSpan(colorSpan, start, ss.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return ss;
    }

    private void initAdapter() {
        GoodsCommentFragment fragment1 = GoodsCommentFragment.newInstance();
        Bundle args1 = new Bundle();
        args1.putInt("type", 2);
        args1.putString("tag", tag);
        args1.putString("cutId", cutId);
        args1.putString("manageId", manageId);
        fragment1.setArguments(args1);

        GoodsCommentFragment fragment2 = GoodsCommentFragment.newInstance();
        Bundle args2 = new Bundle();
        args2.putInt("type", 1);
        args2.putString("tag", tag);
        args2.putString("cutId", cutId);
        args2.putString("manageId", manageId);
        fragment2.setArguments(args2);

        GoodsCommentFragment fragment3 = GoodsCommentFragment.newInstance();
        Bundle args3 = new Bundle();
        args3.putInt("type", 0);
        args3.putString("tag", tag);
        args3.putString("cutId", cutId);
        args3.putString("manageId", manageId);
        fragment3.setArguments(args3);

        GoodsCommentFragment fragment4 = GoodsCommentFragment.newInstance();
        Bundle args4 = new Bundle();
        args4.putInt("type", -1);
        args4.putString("tag", tag);
        args4.putString("cutId", cutId);
        args4.putString("manageId", manageId);
        fragment4.setArguments(args4);

        fragments.clear();
        fragments.add(fragment1);
        fragments.add(fragment2);
        fragments.add(fragment3);
        fragments.add(fragment4);

        FragmentAdapter adapter = new FragmentAdapter(getSupportFragmentManager());
        adapter.setDatas(fragments);
        viewpager.setAdapter(adapter);
        viewpager.setCurrentItem(type);
        viewpager.setOffscreenPageLimit(fragments.size());
    }

    private void initIndicator() {
        CommonNavigator commonNavigator = new CommonNavigator(context);
        commonNavigator.setAdapter(new CommonNavigatorAdapter() {
            @Override
            public int getCount() {
                return tabs.size();
            }

            @Override
            public IPagerTitleView getTitleView(Context context, final int index) {
                SimplePagerTitleView simplePagerTitleView = new ColorTransitionPagerTitleView(context);
                simplePagerTitleView.setNormalColor(ContextCompat.getColor(context, R.color.white_7));
                simplePagerTitleView.setSelectedColor(ContextCompat.getColor(context, R.color.white));
                simplePagerTitleView.setText(tabs.get(index));
                simplePagerTitleView.setTextSize(15);
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
                linePagerIndicator.setColors(ContextCompat.getColor(context, R.color.white));
                return linePagerIndicator;
            }
        });
        indicator.setNavigator(commonNavigator);
        LinearLayout titleContainer = commonNavigator.getTitleContainer();
        titleContainer.setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE);
        titleContainer.setDividerDrawable(new ColorDrawable() {
            @Override
            public int getIntrinsicWidth() {
                return DensityUtil.dp2px(context, 15f);
            }
        });
        ViewPagerHelper.bind(indicator, viewpager);
    }
}