package com.beiwo.klyjaz.util;

import android.text.TextUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @author chenguoguo
 * @name loanmarket
 * @class name：com.beihui.market.util
 * @descripe
 * @time 2018/10/11 15:09
 */
public class ParamsUtils {
    /**
     * 获取话题推荐列表请求参数
     *
     * @param userId
     * @param pageNo
     * @param pageSize
     * @return
     */
    public static Map<String, Object> generateRecommendTopicParams(String userId, int pageNo, int pageSize) {
        Map<String, Object> mMap = new HashMap<>();
        if (!TextUtils.isEmpty(userId)) {
            mMap.put("userId", userId);
        }
        mMap.put("pageNo", pageNo);
        mMap.put("pageSize", pageSize);
        return mMap;
    }

    /**
     * 获取评论参数
     *
     * @param userId
     * @param commentType
     * @param commentContent
     * @param forumId
     * @param toUserId
     * @param selfId
     * @return
     */
    public static Map<String, Object> generateCommentParams(String userId, String commentType, String commentContent, String forumId,
                                                            String toUserId, String selfId, String replyId, String replyContent) {
        Map<String, Object> mMap = new HashMap<>();
        mMap.put("userId", userId);
        mMap.put("commentType", commentType);
        mMap.put("commentContent", commentContent);
        mMap.put("forumId", forumId);
        if (!TextUtils.isEmpty(toUserId)) {
            mMap.put("toUserId", toUserId);
        }
        if (!TextUtils.isEmpty(selfId)) {
            mMap.put("selfId", selfId);
        }
        if (!TextUtils.isEmpty(replyId)) {
            mMap.put("replyId", replyId);
        }
        if (!TextUtils.isEmpty(replyContent)) {
            mMap.put("replyContent", replyContent);
        }
        return mMap;
    }


    /**
     * 获取保存用户参数
     *
     * @param userId
     * @param sex
     * @param introduce
     * @return
     */
    public static Map<String, Object> generateUserInfoParams(String userId, int sex, String introduce) {
        Map<String, Object> mMap = new HashMap<>();
        mMap.put("userId", userId);
        mMap.put("sex", sex);
        if (!TextUtils.isEmpty(introduce)) {
            mMap.put("introduce", introduce);
        }
        return mMap;
    }

    /**
     * 获取草稿箱-待审核列表参数
     *
     * @param userId
     * @param pageNo
     * @param pageSize
     * @return
     */
    public static Map<String, Object> generateDraftsParams(String userId, int pageNo, int pageSize) {
        Map<String, Object> mMap = new HashMap<>();
        mMap.put("userId", userId);
        mMap.put("pageNo", pageNo);
        mMap.put("pageSize", pageSize);
        return mMap;
    }

    /**
     * 获取埋点请求参数
     *
     * @param userId
     * @param type
     * @param linkId
     * @return
     */
    public static Map<String, Object> generateCountUvParams(String userId, String type, String linkId) {
        Map<String, Object> mMap = new HashMap<>();
        mMap.put("userId", userId);
        mMap.put("type", type);
        if (!TextUtils.isEmpty(linkId)) {
            mMap.put("linkId", linkId);
        }
        return mMap;
    }

    /**
     * 动态详情参数
     *
     * @param userId
     * @param forumId
     * @param pageNo
     * @param pageSize
     * @return
     */
    public static Map<String, Object> generateForumParams(String userId, String forumId, int pageNo, int pageSize) {
        Map<String, Object> mMap = new HashMap<>();
        if (!TextUtils.isEmpty(userId)) {
            mMap.put("userId", userId);
        }
        mMap.put("forumId", forumId);
        mMap.put("pageNo", pageNo);
        mMap.put("pageSize", pageSize);
        return mMap;
    }

    /**
     * 获取发布参数
     *
     * @param userId
     * @param imgKey
     * @param forumTitle
     * @param forumContent
     * @param status
     * @param topicId
     * @param forumId
     * @return
     */
    public static Map<String, Object> generatePublishParams(String userId, String imgKey, String forumTitle,
                                                            String forumContent, int status, String topicId,
                                                            String forumId) {
        Map<String, Object> mMap = new HashMap<>();
        mMap.put("userId", userId);
        if (!TextUtils.isEmpty(imgKey)) {
            mMap.put("imgKey", imgKey);
        }
        mMap.put("forumTitle", forumTitle);
        mMap.put("forumContent", forumContent);
        mMap.put("status", status + "");
        if (!TextUtils.isEmpty(topicId)) {
            mMap.put("topicId", topicId);
        }
        if (!TextUtils.isEmpty(forumId)) {
            mMap.put("forumId", forumId);
        }
        return mMap;
    }

    /**
     * 获取产品评论提交参数
     *
     * @param manageId
     * @param loanStatus
     * @param flag
     * @param type
     * @param imageUrl
     * @param content
     * @param userId
     * @return
     */
    public static Map<String, Object> generateGoodsCommentParams(String manageId, int loanStatus,
                                                                 String flag, int type, String imageUrl, String content, String userId) {
        Map<String, Object> mMap = new HashMap<>();
        mMap.put("manageId", manageId);
        mMap.put("loanStatus", loanStatus);
        mMap.put("flag", flag);
        mMap.put("type", type);
        if (!TextUtils.isEmpty(imageUrl)) {
            mMap.put("imageUrl", imageUrl);
        }
        if (!TextUtils.isEmpty(content)) {
            mMap.put("content", content);
        }
        mMap.put("userId", userId);
        return mMap;
    }

    /**
     * 产品详情-选择产品
     *
     * @param manageName
     * @param pageNo
     * @param pageSize
     * @return
     */
    public static Map<String, Object> generateGoodsParams(String manageName, int pageNo, int pageSize) {
        Map<String, Object> mMap = new HashMap<>();
        if (!TextUtils.isEmpty(manageName)) {
            mMap.put("manageName", manageName);
        }
        mMap.put("pageNo", pageNo);
        mMap.put("pageSize", pageSize);
        return mMap;
    }

    /**
     * 获取悬浮窗参数
     *
     * @param userId
     * @return
     */
    public static Map<String, Object> generateFloatingParams(String userId) {
        Map<String, Object> mMap = new HashMap<>();
        mMap.put("platform", "1");
        if (!TextUtils.isEmpty(userId)) {
            mMap.put("userId", userId);
        }
        return mMap;
    }

    /**
     * 获取加载悬浮窗参数
     *
     * @param advertId
     * @param userId
     * @return
     */
    public static Map<String, Object> generateLoadFloatingParams(String advertId, String userId) {
        Map<String, Object> mMap = new HashMap<>();
        mMap.put("platform", "1");
        mMap.put("advertId", advertId);
        if (!TextUtils.isEmpty(userId)) {
            mMap.put("userId", userId);
        }
        return mMap;
    }
}