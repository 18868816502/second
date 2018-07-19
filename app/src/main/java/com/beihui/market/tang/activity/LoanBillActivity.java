package com.beihui.market.tang.activity;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.beihui.market.R;
import com.beihui.market.base.BaseComponentActivity;
import com.beihui.market.entity.LoanAccountIconBean;
import com.beihui.market.helper.SlidePanelHelper;
import com.beihui.market.helper.UserHelper;
import com.beihui.market.injection.component.AppComponent;
import com.beihui.market.tang.rx.RxResponse;
import com.beihui.market.tang.rx.observer.ApiObserver;
import com.beihui.market.ui.adapter.AccountFlowLoanRvAdapter;
import com.beihui.market.ui.rvdecoration.AccountFlowLoanItemDeco;
import com.beihui.market.ui.rvdecoration.AccountFlowLoanStickyHeaderItemDeco;
import com.beihui.market.util.ToastUtils;
import com.beihui.market.view.AlphabetIndexBar;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.gyf.barlibrary.ImmersionBar;

import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedHashSet;
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
    @BindView(R.id.alphabet_fg_account_flow_loan)
    AlphabetIndexBar alphabetIndexBar;

    private List<LoanAccountIconBean> list = null;
    private AccountFlowLoanRvAdapter adapter;
    private String[] alphabetCountList;

    @Override
    public int getLayoutId() {
        return R.layout.f_activity_loan_bill;
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
    }

    private void initRecyclerView() {
        adapter = new AccountFlowLoanRvAdapter(R.layout.list_item_debt_channel);
        recyclerView.addItemDecoration(new AccountFlowLoanItemDeco());
        recyclerView.addItemDecoration(new AccountFlowLoanStickyHeaderItemDeco(this) {
            @Override
            public String getHeaderName(int pos) {
                return adapter.getItem(pos).iconInitials;
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager manager = (LinearLayoutManager) recyclerView.getLayoutManager();
                int firstVisibleItemPosition = manager.findFirstVisibleItemPosition();
                String iconInitials = list.get(firstVisibleItemPosition).iconInitials;
                for (int i = 0; i < alphabetCountList.length; i++) {
                    if (iconInitials.equals(alphabetCountList[i])) {
                        alphabetIndexBar.selectedIndex = i;
                        alphabetIndexBar.invalidate();
                    }
                }
            }
        });

        alphabetIndexBar.setAlphabetSelectedListener(new AlphabetIndexBar.AlphabetSelectedListener() {
            @Override
            public void onAlphabetSelected(int index, String alphabet) {
                for (int i = 0; i < list.size(); i++) {
                    if (alphabet.equals(list.get(i).iconInitials)) {
                        ((LinearLayoutManager) recyclerView.getLayoutManager()).scrollToPositionWithOffset(i, 0);
                    }
                }
            }
        });

        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter ad, View view, int position) {

            }
        });
    }

    @Override
    protected void configureComponent(AppComponent appComponent) {
        appComponent.getApi().queryLoanAccountIcon(UserHelper.getInstance(this).getProfile().getId())
                .compose(RxResponse.<List<LoanAccountIconBean>>compatT())
                .subscribe(new ApiObserver<List<LoanAccountIconBean>>() {
                    @Override
                    public void onNext(@NonNull List<LoanAccountIconBean> data) {
                        list = data;
                        //网贷列表
                        Collections.sort(data, new Comparator<LoanAccountIconBean>() {
                            @Override
                            public int compare(LoanAccountIconBean one, LoanAccountIconBean two) {
                                return one.iconInitials.compareTo(two.iconInitials);
                            }
                        });
                        adapter.notifyDebtChannelChanged(data);

                        LinkedHashSet<String> set = new LinkedHashSet<>();
                        for (int i = 0; i < data.size(); i++) {
                            set.add(data.get(i).iconInitials);
                        }
                        alphabetCountList = new String[set.size()];
                        Iterator<String> iterator = set.iterator();
                        int i = 0;
                        while (iterator.hasNext()) {
                            alphabetCountList[i] = iterator.next();
                            i++;
                        }
                        alphabetIndexBar.setStringArray(alphabetCountList);
                    }
                });
    }

    @OnClick({R.id.tv_house_loan, R.id.tv_car_loan})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_house_loan:
                ToastUtils.showToast(this, "房贷");
                break;
            case R.id.tv_car_loan:
                ToastUtils.showToast(this, "车贷");
                break;
        }
    }
}
