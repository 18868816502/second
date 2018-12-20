package com.beiwo.qnejqaz.social.presenter;

import android.content.Context;
import android.graphics.Bitmap;

import com.beiwo.qnejqaz.api.Api;
import com.beiwo.qnejqaz.api.ResultEntity;
import com.beiwo.qnejqaz.base.BaseRxPresenter;
import com.beiwo.qnejqaz.entity.request.RequestConstants;
import com.beiwo.qnejqaz.helper.UserHelper;
import com.beiwo.qnejqaz.social.bean.DraftEditForumBean;
import com.beiwo.qnejqaz.social.contract.ForumPublishContact;
import com.beiwo.qnejqaz.tang.rx.RxResponse;
import com.beiwo.qnejqaz.tang.rx.observer.ApiObserver;
import com.beiwo.qnejqaz.util.ParamsUtils;
import com.beiwo.qnejqaz.util.RxUtil;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * @name loanmarket
 * @class name：com.beihui.market.ui.presenter
 * @describe 动态发布Presenter
 * @author A
 * @time 2018/9/11 17:19
 */
public class ForumPublishPresenter extends BaseRxPresenter implements ForumPublishContact.Presenter {

    private Api mApi;
    private ForumPublishContact.View mView;
    private Context mContext;
    private UserHelper mUserHelper;

    public ForumPublishPresenter(ForumPublishContact.View view, Context context) {
        mApi = Api.getInstance();
        mView = view;
        mContext = context;
        mUserHelper = UserHelper.getInstance(context);
    }

    @Override
    public void fetchPublishTopic(String imgKey, String forumTitle,
                                  String forumContent, int status, String topicId, String forumId) {
        mApi.publicForumInfo(ParamsUtils.generatePublishParams(mUserHelper.getProfile().getId(),
                getImgKeys(), forumTitle, forumContent, status, topicId, forumId))
                .compose(RxResponse.compatO())
                .subscribe(new ApiObserver<Object>() {
                    @Override
                    public void onNext(@NonNull Object data) {
                        mView.onPublishTopicSucceed();
                    }

                    @Override
                    public void onError(@NonNull Throwable t) {
                        super.onError(t);
                        logError(ForumPublishPresenter.this, t);
                        mView.showErrorMsg(generateErrorMsg(t));
                    }
                });
    }

    @Override
    public void uploadForumImg(Bitmap bitmap) {
        Disposable dis = Observable.just(bitmap)
                .observeOn(Schedulers.io())
                .map(new Function<Bitmap, byte[]>() {
                    @Override
                    public byte[] apply(@NonNull Bitmap source){
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        int quality = 100;
                        source.compress(Bitmap.CompressFormat.JPEG, quality, baos);
                        while (baos.size() > RequestConstants.AVATAR_BYTE_SIZE) {
                            quality -= 5;
                            if (quality <= 0) {
                                quality = 0;
                            }
                            baos.reset();
                            source.compress(Bitmap.CompressFormat.JPEG, quality, baos);
                            if (quality == 0) {
                                break;
                            }
                        }
                        return baos.toByteArray();
                    }
                })
                .observeOn(Schedulers.io())
                .flatMap(new Function<byte[], ObservableSource<ResultEntity<String>>>() {
                    @Override
                    public ObservableSource<ResultEntity<String>> apply(@NonNull byte[] bytes) throws Exception {
                        return mApi.uploadFourmImg(bytes);
                    }
                })
                .compose(RxUtil.<ResultEntity<String>>io2main())
                .subscribe(new Consumer<ResultEntity<String>>() {
                               @Override
                               public void accept(@NonNull ResultEntity<String> result) throws Exception {
                                   if (result.isSuccess()) {
                                       uploadIndex ++;
                                       imgUrls.add(result.getData());
                                       if (uploadIndex < bitmaps.size()) {
                                           uploadForumImg(bitmaps.get(uploadIndex));
                                           return;
                                       }
                                       mView.onUploadImgSucceed(result.getData());
                                   } else {
                                       uploadForumImg(bitmaps.get(uploadIndex));
//                                       mView.showErrorMsg(result.getMsg());
                                   }
                               }
                           },
                        new Consumer<Throwable>() {
                            @Override
                            public void accept(@NonNull Throwable throwable) throws Exception {
                                mView.showErrorMsg(generateErrorMsg(throwable));
                            }
                        });
        addDisposable(dis);
    }

    @Override
    public void fetchEditForum(String forumId) {
        Disposable dis = mApi.fetchEditForum(forumId)
                .compose(RxUtil.<ResultEntity<DraftEditForumBean>>io2main())
                .subscribe(new Consumer<ResultEntity<DraftEditForumBean>>() {
                               @Override
                               public void accept(ResultEntity<DraftEditForumBean> result) throws Exception {
                                   if (result.isSuccess()) {
                                      mView.onEditForumSucceed(result.getData());
                                   } else {
                                       mView.showErrorMsg(result.getMsg());
                                   }
                               }
                           },
                        new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {

                                mView.showErrorMsg(generateErrorMsg(throwable));
                            }
                        });
        addDisposable(dis);
    }

    /**
     * 上传图片转换的bitmap集合
     */
    private List<Bitmap> bitmaps = new ArrayList<>();
    /**
     * 上传图片后返回的图片链接url集合
     */
    private List<String> imgUrls = new ArrayList<>();
    /**
     * 草稿箱存储的url集合
     */
    private List<String> draftUrls = new ArrayList<>();
    /**
     * 当前上传的图片index下标
     */
    private int uploadIndex = 0;

    private StringBuilder sb = new StringBuilder();

    /**
     * 准备上传图片
     * @param list
     */
    public void prepareUpload(List<Bitmap> list){
        this.bitmaps = list;
        uploadForumImg(bitmaps.get(uploadIndex));
    }

    /**
     * 添加草稿箱数据
     * @param list
     */
    public void addDraftUrls(List<String> list){
        draftUrls.clear();
        draftUrls.addAll(list);
    }

    /**
     * 获取Imgkeys
     * @return
     */
    private String getImgKeys(){
        if (imgUrls.size() == bitmaps.size()) {
            draftUrls.addAll(imgUrls);
            int size = draftUrls.size();
            for (int i = 0; i < size; i++) {
                if (i != size - 1) {
                    sb.append(draftUrls.get(i)).append("#");
                } else {
                    sb.append(draftUrls.get(i));
                }
            }
            return sb.toString();
        }
        return "";
    }
}
