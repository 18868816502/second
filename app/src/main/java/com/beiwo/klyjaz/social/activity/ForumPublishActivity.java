package com.beiwo.klyjaz.social.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.InputFilter;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.beiwo.klyjaz.R;
import com.beiwo.klyjaz.base.BaseComponentActivity;
import com.beiwo.klyjaz.injection.component.AppComponent;
import com.beiwo.klyjaz.social.bean.DraftEditForumBean;
import com.beiwo.klyjaz.social.classhelper.ForumPublishHelper;
import com.beiwo.klyjaz.social.contract.ForumPublishContact;
import com.beiwo.klyjaz.social.impl.ForumTitleWatcher;
import com.beiwo.klyjaz.social.presenter.ForumPublishPresenter;
import com.beiwo.klyjaz.social.utils.InputFilterUtils;
import com.beiwo.klyjaz.util.PopUtils;
import com.beiwo.klyjaz.util.ToastUtil;
import com.gyf.barlibrary.ImmersionBar;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;
import com.zhihu.matisse.internal.entity.CaptureStrategy;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * @author chenguoguo
 * @name loanmarket_social
 * @class name：com.beiwo.klyjaz.social.activity
 * @descripe 动态发布页
 * @time 2018/11/06 14:54
 */
public class ForumPublishActivity extends BaseComponentActivity implements ForumPublishContact.View, View.OnClickListener {

    public static final int REQUEST_CODE_CHOOSE = 23;

    @BindView(R.id.tool_bar)
    RelativeLayout rlTitleBar;
    @BindView(R.id.navigate)
    ImageView ivNavigate;
    @BindView(R.id.tv_publish)
    TextView tvPublish;
    @BindView(R.id.head_container)
    LinearLayout headContainer;

    @BindView(R.id.et_publish_title)
    EditText etPublishTitle;
    @BindView(R.id.tv_publish_title_num)
    TextView tvPublishTitleNum;
    @BindView(R.id.et_publish_content)
    EditText etPublishContent;

    private ForumPublishPresenter mPresenter;
    private ForumPublishHelper helper;

    /**
     * 动态标题
     */
    private String mForumTitle;
    /**
     * 动态内容
     */
    private String mForumContent;

    /**
     * 草稿箱进入，发布需要带上该参数
     */
    private String forumId;

    /**
     * 0 发布 3 保存草稿
     */
    private int status;

    @Override
    public int getLayoutId() {
        return R.layout.activity_forum_publish;
    }

    @Override
    public void configViews() {
        ImmersionBar.with(this).titleBar(rlTitleBar).statusBarDarkFont(true).init();
        mPresenter = new ForumPublishPresenter(this, this);
        helper = new ForumPublishHelper(this);
        headContainer.addView(helper.initHead(this));
        ivNavigate.setOnClickListener(this);
        tvPublish.setOnClickListener(this);

        etPublishTitle.addTextChangedListener(new ForumTitleWatcher(this,tvPublishTitleNum));
        etPublishTitle.setFilters(new InputFilter[]{InputFilterUtils.emojiFilter, new InputFilter.LengthFilter(30)});
        etPublishContent.setFilters(new InputFilter[]{InputFilterUtils.emojiFilter, new InputFilter.LengthFilter(1500)});

    }

    @Override
    public void initDatas() {
        if (getIntent() != null) {
            forumId = getIntent().getStringExtra("forumId");
            if (!TextUtils.isEmpty(forumId)) {
                mPresenter.fetchEditForum(forumId);
            }
        }
    }

    @Override
    protected void configureComponent(AppComponent appComponent) {

    }

    @Override
    public void onPublishTopicSucceed() {
        switch (status) {
            case 0:
                ToastUtil.toast(getString(R.string.social_forum_comment_send_tips));
                break;
            case 3:
                ToastUtil.toast(getString(R.string.social_forum_save_succeed));
                break;
            default:
                break;
        }
        finish();
    }

    @Override
    public void onUploadImgSucceed(String imgKey) {
        dismissProgress();
        getForumText();
        mPresenter.fetchPublishTopic("", mForumTitle, mForumContent, status, "", forumId);
    }

    @Override
    public void onUploadImgFailed() {
        ToastUtil.toast(getString(R.string.social_forum_upload_failure));
    }

    @Override
    public void onEditForumSucceed(DraftEditForumBean forumBean) {
        List<String> imgkeys = new ArrayList<>();
        if (forumBean != null) {
            etPublishTitle.setText(forumBean.getTitle());
            etPublishContent.setText(forumBean.getContent());
            if (forumBean.getImgKey() != null && forumBean.getImgKey().size() != 0) {
                for (int i = 0; i < forumBean.getImgKey().size(); i++) {
                    imgkeys.add(forumBean.getImgKey().get(i).getImgUrl());
                }
            }
            helper.setDraftUrls(imgkeys);
        }
    }

    @Override
    public void setPresenter(ForumPublishContact.Presenter presenter) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //返回
            case R.id.navigate:
                //先判断是否有数据
                showSaveDraftDialog();
                break;
            //提交
            case R.id.tv_publish:
                status = 0;
                checkCommit();
                break;
            case R.id.cancel:
                PopUtils.dismiss();
                finish();
                break;
            case R.id.tv_save:
                status = 3;
                PopUtils.dismiss();
                publishForum();
                break;
            case R.id.tv_cancel:
                PopUtils.dismiss();
                break;
            case R.id.tv_commit:
                publishForum();
                break;
            case R.id.add_container:
                checkPermission();
                break;
            default:
                break;
        }
    }

    /**
     * 提交动态前校验
     */
    private void checkCommit() {
        getForumText();
        if (TextUtils.isEmpty(mForumTitle)) {
            ToastUtil.toast(getString(R.string.social_forum_title_hint));
            return;
        }
        if (TextUtils.isEmpty(mForumContent)) {
            ToastUtil.toast(getString(R.string.social_forum_content_hint));
            return;
        }
        if (mForumContent.length() < 10) {
            ToastUtil.toast(getString(R.string.social_forum_content_tips));
            return;
        }
        PopUtils.showForumCommitDialog(getSupportFragmentManager(),this,this);
    }


    /**
     * 保存草稿或者发布动态
     */
    private void publishForum(){
        mPresenter.addDraftUrls(helper.getDraftUrls());
        if (helper.getBitmapList().size() == 0) {
            getForumText();
            mPresenter.fetchPublishTopic("", mForumTitle, mForumContent, status, "", forumId);
        } else {
            showProgress();
            mPresenter.prepareUpload(helper.getBitmapList());
        }
    }

    /**
     * 显示保存草稿弹窗
     */
    private void showSaveDraftDialog() {
        getForumText();
        if (helper.getListSize() != 0 || !TextUtils.isEmpty(mForumTitle) || !TextUtils.isEmpty(mForumContent)) {
            PopUtils.showForumDraftDialog(getSupportFragmentManager(), this, this);
        } else {
            finish();
        }
    }

    /**
     * 获取动态编辑内容和标题
     */
    private void getForumText(){
        mForumTitle = etPublishTitle.getText().toString();
        mForumContent = etPublishContent.getText().toString();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_CHOOSE && resultCode == RESULT_OK) {
            helper.setDatas(Matisse.obtainPathResult(data));
        }
    }

    /**
     * 检查相机权限
     */
    private void checkPermission(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            //申请WRITE_EXTERNAL_STORAGE权限
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA},
                    1);
        } else {
            openPick();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        doNext(requestCode, grantResults);
    }

    private void doNext(int requestCode, int[] grantResults) {
        if (requestCode == 1) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openPick();
            } else {
                Toast.makeText(this, "请在应用管理中打开“相机”访问权限！", Toast.LENGTH_LONG).show();
                finish();
            }
        }
    }

    private void openPick() {
        Matisse.from(this)
                .choose(MimeType.ofImage(), false)
                //自动增长选择的数目
                .countable(true)
                .capture(true)
                //只显示一种媒体类型（图片或者视频）
                .showSingleMediaType(true)
                .captureStrategy(new CaptureStrategy(true, "com.beiwo.klyjaz.fileprovider", "kaola"))
                //限制最大的选择数目
                .maxSelectable(9 - helper.getListSize())
                .gridExpectedSize(getResources().getDimensionPixelSize(R.dimen.dp120))
                .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
                .thumbnailScale(0.85f)
                .imageEngine(new GlideEngine())
                .originalEnable(true)
                .maxOriginalSize(10)
                .autoHideToolbarOnSingleTap(true)
                .forResult(REQUEST_CODE_CHOOSE);
    }
}
