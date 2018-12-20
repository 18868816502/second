package com.beiwo.qnejqaz.tang.activity;

import android.app.Activity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.beiwo.qnejqaz.R;
import com.beiwo.qnejqaz.api.Api;
import com.beiwo.qnejqaz.base.BaseComponentActivity;
import com.beiwo.qnejqaz.entity.CreditCardBean;
import com.beiwo.qnejqaz.helper.DataHelper;
import com.beiwo.qnejqaz.helper.SlidePanelHelper;
import com.beiwo.qnejqaz.tang.rx.RxResponse;
import com.beiwo.qnejqaz.tang.rx.observer.ApiObserver;
import com.beiwo.qnejqaz.ui.adapter.CreditBillAdapter;
import com.beiwo.qnejqaz.umeng.NewVersionEvents;
import com.gyf.barlibrary.ImmersionBar;

import java.util.List;

import butterknife.BindView;
import io.reactivex.annotations.NonNull;

/**
 * https://gitee.com/tangbuzhi
 *
 * @author: Tangbuzhi
 * @version:
 * @package:
 * @description: 信用卡账单列表
 * @modify:
 * @date: 2018/7/20
 */

public class CreditBillActivity extends BaseComponentActivity {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.rv_fg_account_flow_credit_card)
    RecyclerView recyclerView;
    @BindView(R.id.line)
    View line;

    public CreditBillAdapter mAdapter;
    private Activity activity;

    @Override
    public int getLayoutId() {
        return R.layout.x_fragment_account_flow_credit_card;
    }

    @Override
    public void configViews() {
        setupToolbar(mToolbar);
        ImmersionBar.with(this).statusBarDarkFont(true).init();
        SlidePanelHelper.attach(this);
        activity = this;
        mAdapter = new CreditBillAdapter(activity);
        LinearLayoutManager manager = new LinearLayoutManager(activity);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(mAdapter);
        //pv，uv统计
        DataHelper.getInstance(this).onCountUv(NewVersionEvents.TALLYCREDITCARD);
    }

    @Override
    public void initDatas() {
        Api.getInstance().queryBankList()
                .compose(RxResponse.<List<CreditCardBean>>compatT())
                .subscribe(new ApiObserver<List<CreditCardBean>>() {
                    @Override
                    public void onNext(@NonNull List<CreditCardBean> data) {
                        if (mAdapter != null) {
                            mAdapter.addData(data);
                        }
                    }
                });
    }
}
