package com.beihui.market.ui.activity;

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
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.beihui.market.R;
import com.beihui.market.base.BaseComponentActivity;
import com.beihui.market.entity.AppUpdate;
import com.beihui.market.helper.DataCleanManager;
import com.beihui.market.helper.FileProviderHelper;
import com.beihui.market.helper.SlidePanelHelper;
import com.beihui.market.helper.UserHelper;
import com.beihui.market.helper.updatehelper.AppUpdateHelper;
import com.beihui.market.injection.component.AppComponent;
import com.beihui.market.injection.component.DaggerUserProfileComponent;
import com.beihui.market.injection.module.UserProfileModule;
import com.beihui.market.ui.busevents.UserLogoutEvent;
import com.beihui.market.ui.contract.UserProfileContract;
import com.beihui.market.ui.dialog.CommNoneAndroidDialog;
import com.beihui.market.ui.dialog.NicknameDialog;
import com.beihui.market.ui.presenter.UserProfilePresenter;
import com.beihui.market.umeng.Events;
import com.beihui.market.umeng.Statistic;
import com.beihui.market.util.CommonUtils;
import com.beihui.market.util.ImageUtils;
import com.beihui.market.util.LogUtils;
import com.beihui.market.util.viewutils.ToastUtils;
import com.beihui.market.view.CircleImageView;
import com.bumptech.glide.Glide;
import com.gyf.barlibrary.ImmersionBar;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.io.IOException;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;

/**
 * 个人中心页面
 */
@RuntimePermissions
public class UserProfileActivity extends BaseComponentActivity implements UserProfileContract.View, View.OnClickListener {

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
//    @BindView(R.id.phone)
//    TextView phoneTv;
//    @BindView(R.id.profession)
//    TextView professionTv;

    @Inject
    UserProfilePresenter presenter;

    private Dialog avatarSelector;

    private String avatarFilePath;

    private AppUpdateHelper updateHelper = AppUpdateHelper.newInstance();

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
    }

    @Override
    public void initDatas() {

    }

    @Override
    protected void configureComponent(AppComponent appComponent) {
        DaggerUserProfileComponent.builder()
                .appComponent(appComponent)
                .userProfileModule(new UserProfileModule(this))
                .build()
                .inject(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 10 && requestCode == REQUEST_EDIT_NICK_NAME_ACTIVITY && data != null) {
            nickNameTv.setText(data.getStringExtra("updateNickName"));
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
                ToastUtils.showShort(this, "头像解析错误", null);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        UserProfileActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

//    @OnClick({R.id.avatar,R.id.avatar_item, R.id.nick_name_item, R.id.profession_item})
    @OnClick({R.id.avatar,R.id.avatar_item, R.id.fl_navigate_nick_name, R.id.fl_navigate_revise_pwd, R.id.tv_user_profile_exit,
                R.id.fl_version_code, R.id.fl_clear_cache, R.id.fl_about_us, R.id.fl_revise_mobile, R.id.fl_remove_wx_chat})
    void OnItemsClicked(View view) {
        switch (view.getId()) {
            //编辑昵称
            case R.id.fl_navigate_nick_name:
                startActivityForResult(new Intent(this, EditNickNameActivity.class), REQUEST_EDIT_NICK_NAME_ACTIVITY);
                break;
            //修改密码
            case R.id.fl_navigate_revise_pwd:
//                UserCertificationCodeActivity.launch(this, UserHelper.getInstance(this).getProfile().getUserName());
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
                presenter.checkVersion();
                break;
            //清除缓存
            case R.id.fl_clear_cache:
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
                Intent toAboutUs = new Intent(this, AboutUsActivity.class);
                startActivity(toAboutUs);
                break;
            //修改手机号
            case R.id.fl_revise_mobile:
                Intent toNewMobile = new Intent(this, InputNewMobileActivity.class);
                startActivity(toNewMobile);
                break;
            //解除微信绑定
            case R.id.fl_remove_wx_chat:
                new CommNoneAndroidDialog().withTitle("解除绑定").withMessageByGray("确定要解绑微信")
                        .withPositiveBtn("取消", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                            }
                        })
                        .withNegativeBtn("确定", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                            }
                        }).show(getSupportFragmentManager(), CommNoneAndroidDialog.class.getSimpleName());
                break;
            //编辑头像
            case R.id.avatar:
            case R.id.avatar_item:
                showAvatarSelector();
                break;
//            case R.id.nick_name_item:
//                Intent toEditName = new Intent(this, EditNickNameActivity.class);
//                startActivity(toEditName);

//                new NicknameDialog().setNickNameChangedListener(new NicknameDialog.NickNameChangedListener() {
//                    @Override
//                    public void onNickNameChanged(String nickName) {
//                        nickNameTv.setText(nickName);
//                        presenter.updateUserName(nickName);
//                    }
//                }).setNickName(nickNameTv.getText().toString()).show(getSupportFragmentManager(), "nickName");

//                break;
//            case R.id.profession_item:
//                Intent toEditJob = new Intent(this, EditJobGroupActivity.class);
//                startActivity(toEditJob);
//                break;
        }
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
                ToastUtils.showShort(this, "创建头像文件失败", null);
            }
        } else {
            ToastUtils.showShort(this, "创建头像文件失败", null);
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
//        if (profile.getAccount() != null) {
//            phoneTv.setText(CommonUtils.phone2Username(profile.getAccount()));
//        }
//        if (profile.getProfession() != null) {
//            professionTv.setText(profile.getProfession());
//        }
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

        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    @Override
    public void showUserName(String name) {

    }

    @Override
    public void showUpdateNameSuccess(String msg) {
        dismissProgress();
        ToastUtils.showShort(this, msg, null);
    }

    @Override
    public void showLatestVersion(String version) {
        if (version != null) {
            versionNameTv.setText(version);
        }
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
    public void showHasBeenLatest(String msg) {
        ToastUtils.showShort(this, msg, null);
    }
}
