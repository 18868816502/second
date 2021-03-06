package com.beiwo.qnejqaz.ui.activity;


import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.beiwo.qnejqaz.App;
import com.beiwo.qnejqaz.BuildConfig;
import com.beiwo.qnejqaz.R;
import com.beiwo.qnejqaz.base.BaseComponentActivity;
import com.beiwo.qnejqaz.entity.AppUpdate;
import com.beiwo.qnejqaz.helper.DataCleanManager;
import com.beiwo.qnejqaz.helper.SlidePanelHelper;
import com.beiwo.qnejqaz.helper.updatehelper.AppUpdateHelper;
import com.beiwo.qnejqaz.ui.busevents.UserLogoutEvent;
import com.beiwo.qnejqaz.ui.contract.SettingContract;
import com.beiwo.qnejqaz.ui.dialog.AlertDialog;
import com.beiwo.qnejqaz.ui.dialog.CommNoneAndroidDialog;
import com.beiwo.qnejqaz.ui.presenter.SettingPresenter;
import com.beiwo.qnejqaz.umeng.Events;
import com.beiwo.qnejqaz.umeng.Statistic;
import com.beiwo.qnejqaz.util.WeakRefToastUtil;
import com.beiwo.qnejqaz.view.RelativeLayoutBar;
import com.gyf.barlibrary.ImmersionBar;

import org.greenrobot.eventbus.EventBus;

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
    @BindView(R.id.about_kaola)
    RelativeLayoutBar about_kaola;
    //清除缓存的大小
    private String mCacheSize;

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
        presenter = new SettingPresenter(this, this);
    }

    @Override
    public void initDatas() {
        presenter.onStart();
        /*显示版本号*/
        checkVersionRel.setRightTextView1Text("v" + BuildConfig.VERSION_NAME);

        try {
            mCacheSize = DataCleanManager.getFormatSize(DataCleanManager.getInternalCacheSize()
                    + DataCleanManager.getExternalCacheSize());
        } catch (Exception e) {
            e.printStackTrace();
        }
        about_kaola.setLeftTextViewText(String.format(getString(R.string.about_app), getString(R.string.app_name)));
        clearCacheRel.setRightTextView1Text(mCacheSize);
    }

    @OnClick({R.id.about_kaola, R.id.star_me, R.id.check_version, R.id.exit, R.id.clear_cache})
    void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.about_kaola:
                startActivity(new Intent(this, AboutUsActivity.class));
                break;
            case R.id.star_me:
                //制造商
                String manufacturer = android.os.Build.MANUFACTURER;
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
                        }).withNegativeBtn("退出", new View.OnClickListener() {
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
        }
    }

    @Override
    public void setPresenter(SettingContract.Presenter presenter) {
        //injected.nothing to do.
    }

    @Override
    public void showLatestVersion(String version) {
        if (version != null && !TextUtils.isEmpty(version)) {
            checkVersionRel.setRightTextView1Text(version);
        }
    }

    @Override
    public void showLogoutSuccess() {
        //发送用户退出全局事件
        EventBus.getDefault().post(new UserLogoutEvent());
        EventBus.getDefault().post("1");
        Intent broadCast = new Intent();
        broadCast.setAction("logout");
        sendBroadcast(broadCast);

        Intent intent = new Intent(this, App.audit == 2 ? MainActivity.class : VestMainActivity.class);
        intent.putExtra("home", true);
        startActivity(intent);
        finish();
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
        WeakRefToastUtil.showShort(this, msg, null);
    }
}

