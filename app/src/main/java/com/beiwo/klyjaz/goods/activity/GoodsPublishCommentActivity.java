package com.beiwo.klyjaz.goods.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowInsets;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.beiwo.klyjaz.R;
import com.beiwo.klyjaz.base.BaseComponentActivity;
import com.beiwo.klyjaz.goods.contact.GoodsPublishCommentContact;
import com.beiwo.klyjaz.goods.helper.GoodsHelper;
import com.beiwo.klyjaz.goods.presenter.GoodsPublishCommentPresenter;
import com.beiwo.klyjaz.util.ToastUtil;
import com.beiwo.klyjaz.view.CircleImageView;
import com.bumptech.glide.Glide;
import com.gyf.barlibrary.ImmersionBar;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;
import com.zhihu.matisse.internal.entity.CaptureStrategy;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author chenguoguo
 * @name loanmarket
 * @class name：com.beiwo.klyjaz.goods.activity
 * @descripe 产品发布评论页面
 * @time 2018/12/11 11:19
 */
public class GoodsPublishCommentActivity extends BaseComponentActivity implements View.OnClickListener,GoodsPublishCommentContact.View {

    @BindView(R.id.tool_bar)
    Toolbar toolBar;
    @BindView(R.id.navigate)
    ImageView navigate;
    @BindView(R.id.container)
    LinearLayout viewContainer;
    @BindView(R.id.iv_goods)
    CircleImageView ivGoodsLogo;
    @BindView(R.id.tv_goods_title)
    TextView tvGoodsName;
    @BindView(R.id.tv_goods_descripe)
    TextView tvGoodsDescrip;

    private GoodsHelper mHelper;
    private GoodsPublishCommentPresenter mPresenter;
    private String manageId;

    @Override
    public int getLayoutId() {
        return R.layout.activity_goods_publish_comment;
    }

    @Override
    public void configViews() {
        ImmersionBar.with(this).titleBar(toolBar).statusBarDarkFont(true).init();
        mPresenter = new GoodsPublishCommentPresenter(this,this);
        mHelper = new GoodsHelper(this, this);
        viewContainer.addView(mHelper.init01Layout(viewContainer));
        viewContainer.addView(mHelper.init02Layout(viewContainer));
    }

    @Override
    public void initDatas() {
        String logo = getIntent().getStringExtra("logo");
        String name = getIntent().getStringExtra("name");
        manageId = getIntent().getStringExtra("manageId");
        String rate = getIntent().getStringExtra("rate");
        String maxQuota = getIntent().getStringExtra("maxQuota");
        String term = getIntent().getStringExtra("term");

        Glide.with(this).load(logo).into(ivGoodsLogo);
        tvGoodsName.setText(name);
        StringBuilder sb = new StringBuilder();
        sb.append("额度: ").append(maxQuota).append("  期限:").append(term).append("  日息:").append(rate);
        tvGoodsDescrip.setText(sb.toString());
    }

    @OnClick({R.id.navigate})
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.navigate:
                finish();
                break;
            case R.id.add_container:
                checkPermission();
                break;
            case R.id.tv_evaluate:
                if(mHelper.getType() == -2){
                    ToastUtil.toast("请选择综合评价");
                    return;
                }
                if(mHelper.getLoanStatus() == 0){
                    ToastUtil.toast("请选择是否借到");
                    return;
                }
                if(TextUtils.isEmpty(mHelper.getContent())){
                    ToastUtil.toast("请输入文字评价后再提交");
                    return;
                }
                showProgress();
                if(mHelper.getBitmapList().size() == 0){
                    mPresenter.fetchPublishComment(
                            manageId,
                            mHelper.getLoanStatus(),
                            mHelper.getFlag(),
                            mHelper.getType(),
                            "",
                            mHelper.getContent(),
                            "");
                }else{
                    mPresenter.prepareUpload(mHelper.getBitmapList());
                }
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 200 && resultCode == RESULT_OK) {
            mHelper.setPhotos(Matisse.obtainPathResult(data));
        }
    }

    /**
     * 检查相机权限
     */
    private void checkPermission() {
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
                .captureStrategy(new CaptureStrategy(true, getPackageName() + ".fileprovider", "kaola"))
                //限制最大的选择数目
                .maxSelectable(4)
                .gridExpectedSize(getResources().getDimensionPixelSize(R.dimen.dp120))
                .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
                .thumbnailScale(0.85f)
                .imageEngine(new GlideEngine())
                .originalEnable(true)
                .maxOriginalSize(10)
                .autoHideToolbarOnSingleTap(true)
                .forResult(200);
    }

    @Override
    public void onPublishCommentSucceed() {
        ToastUtil.toast("点评成功，感谢您的反馈！");
        finish();
    }

    @Override
    public void onUploadImgSucceed(String imgKey) {
        dismissProgress();
        mPresenter.fetchPublishComment(
                manageId,
                mHelper.getLoanStatus(),
                mHelper.getFlag(),
                mHelper.getType(),
                "",
                mHelper.getContent(),
                "");
    }

    @Override
    public void setPresenter(GoodsPublishCommentContact.Presenter presenter) {

    }

    /**
     * 点击输入法外，隐藏输入法
     * @param ev
     * @return
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            //当isShouldHideInput(v, ev)为true时，表示的是点击输入框区域，则需要显示键盘，同时显示光标，反之，需要隐藏键盘、光标
            if (isShouldHideInput(v, ev)) {

                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    v.clearFocus();
                }
            }
            return super.dispatchTouchEvent(ev);
        }
        // 必不可少，否则所有的组件都不会有TouchEvent了
        if (getWindow().superDispatchTouchEvent(ev)) {
            return true;
        }
        return onTouchEvent(ev);
    }

    public  boolean isShouldHideInput(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] leftTop = { 0, 0 };
            //获取输入框当前的location位置
            v.getLocationInWindow(leftTop);
            int left = leftTop[0];
            int top = leftTop[1];
            int bottom = top + v.getHeight();
            int right = left + v.getWidth();
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                // 点击的是输入框区域，保留点击EditText的事件
                return false;
            } else {
                return true;
            }
        }
        return false;
    }
}
