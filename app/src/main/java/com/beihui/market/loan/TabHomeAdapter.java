package com.beihui.market.loan;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.beihui.market.R;
import com.beihui.market.api.Api;
import com.beihui.market.entity.GroupProductBean;
import com.beihui.market.helper.UserHelper;
import com.beihui.market.tang.rx.RxResponse;
import com.beihui.market.tang.rx.observer.ApiObserver;
import com.beihui.market.ui.activity.UserAuthorizationActivity;
import com.beihui.market.ui.activity.WebViewActivity;
import com.beihui.market.util.DensityUtil;
import com.beihui.market.util.ToastUtil;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.ArrayList;
import java.util.List;

import cn.bingoogolapple.bgabanner.BGABanner;
import io.reactivex.annotations.NonNull;

/**
 * https://gitee.com/tangbuzhi
 *
 * @author: Tangbuzhi
 * @version:
 * @package:
 * @description:
 * @modify:
 * @date: 2018/10/11
 */

public class TabHomeAdapter extends RecyclerView.Adapter<TabHomeAdapter.ViewHolder> implements View.OnClickListener {
    private static final int TYPE_HEADER = R.layout.layout_tab_head;
    private static final int TYPE_NORMAL = R.layout.temlapte_recycler;

    private Activity context;
    private List<String> imgs = new ArrayList<>();
    private List<String> urls = new ArrayList<>();
    private List<String> titles = new ArrayList<>();
    private List<String> looperTexts = new ArrayList<>();
    private RecomProAdapter adapter = new RecomProAdapter();
    private List<GroupProductBean> data = new ArrayList<>();
    private int state;//1 正常状态 2 审核中 3 审核失败

    public void setHeadBanner(List<String> imgs, List<String> urls, List<String> titles) {
        this.imgs = imgs;
        this.urls = urls;
        this.titles = titles;
        notifyItemChanged(0);
    }

    public void setHeadLoopText(List<String> texts) {
        this.looperTexts = texts;
        notifyItemChanged(0);
    }

    public void setState(int state) {
        this.state = state;
        notifyItemChanged(0);
    }

    public void setNormalData(List<GroupProductBean> data) {
        this.data = data;
        notifyItemChanged(1);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = (Activity) parent.getContext();
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false), viewType);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        if (holder.getItemViewType() == TYPE_HEADER) {
            holder.bga_banner.setData(imgs, null);
            holder.bga_banner.setAdapter(new BGABanner.Adapter<ImageView, String>() {
                @Override
                public void fillBannerItem(BGABanner banner, ImageView itemView, @Nullable String model, int position) {
                    Glide.with(context)
                            .load(model)
                            .placeholder(R.color.transparent)
                            .error(R.color.transparent)
                            .fitCenter()
                            .into(itemView);
                }
            });
            holder.bga_banner.setDelegate(new BGABanner.Delegate<ImageView, String>() {
                @Override
                public void onBannerItemClick(BGABanner banner, ImageView itemView, @Nullable String model, int position) {
                    Intent intent = new Intent(context, WebViewActivity.class);
                    intent.putExtra("webViewUrl", urls.get(position));
                    intent.putExtra("webViewTitleName", titles.get(position));
                    context.startActivity(intent);
                }
            });
            holder.tv_pro_1.setOnClickListener(this);
            holder.tv_pro_2.setOnClickListener(this);
            holder.tv_pro_3.setOnClickListener(this);
            holder.tv_pro_4.setOnClickListener(this);
            holder.adt_looper.init(looperTexts, null);
            if (holder.state_container.getChildCount() > 0) {
                holder.state_container.removeAllViews();
            } else {
                if (state == 1)
                    holder.state_container.addView(initState1(R.layout.layout_state_1, 1));
                if (state == 2)
                    holder.state_container.addView(initState1(R.layout.layout_state_2, 2));
                if (state == 3)
                    holder.state_container.addView(initState1(R.layout.layout_state_3, 3));
            }
        }
        if (holder.viewType == TYPE_NORMAL) {
            holder.recycler.setPadding(DensityUtil.dp2px(context, 15f), 0, DensityUtil.dp2px(context, 15f), 0);
            holder.recycler.setLayoutManager(new GridLayoutManager(context, 2) {
                @Override
                public boolean canScrollVertically() {
                    return false;
                }
            });
            holder.recycler.setAdapter(adapter);
            adapter.setNewData(data);
            adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(BaseQuickAdapter a, View view, int position) {
                    final GroupProductBean product = adapter.getData().get(position);
                    if (!UserHelper.getInstance(context).isLogin()) {
                        context.startActivity(new Intent(context, UserAuthorizationActivity.class));
                        return;
                    }
                    Api.getInstance().queryGroupProductSkip(UserHelper.getInstance(context).id(), product.getId())
                            .compose(RxResponse.<String>compatT())
                            .subscribe(new ApiObserver<String>() {
                                @Override
                                public void onNext(@NonNull String data) {
                                    Intent intent = new Intent(context, WebViewActivity.class);
                                    intent.putExtra("webViewUrl", data);
                                    //intent.putExtra("title", ""/*product.getProductName()*/);
                                    intent.putExtra("webViewTitleName", product.getProductName());
                                    context.startActivity(intent);
                                }
                            });
                }
            });
        }
    }

    //state 1
    private TextView tv_seekbar_progress;
    private ImageView iv_edit_money;
    private TextView tv_service_charge;
    private ImageView iv_question;
    private TextView tv_go_loan;
    //state 2
    //state 3

    private View initState1(int layoutRes, int state) {
        View view = LayoutInflater.from(context).inflate(layoutRes, null);
        if (state == 1) {//正常状态
            tv_seekbar_progress = view.findViewById(R.id.tv_seekbar_progress);
            iv_edit_money = view.findViewById(R.id.iv_edit_money);
            tv_service_charge = view.findViewById(R.id.tv_service_charge);
            iv_question = view.findViewById(R.id.iv_question);
            tv_go_loan = view.findViewById(R.id.tv_go_loan);

            iv_edit_money.setOnClickListener(this);
            iv_question.setOnClickListener(this);
            tv_go_loan.setOnClickListener(this);
        }
        if (state == 2) {//审核中

        }
        if (state == 3) {//审核失败

        }
        return view;
    }

    @Override
    public int getItemCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) return TYPE_HEADER;
        else return TYPE_NORMAL;
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent(context, ProTypeActivity.class);
        switch (view.getId()) {
            case R.id.tv_pro_1:
                intent.putExtra("productType", 1);
                context.startActivity(intent);
                break;
            case R.id.tv_pro_2:
                intent.putExtra("productType", 2);
                context.startActivity(intent);
                break;
            case R.id.tv_pro_3:
                intent.putExtra("productType", 4);
                context.startActivity(intent);
                break;
            case R.id.tv_pro_4:
                intent.putExtra("productType", 3);
                context.startActivity(intent);
                break;
            case R.id.iv_edit_money:
                ToastUtil.toast("编辑金钱");
                break;
            case R.id.iv_question:
                ToastUtil.toast("问号");
                break;
            case R.id.tv_go_loan:
                ToastUtil.toast("贷款");
                break;
            default:
                break;
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private int viewType;
        //head
        private BGABanner bga_banner;
        private TextView tv_pro_1;
        private TextView tv_pro_2;
        private TextView tv_pro_3;
        private TextView tv_pro_4;
        private ADTextView adt_looper;
        private FrameLayout state_container;
        //normal
        private RecyclerView recycler;

        public ViewHolder(View itemView, int viewType) {
            super(itemView);
            this.viewType = viewType;
            if (viewType == TYPE_HEADER) {
                bga_banner = itemView.findViewById(R.id.bga_banner);
                tv_pro_1 = itemView.findViewById(R.id.tv_pro_1);
                tv_pro_2 = itemView.findViewById(R.id.tv_pro_2);
                tv_pro_3 = itemView.findViewById(R.id.tv_pro_3);
                tv_pro_4 = itemView.findViewById(R.id.tv_pro_4);
                adt_looper = itemView.findViewById(R.id.adt_looper);
                state_container = itemView.findViewById(R.id.state_container);
            }
            if (viewType == TYPE_NORMAL) {
                recycler = itemView.findViewById(R.id.recycler);
            }
        }
    }
}