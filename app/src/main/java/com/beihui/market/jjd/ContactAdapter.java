package com.beihui.market.jjd;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;


import com.beihui.market.R;
import com.beihui.market.entity.ContactBean;
import com.beihui.market.jjd.activity.ContactsActivity;
import com.beihui.market.jjd.activity.VerticyIDActivity;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * Copyright: dondo (C)2018
 * FileName: ContactAdapter
 * Author: jiang
 * Create on: 2018/8/13 11:13
 * Description:
 */
public class ContactAdapter extends BaseQuickAdapter<ContactBean, BaseViewHolder> {
    private Context context;
    private String value;
    private int requestType = 0;

    public ContactAdapter(int layoutResId, @Nullable List<ContactBean> data, Context context, String value, int requestType) {
        super(layoutResId, data);
        this.context = context;
        this.value = value;
        this.requestType = requestType;
    }

    @Override
    protected void convert(final BaseViewHolder helper, final ContactBean item) {
        helper.setText(R.id.name, item.getDisplayName());
        final String num;
        if (item.getPhoneNum() == null) {
            num = "";
        } else {
            num = item.getPhoneNum();
        }
        if (item.getPhoto() != null) {
            Glide.with(context).load(item.getPhoto()).into((ImageView) helper.getView(R.id.contact_avatar));
        } else {
            Glide.with(context).load(R.drawable.mine_icon_head).into((ImageView) helper.getView(R.id.contact_avatar));
        }
        helper.getView(R.id.add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (requestType == 0) {
                    Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:" + num));
                    intent.putExtra("sms_body", value);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                    helper.getView(R.id.add).setVisibility(View.GONE);
                    helper.getView(R.id.add_contact_tv).setVisibility(View.VISIBLE);
                } else {
                    Intent intent = new Intent();
                    intent.putExtra("contact_name", item.getDisplayName());
                    intent.putExtra("contact_phone", item.getPhoneNum());
                    ((ContactsActivity) mContext).setResult(VerticyIDActivity.RESULTCODE_VERTIFY_CONTACT, intent);
                    ((ContactsActivity) mContext).finish();
                }
            }
        });
    }
}
