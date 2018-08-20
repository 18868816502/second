package com.beihui.market.ui.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.ValueCallback;
import android.widget.RelativeLayout;

import com.beihui.market.R;
import com.beihui.market.api.Api;
import com.beihui.market.api.NetConstants;
import com.beihui.market.base.BaseComponentActivity;
import com.beihui.market.entity.UpLoadBean;
import com.beihui.market.entity.request.RequestConstants;
import com.beihui.market.helper.SlidePanelHelper;
import com.beihui.market.helper.UserHelper;
import com.beihui.market.injection.component.AppComponent;
import com.beihui.market.tang.rx.RxResponse;
import com.beihui.market.tang.rx.observer.ApiObserver;
import com.beihui.market.ui.dialog.AlertDialog;
import com.beihui.market.util.ImageUtils;
import com.beihui.market.util.viewutils.ToastUtils;
import com.google.gson.Gson;
import com.gyf.barlibrary.ImmersionBar;
import com.just.agentweb.AgentWeb;

import java.io.ByteArrayOutputStream;

import butterknife.BindView;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;

@RuntimePermissions
public class DailyMissonActivity extends BaseComponentActivity {

    @BindView(R.id.tool_bar)
    Toolbar toolbar;
    @BindView(R.id.web_view_invitation)
    RelativeLayout relativeLayout;
    private Context context;
    private AgentWeb agentWeb;
    String imgType;
    String activeName;
    String path = null;
    private Bitmap image;


    @Override
    public int getLayoutId() {
        return R.layout.activity_daily_misson_layout;
    }

    @Override
    public void configViews() {
        setupToolbar(toolbar);
        setupToolbarBackNavigation(toolbar, R.drawable.back_white);
        ImmersionBar.with(this).statusBarDarkFont(false).init();
        SlidePanelHelper.attach(this);
        context = this;

    }

    @Override
    public void initDatas() {
        agentWeb = AgentWeb.with(this)
                .setAgentWebParent(relativeLayout, new RelativeLayout.LayoutParams(-1, -1)).
                        useDefaultIndicator(getResources().getColor(R.color.red), 1)
                .createAgentWeb()
                .ready()
                .go(NetConstants.missionUrl(UserHelper.getInstance(context).getProfile().getId()));
        agentWeb.getAgentWebSettings().getWebSettings().setJavaScriptEnabled(true);
        agentWeb.getJsInterfaceHolder().addJavaObject("android", new JsInterration());
    }

    @Override
    protected void configureComponent(AppComponent appComponent) {

    }

    @Override
    protected void onActivityResult(final int requestCode, int resultCode, final Intent data) {

        if (resultCode == RESULT_OK && requestCode == 2) {
            agentWeb.getJsAccessEntrace().callJs("uploadPhoto()", new ValueCallback<String>() {
                @Override
                public void onReceiveValue(String value) {
                    UpLoadBean bean = new Gson().fromJson(value, UpLoadBean.class);
                    imgType = bean.getImgType();
                    activeName = bean.getActiveName();
                    path = getRealPathFromURI(data.getData());

                    if (path != null) {
                        ByteArrayOutputStream baos;
                        image = ImageUtils.getFixedBitmap(path, 512);
                        if (image != null) {
                            baos = new ByteArrayOutputStream();
                            int quality = 100;
                            image.compress(Bitmap.CompressFormat.JPEG, quality, baos);
                            while (baos.size() > RequestConstants.AVATAR_BYTE_SIZE) {
                                quality -= 5;
                                if (quality <= 0) {
                                    quality = 0;
                                }
                                baos.reset();
                                image.compress(Bitmap.CompressFormat.JPEG, quality, baos);
                                if (quality == 0) {
                                    break;
                                }
                            }
                            uploadImg(baos.toByteArray(), imgType, activeName);
                        } else {
                            ToastUtils.showShort(DailyMissonActivity.this, "图片解析错误", null);
                        }
                    }

                }
            });

        }
        if (resultCode == RESULT_OK && requestCode == 1) {
            agentWeb.getJsAccessEntrace().callJs("uploadPhotoOwn()", new ValueCallback<String>() {
                @Override
                public void onReceiveValue(String value) {
                    UpLoadBean bean = new Gson().fromJson(value, UpLoadBean.class);
                    imgType = bean.getImgType();
                    activeName = bean.getActiveName();
                    path = getRealPathFromURI(data.getData());

                    if (path != null) {
                        ByteArrayOutputStream baos;
                        image = ImageUtils.getFixedBitmap(path, 512);
                        if (image != null) {
                            baos = new ByteArrayOutputStream();
                            int quality = 100;
                            image.compress(Bitmap.CompressFormat.JPEG, quality, baos);
                            while (baos.size() > RequestConstants.AVATAR_BYTE_SIZE) {
                                quality -= 5;
                                if (quality <= 0) {
                                    quality = 0;
                                }
                                baos.reset();
                                image.compress(Bitmap.CompressFormat.JPEG, quality, baos);
                                if (quality == 0) {
                                    break;
                                }
                            }
                            uploadImg(baos.toByteArray(), imgType, activeName);
                        } else {
                            ToastUtils.showShort(DailyMissonActivity.this, "图片解析错误", null);
                        }
                    }

                }
            });


        }
    }

    private void uploadImg(final byte[] base64Img, final String imgType, final String activeName) {
        new AlertDialog(this).builder().setMsg("1天只有1次上传机会，经由人工审核，非贷超注册成功页面，奖励不予发放!").setTitle("确认上传该图片么？")
                .setPositiveButton("确认", new View.OnClickListener() {

                    @Override
                    public void onClick(View arg0) {
                        showProgress();
                        Api.getInstance().uploadImg(UserHelper.getInstance(DailyMissonActivity.this).getProfile().getId(),
                                UserHelper.getInstance(DailyMissonActivity.this).getProfile().getAccount(),
                                imgType,
                                activeName,
                                base64Img).compose(RxResponse.compatO()).subscribe(new ApiObserver<Object>() {
                            @Override
                            public void onNext(Object data) {
                                dismissProgress();
                                new AlertDialog(DailyMissonActivity.this).builder().setPositiveButton("我知道了", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                    }
                                }).setTitle("上传成功！").setMsg("我们会尽快审核您上传的图片，审核通过后，奖励到账！您可以至”我的-消息“处查看审核情况。").show();
                            }

                            @Override
                            public void onError(Throwable t) {
                                super.onError(t);
                                dismissProgress();
                            }
                        });
                    }

                }).setNegativeButton("取消", new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
            }
        }).show();

    }

    @SuppressLint("InlinedApi")
    @NeedsPermission({Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE})
    void openAlbum() {
        Intent toAlbum = new Intent(Intent.ACTION_PICK);
        toAlbum.setType("image/*");
        startActivityForResult(toAlbum, 2);
    }

    @SuppressLint("InlinedApi")
    @NeedsPermission({Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE})
    void openAlbumOwn() {
        Intent toAlbum = new Intent(Intent.ACTION_PICK);
        toAlbum.setType("image/*");
        startActivityForResult(toAlbum, 1);
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

    public class JsInterration {

        @JavascriptInterface
        void uploadPhoto() {
            DailyMissonActivityPermissionsDispatcher.openAlbumWithCheck(DailyMissonActivity.this);

        }

        @JavascriptInterface
        void uploadPhotoOwn() {
            DailyMissonActivityPermissionsDispatcher.openAlbumOwnWithCheck(DailyMissonActivity.this);

        }

        @JavascriptInterface
        void enterInviteCode() {
            startActivity(new Intent(context, EnterInviteCodeActivity.class));
        }

        @JavascriptInterface
        void enterLoanMarket() {
            Intent intent = new Intent(context, MainActivity.class);
            intent.putExtra("loan", true);
            startActivity(intent);
        }

    }

}
