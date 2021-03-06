package com.beiwo.qnejqaz.ui.activity;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.beiwo.qnejqaz.App;
import com.beiwo.qnejqaz.R;
import com.beiwo.qnejqaz.base.BaseActivity;
import com.beiwo.qnejqaz.util.SPUtils;
import com.beiwo.qnejqaz.view.IndicatorView;
import com.gyf.barlibrary.ImmersionBar;

import java.util.LinkedList;

/**
 * 欢迎页面 在关于我们里面 Splash也会调用
 */
public class WelcomeActivity extends BaseActivity {

    private boolean fromAboutUs;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        ViewPager viewPager = findViewById(R.id.view_pager);
        IndicatorView indicatorView = findViewById(R.id.indicator_view);
        final View startNowView = findViewById(R.id.start_now);

        viewPager.setAdapter(new WelcomeAdapter());
        indicatorView.setupWithViewPager(viewPager);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                startNowView.setVisibility(position == 2 ? View.VISIBLE : View.GONE);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        startNowView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fromAboutUs) {
                    finish();
                } else {
                    SPUtils.setValue("splash", "");
                    if (App.audit == 1) {
                        Intent intent = new Intent(WelcomeActivity.this, VestMainActivity.class);
                        startActivity(intent);
                    } else {
                        Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
                        startActivity(intent);
                    }
                    finish();
                }

            }
        });
        ImmersionBar.with(this).init();
        fromAboutUs = getIntent().getBooleanExtra("fromAboutUs", false);
    }

    @Override
    protected void onDestroy() {
        ImmersionBar.with(this).destroy();
        super.onDestroy();
    }

    private class WelcomeAdapter extends PagerAdapter {
        private LinkedList<View> cachedView = new LinkedList<>();
        private int[] imageId = {R.mipmap.ic_launcher, R.mipmap.ic_launcher, R.mipmap.ic_launcher};

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View view;
            ViewHolder holder;
            if (cachedView.size() > 0) {
                view = cachedView.pollFirst();
                holder = (ViewHolder) view.getTag();
            } else {
                view = LayoutInflater.from(container.getContext())
                        .inflate(R.layout.viewpager_item_welcom, container, false);
                holder = new ViewHolder();
                holder.imageView = view.findViewById(R.id.image);
                view.setTag(holder);
            }

            holder.imageView.setImageResource(imageId[position]);
            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            View view = (View) object;
            container.removeView(view);
            cachedView.add(view);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }
    }

    private class ViewHolder {
        ImageView imageView;
    }
}