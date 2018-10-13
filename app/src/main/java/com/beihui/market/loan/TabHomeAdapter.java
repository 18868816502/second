package com.beihui.market.loan;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.beihui.market.App;
import com.beihui.market.R;
import com.beihui.market.api.Api;
import com.beihui.market.entity.GroupProductBean;
import com.beihui.market.helper.UserHelper;
import com.beihui.market.jjd.activity.LoanActivity;
import com.beihui.market.jjd.activity.VerticyIDActivity;
import com.beihui.market.jjd.bean.CashUserInfo;
import com.beihui.market.tang.Decoration;
import com.beihui.market.tang.DlgUtil;
import com.beihui.market.tang.rx.RxResponse;
import com.beihui.market.tang.rx.observer.ApiObserver;
import com.beihui.market.ui.activity.UserAuthorizationActivity;
import com.beihui.market.ui.activity.WebViewActivity;
import com.beihui.market.ui.dialog.CommNoneAndroidDialog;
import com.beihui.market.util.DensityUtil;
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
    private static final int TYPE_FOOT = R.layout.layout_home_footview;

    private Activity context;
    private List<String> imgs = new ArrayList<>();
    private List<String> urls = new ArrayList<>();
    private List<String> titles = new ArrayList<>();
    private List<String> looperTexts = new ArrayList<>();
    private RecomProAdapter adapter = new RecomProAdapter();
    private List<GroupProductBean> data = new ArrayList<>();
    private int state;//1 正常状态 2 审核中 3 审核失败
    private int progress = 500;

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

            if (state == 1)
                holder.state_container.addView(initState1(R.layout.layout_state_1, 1));
            if (state == 2)
                holder.state_container.addView(initState1(R.layout.layout_state_2, 2));
            if (state == 3)
                holder.state_container.addView(initState1(R.layout.layout_state_3, 3));
        }
        if (holder.viewType == TYPE_NORMAL) {
            holder.recycler.setPadding(DensityUtil.dp2px(context, 10f), 0, DensityUtil.dp2px(context, 10f), 0);
            holder.recycler.setItemAnimator(new DefaultItemAnimator());
            holder.recycler.addItemDecoration(new Decoration(20, 2));
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

            tv_seekbar_progress.setText(progress + "");
            float charge = progress / 100;
            SpannableString ss = new SpannableString(String.format("%.2f元", charge));
            ForegroundColorSpan span = new ForegroundColorSpan(ContextCompat.getColor(context, R.color.refresh_one));
            ss.setSpan(span, 0, ss.length() - 1, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            tv_service_charge.setText(ss);

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
        return 3;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) return TYPE_HEADER;
        else if (position == 2) return TYPE_FOOT;
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
                DlgUtil.createDlg(context, R.layout.dlg_set_loan_money, DlgUtil.DlgLocation.BOTTOM, new DlgUtil.OnDlgViewClickListener() {
                    @Override
                    public void onViewClick(final Dialog dialog, final View dlgView) {
                        final SeekBar seekbar = dlgView.findViewById(R.id.seekbar);
                        final TextView tv_seekbar_progress = dlgView.findViewById(R.id.tv_seekbar_progress);
                        //8.0适配
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            seekbar.setMax(2000);
                            seekbar.setMin(0);
                        }
                        seekbar.setProgress((int) ((progress - 500) * 2000 / 1500));

                        tv_seekbar_progress.setText(progress + "");
                        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                            @Override
                            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                                tv_seekbar_progress.setText(seekbarProgress(seekbar) + "");
                            }

                            @Override
                            public void onStartTrackingTouch(SeekBar seekBar) {
                            }

                            @Override
                            public void onStopTrackingTouch(SeekBar seekBar) {
                            }
                        });
                        dlgView.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                            }
                        });
                        dlgView.findViewById(R.id.confirm).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                progress = seekbarProgress(seekbar);
                                notifyItemChanged(0);
                                dialog.dismiss();
                            }
                        });
                    }
                });
                break;
            case R.id.iv_question:
                DlgUtil.createDlg(context, R.layout.f_dlg_apl_fail, new DlgUtil.OnDlgViewClickListener() {
                    @Override
                    public void onViewClick(final Dialog dialog, View dlgView) {
                        TextView content = dlgView.findViewById(R.id.content);
                        TextView title = dlgView.findViewById(R.id.dlg_title);
                        title.setText("提示");
                        content.setText("服务费按日息0.1%收取");
                        dlgView.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                            }
                        });
                    }
                });
                break;
            case R.id.tv_go_loan:
                /*用户认证信息查询*/
                if (UserHelper.getInstance(context).isLogin()) {
                    Api.getInstance().userAuth(UserHelper.getInstance(context).id())
                            .compose(RxResponse.<CashUserInfo>compatT())
                            .subscribe(new ApiObserver<CashUserInfo>() {
                                @Override
                                public void onNext(@NonNull CashUserInfo data) {
                                    Intent verifyIntent = new Intent(context, VerticyIDActivity.class);
                                    if (data == null || data.getCashUser() == null) {
                                        verifyIntent.putExtra("mVertifyState", 1);
                                        context.startActivity(verifyIntent);
                                    } else if (data.getCashContact() == null || App.step == 1) {
                                        verifyIntent.putExtra("mVertifyState", 2);
                                        context.startActivity(verifyIntent);
                                    } else if (App.step == 2) {
                                        verifyIntent.putExtra("mVertifyState", 3);
                                        context.startActivity(verifyIntent);
                                    } else {
                                        Intent intent = new Intent(context, LoanActivity.class);
                                        intent.putExtra("money", progress);
                                        intent.putExtra("charge", progress * 1.0f / 100);
                                        context.startActivity(intent);
                                    }
                                }

                                @Override
                                public void onError(@NonNull Throwable t) {
                                    super.onError(t);
                                    Intent intent = new Intent(context, VerticyIDActivity.class);
                                    intent.putExtra("mVertifyState", 1);
                                    context.startActivity(intent);
                                }
                            });
                } else {
                    context.startActivity(new Intent(context, UserAuthorizationActivity.class));
                }
                break;
            default:
                break;
        }
    }

    private int seekbarProgress(SeekBar seekbar) {
        return 500 + (int) (seekbar.getProgress() * 1500 / 2000 / 100) * 100;
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