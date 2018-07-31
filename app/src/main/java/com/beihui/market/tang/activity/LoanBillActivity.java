package com.beihui.market.tang.activity;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.beihui.market.R;
import com.beihui.market.api.Api;
import com.beihui.market.base.BaseComponentActivity;
import com.beihui.market.entity.LoanAccountIconBean;
import com.beihui.market.helper.SlidePanelHelper;
import com.beihui.market.helper.UserHelper;
import com.beihui.market.injection.component.AppComponent;
import com.beihui.market.tang.adapter.LoanBillAdapter;
import com.beihui.market.tang.rx.RxResponse;
import com.beihui.market.tang.rx.observer.ApiObserver;
import com.beihui.market.view.GlideCircleTransform;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;
import com.gyf.barlibrary.ImmersionBar;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.annotations.NonNull;

/**
 * https://gitee.com/tangbuzhi
 *
 * @author: Tangbuzhi
 * @version:
 * @package:
 * @description:
 * @modify:
 * @date: 2018/7/18
 */

public class LoanBillActivity extends BaseComponentActivity {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.rv_fg_account_flow_loan)
    RecyclerView recyclerView;
    @BindView(R.id.tv_custom_loan)
    TextView tv_custom_loan;
    @BindView(R.id.tv_house_name)
    TextView tv_house_name;
    @BindView(R.id.tv_house_remark)
    TextView tv_house_remark;
    @BindView(R.id.iv_house_icon)
    ImageView iv_house_icon;
    @BindView(R.id.tv_car_name)
    TextView tv_car_name;
    @BindView(R.id.tv_car_remark)
    TextView tv_car_remark;
    @BindView(R.id.iv_car_icon)
    ImageView iv_car_icon;

    private LoanBillAdapter loanBillAdapter;
    private Activity activity;
    private LoanAccountIconBean houseBean = null;
    private LoanAccountIconBean carBean = null;
    private LoanAccountIconBean customBean = null;

    @Override
    public int getLayoutId() {
        return R.layout.f_activity_loan_bill;
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
        initRecyclerView();
        Api.getInstance().netIcon()
                .compose(RxResponse.<List<LoanAccountIconBean>>compatT())
                .subscribe(new ApiObserver<List<LoanAccountIconBean>>() {
                    @Override
                    public void onNext(@NonNull List<LoanAccountIconBean> data) {
                        houseBean = data.get(0);
                        tv_house_name.setText(houseBean.iconName);
                        tv_house_remark.setText(houseBean.remark);
                        //Glide.with(activity).load(houseBean.logo).transform(new GlideCircleTransform(activity)).into(iv_house_icon);

                        carBean = data.get(1);
                        tv_car_name.setText(carBean.iconName);
                        tv_car_remark.setText(carBean.remark);
                        //Glide.with(activity).load(carBean.logo).transform(new GlideCircleTransform(activity)).into(iv_car_icon);

                        customBean = data.get(2);
                        tv_custom_loan.setText(customBean.iconName);
                    }
                });
        Api.getInstance().queryLoanAccountIcon(UserHelper.getInstance(this).getProfile().getId())
                .compose(RxResponse.<List<LoanAccountIconBean>>compatT())
                .subscribe(new ApiObserver<List<LoanAccountIconBean>>() {
                    @Override
                    public void onNext(@NonNull List<LoanAccountIconBean> data) {
                        loanBillAdapter.setNewData(data);
                    }
                });
    }

    private void initRecyclerView() {
        loanBillAdapter = new LoanBillAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(loanBillAdapter);
    }

    @Override
    protected void configureComponent(AppComponent appComponent) {
    }

    @OnClick({R.id.tv_custom_loan, R.id.ll_house_loan, R.id.ll_car_loan, R.id.tv_search_others})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_custom_loan:
                Intent customIntent = new Intent(getApplicationContext(), MakeBillActivity.class);
                customIntent.putExtra("type", MakeBillActivity.TYPE_USER_DIFINE);
                if (customBean == null) return;
                customIntent.putExtra("title", customBean.iconName);
                customIntent.putExtra("iconId", customBean.iconId);
                customIntent.putExtra("tallyId", customBean.tallyId);
                startActivity(customIntent);
                break;
            case R.id.ll_house_loan:
                Intent houseIntent = new Intent(getApplicationContext(), MakeBillActivity.class);
                houseIntent.putExtra("type", MakeBillActivity.TYPE_HOUSE_LOAN);
                if (houseBean == null) return;
                houseIntent.putExtra("title", houseBean.iconName);
                houseIntent.putExtra("iconId", houseBean.iconId);
                houseIntent.putExtra("tallyId", houseBean.tallyId);
                startActivity(houseIntent);
                break;
            case R.id.ll_car_loan:
                Intent carIntent = new Intent(getApplicationContext(), MakeBillActivity.class);
                carIntent.putExtra("type", MakeBillActivity.TYPE_CAR_LOAN);
                if (carBean == null) return;
                carIntent.putExtra("title", carBean.iconName);
                carIntent.putExtra("iconId", carBean.iconId);
                carIntent.putExtra("tallyId", carBean.tallyId);
                startActivity(carIntent);
                break;
            case R.id.tv_search_others:
                startActivity(new Intent(this, SearchActivity.class));
                break;
            default:
                break;
        }
    }
}
