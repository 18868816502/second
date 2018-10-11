package com.beihui.market.loan;


import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.beihui.market.App;
import com.beihui.market.R;
import com.beihui.market.api.Api;
import com.beihui.market.base.BaseComponentFragment;
import com.beihui.market.injection.component.AppComponent;
import com.beihui.market.tang.rx.RxResponse;
import com.beihui.market.tang.rx.observer.ApiObserver;
import com.beihui.market.ui.activity.MainActivity;
import com.beihui.market.util.CommonUtils;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
 * @date: 2018/10/9
 */

public class TabLoanFragment extends BaseComponentFragment {
    @BindView(R.id.hold_view)
    View hold_view;
    @BindView(R.id.dtv_money)
    DrawableTextView dtv_money;
    @BindView(R.id.dtv_kind)
    DrawableTextView dtv_kind;
    @BindView(R.id.dtv_sort)
    DrawableTextView dtv_sort;
    @BindView(R.id.refresh_layout)
    SmartRefreshLayout refresh_layout;
    @BindView(R.id.recycler)
    RecyclerView recycler;

    private Map<String, Object> map = new HashMap<>();
    private int pageNo = 1;
    private int pageSize = 10;
    private int checking = 0;//是否为审核产品 0：否 1：是
    private int type = 0;//排序 2 贷款额度 3 贷款利率 4 放款速度
    private int borrowingLow = -1;//金额区别 低值
    private int borrowingHigh = -1;//金额区间 高值
    private int productType = 0;//产品属性 1 高通过率 2 闪电到账 3 大额低息 4 不查征信
    private int tag = 0;//标识第几个tab被选中状态
    private ProductAdapter adapter = new ProductAdapter();

    @Override
    public int getLayoutResId() {
        return R.layout.fragment_tab_loan;
    }

    @Override
    public void configViews() {
        int statusHeight = CommonUtils.getStatusBarHeight(getActivity());
        ViewGroup.LayoutParams params = hold_view.getLayoutParams();
        params.height = statusHeight;
        hold_view.setLayoutParams(params);
    }

    @Override
    public void initDatas() {
        dtv_money.setText("金额");
        dtv_kind.setText("分类");
        dtv_sort.setText("排序");
        dtv_money.setImg(R.drawable.icon_come);
        dtv_kind.setImg(R.drawable.icon_come);
        dtv_sort.setImg(R.drawable.icon_come);

        map.put("pageNo", pageNo);
        map.put("pageSize", pageSize);

        initRecycler();
        request(map);
        refresh_layout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@android.support.annotation.NonNull RefreshLayout refreshLayout) {
                initFieldVar();
                map.clear();
                map.put("pageNo", pageNo);
                map.put("pageSize", pageSize);
                request(map);
            }
        });
        refresh_layout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@android.support.annotation.NonNull RefreshLayout refreshLayout) {
                pageNo++;
                map.clear();
                map.put("pageSize", pageSize);
                map.put("pageNo", pageNo);
                switch (tag) {
                    case 1:
                        if (borrowingLow != -1) map.put("borrowingLow", borrowingLow);
                        if (borrowingHigh != -1) map.put("borrowingHigh", borrowingHigh);
                        break;
                    case 2:
                        if (productType != 0) map.put("productType", productType);
                        break;
                    case 3:
                        if (type != 0) map.put("type", type);
                        break;
                    default:
                        break;
                }
                request(map);
            }
        });
    }

    private void initFieldVar() {
        tag = 0;
        pageNo = 1;
        type = 0;
        borrowingLow = -1;
        borrowingHigh = -1;
        productType = 0;
        dtv_money.setText("金额");
        dtv_kind.setText("分类");
        dtv_sort.setText("排序");
    }

    private void initRecycler() {
        recycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        recycler.setAdapter(adapter);
    }

    private void request(Map<String, Object> map) {
        Api.getInstance().products(map)
                .compose(RxResponse.<List<Product>>compatT())
                .subscribe(new ApiObserver<List<Product>>() {
                    @Override
                    public void onNext(@NonNull List<Product> data) {
                        refresh_layout.finishRefresh();
                        refresh_layout.finishLoadMore();
                        if (pageNo == 1) {
                            if (data == null || data.size() < 1) {
                                empty();
                            } else {
                                adapter.setNewData(data);
                            }
                        } else {
                            adapter.addData(data);
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable t) {
                        super.onError(t);
                        refresh_layout.finishRefresh();
                        refresh_layout.finishLoadMore();
                        empty();
                    }
                });
    }

    private void empty() {
        adapter.setNewData(null);
        adapter.setEmptyView(R.layout.empty_sys_layout, recycler);
        TextView tv_content = adapter.getEmptyView().findViewById(R.id.tv_content);
        tv_content.setText("服务器开小差，去首页 >");
        tv_content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MainActivity.class);
                intent.putExtra("home", true);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void configureComponent(AppComponent appComponent) {
    }

    public static TabLoanFragment newInstance() {
        return new TabLoanFragment();
    }

    @OnClick({R.id.dtv_money, R.id.dtv_kind, R.id.dtv_sort})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.dtv_money:
                PopUtil.pop(getActivity(), R.layout.menu_money, dtv_money, new PopUtil.PopViewClickListener() {
                    @Override
                    public void popClick(final PopupWindow popup) {
                        View contentView = popup.getContentView();
                        View.OnClickListener listener = new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                TextView textView = (TextView) v;
                                dtv_money.setText(textView.getText().toString());
                                dtv_kind.setText("分类");
                                dtv_sort.setText("排序");
                                popup.dismiss();
                                tag = 1;
                                pageNo = 1;
                                map.clear();
                                map.put("pageNo", pageNo);
                                map.put("pageSize", pageSize);
                                switch (v.getId()) {
                                    case R.id.tv_money_all:
                                        borrowingLow = borrowingHigh = -1;
                                        break;
                                    case R.id.tv_money_2k_down:
                                        borrowingLow = -1;
                                        borrowingHigh = 1999;
                                        break;
                                    case R.id.tv_money_2k_5k:
                                        borrowingLow = 2000;
                                        borrowingHigh = 4999;
                                        break;
                                    case R.id.tv_money_5k_1w:
                                        borrowingLow = 5000;
                                        borrowingHigh = 9999;
                                        break;
                                    case R.id.tv_money_1w_3w:
                                        borrowingLow = 10000;
                                        borrowingHigh = 29999;
                                        break;
                                    case R.id.tv_money_3w_above:
                                        borrowingLow = 30000;
                                        borrowingHigh = -1;
                                        break;
                                    default:
                                        break;
                                }
                                if (borrowingLow != -1) map.put("borrowingLow", borrowingLow);
                                if (borrowingHigh != -1) map.put("borrowingHigh", borrowingHigh);
                                request(map);
                            }
                        };
                        contentView.findViewById(R.id.tv_money_all).setOnClickListener(listener);
                        contentView.findViewById(R.id.tv_money_2k_down).setOnClickListener(listener);
                        contentView.findViewById(R.id.tv_money_2k_5k).setOnClickListener(listener);
                        contentView.findViewById(R.id.tv_money_5k_1w).setOnClickListener(listener);
                        contentView.findViewById(R.id.tv_money_1w_3w).setOnClickListener(listener);
                        contentView.findViewById(R.id.tv_money_3w_above).setOnClickListener(listener);
                    }
                });
                break;
            case R.id.dtv_kind:
                PopUtil.pop(getActivity(), R.layout.menu_kind, dtv_kind, new PopUtil.PopViewClickListener() {
                    @Override
                    public void popClick(final PopupWindow popup) {
                        View contentView = popup.getContentView();
                        View.OnClickListener listener = new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                TextView textView = (TextView) v;
                                dtv_kind.setText(textView.getText().toString());
                                dtv_money.setText("金额");
                                dtv_sort.setText("排序");
                                popup.dismiss();
                                tag = 2;
                                pageNo = 1;
                                map.clear();
                                map.put("pageNo", pageNo);
                                map.put("pageSize", pageSize);
                                switch (v.getId()) {
                                    case R.id.tv_kind_1:
                                        productType = 0;
                                        break;
                                    case R.id.tv_kind_2:
                                        productType = 1;
                                        break;
                                    case R.id.tv_kind_3:
                                        productType = 2;
                                        break;
                                    case R.id.tv_kind_4:
                                        productType = 3;
                                        break;
                                    case R.id.tv_kind_5:
                                        productType = 4;
                                        break;
                                    default:
                                        break;
                                }
                                if (productType != 0) map.put("productType", productType);
                                request(map);
                            }
                        };
                        contentView.findViewById(R.id.tv_kind_1).setOnClickListener(listener);
                        contentView.findViewById(R.id.tv_kind_2).setOnClickListener(listener);
                        contentView.findViewById(R.id.tv_kind_3).setOnClickListener(listener);
                        contentView.findViewById(R.id.tv_kind_4).setOnClickListener(listener);
                        contentView.findViewById(R.id.tv_kind_5).setOnClickListener(listener);
                    }
                });
                break;
            case R.id.dtv_sort:
                PopUtil.pop(getActivity(), R.layout.menu_sort, dtv_sort, new PopUtil.PopViewClickListener() {
                    @Override
                    public void popClick(final PopupWindow popup) {
                        View contentView = popup.getContentView();
                        View.OnClickListener listener = new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                TextView textView = (TextView) v;
                                dtv_sort.setText(textView.getText().toString());
                                dtv_money.setText("金额");
                                dtv_kind.setText("分类");
                                popup.dismiss();
                                tag = 3;
                                pageNo = 1;
                                map.clear();
                                map.put("pageNo", pageNo);
                                map.put("pageSize", pageSize);
                                switch (v.getId()) {
                                    case R.id.tv_sort_1:
                                        type = 0;
                                        break;
                                    case R.id.tv_sort_2:
                                        type = 2;
                                        break;
                                    case R.id.tv_sort_3:
                                        type = 3;
                                        break;
                                    case R.id.tv_sort_4:
                                        type = 4;
                                        break;
                                    default:
                                        break;
                                }
                                if (type != 0) map.put("type", type);
                                request(map);
                            }
                        };
                        contentView.findViewById(R.id.tv_sort_1).setOnClickListener(listener);
                        contentView.findViewById(R.id.tv_sort_2).setOnClickListener(listener);
                        contentView.findViewById(R.id.tv_sort_3).setOnClickListener(listener);
                        contentView.findViewById(R.id.tv_sort_4).setOnClickListener(listener);
                    }
                });
                break;
            default:
                break;
        }
    }
}