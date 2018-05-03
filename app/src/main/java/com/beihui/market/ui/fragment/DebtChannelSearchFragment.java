package com.beihui.market.ui.fragment;


import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;

import com.beihui.market.R;
import com.beihui.market.base.BaseComponentFragment;
import com.beihui.market.entity.DebtChannel;
import com.beihui.market.helper.DataStatisticsHelper;
import com.beihui.market.injection.component.AppComponent;
import com.beihui.market.ui.activity.DebtChannelActivity;
import com.beihui.market.ui.adapter.DebtChannelRVAdapter;
import com.beihui.market.ui.dialog.DebtChannelNewDialog;
import com.beihui.market.ui.presenter.DebtChannelPresenter;
import com.beihui.market.ui.rvdecoration.DebtChannelSearchItemDeco;
import com.beihui.market.util.CommonUtils;
import com.beihui.market.util.InputMethodUtil;
import com.beihui.market.view.StateLayout;
import com.beihui.market.view.stateprovider.DebtChannelSearchStateViewProvider;
import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.List;

import butterknife.BindView;

/**
 * @author xhb
 * 网贷账单 搜索页面
 */
public class DebtChannelSearchFragment extends BaseComponentFragment {

    @BindView(R.id.tool_bar)
    FrameLayout toolBar;
    @BindView(R.id.key)
    EditText keyView;
    @BindView(R.id.clear)
    View clear;
    @BindView(R.id.cancel)
    View cancel;

    @BindView(R.id.dim)
    View dim;
    @BindView(R.id.state_layout)
    StateLayout stateLayout;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    private DebtChannelRVAdapter adapter;

    private DebtChannelPresenter presenter;

    @Override
    public int getLayoutResId() {
        return R.layout.fragment_debt_channel_search;
    }

    @Override
    public void configViews() {
        int statusHeight = CommonUtils.getStatusBarHeight(getContext());
        ViewGroup.LayoutParams lp = toolBar.getLayoutParams();
        lp.height += statusHeight;
        toolBar.setLayoutParams(lp);
        toolBar.setPadding(toolBar.getPaddingLeft(), toolBar.getPaddingTop() + statusHeight, toolBar.getPaddingRight(), toolBar.getPaddingBottom());

        stateLayout.setStateViewProvider(new DebtChannelSearchStateViewProvider(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /**
                 * 埋点 	网贷记账自定义
                 */
                //pv，uv统计
                DataStatisticsHelper.getInstance().onCountUv(DataStatisticsHelper.ID_BILL_NET_LOAN_CUSTOM_ACCOUNT);

                presenter.addDebtChannel();
            }
        }));

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.addItemDecoration(new DebtChannelSearchItemDeco());
        adapter = new DebtChannelRVAdapter(R.layout.list_item_debt_channel);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                presenter.selectSearchDebtChannel(position);
                dismiss();
            }
        });

        View.OnClickListener cancelListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        };
        dim.setOnClickListener(cancelListener);
        cancel.setOnClickListener(cancelListener);

        keyView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    stateLayout.setVisibility(View.VISIBLE);
                    dim.setVisibility(View.GONE);
                }
                presenter.search(s.toString());
            }
        });

        dim.postDelayed(new Runnable() {
            @Override
            public void run() {
                keyView.requestFocus();
                InputMethodUtil.openSoftKeyboard(getContext(), keyView);
            }
        }, 100);
    }

    @Override
    public void initDatas() {

    }

    @Override
    protected void configureComponent(AppComponent appComponent) {

    }

    public void setPresenter(DebtChannelPresenter presenter) {
        this.presenter = presenter;
    }

    public void showSearchResult(List<DebtChannel> list) {
        stateLayout.switchState(StateLayout.STATE_CONTENT);
        if (recyclerView != null) {
            adapter.notifyDebtChannelChanged(list);
        }
    }

    public void showNoSearchResult() {
        stateLayout.switchState(StateLayout.STATE_EMPTY);
    }


    private void dismiss() {
        if (keyView != null) {
            InputMethodUtil.closeSoftKeyboard(getContext(), keyView);
            keyView.setText("");
            ((DebtChannelActivity) getActivity()).dismissSearchFragment();
        }
    }
}
