package com.beihui.market.ui.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.beihui.market.BuildConfig;
import com.beihui.market.R;
import com.beihui.market.api.Api;
import com.beihui.market.api.NetConstants;
import com.beihui.market.api.ResultEntity;
import com.beihui.market.base.BaseComponentActivity;
import com.beihui.market.entity.AdBanner;
import com.beihui.market.entity.LastNoticeBean;
import com.beihui.market.entity.TabImage;
import com.beihui.market.entity.TabImageBean;
import com.beihui.market.entity.request.RequestConstants;
import com.beihui.market.event.ShowGuide;
import com.beihui.market.event.TabNewsWebViewFragmentTitleEvent;
import com.beihui.market.event.TabNewsWebViewFragmentUrlEvent;
import com.beihui.market.helper.DataStatisticsHelper;
import com.beihui.market.helper.UserHelper;
import com.beihui.market.helper.updatehelper.AppUpdateHelper;
import com.beihui.market.injection.component.AppComponent;
import com.beihui.market.ui.busevents.NavigateNews;
import com.beihui.market.ui.busevents.UserLoginWithPendingTaskEvent;
import com.beihui.market.ui.dialog.AdDialog;
import com.beihui.market.ui.fragment.TabAccountFragment;
import com.beihui.market.ui.fragment.TabMineFragment;
import com.beihui.market.ui.fragment.TabNewsWebViewFragment;
import com.beihui.market.umeng.Events;
import com.beihui.market.umeng.Statistic;
import com.beihui.market.util.FastClickUtils;
import com.beihui.market.util.RxUtil;
import com.beihui.market.util.SPUtils;
import com.beihui.market.util.SoundUtils;
import com.beihui.market.util.viewutils.ToastUtils;
import com.beihui.market.view.BottomNavigationBar;
import com.beihui.market.view.MarqueeTextView;
import com.bumptech.glide.Glide;
import com.gyf.barlibrary.ImmersionBar;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.BindView;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import okhttp3.Request;

public class MainActivity extends BaseComponentActivity {

    //导航栏背景
    @BindView(R.id.navigation_bar_bg)
    ImageView navigationBarBg;
    //导航栏跟布局
    @BindView(R.id.navigation_bar)
    BottomNavigationBar navigationBar;

    //账单图标
    @BindView(R.id.tab_account_icon)
    ImageView tabAccountIcon;
    //账单
    @BindView(R.id.tab_account_text)
    TextView tabAccountText;


    @BindView(R.id.tab_news)
    RelativeLayout tabNewsRoot;
    @BindView(R.id.tab_news_icon)
    ImageView tabNewsIcon;
    @BindView(R.id.tab_news_text)
    TextView tabNewsText;

    @BindView(R.id.tab_mine_icon)
    ImageView tabMineIcon;
    @BindView(R.id.tab_mine_text)
    TextView tabMineText;

    @BindView(R.id.tv_tab_account_notice)
    MarqueeTextView mNotice;

    @BindView(R.id.iv_ac_main_notice_cross)
    ImageView mNoticeCross;
    @BindView(R.id.ll_ac_main_notice_root)
    LinearLayout mRoot;


    //保存正切换的底部模块 ID
    private int selectedFragmentId = -1;

    private AppUpdateHelper updateHelper = AppUpdateHelper.newInstance();

    private ImageView[] iconView;
    private TextView[] textView;

    @SuppressLint("InlinedApi")
    private String[] needPermission = {
            android.Manifest.permission.READ_EXTERNAL_STORAGE,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    /**
     * 最新公告ID
     */
    private String mNoticeId ="";

    /**
     * 重新进入MainActivity切换的对应的Fragment
     */
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Bundle extras = intent.getExtras();
        if (extras != null) {
            if (extras.getBoolean("account")) {
//                navigationBar.select(R.id.tab_account);

                navigationBar.select(R.id.tab_account);
            }

            if (extras.getBoolean("mine")) {
//                navigationBar.select(R.id.tab_mine);
                navigationBar.select(R.id.tab_mine);
            }
        }
//        if (getIntent().getBooleanExtra("home", false)) {
//            navigationBar.select(R.id.tab_home);
//        }
    }

    /**
     * 广告页弹窗
     */
    @Override
    protected void onStart() {
        super.onStart();
        if (SPUtils.getShowMainAddBanner(this)) {
            SPUtils.setShowMainAddBanner(this, false);

            Api.getInstance().querySupernatant(RequestConstants.SUP_TYPE_DIALOG)
                    .compose(RxUtil.<ResultEntity<List<AdBanner>>>io2main())
                    .subscribe(new Consumer<ResultEntity<List<AdBanner>>>() {
                                   @Override
                                   public void accept(@NonNull ResultEntity<List<AdBanner>> result) throws Exception {
                                       if (result.isSuccess()) {
                                           AdBanner adBanner = result.getData().get(0);
                                           if (!adBanner.getId().equals(SPUtils.getValue(MainActivity.this, adBanner.getId()))) {
                                               showAdDialog(adBanner);
                                           }
                                           //更新广告展示时间
                                           SPUtils.setLastAdShowTime(MainActivity.this, System.currentTimeMillis());
                                       } else {
                                           showErrorMsg(result.getMsg());
                                       }
                                   }
                               },
                            new Consumer<Throwable>() {
                                @Override
                                public void accept(@NonNull Throwable throwable) throws Exception {

                                }
                            });
        }
    }

    private void showAdDialog(final AdBanner ad) {
        //umeng统计
        Statistic.onEvent(Events.RESUME_AD_DIALOG);

        //pv，uv统计
        DataStatisticsHelper.getInstance().onCountUv(DataStatisticsHelper.ID_SHOW_HOME_AD_DIALOG);

        new AdDialog().setAd(ad).setListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //umeng统计
                Statistic.onEvent(Events.CLICK_AD_DIALOG);

                //统计点击
                DataStatisticsHelper.getInstance().onAdClicked(ad.getId(), 3);

                //pv，uv统计
                DataStatisticsHelper.getInstance().onCountUv(DataStatisticsHelper.ID_CLICK_HONE_AD_DIALOG);

                //是否需要登录
                if (ad.needLogin()) {
                    if (UserHelper.getInstance(MainActivity.this).getProfile() == null) {
                        UserAuthorizationActivity.launchWithPending(MainActivity.this, ad);
                        return;
                    }
                }

                //跳原生还是跳Web
                if (ad.isNative()) {
                    Intent intent = new Intent(MainActivity.this, LoanDetailActivity.class);
                    intent.putExtra("loanId", ad.getLocalId());
                    startActivity(intent);

                    SPUtils.setValue(MainActivity.this, ad.getId());
                } else if (!TextUtils.isEmpty(ad.getUrl())) {
                    String url = ad.getUrl();
                    if (url.contains("USERID") && UserHelper.getInstance(MainActivity.this).getProfile() != null) {
                        url = url.replace("USERID", UserHelper.getInstance(MainActivity.this).getProfile().getId());
                    }
                    Intent intent = new Intent(MainActivity.this, ComWebViewActivity.class);
                    intent.putExtra("title", ad.getTitle());
                    intent.putExtra("url", url);
                    startActivity(intent);

                    SPUtils.setValue(MainActivity.this, ad.getId());
                }
            }
        }).show(getSupportFragmentManager(), AdDialog.class.getSimpleName());
    }



    @Override
    protected void onDestroy() {
        updateHelper.destroy();
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void configViews() {
        iconView = new ImageView[]{tabAccountIcon, tabNewsIcon, tabMineIcon};
        textView = new TextView[]{ tabAccountText, tabNewsText, tabMineText};

        EventBus.getDefault().register(this);
        ImmersionBar.with(this).fitsSystemWindows(false).statusBarColor(R.color.transparent).init();
        navigationBar.setOnSelectedChangedListener(new BottomNavigationBar.OnSelectedChangedListener() {
            @Override
            public void onSelected(int selectedId) {
                //点击音效
                SoundUtils.getInstance().playTab();
                if (selectedId != selectedFragmentId ) {
                    selectTab(selectedId);
                }
            }

        });

    }

    @Override
    public void initDatas() {
        checkPermission();
        updateHelper.checkUpdate(this);
        /**
         * 请求底部导航栏图标 文字 字体颜色
         */
        queryBottomImage();

        /**
         * 查询公告
         */
        Api.getInstance().getNewNotice().compose(RxUtil.<ResultEntity<LastNoticeBean>>io2main())
                .subscribe(new Consumer<ResultEntity<LastNoticeBean>>() {
                               @Override
                               public void accept(ResultEntity<LastNoticeBean> result) throws Exception {
                                   if (result.isSuccess()) {
                                       if (!result.getData().getId().equals(SPUtils.getValue(MainActivity.this, result.getData().getId()))) {
                                           mRoot.setVisibility(View.VISIBLE);
                                           //mNotice.setText("【" + result.getData().getTitle() + "】" + result.getData().getExplain());
                                           mNotice.setText(result.getData().getExplain());
                                           mNoticeId = result.getData().getId();


                                           mNotice.setMovementMethod(ScrollingMovementMethod.getInstance());
                                           mNotice.setSelected(true);
                                           mNotice.requestFocus();
                                           mNotice.setFocusableInTouchMode(true);
                                       }
                                   } else {
                                       Toast.makeText(MainActivity.this, result.getMsg(), Toast.LENGTH_SHORT).show();
                                   }
                               }
                           },
                        new Consumer<Throwable>() {
                            @Override
                            public void accept(final Throwable throwable) throws Exception {

                            }
                        });

        /**
         * 关闭公告并记录最新公告ID
         */
        mNoticeCross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRoot.setVisibility(View.GONE);
                SPUtils.setValue(MainActivity.this, mNoticeId);
            }
        });
    }

    //空事件
    @Override
    protected void configureComponent(AppComponent appComponent) {

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void navigateNews(NavigateNews event) {
        navigationBar.select(R.id.tab_news);
    }

    /**
     * 点击广告要求登录，登录成功之后收到事件完成后续动作
     * 事件由UserAuthorizationActivity发出
     */
    @Subscribe
    public void onLoginWithPendingTask(UserLoginWithPendingTaskEvent event) {
        final AdBanner ad = event.adBanner;
        navigationBar.postDelayed(new Runnable() {
            @Override
            public void run() {
                //跳Native还是跳Web
                if (ad.isNative()) {
                    Intent intent = new Intent(MainActivity.this, LoanDetailActivity.class);
                    intent.putExtra("loanId", ad.getLocalId());
                    intent.putExtra("loanName", ad.getTitle());
                    startActivity(intent);
                } else if (!TextUtils.isEmpty(ad.getUrl())) {
                    //跳转网页时，url不为空情况下才跳转
                    Intent intent = new Intent(MainActivity.this, ComWebViewActivity.class);
                    intent.putExtra("url", ad.getUrl());
                    intent.putExtra("title", ad.getTitle());
                    startActivity(intent);
                }
            }
        }, 400);
    }

    /**
     * 三大模块
     */
    public Fragment tabHome;
    public Fragment tabFind;
    public Fragment tabMine;

    private void selectTab(int id) {
        selectedFragmentId = id;

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        if (tabHome == null) {
            tabHome = TabAccountFragment.newInstance();
            ft.add(R.id.tab_fragment, tabHome);
        }
        ft.hide(tabHome);
        if (tabFind == null) {
            tabFind = TabNewsWebViewFragment.newInstance();
            ft.add(R.id.tab_fragment, tabFind);
        }
        ft.hide(tabFind);
        if (tabMine == null) {
            tabMine = TabMineFragment.newInstance();
            ft.add(R.id.tab_fragment, tabMine);
        }
        ft.hide(tabMine);
        switch (id) {
            //账单
            case R.id.tab_account:
                if (tabHome == null) {
                    tabHome = TabAccountFragment.newInstance();
                    ft.add(R.id.tab_fragment, tabHome);
                }
                ft.show(tabHome);
                break;
            //发现
            case R.id.tab_news:
                if (tabFind == null) {
                    tabFind = TabNewsWebViewFragment.newInstance();
                    ft.add(R.id.tab_fragment, tabFind);
                }
                ft.show(tabFind);
                break;
            //我的
            case R.id.tab_mine:
                if (tabMine == null) {
                    tabMine = TabMineFragment.newInstance();
                    ft.add(R.id.tab_fragment, tabMine);
                }
                ft.show(tabMine);
                break;
        }
        ft.commit();
        if (id == R.id.tab_account) {
            Log.e("adfsjafl", "shfodshfjldfjlfj");
            EventBus.getDefault().postSticky(new ShowGuide());
        }
    }

    @Override
    public void onBackPressed() {
        if (FastClickUtils.isFastBackPress()) {
            super.onBackPressed();
        } else {
            Toast.makeText(this, "再按一次退出", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void finish() {
        override = false;
        super.finish();
    }

    private void checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!SPUtils.getCheckPermission(this)) {
                ArrayList<String> permission = new ArrayList<>();
                for (int i = 0; i < needPermission.length; ++i) {
                    if (ContextCompat.checkSelfPermission(this, needPermission[i]) != PackageManager.PERMISSION_GRANTED) {
                        permission.add(needPermission[i]);
                    }
                }
                if (permission.size() > 0) {
                    try {
                        ActivityCompat.requestPermissions(this, permission.toArray(needPermission), 1);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                SPUtils.setCheckPermission(this, true);
            }
        }
    }

    private void queryBottomImage() {
        Api.getInstance().queryBottomImage()
                .compose(RxUtil.<ResultEntity<TabImageBean>>io2main())
                .subscribe(new Consumer<ResultEntity<TabImageBean>>() {
                               @Override
                               public void accept(ResultEntity<TabImageBean> result) throws Exception {
                                   if (result.isSuccess()) {
                                       if (result.getData() != null) {
                                           /**
                                         * 审核 1-资讯页，2-借贷页
                                         */
                                           if (result.getData().audit == 1) {
                                               NetConstants.H5_FIND_WEVVIEW_DETAIL = BuildConfig.H5_DOMAIN + "/information.html";
                                               EventBus.getDefault().post(new TabNewsWebViewFragmentUrlEvent());

                                           }
                                           if (result.getData().audit == 2) {
                                               NetConstants.H5_FIND_WEVVIEW_DETAIL  = NetConstants.H5_FIND_WEVVIEW_DETAIL_COPY;
                                               EventBus.getDefault().post(new TabNewsWebViewFragmentUrlEvent());
                                           }
                                           if (result.getData().bottomList.size() > 0) {
                                               updateBottomSelector(result.getData().bottomList);
                                           } else {
                                               navigationBar.select(R.id.tab_account);
                                           }
                                       } else {
                                           navigationBar.select(R.id.tab_account);
                                       }
                                   }
                               }
                           },
                        new Consumer<Throwable>() {
                            @Override
                            public void accept(final Throwable throwable) throws Exception {
                                Log.e("MainActivity", throwable.toString());
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        navigationBar.select(R.id.tab_account);

                                        try {
                                            throw throwable;
                                        } catch (Throwable throwable1) {
                                            throwable1.printStackTrace();
                                        }
                                    }
                                });
                            }
                        });
    }

    private void updateBottomSelector(List<TabImage> list) {
        Collections.sort(list, new Comparator<TabImage>() {
            @Override
            public int compare(TabImage o1, TabImage o2) {
                return o1.getPosition() - o2.getPosition();
            }
        });

        TabImage bgTabImage = null;
        for (TabImage tabImage : list) {
            if (tabImage.getPosition() == 6) {
                bgTabImage = tabImage;
                break;
            }
        }
        if (bgTabImage != null) {
            Glide.with(this)
                    .load(bgTabImage.getUnselectedImage())
                    .asBitmap()
                    .centerCrop()
                    .into(navigationBarBg);
        }

        final OkHttpClient client = new OkHttpClient();
        for (int i = 0; i < list.size(); ++i) {
            TabImage tabImage = list.get(i);
            /**
             * @author xhb
             * focus配置app模块展示的优先级
             */
            if (tabImage.getFocus() != null && "1".equals(tabImage.getFocus())) {
                if (tabImage.getPosition() == 1) {
                    navigationBar.select(R.id.tab_account);
                } else if (tabImage.getPosition() == 2) {
                    navigationBar.select(R.id.tab_news);
                } else if (tabImage.getPosition() == 3) {
                    navigationBar.select(R.id.tab_mine);
                }
            } else {
                navigationBar.select(R.id.tab_account);
            }
            if (tabImage.getPosition() == 2) {
                EventBus.getDefault().post(new TabNewsWebViewFragmentTitleEvent(tabImage.getName()));
            }

            final int index = tabImage.getPosition() - 1;
            if (index < 0 || index >= textView.length) {
                continue;
            }

            if (!TextUtils.isEmpty(tabImage.getSelectedFontColor())) {
                int[] colors = new int[]{
                        Color.parseColor("#" + tabImage.getSelectedFontColor()),
                        Color.parseColor("#" + tabImage.getSelectedFontColor()),
                        Color.parseColor("#" + tabImage.getUnselectedFontColor())
                };
                int[][] states = new int[3][];
                states[0] = new int[]{android.R.attr.state_selected};
                states[1] = new int[]{android.R.attr.state_pressed};
                states[2] = new int[]{};
                ColorStateList colorStateList = new ColorStateList(states, colors);
                textView[index].setTextColor(colorStateList);
                textView[index].setText(tabImage.getName());
            }

            if (!TextUtils.isEmpty(tabImage.getSelectedImage())) {
                Observable.just(new String[]{tabImage.getSelectedImage(), tabImage.getUnselectedImage()})
                        .observeOn(Schedulers.io())
                        .map(new Function<String[], Bitmap[]>() {
                            @Override
                            public Bitmap[] apply(String[] strings) throws Exception {
                                Bitmap[] images = new Bitmap[2];
                                byte[] bytes = client.newCall(new Request.Builder().url(strings[0]).build()).execute().body().bytes();
                                images[0] = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);

                                bytes = client.newCall(new Request.Builder().url(strings[1]).build()).execute().body().bytes();
                                images[1] = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);

                                return images;
                            }
                        })
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<Bitmap[]>() {
                                       @Override
                                       public void accept(Bitmap[] bitmaps) throws Exception {
                                           if (bitmaps[0] != null && bitmaps[1] != null) {
                                               StateListDrawable stateListDrawable = new StateListDrawable();
                                               stateListDrawable.addState(new int[]{android.R.attr.state_selected}, new BitmapDrawable(getResources(), bitmaps[0]));
                                               stateListDrawable.addState(new int[]{android.R.attr.state_pressed}, new BitmapDrawable(getResources(), bitmaps[0]));
                                               stateListDrawable.addState(new int[]{}, new BitmapDrawable(getResources(), bitmaps[1]));

                                               iconView[index].setImageDrawable(stateListDrawable);
                                           }
                                       }
                                   },
                                new Consumer<Throwable>() {
                                    @Override
                                    public void accept(final Throwable throwable) throws Exception {
                                        Log.e("MainActivity", throwable.toString());
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                try {
                                                    throw throwable;
                                                } catch (Throwable throwable1) {
                                                    throwable1.printStackTrace();
                                                }
                                            }
                                        });
                                    }
                                });
            }
        }
    }

}
