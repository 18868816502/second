package com.beihui.market.ui.adapter;


import android.graphics.Color;
import android.widget.ImageView;

import com.beihui.market.R;
import com.beihui.market.entity.LoanBill;
import com.beihui.market.util.CommonUtils;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.mcxtzhang.swipemenulib.SwipeMenuLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static android.text.TextUtils.isEmpty;

public class MyLoanBillDebtAdapter extends BaseQuickAdapter<LoanBill.Row, BaseViewHolder> {

    private int billType;
    private List<LoanBill.Row> dataSet = new ArrayList<>();

    private int colorPaid = Color.parseColor("#424251");
    private int colorUnpaid = Color.parseColor("#ff395e");

    public MyLoanBillDebtAdapter(int layoutResId, int billType) {
        super(layoutResId);
        this.billType = billType;
    }

    @Override
    protected void convert(BaseViewHolder helper, LoanBill.Row item) {
        helper.addOnClickListener(R.id.content_container);
//        helper.addOnClickListener(R.id.hide_show);

        if (billType == 1 || billType == 3) {
            //网贷账单
            bindLoanDebt(helper, item);
        } else {
            //信用卡账单
            bindBillDebt(helper, item);
        }
        //是否已隐藏
//        boolean isHidden = item.getHide() == 0;//隐藏
//        helper.setVisible(R.id.is_hidden, isHidden);
//        helper.setText(R.id.debt_visibility, !isHidden ? "首页隐藏" : "首页显示");
//        helper.getView(R.id.debt_eye).setSelected(!isHidden);

        //已还清的账单不能滑动操作
        ((SwipeMenuLayout) helper.getView(R.id.swipe_menu_layout)).setSwipeEnable(item.getStatus() != 2);
    }

    private void bindLoanDebt(BaseViewHolder helper, LoanBill.Row item) {
        //渠道logo
        if (!isEmpty(item.getLogo())) {
            Glide.with(helper.itemView.getContext())
                    .load(item.getLogo())
                    .asBitmap()
                    .centerCrop()
                    .placeholder(R.drawable.mine_bank_default_icon)
                    .into((ImageView) helper.getView(R.id.channel_logo));
        } else {
            ((ImageView) helper.getView(R.id.channel_logo)).setImageResource(R.drawable.mine_bank_default_icon);
        }
        //渠道名称 如果是快捷记账则为账单名称
        if (billType == 1) {
            if (!isEmpty(item.getChannelName())) {
                helper.setText(R.id.channel_name, item.getChannelName());
            } else {
                helper.setText(R.id.channel_name, "");

            }
        } else {
            if (!isEmpty(item.getBillName())) {
                helper.setText(R.id.channel_name, item.getBillName());
            } else {
                helper.setText(R.id.channel_name, "");
            }
        }
        //借款金额
//        helper.setText(R.id.debt_amount, "待还金额" + CommonUtils.keep2digitsWithoutZero(item.getAmount()) + "元");
        switch (item.getStatus()) {
            case 1://待还
                helper.setTextColor(R.id.status, colorUnpaid);
                helper.setText(R.id.status, "待还款");
                break;
            case 2://已还
                helper.setTextColor(R.id.status, colorPaid);
                helper.setText(R.id.status, "已还清");
                break;
            case 3://逾期
                helper.setTextColor(R.id.status, colorUnpaid);
                helper.setText(R.id.status, "待还款");
                break;
            default:
                helper.setText(R.id.status, "");
        }
    }

    private void bindBillDebt(BaseViewHolder helper, LoanBill.Row item) {
        //银行logo
        if (!isEmpty(item.getLogo())) {
            Glide.with(helper.itemView.getContext())
                    .load(item.getLogo())
                    .asBitmap()
                    .centerCrop()
                    .placeholder(R.drawable.image_place_holder)
                    .into((ImageView) helper.getView(R.id.bank_logo));
        } else {
            ((ImageView) helper.getView(R.id.bank_logo)).setImageResource(R.drawable.image_place_holder);
        }
        //银行名字
        if (!isEmpty(item.getBankName())) {
            helper.setText(R.id.bank_name, item.getBankName());
        } else {
            helper.setText(R.id.bank_name, "");
        }
        //信用卡尾号
        if (!isEmpty(item.getCardNums())) {
            helper.setText(R.id.bank_card_num, item.getCardNums());
        } else {
            helper.setText(R.id.bank_card_num, "");
        }

        switch (item.getStatus()) {
            case 1://待还
                helper.setTextColor(R.id.bank_card_status, colorUnpaid);
//                helper.setText(R.id.bank_card_status, "待还款");
                break;
            case 2://已还
                helper.setTextColor(R.id.bank_card_status, colorPaid);
//                helper.setText(R.id.bank_card_status, "已还清");
                break;
            case 3://逾期
                helper.setTextColor(R.id.bank_card_status, colorUnpaid);
//                helper.setText(R.id.bank_card_status, "待还款");
                break;
            default:
                helper.setText(R.id.bank_card_status, "");
        }
        //出账日
//        helper.setText(R.id.bill_day, String.format(Locale.CHINA, "每月%d日", item.getBillDay()));
        //还款日
//        helper.setText(R.id.due_day, String.format(Locale.CHINA, "每月%d日", item.getDueDay()));
    }

    public void notifyLoanBillChanged(List<LoanBill.Row> list) {
        dataSet.clear();
        if (list != null && list.size() > 0) {
            dataSet.addAll(list);
        }
        setNewData(dataSet);
        disableLoadMoreIfNotFullPage();
    }
}
