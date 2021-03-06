package com.beiwo.qnejqaz.ui.dialog;


import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.beiwo.qnejqaz.App;
import com.beiwo.qnejqaz.BuildConfig;
import com.beiwo.qnejqaz.R;
import com.beiwo.qnejqaz.api.Api;
import com.beiwo.qnejqaz.helper.UserHelper;
import com.beiwo.qnejqaz.tang.rx.RxResponse;
import com.beiwo.qnejqaz.tang.rx.observer.ApiObserver;
import com.beiwo.qnejqaz.util.CommonUtils;
import com.beiwo.qnejqaz.util.ToastUtil;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMWeb;

import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * 分享对话框 微信 朋友圈 QQ 微博
 */
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
        getDialog().setCanceledOnTouchOutside(true);
        Window window = getDialog().getWindow();
        if (window != null) {
            WindowManager.LayoutParams lp = window.getAttributes();
            window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
            window.setAttributes(lp);
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            window.setGravity(Gravity.BOTTOM);
        }
    }

    @Override
    public void onDestroyView() {
        unbinder.unbind();
        super.onDestroyView();
    }

    @SuppressLint("InlinedApi")
    @OnClick({R.id.share_wechat, R.id.share_wechat_moment, R.id.share_qq, R.id.share_weibo, R.id.cancel})
    void OnViewClicked(View view) {
        switch (view.getId()) {
            //微信
            case R.id.share_wechat:
                if (CommonUtils.isAppInstalled(getActivity(), "com.tencent.mm")) {
                    shareWeb(SHARE_MEDIA.WEIXIN);
                } else {
                    ToastUtil.toast("未安装");
                }
                break;
            //朋友圈
            case R.id.share_wechat_moment:
                if (CommonUtils.isAppInstalled(getActivity(), "com.tencent.mm")) {
                    shareWeb(SHARE_MEDIA.WEIXIN_CIRCLE);
                } else {
                    ToastUtil.toast("未安装");
                }
                break;
            //QQ
            case R.id.share_qq:
                if (CommonUtils.isAppInstalled(getActivity(), "com.tencent.mobileqq")) {
                    //qq分享需要存储权限
                    if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                            || ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        String[] permission = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
                        this.requestPermissions(permission, 1);
                    } else {
                        shareWeb(SHARE_MEDIA.QQ);
                    }
                } else {
                    ToastUtil.toast("未安装");
                }
                break;
            //微博
            case R.id.share_weibo:
                if (CommonUtils.isAppInstalled(getActivity(), "com.sina.weibo")) {
                    shareWeb(SHARE_MEDIA.SINA);
                } else {
                    ToastUtil.toast("未安装");
                }
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
        dismiss();
        if (umWeb == null) {
            throw new IllegalStateException("未设置分享内容 ");
        }
        new ShareAction(getActivity()).withMedia(this.umWeb).setPlatform(media).setCallback(new UMShareListener() {
            @Override
            public void onStart(SHARE_MEDIA share_media) {
            }

            @Override
            public void onResult(SHARE_MEDIA share_media) {
                if (BuildConfig.API_ENV) {
                    Api.getInstance().shareUser(UserHelper.getInstance(App.getInstance().getApplicationContext()).getProfile().getId())
                            .compose(RxResponse.compatO())
                            .subscribe(new ApiObserver<Object>() {
                                @Override
                                public void onNext(Object data) {
                                }
                            });
                }
            }

            @Override
            public void onError(SHARE_MEDIA share_media, Throwable throwable) {
            }

            @Override
            public void onCancel(SHARE_MEDIA share_media) {
            }
        }).share();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                shareWeb(SHARE_MEDIA.QQ);
            }

        }
    }
}
