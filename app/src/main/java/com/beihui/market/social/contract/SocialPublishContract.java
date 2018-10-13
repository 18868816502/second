package com.beihui.market.social.contract;


import android.graphics.Bitmap;

import com.beihui.market.base.BasePresenter;
import com.beihui.market.base.BaseView;
import com.beihui.market.social.bean.DraftEditForumBean;

public interface SocialPublishContract {

    interface Presenter extends BasePresenter {
        /**
         * 保存草稿
         * @param userId
         * @param title
         * @param content
         */
        void saveDraft(String userId,String title,String content);

        /**
         * 发布话题
         */
        void fetchPublishTopic(String imgKey, String forumTitle,
                               String forumContent, String status,String topicId,String forumId);

        /**
         * 上传图片
         * @param bitmap
         */
        void uploadForumImg(Bitmap bitmap);

        /**
         * 获取草稿编辑信息
         * @param forumId
         */
        void fetchEditForum(String forumId);
    }

    interface View extends BaseView<Presenter> {

        /**
         * 保存草稿成功
         */
        void onSaveDraftSucceed();

        /**
         * 发布话题成功
         */
        void onPublishTopicSucceed();

        /**
         * 上传图片成功
         * @param imgKey 发布动态接口的imgkey
         */
        void onUploadImgSucceed(String imgKey);

        /**
         * 上传图片失败
         */
        void onUploadImgFailed();

        /**
         * 获取草稿箱信息成功
         */
        void onEditForumSucceed(DraftEditForumBean forumBean);
    }

}
