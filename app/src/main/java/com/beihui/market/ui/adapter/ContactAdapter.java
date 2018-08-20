package com.beihui.market.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;

import com.beihui.market.R;
import com.beihui.market.api.Api;
import com.beihui.market.entity.ContactBean;
import com.beihui.market.helper.UserHelper;
import com.beihui.market.tang.rx.RxResponse;
import com.beihui.market.tang.rx.observer.ApiObserver;
import com.beihui.market.util.LogUtils;
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


    public ContactAdapter(int layoutResId, @Nullable List<ContactBean> data, Context context) {
        super(layoutResId, data);
        this.context = context;
    }

    @Override
    protected void convert(final BaseViewHolder helper, final ContactBean item) {

        helper.setText(R.id.name, item.getDisplayName());
//        helper.getView(R.id.add).setVisibility(View.GONE);
//        helper.getView(R.id.add_contact_tv).setVisibility(View.VISIBLE);
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
                Api.getInstance().getInviteMsg(UserHelper.getInstance(context).getProfile().getId()).compose(RxResponse.<String>compatT()).subscribe(new ApiObserver<String>() {
                    @Override
                    public void onNext(String data) {
                        Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:" + num));
                        intent.putExtra("sms_body", data);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                    }
                });
            }
        });

    }
}
