package com.beihui.market.ui.dialog;


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
import com.beihui.market.util.viewutils.ToastUtils;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMWeb;

import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class ShareDialog extends DialogFragment {

    Unbinder unbinder;

    private UMWeb umWeb;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.CommonBottomDialogStyle);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_share, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        Window window = getDialog().getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setAttributes(lp);

        window.setGravity(Gravity.BOTTOM);
    }

    @Override
    public void onDestroyView() {
        unbinder.unbind();
        super.onDestroyView();
    }

    @OnClick({R.id.share_wechat, R.id.share_wechat_moment, R.id.share_qq, R.id.share_weibo, R.id.cancel})
    void OnViewClicked(View view) {
        switch (view.getId()) {
            case R.id.share_wechat:
                shareWeb(SHARE_MEDIA.WEIXIN);
                break;
            case R.id.share_wechat_moment:
                shareWeb(SHARE_MEDIA.WEIXIN_CIRCLE);
                break;
            case R.id.share_qq:
                shareWeb(SHARE_MEDIA.QQ);
                break;
            case R.id.share_weibo:
                shareWeb(SHARE_MEDIA.SINA);
                break;
            case R.id.cancel:
                dismiss();
                break;
        }
    }

    public ShareDialog setUmWeb(UMWeb umWeb) {
        this.umWeb = umWeb;
        return this;
    }

    private void shareWeb(SHARE_MEDIA media) {
        if (umWeb == null) {
            throw new IllegalStateException("未设置分享内容 ");
        }
        new ShareAction(getActivity()).withMedia(this.umWeb).setPlatform(media).setCallback(new UMShareListener() {
            @Override
            public void onStart(SHARE_MEDIA share_media) {
            }

            @Override
            public void onResult(SHARE_MEDIA share_media) {
                ToastUtils.showShort(getContext(), "分享成功", null);
            }

            @Override
            public void onError(SHARE_MEDIA share_media, Throwable throwable) {
                ToastUtils.showShort(getContext(), "分享错误", null);
            }

            @Override
            public void onCancel(SHARE_MEDIA share_media) {
                ToastUtils.showShort(getContext(), "分享取消", null);
            }
        }).share();

        dismiss();
    }

}
