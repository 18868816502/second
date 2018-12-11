package com.beiwo.klyjaz.goods.contact;


import android.graphics.Bitmap;

import com.beiwo.klyjaz.base.BasePresenter;
import com.beiwo.klyjaz.base.BaseView;
import com.beiwo.klyjaz.social.bean.DraftEditForumBean;

public interface GoodsPublishCommentContact {

    interface Presenter extends BasePresenter {

        /**
         * 发布评论
         */
        void fetchPublishComment(String manageId, int loanStatus,
                                 String flag, int type, String imageUrl, String content,String userId);

        /**
         * 上传图片
         * @param bitmap
         */
        void uploadImg(Bitmap bitmap);


    }

    interface View extends BaseView<Presenter> {

        /**
         * 发布评论成功
         */
        void onPublishCommentSucceed();

        /**
         * 上传图片成功
         * @param imgKey 发布动态接口的imgkey
         */
        void onUploadImgSucceed(String imgKey);


    }

}
