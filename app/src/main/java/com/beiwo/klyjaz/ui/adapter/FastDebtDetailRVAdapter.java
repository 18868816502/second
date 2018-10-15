package com.beiwo.klyjaz.ui.adapter;


import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.beiwo.klyjaz.R;
import com.beiwo.klyjaz.entity.DebeDetailRecord;
import com.beiwo.klyjaz.entity.FastDebtDetail;
import com.beiwo.klyjaz.util.FormatNumberUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * 快速记账详情适配器
 */
public class FastDebtDetailRVAdapter extends BaseQuickAdapter<FastDebtDetail.DetailListBean, BaseViewHolder> {


    public static final int VIEW_ITEM_TYPE = R.layout.x_item_debt_detail_pay_detail;
    public static final int VIEW_ITEM_NO_RECORD = R.layout.x_item_debt_detail_no_record;

    public Drawable upIcon;
    public Drawable dowmIcon;

    public Activity mActivity;

    private List<FastDebtDetail.DetailListBean> dataSet = new ArrayList<>();

    private List<DebeDetailRecord> mDebeDetailRecordList = new ArrayList<>();

    public String mSelectTermRepayDate = null;


    private int[] colors = {Color.WHITE, Color.parseColor("#424251"), Color.parseColor("#909298"), Color.parseColor("#ff395e")};
    private String[] status = {"", "待还", "已还", "逾期"};

    //
    private Integer showOldPosition = null;

    public FastDebtDetailRVAdapter(Activity activity) {
        super(R.layout.list_item_debt_detail_pay_plan);

        mActivity = activity;
        upIcon= activity.getResources().getDrawable(R.drawable.up_icon);
        dowmIcon= activity.getResources().getDrawable(R.drawable.dowm_icon);
    }

    @Override
    protected void convert(final BaseViewHolder helper, FastDebtDetail.DetailListBean item) {
        helper.setText(R.id.th, item.getTermNo() + "期")      //设置期数
                .setText(R.id.date, item.getTermRepayDate().replace("-", "."))    //设置日期
                .setText(R.id.amount, "¥" + FormatNumberUtils.FormatNumberFor2(item.getTermPayableAmount()))  //设置金额
                .setText(R.id.status, status[item.getStatus()])
                .setTextColor(R.id.status, colors[item.getStatus()])
                .setTextColor(R.id.th, item.termRepayDate.equals(mSelectTermRepayDate) ? Color.RED : Color.parseColor("#424251"))
                .setTextColor(R.id.amount, item.termRepayDate.equals(mSelectTermRepayDate)  ? Color.RED : Color.parseColor("#424251"))
                .addOnClickListener(R.id.ll_item_debt_detail_root);

        helper.getView(R.id.iv_item_debt_detail_pay_arrow).setRotation(item.isShow ? 90 : 0);

        LinearLayout payPlanRoot = helper.<LinearLayout>getView(R.id.ll_item_debt_detail_play_detail);
        int childCount = payPlanRoot.getChildCount();

        if (childCount > 0) {
            payPlanRoot.removeAllViews();
        }
        int size = mDebeDetailRecordList.size();
        if (item.isShow) {
            if (size <= 0) {
                //装载暂无记录的布局
                View itemView = LayoutInflater.from(mActivity).inflate(VIEW_ITEM_NO_RECORD, null, false);
                payPlanRoot.addView(itemView);
            } else {
                for (int i = 0; i < size; i++) {
                    DebeDetailRecord record = mDebeDetailRecordList.get(i);
                    View itemView = LayoutInflater.from(mActivity).inflate(VIEW_ITEM_TYPE, null, false);
                    payPlanRoot.addView(itemView);
                    TextView header = (TextView) itemView.findViewById(R.id.tv_item_pay_detail_record);
//                    View line = itemView.findViewById(R.id.tv_item_pay_detail_under_line);
                    TextView content = (TextView) itemView.findViewById(R.id.tv_item_pay_detail_content);
                    TextView date = (TextView) itemView.findViewById(R.id.tv_item_pay_detail_date);
                    TextView money = (TextView) itemView.findViewById(R.id.tv_item_pay_detail_money);
                    if (i == 0) {
                        header.setVisibility(View.VISIBLE);
//                        line.setVisibility(View.VISIBLE);
                    } else {
                        header.setVisibility(View.GONE);
//                        line.setVisibility(View.GONE);
                    }

                    if (!TextUtils.isEmpty(record.discription)) {
                        content.setText(record.discription);
                    }
                    if (!TextUtils.isEmpty(record.transDate)) {
                        date.setText(record.transDate.substring(5, 10).replace("-", "/"));
                    }
                    money.setText(FormatNumberUtils.FormatNumberFor2(record.amount));
                    if (TextUtils.isEmpty(record.amount+"") || record.amount < 0D) {
                        money.setTextColor(Color.parseColor("#4CC99E"));
                    } else {
                        money.setTextColor(Color.parseColor("#424251"));
                    }
                }
            }
        }
    }

    public void notifyPayPlanChanged(List<FastDebtDetail.DetailListBean> list, int currentTerm) {
//        currentIndex = currentTerm;
        if (list.size() > 0) {
            mSelectTermRepayDate = list.get(0).termRepayDate;
        }
        dataSet.clear();
        if (list != null && list.size() > 0) {
            dataSet.addAll(list);
        }
        setNewData(dataSet);
    }



    public void setThTextColor(String termRepayDate) {
        mSelectTermRepayDate = termRepayDate;
        notifyDataSetChanged();
    }

    public void showDebtDetailRecord(int position, List<DebeDetailRecord> debeDetailRecordList) {
        if (mDebeDetailRecordList.size() > 0) {
            mDebeDetailRecordList.clear();
        }
        mDebeDetailRecordList.addAll(debeDetailRecordList);

        if (this.showOldPosition == null || this.showOldPosition == position) {
            dataSet.get(position).isShow = !dataSet.get(position).isShow ;
        } else {
            dataSet.get(position).isShow = true;
            dataSet.get(showOldPosition).isShow = false;
        }
        this.showOldPosition = position;
        notifyDataSetChanged();
    }
}