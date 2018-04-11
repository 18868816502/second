package com.beihui.market.ui.activity;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.beihui.market.R;
import com.beihui.market.api.Api;
import com.beihui.market.api.NetConstants;
import com.beihui.market.api.ResultEntity;
import com.beihui.market.base.BaseComponentActivity;
import com.beihui.market.entity.request.RequestConstants;
import com.beihui.market.helper.FileProviderHelper;
import com.beihui.market.helper.SlidePanelHelper;
import com.beihui.market.helper.UserHelper;
import com.beihui.market.injection.component.AppComponent;
import com.beihui.market.util.ImageUtils;
import com.beihui.market.util.LogUtils;
import com.beihui.market.util.RxUtil;
import com.beihui.market.util.viewutils.ToastUtils;
import com.beihui.market.view.EditTextUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;

import butterknife.BindView;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;

@RuntimePermissions
public class HelpAndFeedActivity extends BaseComponentActivity {

    private final int REQUEST_CODE_CAMERA = 1;
    private final int REQUEST_CODE_ALBUM = 2;

    @BindView(R.id.tool_bar)
    Toolbar toolbar;
    @BindView(R.id.help)
    TextView helpTv;
    @BindView(R.id.feedback)
    TextView feedbackTv;
    @BindView(R.id.view_pager)
    ViewPager viewPager;

    private ProgressBar progressBar;
    private WebView webView;

    private EditText etFeedContent;
    private TextView tvContentNum;
    private ImageView ivFeedImage;

    private String imageFilePath;
    private Bitmap image;

    /**
     * 当前选中的tab
     */
    private View selected;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            String path = null;
            if (requestCode == REQUEST_CODE_CAMERA) {
                //相机
                path = imageFilePath;
            } else if (requestCode == REQUEST_CODE_ALBUM) {
                //相册
                path = ImageUtils.getRealPathFromURI(this, data.getData());
            }
            if (path != null) {
                image = ImageUtils.getFixedBitmap(path, 512);
            }

            if (image != null) {
                ivFeedImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
                ivFeedImage.setImageBitmap(image);
            } else {
                ToastUtils.showShort(this, "图片解析错误", null);
            }

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        HelpAndFeedActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    @Override
    public int getLayoutId() {
        return R.layout.actiivty_debt_help_and_feed;
    }

    @Override
    public void configViews() {
        setupToolbar(toolbar);
        viewPager.setAdapter(new HelpFeedAdapter());

        View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v != selected) {
                    select(v);
                }
            }
        };
        helpTv.setOnClickListener(clickListener);
        feedbackTv.setOnClickListener(clickListener);
        this.select(helpTv);

        SlidePanelHelper.attach(this);
    }

    @Override
    public void initDatas() {
    }

    @Override
    protected void configureComponent(AppComponent appComponent) {

    }

    @NeedsPermission({Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE})
    void openCamera() {
        Intent toCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        String dirPath = FileProviderHelper.getTempDirPath(this);
        File dir = new File(dirPath);
        if (!dir.exists()) {
            if (!dir.mkdirs()) {
                LogUtils.i(this.getClass().getSimpleName(), "create dir failed");
                return;
            }
        }
        String filePath = dirPath + "/" + System.currentTimeMillis() + ".jpg";
        File imageFile = new File(filePath);
        if (!imageFile.exists()) {
            try {
                if (!imageFile.createNewFile()) {
                    LogUtils.i(this.getClass().getSimpleName(), "create new image file failed");
                    return;
                }
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
        }

        Uri uri = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            try {
                uri = FileProvider.getUriForFile(this, FileProviderHelper.getFileProvider(this), imageFile);
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            }
            toCamera.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        } else {
            uri = Uri.fromFile(imageFile);
        }
        imageFilePath = imageFile.getPath();
        if (uri != null) {
            toCamera.putExtra(MediaStore.EXTRA_OUTPUT, uri);
            startActivityForResult(toCamera, REQUEST_CODE_CAMERA);
        } else {
            ToastUtils.showShort(this, "创建图像文件失败", null);
        }
    }

    @SuppressLint("InlinedApi")
    @NeedsPermission({Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE})
    void openAlbum() {
        Intent toAlbum = new Intent(Intent.ACTION_PICK);
        toAlbum.setType("image/*");
        startActivityForResult(toAlbum, REQUEST_CODE_ALBUM);
    }

    private void showImagePicker() {
        final Dialog dialog = new Dialog(this, R.style.AvatarSelectorStyle);
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_avatar_selector, null);
        View.OnClickListener onClickListener = new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();
                switch (v.getId()) {
                    case R.id.from_camera:
                        HelpAndFeedActivityPermissionsDispatcher.openCameraWithCheck(HelpAndFeedActivity.this);
                        break;
                    case R.id.from_album:
                        HelpAndFeedActivityPermissionsDispatcher.openAlbumWithCheck(HelpAndFeedActivity.this);
                        break;
                }
            }
        };
        view.findViewById(R.id.from_camera).setOnClickListener(onClickListener);
        view.findViewById(R.id.from_album).setOnClickListener(onClickListener);
        view.findViewById(R.id.cancel).setOnClickListener(onClickListener);
        dialog.setContentView(view);
        dialog.setCanceledOnTouchOutside(true);
        Window window = dialog.getWindow();
        if (window != null) {
            WindowManager.LayoutParams lp = window.getAttributes();
            window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
            window.setAttributes(lp);
            window.setGravity(Gravity.BOTTOM);
        }
        dialog.show();
    }

    /**
     * 选中tab
     *
     * @param view tab view
     */
    private void select(View view) {
        if (selected != null) {
            selected.setSelected(false);
        }
        selected = view;
        if (selected != null) {
            selected.setSelected(true);
        }

        viewPager.setCurrentItem(selected == helpTv ? 0 : 1);
    }

    private View generateHelpView(ViewGroup container) {
        View helpView = LayoutInflater.from(HelpAndFeedActivity.this)
                .inflate(R.layout.pager_item_debt_help, container, false);
        progressBar = helpView.findViewById(R.id.progress_bar);
        webView = helpView.findViewById(R.id.web_view);
        webView.setWebViewClient(new WebViewClient());
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                progressBar.setProgress(newProgress);
                if (newProgress == 100) {
                    progressBar.setVisibility(View.GONE);
                }
            }
        });
        webView.loadUrl(NetConstants.H5_HELP);
        return helpView;
    }

    private View generateFeedView(ViewGroup container) {
        View feedView = LayoutInflater.from(HelpAndFeedActivity.this)
                .inflate(R.layout.pager_item_debt_feed, container, false);
        etFeedContent = feedView.findViewById(R.id.feed_content);
        tvContentNum = feedView.findViewById(R.id.content_num);
        ivFeedImage = feedView.findViewById(R.id.feed_image);

        EditTextUtils.addDisableEmojiInputFilter(etFeedContent);

        etFeedContent.addTextChangedListener(new TextWatcher() {

            int edge = 300;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @SuppressLint("SetTextI18n")
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int len = 0;
                if (etFeedContent.getEditableText().length() > 0) {
                    byte[] bytes = etFeedContent.getText().toString().getBytes(Charset.forName("gb2312"));
                    if (bytes.length > edge * 2) {
                        etFeedContent.getEditableText().delete(etFeedContent.length() - 1, etFeedContent.length());
                    }
                    len = bytes.length / 2;
                }
                tvContentNum.setText((edge - len) + "");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        ivFeedImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showImagePicker();
            }
        });

        feedView.findViewById(R.id.submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Context context = HelpAndFeedActivity.this;
                if (etFeedContent.getText().length() > 0) {
                    Disposable dis = Observable.just(1)
                            .observeOn(Schedulers.io())
                            .flatMap(new Function<Integer, ObservableSource<ResultEntity>>() {
                                @Override
                                public ObservableSource<ResultEntity> apply(Integer source) throws Exception {
                                    ByteArrayOutputStream baos = null;
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
                                    }
                                    return Api.getInstance().submitFeedback(UserHelper.getInstance(context).getProfile().getId(), etFeedContent.getText().toString(),
                                            baos != null ? baos.toByteArray() : null, baos != null ? System.currentTimeMillis() + ".jpg" : null);
                                }
                            })
                            .compose(RxUtil.<ResultEntity>io2main())
                            .subscribe(new Consumer<ResultEntity>() {
                                           @Override
                                           public void accept(ResultEntity result) throws Exception {
                                               if (result.isSuccess()) {
                                                   ToastUtils.showShort(context, "提交成功", null);
                                                   finish();
                                               } else {
                                                   ToastUtils.showShort(context, result.getMsg(), null);
                                               }
                                           }
                                       },
                                    new Consumer<Throwable>() {
                                        @Override
                                        public void accept(Throwable throwable) throws Exception {
                                            Log.e("HelpAndFeedActivity", throwable.toString());
                                        }
                                    });
                } else {
                    ToastUtils.showShort(context, "请输入您的意见", null);
                }
            }
        });
        return feedView;
    }

    class HelpFeedAdapter extends PagerAdapter {

        private View helpView;
        private View feedView;

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            if (position == 0) {
                if (helpView == null) {
                    helpView = generateHelpView(container);
                }
                container.addView(helpView);
                return helpView;
            } else {
                if (feedView == null) {
                    feedView = generateFeedView(container);
                }
                container.addView(feedView);
                return feedView;
            }
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return position == 0 ? "记账说明" : "用户反馈";
        }
    }
}
