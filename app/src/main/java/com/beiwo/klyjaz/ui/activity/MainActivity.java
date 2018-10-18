package com.beiwo.klyjaz.ui.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
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
import android.widget.TextView;

import com.beiwo.klyjaz.BuildConfig;
import com.beiwo.klyjaz.R;
import com.beiwo.klyjaz.api.Api;
import com.beiwo.klyjaz.api.NetConstants;
import com.beiwo.klyjaz.base.BaseComponentActivity;
import com.beiwo.klyjaz.entity.AdBanner;
import com.beiwo.klyjaz.entity.TabImage;
import com.beiwo.klyjaz.entity.TabImageBean;
import com.beiwo.klyjaz.event.TabNewsWebViewFragmentUrlEvent;
import com.beiwo.klyjaz.helper.DataStatisticsHelper;
import com.beiwo.klyjaz.helper.UserHelper;
import com.beiwo.klyjaz.helper.updatehelper.AppUpdateHelper;
import com.beiwo.klyjaz.injection.component.AppComponent;
import com.beiwo.klyjaz.loan.TabHomeFragment;
import com.beiwo.klyjaz.loan.TabLoanFragment;
import com.beiwo.klyjaz.tang.fragment.ToolFragment;
import com.beiwo.klyjaz.tang.rx.RxResponse;
import com.beiwo.klyjaz.tang.rx.observer.ApiObserver;
import com.beiwo.klyjaz.ui.busevents.NavigateNews;
import com.beiwo.klyjaz.ui.busevents.UserLoginWithPendingTaskEvent;
import com.beiwo.klyjaz.ui.dialog.AdDialog;
import com.beiwo.klyjaz.ui.fragment.PersonalFragment;
import com.beiwo.klyjaz.ui.fragment.SocialRecommendFragment;
import com.beiwo.klyjaz.umeng.Events;
import com.beiwo.klyjaz.umeng.NewVersionEvents;
import com.beiwo.klyjaz.umeng.Statistic;
import com.beiwo.klyjaz.util.SPUtils;
import com.beiwo.klyjaz.util.ToastUtil;
import com.beiwo.klyjaz.view.BottomNavigationBar;
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
    //账单
    @BindView(R.id.tab_bill_icon)
    ImageView tabBillIcon;
    @BindView(R.id.tab_bill_text)
    TextView tabBillText;
    //发现
    @BindView(R.id.tab_discover_icon)
    ImageView tabDiscoverIcon;
    @BindView(R.id.tab_discover_text)
    TextView tabDiscoverText;
    //发现
    @BindView(R.id.tab_social_icon)
    ImageView tabSocialIcon;
    @BindView(R.id.tab_social_text)
    TextView tabSocialText;
    //个人
    @BindView(R.id.tab_mine_icon)
    ImageView tabMineIcon;
    @BindView(R.id.tab_mine_text)
    TextView tabMineText;
    //第三个tab
    @BindView(R.id.tab_three_icon)
    ImageView tabThreeIcon;
    @BindView(R.id.tab_three_text)
    TextView tabThreeText;

    //保存正切换的底部模块 ID
    private int selectedFragmentId = -1;

    private AppUpdateHelper updateHelper = AppUpdateHelper.newInstance();

    private ImageView[] iconView;
    private TextView[] textView;
    private boolean flag = false;

    @SuppressLint("InlinedApi")
    private String[] needPermission = {
            android.Manifest.permission.READ_EXTERNAL_STORAGE,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    private MainActivity activity;
    private Bundle extras;
    private Bundle extras1;

    /**
     * 重新进入MainActivity切换的对应的Fragment
     */
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        extras = intent.getExtras();
        extras1 = intent.getExtras();
        if (extras != null) {
            if (extras.getBoolean("account")) {
                navigationBar.select(R.id.tab_bill_root);
                if (!TextUtils.isEmpty(extras.getString("moxieMsg"))) {
                    ToastUtil.toast(extras.getString("moxieMsg"));
                }
            }
            if (extras.getBoolean("loan")) {
                navigationBar.select(R.id.tab_discover_root);
            }
            if (extras.getBoolean("home")) {
                navigationBar.select(R.id.tab_bill_root);
            }
            if (extras1.getBoolean("istk")) {
                navigationBar.select(R.id.tab_bill_root);
                if (!TextUtils.isEmpty(extras1.getString("tankuang"))) {
                    Intent tkIntent = new Intent(MainActivity.this, GetuiDialogActivity.class);
                    tkIntent.putExtra("pending_json", extras1.getString("tankuang"));
                    startActivity(tkIntent);
                }
            }
        }
    }

    /*广告页弹窗*/
    @Override
    protected void onStart() {
        super.onStart();
        if (getIntent() != null && getIntent().getExtras() != null && !flag) {
            extras1 = getIntent().getExtras();
            if (extras1.getBoolean("istk")) {
                navigationBar.select(R.id.tab_bill_root);
                if (!TextUtils.isEmpty(extras1.getString("tankuang"))) {
                    Intent tkIntent = new Intent(MainActivity.this, GetuiDialogActivity.class);
                    tkIntent.putExtra("pending_json", extras1.getString("tankuang"));
                    startActivity(tkIntent);
                }
            }
        }
    }

    private void showAdDialog(final AdBanner ad) {
        if (ad.getShowTimes() == 1) {//仅显示一次
            if (ad.getId().equals(SPUtils.getValue(activity, ad.getId()))) {
                return;
            } else {
                SPUtils.setValue(activity, ad.getId());
            }
        } else if (ad.getShowTimes() == 2) {//未点击继续显示
            if (ad.getId().equals(SPUtils.getValue(activity, ad.getId()))) {
                return;
            }
        } else {//其他情况不展示弹窗广告
            return;
        }
        //更新广告展示时间
        SPUtils.setLastAdShowTime(MainActivity.this, System.currentTimeMillis());
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
                if (ad.isNative()) {//跳原生还是跳Web
                    Intent intent = new Intent(MainActivity.this, LoanDetailActivity.class);
                    intent.putExtra("loanId", ad.getLocalId());
                    startActivity(intent);
                    SPUtils.setValue(activity, ad.getId());
                } else if (!TextUtils.isEmpty(ad.getUrl())) {
                    String url = ad.getUrl();
                    if (url.contains("USERID") && UserHelper.getInstance(MainActivity.this).getProfile() != null) {
                        url = url.replace("USERID", UserHelper.getInstance(MainActivity.this).getProfile().getId());
                    }
                    Intent intent = new Intent(MainActivity.this, ComWebViewActivity.class);
                    intent.putExtra("title", ad.getTitle());
                    intent.putExtra("url", url);
                    startActivity(intent);
                    SPUtils.setValue(activity, ad.getId());
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
        iconView = new ImageView[]{tabBillIcon, tabDiscoverIcon, tabThreeIcon, tabSocialIcon, tabMineIcon};
        textView = new TextView[]{tabBillText, tabDiscoverText, tabThreeText, tabSocialText, tabMineText};
        activity = this;
        if (!EventBus.getDefault().isRegistered(this)) EventBus.getDefault().register(this);
        navigationBar.setOnSelectedChangedListener(new BottomNavigationBar.OnSelectedChangedListener() {
            @Override
            public void onSelected(int selectedId) {
                if (selectedId != selectedFragmentId) {
                    selectTab(selectedId);
                }
            }
        });
    }

    @Override
    public void initDatas() {
        checkPermission();
        queryBottomImage();//请求底部导航栏图标 文字 字体颜色
        /*用户首次进入app，弹出登陆界面*/
        try {
            if (!"splash".equals(SPUtils.getValue(this, "splash")) &&
                    !TextUtils.equals("userLogin", SPUtils.getValue(this, "userLogin")) && !UserHelper.getInstance(this).isLogin()) {
                UserAuthorizationActivity.launch(this);
                SPUtils.setValue(this, "splash");
                SPUtils.setValue(this, "userLogin");
            }
        } catch (Exception e) {
        }
        updateHelper.checkUpdate(this);
        Api.getInstance().querySupernatant(3)
                .compose(RxResponse.<List<AdBanner>>compatT())
                .subscribe(new ApiObserver<List<AdBanner>>() {
                    @Override
                    public void onNext(@NonNull List<AdBanner> data) {
                        if (data != null && data.size() > 0) {
                            AdBanner adBanner = data.get(0);
                            if (adBanner.getLocation() == 2) {//发现页
                                navigationBar.select(R.id.tab_discover_root);
                            } else {//首页
                                navigationBar.select(R.id.tab_bill_root);
                            }
                            showAdDialog(adBanner);
                        }
                    }
                });
    }

    public static void main(Activity activity) {
        activity.startActivity(new Intent(activity, MainActivity.class));
        activity.finish();
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

    private TabHomeFragment tabHome;
    private TabLoanFragment tabDiscover;
    private ToolFragment tabTool;
    public SocialRecommendFragment tabSocial;
    private PersonalFragment tabMine;
    public Fragment currentFragment;

    private void selectTab(int id) {
        selectedFragmentId = id;
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        if (tabHome == null) {
            tabHome = TabHomeFragment.newInstance();
            ft.add(R.id.tab_fragment, tabHome).hide(tabHome);
        }
        if (tabDiscover == null) {
            tabDiscover = TabLoanFragment.newInstance();
            ft.add(R.id.tab_fragment, tabDiscover).hide(tabDiscover);
        }
        if (tabSocial == null) {
            tabSocial = SocialRecommendFragment.getInstance();
            ft.add(R.id.tab_fragment, tabSocial).hide(tabSocial);
        }
        if (tabTool == null) {
            tabTool = ToolFragment.newInstance();
            ft.add(R.id.tab_fragment, tabTool).hide(tabTool);
        }
        if (tabMine == null) {
            tabMine = PersonalFragment.newInstance();
            ft.add(R.id.tab_fragment, tabMine).hide(tabMine);
        }
        switch (id) {
            case R.id.tab_bill_root://账单
                ft.hide(tabDiscover).hide(tabSocial).hide(tabMine).show(tabHome).hide(tabTool);
                ImmersionBar.with(this).statusBarDarkFont(false).init();
                //pv，uv统计
                DataStatisticsHelper.getInstance().onCountUv(NewVersionEvents.REPORTBUTTON);
                currentFragment = tabHome;
                break;
            case R.id.tab_discover_root://发现
                ft.show(tabDiscover).hide(tabHome).hide(tabTool).hide(tabMine).hide(tabSocial);
                ImmersionBar.with(this).statusBarDarkFont(true).init();
                //pv，uv统计
                DataStatisticsHelper.getInstance().onCountUv(NewVersionEvents.HPTALLY);
                currentFragment = tabDiscover;
                break;
            case R.id.tab_three_root://社区
                ft.show(tabSocial).hide(tabDiscover).hide(tabHome).hide(tabTool).hide(tabMine);
                ImmersionBar.with(this).statusBarDarkFont(true).init();
                //pv，uv统计
                DataStatisticsHelper.getInstance().onCountUv(NewVersionEvents.HPTALLY);
                currentFragment = tabDiscover;
                break;
            case R.id.tab_social_root://工具
                ft.show(tabTool).hide(tabHome).hide(tabDiscover).hide(tabMine).hide(tabSocial);
                ImmersionBar.with(this).statusBarDarkFont(false).init();
                //pv，uv统计
                DataStatisticsHelper.getInstance().onCountUv(NewVersionEvents.HPTALLY);
                currentFragment = tabDiscover;
                break;
            case R.id.tab_mine_root://个人
                ft.show(tabMine).hide(tabHome).hide(tabDiscover).hide(tabTool).hide(tabSocial);
                ImmersionBar.with(this).statusBarDarkFont(true).init();
                //pv，uv统计
                DataStatisticsHelper.getInstance().onCountUv(NewVersionEvents.DISCOVERBUTTON);
                currentFragment = tabMine;
                break;
            default:
                break;
        }
        ft.commitAllowingStateLoss();
    }

    private long exitTime = 0;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if (System.currentTimeMillis() - exitTime > 2000) {
                ToastUtil.toast("再按一次退出");
                exitTime = System.currentTimeMillis();
            } else {
                finish();
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
                        defaultTabIconTxt();
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
        tabTxt.add("首页");
        tabTxt.add("贷款");
        tabTxt.add("社区");
        tabTxt.add("工具");
        tabTxt.add("我的");
        List<Drawable[]> drawables = new ArrayList<>();
        Drawable[] bitmaps0 = new Drawable[]{ContextCompat.getDrawable(this, R.drawable.tab_home_btn_press), ContextCompat.getDrawable(this, R.drawable.tab_home_btn_normal)};
        Drawable[] bitmaps1 = new Drawable[]{ContextCompat.getDrawable(this, R.drawable.tab_borrow_btn_press), ContextCompat.getDrawable(this, R.drawable.tab_borrow_btn_normal)};
        Drawable[] bitmaps2 = new Drawable[]{ContextCompat.getDrawable(this, R.drawable.tab_community_btn_normal), ContextCompat.getDrawable(this, R.drawable.tab_community_btn_normal)};
        Drawable[] bitmaps3 = new Drawable[]{ContextCompat.getDrawable(this, R.drawable.tab_tool_btn_press), ContextCompat.getDrawable(this, R.drawable.tab_tool_btn_normal)};
        Drawable[] bitmaps4 = new Drawable[]{ContextCompat.getDrawable(this, R.drawable.tab_me_btn_press), ContextCompat.getDrawable(this, R.drawable.tab_me_btn_normal)};
        drawables.add(bitmaps0);
        drawables.add(bitmaps1);
        drawables.add(bitmaps2);
        drawables.add(bitmaps3);
        drawables.add(bitmaps4);
        for (int i = 0; i < tabTxt.size(); i++) {
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
            if (TextUtils.equals("1", tabImage.getFocus())) {

            }
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
            //tab字体颜色和文字
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
                        }, new Consumer<Throwable>() {
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

    @Override
    protected void onStop() {
        super.onStop();
        flag = true;
        extras1 = null;
    }
}