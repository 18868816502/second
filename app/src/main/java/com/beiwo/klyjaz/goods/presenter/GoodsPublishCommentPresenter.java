package com.beiwo.klyjaz.goods.presenter;

import android.content.Context;
import android.graphics.Bitmap;

import com.beiwo.klyjaz.api.Api;
import com.beiwo.klyjaz.api.ResultEntity;
import com.beiwo.klyjaz.base.BaseRxPresenter;
import com.beiwo.klyjaz.entity.UploadImg;
import com.beiwo.klyjaz.entity.request.RequestConstants;
import com.beiwo.klyjaz.goods.contact.GoodsPublishCommentContact;
import com.beiwo.klyjaz.helper.UserHelper;
import com.beiwo.klyjaz.social.bean.DraftEditForumBean;
import com.beiwo.klyjaz.social.contract.ForumPublishContact;
import com.beiwo.klyjaz.tang.rx.RxResponse;
import com.beiwo.klyjaz.tang.rx.observer.ApiObserver;
import com.beiwo.klyjaz.util.ParamsUtils;
import com.beiwo.klyjaz.util.RxUtil;

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
public class GoodsPublishCommentPresenter extends BaseRxPresenter implements GoodsPublishCommentContact.Presenter {

    private Api mApi;
    private GoodsPublishCommentContact.View mView;
    private Context mContext;
    private UserHelper mUserHelper;

    public GoodsPublishCommentPresenter(GoodsPublishCommentContact.View view, Context context) {
        mApi = Api.getInstance();
        mView = view;
        mContext = context;
        mUserHelper = UserHelper.getInstance(context);
    }

    @Override
    public void fetchPublishComment(String manageId, int loanStatus,
                                  String flag, int type, String imageUrl, String content,String userId) {
        mApi.commentGoods(ParamsUtils.generateGoodsCommentParams(manageId,loanStatus,
                flag, type, getImgKeys(), content, mUserHelper.getProfile().getId()))
                .compose(RxResponse.compatO())
                .subscribe(new ApiObserver<Object>() {
                    @Override
                    public void onNext(@NonNull Object data) {
                        mView.onPublishCommentSucceed();
                    }

                    @Override
                    public void onError(@NonNull Throwable t) {
                        super.onError(t);
                        logError(GoodsPublishCommentPresenter.this, t);
                        mView.showErrorMsg(generateErrorMsg(t));
                    }
                });
    }

    @Override
    public void uploadImg(Bitmap bitmap) {
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
                .flatMap(new Function<byte[], ObservableSource<ResultEntity<UploadImg>>>() {
                    @Override
                    public ObservableSource<ResultEntity<UploadImg>> apply(@NonNull byte[] bytes) throws Exception {
                        return mApi.uploadImg(bytes);
                    }
                })
                .compose(RxUtil.<ResultEntity<UploadImg>>io2main())
                .subscribe(new Consumer<ResultEntity<UploadImg>>() {
                               @Override
                               public void accept(@NonNull ResultEntity<UploadImg> result) throws Exception {
                                   if (result.isSuccess()) {
                                       uploadIndex ++;
                                       imgUrls.add(result.getData().getUrl());
                                       if (uploadIndex < bitmaps.size()) {
                                           uploadImg(bitmaps.get(uploadIndex));
                                           return;
                                       }
                                       mView.onUploadImgSucceed(result.getData().getUrl());
                                   } else {
                                       uploadImg(bitmaps.get(uploadIndex));
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


    /**
     * 上传图片转换的bitmap集合
     */
    private List<Bitmap> bitmaps = new ArrayList<>();
    /**
     * 上传图片后返回的图片链接url集合
     */
    private List<String> imgUrls = new ArrayList<>();
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
        uploadImg(bitmaps.get(uploadIndex));
    }



    /**
     * 获取Imgkeys
     * @return
     */
    private String getImgKeys(){
        if (imgUrls.size() == bitmaps.size()) {
            for (int i = 0; i < imgUrls.size(); i++) {
                if (i != imgUrls.size() - 1) {
                    sb.append(imgUrls.get(i)).append("#");
                } else {
                    sb.append(imgUrls.get(i));
                }
            }
            return sb.toString();
        }
        return "";
    }
}
