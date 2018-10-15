package com.beiwo.klyjaz.tang.adapter;

import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.beiwo.klyjaz.R;
import com.beiwo.klyjaz.entity.Ticket;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

/**
 * https://gitee.com/tangbuzhi
 *
 * @author: Tangbuzhi
 * @version:
 * @package:
 * @description:
 * @modify:
 * @date: 2018/9/5
 */

public class TicketAdapter extends BaseQuickAdapter<Ticket, BaseViewHolder> {
    public TicketAdapter() {
        super(R.layout.f_layout_ticket_item);
        openLoadAnimation(ALPHAIN);
    }

    @Override
    protected void convert(BaseViewHolder helper, Ticket item) {
        helper.setText(R.id.ticket_company_name, item.getCompanyName());
        TextView ticket_tax_no = helper.getView(R.id.ticket_tax_no);
        TextView ticket_tax_tag = helper.getView(R.id.ticket_tax_tag);
        if (!TextUtils.isEmpty(item.getTaxNo())) {
            ticket_tax_no.setText(item.getTaxNo());
            ticket_tax_no.setVisibility(View.VISIBLE);
            ticket_tax_tag.setVisibility(View.VISIBLE);
        } else {
            ticket_tax_no.setVisibility(View.GONE);
            ticket_tax_tag.setVisibility(View.GONE);
        }
    }
}