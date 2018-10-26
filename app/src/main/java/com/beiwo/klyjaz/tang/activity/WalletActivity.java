package com.beiwo.klyjaz.tang.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.beiwo.klyjaz.R;
import com.beiwo.klyjaz.api.Api;
import com.beiwo.klyjaz.base.BaseComponentActivity;
import com.beiwo.klyjaz.entity.PurseBalance;
import com.beiwo.klyjaz.helper.SlidePanelHelper;
import com.beiwo.klyjaz.helper.UserHelper;
import com.beiwo.klyjaz.injection.component.AppComponent;
import com.beiwo.klyjaz.tang.Decoration;
import com.beiwo.klyjaz.tang.adapter.WalletAdapter;
import com.beiwo.klyjaz.tang.rx.RxResponse;
import com.beiwo.klyjaz.tang.rx.observer.ApiObserver;
import com.beiwo.klyjaz.util.ToastUtil;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.gyf.barlibrary.ImmersionBar;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

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
 * @date: 2018/8/16
 */

public class WalletActivity extends BaseComponentActivity {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.recycler)
    RecyclerView recycler;
    @BindView(R.id.tv_amount_balance)
    TextView tv_amount_balance;

    private WalletAdapter adapter = new WalletAdapter();
    private double balance;
    private int last = 0;

    @Override
    public int getLayoutId() {
        return R.layout.f_activity_wallet;
    }

    @Override
    public void configViews() {
        setupToolbar(mToolbar);
        ImmersionBar.with(this).statusBarDarkFont(true).init();
        SlidePanelHelper.attach(this);
        initRecyclerView();
    }

    @Override
    public void initDatas() {
        if (UserHelper.getInstance(this).isLogin())
            Api.getInstance().purseBalance(UserHelper.getInstance(this).id())
                    .compose(RxResponse.<PurseBalance>compatT())
                    .subscribe(new ApiObserver<PurseBalance>() {
                        @Override
                        public void onNext(@NonNull PurseBalance data) {
                            tv_amount_balance.setText(String.format("%.2f", data.getBalance()));
                            balance = data.getBalance();
                            List<PurseBalance.Amount> amountList = data.getAmountList();
                            if (amountList != null && amountList.size() > last) {
                                amountList.get(last).setSelect(true);
                                adapter.setNewData(amountList);
                            }
                        }
                    });
    }

    private void initRecyclerView() {
        recycler.setLayoutManager(new GridLayoutManager(this, 3));
        recycler.setItemAnimator(new DefaultItemAnimator());
        recycler.addItemDecoration(new Decoration(35, 3));
        recycler.setAdapter(adapter);
        recycler.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter a, View view, int position) {
                if (position == last) return;
                adapter.getData().get(position).setSelect(true);
                adapter.getData().get(last).setSelect(false);
                last = position;
                adapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    protected void configureComponent(AppComponent appComponent) {
    }

    @OnClick({R.id.tv_withdraw_record, R.id.tv_withdraw})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_withdraw_record:
                startActivity(new Intent(this, WithdrawRecordActivity.class));
                break;
            case R.id.tv_withdraw:
                if (adapter.getData() == null || adapter.getData().size() < last + 1) return;
                if (balance < adapter.getData().get(last).getAmount()) {
                    ToastUtil.toast("余额不足，再去赚钱吧");
                    return;
                }
                Intent intent = new Intent(this, WithdrawActivity.class);
                intent.putExtra("amount", adapter.getData().get(last).getAmount());
                startActivity(intent);
                break;
            default:
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!EventBus.getDefault().isRegistered(this)) EventBus.getDefault().register(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void reciveMsg(String balance) {
        if (!TextUtils.isEmpty(balance)) {
            //tv_amount_balance.setText(balance);
            initDatas();
        }
    }
}