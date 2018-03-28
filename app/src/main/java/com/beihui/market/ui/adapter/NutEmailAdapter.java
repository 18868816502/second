package com.beihui.market.ui.adapter;


import android.widget.ImageView;

import com.beihui.market.R;
import com.beihui.market.entity.NutEmail;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NutEmailAdapter extends BaseQuickAdapter<NutEmail, BaseViewHolder> {

    private List<NutEmail> dataSet = new ArrayList<>();

    private Map<String, Integer> type2Logo = new HashMap<>();

    public NutEmailAdapter() {
        super(R.layout.rv_item_nut_email);
        type2Logo.put("QQ", R.drawable.qqmail_logo);
        type2Logo.put("MAIL163", R.drawable.mail163_logo);
        type2Logo.put("MAIL126", R.drawable.mail126_logo);
        type2Logo.put("HOTMAIL", R.drawable.hotmail_logo);
        type2Logo.put("SINA", R.drawable.sinamail_logo);
    }

    @Override
    protected void convert(BaseViewHolder helper, NutEmail item) {
        if (type2Logo.containsKey(item.getSymbol())) {
            ((ImageView) helper.getView(R.id.email_logo)).setImageResource(type2Logo.get(item.getSymbol()));
        }
    }

    public void notifyNutEmailChanged(List<NutEmail> list) {
        dataSet.clear();
        if (list != null && list.size() > 0) {
            dataSet.addAll(list);
        }
        setNewData(dataSet);
    }
}
