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
        //手动账单，导入账单都可能出现暂无账单明细
        addItemType(ITEM_TYPE_HINT, R.layout.rv_item_credit_card_detail_hint);
        //手动和导入的列表使用不同样式item,手动账单没有详细
        if (hand) {
            addItemType(ITEM_TYPE_MONTH_ABSTRACT, R.layout.rv_item_bill_month_hand);
        } else {
            addItemType(ITEM_TYPE_MONTH_ABSTRACT, R.layout.rv_item_bill_month_leading_in);
            //导入账单才有详细信息
            addItemType(ITEM_TYPE_DETAIL, R.layout.rv_item_bill_detail);
        }
    }

    @Override
    protected void convert(BaseViewHolder helper, CreditCardDebtDetailMultiEntity item) {
        switch (item.getItemType()) {
            case ITEM_TYPE_MONTH_ABSTRACT:
                //手动账单，导入账单都可以选中详情
                helper.addOnClickListener(R.id.month_bill_container);

                //手动账单包含导入账单的所有样式
                bindMonthBillLeadingIn(helper, item.getMonthBill(), item.isHeadExpanded());
                if (hand) {
                    //只有手动账单能够修改，添加金额
                    bindMonthBillHand(helper, item.getMonthBill(), item.isHeadExpanded());
                    //只有手动账单能够添加，修改金额
                    helper.addOnClickListener(R.id.edit_amount);
                    helper.addOnClickListener(R.id.add_amount);
                }
                break;
            case ITEM_TYPE_DETAIL:
                bindBillDetail(helper, item.getBillDetail());
                break;
        }
    }

    /**
     * 绑定导入账单信息
     */
    private void bindMonthBillLeadingIn(BaseViewHolder holder, CreditCardDebtBill bill, boolean expanded) {
        //账单日期
        if (!isEmpty(bill.getBillDate())) {
            try {
                calendar.setTime(dateFormat.parse(bill.getBillDate()));
                holder.setText(R.id.debt_month, (calendar.get(Calendar.MONTH) + 1) + "月");
                holder.setText(R.id.debt_year, calendar.get(Calendar.YEAR) + "年");
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        //账单金额
        holder.setText(R.id.debt_amount, String.valueOf((char) 165) + keep2digitsWithoutZero(bill.getNewBalance()));
        //账单状态
        switch (bill.getStatus()) {
            case 1://待还
                holder.setText(R.id.debt_status, String.format(Locale.CHINA, "距离还款日%d天", bill.getReturnDay()));
                break;
            case 2://已还
                if (bill.getNewBalance() > 0) {
                    holder.setText(R.id.debt_status, "已还清");
                } else {
                    //已还清的无金额账单显示无账单
                    holder.setText(R.id.debt_status, "无账单");
                }
                break;
            case 3://逾期
                SpannableString ss = new SpannableString("逾期" + Math.abs(bill.getReturnDay()) + "天");
                ss.setSpan(new ForegroundColorSpan(Color.parseColor("#ff395e")), 2, ss.length() - 1, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
                holder.setText(R.id.debt_status, ss);
                break;
            case 4://已出账
                holder.setText(R.id.debt_status, String.format(Locale.CHINA, "已出账%d天", bill.getOutBillDay()));
                break;
            case 5://未出账
                //未出账，且未返回金额,则设置0
                if (bill.getNewBalance() == -1) {
                    holder.setText(R.id.debt_amount, String.valueOf((char) 165) + "0");
                }
                holder.setText(R.id.debt_status, bill.getOutBillDay() == 0 ? "今天出账" : String.format(Locale.CHINA, "%d天后出账", bill.getOutBillDay()));
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
        //展开的三角
        holder.getView(R.id.expand_collapse_hint).setRotation(expanded ? 90 : 0);
    }

    /**
     * 绑定手动账单信息
     */
    private void bindMonthBillHand(BaseViewHolder holder, CreditCardDebtBill bill, boolean expanded) {
        //手动账单可以添加，修改金额
        holder.setVisible(R.id.amount_block, false);
        holder.setVisible(R.id.edit_amount, false);
        holder.setVisible(R.id.add_amount, false);
        holder.setGone(R.id.edit_amount_block, true);
        switch (bill.getStatus()) {
            case 1://待还
                //待还的账单可以修改金额
                holder.setVisible(R.id.amount_block, true);
                holder.setVisible(R.id.edit_amount, true);
                break;
            case 2://已还
                //已还清的账单无法修改金额
                holder.setVisible(R.id.amount_block, true);
                break;
            case 3://逾期
                //逾期账单==待还账单
                holder.setVisible(R.id.amount_block, true);
                holder.setVisible(R.id.edit_amount, true);
                break;
            case 4://已出账
                if (bill.getNewBalance() == -1) {//没有金额的已出账账单，显示添加
                    holder.setVisible(R.id.add_amount, true);
                }
                break;
            case 5://未出账
                //未出账账单不能添加，修改,显示距出账时间
                holder.setVisible(R.id.amount_block, true);
                holder.setVisible(R.id.edit_amount, false);
                holder.setGone(R.id.edit_amount_block, false);
                break;
            case 6://无账单
                break;
        }
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
            //更新月份账单数据源，现在更新金额后需要重新拉取列表
            for (int i = 0; i < lastPageSize; ++i) {
                dataSet.get(i).updateMonthBill(list.get(i));
            }
            //添加新的月份账单数据，MultiEntity状态初始
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
