package com.beiwo.klyjaz.tang.activity;

import android.app.Dialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.beiwo.klyjaz.R;
import com.beiwo.klyjaz.anim.SlideInLeftAnimator;
import com.beiwo.klyjaz.api.Api;
import com.beiwo.klyjaz.base.BaseComponentActivity;
import com.beiwo.klyjaz.entity.CreditBill;
import com.beiwo.klyjaz.entity.CreditCardDebtDetail;
import com.beiwo.klyjaz.helper.SlidePanelHelper;
import com.beiwo.klyjaz.helper.UserHelper;
import com.beiwo.klyjaz.injection.component.AppComponent;
import com.beiwo.klyjaz.tang.DlgUtil;
import com.beiwo.klyjaz.tang.MoxieUtil;
import com.beiwo.klyjaz.tang.StringUtil;
import com.beiwo.klyjaz.tang.adapter.DetailCreditAdapter;
import com.beiwo.klyjaz.tang.rx.RxResponse;
import com.beiwo.klyjaz.tang.rx.observer.ApiObserver;
import com.beiwo.klyjaz.view.pulltoswipe.PulledTabAccountRecyclerView;
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
    @BindView(R.id.tv_toolbar_title)
    TextView tv_toolbar_title;
    @BindView(R.id.recycler)
    PulledTabAccountRecyclerView mRecyclerView;
    @BindView(R.id.tv_update_time)
    TextView tv_update_time;

    private String recordId;
    private String billId;
    private String title;
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
        title = getIntent().getStringExtra("title");
        initRecyclerView();
        request();
    }

    public void request() {
        //信用卡头信息
        Api.getInstance().fetchCreditCardDebtDetail(UserHelper.getInstance(activity).id(), recordId, billId)
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

    private int scrollY;

    private void initRecyclerView() {
        creditAdapter = new DetailCreditAdapter(this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(activity));
        mRecyclerView.setAdapter(creditAdapter);
        mRecyclerView.setItemAnimator(new SlideInLeftAnimator());
        mRecyclerView.setCanPullUp(false);
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                int height = mToolbar.getHeight();
                scrollY += dy;
                if (scrollY > height) {
                    tv_toolbar_title.setText(title);
                } else {
                    tv_toolbar_title.setText("账单详情");
                }
            }
        });
    }

    public PulledTabAccountRecyclerView getRecyclerView() {
        return mRecyclerView;
    }

    @Override
    protected void configureComponent(AppComponent appComponent) {
    }

    @OnClick({R.id.ll_synchronized, R.id.iv_more_setting})
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
            case R.id.ll_synchronized:
                MoxieUtil.sychronized(creditAdapter.getBankCode(), activity);
                break;
            default:
                break;
        }
    }
}