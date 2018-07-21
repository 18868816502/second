package com.beihui.market.tang.activity;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

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
 * @date: 2018/7/21
 */

public class SearchActivity extends BaseComponentActivity {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.et_search_key)
    EditText etSearchKey;
    @BindView(R.id.recycler)
    RecyclerView recyclerView;

    private LoanBillAdapter loanBillAdapter;
    private Activity activity;

    @Override
    public int getLayoutId() {
        return R.layout.f_activity_search;
    }

    @Override
    public void configViews() {
        activity = this;
        setupToolbar(mToolbar, true);
        ImmersionBar.with(this).statusBarDarkFont(true).init();
        SlidePanelHelper.attach(this);
    }

    @Override
    public void initDatas() {
        initRecyclerView();
        etSearchKey.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s != null && !s.toString().trim().isEmpty()) {
                    Api.getInstance().queryLoanAccountIcon(UserHelper.getInstance(activity).id(), s.toString().trim())
                            .compose(RxResponse.<List<LoanAccountIconBean>>compatT())
                            .subscribe(new ApiObserver<List<LoanAccountIconBean>>() {
                                @Override
                                public void onNext(@NonNull List<LoanAccountIconBean> data) {
                                    loanBillAdapter.setNewData(data);
                                }
                            });
                }
            }
        });
    }

    private void initRecyclerView() {
        loanBillAdapter = new LoanBillAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(loanBillAdapter);

        recyclerView.addOnItemTouchListener(new OnItemChildClickListener() {
            @Override
            public void onSimpleItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                LoanAccountIconBean iconBean = loanBillAdapter.getData().get(position);
                Intent intent = new Intent(getApplicationContext(), MakeBillActivity.class);
                intent.putExtra("type", MakeBillActivity.TYPE_NET_LOAN);
                intent.putExtra("title", iconBean.iconName);
                intent.putExtra("iconId", iconBean.iconId);
                intent.putExtra("tallyId", iconBean.tallyId);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void configureComponent(AppComponent appComponent) {
    }

    @OnClick(R.id.tv_cancel)
    public void onViewClicked() {
        finish();
    }
}
