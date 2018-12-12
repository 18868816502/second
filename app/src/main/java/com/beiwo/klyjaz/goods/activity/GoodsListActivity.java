package com.beiwo.klyjaz.goods.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.beiwo.klyjaz.R;
import com.beiwo.klyjaz.api.Api;
import com.beiwo.klyjaz.api.ResultEntity;
import com.beiwo.klyjaz.base.BaseComponentActivity;
import com.beiwo.klyjaz.entity.GoodsManageBean;
import com.beiwo.klyjaz.goods.adapter.GoodsListAdapter;
import com.beiwo.klyjaz.helper.SlidePanelHelper;
import com.beiwo.klyjaz.util.ParamsUtils;
import com.beiwo.klyjaz.util.RxUtil;
import com.beiwo.klyjaz.util.ToastUtil;
import com.beiwo.klyjaz.view.SearchEditText;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.gyf.barlibrary.ImmersionBar;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import io.reactivex.functions.Consumer;

/**
 * @author: A
 * @version: 1.3.0
 * @package: com.beiwo.klyjaz
 * @description: 产品选择列表
 * @modify:
 * @date: 2018/12/11
 */
public class GoodsListActivity extends BaseComponentActivity {

    @BindView(R.id.tool_bar)
    Toolbar toolbar;
    @BindView(R.id.refresh_layout)
    SmartRefreshLayout refresh_layout;
    @BindView(R.id.et_search)
    SearchEditText etSearch;
    @BindView(R.id.recycler)
    RecyclerView recyclerView;

    private GoodsListAdapter mAdapter;
    private int pageNo = 1;
    private int pageSize = 10;
    private List<GoodsManageBean.RowsBean> list;
    private String manageName = "";

    @Override
    public int getLayoutId() {
        return R.layout.activity_goods_list;
    }

    @Override
    public void configViews() {
        setupToolbar(toolbar);
        ImmersionBar.with(this).statusBarDarkFont(true).init();
        SlidePanelHelper.attach(this);
        mAdapter = new GoodsListAdapter();
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(mAdapter);

        refresh_layout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                pageNo = 1;
                fetchData();
            }
        });
        refresh_layout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                pageNo++;
                if(list.size() >= 10){
                    fetchData();
                }else{
                    refresh_layout.finishLoadMore();
                }
            }
        });

        etSearch.setOnSearchClickListener(new SearchEditText.OnSearchClickListener() {
            @Override
            public void onSearchClick(View view) {
                manageName = etSearch.getText().toString();
                fetchData();
            }
        });
        etSearch.addTextChangedListener(new TextWatcher() {
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
        });
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
                        Intent intent = new Intent(GoodsListActivity.this, GoodsPublishCommentActivity.class);
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

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (null != this.getCurrentFocus()) {
            /**
             * 点击空白位置 隐藏软键盘
             */
            InputMethodManager mInputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            return mInputMethodManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), 0);
        }
        return super.onTouchEvent(event);
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
}
