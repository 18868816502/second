package com.beihui.market.ui.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.beihui.market.R;
import com.beihui.market.base.BaseComponentActivity;
import com.beihui.market.injection.component.AppComponent;
import com.beihui.market.social.bean.DraftEditForumBean;
import com.beihui.market.social.component.DaggerSocialPublishComponent;
import com.beihui.market.social.contract.SocialPublishContract;
import com.beihui.market.social.module.SocialPublishModule;
import com.beihui.market.social.presenter.SocialPublishPresenter;
import com.beihui.market.ui.adapter.CommunityPublishAdapter;
import com.beihui.market.ui.listeners.OnItemClickListener;
import com.beihui.market.ui.listeners.OnSaveEditListener;
import com.beihui.market.util.FileUtils;
import com.beihui.market.util.ImageUtils;
import com.beihui.market.util.ToastUtil;
import com.beihui.market.view.dialog.PopDialog;
import com.gyf.barlibrary.ImmersionBar;
import com.zhihu.matisse.Matisse;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

/**
 * @name loanmarket
 * @class name：com.beihui.market.ui.activity
 * @class describe 社区发布页面
 * @author chenguoguo
 * @time 2018/9/11 18:45
 */
public class CommunityPublishActivity extends BaseComponentActivity implements SocialPublishContract.View,
        View.OnClickListener,PopDialog.OnInitPopListener,OnItemClickListener,OnSaveEditListener {

    public static final int REQUEST_CODE_CHOOSE = 23;

    @BindView(R.id.tool_bar)
    RelativeLayout rlTitleBar;
    @BindView(R.id.navigate)
    ImageView ivNavigate;
    @BindView(R.id.tv_publish)
    TextView tvPublish;
    @BindView(R.id.recycler)
    RecyclerView recyclerView;

    @Inject
    SocialPublishPresenter mPresenter;

    private CommunityPublishAdapter adapter;
    private PopDialog popDialog;
    private int mPopType = 0;
    private List<Uri> uriList;
    private List<String> pathList;
    /**
     * 记录上传失败的base64
     */
    private List<String> failList;
    /**
     * 当前上传位置
     */
    private int uploadIndex = 0;
    /**
     * 选中的图片base64
     */
    private List<Bitmap> base64List;
    private List<String> imgKeys;

    private String mTopicTitle;
    private String mTopicContent;
    private StringBuilder sb;
    private String status = "0";
    private String forumId;

    private List<String> httpUrls;
    private List<String> httpImgKeys;

    @Override
    public int getLayoutId() {
        return R.layout.activity_community_publish;
    }

    @Override
    public void configViews() {
        ImmersionBar.with(this).titleBar(rlTitleBar).statusBarDarkFont(true).init();
        adapter = new CommunityPublishAdapter(this);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        adapter.setOnItemClickListener(this);

        ivNavigate.setOnClickListener(this);
        tvPublish.setOnClickListener(this);
        sb = new StringBuilder();
        getIntentData();
    }

    private void getIntentData() {
        if(getIntent()!=null){
            forumId = getIntent().getStringExtra("forumId");
            if(!TextUtils.isEmpty(forumId)){
                mPresenter.fetchEditForum(forumId);
            }
        }
    }

    @Override
    public void initDatas() {
        failList = new ArrayList<>();
        base64List = new ArrayList<>();
        imgKeys = new ArrayList<>();
        pathList = new ArrayList<>();

        httpUrls = new ArrayList<>();
        httpImgKeys = new ArrayList<>();
    }

    @Override
    protected void configureComponent(AppComponent appComponent) {
        DaggerSocialPublishComponent.builder()
                .appComponent(appComponent)
                .socialPublishModule(new SocialPublishModule(this))
                .build()
                .inject(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.navigate:
                //先判断是否有数据
                mPopType = 0;
                showDialogTips(R.layout.dialog_community_publish_save);
                break;
            case R.id.tv_publish:
                mPopType = 1;
                showDialogTips(R.layout.dialog_community_publish_commit);
                break;
            case R.id.cancel:
            case R.id.tv_cancel:
                popDialog.dismiss();
                break;
            case R.id.tv_save:
                //保存
                status = "0";
                if(uriList == null){
                    mPresenter.fetchPublishTopic("",mTopicTitle,mTopicContent,status,"","");
                }else{
                    uploadImg();
                }
                break;
            case R.id.tv_commit:
                //提交发布的内容
                popDialog.dismiss();
                if(TextUtils.isEmpty(mTopicTitle)){
                    ToastUtil.toast("请填写标题");
                    return;
                }
                if(TextUtils.isEmpty(mTopicContent)){
                    ToastUtil.toast("请填写内容");
                    return;
                }
                status = "3";
                if(uriList == null){
                    mPresenter.fetchPublishTopic("",mTopicTitle,mTopicContent,status,"","");
                }else{
                    uploadImg();
                }
                break;
                default:
                    break;
        }
    }

    /**
     * 提交图片
     */
    private void uploadImg() {
        int size = pathList.size();
        for(int i = 0 ; i < size ; i++){
            Bitmap bitmap = ImageUtils.getFixedBitmap(pathList.get(i), 512);
            base64List.add(bitmap);
        }
        mPresenter.uploadForumImg(base64List.get(uploadIndex));
    }

    /**
     * 根据布局显示弹窗
     * @param layoutId 布局id
     */
    private void showDialogTips(int layoutId){
        popDialog = new PopDialog.Builder(getSupportFragmentManager(),this)
                .setLayoutId(layoutId)
                .setWidth(270)
                .setHeight(120)
                .setGravity(Gravity.CENTER)
                .setCancelableOutside(false)
                .setInitPopListener(this)
                .create();
        popDialog.show();
    }

    @Override
    public void initPop(View view, PopDialog mPopDialog) {
        switch (mPopType){
            case 0:
                view.findViewById(R.id.tv_cancel).setOnClickListener(this);
                view.findViewById(R.id.tv_save).setOnClickListener(this);
                break;
            case 1:
                view.findViewById(R.id.tv_cancel).setOnClickListener(this);
                view.findViewById(R.id.tv_commit).setOnClickListener(this);
                break;
                default:
                    break;
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_CHOOSE && resultCode == RESULT_OK) {
//            adapter.setHeadData(Matisse.obtainResult(data), Matisse.obtainPathResult(data));
//            uriList = Matisse.obtainResult(data);
//            pathList = Matisse.obtainPathResult(data);
            pathList.addAll(Matisse.obtainPathResult(data));
            adapter.setHeadData(pathList);
        }
    }

    @Override
    public void onItemClick(int position) {
        if(pathList != null) {
            pathList.remove(position);
            adapter.setHeadData(pathList);
        }
    }

    @Override
    public void onSaveDraftSucceed() {

    }

    @Override
    public void onPublishTopicSucceed() {
        ToastUtil.toast("发布成功");
        finish();
    }

    @Override
    public void onUploadImgSucceed(String imgKey) {
        uploadIndex ++;
        imgKeys.add(imgKey);
        if(uploadIndex < base64List.size()){
            mPresenter.uploadForumImg(base64List.get(uploadIndex));
            return;
        }

        //所有图片上传完毕
        if(imgKeys.size() == base64List.size()){
            int size = imgKeys.size();
            for(int i = 0 ; i < size ; i++){
                if(i != size - 1){
                    sb.append(imgKeys.get(i)+"#");
                }else{
                    sb.append(imgKeys.get(i));
                }
            }
            mPresenter.fetchPublishTopic(sb.toString(),mTopicTitle,mTopicContent,status,"","");
        }

    }

    @Override
    public void onUploadImgFailed() {
//        failList.add(base64List.get(uploadIndex));
//        uploadIndex ++;
        //如果上传失败则重新上传
        mPresenter.uploadForumImg(base64List.get(uploadIndex));
    }

    @Override
    public void onEditForumSucceed(DraftEditForumBean forumBean) {
        if(forumBean!=null) {
            if(forumBean.getImgKey()!=null&&forumBean.getImgKey().size()!=0){
                for(int i = 0 ; i < forumBean.getImgKey().size() ; i ++ ){
                    httpImgKeys.add(forumBean.getImgKey().get(i).getId());
                    httpUrls.add(forumBean.getImgKey().get(i).getImgUrl());
                }
            }
            adapter.setData(httpUrls,forumBean.getTitle(),forumBean.getContent());
        }
    }

    @Override
    public void setPresenter(SocialPublishContract.Presenter presenter) {

    }

    @Override
    public void onSaveEdit(int flag, String strEdit) {
        if(flag == 1){
            mTopicTitle = strEdit;
        }else{
            mTopicContent = strEdit;
        }
    }
}
