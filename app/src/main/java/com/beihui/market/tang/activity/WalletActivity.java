package com.beihui.market.tang.activity;

import android.content.Intent;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.beihui.market.R;
import com.beihui.market.api.Api;
import com.beihui.market.base.BaseComponentActivity;
import com.beihui.market.entity.PurseBalance;
import com.beihui.market.helper.SlidePanelHelper;
import com.beihui.market.helper.UserHelper;
import com.beihui.market.injection.component.AppComponent;
import com.beihui.market.tang.Decoration;
import com.beihui.market.tang.adapter.WalletAdapter;
import com.beihui.market.tang.rx.RxResponse;
import com.beihui.market.tang.rx.observer.ApiObserver;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
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

    @Override
    public int getLayoutId() {
        return R.layout.f_activity_wallet;
    }

    @Override
    public void configViews() {
        setupToolbar(mToolbar);
        ImmersionBar.with(this).statusBarDarkFont(true).init();
        SlidePanelHelper.attach(this);
    }

    @Override
    public void initDatas() {
        initRecyclerView();
        if (UserHelper.getInstance(this).isLogin())
            Api.getInstance().purseBalance(UserHelper.getInstance(this).id())
                    .compose(RxResponse.<PurseBalance>compatT())
                    .subscribe(new ApiObserver<PurseBalance>() {
                        @Override
                        public void onNext(@NonNull PurseBalance data) {
                            tv_amount_balance.setText(String.format("%.2f", data.getBalance()));
                            List<PurseBalance.Amount> amountList = data.getAmountList();
                            if (amountList != null && amountList.size() > 0) {
                                amountList.get(0).setSelect(true);
                                adapter.setNewData(amountList);
                            }
                        }
                    });
    }

    private int last = 0;

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
                startActivity(new Intent(this, WithdrawActivity.class));
                break;
            default:
                break;
        }
    }
}