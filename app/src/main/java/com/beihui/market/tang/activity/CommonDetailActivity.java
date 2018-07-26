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
import com.beihui.market.entity.DetailHead;
import com.beihui.market.entity.DetailList;
import com.beihui.market.helper.SlidePanelHelper;
import com.beihui.market.helper.UserHelper;
import com.beihui.market.injection.component.AppComponent;
import com.beihui.market.tang.DlgUtil;
import com.beihui.market.tang.adapter.DetailCommonAdapter;
import com.beihui.market.tang.adapter.DetailItemAdapter;
import com.beihui.market.tang.rx.RxResponse;
import com.beihui.market.tang.rx.observer.ApiObserver;
import com.beihui.market.util.ToastUtils;
import com.beihui.market.view.pulltoswipe.PulledTabAccountRecyclerView;
import com.gyf.barlibrary.ImmersionBar;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

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
 * @date: 2018/7/22
 */

public class CommonDetailActivity extends BaseComponentActivity {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.recycler)
    PulledTabAccountRecyclerView mRecyclerView;
    @BindView(R.id.tv_finish_all)
    TextView tv_finish_all;

    private CommonDetailActivity activity;
    private String recordId;
    private String billId;
    private DetailCommonAdapter itemAdapter;

    @Override
    public int getLayoutId() {
        return R.layout.f_activity_bill_detail;
    }

    @Override
    public void configViews() {
        EventBus.getDefault().register(this);
        setupToolbar(mToolbar);
        ImmersionBar.with(this).statusBarDarkFont(true).init();
        SlidePanelHelper.attach(this);
        activity = this;
    }

    @Override
    public void initDatas() {
        recordId = getIntent().getStringExtra("recordId");
        billId = getIntent().getStringExtra("billId");
        request();
        initRecyclerView();
    }

    public void request() {
        //账单详情头
        Api.getInstance().commonDetailHead(UserHelper.getInstance(activity).id(), recordId)
                .compose(RxResponse.<DetailHead>compatT())
                .subscribe(new ApiObserver<DetailHead>() {
                    @Override
                    public void onNext(@NonNull DetailHead data) {
                        itemAdapter.notifyHead(data);
                        if (data.getStatus() == 2) {
                            tv_finish_all.setVisibility(View.GONE);
                        } else {
                            tv_finish_all.setVisibility(View.VISIBLE);
                        }
                    }
                });
        //账单详情列表
        Api.getInstance().commonDetailList(UserHelper.getInstance(activity).id(), recordId, 1, 20)
                .compose(RxResponse.<DetailList>compatT())
                .subscribe(new ApiObserver<DetailList>() {
                    @Override
                    public void onNext(@NonNull DetailList data) {
                        itemAdapter.notifyList(data.getRows());
                    }
                });
    }

    private void initRecyclerView() {
        itemAdapter = new DetailCommonAdapter(activity);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(activity));
        mRecyclerView.setAdapter(itemAdapter);
        mRecyclerView.setItemAnimator(new SlideInLeftAnimator());
        mRecyclerView.setCanPullUp(false);
    }

    @Override
    protected void configureComponent(AppComponent appComponent) {
    }

    @OnClick({R.id.iv_more_setting, R.id.tv_finish_all})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_more_setting://更多设置
                /*if (itemAdapter.getBillStatus()) {
                    ToastUtils.showToast(activity, "账单已结清，无需更多操作");
                    return;
                }*/
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
                                    case R.id.tv_edit_num:
                                        dialog.dismiss();
                                        ToastUtils.showToast(activity, "修改金额");
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
                                                                Api.getInstance().deleteFastDebt(UserHelper.getInstance(activity).id(), recordId)
                                                                        .compose(RxResponse.compatO())
                                                                        .subscribe(new ApiObserver<Object>() {
                                                                            @Override
                                                                            public void onNext(@NonNull Object data) {
                                                                                EventBus.getDefault().post("1");
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
                        dlgView.findViewById(R.id.tv_edit_num).setOnClickListener(clickListener);
                        dlgView.findViewById(R.id.tv_delete_bill).setOnClickListener(clickListener);
                        dlgView.findViewById(R.id.cancel).setOnClickListener(clickListener);
                    }
                });
                break;
            case R.id.tv_finish_all://结清全部
                DlgUtil.createDlg(activity, R.layout.f_dlg_close_all, new DlgUtil.OnDlgViewClickListener() {
                    @Override
                    public void onViewClick(final Dialog dialog, View dlgView) {
                        View.OnClickListener clickListener = new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                switch (v.getId()) {
                                    case R.id.cancel:
                                        dialog.dismiss();
                                        break;
                                    case R.id.confirm:
                                        dialog.dismiss();
                                        Api.getInstance().commonCloseAll(UserHelper.getInstance(activity).id(), recordId)
                                                .compose(RxResponse.compatO())
                                                .subscribe(new ApiObserver<Object>() {
                                                    @Override
                                                    public void onNext(@NonNull Object data) {
                                                        EventBus.getDefault().post("1");
                                                        request();
                                                    }
                                                });
                                        break;
                                    default:
                                        break;
                                }
                            }
                        };
                        dlgView.findViewById(R.id.confirm).setOnClickListener(clickListener);
                        dlgView.findViewById(R.id.cancel).setOnClickListener(clickListener);
                    }
                });
                break;
            default:
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void recieveMsg(String remark) {
        if (remark != null && !remark.isEmpty()) {
            itemAdapter.notifyRemark(remark);
        }
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }
}