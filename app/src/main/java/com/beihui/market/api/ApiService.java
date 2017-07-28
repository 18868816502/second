package com.beihui.market.api;

import com.beihui.market.entity.Announce;
import com.beihui.market.entity.AnnounceAbstract;
import com.beihui.market.entity.AnnounceDetail;
import com.beihui.market.entity.News;
import com.beihui.market.entity.Phone;
import com.beihui.market.entity.Profession;
import com.beihui.market.entity.ReNews;
import com.beihui.market.entity.SysMsg;
import com.beihui.market.entity.SysMsgAbstract;
import com.beihui.market.entity.SysMsgDetail;
import com.beihui.market.entity.UserProfile;
import com.beihui.market.entity.UserProfileAbstract;

import java.util.ArrayList;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface ApiService {

    /**
     * 登录
     */
    @FormUrlEncoded
    @POST("/clientUser/login")
    Observable<ResultEntity<UserProfileAbstract>> login(@Field("account") String account, @Field("pwd") String pwd);

    /**
     * 请求验证码
     */
    @FormUrlEncoded
    @POST("/sms/sendSms")
    Observable<ResultEntity<Phone>> requestSms(@Field("phone") String phone, @Field("type") String type);

    /**
     * 验证验证码
     */
    @FormUrlEncoded
    @POST("/clientUser/verificationCodeVerify")
    Observable<ResultEntity> verifyCode(@Field("account") String account, @Field("verificationCode") String verificationCode,
                                        @Field("verificationCodeType") String verificationCodeType);

    /**
     * 注册
     */
    @FormUrlEncoded
    @POST("/clientUser/register")
    Observable<ResultEntity> register(@Field("platform") int platform, @Field("account") String account,
                                      @Field("pwd") String pwd, @Field("channelId") String channelId,
                                      @Field("inviteCode") String inviteCode);

    /**
     * 更新密码，重置或者修改
     */
    @FormUrlEncoded
    @POST("/clientUser/updatePwd")
    Observable<ResultEntity> updatePwd(@Field("userId") String id, @Field("account") String account, @Field("pwdType") int pwdType,
                                       @Field("pwd") String pwd, @Field("originPwd") String originPwd,
                                       @Field("pwd2") String pwd2);

    /**
     * 用户个人中心信息
     */
    @FormUrlEncoded
    @POST("/clientUserDetail/personalCenter")
    Observable<ResultEntity<UserProfile>> userProfile(@Field("userId") String id);

    /**
     * 修改用户名
     */
    @FormUrlEncoded
    @POST("/clientUserDetail/updateNickName")
    Observable<ResultEntity> updateUsername(@Field("userId") String id, @Field("userName") String userName);

    /**
     * 获取职业列表
     */
    @FormUrlEncoded
    @POST("/clientUserDetail/showProfession")
    Observable<ResultEntity<ArrayList<Profession>>> queryProfession(@Field("userId") String id);

    /**
     * 修改职业
     */
    @FormUrlEncoded
    @POST("/clientUserDetail/updateProfession")
    Observable<ResultEntity> updateProfession(@Field("userId") String id, @Field("professionType") int professionType);

    /**
     * 退出登录
     */
    @FormUrlEncoded
    @POST("/clientUser/logout")
    Observable<ResultEntity> logout(@Field("userId") String id);

    /**
     * 消息中心-公告
     */
    @POST("/notice/home")
    Observable<ResultEntity<AnnounceAbstract>> announceHome();

    /**
     * 公告列表
     */
    @FormUrlEncoded
    @POST("/notice/list")
    Observable<ResultEntity<Announce>> announceList(@Field("pageNo") int pageNum, @Field("pageSize") int pageSize);

    /**
     * 公告详情
     */
    @FormUrlEncoded
    @POST("notice/details")
    Observable<ResultEntity<AnnounceDetail>> announceDetail(@Field("id") String id);

    /**
     * 消息中心-系统消息
     */
    @FormUrlEncoded
    @POST("/systemMessage/home")
    Observable<ResultEntity<SysMsgAbstract>> sysMsgHome(@Field("userId") String userId);

    /**
     * 系统消息列表
     */
    @FormUrlEncoded
    @POST("/systemMessage/list")
    Observable<ResultEntity<SysMsg>> sysMsgList(@Field("userId") String userId, @Field("pageNo") int pageNum,
                                                @Field("pageSize") int pageSize);

    /**
     * 系统消息详情
     */
    @FormUrlEncoded
    @POST("/systemMessage/details")
    Observable<ResultEntity<SysMsgDetail>> sysMsgDetail(@Field("id") String id);

    /**
     * 资讯列表
     */
    @FormUrlEncoded
    @POST("/information/list")
    Observable<ResultEntity<News>> queryNews(@Field("pageNo") int pageNum, @Field("pageSize") int pageSize);

    /**
     * 推荐资讯
     */
    @FormUrlEncoded
    @POST("/pushInfo/list")
    Observable<ResultEntity<ReNews>> queryReNews(@Field("pageNo") int pageNum, @Field("pageSize") int pageSize);
}
