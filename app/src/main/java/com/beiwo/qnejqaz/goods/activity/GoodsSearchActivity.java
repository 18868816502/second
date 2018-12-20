package com.beiwo.qnejqaz.goods.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.beiwo.qnejqaz.R;
import com.beiwo.qnejqaz.api.Api;
import com.beiwo.qnejqaz.api.ResultEntity;
import com.beiwo.qnejqaz.base.BaseComponentActivity;
import com.beiwo.qnejqaz.entity.GoodsManageBean;
import com.beiwo.qnejqaz.goods.adapter.GoodsListAdapter;
import com.beiwo.qnejqaz.helper.SlidePanelHelper;
import com.beiwo.qnejqaz.util.CommonUtils;
import com.beiwo.qnejqaz.util.ParamsUtils;
import com.beiwo.qnejqaz.util.RxUtil;
import com.beiwo.qnejqaz.util.ToastUtil;
import com.beiwo.qnejqaz.view.RecycleViewDivider;
import com.beiwo.qnejqaz.view.SearchEditText;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.gyf.barlibrary.ImmersionBar;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.functions.Consumer;

public class GoodsSearchActivity extends BaseComponentActivity implements OnRefreshListener, OnLoadMoreListener, TextWatcher {

    @BindView(R.id.hold_view)
    View hold_view;
    @BindView(R.id.refresh_layout)
    SmartRefreshLayout refresh_layout;
    @BindView(R.id.et_search)
    SearchEditText etSearch;
    @BindView(R.id.tv_cancel)
    TextView tvCancel;
    @BindView(R.id.recycler)
    RecyclerView recyclerView;
    @BindView(R.id.empty_container)
    LinearLayout emptyContainer;
    @BindView(R.id.tip_container)
    LinearLayout tipContainer;

    private GoodsListAdapter mAdapter;
    private int pageNo = 1;
    private int pageSize = 10;
    private List<GoodsManageBean.RowsBean> list;
    private String manageName;

    @Override
    public int getLayoutId() {
        return R.layout.activity_goods_search;
    }

    @Override
    public void configViews() {
        int statusHeight = CommonUtils.getStatusBarHeight(this);
        ViewGroup.LayoutParams params = hold_view.getLayoutParams();
        params.height = statusHeight;
        hold_view.setBackgroundResource(R.color.white);
        hold_view.setLayoutParams(params);
        ImmersionBar.with(this).statusBarDarkFont(true).init();
        SlidePanelHelper.attach(this);

        //showSoftInputFromWindow();
        mAdapter = new GoodsListAdapter();
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(mAdapter);

        refresh_layout.setOnRefreshListener(this);
        refresh_layout.setOnLoadMoreListener(this);
        etSearch.addTextChangedListener(this);
    }

    @OnClick({R.id.tv_cancel})
    public void onViewClick(View view) {
        finish();
    }

    @Override
    public void initDatas() {
        list = new ArrayList<>();
        fetchData();
        mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                switch (view.getId()) {
                    case R.id.tv_go_comment:
                        Intent intent = new Intent(GoodsSearchActivity.this, GoodsPublishCommentActivity.class);
                        intent.putExtra("manageId", list.get(position).getManageId());
                        intent.putExtra("logo", list.get(position).getLogo());
                        intent.putExtra("name", list.get(position).getName());
                        intent.putExtra("maxQuota", list.get(position).getMaxQuota());
                        intent.putExtra("term", list.get(position).getTerm());
                        intent.putExtra("rate", list.get(position).getRate());
                        startActivity(intent);
                        break;
                    default:
                        break;
                }
            }
        });
    }

    /**
     * 获取产品数据
     */
    @SuppressLint("CheckResult")
    private void fetchData() {
        Api.getInstance().manageList(ParamsUtils.generateGoodsParams(manageName, pageNo, pageSize))
                .compose(RxUtil.<ResultEntity<GoodsManageBean>>io2main())
                .subscribe(new Consumer<ResultEntity<GoodsManageBean>>() {
                               @Override
                               public void accept(ResultEntity<GoodsManageBean> result) {
                                   refresh_layout.finishRefresh();
                                   refresh_layout.finishLoadMore();
                                   if (result.isSuccess()) {
                                       if (pageNo == 1) {
                                           list.clear();
                                       }
                                       list.addAll(result.getData().getRows());
                                       mAdapter.notifyGoodsChanged(list);
                                       mAdapter.notifyDataSetChanged();
                                       if (list.size() == 0) {
                                           refresh_layout.setVisibility(View.GONE);
                                           emptyContainer.setVisibility(View.VISIBLE);
                                       } else {
                                           refresh_layout.setVisibility(View.VISIBLE);
                                           emptyContainer.setVisibility(View.GONE);
                                       }
                                       if (TextUtils.isEmpty(manageName) || list.size() == 0) {
                                           tipContainer.setVisibility(View.GONE);
                                       } else {
                                           tipContainer.setVisibility(View.VISIBLE);
                                       }
                                   } else {
                                       ToastUtil.toast(result.getMsg());
                                   }
                               }
                           },
                        new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) {
                                ToastUtil.toast(throwable.getMessage());
                            }
                        });
    }

    /*显示输入法*/
    public void showSoftInputFromWindow() {
        etSearch.setFocusable(true);
        etSearch.setFocusableInTouchMode(true);
        etSearch.requestFocus();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
    }

    /**
     * 点击输入法外，隐藏输入法
     *
     * @param ev
     * @return
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            //当isShouldHideInput(v, ev)为true时，表示的是点击输入框区域，则需要显示键盘，同时显示光标，反之，需要隐藏键盘、光标
            if (isShouldHideInput(v, ev)) {

                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    v.clearFocus();
                }
            }
            return super.dispatchTouchEvent(ev);
        }
        // 必不可少，否则所有的组件都不会有TouchEvent了
        if (getWindow().superDispatchTouchEvent(ev)) {
            return true;
        }
        return onTouchEvent(ev);
    }

    public boolean isShouldHideInput(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] leftTop = {0, 0};
            //获取输入框当前的location位置
            v.getLocationInWindow(leftTop);
            int left = leftTop[0];
            int top = leftTop[1];
            int bottom = top + v.getHeight();
            int right = left + v.getWidth();
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                // 点击的是输入框区域，保留点击EditText的事件
                return false;
            } else {
                return true;
            }
        }
        return false;
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        pageNo++;
        if (list.size() >= 10) {
            fetchData();
        } else {
            refresh_layout.finishLoadMore();
        }
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        pageNo = 1;
        fetchData();
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        manageName = s.toString();
        fetchData();
    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}
