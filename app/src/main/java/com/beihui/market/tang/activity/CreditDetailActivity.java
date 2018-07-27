package com.beihui.market.tang.activity;

import android.app.Dialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.beihui.market.R;
import com.beihui.market.anim.SlideInLeftAnimator;
import com.beihui.market.api.Api;
import com.beihui.market.base.BaseComponentActivity;
import com.beihui.market.entity.CreditBill;
import com.beihui.market.entity.CreditCardDebtDetail;
import com.beihui.market.helper.SlidePanelHelper;
import com.beihui.market.helper.UserHelper;
import com.beihui.market.injection.component.AppComponent;
import com.beihui.market.tang.DlgUtil;
import com.beihui.market.tang.MoxieUtil;
import com.beihui.market.tang.StringUtil;
import com.beihui.market.tang.adapter.DetailCreditAdapter;
import com.beihui.market.tang.rx.RxResponse;
import com.beihui.market.tang.rx.observer.ApiObserver;
import com.beihui.market.view.pulltoswipe.PulledTabAccountRecyclerView;
import com.gyf.barlibrary.ImmersionBar;

import org.greenrobot.eventbus.EventBus;

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
 * @date: 2018/7/24
 */

public class CreditDetailActivity extends BaseComponentActivity {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.recycler)
    PulledTabAccountRecyclerView mRecyclerView;
    @BindView(R.id.tv_synchronized)
    TextView tv_synchronized;
    @BindView(R.id.tv_update_time)
    TextView tv_update_time;

    private String recordId;
    private String billId;
    private CreditDetailActivity activity;
    private DetailCreditAdapter creditAdapter;

    @Override
    public int getLayoutId() {
        return R.layout.f_activity_credit_detail;
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
        initRecyclerView();
        request();
    }

    public void request() {
        //信用卡头信息
        Api.getInstance().fetchCreditCardDebtDetail(UserHelper.getInstance(activity).id(), recordId)
                .compose(RxResponse.<CreditCardDebtDetail>compatT())
                .subscribe(new ApiObserver<CreditCardDebtDetail>() {
                    @Override
                    public void onNext(@NonNull CreditCardDebtDetail data) {
                        creditAdapter.notifyHead(data);
                        tv_update_time.setText(StringUtil.time2Str(data.getLastCollectionDate()) + "更新");
                    }
                });
        //账单列表
        Api.getInstance().creditList(UserHelper.getInstance(activity).id(), recordId)
                .compose(RxResponse.<List<CreditBill>>compatT())
                .subscribe(new ApiObserver<List<CreditBill>>() {
                    @Override
                    public void onNext(@NonNull List<CreditBill> data) {
                        creditAdapter.notifyList(data);
                    }
                });
    }

    private void initRecyclerView() {
        creditAdapter = new DetailCreditAdapter(this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(activity));
        mRecyclerView.setAdapter(creditAdapter);
        mRecyclerView.setItemAnimator(new SlideInLeftAnimator());
        mRecyclerView.setCanPullUp(false);
    }

    @Override
    protected void configureComponent(AppComponent appComponent) {
    }

    @OnClick({R.id.tv_synchronized, R.id.iv_more_setting})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_more_setting:
                DlgUtil.createDlg(activity, R.layout.f_dlg_edit_bill, DlgUtil.DlgLocation.BOTTOM, new DlgUtil.OnDlgViewClickListener() {
                    @Override
                    public void onViewClick(final Dialog dialog, View dlgView) {
                        View.OnClickListener clickListener = new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                switch (v.getId()) {
                                    case R.id.cancel:
                                        dialog.dismiss();
                                        break;
                                    case R.id.tv_delete_bill:
                                        dialog.dismiss();
                                        DlgUtil.createDlg(activity, R.layout.f_dlg_delete_bill, new DlgUtil.OnDlgViewClickListener() {
                                            @Override
                                            public void onViewClick(final Dialog dlg, View dlgView) {
                                                View.OnClickListener clickListener1 = new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        switch (v.getId()) {
                                                            case R.id.cancel:
                                                                dlg.dismiss();
                                                                break;
                                                            case R.id.confirm:
                                                                dlg.dismiss();
                                                                Api.getInstance().deleteCredit(UserHelper.getInstance(activity).id(), recordId, 0)
                                                                        .compose(RxResponse.compatO())
                                                                        .subscribe(new ApiObserver<Object>() {
                                                                            @Override
                                                                            public void onNext(@NonNull Object data) {
                                                                                EventBus.getDefault().post("1");
                                                                                setResult(100);
                                                                                finish();
                                                                            }
                                                                        });
                                                                break;
                                                            default:
                                                                break;
                                                        }
                                                    }
                                                };
                                                dlgView.findViewById(R.id.confirm).setOnClickListener(clickListener1);
                                                dlgView.findViewById(R.id.cancel).setOnClickListener(clickListener1);
                                            }
                                        });
                                        break;
                                    default:
                                        break;
                                }
                            }
                        };
                        dlgView.findViewById(R.id.tv_edit_num).setVisibility(View.GONE);
                        dlgView.findViewById(R.id.tv_delete_bill).setOnClickListener(clickListener);
                        dlgView.findViewById(R.id.cancel).setOnClickListener(clickListener);
                    }
                });
                break;
            case R.id.tv_synchronized:
                MoxieUtil.sychronized(creditAdapter.getBankCode(), activity);
                break;
            default:
                break;
        }
    }
}