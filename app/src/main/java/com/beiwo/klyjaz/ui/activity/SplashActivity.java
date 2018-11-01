package com.beiwo.klyjaz.ui.activity;

import android.support.annotation.NonNull;

import com.beiwo.klyjaz.App;
import com.beiwo.klyjaz.R;
import com.beiwo.klyjaz.api.Api;
import com.beiwo.klyjaz.base.BaseComponentActivity;
import com.beiwo.klyjaz.entity.Audit;
import com.beiwo.klyjaz.injection.component.AppComponent;
import com.beiwo.klyjaz.tang.rx.RxResponse;
import com.beiwo.klyjaz.tang.rx.observer.ApiObserver;

/**
 * https://gitee.com/tangbuzhi
 *
 * @author: Tangbuzhi
 * @version:
 * @package:
 * @description:
 * @modify:
 * @date: 2018/11/1
 */

public class SplashActivity extends BaseComponentActivity {
    @Override
    public int getLayoutId() {
        return R.layout.activity_splash;
    }

    @Override
    public void configViews() {
        /*是否审核状态*/
        Api.getInstance().audit()
                .compose(RxResponse.<Audit>compatT())
                .subscribe(new ApiObserver<Audit>() {
                    @Override
                    public void onNext(Audit data) {
                        App.audit = data.audit;
                        launch();
                    }

                    @Override
                    public void onError(@NonNull Throwable t) {
                        super.onError(t);
                        launch();
                    }
                });
    }

    @Override
    public void initDatas() {
    }

    private void launch() {
        if (App.audit == 2) MainActivity.init(this);
        else VestMainActivity.init(this);
    }

    @Override
    protected void configureComponent(AppComponent appComponent) {
    }
}