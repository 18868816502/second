package com.beihui.market.tang.activity;

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

public class NetLoanDetailActivity extends BaseComponentActivity {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.recycler)
    PulledTabAccountRecyclerView mRecyclerView;
    @BindView(R.id.tv_finish_all)
    TextView tv_finish_all;

    private NetLoanDetailActivity activity;
    private String recordId;
    private String billId;
    private DetailItemAdapter itemAdapter;

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
        Api.getInstance().detailHead(UserHelper.getInstance(activity).id(), recordId)
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
        Api.getInstance().detailList(UserHelper.getInstance(activity).id(), recordId, 1, 20)
                .compose(RxResponse.<DetailList>compatT())
                .subscribe(new ApiObserver<DetailList>() {
                    @Override
                    public void onNext(@NonNull DetailList data) {
                        itemAdapter.notifyList(data.getRows());
                    }
                });
    }

    private void initRecyclerView() {
        itemAdapter = new DetailItemAdapter(activity);
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
                ToastUtils.showToast(activity, "更多设置");
                break;
            case R.id.tv_finish_all://结清全部
                Api.getInstance().closeAll(UserHelper.getInstance(activity).id(), recordId)
                        .compose(RxResponse.compatO())
                        .subscribe(new ApiObserver<Object>() {
                            @Override
                            public void onNext(@NonNull Object data) {
                                request();
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
