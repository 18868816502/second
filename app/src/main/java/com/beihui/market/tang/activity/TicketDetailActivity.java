package com.beihui.market.tang.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.beihui.market.R;
import com.beihui.market.api.Api;
import com.beihui.market.base.BaseComponentActivity;
import com.beihui.market.entity.Ticket;
import com.beihui.market.helper.SlidePanelHelper;
import com.beihui.market.helper.UserHelper;
import com.beihui.market.injection.component.AppComponent;
import com.beihui.market.tang.DlgUtil;
import com.beihui.market.tang.rx.RxResponse;
import com.beihui.market.tang.rx.observer.ApiObserver;
import com.beihui.market.util.ToastUtil;
import com.gyf.barlibrary.ImmersionBar;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.annotations.NonNull;

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

public class TicketDetailActivity extends BaseComponentActivity {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.ticket_detail_company_name)
    TextView company_name;
    @BindView(R.id.ticket_detail_tax_no)
    TextView tax_no;
    @BindView(R.id.ticket_detail_company_address)
    TextView company_address;
    @BindView(R.id.ticket_detail_phone_no)
    TextView phone_no;
    @BindView(R.id.ticket_detail_bank_name)
    TextView bank_name;
    @BindView(R.id.ticket_detail_bank_account)
    TextView bank_account;

    private Ticket ticket = null;
    private Activity activity;

    @Override
    public int getLayoutId() {
        return R.layout.f_activity_ticket_detail;
    }

    @Override
    public void configViews() {
        activity = this;
        setupToolbar(toolbar);
        ImmersionBar.with(this).statusBarDarkFont(true).init();
        SlidePanelHelper.attach(this);
    }

    @Override
    public void initDatas() {
        if (getIntent() != null) {
            Object obj = getIntent().getSerializableExtra("ticket");
            if (obj != null) ticket = (Ticket) obj;
        }
        if (ticket != null) setView(ticket);
    }

    private void setView(Ticket ticket) {
        company_name.setText(ticket.getCompanyName());
        tax_no.setText(ticket.getTaxNo() == null ? "" : ticket.getTaxNo());
        company_address.setText(ticket.getCompanyAddress() == null ? "" : ticket.getCompanyAddress());
        phone_no.setText(ticket.getTelephone() == null ? "" : ticket.getTelephone());
        bank_name.setText(ticket.getOpenBank() == null ? "" : ticket.getOpenBank());
        bank_account.setText(ticket.getBankAccount() == null ? "" : ticket.getBankAccount());
    }

    @Override
    protected void configureComponent(AppComponent appComponent) {
    }

    @OnClick({R.id.iv_more_setting})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_more_setting:
                DlgUtil.createDlg(this, R.layout.f_dlg_edit_ticket, DlgUtil.DlgLocation.BOTTOM, new DlgUtil.OnDlgViewClickListener() {
                    @Override
                    public void onViewClick(final Dialog dialog, View dlgView) {
                        View.OnClickListener clickListener = new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                switch (v.getId()) {
                                    case R.id.cancel:
                                        dialog.dismiss();
                                        break;
                                    case R.id.ticket_edit:
                                        dialog.dismiss();
                                        Intent intent = new Intent(activity, AddTicketActivity.class);
                                        intent.putExtra("ticket", ticket);
                                        startActivity(intent);
                                        break;
                                    case R.id.ticket_delete:
                                        dialog.dismiss();
                                        DlgUtil.createDlg(activity, R.layout.f_dlg_delete_bill, new DlgUtil.OnDlgViewClickListener() {
                                            @Override
                                            public void onViewClick(final Dialog dlg, View dlgView) {
                                                View.OnClickListener clickListener1 = new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        switch (v.getId()) {
                                                            case R.id.cancel:
                                                                dlg.dismiss();
                                                                break;
                                                            case R.id.confirm:
                                                                dlg.dismiss();
                                                                Api.getInstance().deleteTicket(UserHelper.getInstance(activity).id(), ticket.getInvoiceId())
                                                                        .compose(RxResponse.compatO())
                                                                        .subscribe(new ApiObserver<Object>() {
                                                                            @Override
                                                                            public void onNext(@NonNull Object data) {
                                                                                ToastUtil.toast("发票删除成功");
                                                                                finish();
                                                                            }
                                                                        });
                                                                break;
                                                            default:
                                                                break;
                                                        }
                                                    }
                                                };
                                                dlgView.findViewById(R.id.confirm).setOnClickListener(clickListener1);
                                                dlgView.findViewById(R.id.cancel).setOnClickListener(clickListener1);
                                                TextView tipContent = dlgView.findViewById(R.id.tip_content);
                                                tipContent.setText("确定删除吗？");
                                            }
                                        });
                                        break;
                                    default:
                                        break;
                                }
                            }
                        };
                        dlgView.findViewById(R.id.ticket_edit).setOnClickListener(clickListener);
                        dlgView.findViewById(R.id.ticket_delete).setOnClickListener(clickListener);
                        dlgView.findViewById(R.id.cancel).setOnClickListener(clickListener);
                    }
                });
                break;
            default:
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void recieve(Ticket ticket) {
        setView(ticket);
    }
}