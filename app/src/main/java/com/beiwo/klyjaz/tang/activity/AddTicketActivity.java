package com.beiwo.klyjaz.tang.activity;

import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.beiwo.klyjaz.R;
import com.beiwo.klyjaz.api.Api;
import com.beiwo.klyjaz.base.BaseComponentActivity;
import com.beiwo.klyjaz.entity.Ticket;
import com.beiwo.klyjaz.helper.SlidePanelHelper;
import com.beiwo.klyjaz.helper.UserHelper;
import com.beiwo.klyjaz.injection.component.AppComponent;
import com.beiwo.klyjaz.tang.rx.RxResponse;
import com.beiwo.klyjaz.tang.rx.observer.ApiObserver;
import com.beiwo.klyjaz.util.ToastUtil;
import com.beiwo.klyjaz.view.ClearEditText;
import com.gyf.barlibrary.ImmersionBar;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.Map;

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

public class AddTicketActivity extends BaseComponentActivity {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.toolbar_title)
    TextView toolbar_title;
    @BindView(R.id.toolbar_right_txt)
    TextView toolbar_right_txt;
    @BindView(R.id.cet_company_name)
    ClearEditText cet_company_name;
    @BindView(R.id.cet_tax_no)
    ClearEditText cet_tax_no;
    @BindView(R.id.cet_company_address)
    ClearEditText cet_company_address;
    @BindView(R.id.cet_phone_no)
    ClearEditText cet_phone_no;
    @BindView(R.id.cet_bank_name)
    ClearEditText cet_bank_name;
    @BindView(R.id.cet_bank_account)
    ClearEditText cet_bank_account;

    private Ticket ticket = null;
    private String companyName;
    private String taxNo;
    private String companyAddress;
    private String telephone;
    private String openBank;
    private String bankAccount;
    private Map<String, Object> map = new HashMap<>();

    @Override
    public int getLayoutId() {
        return R.layout.f_layout_add_ticket;
    }

    @Override
    public void configViews() {
        setupToolbarBackNavigation(toolbar, R.drawable.back);
        ImmersionBar.with(this).statusBarDarkFont(true).init();
        SlidePanelHelper.attach(this);
        toolbar_right_txt.setVisibility(View.VISIBLE);
        toolbar_right_txt.setText("保存");
    }

    @Override
    public void initDatas() {
        if (getIntent() != null) {
            Object obj = getIntent().getSerializableExtra("ticket");
            if (obj != null) ticket = (Ticket) obj;
        }
        if (ticket != null) {
            toolbar_title.setText("编辑发票");
            map.put("invoiceId", ticket.getInvoiceId());

            cet_company_name.setText(ticket.getCompanyName());
            if (!TextUtils.isEmpty(ticket.getTaxNo())) cet_tax_no.setText(ticket.getTaxNo());
            if (!TextUtils.isEmpty(ticket.getCompanyAddress()))
                cet_company_address.setText(ticket.getCompanyAddress());
            if (!TextUtils.isEmpty(ticket.getTelephone()))
                cet_phone_no.setText(ticket.getTelephone());
            if (!TextUtils.isEmpty(ticket.getOpenBank()))
                cet_bank_name.setText(ticket.getOpenBank());
            if (!TextUtils.isEmpty(ticket.getBankAccount()))
                cet_bank_account.setText(ticket.getBankAccount());
        } else {
            toolbar_title.setText("添加发票");
        }
        map.put("userId", UserHelper.getInstance(this).id());

        cet_company_name.setMaxLenght(20);
        cet_tax_no.setMaxLenght(20);
        cet_company_address.setMaxLenght(40);
        cet_phone_no.setMaxLenght(20);
        cet_bank_name.setMaxLenght(20);
        cet_bank_account.setMaxLenght(20);
    }

    @Override
    protected void configureComponent(AppComponent appComponent) {
    }

    @OnClick({R.id.toolbar_right_txt})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.toolbar_right_txt:
                //公司名称
                companyName = str(cet_company_name);
                if (TextUtils.isEmpty(companyName)) {
                    ToastUtil.toast("请填写公司名称");
                    return;
                }
                map.put("companyName", companyName);
                //税号
                taxNo = str(cet_tax_no);
                if (!TextUtils.isEmpty(taxNo)) {
                    map.put("taxNo", taxNo);
                }
                //公司地址
                companyAddress = str(cet_company_address);
                if (!TextUtils.isEmpty(companyAddress)) {
                    map.put("companyAddress", companyAddress);
                }
                // 电话号码
                telephone = str(cet_phone_no);
                if (!TextUtils.isEmpty(telephone)) {
                    map.put("telephone", telephone);
                }
                // 开户银行
                openBank = str(cet_bank_name);
                if (!TextUtils.isEmpty(openBank)) {
                    map.put("openBank", openBank);
                }
                // 银行账户
                bankAccount = str(cet_bank_account);
                if (!TextUtils.isEmpty(bankAccount)) {
                    map.put("bankAccount", bankAccount);
                }
                Api.getInstance().saveTicket(map)
                        .compose(RxResponse.<Ticket>compatT())
                        .subscribe(new ApiObserver<Ticket>() {
                            @Override
                            public void onNext(@NonNull Ticket data) {
                                if (ticket != null) {
                                    ToastUtil.toast("编辑成功");
                                    EventBus.getDefault().post(data);
                                } else {
                                    ToastUtil.toast("添加成功");
                                }
                                finish();
                            }
                        });
                break;
            default:
                break;
        }
    }

    private String str(ClearEditText editText) {
        String trim = editText.getText().toString().trim();
        if (!TextUtils.isEmpty(trim)) return trim;
        else return "";
    }
}
