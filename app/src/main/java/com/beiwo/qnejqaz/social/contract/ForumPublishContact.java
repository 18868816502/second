package com.beiwo.qnejqaz.social.contract;


import android.graphics.Bitmap;

import com.beiwo.qnejqaz.base.BasePresenter;
import com.beiwo.qnejqaz.base.BaseView;
import com.beiwo.qnejqaz.social.bean.DraftEditForumBean;

public interface ForumPublishContact {

    interface Presenter extends BasePresenter {

        /**
         * 发布话题
         */
        void fetchPublishTopic(String imgKey, String forumTitle,
                               String forumContent, int status, String topicId, String forumId);

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
