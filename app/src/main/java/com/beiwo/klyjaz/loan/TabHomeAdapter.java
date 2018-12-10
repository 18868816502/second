package com.beiwo.klyjaz.loan;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.beiwo.klyjaz.App;
import com.beiwo.klyjaz.BuildConfig;
import com.beiwo.klyjaz.R;
import com.beiwo.klyjaz.api.Api;
import com.beiwo.klyjaz.api.NetConstants;
import com.beiwo.klyjaz.entity.Product;
import com.beiwo.klyjaz.entity.UserProfileAbstract;
import com.beiwo.klyjaz.helper.DataStatisticsHelper;
import com.beiwo.klyjaz.helper.UserHelper;
import com.beiwo.klyjaz.jjd.activity.LoanActivity;
import com.beiwo.klyjaz.jjd.activity.VerticyIDActivity;
import com.beiwo.klyjaz.jjd.bean.CashUserInfo;
import com.beiwo.klyjaz.social.activity.ForumDetailActivity;
import com.beiwo.klyjaz.social.activity.TopicDetailActivity;
import com.beiwo.klyjaz.social.bean.ForumBean;
import com.beiwo.klyjaz.social.bean.IndexForum;
import com.beiwo.klyjaz.tang.Decoration;
import com.beiwo.klyjaz.tang.DlgUtil;
import com.beiwo.klyjaz.tang.RoundCornerTransformation;
import com.beiwo.klyjaz.tang.StringUtil;
import com.beiwo.klyjaz.tang.rx.RxResponse;
import com.beiwo.klyjaz.tang.rx.observer.ApiObserver;
import com.beiwo.klyjaz.ui.activity.WebViewActivity;
import com.beiwo.klyjaz.umeng.NewVersionEvents;
import com.beiwo.klyjaz.util.DensityUtil;
import com.beiwo.klyjaz.util.FormatNumberUtils;
import com.beiwo.klyjaz.util.SPUtils;
import com.beiwo.klyjaz.view.BannerLayout;
import com.beiwo.klyjaz.view.GlideCircleTransform;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
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
 * @date: 2018/10/11
 */

public class TabHomeAdapter extends RecyclerView.Adapter<TabHomeAdapter.ViewHolder> implements View.OnClickListener {
    private static final int TYPE_HEADER = R.layout.layout_tab_head;//0
    private static final int TYPE_HEADER1 = R.layout.layout_tab_head1;//1
    private static final int TYPE_GOODS = R.layout.layout_home_hot;//2
    private static final int TYPE_TOPIC = R.layout.layout_home_topic;//3
    private static final int TYPE_NORMAL = R.layout.temlapte_recycler;//4
    private static final int TYPE_FOOT = R.layout.layout_home_footview;//5

    @Override
    public int getItemCount() {
        return 6;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) return TYPE_HEADER;
        else if (position == 1) return TYPE_HEADER1;
        else if (position == 2) return TYPE_GOODS;
        else if (position == 3) return TYPE_TOPIC;
        else if (position == 5) return TYPE_FOOT;
        else return TYPE_NORMAL;
    }

    private Activity context;
    private List<String> imgs = new ArrayList<>();
    private List<String> urls = new ArrayList<>();
    private List<String> titles = new ArrayList<>();
    private List<Boolean> needLogin = new ArrayList<>();
    private List<String> looperTexts = new ArrayList<>();
    private RecomProAdapter adapter = new RecomProAdapter();
    private List<Product> data = new ArrayList<>();
    private int state = 0;//1 正常状态 2 审核中 3 审核失败
    private String auditDate;
    private String overDate;
    private Handler handler = new Handler(Looper.getMainLooper());
    private long currentMillSecond = 0;//当前毫秒数
    private Runnable timeRunable = new Runnable() {
        @Override
        public void run() {
            currentMillSecond = currentMillSecond - 1000;
            if (currentMillSecond / 1000 <= 0) {
                if (state == 3) setStateNormal();//审核失败 => 普通状态
                if (state == 2) setStateFail(overDate);//审核中 => 审核失败
                return;
            }
            if (state == 3) tv_time_counter.setText(StringUtil.getFormatHMS(currentMillSecond));
            handler.postDelayed(this, 1000);
        }
    };
    private int progress = 2000;
    private PopAdapter popAdapter = new PopAdapter();
    private IndexForum indexForum = null;
    private List<Product> hotGoods = new ArrayList<>();

    public void setHeadBanner(List<String> imgs, List<String> urls, List<String> titles, List<Boolean> needLogin) {
        this.imgs = imgs;
        this.urls = urls;
        this.titles = titles;
        this.needLogin = needLogin;
        notifyItemChanged(0);
    }

    public void setHeadLoopText(List<String> texts) {
        this.looperTexts = texts;
        notifyItemChanged(0);
    }

    public void setStateNormal() {
        this.state = 1;
        SPUtils.setValue("checking", "false");
        notifyItemChanged(1);
    }

    public void setStateChecking(String auditDate, String overDate) {
        this.state = 2;
        this.auditDate = auditDate;
        this.overDate = overDate;
        notifyItemChanged(1);
    }

    public void setStateFail(String overDate) {
        this.state = 3;
        this.overDate = overDate;
        notifyItemChanged(1);
    }

    public void setHotGoodsData(List<Product> data) {
        hotGoods = data;
        notifyItemChanged(2);
    }

    public void setTopic(IndexForum data) {
        this.indexForum = data;
        notifyItemChanged(3);
    }

    public void setNormalData(List<Product> data) {
        this.data = data;
        notifyItemChanged(4);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = (Activity) parent.getContext();
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false), viewType);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        if (holder.getItemViewType() == TYPE_HEADER) {
            holder.banner_layout.setImageLoader(new BannerLayout.ImageLoader() {
                @Override
                public void displayImage(Context context, String path, ImageView imageView) {
                    Glide.with(context).load(path).placeholder(R.drawable.no_banner).error(R.drawable.no_banner).into(imageView);
                }
            });
            if (imgs != null && imgs.size() > 0) holder.banner_layout.setViewUrls(imgs);
            holder.banner_layout.setOnBannerItemClickListener(new BannerLayout.OnBannerItemClickListener() {
                @Override
                public void onItemClick(int position) {
                    if (needLogin.get(position) && !UserHelper.getInstance(context).isLogin()) {
                        DlgUtil.loginDlg(context, null);
                    } else {
                        if (!TextUtils.isEmpty(urls.get(position))) {
                            Intent intent = new Intent(context, WebViewActivity.class);
                            intent.putExtra("webViewUrl", urls.get(position));
                            intent.putExtra("webViewTitleName", titles.get(position));
                            context.startActivity(intent);
                        }
                    }
                }
            });
            holder.tv_pro_1.setOnClickListener(this);
            holder.tv_pro_2.setOnClickListener(this);
            holder.tv_pro_3.setOnClickListener(this);
            holder.tv_pro_4.setOnClickListener(this);
            holder.adt_looper.init(looperTexts, null);
        }
        if (holder.viewType == TYPE_HEADER1) {
            if (holder.state_container.getChildCount() > 0) holder.state_container.removeAllViews();
            if (state == 1)
                holder.state_container.addView(initState1(R.layout.layout_state_1, 1));
            if (state == 2) {
                holder.state_container.addView(initState1(R.layout.layout_state_2, 2));
                if (!TextUtils.equals("true", SPUtils.getValue("checking"))) {
                    Api.getInstance().queryGroupProductList(NetConstants.SECOND_PRODUCT_CHECKING1)
                            .compose(RxResponse.<List<Product>>compatT())
                            .subscribe(new ApiObserver<List<Product>>() {
                                @Override
                                public void onNext(@NonNull List<Product> data) {
                                    popAdapter.setNewData(data);
                                }
                            });
                    DlgUtil.createDlg(context, R.layout.dlg_checking, new DlgUtil.OnDlgViewClickListener() {
                        @Override
                        public void onViewClick(final Dialog dialog, View dlgView) {
                            dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                                @Override
                                public void onDismiss(DialogInterface dialog) {
                                    SPUtils.setValue("checking", "true");
                                }
                            });
                            RecyclerView recycler = dlgView.findViewById(R.id.recycler);
                            recycler.setLayoutManager(new GridLayoutManager(context, 3) {
                                @Override
                                public boolean canScrollVertically() {
                                    return false;
                                }
                            });
                            recycler.setAdapter(popAdapter);
                            DlgUtil.cancelClick(dialog, dlgView);
                            popAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                                @Override
                                public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                                    productItemClick(popAdapter.getData().get(position));
                                    dialog.dismiss();
                                }
                            });
                        }
                    });
                    long nowStamp = System.currentTimeMillis();
                    currentMillSecond = StringUtil.timeGapSecond(auditDate, StringUtil.stamp2Str(nowStamp)) * 1000;
                    handler.removeCallbacksAndMessages(null);
                    handler.post(timeRunable);
                }
            }
            if (state == 3) {
                holder.state_container.addView(initState1(R.layout.layout_state_3, 3));
                long nowStamp = System.currentTimeMillis();
                currentMillSecond = StringUtil.timeGapSecond(overDate, StringUtil.stamp2Str(nowStamp)) * 1000;
                handler.removeCallbacksAndMessages(null);
                handler.post(timeRunable);
            }
        }
        if (holder.viewType == TYPE_TOPIC) {
            if (indexForum != null) {
                final ForumBean topic = indexForum.getTopic();
                if (topic != null) {
                    Glide.with(context)
                            .load(topic.getImgUrl())
                            .bitmapTransform(new CenterCrop(context), new RoundCornerTransformation(context, 6, RoundCornerTransformation.CornerType.ALL))
                            .error(R.mipmap.ic_launcher)
                            .into(holder.iv_topic_img);
                    holder.tv_topic_title.setText(topic.getTopicTitle());
                    holder.tv_topic_content.setText(topic.getTopicContent());
                    holder.tv_topic_num.setText(String.format(context.getString(R.string.topic_num_look), topic.getOnLookCount()));
                    List<String> headUrls = topic.getTopicUserHeadUrl();
                    if (headUrls != null) {
                        if (headUrls.size() > 2) {
                            holder.iv_topic_icon0.setVisibility(View.VISIBLE);
                            holder.iv_topic_icon1.setVisibility(View.VISIBLE);
                            holder.iv_topic_icon2.setVisibility(View.VISIBLE);
                            Glide.with(context)
                                    .load(headUrls.get(0))
                                    .centerCrop()
                                    .transform(new GlideCircleTransform(context))
                                    .error(R.drawable.mine_icon_head)
                                    .into(holder.iv_topic_icon0);
                            Glide.with(context)
                                    .load(headUrls.get(1))
                                    .centerCrop()
                                    .transform(new GlideCircleTransform(context))
                                    .error(R.drawable.mine_icon_head)
                                    .into(holder.iv_topic_icon1);
                            Glide.with(context)
                                    .load(headUrls.get(2))
                                    .centerCrop()
                                    .transform(new GlideCircleTransform(context))
                                    .error(R.drawable.mine_icon_head)
                                    .into(holder.iv_topic_icon2);
                        } else if (headUrls.size() > 1) {
                            holder.iv_topic_icon0.setVisibility(View.VISIBLE);
                            holder.iv_topic_icon1.setVisibility(View.VISIBLE);
                            holder.iv_topic_icon2.setVisibility(View.GONE);
                            Glide.with(context)
                                    .load(headUrls.get(0))
                                    .centerCrop()
                                    .transform(new GlideCircleTransform(context))
                                    .error(R.drawable.mine_icon_head)
                                    .into(holder.iv_topic_icon0);
                            Glide.with(context)
                                    .load(headUrls.get(1))
                                    .centerCrop()
                                    .transform(new GlideCircleTransform(context))
                                    .error(R.drawable.mine_icon_head)
                                    .into(holder.iv_topic_icon1);
                        } else if (headUrls.size() > 0) {
                            holder.iv_topic_icon0.setVisibility(View.VISIBLE);
                            holder.iv_topic_icon1.setVisibility(View.GONE);
                            holder.iv_topic_icon2.setVisibility(View.GONE);
                            Glide.with(context)
                                    .load(headUrls.get(0))
                                    .centerCrop()
                                    .transform(new GlideCircleTransform(context))
                                    .error(R.drawable.mine_icon_head)
                                    .into(holder.iv_topic_icon0);
                        } else {
                            holder.iv_topic_icon0.setVisibility(View.GONE);
                            holder.iv_topic_icon1.setVisibility(View.GONE);
                            holder.iv_topic_icon2.setVisibility(View.GONE);
                        }
                    }
                    holder.csl_topic_wrap.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            DataStatisticsHelper.getInstance(context).onCountUvPv("CommunityTopicHit", topic.getTopicId());
                            Intent intent = new Intent(context, TopicDetailActivity.class);
                            intent.putExtra("topicId", topic.getTopicId());
                            context.startActivity(intent);
                        }
                    });
                }
                final ForumBean forum = indexForum.getForum();
                if (forum != null) {
                    Glide.with(context)
                            .load(forum.getUserHeadUrl())
                            .centerCrop()
                            .transform(new GlideCircleTransform(context))
                            .error(R.drawable.mine_icon_head)
                            .into(holder.iv_forum_icon);
                    holder.tv_forum_name.setText(forum.getUserName());
                    holder.tv_forum_time.setText(StringUtil.time2Str(forum.getGmtCreate()));
                    holder.tv_forum_title.setText(forum.getTitle());
                    holder.tv_forum_content.setText(forum.getContent());
                    holder.tv_forum_praise_num.setText(forum.getPraiseCount() + "赞");
                    holder.csl_forum_wrap.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            DataStatisticsHelper.getInstance(context).onCountUvPv(NewVersionEvents.COMMUNITY_FORUM_HIT, forum.getForumId());
                            Intent intent = new Intent(context, ForumDetailActivity.class);
                            intent.putExtra("forumId", forum.getForumId());
                            intent.putExtra("userId", UserHelper.getInstance(context).isLogin() ? UserHelper.getInstance(context).id() : "");
                            context.startActivity(intent);
                        }
                    });
                }
            }
        }
        if (holder.viewType == TYPE_NORMAL) {
            if (holder.recycler.getPaddingLeft() <= 0) {
                holder.recycler.setPadding(DensityUtil.dp2px(context, 10f), 0, DensityUtil.dp2px(context, 10f), 0);
                holder.recycler.addItemDecoration(new Decoration(20, 2));
            }
            holder.recycler.setFocusableInTouchMode(false);
            holder.recycler.setItemAnimator(new DefaultItemAnimator());
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
                    Product product = adapter.getData().get(position);
                    productItemClick(product);
                }
            });
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

    //state 1
    private TextView tv_seekbar_progress;
    private ImageView iv_edit_money;
    private ImageView iv_question;
    private TextView tv_go_loan;
    //state 2
    //state 3
    private TextView tv_time_counter;

    private View initState1(int layoutRes, int state) {
        View view = LayoutInflater.from(context).inflate(layoutRes, null);
        if (state == 1) {//正常状态
            tv_seekbar_progress = view.findViewById(R.id.tv_seekbar_progress);
            iv_edit_money = view.findViewById(R.id.iv_edit_money);
            iv_question = view.findViewById(R.id.iv_question);
            tv_go_loan = view.findViewById(R.id.tv_go_loan);

            tv_seekbar_progress.setText(FormatNumberUtils.FormatNumberFor0(progress));

            iv_edit_money.setOnClickListener(this);
            iv_question.setOnClickListener(this);
            tv_go_loan.setOnClickListener(this);
        }
        if (state == 3) {//审核失败
            tv_time_counter = view.findViewById(R.id.tv_time_counter);
        }
        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_pro_1:
                Intent intent1 = new Intent(context, ProTypeActivity.class);
                intent1.putExtra("productType", 1);
                context.startActivity(intent1);
                break;
            case R.id.tv_pro_2:
                Intent intent2 = new Intent(context, ProTypeActivity.class);
                intent2.putExtra("productType", 2);
                context.startActivity(intent2);
                break;
            case R.id.tv_pro_3:
                Intent intent4 = new Intent(context, ProTypeActivity.class);
                intent4.putExtra("productType", 4);
                context.startActivity(intent4);
                break;
            case R.id.tv_pro_4:
                Intent intent3 = new Intent(context, ProTypeActivity.class);
                intent3.putExtra("productType", 3);
                context.startActivity(intent3);
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
                        seekbar.setProgress(((progress - 500) * 2000 / 1500));

                        tv_seekbar_progress.setText(FormatNumberUtils.FormatNumberFor0(progress));
                        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                            @Override
                            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                                tv_seekbar_progress.setText(FormatNumberUtils.FormatNumberFor0(seekbarProgress(seekbar)));
                            }

                            @Override
                            public void onStartTrackingTouch(SeekBar seekBar) {
                            }

                            @Override
                            public void onStopTrackingTouch(SeekBar seekBar) {
                            }
                        });
                        DlgUtil.cancelClick(dialog, dlgView);
                        dlgView.findViewById(R.id.confirm).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                progress = seekbarProgress(seekbar);
                                notifyItemChanged(1);
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
                if (!UserHelper.getInstance(context).isLogin()) {
                    DlgUtil.loginDlg(context, null);
                    return;
                }
                DataStatisticsHelper.getInstance(context).onCountUv("HPLoanImmediately");
                Api.getInstance().userAuth(UserHelper.getInstance(context).id())
                        .compose(RxResponse.<CashUserInfo>compatT())
                        .subscribe(new ApiObserver<CashUserInfo>() {
                            @Override
                            public void onNext(@NonNull CashUserInfo data) {
                                Intent verifyIntent = new Intent(context, VerticyIDActivity.class);
                                verifyIntent.putExtra("money", progress);
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
                break;
            default:
                break;
        }
    }

    private int seekbarProgress(SeekBar seekbar) {
        return 500 + (seekbar.getProgress() * 1500 / 2000 / 100) * 100;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private int viewType;
        //head
        private BannerLayout banner_layout;
        private TextView tv_pro_1;
        private TextView tv_pro_2;
        private TextView tv_pro_3;
        private TextView tv_pro_4;
        private ADTextView adt_looper;
        private FrameLayout state_container;
        //hot
        private LinearLayout ll_hot_head;
        private RecyclerView hot_recycler;
        //topic
        private ConstraintLayout csl_topic_wrap, csl_forum_wrap;
        private ImageView iv_topic_img;
        private TextView tv_topic_title;
        private TextView tv_topic_content;
        private ImageView iv_topic_icon0, iv_topic_icon1, iv_topic_icon2;
        private TextView tv_topic_num;
        private ImageView iv_forum_icon;
        private TextView tv_forum_name;
        private TextView tv_forum_time;
        private TextView tv_forum_title;
        private TextView tv_forum_content;
        private TextView tv_forum_praise_num;
        //normal
        private RecyclerView recycler;

        public ViewHolder(View itemView, int viewType) {
            super(itemView);
            this.viewType = viewType;
            if (viewType == TYPE_HEADER) {
                banner_layout = itemView.findViewById(R.id.banner_layout);
                tv_pro_1 = itemView.findViewById(R.id.tv_pro_1);
                tv_pro_2 = itemView.findViewById(R.id.tv_pro_2);
                tv_pro_3 = itemView.findViewById(R.id.tv_pro_3);
                tv_pro_4 = itemView.findViewById(R.id.tv_pro_4);
                adt_looper = itemView.findViewById(R.id.adt_looper);
            }
            if (viewType == TYPE_HEADER1) {
                state_container = itemView.findViewById(R.id.state_container);
            }
            if (viewType == TYPE_GOODS) {
                ll_hot_head = itemView.findViewById(R.id.ll_hot_head);
                hot_recycler = itemView.findViewById(R.id.hot_recycler);
            }
            if (viewType == TYPE_TOPIC) {
                csl_topic_wrap = itemView.findViewById(R.id.csl_topic_wrap);
                csl_forum_wrap = itemView.findViewById(R.id.csl_forum_wrap);
                iv_topic_img = itemView.findViewById(R.id.iv_topic_img);
                tv_topic_title = itemView.findViewById(R.id.tv_topic_title);
                tv_topic_content = itemView.findViewById(R.id.tv_topic_content);
                iv_topic_icon0 = itemView.findViewById(R.id.iv_topic_icon0);
                iv_topic_icon1 = itemView.findViewById(R.id.iv_topic_icon1);
                iv_topic_icon2 = itemView.findViewById(R.id.iv_topic_icon2);
                tv_topic_num = itemView.findViewById(R.id.tv_topic_num);
                iv_forum_icon = itemView.findViewById(R.id.iv_forum_icon);
                tv_forum_name = itemView.findViewById(R.id.tv_forum_name);
                tv_forum_time = itemView.findViewById(R.id.tv_forum_time);
                tv_forum_title = itemView.findViewById(R.id.tv_forum_title);
                tv_forum_content = itemView.findViewById(R.id.tv_forum_content);
                tv_forum_praise_num = itemView.findViewById(R.id.tv_forum_praise_num);
            }
            if (viewType == TYPE_NORMAL) {
                recycler = itemView.findViewById(R.id.recycler);
            }
        }
    }
}