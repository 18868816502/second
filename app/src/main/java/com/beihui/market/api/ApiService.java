package com.beihui.market.api;

import com.beihui.market.entity.Phone;
import com.beihui.market.entity.Profession;
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
    Observable<ResultEntity> updatePwd(@Field("id") String id, @Field("account") String account, @Field("pwdType") int pwdType,
                                       @Field("pwd") String pwd, @Field("originPwd") String originPwd,
                                       @Field("pwd2") String pwd2);

    /**
     * 用户个人中心信息
     */
    @FormUrlEncoded
    @POST("/clientUserDetail/personalCenter")
    Observable<ResultEntity<UserProfile>> userProfile(@Field("id") String id);

    /**
     * 修改用户名
     */
    @FormUrlEncoded
    @POST("/clientUserDetail/updateNickName")
    Observable<ResultEntity> updateUsername(@Field("id") String id, @Field("userName") String userName);

    /**
     * 获取职业列表
     */
    @FormUrlEncoded
    @POST("/clientUserDetail/showProfession")
    Observable<ResultEntity<ArrayList<Profession>>> queryProfession(@Field("id") String id);

    /**
     * 修改职业
     */
    @FormUrlEncoded
    @POST("/clientUserDetail/updateProfession")
    Observable<ResultEntity> updateProfession(@Field("id") String id, @Field("professionType") int professionType);

    /**
     * 退出登录
     */
    @FormUrlEncoded
    @POST("/clientUser/logout")
    Observable<ResultEntity> logout(@Field("id") String id);
}
