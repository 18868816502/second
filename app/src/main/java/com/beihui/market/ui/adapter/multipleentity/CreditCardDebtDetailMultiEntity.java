package com.beihui.market.ui.adapter.multipleentity;


import com.beihui.market.entity.BillDetail;
import com.beihui.market.entity.CreditCardDebtBill;
import com.chad.library.adapter.base.entity.AbstractExpandableItem;
import com.chad.library.adapter.base.entity.MultiItemEntity;

public class CreditCardDebtDetailMultiEntity extends AbstractExpandableItem<CreditCardDebtDetailMultiEntity> implements MultiItemEntity {

    public static final int ITEM_TYPE_MONTH_ABSTRACT = 1;

    public static final int ITEM_TYPE_DETAIL = 2;

    public static final int ITEM_TYPE_HINT = 3;


    private CreditCardDebtBill bill;
    private BillDetail billDetail;

    private boolean headExpanded;
    private boolean isInit;

    public CreditCardDebtDetailMultiEntity(CreditCardDebtBill bill, BillDetail billDetail) {
        this.bill = bill;
        this.billDetail = billDetail;
    }

    @Override
    public int getItemType() {
        if (bill != null) {
            return ITEM_TYPE_MONTH_ABSTRACT;
        } else if (billDetail != null) {
            return ITEM_TYPE_DETAIL;
        } else {
            return ITEM_TYPE_HINT;
        }
    }

    @Override
    public int getLevel() {
        return bill != null ? 0 : 1;
    }

    public boolean isHeadExpanded() {
        return headExpanded;
    }

    public void setHeadExpanded(boolean expanded) {
        this.headExpanded = expanded;
    }

    public boolean isInit() {
        return isInit;
    }

    public void setInit(boolean init) {
        isInit = init;
    }

    public CreditCardDebtBill getMonthBill() {
        return bill;
    }

    public BillDetail getBillDetail() {
        return billDetail;
    }
}
