package com.beihui.market.tang.activity;

import android.app.Activity;
import android.support.v7.widget.Toolbar;

import com.beihui.market.R;
import com.beihui.market.api.Api;
import com.beihui.market.base.BaseComponentActivity;
import com.beihui.market.entity.DebtDetail;
import com.beihui.market.helper.SlidePanelHelper;
import com.beihui.market.helper.UserHelper;
import com.beihui.market.injection.component.AppComponent;
import com.beihui.market.tang.rx.RxResponse;
import com.beihui.market.tang.rx.observer.ApiObserver;
import com.gyf.barlibrary.ImmersionBar;

import butterknife.BindView;
import io.reactivex.annotations.NonNull;

/**
 * https://gitee.com/tangbuzhi
 *
 * @author: Tangbuzhi
 * @version:
 * @package:
 * @description:
 * @modify:
 * @date: 2018/7/22
 */

public class NetLoanDetailActivity extends BaseComponentActivity {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    private Activity activity;
    private String recordId;
    private String billId;

    @Override
    public int getLayoutId() {
        return R.layout.f_activity_bill_detail;
    }

    @Override
    public void configViews() {
        setupToolbar(mToolbar);
        ImmersionBar.with(this).statusBarDarkFont(true).init();
        SlidePanelHelper.attach(this);
        activity = this;
    }

    @Override
    public void initDatas() {
        recordId = getIntent().getStringExtra("recordId");
        billId = getIntent().getStringExtra("billId");
        //网贷账单详情
        Api.getInstance().fetchLoanDebtDetail(UserHelper.getInstance(activity).id(), recordId, billId)
                .compose(RxResponse.<DebtDetail>compatT())
                .subscribe(new ApiObserver<DebtDetail>() {
                    @Override
                    public void onNext(@NonNull DebtDetail data) {
                        String channelName = data.getChannelName();
                        String logo = data.getLogo();

                    }
                });
    }

    @Override
    protected void configureComponent(AppComponent appComponent) {

    }
}
