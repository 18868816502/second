package com.beiwo.qnejqaz.loan;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.beiwo.qnejqaz.App;
import com.beiwo.qnejqaz.BuildConfig;
import com.beiwo.qnejqaz.R;
import com.beiwo.qnejqaz.api.Api;
import com.beiwo.qnejqaz.entity.Product;
import com.beiwo.qnejqaz.entity.UserProfileAbstract;
import com.beiwo.qnejqaz.helper.UserHelper;
import com.beiwo.qnejqaz.tang.DlgUtil;
import com.beiwo.qnejqaz.tang.rx.RxResponse;
import com.beiwo.qnejqaz.tang.rx.observer.ApiObserver;
import com.beiwo.qnejqaz.ui.activity.MainActivity;
import com.beiwo.qnejqaz.ui.activity.WebViewActivity;
import com.beiwo.qnejqaz.util.DensityUtil;
import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.annotations.NonNull;

/**
 * https://gitee.com/tangbuzhi
 *
 * @author: Tangbuzhi
 * @version:
 * @package:
 * @description:
 * @modify:
 * @date: 2018/10/18
 */

public class ProType1Adapter extends RecyclerView.Adapter<ProType1Adapter.ViewHolder> {

    private static final int TYPE_HEAD = R.layout.type_head;
    private static final int TYPE_NORMAL = R.layout.temlapte_v_linearlayout;
    private static final int TYPE_EMPTY = R.layout.empty_layout_product;

    private List<Product> datas = new ArrayList();
    private int pageType;
    private Activity context;
    private Handler handler = new Handler(Looper.getMainLooper());
    private boolean isExcuted = true;
    private List<List<Product>> data1 = new ArrayList<>();
    private List<List<Product>> data2 = new ArrayList<>();
    private List<RecyclerView> type0 = new ArrayList<>();
    private List<RecyclerView> type1 = new ArrayList<>();
    private List<PopAdapter> adapters0 = new ArrayList<>();
    private List<SubAdatpter> adapters1 = new ArrayList<>();

    public ProType1Adapter(int pageType) {
        this.pageType = pageType;
    }

    public void setData(List<Product> data) {
        if (data == null || data.size() < 1) {
            notifyItemChanged(1);
            return;
        }
        this.datas = data;
        notifyItemChanged(1);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = (Activity) parent.getContext();
        return new ViewHolder(LayoutInflater.from(context).inflate(viewType, parent, false), viewType);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        if (holder.viewType == TYPE_HEAD) {
            holder.iv_head_icon.setImageResource(pageType);
        }
        if (holder.viewType == TYPE_EMPTY) {
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    holder.empty_view.setImageResource(R.mipmap.no_data);
                    holder.tv_content.setText("服务器开小差，去首页 >");
                    holder.tv_content.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(context, MainActivity.class);
                            intent.putExtra("home", true);
                            context.startActivity(intent);
                        }
                    });
                }
            }, 500);
        }
        if (holder.viewType == TYPE_NORMAL) {
            holder.ll_container.removeAllViews();
            type0.clear();
            type1.clear();
            data1.clear();
            data2.clear();
            adapters0.clear();
            adapters1.clear();
            int size = datas.size();
            if (pageType == R.mipmap.ic_head_btn1 || pageType == R.mipmap.ic_head_btn2) {
                for (int i = 1; i < getItemCount(); i++) {
                    if (i % 2 != 0) {
                        int last;
                        if (size > (i - 1) / 2 * 12 + 8) {
                            last = (i - 1) / 2 * 12 + 8;
                        } else {
                            last = size;
                        }
                        data1.add(datas.subList((i - 1) / 2 * 12, last));
                        RecyclerView recycler = new RecyclerView(context);
                        recycler.setBackgroundResource(R.color.white);
                        type0.add(recycler);
                        PopAdapter popAdapter = new PopAdapter();
                        adapters0.add(popAdapter);
                        holder.ll_container.addView(recycler);
                    } else {
                        int last;
                        if (size > 12 + (i - 1) / 2 * 12) {
                            last = 12 + (i - 1) / 2 * 12;
                        } else {
                            last = size;
                        }
                        data2.add(datas.subList(8 + (i - 1) / 2 * 12, last));
                        RecyclerView recycler1 = new RecyclerView(context);
                        recycler1.setBackgroundResource(R.color.white);
                        SubAdatpter subAdatpter = new SubAdatpter();
                        adapters1.add(subAdatpter);
                        type1.add(recycler1);
                        holder.ll_container.addView(recycler1);
                    }
                }

                if (isExcuted) {
                    for (int j = 0; j < type0.size(); j++) {
                        RecyclerView recyclerView = type0.get(j);
                        initRecycler(recyclerView, 4);
                        recyclerView.setAdapter(adapters0.get(j));
                        adapters0.get(j).setNewData(data1.get(j));
                        final int finalJ = j;
                        adapters0.get(j).setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                            @Override
                            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                                int pos = finalJ * 12 + position;
                                Product product = datas.get(pos);
                                productItemClick(product);
                            }
                        });
                    }
                    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    lp.topMargin = DensityUtil.dp2px(context, 8);
                    lp.bottomMargin = DensityUtil.dp2px(context, 8);
                    for (int k = 0; k < type1.size(); k++) {
                        RecyclerView recyclerView = type1.get(k);
                        initRecycler(recyclerView, 2);
                        recyclerView.setLayoutParams(lp);
                        recyclerView.setAdapter(adapters1.get(k));
                        adapters1.get(k).setNewData(data2.get(k));
                        final int finalK = k;
                        adapters1.get(k).setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                            @Override
                            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                                int pos = finalK * 12 + position + 8;
                                Product product = datas.get(pos);
                                productItemClick(product);
                            }
                        });
                    }
                    isExcuted = !isExcuted;
                }
            }
            if (pageType == R.mipmap.ic_head_btn3 || pageType == R.mipmap.ic_head_btn4) {
                for (int i = 1; i < getItemCount(); i++) {
                    if (i % 2 != 0) {
                        int last;
                        if (size > (i - 1) / 2 * 8 + 4) {
                            last = (i - 1) / 2 * 8 + 4;
                        } else {
                            last = size;
                        }
                        data1.add(datas.subList((i - 1) / 2 * 8, last));
                        RecyclerView recycler = new RecyclerView(context);
                        recycler.setBackgroundResource(R.color.white);
                        type0.add(recycler);
                        holder.ll_container.addView(recycler);
                        SubAdatpter subAdatpter = new SubAdatpter();
                        adapters1.add(subAdatpter);
                    } else {
                        int last;
                        if (size > 8 + (i - 1) / 2 * 8) {
                            last = 8 + (i - 1) / 2 * 8;
                        } else {
                            last = size;
                        }
                        data2.add(datas.subList(4 + (i - 1) / 2 * 8, last));
                        RecyclerView recycler1 = new RecyclerView(context);
                        recycler1.setBackgroundResource(R.color.white);
                        type1.add(recycler1);
                        holder.ll_container.addView(recycler1);
                        PopAdapter popAdapter = new PopAdapter();
                        adapters0.add(popAdapter);
                    }
                }

                if (isExcuted) {
                    for (int j = 0; j < type0.size(); j++) {
                        RecyclerView recyclerView = type0.get(j);
                        initRecycler(recyclerView, 2);
                        recyclerView.setAdapter(adapters1.get(j));
                        adapters1.get(j).setNewData(data1.get(j));
                        final int finalK = j;
                        adapters1.get(j).setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                            @Override
                            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                                int pos = finalK * 8 + position;
                                Product product = datas.get(pos);
                                productItemClick(product);
                            }
                        });
                    }
                    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    lp.topMargin = DensityUtil.dp2px(context, 8);
                    lp.bottomMargin = DensityUtil.dp2px(context, 8);
                    for (int k = 0; k < type1.size(); k++) {
                        RecyclerView recyclerView = type1.get(k);
                        initRecycler(recyclerView, 4);
                        recyclerView.setLayoutParams(lp);

                        recyclerView.setAdapter(adapters0.get(k));
                        adapters0.get(k).setNewData(data2.get(k));
                        final int finalJ = k;
                        adapters0.get(k).setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                            @Override
                            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                                int pos = finalJ * 8 + position + 4;
                                Product product = datas.get(pos);
                                productItemClick(product);
                            }
                        });
                    }
                    isExcuted = !isExcuted;
                }
            }
        }
    }

    private void productItemClick(final Product product) {
        if (BuildConfig.FORCE_LOGIN && !UserHelper.getInstance(context).isLogin()) {
            DlgUtil.loginDlg(context, new DlgUtil.OnLoginSuccessListener() {
                @Override
                public void success(UserProfileAbstract data) {
                    goProduct(product);
                }
            });
        } else goProduct(product);
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
                        context.startActivity(intent);
                    }
                });
    }

    private void initRecycler(RecyclerView recycler, int spanSize) {
        recycler.setItemAnimator(new DefaultItemAnimator());
        recycler.setLayoutManager(new GridLayoutManager(context, spanSize) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        if (datas == null || datas.size() < 1) {
            return 2;
        } else {
            if (pageType == R.mipmap.ic_head_btn3 || pageType == R.mipmap.ic_head_btn4) {
                if (datas.size() % 8 > 4) {
                    return datas.size() / 5 * 2 + 1;
                } else {
                    return datas.size() / 8 * 2 + 2;
                }
            }
            if (pageType == R.mipmap.ic_head_btn1 || pageType == R.mipmap.ic_head_btn2) {
                if (datas.size() % 12 > 8) {
                    return datas.size() / 9 * 2 + 1;
                } else {
                    return datas.size() / 12 * 2 + 2;
                }
            }
        }
        return 0;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) return TYPE_HEAD;
        else {
            if (datas == null || datas.size() < 1) return TYPE_EMPTY;
            else return TYPE_NORMAL;
        }
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private int viewType;
        private ImageView iv_head_icon;
        private LinearLayout ll_container;
        private ImageView empty_view;
        private TextView tv_content;

        public ViewHolder(View itemView, int viewType) {
            super(itemView);
            this.viewType = viewType;
            if (viewType == TYPE_HEAD) {
                iv_head_icon = itemView.findViewById(R.id.iv_head_icon);
            }
            if (viewType == TYPE_EMPTY) {
                empty_view = itemView.findViewById(R.id.empty_view);
                tv_content = itemView.findViewById(R.id.tv_content);
            }
            if (viewType == TYPE_NORMAL) {
                ll_container = itemView.findViewById(R.id.ll_container);
            }
        }
    }
}