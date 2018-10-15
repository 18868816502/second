package com.beiwo.klyjaz.tang.adapter;

import android.app.Dialog;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.TextView;

import com.beiwo.klyjaz.R;
import com.beiwo.klyjaz.entity.WithdrawRecord;
import com.beiwo.klyjaz.tang.DlgUtil;
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
 * @date: 2018/8/17
 */

public class WithdrawRecordAdapter extends BaseQuickAdapter<WithdrawRecord.Rows, BaseViewHolder> {
    public WithdrawRecordAdapter() {
        super(R.layout.f_layout_withdraw_record_item);
        openLoadAnimation(ALPHAIN);
    }

    @Override
    protected void convert(final BaseViewHolder helper, final WithdrawRecord.Rows item) {
        helper.setText(R.id.tv_account_name, item.getTradeName())
                .setText(R.id.tv_account_amount, String.format("%.2f", -item.getTradeAmount()))
                .setText(R.id.tv_account_alp, item.getTradeAccount())
                .setText(R.id.tv_account_time, item.getGmtCreate())
                .setVisible(R.id.iv_account_status, item.getStatus() == 0);
        TextView tv_account_status = helper.getView(R.id.tv_account_status);
        String status = "";
        int color = -1;
        switch (item.getStatus()) {// 0- 交易失败, 1-处理中, 2-交易成功
            case 0:
                status = "提现失败";
                color = ContextCompat.getColor(helper.itemView.getContext(), R.color.refresh_one);
                break;
            case 1:
                status = "审核中";
                color = ContextCompat.getColor(helper.itemView.getContext(), R.color.black_1);
                break;
            case 2:
                status = "提现成功";
                color = ContextCompat.getColor(helper.itemView.getContext(), R.color.withdraw_success);
                break;
            default:
                break;
        }
        tv_account_status.setText(status);
        tv_account_status.setTextColor(color);
        if (item.getStatus() == 0) {
            helper.addOnClickListener(R.id.iv_account_status);
            View.OnClickListener listener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DlgUtil.createDlg(helper.itemView.getContext(), R.layout.f_dlg_apl_fail, new DlgUtil.OnDlgViewClickListener() {
                        @Override
                        public void onViewClick(final Dialog dialog, View dlgView) {
                            TextView content = dlgView.findViewById(R.id.content);
                            content.setText(item.getReason());
                            dlgView.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialog.dismiss();
                                }
                            });
                        }
                    });
                }
            };
            helper.getView(R.id.iv_account_status).setOnClickListener(listener);
        }
    }
}