package com.beiwo.qnejqaz.ui.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.beiwo.qnejqaz.BuildConfig;
import com.beiwo.qnejqaz.R;
import com.beiwo.qnejqaz.api.Api;
import com.beiwo.qnejqaz.api.ResultEntity;
import com.beiwo.qnejqaz.base.BaseComponentActivity;
import com.beiwo.qnejqaz.entity.AppUpdate;
import com.beiwo.qnejqaz.helper.DataCleanManager;
import com.beiwo.qnejqaz.helper.FileProviderHelper;
import com.beiwo.qnejqaz.helper.SlidePanelHelper;
import com.beiwo.qnejqaz.helper.UserHelper;
import com.beiwo.qnejqaz.helper.updatehelper.AppUpdateHelper;
import com.beiwo.qnejqaz.social.activity.EditProduceActivity;
import com.beiwo.qnejqaz.ui.busevents.UserLogoutEvent;
import com.beiwo.qnejqaz.ui.contract.UserProfileContract;
import com.beiwo.qnejqaz.ui.dialog.CommNoneAndroidDialog;
import com.beiwo.qnejqaz.ui.presenter.UserProfilePresenter;
import com.beiwo.qnejqaz.umeng.Events;
import com.beiwo.qnejqaz.umeng.Statistic;
import com.beiwo.qnejqaz.util.CommonUtils;
import com.beiwo.qnejqaz.util.ImageUtils;
import com.beiwo.qnejqaz.util.LogUtils;
import com.beiwo.qnejqaz.util.PopUtils;
import com.beiwo.qnejqaz.util.RxUtil;
import com.beiwo.qnejqaz.util.ToastUtil;
import com.beiwo.qnejqaz.util.WeakRefToastUtil;
import com.beiwo.qnejqaz.view.CircleImageView;
import com.beiwo.qnejqaz.view.dialog.PopDialog;
import com.bumptech.glide.Glide;
import com.gyf.barlibrary.ImmersionBar;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.functions.Consumer;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;

import static com.beiwo.qnejqaz.ui.activity.EditNickNameActivity.RESULT_OK_EDIT_NICK_NAME_ACTIVITY;

/**
 * 个人中心页面
 */
@RuntimePermissions
public class UserProfileActivity extends BaseComponentActivity implements UserProfileContract.View, View.OnClickListener, PopDialog.OnInitPopListener {

    public static final int REQUEST_EDIT_NICK_NAME_ACTIVITY = 0;

    @BindView(R.id.tool_bar)
    Toolbar toolbar;
    @BindView(R.id.avatar)
    CircleImageView avatarIv;
    @BindView(R.id.tv_navigate_nick_name)
    TextView nickNameTv;
    @BindView(R.id.tv_version_name)
    TextView versionNameTv;
    @BindView(R.id.tv_clear_cache)
    TextView cacheSize;
    @BindView(R.id.tv_user_profile_mobile)
    TextView userProfileMobile;
    @BindView(R.id.tv_user_profile_wxchat)
    TextView userProfileWxChat;
    @BindView(R.id.fl_remove_wx_chat)
    FrameLayout wxFrame;
    @BindView(R.id.view_bottom)
    View btomView;

    @BindView(R.id.fl_sex)
    FrameLayout flSex;
    @BindView(R.id.tv_sex)
    TextView tvSex;
    @BindView(R.id.fl_produce)
    FrameLayout flProduce;
    @BindView(R.id.tv_produce)
    TextView tvProduce;

    UserProfilePresenter presenter;

    private Dialog avatarSelector;

    private String avatarFilePath;

    //清楚缓存的大小
    private String mCacheSize;

    private AppUpdateHelper updateHelper = AppUpdateHelper.newInstance();

    private int mSex = 1;

    @Override
    protected void onResume() {
        super.onResume();
        presenter.onStart();
    }

    @Override
    protected void onDestroy() {
        updateHelper.destroy();
        presenter.onDestroy();
        presenter = null;
        super.onDestroy();
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_user_profile;
    }

    @Override
    public void configViews() {
        ImmersionBar.with(this).statusBarDarkFont(true).init();
        setupToolbar(toolbar);

        SlidePanelHelper.attach(this);
        presenter = new UserProfilePresenter(this,this);

        //pv，uv统计
//        DataStatisticsHelper.newInstance().onCountUv(NewVersionEvents.PI);
    }

    @Override
    public void initDatas() {
        /**
         * 显示版本号
         */
        versionNameTv.setText("v" + BuildConfig.VERSION_NAME);

        try {
            mCacheSize = DataCleanManager.getFormatSize(DataCleanManager.getInternalCacheSize()
                    + DataCleanManager.getExternalCacheSize());
        } catch (Exception e) {
            e.printStackTrace();
        }

        cacheSize.setText(mCacheSize);

        Intent intent = getIntent();
        if (intent != null) {
            tvProduce.setText(intent.getStringExtra("introduce"));
            mSex = intent.getIntExtra("sex", 1);
            tvSex.setText(mSex == 1 ? "男" : "女");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK_EDIT_NICK_NAME_ACTIVITY && requestCode == REQUEST_EDIT_NICK_NAME_ACTIVITY && data != null) {
            nickNameTv.setText(data.getStringExtra("updateNickName"));
            return;
        }
        if (requestCode == EditProduceActivity.RESULT_OK_EDIT_PRODUCE_ACTIVITY && resultCode == RESULT_OK) {
            if (data != null) {
                tvProduce.setText(data.getStringExtra("introduce"));
                return;
            }
        }

        if (resultCode == RESULT_OK) {
            Bitmap avatar = null;
            String path = null;
            if (requestCode == 1) {
                //相机
                path = avatarFilePath;
            } else if (requestCode == 2) {
                //相册
                path = getRealPathFromURI(data.getData());
            }
            if (path != null) {
                avatar = ImageUtils.getFixedBitmap(path, 512);
            }
            if (avatar != null) {
                presenter.updateAvatar(avatar);
            } else {
                //WeakRefToastUtil.showShort(this, "头像解析错误", null);
                ToastUtil.toast("头像解析错误");
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        UserProfileActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    @OnClick({R.id.avatar, R.id.avatar_item, R.id.fl_navigate_nick_name, R.id.fl_navigate_revise_pwd, R.id.tv_user_profile_exit,
            R.id.fl_version_code, R.id.fl_clear_cache, R.id.fl_about_us, R.id.fl_produce, R.id.fl_sex})
    void OnItemsClicked(View view) {
        switch (view.getId()) {
            //编辑昵称
            case R.id.fl_navigate_nick_name:

                //pv，uv统计
//                DataStatisticsHelper.newInstance().onCountUv(NewVersionEvents.PINICKNAME);

                startActivityForResult(new Intent(this, EditNickNameActivity.class), REQUEST_EDIT_NICK_NAME_ACTIVITY);
                break;
            //修改密码
            case R.id.fl_navigate_revise_pwd:
                //pv，uv统计
//                DataStatisticsHelper.newInstance().onCountUv(NewVersionEvents.PICHANGEPWD);

                if (UserHelper.getInstance(this).getProfile() == null || UserHelper.getInstance(this).getProfile().getAccount() == null) {
                    return;
                }
                Intent toResetPsd = new Intent(this, ResetPsdActivity.class);
                toResetPsd.putExtra("tileName", "设置新密码");
                startActivity(toResetPsd);
                break;
            //安全退出
            case R.id.tv_user_profile_exit:
                //umeng统计
                Statistic.onEvent(Events.SETTING_EXIT);
                new CommNoneAndroidDialog().withMessage("确认退出" + getString(R.string.app_name))
                        .withPositiveBtn("再看看", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                //umeng统计
                                Statistic.onEvent(Events.EXIT_DISMISS);
                            }
                        })
                        .withNegativeBtn("退出", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                //umeng统计
                                Statistic.onEvent(Events.EXIT_CONFIRM);

                                presenter.logout();
                            }
                        }).show(getSupportFragmentManager(), CommNoneAndroidDialog.class.getSimpleName());
                break;
            //版本号
            case R.id.fl_version_code:
                //pv，uv统计
//                DataStatisticsHelper.newInstance().onCountUv(NewVersionEvents.PIVERSIONNUMBER);

                presenter.checkVersion();
                break;
            //清除缓存
            case R.id.fl_clear_cache:
                //pv，uv统计
//                DataStatisticsHelper.newInstance().onCountUv(NewVersionEvents.PICLEARCACHE);

                boolean a = DataCleanManager.cleanInternalCache();
                boolean b = DataCleanManager.cleanExternalCache();
                if (a && b) {
                    cacheSize.setText("0M");
                    Toast.makeText(this, "缓存已清除", Toast.LENGTH_SHORT).show();
                } else {
                    cacheSize.setText("清除失败");
                }
                break;
            //关于我们
            case R.id.fl_about_us:
                //pv，uv统计
//                DataStatisticsHelper.newInstance().onCountUv(NewVersionEvents.PIABOUTUS);

                Intent toAboutUs = new Intent(this, AboutUsActivity.class);
                startActivity(toAboutUs);
                break;
//            //修改手机号
//            case R.id.fl_revise_mobile:
//
//                //pv，uv统计
////                DataStatisticsHelper.newInstance().onCountUv(NewVersionEvents.PIPHONE);
//
//                Intent toNewMobile = new Intent(this, InputNewMobileActivity.class);
//                toNewMobile.putExtra("bingNewMobile", "bingNewMobile");
//                startActivity(toNewMobile);
//                break;
            //解除微信绑定
//            case R.id.fl_remove_wx_chat:
//                //pv，uv统计
////                DataStatisticsHelper.newInstance().onCountUv(NewVersionEvents.PIWECHAT);
//
//                if ("未绑定".equals(userProfileWxChat.getText().toString())) {
//                    bindWXChat();
//                } else {
//                    new CommNoneAndroidDialog().withTitle("解除绑定").withMessageByGray("确定要解绑微信？")
//                            .withNegativeBtn("确定", new View.OnClickListener() {
//                                @Override
//                                public void onClick(View v) {
//                                    unBindWXChat();
//                                }
//                            })
//                            .withPositiveBtn("退出", new View.OnClickListener() {
//                                @Override
//                                public void onClick(View v) {
//
//                                }
//                            }).show(getSupportFragmentManager(), CommNoneAndroidDialog.class.getSimpleName());
//                }
//                break;
            //编辑头像
            case R.id.avatar:
            case R.id.avatar_item:
                showAvatarSelector();

                //pv，uv统计
//                DataStatisticsHelper.newInstance().onCountUv(NewVersionEvents.PIHEADPORTRAIT);
                break;
            case R.id.fl_sex:
                PopUtils.showBottomPopWindow(R.layout.dialog_user_choose_sex, getSupportFragmentManager(), this, this);
                break;
            case R.id.fl_produce:
                Intent intent = new Intent(this, EditProduceActivity.class);
                intent.putExtra("sex", mSex);
                startActivityForResult(intent, EditProduceActivity.RESULT_OK_EDIT_PRODUCE_ACTIVITY);
                break;
            default:
                break;
        }
    }

    /**
     * 绑定微信
     */
    private void bindWXChat() {
        UMAuthListener listener = new UMAuthListener() {

            @Override
            public void onStart(SHARE_MEDIA share_media) {
            }

            @Override
            public void onComplete(SHARE_MEDIA share_media, int i, Map<String, String> map) {
                String openid = map.get("openid");
                String unionid = map.get("unionid");
                Log.e("asdfdas", "openID---> " + openid);
                Log.e("asdfdas", "unionid---> " + unionid);

                Api.getInstance().bindWXChat(UserHelper.getInstance(UserProfileActivity.this).getProfile().getId(), unionid)
                        .compose(RxUtil.<ResultEntity>io2main())
                        .subscribe(new Consumer<ResultEntity>() {
                                       @Override
                                       public void accept(@NonNull ResultEntity result) throws Exception {
                                           showErrorMsg(result.getMsg());
                                           if (result.isSuccess()) {
                                               userProfileWxChat.setText("已绑定");
                                           }
                                       }
                                   },
                                new Consumer<Throwable>() {
                                    @Override
                                    public void accept(@io.reactivex.annotations.NonNull Throwable throwable) throws Exception {
                                        showErrorMsg("网络错误");
                                    }
                                });

            }

            @Override
            public void onError(SHARE_MEDIA share_media, int i, Throwable throwable) {
                Log.e("xjb", "i __ " + i + "throwble -- " + throwable.getMessage());
                WeakRefToastUtil.showShort(UserProfileActivity.this, "授权失败", null);
            }

            @Override
            public void onCancel(SHARE_MEDIA share_media, int i) {
                WeakRefToastUtil.showShort(UserProfileActivity.this, "授权取消", null);
            }
        };
        UMShareAPI.get(UserProfileActivity.this).getPlatformInfo(this, SHARE_MEDIA.WEIXIN, listener);
    }

    /**
     * 解绑微信
     */
    private void unBindWXChat() {
        Api.getInstance().unBindWXChat(UserHelper.getInstance(this).getProfile().getId())
                .compose(RxUtil.<ResultEntity>io2main())
                .subscribe(new Consumer<ResultEntity>() {
                               @Override
                               public void accept(@NonNull ResultEntity result) throws Exception {
                                   showErrorMsg(result.getMsg());
                                   userProfileWxChat.setText("未绑定");
                               }
                           },
                        new Consumer<Throwable>() {
                            @Override
                            public void accept(@io.reactivex.annotations.NonNull Throwable throwable) throws Exception {
                                showErrorMsg("网络错误");
                            }
                        });
    }


    @Override
    public void onClick(View v) {
        if (avatarSelector != null) {
            avatarSelector.dismiss();
        }
        switch (v.getId()) {
            case R.id.from_camera:
                UserProfileActivityPermissionsDispatcher.openCameraWithCheck(this);
                break;
            case R.id.from_album:
                UserProfileActivityPermissionsDispatcher.openAlbumWithCheck(this);
                break;
            case R.id.tv_man:
                PopUtils.dismiss();
                mSex = 1;
                presenter.fetchSaveUserInfo(mSex);
                break;
            case R.id.tv_woman:
                PopUtils.dismiss();
                mSex = 2;
                presenter.fetchSaveUserInfo(mSex);
                break;
            case R.id.tv_cancel:
                PopUtils.dismiss();
                break;
            default:
                break;
        }
    }

    @SuppressLint("InlinedApi")
    @NeedsPermission({Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE})
    void openCamera() {
        Intent toCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        String dirPath = FileProviderHelper.getTempDirPath(this);
        File dir = new File(dirPath);
        if (!dir.exists()) {
            boolean result = dir.mkdirs();
            LogUtils.i(this.getClass().getSimpleName(), "result of mkdirs " + result);
        }
        String filePath = dirPath + "/" + System.currentTimeMillis() + ".jpg";
        File avatarFile = new File(filePath);
        if (!avatarFile.exists()) {
            try {
                boolean result = avatarFile.createNewFile();
                LogUtils.i(this.getClass().getSimpleName(), "create new avatar file result " + result);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (avatarFile.exists()) {
            Uri uri = null;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                try {
                    uri = FileProvider.getUriForFile(this, FileProviderHelper.getFileProvider(this), avatarFile);
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                }
                toCamera.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            } else {
                uri = Uri.fromFile(avatarFile);
            }
            avatarFilePath = avatarFile.getPath();
            if (uri != null) {
                toCamera.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                startActivityForResult(toCamera, 1);
            } else {
                WeakRefToastUtil.showShort(this, "创建头像文件失败", null);
            }
        } else {
            WeakRefToastUtil.showShort(this, "创建头像文件失败", null);
        }
    }

    @SuppressLint("InlinedApi")
    @NeedsPermission({Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE})
    void openAlbum() {
        Intent toAlbum = new Intent(Intent.ACTION_PICK);
        toAlbum.setType("image/*");
        startActivityForResult(toAlbum, 2);
    }

    String getRealPathFromURI(Uri uri) {
        if (uri != null) {
            //小米手机
            if (uri.getScheme().equals("file")) {
                return uri.getEncodedPath();
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                Cursor cursor = getContentResolver().query(uri, null, null, null, null);
                if (cursor != null) {
                    cursor.moveToFirst();
                    int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                    String path = cursor.getString(idx);
                    cursor.close();
                    return path;
                }
            } else {
                return uri.getPath();
            }
        }
        return null;
    }

    private void showAvatarSelector() {
        avatarSelector = new Dialog(this, R.style.AvatarSelectorStyle);
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_avatar_selector, null);
        view.findViewById(R.id.from_camera).setOnClickListener(this);
        view.findViewById(R.id.from_album).setOnClickListener(this);
        view.findViewById(R.id.cancel).setOnClickListener(this);
        avatarSelector.setContentView(view);
        avatarSelector.setCanceledOnTouchOutside(true);
        Window window = avatarSelector.getWindow();
        if (window != null) {
            WindowManager.LayoutParams lp = window.getAttributes();
            window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
            window.setAttributes(lp);
            window.setGravity(Gravity.BOTTOM);
        }
        avatarSelector.show();
    }

    @Override
    public void setPresenter(UserProfileContract.Presenter presenter) {
        //injected.nothing to do.
    }

    @Override
    public void showProfile(UserHelper.Profile profile) {
        if (profile.getHeadPortrait() != null) {
            Glide.with(this)
                    .load(profile.getHeadPortrait())
                    .asBitmap()
                    .into(avatarIv);
        }
        if (profile.getUserName() != null) {
            nickNameTv.setText(profile.getUserName());
        }
        if (profile.getBingPhone() != null) {
            userProfileMobile.setText(CommonUtils.changeTel(profile.getBingPhone()));
        } else {
            userProfileMobile.setText("未绑定");
        }
        if (!TextUtils.isEmpty(profile.getWxUnionId())) {
            userProfileWxChat.setText("已绑定");
            wxFrame.setVisibility(View.VISIBLE);
            btomView.setVisibility(View.VISIBLE);
        } else {
            //userProfileWxChat.setText("未绑定");
            wxFrame.setVisibility(View.GONE);
            btomView.setVisibility(View.GONE);
        }
    }


    @Override
    public void showAvatarUpdateSuccess(String avatar) {
        dismissProgress();
        if (avatar != null) {
            Glide.with(this)
                    .load(avatar)
                    .asBitmap()
                    .into(avatarIv);
        }
    }

    /**
     * 退出登录
     */
    @Override
    public void showLogoutSuccess() {

        //发送用户退出全局事件
        EventBus.getDefault().post(new UserLogoutEvent());
        finish();
    }

    @Override
    public void showUserName(String name) {

    }

    @Override
    public void showUpdateNameSuccess(String msg) {
        dismissProgress();
        WeakRefToastUtil.showShort(this, msg, null);
    }

    @Override
    public void showLatestVersion(String version) {
//        if (version != null) {
//            versionNameTv.setText(version);
//        }
    }

    @Override
    public void showUpdate(AppUpdate update) {
        final AppUpdate appInfo = update;
        CommNoneAndroidDialog dialog = new CommNoneAndroidDialog()
                .withMessage(update.getContent())
                .withPositiveBtn("立即更新", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        updateHelper.processAppUpdate(appInfo, UserProfileActivity.this);
                    }
                })
                .withNegativeBtn("稍后再说", null);
        dialog.setCancelable(false);
        dialog.show(getSupportFragmentManager(), "Update");
    }

    @Override
    public void onUpdateSexSucceed() {
        if (mSex == 1) {
            tvSex.setText("男");
        } else {
            tvSex.setText("女");
        }
    }

    @Override
    public void showHasBeenLatest(String msg) {
        WeakRefToastUtil.showShort(this, msg, null);
    }

    @Override
    public void initPop(View view, PopDialog mPopDialog) {
        view.findViewById(R.id.tv_man).setOnClickListener(this);
        view.findViewById(R.id.tv_woman).setOnClickListener(this);
        view.findViewById(R.id.tv_cancel).setOnClickListener(this);
    }
}
