package com.beihui.market.ui.adapter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.beihui.market.R;
import com.beihui.market.api.Api;
import com.beihui.market.api.ResultEntity;
import com.beihui.market.entity.AnalysisChartBean;
import com.beihui.market.entity.BillLoanAnalysisBean;
import com.beihui.market.entity.GroupProductBean;
import com.beihui.market.helper.UserHelper;
import com.beihui.market.ui.activity.WebViewActivity;
import com.beihui.market.util.CommonUtils;
import com.beihui.market.util.RxUtil;
import com.beihui.market.view.GlideCircleTransform;
import com.beihui.market.view.pulltoswipe.PulledRecyclerView;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.functions.Consumer;

/**
 * Created by admin on 2018/5/22.
 * 网贷分析适配器
 */

public class BillLoanAnalysisMonthRvAdapter extends RecyclerView.Adapter<BillLoanAnalysisMonthRvAdapter.ViewHolder> {

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

    public List<List<GroupProductBean>> footerList = new ArrayList<>();

    //头布局
    public static final int VIEW_HEADER = R.layout.x_item_bill_loan_analysis_header;
    //头布局 未还已还逾期数据
    public static final int VIEW_HEADER_DATA = R.layout.x_item_bill_loan_analysis_header_data;
    //列表布局
    public static final int VIEW_NORMAL = R.layout.x_item_bill_loan_analysis_normal;
    //脚布局
//    public static final int VIEW_FOOTER = R.layout.x_item_bill_loan_analysis_footer;
    public static final int VIEW_FOOTER = R.layout.x_item_bill_loan_analysis_second_account;

    //没有数据的布局
    public static final int VIEW_NODATA = R.layout.x_item_bill_loan_analysis_nodata;
    private LinearLayoutManager manager;
    public int firstItemPosition = -1;
    public boolean mShowFirstItemPosition = false;

    public BillLoanAnalysisMonthRvAdapter(Activity mActivity) {
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
                holder.mDeatilDateTwo.setText("");
                holder.mDeatilDateThree.setText("");
            } else {
                if (mType == 2) {
                    holder.mDeatilDateTwo.setText(mListBean.timeTitleBottom);
                    holder.mDeatilDateThree.setText("周");
                }
                if (mType == 3) {
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
            int placeHolder;
            if (listBean.getType() == 1) {
                //网贷账单
                placeHolder = R.drawable.bill_internetbank_icon;
            } else if (listBean.getType() == 3){
                //快捷记账
                placeHolder = R.drawable.mine_mail_icon;
            } else {
                //信用卡账单
                placeHolder = R.drawable.mine_bank_default_icon;
            }
            Glide.with(mActivity).load(listBean.getLogoUrl()).centerCrop().transform(new GlideCircleTransform(mActivity)).placeholder(placeHolder).into(holder.mAvatar);


            holder.mName.setText(listBean.getTitle());

            StringBuilder builder = new StringBuilder();
            if (listBean.getType() == 2) {
                builder.append(listBean.getMonth()+"月");
            } else {
                if (listBean.getTotalTerm() == -1) {
                    builder.append("循环");
                } else {
                    builder.append(listBean.getTerm() + "/").append(listBean.getTotalTerm() + "");
                }
            }
            holder.mDate.setText(builder.toString());

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

            holder.mMoney.setText(CommonUtils.keep2digitsWithoutZero(listBean.getAmount()));

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
            int i;
            if (mListBean.getList().size() == 0) {
                i = position - 3;
            } else {
                i = position - 2 - mListBean.getList().size();
            }
            final List<GroupProductBean> productBeans = footerList.get(i);
            if (i == 0) {
                holder.mSecondAccountRoot.setVisibility(View.VISIBLE);
                holder.mSecondAccount.setVisibility(View.VISIBLE);
            } else {
                holder.mSecondAccountRoot.setVisibility(View.GONE);
                holder.mSecondAccount.setVisibility(View.GONE);
            }
            int size = productBeans.size();
            if (size > 0) {
                holder.mSecondAccountOne.setText(productBeans.get(0).getProductName());
                Glide.with(mActivity).load(productBeans.get(0).getLogoUrl()).into(holder.mSecondAccountOneIcon);
                holder.mSecondAccountOneRoot.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        skipProduce(productBeans.get(0).getId(), productBeans.get(0).getProductName());
                    }
                });
            }
            if (size > 1) {
                holder.mSecondAccountTwo.setText(productBeans.get(1).getProductName());
                Glide.with(mActivity).load(productBeans.get(1).getLogoUrl()).into(holder.mSecondAccountTwoIcon);
                holder.mSecondAccountTwoRoot.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        skipProduce(productBeans.get(1).getId(), productBeans.get(1).getProductName());
                    }
                });
            }
            if (size > 2) {
                holder.mSecondAccountThree.setText(productBeans.get(2).getProductName());
                Glide.with(mActivity).load(productBeans.get(2).getLogoUrl()).into(holder.mSecondAccountThreeIcon);
                holder.mSecondAccountThreeRoot.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        skipProduce(productBeans.get(2).getId(), productBeans.get(2).getProductName());
                    }
                });
            }
            if (size > 3) {
                holder.mSecondAccountFour.setText(productBeans.get(3).getProductName());
                Glide.with(mActivity).load(productBeans.get(3).getLogoUrl()).into(holder.mSecondAccountFourIcon);
                holder.mSecondAccountFourRoot.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        skipProduce(productBeans.get(3).getId(), productBeans.get(3).getProductName());
                    }
                });
            }


        }
    }

    private void skipProduce(String productId, final String titleName) {
        //底部数据
        Api.getInstance().queryGroupProductSkip(UserHelper.getInstance(mActivity).getProfile().getId(), productId)
                .compose(RxUtil.<ResultEntity<String>>io2main())
                .subscribe(new Consumer<ResultEntity<String>>() {
                               @Override
                               public void accept(ResultEntity<String> result) throws Exception {
                                   if (result.isSuccess()) {
                                       Intent intent = new Intent(mActivity, WebViewActivity.class);
                                       intent.putExtra("webViewUrl", result.getData());
                                       intent.putExtra("webViewTitleName", titleName);
                                       mActivity.startActivity(intent);
                                   } else {
                                       Toast.makeText(mActivity, result.getMsg(), Toast.LENGTH_SHORT).show();
                                   }
                               }
                           },
                        new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {

                            }
                        });
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
        return mListBean.getList().size() == 0 ? 3 + footerList.size() : mListBean.getList().size() + 2 + footerList.size();
    }


    /**
     * 刷新脚部布局
     */
    public void notifyBottomData(List<List<GroupProductBean>> data) {
        if (footerList.size() > 0) {
            footerList.clear();
        }
        footerList.addAll(data);
        notifyDataSetChanged();
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

    public interface OnGroupProductItemClickListener{
        void onItemclick(GroupProductBean productBean);
    }

    public OnGroupProductItemClickListener onGroupProductItemClickListener;

    public void setOnGroupProductItemClickListener(OnGroupProductItemClickListener onGroupProductItemClickListener) {
        this.onGroupProductItemClickListener = onGroupProductItemClickListener;
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
        public TextView mDeatilDateTwo;
        public TextView mDeatilDateThree;
        public TextView mUnPay;
        public TextView mAllPay;
        public TextView mOverduepay;

        //列表布局
        public LinearLayout mListRoot;
        public ImageView mAvatar;
        public TextView mName;
        public TextView mDate;
        public TextView mStatus;
        public TextView mMoney;


        //脚布局
        public RelativeLayout mSecondAccountRoot;
        public TextView mSecondAccount;
        public TextView mSecondAccountOne;
        public ImageView mSecondAccountOneIcon;
        public TextView mSecondAccountTwo;
        public ImageView mSecondAccountTwoIcon;
        public TextView mSecondAccountThree;
        public ImageView mSecondAccountThreeIcon;
        public TextView mSecondAccountFour;
        public ImageView mSecondAccountFourIcon;


        public LinearLayout mSecondAccountOneRoot;
        public LinearLayout mSecondAccountTwoRoot;
        public LinearLayout mSecondAccountThreeRoot;
        public LinearLayout mSecondAccountFourRoot;

        public ViewHolder(View itemView, int viewType) {
            super(itemView);
            this.viewType = viewType;
            if (viewType == VIEW_HEADER) {
                mTitle = (TextView)itemView.findViewById(R.id.rv_item_analysis_title);
                multiGroupHistogramView = (PulledRecyclerView)itemView.findViewById(R.id.rc_chart_view);
            }
            if (viewType == VIEW_HEADER_DATA) {
                mYear = (TextView)itemView.findViewById(R.id.tv_item_loan_analysis_year);
                mDeatilDateTwo = (TextView)itemView.findViewById(R.id.tv_item_date_type_two);
                mDeatilDateThree = (TextView)itemView.findViewById(R.id.tv_item_date_type_three);

                mUnPay = (TextView)itemView.findViewById(R.id.tv_item_loan_analysis_unpay);
                mAllPay = (TextView)itemView.findViewById(R.id.tv_item_loan_analysis_all_pay);
                mOverduepay = (TextView)itemView.findViewById(R.id.tv_item_loan_analysis_overdue_pay);
            }
            if (viewType == VIEW_NORMAL) {
                mListRoot = (LinearLayout)itemView.findViewById(R.id.ll_item_normal_loan_analysis_root);
                mAvatar = (ImageView)itemView.findViewById(R.id.civ_item_normal_loan_analysis);
                mName = (TextView)itemView.findViewById(R.id.tv_item_normal_loan_analysis_name);
                mDate = (TextView)itemView.findViewById(R.id.tv_item_normal_loan_analysis_date);
                mStatus = (TextView)itemView.findViewById(R.id.tv_item_normal_loan_analysis_status);
                mMoney = (TextView)itemView.findViewById(R.id.tv_item_normal_loan_analysis_money);
            }
            if (viewType == VIEW_FOOTER) {
                mSecondAccountRoot = (RelativeLayout)itemView.findViewById(R.id.tv_footer_second_account_root);
                mSecondAccount = (TextView)itemView.findViewById(R.id.tv_footer_second_account);
                mSecondAccountOne = (TextView)itemView.findViewById(R.id.tv_footer_second_account_one);
                mSecondAccountOneIcon = (ImageView)itemView.findViewById(R.id.iv_footer_second_account_one_icon);
                mSecondAccountTwo = (TextView)itemView.findViewById(R.id.tv_footer_second_account_two);
                mSecondAccountTwoIcon = (ImageView)itemView.findViewById(R.id.iv_footer_second_account_two_icon);
                mSecondAccountThree = (TextView)itemView.findViewById(R.id.tv_footer_second_account_three);
                mSecondAccountThreeIcon = (ImageView)itemView.findViewById(R.id.iv_footer_second_account_three);
                mSecondAccountFour = (TextView)itemView.findViewById(R.id.tv_footer_second_account_four);
                mSecondAccountFourIcon = (ImageView)itemView.findViewById(R.id.iv_footer_second_account_four);


                mSecondAccountOneRoot = (LinearLayout)itemView.findViewById(R.id.ll_footer_second_account_one_root);
                mSecondAccountTwoRoot = (LinearLayout)itemView.findViewById(R.id.ll_footer_second_account_two_root);
                mSecondAccountThreeRoot = (LinearLayout)itemView.findViewById(R.id.ll_footer_second_account_three_root);
                mSecondAccountFourRoot = (LinearLayout)itemView.findViewById(R.id.ll_footer_second_account_four_root);
            }
        }
    }
}