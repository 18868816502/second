package com.beihui.market.helper;


import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.beihui.market.App;
import com.beihui.market.api.Api;
import com.beihui.market.api.ResultEntity;
import com.beihui.market.entity.AppUpdate;
import com.beihui.market.injection.component.DaggerAppUpdateHelperComponent;
import com.beihui.market.ui.dialog.CommNoneAndroidDialog;
import com.beihui.market.util.LogUtils;
import com.beihui.market.util.RxUtil;

import java.io.File;
import java.lang.ref.WeakReference;

import javax.inject.Inject;

import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class AppUpdateHelper {

    private static AppUpdateHelper sInstance;

    @Inject
    Api api;

    private Disposable disposable;
    private AppUpdate app;
    private WeakReference<AppCompatActivity> weakReference;
    private DownloadAppTask task;

    public static AppUpdateHelper getInstance() {
        if (sInstance == null) {
            synchronized (AppUpdateHelper.class) {
                if (sInstance == null) {
                    sInstance = new AppUpdateHelper();
                }
            }
        }
        return sInstance;
    }

    private AppUpdateHelper() {
        DaggerAppUpdateHelperComponent.builder()
                .appComponent(App.getInstance().getAppComponent())
                .build()
                .inject(this);
    }

    public void checkUpdate(AppCompatActivity activity) {
        weakReference = new WeakReference<>(activity);
        if (this.disposable != null && !this.disposable.isDisposed()) {
            this.disposable.dispose();
        }
        disposable = api.queryUpdate()
                .compose(RxUtil.<ResultEntity<AppUpdate>>io2main())
                .subscribe(new Consumer<ResultEntity<AppUpdate>>() {
                               @Override
                               public void accept(@NonNull ResultEntity<AppUpdate> result) throws Exception {
                                   if (result.isSuccess()) {
                                       app = result.getData();
                                       handleUpdate(app, weakReference);
                                   }
                               }
                           },
                        new Consumer<Throwable>() {
                            @Override
                            public void accept(@NonNull Throwable throwable) throws Exception {
                                LogUtils.e("AppUpdateHelper", throwable);
                            }
                        });
    }

    public void destroy() {
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
        }
        disposable = null;
        if (task != null && !task.isCancelled()) {
            task.cancel(true);
        }
        task = null;
    }

    private void handleUpdate(AppUpdate app, WeakReference<AppCompatActivity> wr) {
        if (wr.get() != null) {
            AppCompatActivity context = wr.get();
            try {
                String version = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
                if (version.compareTo(app.getVersion()) < 0) {
                    CommNoneAndroidDialog dialog = new CommNoneAndroidDialog()
                            .withMessage(app.getContent())
                            .withPositiveBtn("立即更新", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                }
                            });
                    //非强制更新
                    if (app.getHasForcedUpgrade() != 1) {
                        dialog.withNegativeBtn("稍后再说", null);
                    }
                    dialog.setCancelable(false);
                    dialog.show(context.getSupportFragmentManager(), "Update");
                }
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    private void install(String fileName) {
        Context context = weakReference.get();
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(new File(Environment.getExternalStorageDirectory(), fileName + ".apk")),
                "application/vnd.android.package-archive");
        context.startActivity(intent);

    }

    private class DownloadAppTask extends AsyncTask<String, Long, Boolean> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(String... params) {
            return null;
        }

        @Override
        protected void onProgressUpdate(Long... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
        }
    }
}
