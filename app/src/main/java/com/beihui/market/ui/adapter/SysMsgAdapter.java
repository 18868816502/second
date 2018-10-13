package com.beihui.market.ui.adapter;


import android.content.Context;

import com.beihui.market.R;
import com.beihui.market.entity.SysMsg;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class SysMsgAdapter extends BaseQuickAdapter<SysMsg.Row, BaseViewHolder> {
    private SimpleDateFormat dateFormat = new SimpleDateFormat("MM月dd日 HH:mm", Locale.CHINA);
    private List<SysMsg.Row> dataSet = new ArrayList<>();
    private Context context;

    public SysMsgAdapter(Context context) {
        super(R.layout.rv_sys_msg);
        this.context = context;
    }


    @Override
    protected void convert(BaseViewHolder helper, SysMsg.Row item) {
        if (item.getTitle() != null) {
            helper.setText(R.id.title, item.getTitle());
        }
        if (item.getExplain() != null) {
//            helper.getView(R.id.content).getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListenerByEllipSize((TextView) helper.getView(R.id.content),2));
            helper.setText(R.id.content, item.getExplain());
        }
        helper.setText(R.id.date, dateFormat.format(new Date(item.getGmtCreate())));
        helper.setGone(R.id.iv_system_message_dot, item.getIsRead() == 0); //是否已读，0未读，1已读
        if (item.getIsRead() == 0) {
            helper.setTextColor(R.id.title, context.getResources().getColor(R.color.black_1));
        } else {
            helper.setTextColor(R.id.title, context.getResources().getColor(R.color.black_2));
        }
        helper.addOnClickListener(R.id.tv_delete);


    }

    public void notifySysMsgChanged(List<SysMsg.Row> list) {
        dataSet.clear();
        if (list != null && list.size() > 0) {
            dataSet.addAll(list);
        }
        setNewData(dataSet);
    }
}
