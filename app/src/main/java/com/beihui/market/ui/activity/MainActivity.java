package com.beihui.market.ui.activity;

import android.annotation.SuppressLint;
import android.content.Context;
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
import android.util.Log;
import android.view.MotionEvent;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.beihui.market.R;
import com.beihui.market.api.Api;
import com.beihui.market.api.ResultEntity;
import com.beihui.market.base.BaseComponentActivity;
import com.beihui.market.entity.AdBanner;
import com.beihui.market.entity.TabImage;
import com.beihui.market.helper.updatehelper.AppUpdateHelper;
import com.beihui.market.injection.component.AppComponent;
import com.beihui.market.ui.busevents.NavigateNews;
import com.beihui.market.ui.busevents.UserLoginWithPendingTaskEvent;
import com.beihui.market.ui.fragment.TabAccountFragment;
import com.beihui.market.ui.fragment.TabLoanFragment;
import com.beihui.market.ui.fragment.TabMineFragment;
import com.beihui.market.ui.fragment.TabNewsFragment;
import com.beihui.market.ui.fragment.TabNewsWebViewFragment;
import com.beihui.market.util.FastClickUtils;
import com.beihui.market.util.RxUtil;
import com.beihui.market.util.SPUtils;
import com.beihui.market.util.SoundUtils;
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

    @BindView(R.id.tab_news_icon)
    ImageView tabNewsIcon;
    @BindView(R.id.tab_news_text)
    TextView tabNewsText;

    @BindView(R.id.tab_mine_icon)
    ImageView tabMineIcon;
    @BindView(R.id.tab_mine_text)
    TextView tabMineText;

//    @BindView(R.id.tab_home_icon)
//    ImageView tabHomeIcon;
//    @BindView(R.id.tab_home_text)
//    TextView tabHomeText;

    private int selectedFragmentId = -1;

    private InputMethodManager inputMethodManager;

    private AppUpdateHelper updateHelper = AppUpdateHelper.newInstance();

    private ImageView[] iconView;
    private TextView[] textView;

    @SuppressLint("InlinedApi")
    private String[] needPermission = {
            android.Manifest.permission.READ_EXTERNAL_STORAGE,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Bundle extras = intent.getExtras();
        if (extras != null) {
            if (extras.getBoolean("account")) {
//                navigationBar.select(R.id.tab_account);
                navigationBar.select(R.id.tab_mine);
            }
        }
//        if (getIntent().getBooleanExtra("home", false)) {
//            navigationBar.select(R.id.tab_home);
//        }
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

                if (selectedId != selectedFragmentId) {
                    selectTab(selectedId);
                }
            }
        });
//        navigationBar.select(R.id.tab_account);
        navigationBar.select(R.id.tab_mine);

        queryBottomImage();
    }

    @Override
    public void initDatas() {
        checkPermission();
        updateHelper.checkUpdate(this);
    }

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

    private void selectTab(int id) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();

        String oldTag = makeTag(selectedFragmentId);
        selectedFragmentId = id;
        String newTag = makeTag(selectedFragmentId);
        Fragment lastSelected = fm.findFragmentByTag(oldTag);
        if (lastSelected != null) {
            ft.detach(lastSelected);
        }
        Fragment newSelected = fm.findFragmentByTag(newTag);
        if (newSelected == null) {
            switch (id) {
                //账单
                case R.id.tab_account:
                    newSelected = TabAccountFragment.newInstance();
                    break;
                //借款
//                case R.id.tab_home:
//                    newSelected = TabLoanFragment.newInstance();
//                    break;
                //资讯
                case R.id.tab_news:
                    newSelected = TabNewsWebViewFragment.newInstance();
                    break;
                //我的
                case R.id.tab_mine:
                    newSelected = TabMineFragment.newInstance();
                    break;
            }
            ft.add(R.id.tab_fragment, newSelected, newTag);
        } else {
            ft.attach(newSelected);
        }
        ft.commit();
    }

    private String makeTag(int id) {
        return "TabFragmentId=" + id;
    }

//    @Override
//    public boolean dispatchTouchEvent(MotionEvent ev) {
//        if (selectedFragmentId == R.id.tab_home) {
//            if (ev.getAction() == MotionEvent.ACTION_DOWN) {
//                if (inputMethodManager == null) {
//                    inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//                }
//                if (getCurrentFocus() != null && getCurrentFocus().getWindowToken() != null) {
//                    if (getCurrentFocus() instanceof EditText) {
//                        getCurrentFocus().clearFocus();
//                        inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
//                    }
//                }
//            }
//        }
//        return super.dispatchTouchEvent(ev);
//    }

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
                .compose(RxUtil.<ResultEntity<List<TabImage>>>io2main())
                .subscribe(new Consumer<ResultEntity<List<TabImage>>>() {
                               @Override
                               public void accept(ResultEntity<List<TabImage>> result) throws Exception {
                                   if (result.isSuccess()) {
                                       if (result.getData() != null && result.getData().size() > 0) {
                                           updateBottomSelector(result.getData());
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
