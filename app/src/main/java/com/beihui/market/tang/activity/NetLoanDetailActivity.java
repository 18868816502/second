package com.beihui.market.tang.activity;

import android.app.Dialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.beihui.market.R;
import com.beihui.market.api.Api;
import com.beihui.market.base.BaseComponentActivity;
import com.beihui.market.entity.DetailHead;
import com.beihui.market.entity.DetailList;
import com.beihui.market.helper.SlidePanelHelper;
import com.beihui.market.helper.UserHelper;
import com.beihui.market.injection.component.AppComponent;
import com.beihui.market.tang.DlgUtil;
import com.beihui.market.tang.adapter.DetailItemAdapter;
import com.beihui.market.tang.rx.RxResponse;
import com.beihui.market.tang.rx.observer.ApiObserver;
import com.beihui.market.util.ToastUtil;
import com.beihui.market.view.pulltoswipe.PullToRefreshScrollLayout;
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

public class NetLoanDetailActivity extends BaseComponentActivity {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.tv_toolbar_title)
    TextView tv_toolbar_title;
    @BindView(R.id.recycler)
    PulledTabAccountRecyclerView mRecyclerView;
    @BindView(R.id.ll_finish_all)
    LinearLayout ll_finish_all;
    @BindView(R.id.prl_fg_tab_account_list)
    PullToRefreshScrollLayout pullMoreLayout;

    private NetLoanDetailActivity activity;
    private String recordId;
    private String billId;
    private String title;
    private DetailItemAdapter itemAdapter;
    private int page = 1;
    private FrameLayout.LayoutParams params;
    private int bottomMargin;

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
        title = getIntent().getStringExtra("title");
        initRecyclerView();
        request();
    }

    public void request() {
        page = 1;
        Api.getInstance().detailHead(UserHelper.getInstance(activity).id(), recordId)
                .compose(RxResponse.<DetailHead>compatT())
                .subscribe(new ApiObserver<DetailHead>() {
                    @Override
                    public void onNext(@NonNull DetailHead data) {
                        itemAdapter.notifyHead(data);
                        if (data.getStatus() == 2) {
                            ll_finish_all.setVisibility(View.GONE);
                            params.bottomMargin = 0;
                            pullMoreLayout.setLayoutParams(params);
                        } else {
                            ll_finish_all.setVisibility(View.VISIBLE);
                            params.bottomMargin = bottomMargin;
                            pullMoreLayout.setLayoutParams(params);
                        }
                    }
                });
        Api.getInstance().detailList(UserHelper.getInstance(activity).id(), recordId, page, 500)
                .compose(RxResponse.<DetailList>compatT())
                .subscribe(new ApiObserver<DetailList>() {
                    @Override
                    public void onNext(@NonNull DetailList data) {
                        itemAdapter.notifyList(data.getRows());
                    }
                });
    }

    private int scrollY;

    private void initRecyclerView() {
        itemAdapter = new DetailItemAdapter(activity);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(activity));
        mRecyclerView.setAdapter(itemAdapter);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());//SlideInLeftAnimator
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
        params = (FrameLayout.LayoutParams) pullMoreLayout.getLayoutParams();
        bottomMargin = params.bottomMargin;
    }

    @Override
    protected void configureComponent(AppComponent appComponent) {
    }

    @OnClick({R.id.iv_more_setting, R.id.ll_finish_all})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_more_setting://更多设置
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
                                        ToastUtil.toast("修改金额");
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
                                                                Api.getInstance().deleteDebt(UserHelper.getInstance(activity).id(), recordId)
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
                        dlgView.findViewById(R.id.tv_edit_num).setOnClickListener(clickListener);
                        dlgView.findViewById(R.id.tv_delete_bill).setOnClickListener(clickListener);
                        dlgView.findViewById(R.id.cancel).setOnClickListener(clickListener);
                    }
                });
                break;
            case R.id.ll_finish_all://结清全部
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
                                        Api.getInstance().closeAll(UserHelper.getInstance(activity).id(), recordId)
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