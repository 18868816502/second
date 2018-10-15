package com.beiwo.klyjaz.tang.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import com.beiwo.klyjaz.R;
import com.beiwo.klyjaz.api.Api;
import com.beiwo.klyjaz.base.BaseComponentActivity;
import com.beiwo.klyjaz.entity.LoanAccountIconBean;
import com.beiwo.klyjaz.helper.SlidePanelHelper;
import com.beiwo.klyjaz.helper.UserHelper;
import com.beiwo.klyjaz.injection.component.AppComponent;
import com.beiwo.klyjaz.tang.adapter.LoanBillAdapter;
import com.beiwo.klyjaz.tang.rx.RxResponse;
import com.beiwo.klyjaz.tang.rx.observer.ApiObserver;
import com.beiwo.klyjaz.util.InputMethodUtil;
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
    private String searchKey;
    private Handler handler = new Handler();
    private Runnable task = new Runnable() {
        @Override
        public void run() {
            Api.getInstance().queryLoanAccountIcon(UserHelper.getInstance(activity).id(), searchKey)
                    .compose(RxResponse.<List<LoanAccountIconBean>>compatT())
                    .subscribe(new ApiObserver<List<LoanAccountIconBean>>() {
                        @Override
                        public void onNext(@NonNull List<LoanAccountIconBean> data) {
                            if (data != null && data.size() > 0)
                                loanBillAdapter.setNewData(data);
                            else {
                                loanBillAdapter.setNewData(null);
                                loanBillAdapter.setEmptyView(R.layout.f_layout_search_empty, recyclerView);
                            }
                        }

                        @Override
                        public void onError(@NonNull Throwable t) {
                            super.onError(t);
                            loanBillAdapter.setNewData(null);
                            loanBillAdapter.setEmptyView(R.layout.f_layout_search_empty, recyclerView);
                        }
                    });
        }
    };

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
        InputMethodUtil.openSoftKeyboard(this, etSearchKey);
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
                searchKey = TextUtils.isEmpty(s) ? "" : s.toString().trim();
                if (TextUtils.isEmpty(searchKey) || "".equals(searchKey)) {
                    if (handler != null) handler.removeCallbacks(task);
                    return;
                } else {
                    if (handler != null) handler.removeCallbacks(task);
                    handler.postDelayed(task, 1000);
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

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState != RecyclerView.SCROLL_STATE_IDLE) {
                    InputMethodUtil.closeSoftKeyboard(activity);
                }
            }
        });
    }

    @Override
    protected void configureComponent(AppComponent appComponent) {
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
        handler = null;
    }

    @OnClick(R.id.tv_cancel)
    public void onViewClicked() {
        finish();
    }
}
