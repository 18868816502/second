package com.beihui.market.ui.dialog;


import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.beihui.market.R;
import com.beihui.market.helper.DataStatisticsHelper;
import com.beihui.market.umeng.Events;
import com.beihui.market.umeng.Statistic;
import com.beihui.market.util.viewutils.ToastUtils;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class WeChatPublicDialog extends DialogFragment {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NO_TITLE, 0);
        setCancelable(false);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_we_chat_public, container, false);
        ButterKnife.bind(this, view);
        ClipboardManager cm = (ClipboardManager) getContext().getSystemService(Context.CLIPBOARD_SERVICE);
        if (cm != null) {
            cm.setPrimaryClip(ClipData.newPlainText("loan_market", "爱信管家"));
            ToastUtils.showShort(getContext(), "公众号复制成功", null);
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
            lp.gravity = Gravity.TOP;
            lp.y = (int) (65 * getResources().getDisplayMetrics().density);
            window.setAttributes(lp);

        }
    }

    @OnClick({R.id.close_image, R.id.goto_wechat})
    void onItemClicked(View view) {
        switch (view.getId()) {
            case R.id.close_image:
                dismiss();
                break;
            case R.id.goto_wechat:
                //umeng统计
                Statistic.onEvent(Events.CLICK_WECHAT_GO);

                //pv，uv统计
                DataStatisticsHelper.getInstance().onCountUv(DataStatisticsHelper.ID_CLICK_GO_WECHAT);

                try {
                    Intent intent = new Intent();
                    ComponentName cmp = new ComponentName("com.tencent.mm", "com.tencent.mm.ui.LauncherUI");
                    intent.setAction(Intent.ACTION_MAIN);
                    intent.addCategory(Intent.CATEGORY_LAUNCHER);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.setComponent(cmp);
                    startActivityForResult(intent, 0);

                    dismiss();
                } catch (Exception e) {
                    //若无法正常跳转，在此进行错误处理
                    ToastUtils.showShort(getContext(), "无法跳转到微信，请检查您是否安装了微信！", null);
                }
                break;
        }
    }
}
