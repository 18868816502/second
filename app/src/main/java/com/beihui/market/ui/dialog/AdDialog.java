package com.beihui.market.ui.dialog;


import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.beihui.market.R;
import com.beihui.market.entity.AdBanner;
import com.beihui.market.ui.activity.MainActivity;
import com.beihui.market.util.SPUtils;
import com.bumptech.glide.Glide;

public class AdDialog extends DialogFragment {

    private AdBanner ad;
    private View.OnClickListener listener;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NO_TITLE, 0);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.dialog_ad, container, false);
        view.findViewById(R.id.close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        if (ad != null && ad.getImgUrl() != null) {
            ImageView imageView = (ImageView) view.findViewById(R.id.ad_image);
            Glide.with(getContext())
                    .load(ad.getImgUrl())
                    .into(imageView);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                    if (listener != null) {
                        listener.onClick(view);
                    }
                }
            });
        }
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        getDialog().setCanceledOnTouchOutside(false);
        Window window = getDialog().getWindow();
        if (window != null) {
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            WindowManager.LayoutParams lp = window.getAttributes();
            lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
            window.setAttributes(lp);
        }
    }

    public AdDialog setAd(AdBanner ad) {
        this.ad = ad;
        return this;
    }

    public AdDialog setListener(View.OnClickListener listener) {
        this.listener = listener;
        return this;
    }
}
