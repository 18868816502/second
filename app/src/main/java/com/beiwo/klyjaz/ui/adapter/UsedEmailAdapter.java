package com.beiwo.klyjaz.ui.adapter;


import android.text.TextUtils;

import com.beiwo.klyjaz.R;
import com.beiwo.klyjaz.entity.UsedEmail;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.ArrayList;
import java.util.List;

public class UsedEmailAdapter extends BaseQuickAdapter<UsedEmail, BaseViewHolder> {

    private List<UsedEmail> dataSet = new ArrayList<>();

    public UsedEmailAdapter() {
        super(R.layout.rv_item_used_email);
    }

    @Override
    protected void convert(BaseViewHolder helper, UsedEmail item) {
        helper.setText(R.id.email_account, TextUtils.isEmpty(item.getEmail()) ? "" : item.getEmail());
        helper.setText(R.id.update_time, TextUtils.isEmpty(item.getLastCollectionDate()) ? "" : item.getLastCollectionDate());
    }

    public void notifyUsedEmailChanged(List<UsedEmail> list) {
        dataSet.clear();
        if (list != null && list.size() > 0) {
            dataSet.addAll(list);
        }
        setNewData(dataSet);
    }
}
