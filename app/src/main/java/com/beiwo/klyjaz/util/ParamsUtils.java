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
     * @param userId
     * @param pageNo
     * @param pageNum
     * @return
     */
    public static Map<String,Object> generateRecommendTopicParams(String userId, int pageNo, int pageNum){
        Map<String,Object> mMap = new HashMap<>();
        if(!TextUtils.isEmpty(userId)) {
            mMap.put("userId", userId);
        }
        mMap.put("pageNo",pageNo);
        mMap.put("pageNum",pageNum);
        return mMap;
    }

    /**
     * 获取评论参数
     * @param userId
     * @param commentType
     * @param commentContent
     * @param forumId
     * @param toUserId
     * @param selfId
     * @return
     */
    public static Map<String,Object> generateCommentParams(String userId, String commentType, String commentContent, String forumId,
                                                           String toUserId, String selfId,String replyId,String replyContent){
        Map<String,Object> mMap = new HashMap<>();
        mMap.put("userId",userId);
        mMap.put("commentType",commentType);
        mMap.put("commentContent",commentContent);
        mMap.put("forumId",forumId);
        if(!TextUtils.isEmpty(toUserId)) {
            mMap.put("toUserId", toUserId);
        }
        if(!TextUtils.isEmpty(selfId)) {
            mMap.put("selfId", selfId);
        }
        if(!TextUtils.isEmpty(replyId)) {
            mMap.put("replyId", replyId);
        }
        if(!TextUtils.isEmpty(replyContent)) {
            mMap.put("replyContent", replyContent);
        }
        return mMap;
    }


    /**
     * 获取保存用户参数
     * @param userId
     * @param sex
     * @param introduce
     * @return
     */
    public static Map<String,Object> generateUserInfoParams(String userId, int sex, String introduce){
        Map<String,Object> mMap = new HashMap<>();
        mMap.put("userId", userId);
        mMap.put("sex", sex);
        if(!TextUtils.isEmpty(introduce)) {
            mMap.put("introduce", introduce);
        }
        return mMap;
    }

    /**
     * 获取草稿箱-待审核列表参数
     * @param userId
     * @param pageNo
     * @param pageSize
     * @return
     */
    public static Map<String,Object> generateDraftsParams(String userId,int pageNo, int pageSize){
        Map<String,Object> mMap = new HashMap<>();
        mMap.put("userId", userId);
        mMap.put("pageNo", pageNo);
        mMap.put("pageSize", pageSize);
        return mMap;
    }

}
