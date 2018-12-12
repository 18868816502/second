package com.beiwo.klyjaz.goods.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.beiwo.klyjaz.App;
import com.beiwo.klyjaz.BuildConfig;
import com.beiwo.klyjaz.R;
import com.beiwo.klyjaz.api.Api;
import com.beiwo.klyjaz.entity.Goods;
import com.beiwo.klyjaz.entity.HotTop;
import com.beiwo.klyjaz.entity.Product;
import com.beiwo.klyjaz.entity.UserProfileAbstract;
import com.beiwo.klyjaz.goods.activity.GoodsDetailActivity;
import com.beiwo.klyjaz.helper.UserHelper;
import com.beiwo.klyjaz.tang.DlgUtil;
import com.beiwo.klyjaz.tang.rx.RxResponse;
import com.beiwo.klyjaz.tang.rx.observer.ApiObserver;
import com.beiwo.klyjaz.ui.activity.WebViewActivity;
import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * https://gitee.com/tangbuzhi
 *
 * @author: Tangbuzhi
 * @version:
 * @package:
 * @description:
 * @modify:
 * @date: 2018/12/11
 */
public class LoanGoodsAdapter extends RecyclerView.Adapter<LoanGoodsAdapter.ViewHolder> {
    private static final int TYPE_TODAY_RECOM = R.layout.layout_goods_today_recom;//0
    private static final int TYPE_TOP_HOT = R.layout.layout_goods_top_hot;//1
    private static final int TYPE_GOOD_GOODS = R.layout.layout_good_goods;//2

    @Override
    public int getItemCount() {
        return 3;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) return TYPE_TODAY_RECOM;
        else if (position == 1) return TYPE_TOP_HOT;
        else if (position == 2) return TYPE_GOOD_GOODS;
        else return -1;
    }

    private Activity context;
    private List<Product> todayRecomData = new ArrayList<>();
    private List<HotTop> hotTopData = new ArrayList<>();
    private List<Goods> goodsData = new ArrayList<>();
    private TodayRecomAdapter todayRecomAdapter = new TodayRecomAdapter();
    private HotTopAdapter hotTopAdapter = new HotTopAdapter();
    private GoodsItemAdapter goodsItemAdapter = new GoodsItemAdapter();

    public void setTodayRecom(List<Product> data) {
        todayRecomData = data;
        notifyItemChanged(0);
    }

    public void setHotTop(List<HotTop> data) {
        hotTopData = data;
        notifyItemChanged(1);
    }

    public void setGood(List<Goods> data) {
        goodsData = data;
        notifyItemChanged(2);
    }

    public void appendGood(List<Goods> data) {
        goodsData.addAll(data);
        notifyItemChanged(2);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = (Activity) parent.getContext();
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false), viewType);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (holder.viewType == TYPE_TODAY_RECOM) {
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
            linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
            initRecycler(holder.recycler_today_recom, linearLayoutManager, todayRecomAdapter);
            todayRecomAdapter.setNewData(todayRecomData);
            todayRecomAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                    Product product = todayRecomAdapter.getData().get(position);
                    productItemClick(product.getId(), product.getProductName());
                }
            });
        }
        if (holder.viewType == TYPE_TOP_HOT) {
            LinearLayoutManager layoutManager = new LinearLayoutManager(context) {
                @Override
                public boolean canScrollVertically() {
                    return false;
                }
            };
            initRecycler(holder.recycler_hot_top, layoutManager, hotTopAdapter);
            hotTopAdapter.setNewData(hotTopData);
            hotTopAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                    HotTop hotTop = hotTopAdapter.getData().get(position);
                    productItemClick(hotTop.getProductId(), hotTop.getProductName());
                }
            });
        }
        if (holder.viewType == TYPE_GOOD_GOODS) {
            LinearLayoutManager layoutManager = new LinearLayoutManager(context) {
                @Override
                public boolean canScrollVertically() {
                    return false;
                }
            };
            initRecycler(holder.recycler_goods, layoutManager, goodsItemAdapter);
            goodsItemAdapter.setNewData(goodsData);
            goodsItemAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                    Goods goods = goodsItemAdapter.getData().get(position);
                    Intent intent = new Intent(context, GoodsDetailActivity.class);
                    intent.putExtra("cutId", goods.getPraiseCutId());
                    intent.putExtra("manageId", goods.getManageId());
                    context.startActivity(intent);
                }
            });
        }
    }

    private void productItemClick(final String goodId, final String name) {
        if (BuildConfig.FORCE_LOGIN && !UserHelper.getInstance(context).isLogin()) {
            DlgUtil.loginDlg(context, new DlgUtil.OnLoginSuccessListener() {
                @Override
                public void success(UserProfileAbstract data) {
                    goProduct(goodId, name);
                }
            });
        } else goProduct(goodId, name);
    }

    private void goProduct(String goodId, final String name) {
        String id = UserHelper.getInstance(context).isLogin() ? UserHelper.getInstance(context).id() : App.androidId;
        Api.getInstance().queryGroupProductSkip(id, goodId)
                .compose(RxResponse.<String>compatT())
                .subscribe(new ApiObserver<String>() {
                    @Override
                    public void onNext(@NonNull String data) {
                        Intent intent = new Intent(context, WebViewActivity.class);
                        intent.putExtra("webViewUrl", data);
                        intent.putExtra("webViewTitleName", name);
                        context.startActivity(intent);
                    }
                });
    }

    private void initRecycler(RecyclerView recycler, RecyclerView.LayoutManager manager, BaseQuickAdapter adapter) {
        recycler.setHasFixedSize(true);
        recycler.setItemAnimator(new DefaultItemAnimator());
        recycler.setLayoutManager(manager);
        recycler.setFocusableInTouchMode(false);
        recycler.setAdapter(adapter);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private int viewType;
        private RecyclerView recycler_today_recom;
        private RecyclerView recycler_hot_top;
        private RecyclerView recycler_goods;

        public ViewHolder(View itemView, int viewType) {
            super(itemView);
            this.viewType = viewType;
            if (viewType == TYPE_TODAY_RECOM)
                recycler_today_recom = itemView.findViewById(R.id.recycler_today_recom);
            if (viewType == TYPE_TOP_HOT)
                recycler_hot_top = itemView.findViewById(R.id.recycler_hot_top);
            if (viewType == TYPE_GOOD_GOODS)
                recycler_goods = itemView.findViewById(R.id.recycler_goods);
        }
    }
}