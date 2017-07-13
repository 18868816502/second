package com.beihui.market.ui.adapter;


import com.beihui.market.R;
import com.beihui.market.ui.adapter.baserecyclerviehelper.MessageCenterItemType;
import com.beihui.market.ui.adapter.baserecyclerviehelper.MsgItem;
import com.beihui.market.ui.adapter.baserecyclerviehelper.MsgSectionHeaderItem;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.util.ArrayList;
import java.util.List;

public class MessageCenterAdapter extends BaseMultiItemQuickAdapter<MultiItemEntity, BaseViewHolder> {

    private List<MultiItemEntity> dataSet;

    public MessageCenterAdapter() {
        super(null);
        addItemType(MessageCenterItemType.ITEM_TYPE_HEADER, R.layout.rv_item_message_center_section_header);
        addItemType(MessageCenterItemType.ITEM_TYPE_NEWS, R.layout.rv_item_news);
    }

    @Override
    protected void convert(BaseViewHolder helper, MultiItemEntity item) {
        if (item.getItemType() == MessageCenterItemType.ITEM_TYPE_NEWS) {
        }
    }

    public void notifyMessageChanged(List<String> list) {
        if (dataSet == null) {
            dataSet = new ArrayList<>();
        }
        dataSet.clear();
        if (list != null && list.size() > 0) {
            dataSet.add(new MsgSectionHeaderItem());
            for (String msg : list) {
                dataSet.add(new MsgItem());
            }
        }
        setNewData(dataSet);
    }
}
