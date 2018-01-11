package com.beihui.market.ui.adapter.multipleentity;


import com.beihui.market.entity.Debt;
import com.chad.library.adapter.base.entity.MultiItemEntity;

public class AllDebtMultiEntity implements MultiItemEntity {

    public static final int ITEM_TYPE_TIMESTAMP = 1;
    public static final int ITEM_TYPE_DEBT = 2;


    public Debt debt;
    public String date;

    public AllDebtMultiEntity(Debt debt, String date) {
        this.debt = debt;
        this.date = date;
    }

    @Override
    public int getItemType() {
        return debt == null ? ITEM_TYPE_TIMESTAMP : ITEM_TYPE_DEBT;
    }
}
