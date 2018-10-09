package com.beihui.market.social.contract;


import com.beihui.market.base.BasePresenter;
import com.beihui.market.base.BaseView;

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
                               String forumContent, String status,String topicId);

        /**
         * 上传图片
         * @param base64
         */
        void uploadForumImg(int index,String base64);
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
    }

}
