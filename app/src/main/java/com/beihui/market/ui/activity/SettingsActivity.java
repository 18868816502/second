package com.beihui.market.ui.activity;


import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.beihui.market.BuildConfig;
import com.beihui.market.R;
import com.beihui.market.base.BaseComponentActivity;
import com.beihui.market.entity.AppUpdate;
import com.beihui.market.helper.DataCleanManager;
import com.beihui.market.helper.SlidePanelHelper;
import com.beihui.market.helper.updatehelper.AppUpdateHelper;
import com.beihui.market.injection.component.AppComponent;
import com.beihui.market.injection.component.DaggerSettingComponent;
import com.beihui.market.injection.module.SettingModule;
import com.beihui.market.ui.busevents.UserLogoutEvent;
import com.beihui.market.ui.contract.SettingContract;
import com.beihui.market.ui.dialog.AlertDialog;
import com.beihui.market.ui.dialog.CommNoneAndroidDialog;
import com.beihui.market.ui.presenter.SettingPresenter;
import com.beihui.market.umeng.Events;
import com.beihui.market.umeng.Statistic;
import com.beihui.market.util.viewutils.ToastUtils;
import com.beihui.market.view.RelativeLayoutBar;
import com.gyf.barlibrary.ImmersionBar;

import org.greenrobot.eventbus.EventBus;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 设置页面
 */
public class SettingsActivity extends BaseComponentActivity implements SettingContract.View {

    @BindView(R.id.tool_bar)
    Toolbar toolbar;
    @BindView(R.id.clear_cache)
    RelativeLayoutBar clearCacheRel;
    @BindView(R.id.check_version)
    RelativeLayoutBar checkVersionRel;
    //清楚缓存的大小
    private String mCacheSize;

    @Inject
    SettingPresenter presenter;

    private AppUpdateHelper updateHelper = AppUpdateHelper.newInstance();

    @Override
    protected void onDestroy() {
        updateHelper.destroy();
        presenter.onDestroy();
        presenter = null;
        super.onDestroy();
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_settings;
    }

    @Override
    public void configViews() {
        ImmersionBar.with(this).statusBarDarkFont(true).init();
        setupToolbar(toolbar);

        SlidePanelHelper.attach(this);
    }

    @Override
    public void initDatas() {
        presenter.onStart();
        /**
         * 显示版本号
         */
        checkVersionRel.setRightTextView1Text("v" + BuildConfig.VERSION_NAME);

        try {
            mCacheSize = DataCleanManager.getFormatSize(DataCleanManager.getInternalCacheSize()
                    + DataCleanManager.getExternalCacheSize());
        } catch (Exception e) {
            e.printStackTrace();
        }

        clearCacheRel.setRightTextView1Text(mCacheSize);
    }

    @Override
    protected void configureComponent(AppComponent appComponent) {
        DaggerSettingComponent.builder()
                .appComponent(appComponent)
                .settingModule(new SettingModule(this))
                .build()
                .inject(this);
    }

    @OnClick({R.id.about_kaola, R.id.star_me, R.id.check_version, R.id.exit, R.id.clear_cache})
    void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.about_kaola:
                startActivity(new Intent(SettingsActivity.this, AboutKaolaActivity.class));
                break;
            case R.id.star_me:
                String model = android.os.Build.MODEL;
                //品牌
                String brand = android.os.Build.BRAND;
                //制造商
                String manufacturer = android.os.Build.MANUFACTURER;
                Log.e("MANUFACTURER", "MANUFACTURER--> " + manufacturer);
                if ("samsung".equals(manufacturer)) {
                    goToSamsungappsMarket();
                } else {
                    try {
                        Intent toMarket = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + getApplicationInfo().packageName));
                        startActivityWithoutOverride(toMarket);
                    } catch (ActivityNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                break;
            case R.id.exit:
                //umeng统计
                Statistic.onEvent(Events.SETTING_EXIT);

                new CommNoneAndroidDialog().withMessage("确认退出" + getString(R.string.app_name))
                        .withPositiveBtn("再看看", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                //umeng统计
                                Statistic.onEvent(Events.EXIT_DISMISS);
                            }
                        })
                        .withNegativeBtn("退出", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                //umeng统计
                                Statistic.onEvent(Events.EXIT_CONFIRM);

                                presenter.logout();
                            }
                        }).show(getSupportFragmentManager(), CommNoneAndroidDialog.class.getSimpleName());
                break;
            case R.id.check_version:
                presenter.checkVersion();
                break;

            case R.id.clear_cache:

                showAlertDialog();
                break;
        }
    }

    /**
     * 弹窗
     */
    private void showAlertDialog() {
        final AlertDialog dialog = new AlertDialog(this);
        dialog.builder().setMsg("确定清空缓存？")
                .setPositiveButton("确定", new View.OnClickListener() {

                    @Override
                    public void onClick(View arg0) {
                        boolean a = DataCleanManager.cleanInternalCache();
                        boolean b = DataCleanManager.cleanExternalCache();
                        if (a && b) {
                            clearCacheRel.setRightTextView1Text("0M");
                            Toast.makeText(SettingsActivity.this, "缓存已清除", Toast.LENGTH_SHORT).show();
                        } else {
                            clearCacheRel.setRightTextView1Text("清除失败");
                        }

                    }
                }).setNegativeButton("取消", new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
            }
        }).show();


    }


    /**
     * https://www.cnblogs.com/qwangxiao/p/8030389.html
     */
    public void goToSamsungappsMarket() {
        Uri uri = Uri.parse("http://www.samsungapps.com/appquery/appDetail.as?appId=" + getApplicationInfo().packageName);
        Intent goToMarket = new Intent();
        goToMarket.setClassName("com.sec.android.app.samsungapps", "com.sec.android.app.samsungapps.Main");
        goToMarket.setData(uri);
        try {
            startActivity(goToMarket);
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setPresenter(SettingContract.Presenter presenter) {
        //injected.nothing to do.
    }

    @Override
    public void showLatestVersion(String version) {
        if (version != null) {
        }
    }

    @Override
    public void showLogoutSuccess() {

        //发送用户退出全局事件
        EventBus.getDefault().post(new UserLogoutEvent());
        Intent broadCast = new Intent();
        broadCast.setAction("logout");
        sendBroadcast(broadCast);

        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }


    @Override
    public void showUpdate(AppUpdate update) {
        final AppUpdate appInfo = update;
        CommNoneAndroidDialog dialog = new CommNoneAndroidDialog()
                .withMessage(update.getContent())
                .withPositiveBtn("立即更新", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        updateHelper.processAppUpdate(appInfo, SettingsActivity.this);
                    }
                })
                .withNegativeBtn("稍后再说", null);
        dialog.setCancelable(false);
        dialog.show(getSupportFragmentManager(), "Update");
    }

    @Override
    public void showHasBeenLatest(String msg) {
        ToastUtils.showShort(this, msg, null);
    }
}

