package com.beiwo.klyjaz.loan;


import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.beiwo.klyjaz.App;
import com.beiwo.klyjaz.BuildConfig;
import com.beiwo.klyjaz.R;
import com.beiwo.klyjaz.api.Api;
import com.beiwo.klyjaz.base.BaseComponentFragment;
import com.beiwo.klyjaz.entity.Product;
import com.beiwo.klyjaz.entity.UserProfileAbstract;
import com.beiwo.klyjaz.helper.UserHelper;
import com.beiwo.klyjaz.injection.component.AppComponent;
import com.beiwo.klyjaz.tang.DlgUtil;
import com.beiwo.klyjaz.tang.rx.RxResponse;
import com.beiwo.klyjaz.tang.rx.observer.ApiObserver;
import com.beiwo.klyjaz.ui.activity.MainActivity;
import com.beiwo.klyjaz.ui.activity.WebViewActivity;
import com.beiwo.klyjaz.util.CommonUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
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

    private Activity context;
    private Map<String, Object> map = new HashMap<>();
    private int pageNo = 1;
    private int pageSize = 10;
    private int checking = 0;//是否为审核产品 0：否 1：是
    private int type = 0;//排序 2 贷款额度 3 贷款利率 4 放款速度
    private int moneyType = 0;
    private int borrowingLow = -1;//金额区别 低值
    private int borrowingHigh = -1;//金额区间 高值
    private int productType = 0;//产品属性 1 新品推荐 2 闪电到账 3 大额低息 4 不查征信
    private ProductAdapter adapter = new ProductAdapter();
    private Drawable right;
    private int selectColor;

    @Override
    public int getLayoutResId() {
        return R.layout.fragment_tab_loan;
    }

    @Override
    public void configViews() {
        context = getActivity();
        int statusHeight = CommonUtils.getStatusBarHeight(context);
        ViewGroup.LayoutParams params = hold_view.getLayoutParams();
        params.height = statusHeight;
        hold_view.setLayoutParams(params);
    }

    @Override
    public void initDatas() {
        initFieldVar();

        map.put("pageNo", pageNo);
        map.put("pageSize", pageSize);
        map.put("platform", 1);

        right = ContextCompat.getDrawable(context, R.drawable.loan_selected);
        right.setBounds(0, 0, right.getMinimumWidth(), right.getMinimumHeight());
        selectColor = ContextCompat.getColor(getContext(), R.color.refresh_one);

        initRecycler();
        request(map);
        refresh_layout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@android.support.annotation.NonNull RefreshLayout refreshLayout) {
                initFieldVar();
                map.clear();
                map.put("pageNo", pageNo);
                map.put("pageSize", pageSize);
                map.put("platform", 1);
                request(map);
            }
        });
        refresh_layout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@android.support.annotation.NonNull RefreshLayout refreshLayout) {
                pageNo++;
                map.put("pageNo", pageNo);
                request(map);
            }
        });
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter a, View view, int position) {
                final Product product = adapter.getData().get(position);
                if (BuildConfig.FORCE_LOGIN && !UserHelper.getInstance(context).isLogin()) {
                    DlgUtil.loginDlg(context, new DlgUtil.OnLoginSuccessListener() {
                        @Override
                        public void success(UserProfileAbstract data) {
                            goProduct(product);
                        }
                    });
                } else goProduct(product);
            }
        });
    }

    private void goProduct(final Product product) {
        String id = UserHelper.getInstance(context).isLogin() ? UserHelper.getInstance(context).id() : App.androidId;
        Api.getInstance().queryGroupProductSkip(id, product.getId())
                .compose(RxResponse.<String>compatT())
                .subscribe(new ApiObserver<String>() {
                    @Override
                    public void onNext(@NonNull String data) {
                        Intent intent = new Intent(context, WebViewActivity.class);
                        intent.putExtra("webViewUrl", data);
                        intent.putExtra("webViewTitleName", product.getProductName());
                        startActivity(intent);
                    }
                });
    }

    private void initFieldVar() {
        pageNo = 1;
        type = 0;
        moneyType = 0;
        borrowingLow = -1;
        borrowingHigh = -1;
        productType = 0;
        dtv_money.setText("金额");
        dtv_kind.setText("分类");
        dtv_sort.setText("排序");
        dtv_money.setImg(R.mipmap.ic_down);
        dtv_kind.setImg(R.mipmap.ic_down);
        dtv_sort.setImg(R.mipmap.ic_down);
    }

    private void initRecycler() {
        recycler.setLayoutManager(new LinearLayoutManager(context));
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
        adapter.setEmptyView(R.layout.empty_layout, recycler);
        TextView tv_content = adapter.getEmptyView().findViewById(R.id.tv_content);
        tv_content.setText("服务器开小差，去首页 >");
        tv_content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, MainActivity.class);
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
                dtv_money.setImg(R.mipmap.ic_up);
                dtv_money.setTextHighLight(true);
                PopUtil.pop(context, R.layout.menu_money, dtv_money, new PopUtil.PopViewClickListener() {
                    @Override
                    public void popClick(final PopupWindow popup) {
                        popup.setOnDismissListener(new PopupWindow.OnDismissListener() {
                            @Override
                            public void onDismiss() {
                                dtv_money.setImg(R.mipmap.ic_down);
                                dtv_money.setTextHighLight(false);
                            }
                        });
                        View contentView = popup.getContentView();
                        View.OnClickListener listener = new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (v instanceof TextView) {
                                    TextView textView = (TextView) v;
                                    dtv_money.setText(textView.getText().toString());
                                }
                                popup.dismiss();
                                pageNo = 1;
                                map.put("pageNo", pageNo);
                                switch (v.getId()) {
                                    case R.id.tv_money_all:
                                        moneyType = 0;
                                        borrowingLow = borrowingHigh = -1;
                                        break;
                                    case R.id.tv_money_2k_down:
                                        moneyType = 1;
                                        borrowingLow = -1;
                                        borrowingHigh = 1999;
                                        break;
                                    case R.id.tv_money_2k_5k:
                                        moneyType = 2;
                                        borrowingLow = 2000;
                                        borrowingHigh = 4999;
                                        break;
                                    case R.id.tv_money_5k_1w:
                                        moneyType = 3;
                                        borrowingLow = 5000;
                                        borrowingHigh = 9999;
                                        break;
                                    case R.id.tv_money_1w_3w:
                                        moneyType = 4;
                                        borrowingLow = 10000;
                                        borrowingHigh = 29999;
                                        break;
                                    case R.id.tv_money_3w_above:
                                        moneyType = 5;
                                        borrowingLow = 30000;
                                        borrowingHigh = -1;
                                        break;
                                    default:
                                        popup.dismiss();
                                        break;
                                }
                                if (borrowingLow == -1) map.remove("borrowingLow");
                                else map.put("borrowingLow", borrowingLow);
                                if (borrowingHigh == -1) map.remove("borrowingHigh");
                                else map.put("borrowingHigh", borrowingHigh);
                                if (v.getId() != R.id.view_part) request(map);
                            }
                        };
                        TextView tv_money_all = contentView.findViewById(R.id.tv_money_all);
                        TextView tv_money_2k_down = contentView.findViewById(R.id.tv_money_2k_down);
                        TextView tv_money_2k_5k = contentView.findViewById(R.id.tv_money_2k_5k);
                        TextView tv_money_5k_1w = contentView.findViewById(R.id.tv_money_5k_1w);
                        TextView tv_money_1w_3w = contentView.findViewById(R.id.tv_money_1w_3w);
                        TextView tv_money_3w_above = contentView.findViewById(R.id.tv_money_3w_above);
                        switch (moneyType) {
                            case 1:
                                tv_money_2k_down.setCompoundDrawables(null, null, right, null);
                                tv_money_2k_down.setTextColor(selectColor);
                                break;
                            case 2:
                                tv_money_2k_5k.setCompoundDrawables(null, null, right, null);
                                tv_money_2k_5k.setTextColor(selectColor);
                                break;
                            case 3:
                                tv_money_5k_1w.setCompoundDrawables(null, null, right, null);
                                tv_money_5k_1w.setTextColor(selectColor);
                                break;
                            case 4:
                                tv_money_1w_3w.setCompoundDrawables(null, null, right, null);
                                tv_money_1w_3w.setTextColor(selectColor);
                                break;
                            case 5:
                                tv_money_3w_above.setCompoundDrawables(null, null, right, null);
                                tv_money_3w_above.setTextColor(selectColor);
                                break;
                            default:
                                tv_money_all.setCompoundDrawables(null, null, right, null);
                                tv_money_all.setTextColor(selectColor);
                                break;
                        }
                        tv_money_all.setOnClickListener(listener);
                        tv_money_2k_down.setOnClickListener(listener);
                        tv_money_2k_5k.setOnClickListener(listener);
                        tv_money_5k_1w.setOnClickListener(listener);
                        tv_money_1w_3w.setOnClickListener(listener);
                        tv_money_3w_above.setOnClickListener(listener);
                        contentView.findViewById(R.id.view_part).setOnClickListener(listener);
                    }
                });
                break;
            case R.id.dtv_kind:
                dtv_kind.setImg(R.mipmap.ic_up);
                dtv_kind.setTextHighLight(true);
                PopUtil.pop(context, R.layout.menu_kind, dtv_kind, new PopUtil.PopViewClickListener() {
                    @Override
                    public void popClick(final PopupWindow popup) {
                        popup.setOnDismissListener(new PopupWindow.OnDismissListener() {
                            @Override
                            public void onDismiss() {
                                dtv_kind.setImg(R.mipmap.ic_down);
                                dtv_kind.setTextHighLight(false);
                            }
                        });
                        View contentView = popup.getContentView();
                        View.OnClickListener listener = new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (v instanceof TextView) {
                                    TextView textView = (TextView) v;
                                    dtv_kind.setText(textView.getText().toString());
                                }
                                popup.dismiss();
                                pageNo = 1;
                                map.put("pageNo", pageNo);
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
                                        popup.dismiss();
                                        break;
                                }
                                if (productType == 0) map.remove("productType");
                                else map.put("productType", productType);
                                if (v.getId() != R.id.view_part) request(map);
                            }
                        };
                        TextView tv_kind_1 = contentView.findViewById(R.id.tv_kind_1);
                        TextView tv_kind_2 = contentView.findViewById(R.id.tv_kind_2);
                        TextView tv_kind_3 = contentView.findViewById(R.id.tv_kind_3);
                        TextView tv_kind_4 = contentView.findViewById(R.id.tv_kind_4);
                        TextView tv_kind_5 = contentView.findViewById(R.id.tv_kind_5);
                        switch (productType) {
                            case 1:
                                tv_kind_2.setCompoundDrawables(null, null, right, null);
                                tv_kind_2.setTextColor(selectColor);
                                break;
                            case 2:
                                tv_kind_3.setCompoundDrawables(null, null, right, null);
                                tv_kind_3.setTextColor(selectColor);
                                break;
                            case 3:
                                tv_kind_4.setCompoundDrawables(null, null, right, null);
                                tv_kind_4.setTextColor(selectColor);
                                break;
                            case 4:
                                tv_kind_5.setCompoundDrawables(null, null, right, null);
                                tv_kind_5.setTextColor(selectColor);
                                break;
                            default:
                                tv_kind_1.setCompoundDrawables(null, null, right, null);
                                tv_kind_1.setTextColor(selectColor);
                                break;
                        }
                        tv_kind_1.setOnClickListener(listener);
                        tv_kind_2.setOnClickListener(listener);
                        tv_kind_3.setOnClickListener(listener);
                        tv_kind_4.setOnClickListener(listener);
                        tv_kind_5.setOnClickListener(listener);
                        contentView.findViewById(R.id.view_part).setOnClickListener(listener);
                    }
                });
                break;
            case R.id.dtv_sort:
                dtv_sort.setImg(R.mipmap.ic_up);
                dtv_sort.setTextHighLight(true);
                PopUtil.pop(context, R.layout.menu_sort, dtv_sort, new PopUtil.PopViewClickListener() {
                    @Override
                    public void popClick(final PopupWindow popup) {
                        popup.setOnDismissListener(new PopupWindow.OnDismissListener() {
                            @Override
                            public void onDismiss() {
                                dtv_sort.setImg(R.mipmap.ic_down);
                                dtv_sort.setTextHighLight(false);
                            }
                        });
                        View contentView = popup.getContentView();
                        View.OnClickListener listener = new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (v instanceof TextView) {
                                    TextView textView = (TextView) v;
                                    dtv_sort.setText(textView.getText().toString());
                                }
                                popup.dismiss();
                                pageNo = 1;
                                map.put("pageNo", pageNo);
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
                                        popup.dismiss();
                                        break;
                                }
                                if (type == 0) map.remove("type");
                                else map.put("type", type);
                                if (v.getId() != R.id.view_part) request(map);
                            }
                        };
                        TextView tv_sort_1 = contentView.findViewById(R.id.tv_sort_1);
                        TextView tv_sort_2 = contentView.findViewById(R.id.tv_sort_2);
                        TextView tv_sort_3 = contentView.findViewById(R.id.tv_sort_3);
                        TextView tv_sort_4 = contentView.findViewById(R.id.tv_sort_4);
                        switch (type) {
                            case 2:
                                tv_sort_2.setCompoundDrawables(null, null, right, null);
                                tv_sort_2.setTextColor(selectColor);
                                break;
                            case 3:
                                tv_sort_3.setCompoundDrawables(null, null, right, null);
                                tv_sort_3.setTextColor(selectColor);
                                break;
                            case 4:
                                tv_sort_4.setCompoundDrawables(null, null, right, null);
                                tv_sort_4.setTextColor(selectColor);
                                break;
                            default:
                                tv_sort_1.setCompoundDrawables(null, null, right, null);
                                tv_sort_1.setTextColor(selectColor);
                                break;
                        }
                        tv_sort_1.setOnClickListener(listener);
                        tv_sort_2.setOnClickListener(listener);
                        tv_sort_3.setOnClickListener(listener);
                        tv_sort_4.setOnClickListener(listener);
                        contentView.findViewById(R.id.view_part).setOnClickListener(listener);
                    }
                });
                break;
            default:
                break;
        }
    }
}