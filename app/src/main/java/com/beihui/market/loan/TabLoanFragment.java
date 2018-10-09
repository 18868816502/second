package com.beihui.market.loan;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.beihui.market.R;
import com.beihui.market.base.BaseComponentFragment;
import com.beihui.market.injection.component.AppComponent;
import com.beihui.market.util.CommonUtils;
import com.beihui.market.util.DensityUtil;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import butterknife.BindView;
import butterknife.OnClick;

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
        initRecycler();
    }

    private void initRecycler() {

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
                                popup.dismiss();
                                switch (v.getId()) {
                                    case R.id.tv_money_all:
                                        break;
                                    case R.id.tv_money_2k_down:
                                        break;
                                    case R.id.tv_money_2k_5k:
                                        break;
                                    case R.id.tv_money_5k_1w:
                                        break;
                                    case R.id.tv_money_1w_3w:
                                        break;
                                    case R.id.tv_money_3w_above:
                                        break;
                                    default:
                                        break;
                                }
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
                                popup.dismiss();
                                switch (v.getId()) {
                                    case R.id.tv_kind_1:
                                        break;
                                    case R.id.tv_kind_2:
                                        break;
                                    case R.id.tv_kind_3:
                                        break;
                                    case R.id.tv_kind_4:
                                        break;
                                    case R.id.tv_kind_5:
                                        break;
                                    default:
                                        break;
                                }
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
                                popup.dismiss();
                                switch (v.getId()) {
                                    case R.id.tv_sort_1:
                                        break;
                                    case R.id.tv_sort_2:
                                        break;
                                    case R.id.tv_sort_3:
                                        break;
                                    case R.id.tv_sort_4:
                                        break;
                                    default:
                                        break;
                                }
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