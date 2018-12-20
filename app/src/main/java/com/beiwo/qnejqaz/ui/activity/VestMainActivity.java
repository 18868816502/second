package com.beiwo.qnejqaz.ui.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
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
import android.widget.ImageView;
import android.widget.TextView;

import com.beiwo.qnejqaz.R;
import com.beiwo.qnejqaz.base.BaseComponentActivity;
import com.beiwo.qnejqaz.helper.DataHelper;
import com.beiwo.qnejqaz.helper.updatehelper.AppUpdateHelper;
import com.beiwo.qnejqaz.scdk.fragment.LoanFragment;
import com.beiwo.qnejqaz.tang.fragment.SocialRecomFragment;
import com.beiwo.qnejqaz.tang.fragment.ToolFragment;
import com.beiwo.qnejqaz.ui.fragment.PersonalFragment;
import com.beiwo.qnejqaz.umeng.NewVersionEvents;
import com.beiwo.qnejqaz.util.SPUtils;
import com.beiwo.qnejqaz.util.ToastUtil;
import com.beiwo.qnejqaz.view.BottomNavigationBar;
import com.gyf.barlibrary.ImmersionBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * https://gitee.com/tangbuzhi
 *
 * @author: Tangbuzhi
 * @version:
 * @package:
 * @description:
 * @modify:
 * @date: 2018/9/14
 */

public class VestMainActivity extends BaseComponentActivity {
    //导航栏背景
    @BindView(R.id.navigation_bar_bg)
    ImageView navigationBarBg;
    //导航栏跟布局
    @BindView(R.id.navigation_bar)
    BottomNavigationBar navigationBar;
    //借钱
    @BindView(R.id.tab_bill_icon)
    ImageView tabBillIcon;
    @BindView(R.id.tab_bill_text)
    TextView tabBillText;
    //社区
    @BindView(R.id.tab_three_icon)
    ImageView tabSocialIcon;
    @BindView(R.id.tab_three_text)
    TextView tabSocialText;
    //发现
    @BindView(R.id.tab_discover_icon)
    ImageView tabDiscoverIcon;
    @BindView(R.id.tab_discover_text)
    TextView tabDiscoverText;
    //工具
    @BindView(R.id.tab_tools_icon)
    ImageView tabToosIcon;
    @BindView(R.id.tab_tools_text)
    TextView tabToolsText;
    //个人
    @BindView(R.id.tab_mine_icon)
    ImageView tabMineIcon;
    @BindView(R.id.tab_mine_text)
    TextView tabMineText;

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

    private VestMainActivity activity;
    private Bundle extras;

    public static void init(Activity activity) {
        activity.startActivity(new Intent(activity, VestMainActivity.class));
        activity.finish();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        extras = intent.getExtras();
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
                String webViewUrl = extras.getString("webViewUrl");
                if (!TextUtils.isEmpty(webViewUrl)) {
                    tabHome.webViewUrl = webViewUrl;
                    tabHome.checking();
                }
                if (extras.getBoolean("dismiss")) {
                    tabHome.normalState();
                }
            }
            if (extras.getBoolean("istk")) {
                navigationBar.select(R.id.tab_bill_root);
                if (!TextUtils.isEmpty(extras.getString("tankuang"))) {
                    Intent tkIntent = new Intent(activity, GetuiDialogActivity.class);
                    tkIntent.putExtra("pending_json", extras.getString("tankuang"));
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
            extras = getIntent().getExtras();
            if (extras.getBoolean("istk")) {
                navigationBar.select(R.id.tab_bill_root);
                if (!TextUtils.isEmpty(extras.getString("tankuang"))) {
                    Intent tkIntent = new Intent(activity, GetuiDialogActivity.class);
                    tkIntent.putExtra("pending_json", extras.getString("tankuang"));
                    startActivity(tkIntent);
                }
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        switchTab();
    }

    @Override
    protected void onDestroy() {
        updateHelper.destroy();
        super.onDestroy();
    }

    @Override
    public int getLayoutId() {
        return R.layout.vest_activity_main;
    }

    @Override
    public void configViews() {
        iconView = new ImageView[]{tabBillIcon, tabSocialIcon, tabToosIcon, tabMineIcon};
        textView = new TextView[]{tabBillText, tabSocialText, tabToolsText, tabMineText};
        activity = this;
        navigationBar.setOnSelectedChangedListener(new BottomNavigationBar.OnSelectedChangedListener() {
            @Override
            public void onSelected(int selectedId) {
                if (selectedId != selectedFragmentId) {
                    selectTab(selectedId);
                    if (selectedId == R.id.tab_three_root) {
                        //pv，uv统计
                        DataHelper.getInstance(VestMainActivity.this).onCountUvPv(NewVersionEvents.COMMUNITY_RECOMMEND_PAGE, "");
                    }
                }
            }
        });
    }

    @Override
    public void initDatas() {
        checkPermission();
        defaultTabIconTxt();
    }

    public void switchTab() {
        if (selectedFragmentId != -1) {
            selectTab(selectedFragmentId);
        }
    }

    private LoanFragment tabHome;
    private SocialRecomFragment tabSocial;
    private ToolFragment tabTool;
    private PersonalFragment tabMine;

    private void selectTab(int id) {
        selectedFragmentId = id;
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        if (tabHome == null) {
            tabHome = LoanFragment.newInstance();
            ft.add(R.id.tab_fragment, tabHome).hide(tabHome);
        }
        if (tabSocial == null) {
            tabSocial = SocialRecomFragment.newInstance();
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
            case R.id.tab_bill_root:
                ft.hide(tabMine).hide(tabSocial).hide(tabTool).show(tabHome);
                ImmersionBar.with(this).statusBarDarkFont(false).init();
                //pv，uv统计
                DataHelper.getInstance(this).onCountUv(NewVersionEvents.REPORTBUTTON);
                break;
            case R.id.tab_three_root://社区
                ft.show(tabSocial).hide(tabHome).hide(tabTool).hide(tabMine);
                ImmersionBar.with(this).statusBarDarkFont(true).init();
                break;
            case R.id.tab_tools_root://工具
                ft.show(tabTool).hide(tabHome).hide(tabMine).hide(tabSocial);
                ImmersionBar.with(this).statusBarDarkFont(true).init();
                //pv，uv统计
                DataHelper.getInstance(this).onCountUv(NewVersionEvents.HPTALLY);
                break;
            case R.id.tab_mine_root://个人
                ft.show(tabMine).hide(tabHome).hide(tabTool).hide(tabSocial);
                ImmersionBar.with(this).statusBarDarkFont(false).init();
                //pv，uv统计
                DataHelper.getInstance(this).onCountUv(NewVersionEvents.DISCOVERBUTTON);
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
            if (!SPUtils.getCheckPermission()) {
                ArrayList<String> permission = new ArrayList<>();
                for (String p : needPermission) {
                    if (ContextCompat.checkSelfPermission(this, p) != PackageManager.PERMISSION_GRANTED) {
                        permission.add(p);
                    }
                }
                if (permission.size() > 0) {
                    try {
                        ActivityCompat.requestPermissions(this, permission.toArray(needPermission), 1);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                SPUtils.setCheckPermission(true);
            }
        }
    }

    private void defaultTabIconTxt() {
        int[] colors = new int[]{
                ContextCompat.getColor(this, R.color.refresh_one),
                ContextCompat.getColor(this, R.color.refresh_one),
                ContextCompat.getColor(this, R.color.black_2)
        };
        int[][] states = new int[3][];
        states[0] = new int[]{android.R.attr.state_selected};
        states[1] = new int[]{android.R.attr.state_pressed};
        states[2] = new int[]{};
        ColorStateList colorStateList = new ColorStateList(states, colors);
        List<String> tabTxt = new ArrayList<>();
        tabTxt.add("贷款");
        tabTxt.add("社区");
        tabTxt.add("工具");
        tabTxt.add("我的");
        List<Drawable[]> drawables = new ArrayList<>();
        Drawable[] bitmaps0 = new Drawable[]{ContextCompat.getDrawable(this, R.drawable.tab_borrow_btn_press), ContextCompat.getDrawable(this, R.drawable.tab_borrow_btn_normal)};
        Drawable[] bitmaps1 = new Drawable[]{ContextCompat.getDrawable(this, R.drawable.tab_community_btn_press), ContextCompat.getDrawable(this, R.drawable.tab_community_btn_normal)};
        Drawable[] bitmaps2 = new Drawable[]{ContextCompat.getDrawable(this, R.drawable.tab_tool_btn_press), ContextCompat.getDrawable(this, R.drawable.tab_tool_btn_normal)};
        Drawable[] bitmaps3 = new Drawable[]{ContextCompat.getDrawable(this, R.drawable.tab_me_btn_press), ContextCompat.getDrawable(this, R.drawable.tab_me_btn_normal)};
        drawables.add(bitmaps0);
        drawables.add(bitmaps1);
        drawables.add(bitmaps2);
        drawables.add(bitmaps3);
        for (int i = 0; i < tabTxt.size(); i++) {
            textView[i].setTextColor(colorStateList);
            textView[i].setText(tabTxt.get(i));
            StateListDrawable stateListDrawable = new StateListDrawable();
            stateListDrawable.addState(new int[]{android.R.attr.state_selected}, drawables.get(i)[0]);
            stateListDrawable.addState(new int[]{android.R.attr.state_pressed}, drawables.get(i)[0]);
            stateListDrawable.addState(new int[]{}, drawables.get(i)[1]);
            iconView[i].setImageDrawable(stateListDrawable);
        }
        navigationBar.select(R.id.tab_bill_root);
    }

    @Override
    protected void onStop() {
        super.onStop();
        flag = true;
    }
}