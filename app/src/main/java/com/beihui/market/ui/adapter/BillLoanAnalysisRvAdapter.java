package com.beihui.market.ui.adapter;

import android.app.Activity;
import android.graphics.Color;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.beihui.market.R;
import com.beihui.market.entity.AnalysisChartBean;
import com.beihui.market.entity.AnalysisOverviewBean;
import com.beihui.market.entity.BillLoanAnalysisBean;
import com.beihui.market.util.CommonUtils;
import com.beihui.market.view.CircleImageView;
import com.beihui.market.view.multiChildHistogram.MultiGroupHistogramChildData;
import com.beihui.market.view.multiChildHistogram.MultiGroupHistogramGroupData;
import com.beihui.market.view.multiChildHistogram.MultiGroupHistogramView;
import com.beihui.market.view.pulltoswipe.PulledRecyclerView;
import com.bumptech.glide.DrawableTypeRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Handler;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by admin on 2018/5/22.
 * 网贷分析适配器
 */

public class BillLoanAnalysisRvAdapter extends RecyclerView.Adapter<BillLoanAnalysisRvAdapter.ViewHolder> {

    public Activity mActivity;

    public BillLoanRvAdapter mAdapter;

    /**
     * 类型 2-周 3-月
     */
    public int mType = 3;

    /**
     * 列表数据源
     */
    public BillLoanAnalysisBean mListBean = new BillLoanAnalysisBean();


    //柱形图数据源
    public List<AnalysisChartBean> mList = new ArrayList<>();

    //头布局
    public static final int VIEW_HEADER = R.layout.x_item_bill_loan_analysis_header;
    //头布局 未还已还逾期数据
    public static final int VIEW_HEADER_DATA = R.layout.x_item_bill_loan_analysis_header_data;
    //列表布局
    public static final int VIEW_NORMAL = R.layout.x_item_bill_loan_analysis_normal;
    //脚布局
    public static final int VIEW_FOOTER = R.layout.x_item_bill_loan_analysis_footer;

    //没有数据的布局
    public static final int VIEW_NODATA = R.layout.x_item_bill_loan_analysis_nodata;
    private LinearLayoutManager manager;
    public int firstItemPosition = -1;
    public boolean mShowFirstItemPosition = false;

    public BillLoanAnalysisRvAdapter(Activity mActivity) {
        this.mActivity = mActivity;

        mAdapter = new BillLoanRvAdapter(mActivity);
        manager = new LinearLayoutManager(mActivity);
        manager.setOrientation(LinearLayoutManager.HORIZONTAL);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder viewHolder = new ViewHolder(LayoutInflater.from(mActivity).inflate(viewType, parent, false), viewType);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        if (holder.viewType == VIEW_HEADER ) {
            holder.multiGroupHistogramView.setLayoutManager(manager);
            holder.multiGroupHistogramView.setAdapter(mAdapter);
            mAdapter.notifyChartData(mList, mType);
            Log.e("dfasaf", "firstItemPosition ---> " +firstItemPosition);
            Log.e("dfasaf", "mShowFirstItemPosition ---> " +mShowFirstItemPosition);
            if (firstItemPosition != -1 && mShowFirstItemPosition) {
                manager.scrollToPositionWithOffset(firstItemPosition, 0);
            } else {
                if (mType == 2) {
//                    holder.multiGroupHistogramView.scrollToPosition(10);
//                     manager.scrollToPositionWithOffset(10, 0);
                    holder.multiGroupHistogramView.smoothScrollToPosition(15);
                } else if (mType == 3) {
//                    holder.multiGroupHistogramView.scrollToPosition(4);
//                    manager.scrollToPositionWithOffset(4, 0);
                    holder.multiGroupHistogramView.smoothScrollToPosition(9);
                }
            }

            /**
             * 标题
             */
            holder.mTitle.setText(mType == 2 ? "近12周还款趋势" : "近6月还款趋势");

            holder.multiGroupHistogramView.setOnItemScrollChanged(new PulledRecyclerView.OnItemScrollChanged() {
                @Override
                public void onScrollChanged() {
                    // 只有LinearLayoutManager才有查找第一个和最后一个可见view位置的方法
                    firstItemPosition = manager.findFirstVisibleItemPosition();
                }
            });
        }

        /**
         * 头布局 未还 已还 逾期 数据
         */
        if (holder.viewType == VIEW_HEADER_DATA) {
            //头布局的数据 时间 未还 已还 逾期
            if (TextUtils.isEmpty(mListBean.timeTitleTop)) {
                holder.mYear.setText("");
            } else {
                holder.mYear.setText(mListBean.timeTitleTop);
            }
            if (TextUtils.isEmpty(mListBean.timeTitleBottom)) {
                holder.mDeatilDateOne.setText("");
                holder.mDeatilDateTwo.setText("");
                holder.mDeatilDateThree.setText("");
            } else {
                if (mType == 2) {
                    holder.mDeatilDateOne.setText("第");
                    holder.mDeatilDateTwo.setText(mListBean.timeTitleBottom);
                    holder.mDeatilDateThree.setText("周");
                }
                if (mType == 3) {
                    holder.mDeatilDateOne.setText("");
                    holder.mDeatilDateTwo.setText(mListBean.timeTitleBottom);
                    holder.mDeatilDateThree.setText("月");
                }
            }
            holder.mUnPay.setText(CommonUtils.keep2digitsWithoutZero(mListBean.getUnpayAmount()));
            holder.mAllPay.setText(CommonUtils.keep2digitsWithoutZero(mListBean.getReturnAmount()));
            holder.mOverduepay.setText(CommonUtils.keep2digitsWithoutZero(mListBean.getOverAmount()));
        }

        /**
         * 列表数据
         */
        if (holder.viewType == VIEW_NORMAL) {
            final BillLoanAnalysisBean.ListBean listBean = mListBean.getList().get(getPosition(position));

            if (listBean.getType() == 1) {
                //网贷账单
//                Glide.with(mActivity).load(listBean.getLogoUrl()).placeholder(R.drawable.bill_internetbank_icon).into(holder.mAvatar);
                holder.mAvatar.setImageDrawable(mActivity.getResources().getDrawable(R.drawable.bill_internetbank_icon));
                Glide.with(mActivity).load(listBean.getLogoUrl()).placeholder(R.drawable.bill_internetbank_icon)
//                        .into(holder.mAvatar);
                        .into(new SimpleTarget<GlideDrawable>() {
                            @Override
                            public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                                holder.mAvatar.setImageDrawable(resource);
                            }
                        });
            } else if (listBean.getType() == 3){
                //快捷记账
//                Glide.with(mActivity).load(listBean.getLogoUrl()).placeholder(R.drawable.mine_mail_icon).into(holder.mAvatar);
                holder.mAvatar.setImageDrawable(mActivity.getResources().getDrawable(R.drawable.mine_mail_icon));
                Glide.with(mActivity).load(listBean.getLogoUrl()).placeholder(R.drawable.mine_mail_icon)
//                        .into(holder.mAvatar);
                        .into(new SimpleTarget<GlideDrawable>() {
                            @Override
                            public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                                holder.mAvatar.setImageDrawable(resource);
                            }
                        });
            } else {
                //信用卡账单
//                Glide.with(mActivity).load(listBean.getLogoUrl()).placeholder(R.drawable.mine_bank_default_icon).into(holder.mAvatar);
                holder.mAvatar.setImageDrawable(mActivity.getResources().getDrawable(R.drawable.mine_bank_default_icon));
                Glide.with(mActivity).load(listBean.getLogoUrl()).placeholder(R.drawable.mine_bank_default_icon)
//                        .into(holder.mAvatar);
                        .into(new SimpleTarget<GlideDrawable>() {
                            @Override
                            public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                                holder.mAvatar.setImageDrawable(resource);
                            }
                        });
            }

            holder.mName.setText(listBean.getTitle());

            StringBuilder builder = new StringBuilder();
            if (listBean.getType() == 2) {
                builder.append(listBean.getMonth()+"月");
            } else {
                builder.append(listBean.getTerm()+"/" ).append(listBean.getTotalTerm()+"");
            }
            holder.mDate.setText(CommonUtils.keep2digitsWithoutZero(listBean.getAmount()) + "    " + builder.toString());

            //状态 1-待还 2-已还 3-逾期 4-已出账 5-未出账 6-无账单
            if (listBean.getStatus() == 1) {
                holder.mStatus.setText("未还");
                holder.mStatus.setTextColor(Color.parseColor("#424251"));
            } else if (listBean.getStatus() == 2) {
                holder.mStatus.setText("已还");
                holder.mStatus.setTextColor(Color.parseColor("#9F9FAC"));
            }else if (listBean.getStatus() == 3) {
                holder.mStatus.setText("逾期");
                holder.mStatus.setTextColor(Color.parseColor("#FF5240"));
            }else if (listBean.getStatus() == 4) {
                holder.mStatus.setText("已出账");
                holder.mStatus.setTextColor(Color.parseColor("#9F9FAC"));
            }else if (listBean.getStatus() == 5) {
                holder.mStatus.setText("未出账");
                holder.mStatus.setTextColor(Color.parseColor("#9F9FAC"));
            }

            holder.mListRoot.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onItemClickListener != null) {
                        onItemClickListener.onItemclick(listBean);
                    }
                }
            });
        }


        /**
         * 脚布局
         */
        if (holder.viewType == VIEW_FOOTER) {
            holder.mFooterAll.setText(mFooterAll);
            holder.mFooterPart.setText(mFooterPart);
            holder.mFooterOverdue.setText(mFooterOverdue);
        }
    }

    private int getPosition(int position) {
        return position - 2;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return VIEW_HEADER;
        } else if (position == 1) {
            return VIEW_HEADER_DATA;
        } else if (mListBean.getList().size() > 0 && position <= mListBean.getList().size() + 1) {
            return VIEW_NORMAL;
        } else {
            if (mListBean.getList().size() == 0 && position == 2) {
                return VIEW_NODATA;
            } else {
                return VIEW_FOOTER;
            }
        }
    }

    @Override
    public int getItemCount() {
        return mListBean.getList().size() == 0 ? 4 : mListBean.getList().size() + 3;
    }

    public String mFooterAll = " ";
    public String mFooterPart = " ";
    public String mFooterOverdue = " ";

    /**
     * 刷新脚部布局
     */
    public void notifyBottomData(AnalysisOverviewBean data) {
        mFooterAll = CommonUtils.keep2digitsWithoutZero(data.getRepayAmount());
        mFooterPart = CommonUtils.keep2digitsWithoutZero(data.getUnpayAmount());
        mFooterOverdue = CommonUtils.keep2digitsWithoutZero(data.getOverAmount());
        notifyItemChanged(mListBean.getList().size() == 0 ? 3 :  mListBean.getList().size()+1);
    }

    /**
     * 刷新列表数据
     */
    public void notifyListData(BillLoanAnalysisBean data, String  timeTitleTop, String timeTitleBottom) {
        mListBean.setOverAmount(data.getOverAmount());
        mListBean.setReturnAmount(data.getReturnAmount());
        mListBean.setUnpayAmount(data.getUnpayAmount());
        mListBean.timeTitleTop = timeTitleTop;
        mListBean.timeTitleBottom = timeTitleBottom;
        if (mListBean.getList().size() > 0) {
            mListBean.getList().clear();
        }
        mListBean.getList().addAll(data.getList());
        notifyDataSetChanged();
    }

    /**
     * 刷新柱状图数据
     */
    public void notifyChartData(List<AnalysisChartBean> data) {
        if (mAdapter != null) {
            if (mList.size() > 0) {
                mList.clear();
            }
            mList.addAll(data);
            if (mList.size() > 0) {
                if (mType == 2) {
                    mList.get(11).isSelect = true;
                }
                if (mType == 3) {
                    mList.get(5).isSelect = true;
                }
            }
            mAdapter.notifyChartData(data, mType);
        }
    }

    public interface OnItemClickListener{
        void onItemclick(BillLoanAnalysisBean.ListBean listBean );
    }

    public OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    /**
     * 设置类型
     */
    public void setType(int type) {
        this.mType = type;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public int viewType;

        //头布局
        //MultiGroupHistogramView multiGroupHistogramView;
        public PulledRecyclerView multiGroupHistogramView;
        public TextView mTitle;

        //头布局 未还已还逾期数据
        public TextView mYear;
        public TextView mDeatilDateOne;
        public TextView mDeatilDateTwo;
        public TextView mDeatilDateThree;
        public TextView mUnPay;
        public TextView mAllPay;
        public TextView mOverduepay;

        //列表布局
        public LinearLayout mListRoot;
        public CircleImageView mAvatar;
        public TextView mName;
        public TextView mDate;
        public TextView mStatus;


        //脚布局
        public TextView mFooterAll;
        public TextView mFooterPart;
        public TextView mFooterOverdue;

        public ViewHolder(View itemView, int viewType) {
            super(itemView);
            this.viewType = viewType;
            if (viewType == VIEW_HEADER) {
                mTitle = (TextView)itemView.findViewById(R.id.rv_item_analysis_title);
                multiGroupHistogramView = (PulledRecyclerView)itemView.findViewById(R.id.rc_chart_view);
            }
            if (viewType == VIEW_HEADER_DATA) {
                mYear = (TextView)itemView.findViewById(R.id.tv_item_loan_analysis_year);
                mDeatilDateOne = (TextView)itemView.findViewById(R.id.tv_item_date_type_one);
                mDeatilDateTwo = (TextView)itemView.findViewById(R.id.tv_item_date_type_two);
                mDeatilDateThree = (TextView)itemView.findViewById(R.id.tv_item_date_type_three);

                mUnPay = (TextView)itemView.findViewById(R.id.tv_item_loan_analysis_unpay);
                mAllPay = (TextView)itemView.findViewById(R.id.tv_item_loan_analysis_all_pay);
                mOverduepay = (TextView)itemView.findViewById(R.id.tv_item_loan_analysis_overdue_pay);
            }
            if (viewType == VIEW_NORMAL) {
                mListRoot = (LinearLayout)itemView.findViewById(R.id.ll_item_normal_loan_analysis_root);
                mAvatar = (CircleImageView)itemView.findViewById(R.id.civ_item_normal_loan_analysis);
                mName = (TextView)itemView.findViewById(R.id.tv_item_normal_loan_analysis_name);
                mDate = (TextView)itemView.findViewById(R.id.tv_item_normal_loan_analysis_date);
                mStatus = (TextView)itemView.findViewById(R.id.tv_item_normal_loan_analysis_status);
            }
            if (viewType == VIEW_FOOTER) {
                mFooterAll = (TextView)itemView.findViewById(R.id.tv_item_bill_loan_footer_all);
                mFooterPart = (TextView)itemView.findViewById(R.id.tv_item_bill_loan_footer_part);
                mFooterOverdue = (TextView)itemView.findViewById(R.id.tv_item_bill_loan_footer_overdue);
            }
        }
    }
}