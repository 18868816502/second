package com.beihui.market.ui.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.beihui.market.BuildConfig;
import com.beihui.market.R;
import com.beihui.market.api.Api;
import com.beihui.market.api.NetConstants;
import com.beihui.market.base.BaseComponentActivity;
import com.beihui.market.entity.AdBanner;
import com.beihui.market.entity.TabImage;
import com.beihui.market.entity.TabImageBean;
import com.beihui.market.event.TabNewsWebViewFragmentUrlEvent;
import com.beihui.market.helper.DataStatisticsHelper;
import com.beihui.market.helper.UserHelper;
import com.beihui.market.helper.updatehelper.AppUpdateHelper;
import com.beihui.market.injection.component.AppComponent;
import com.beihui.market.tang.rx.RxResponse;
import com.beihui.market.tang.rx.observer.ApiObserver;
import com.beihui.market.ui.busevents.NavigateNews;
import com.beihui.market.ui.busevents.UserLoginWithPendingTaskEvent;
import com.beihui.market.ui.dialog.AdDialog;
import com.beihui.market.ui.fragment.HomeFragment;
import com.beihui.market.ui.fragment.PersonalFragment;
import com.beihui.market.ui.fragment.TabNewsWebViewFragment;
import com.beihui.market.umeng.Events;
import com.beihui.market.umeng.NewVersionEvents;
import com.beihui.market.umeng.Statistic;
import com.beihui.market.util.CommonUtils;
import com.beihui.market.util.FastClickUtils;
import com.beihui.market.util.SPUtils;
import com.beihui.market.util.SoundUtils;
import com.beihui.market.util.ToastUtil;
import com.beihui.market.view.BottomNavigationBar;
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
import zhy.com.highlight.HighLight;

public class MainActivity extends BaseComponentActivity {

    //导航栏背景
    @BindView(R.id.navigation_bar_bg)
    ImageView navigationBarBg;
    //导航栏跟布局
    @BindView(R.id.navigation_bar)
    BottomNavigationBar navigationBar;

    //账单
    @BindView(R.id.tab_bill_icon)
    ImageView tabBillIcon;
    @BindView(R.id.tab_bill_text)
    TextView tabBillText;
    //发现
    @BindView(R.id.tab_discover_root)
    RelativeLayout tabDiscoverRoot;
    @BindView(R.id.tab_discover_icon)
    ImageView tabDiscoverIcon;
    @BindView(R.id.tab_discover_text)
    TextView tabDiscoverText;
    //个人
    @BindView(R.id.tab_mine_root)
    RelativeLayout tabMineRoot;
    @BindView(R.id.tab_mine_icon)
    ImageView tabMineIcon;
    @BindView(R.id.tab_mine_text)
    TextView tabMineText;

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
    private String mNoticeId = "";

    //高亮
    private HighLight infoHighLight;

    /**
     * 重新进入MainActivity切换的对应的Fragment
     */
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Bundle extras = intent.getExtras();
        if (extras != null) {
            if (extras.getBoolean("account")) {
                navigationBar.select(R.id.tab_bill_root);
                if (!TextUtils.isEmpty(extras.getString("moxieMsg"))) {
                    ToastUtil.toast(extras.getString("moxieMsg"));
                }
            }
            if (extras.getBoolean("istk")) {
                if (!TextUtils.isEmpty(extras.getString("tankuang"))) {
                    Intent tkIntent = new Intent(MainActivity.this, GetuiDialogActivity.class);
                    tkIntent.putExtra("pending_json", extras.getString("tankuang"));
                    if (!CommonUtils.isForeground(MainActivity.this, GetuiDialogActivity.class.getName())) {
                        startActivity(intent);
                    }
                }
            }
        }
    }

    /*广告页弹窗*/
    @Override
    protected void onStart() {
        super.onStart();
        if (SPUtils.getShowMainAddBanner(this)) {
            SPUtils.setShowMainAddBanner(this, false);
            Api.getInstance().querySupernatant(3)
                    .compose(RxResponse.<List<AdBanner>>compatT())
                    .subscribe(new ApiObserver<List<AdBanner>>() {
                        @Override
                        public void onNext(@NonNull List<AdBanner> data) {
                            if (data != null && data.size() > 0) {
                                AdBanner adBanner = data.get(0);
                                if (!adBanner.getId().equals(SPUtils.getValue(MainActivity.this, adBanner.getId()))) {
                                    if (adBanner.getLocation() == 2) {//发现页
                                        navigationBar.select(R.id.tab_discover_root);
                                    } else {//首页
                                        navigationBar.select(R.id.tab_bill_root);
                                    }
                                    showAdDialog(adBanner);
                                }
                                //更新广告展示时间
                                SPUtils.setLastAdShowTime(MainActivity.this, System.currentTimeMillis());
                            }
                        }
                    });
        }
        //showGuide();//显示高亮
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
        iconView = new ImageView[]{tabBillIcon, tabDiscoverIcon, tabMineIcon};
        textView = new TextView[]{tabBillText, tabDiscoverText, tabMineText};

        EventBus.getDefault().register(this);
        ImmersionBar.with(this).fitsSystemWindows(false).statusBarColor(R.color.transparent).init();
        navigationBar.setOnSelectedChangedListener(new BottomNavigationBar.OnSelectedChangedListener() {
            @Override
            public void onSelected(int selectedId) {
                SoundUtils.getInstance().playTab();//点击音效
                if (selectedId != selectedFragmentId) {
                    selectTab(selectedId);
                }
            }
        });
        selectTab(R.id.tab_bill_icon);
    }

    @Override
    public void initDatas() {
        checkPermission();
        updateHelper.checkUpdate(this);
        queryBottomImage();//请求底部导航栏图标 文字 字体颜色

        //添加账单
        /*mAddBill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!FastClickUtils.isFastClick()) {
                    if (UserHelper.getInstance(MainActivity.this).getProfile() == null || UserHelper.getInstance(MainActivity.this).getProfile().getId() == null) {
                    //UserAuthorizationActivity.launch(MainActivity.this, null);
                        Intent intent = new Intent(MainActivity.this, UserAuthorizationActivity.class);
                        startActivity(intent);
                    } else {
                        Intent intent = new Intent(MainActivity.this, AccountFlowActivity.class);
                        startActivity(intent);
                    }
                }
            }
        });*/
    }

    //空事件
    @Override
    protected void configureComponent(AppComponent appComponent) {
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void navigateNews(NavigateNews event) {
        navigationBar.select(R.id.tab_discover_root);
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
    public HomeFragment tabHome;
    public TabNewsWebViewFragment tabDiscover;
    public PersonalFragment tabMine;

    private void selectTab(int id) {
        selectedFragmentId = id;
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        if (tabHome != null) {
            ft.hide(tabHome);
        }
        if (tabDiscover != null) {
            ft.hide(tabDiscover);
        }
        if (tabMine != null) {
            ft.hide(tabMine);
        }
        switch (id) {
            case R.id.tab_bill_root://账单
                if (tabHome == null) {
                    //tabDiscover = BillLoanAnalysisFragment.newInstance();
                    tabHome = HomeFragment.newInstance();
                    ft.add(R.id.tab_fragment, tabHome);
                }
                ft.show(tabHome);
                ImmersionBar.with(this).statusBarDarkFont(false).init();
                //pv，uv统计
                DataStatisticsHelper.getInstance().onCountUv(NewVersionEvents.REPORTBUTTON);

                /*tabBillIcon.setImageResource(R.mipmap.ic_tab_bill_select);
                tabBillText.setTextColor(ContextCompat.getColor(this, R.color.black_1));
                tabDiscoverIcon.setImageResource(R.mipmap.ic_tab_discover_normal);
                tabDiscoverText.setTextColor(ContextCompat.getColor(this, R.color.black_2));
                tabMineIcon.setImageResource(R.mipmap.ic_tab_mine_normal);
                tabMineText.setTextColor(ContextCompat.getColor(this, R.color.black_2));*/
                break;
            case R.id.tab_discover_root://发现
                if (tabDiscover == null) {
                    //tabHome = TabAccountFragment.newInstance();
                    tabDiscover = TabNewsWebViewFragment.newInstance();
                    ft.add(R.id.tab_fragment, tabDiscover);
                }
                ft.show(tabDiscover);
                ImmersionBar.with(this).statusBarDarkFont(true).init();
                //pv，uv统计
                DataStatisticsHelper.getInstance().onCountUv(NewVersionEvents.HPTALLY);

                if (tabHome != null) tabHome.recycler().smoothScrollToPosition(0);

                /*tabBillIcon.setImageResource(R.mipmap.ic_tab_bill_normal);
                tabBillText.setTextColor(ContextCompat.getColor(this, R.color.black_2));
                tabDiscoverIcon.setImageResource(R.mipmap.ic_tab_discover_select);
                tabDiscoverText.setTextColor(ContextCompat.getColor(this, R.color.black_1));
                tabMineIcon.setImageResource(R.mipmap.ic_tab_mine_normal);
                tabMineText.setTextColor(ContextCompat.getColor(this, R.color.black_2));*/
                break;
            case R.id.tab_mine_root://个人
                if (tabMine == null) {
                    //tabMine = TabNewsWebViewFragment.newInstance();
                    tabMine = PersonalFragment.newInstance();
                    ft.add(R.id.tab_fragment, tabMine);
                }
                ft.show(tabMine);
                ImmersionBar.with(this).statusBarDarkFont(true).init();
                //pv，uv统计
                DataStatisticsHelper.getInstance().onCountUv(NewVersionEvents.DISCOVERBUTTON);

                if (tabHome != null) tabHome.recycler().smoothScrollToPosition(0);

                /*tabBillIcon.setImageResource(R.mipmap.ic_tab_bill_normal);
                tabBillText.setTextColor(ContextCompat.getColor(this, R.color.black_2));
                tabDiscoverIcon.setImageResource(R.mipmap.ic_tab_discover_normal);
                tabDiscoverText.setTextColor(ContextCompat.getColor(this, R.color.black_2));
                tabMineIcon.setImageResource(R.mipmap.ic_tab_mine_select);
                tabMineText.setTextColor(ContextCompat.getColor(this, R.color.black_1));*/
                break;
            default:
                break;
        }
        ft.commit();
    }

    @Override
    public void onBackPressed() {
        if (FastClickUtils.isFastBackPress()) {
            super.onBackPressed();
        } else {
            Toast.makeText(this, "再按一次退出", Toast.LENGTH_SHORT).show();
        }
    }

    private long exitTime = 0;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if (System.currentTimeMillis() - exitTime > 2000) {
                //ToastUtil.showToast(this, "再按一次退出");
                ToastUtil.toast("再按一次退出");
                exitTime = System.currentTimeMillis();
            } else {
                finish();
                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
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
                .compose(RxResponse.<TabImageBean>compatT())
                .subscribe(new ApiObserver<TabImageBean>() {
                    @Override
                    public void onNext(@NonNull TabImageBean data) {
                        //审核 1-资讯页，2-借贷页
                        if (data.audit == 1) {
                            NetConstants.H5_FIND_WEVVIEW_DETAIL = BuildConfig.H5_DOMAIN + "/information-v2.html";
                            EventBus.getDefault().post(new TabNewsWebViewFragmentUrlEvent());
                        } else if (data.audit == 2) {
                            NetConstants.H5_FIND_WEVVIEW_DETAIL = NetConstants.H5_FIND_WEVVIEW_DETAIL_COPY;
                            EventBus.getDefault().post(new TabNewsWebViewFragmentUrlEvent());
                        }
                        if (data.bottomList.size() > 0) {
                            updateBottomSelector(data.bottomList);
                        } else {
                            defaultTabIconTxt();
                            navigationBar.select(R.id.tab_bill_root);
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable t) {
                        super.onError(t);
                        navigationBar.select(R.id.tab_bill_root);
                    }
                });
    }

    private void defaultTabIconTxt() {
        int[] colors = new int[]{
                ContextCompat.getColor(this, R.color.black_1),
                ContextCompat.getColor(this, R.color.black_1),
                ContextCompat.getColor(this, R.color.black_2)
        };
        int[][] states = new int[3][];
        states[0] = new int[]{android.R.attr.state_selected};
        states[1] = new int[]{android.R.attr.state_pressed};
        states[2] = new int[]{};
        ColorStateList colorStateList = new ColorStateList(states, colors);
        List<String> tabTxt = new ArrayList<>();
        tabTxt.add("账单");
        tabTxt.add("发现");
        tabTxt.add("我的");
        List<Drawable[]> drawables = new ArrayList<>();
        Drawable[] bitmaps0 = new Drawable[]{ContextCompat.getDrawable(this, R.mipmap.ic_tab_bill_select), ContextCompat.getDrawable(this, R.mipmap.ic_tab_bill_normal)};
        Drawable[] bitmaps1 = new Drawable[]{ContextCompat.getDrawable(this, R.mipmap.ic_tab_discover_select), ContextCompat.getDrawable(this, R.mipmap.ic_tab_discover_normal)};
        Drawable[] bitmaps2 = new Drawable[]{ContextCompat.getDrawable(this, R.mipmap.ic_tab_mine_select), ContextCompat.getDrawable(this, R.mipmap.ic_tab_mine_normal)};
        drawables.add(bitmaps0);
        drawables.add(bitmaps1);
        drawables.add(bitmaps2);
        for (int i = 0; i < 3; i++) {
            textView[i].setTextColor(colorStateList);
            textView[i].setText(tabTxt.get(i));
            StateListDrawable stateListDrawable = new StateListDrawable();
            stateListDrawable.addState(new int[]{android.R.attr.state_selected}, drawables.get(i)[0]);
            stateListDrawable.addState(new int[]{android.R.attr.state_pressed}, drawables.get(i)[0]);
            stateListDrawable.addState(new int[]{}, drawables.get(i)[1]);
            iconView[i].setImageDrawable(stateListDrawable);
        }
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
            //位置：1第1位，2第2位，3第3位，4第4位，5第5位，6底部栏横条
            if (tabImage.getPosition() == 6) {
                bgTabImage = tabImage;
                break;
            }
        }
        //底部栏背景
        if (bgTabImage != null) {
            Glide.with(this)
                    .load(bgTabImage.getUnselectedImage())
                    .asBitmap()
                    .centerCrop()
                    .into(navigationBarBg);
        }


        boolean isShowTabAccount = true;
        for (int i = 0; i < list.size(); ++i) {
            TabImage tabImage = list.get(i);
            /*focus配置app模块展示的优先级*/
            if (tabImage.getFocus() != null && "1".equals(tabImage.getFocus())) {
                if (tabImage.getPosition() == 1) {
                    navigationBar.select(R.id.tab_bill_root);
                    isShowTabAccount = false;
                } else if (tabImage.getPosition() == 2) {
                    navigationBar.select(R.id.tab_discover_root);
                    isShowTabAccount = false;
                } else if (tabImage.getPosition() == 3) {
                    navigationBar.select(R.id.tab_mine_root);
                    isShowTabAccount = false;
                }
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
                                OkHttpClient client = new OkHttpClient();
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
        if (isShowTabAccount) {//都没有选择那就选择首页
            navigationBar.select(R.id.tab_bill_root);
        }
    }
}
