package com.beihui.market.ui.adapter;


import android.graphics.Color;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;

import com.beihui.market.R;
import com.beihui.market.entity.BillDetail;
import com.beihui.market.entity.CreditCardDebtBill;
import com.beihui.market.ui.adapter.multipleentity.CreditCardDebtDetailMultiEntity;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static android.text.TextUtils.isEmpty;
import static com.beihui.market.ui.adapter.multipleentity.CreditCardDebtDetailMultiEntity.ITEM_TYPE_DETAIL;
import static com.beihui.market.ui.adapter.multipleentity.CreditCardDebtDetailMultiEntity.ITEM_TYPE_HINT;
import static com.beihui.market.ui.adapter.multipleentity.CreditCardDebtDetailMultiEntity.ITEM_TYPE_MONTH_ABSTRACT;
import static com.beihui.market.util.CommonUtils.keep2digitsWithoutZero;

public class CreditCardDebtDetailAdapter extends BaseMultiItemQuickAdapter<CreditCardDebtDetailMultiEntity, BaseViewHolder> {
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.CHINA);
    private final SimpleDateFormat shortDateFormat = new SimpleDateFormat("MM月dd日", Locale.CHINA);
    private final SimpleDateFormat shortDateFormat1 = new SimpleDateFormat("dd/MM", Locale.CHINA);
    private final Calendar calendar = Calendar.getInstance(Locale.CHINA);

    private List<CreditCardDebtDetailMultiEntity> dataSet = new ArrayList<>();
    private List<CreditCardDebtDetailMultiEntity> pureDataSet = new ArrayList<>();

    private int colorIn = Color.parseColor("#5591FF");
    private int colorOut = Color.parseColor("#424251");

    private int lastPageSize;

    /**
     * 是否是手动记账的账单
     */
    private boolean hand;

    public CreditCardDebtDetailAdapter(boolean hand) {
        super(null);
        this.hand = hand;
        addItemType(ITEM_TYPE_MONTH_ABSTRACT, R.layout.rv_item_credit_card_detail_month_abstract);
        addItemType(ITEM_TYPE_DETAIL, R.layout.rv_item_credit_card_detail_detail);
        addItemType(ITEM_TYPE_HINT, R.layout.rv_item_credit_card_detail_hint);
    }

    @Override
    protected void convert(BaseViewHolder helper, CreditCardDebtDetailMultiEntity item) {
        switch (item.getItemType()) {
            case ITEM_TYPE_MONTH_ABSTRACT:
                bindMonthBill(helper, item.getMonthBill(), item.isHeadExpanded());
                break;
            case ITEM_TYPE_DETAIL:
                bindBillDetail(helper, item.getBillDetail());
                break;
            case ITEM_TYPE_HINT:
                break;
        }
    }

    private void bindMonthBill(BaseViewHolder holder, CreditCardDebtBill bill, boolean expanded) {
        holder.addOnClickListener(R.id.month_bill_container);
        holder.addOnClickListener(R.id.edit_amount);
        holder.addOnClickListener(R.id.add_amount);

        if (!isEmpty(bill.getBillDate())) {
            try {
                calendar.setTime(dateFormat.parse(bill.getBillDate()));
                holder.setText(R.id.debt_month, (calendar.get(Calendar.MONTH) + 1) + "月");
                holder.setText(R.id.debt_year, calendar.get(Calendar.YEAR) + "年");
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        holder.setText(R.id.debt_amount, String.valueOf((char) 165) + keep2digitsWithoutZero(bill.getNewBalance()));
        if (hand) {
            //手动记账的账单
            holder.setVisible(R.id.amount_block, true);
            if (bill.getStatus() == 5) {
                //已出账
                if (bill.getNewBalance() >= 0) {
                    //已出账，有数据，显示修改
                    holder.setVisible(R.id.add_amount, false);
                    holder.setVisible(R.id.edit_amount, true);
                } else {
                    //已出账，没数据，显示添加
                    holder.setVisible(R.id.amount_block, false);
                    holder.setVisible(R.id.add_amount, true);
                }
            }

        } else {
            //导入的账单，没有更新操作
            holder.setVisible(R.id.amount_block, true);
            holder.setVisible(R.id.add_amount, false);
            holder.setVisible(R.id.edit_amount, false);
        }

        switch (bill.getStatus()) {
            case 1://待还
                holder.setText(R.id.debt_status, String.format(Locale.CHINA, "距离还款日%d天", bill.getReturnDay()));
                break;
            case 2://已还
                holder.setText(R.id.debt_status, "已还清");
                break;
            case 3://逾期
                SpannableString ss = new SpannableString("逾期" + Math.abs(bill.getReturnDay()) + "天");
                ss.setSpan(new ForegroundColorSpan(Color.parseColor("#ff395e")), 2, ss.length() - 1, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
                holder.setText(R.id.debt_status, ss);
                break;
            case 4://未出账
                holder.setText(R.id.debt_status, bill.getOutBillDay() == 0 ? "今天出账" : String.format(Locale.CHINA, "%d天后出账", bill.getOutBillDay()));
                break;
            case 5://已出账
                holder.setText(R.id.debt_status, String.format(Locale.CHINA, "已出账%d天", bill.getOutBillDay()));
                break;
            case 6://无账单
                holder.setText(R.id.debt_status, "无账单");
                break;
        }

        //账单周期
        if (!isEmpty(bill.getStartTime()) && !isEmpty(bill.getEndTime())) {
            try {
                Date startDate = dateFormat.parse(bill.getStartTime());
                Date endDate = dateFormat.parse(bill.getEndTime());
                holder.setText(R.id.bill_range, "账单周期 " + shortDateFormat.format(startDate) + "-" + shortDateFormat.format(endDate));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        //是否已经展开
        holder.setGone(R.id.bill_range_block, expanded);

    }

    private void bindBillDetail(BaseViewHolder holder, BillDetail detail) {
        //图标
        if (detail.getType() == 1) {
            //还款
            holder.setImageResource(R.id.detail_image, R.drawable.bill_detail_in);
            holder.setTextColor(R.id.detail_amount, colorIn);
        } else {
            holder.setImageResource(R.id.detail_image, R.drawable.bill_detail_out);
            holder.setTextColor(R.id.detail_amount, colorOut);
        }
        //标题
        if (!isEmpty(detail.getDiscription())) {
            holder.setText(R.id.detail_desc, detail.getDiscription());
        }
        //日期
        if (!isEmpty(detail.getTransDate())) {
            try {
                holder.setText(R.id.detail_date, shortDateFormat1.format(dateFormat.parse(detail.getTransDate())));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        //金额
        holder.setText(R.id.detail_amount, String.valueOf((char) 165) + keep2digitsWithoutZero(detail.getAmountMoney()));
    }

    public void expandMonthBill(int index) {
        dataSet.get(index).setHeadExpanded(true);
        notifyDataSetChanged();
    }

    public void collapseMonthBill(int index) {
        dataSet.get(index).setHeadExpanded(false);
        notifyDataSetChanged();
        //收缩
        collapse(index + 1);
    }

    public void notifyDebtListChanged(List<CreditCardDebtBill> list) {
        if (list != null && list.size() > 0) {
            for (int i = lastPageSize; i < list.size(); ++i) {
                dataSet.add(new CreditCardDebtDetailMultiEntity(list.get(i), null));
            }
            lastPageSize = list.size();
        } else {
            dataSet.clear();
        }
        pureDataSet.clear();
        pureDataSet.addAll(dataSet);
        setNewData(dataSet);
        disableLoadMoreIfNotFullPage();
    }

    public void notifyBillDetailChanged(List<BillDetail> list, int index) {
        CreditCardDebtDetailMultiEntity entity = dataSet.get(index);
        entity.setInit(true);
        if (entity.getSubItems() != null) {
            entity.getSubItems();
        }
        if (list != null && list.size() > 0) {
            for (int i = 0; i < list.size(); ++i) {
                entity.addSubItem(new CreditCardDebtDetailMultiEntity(null, list.get(i)));
            }
        } else {
            entity.addSubItem(new CreditCardDebtDetailMultiEntity(null, null));
        }
        //展开
        expand(index + 1, true);
    }

    public int indexOf(CreditCardDebtDetailMultiEntity entity) {
        return pureDataSet.indexOf(entity);
    }
}
