package com.beihui.market.ui.adapter.baserecyclerviehelper;


import com.chad.library.adapter.base.entity.MultiItemEntity;

public class MsgItem implements MultiItemEntity {

    @Override
    public int getItemType() {
        return MessageCenterItemType.ITEM_TYPE_NEWS;
    }
}
