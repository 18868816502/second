package com.beihui.market.api;

import com.beihui.market.entity.Phone;
import com.beihui.market.entity.Profession;
import com.beihui.market.entity.UserProfile;
import com.beihui.market.entity.UserProfileAbstract;

import java.util.ArrayList;

import io.reactivex.Observable;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiService {

    /**
     * 登录
     */
    @POST("/clientUser/login")
    Observable<ResultEntity<UserProfileAbstract>> login(@Query("account") String account, @Query("pwd") String pwd);

    /**
     * 请求验证码
     */
    @POST("/sms/sendSms")
    Observable<ResultEntity<Phone>> requestSms(@Query("phone") String phone, @Query("type") String type);

    /**
     * 验证验证码
     */
    @POST("/clientUser/verificationCodeVerify")
    Observable<ResultEntity> verifyCode(@Query("account") String account, @Query("verificationCode") String verificationCode,
                                        @Query("verificationCodeType") String verificationCodeType);

    /**
     * 注册
     */
    @POST("/clientUser/register")
    Observable<ResultEntity> register(@Query("platform") int platform, @Query("account") String account,
                                      @Query("pwd") String pwd, @Query("channelId") String channelId,
                                      @Query("inviteCode") String inviteCode);

    /**
     * 更新密码，重置或者修改
     */
    @POST("/clientUser/updatePwd")
    Observable<ResultEntity> updatePwd(@Query("id") String id, @Query("pwdType") int pwdType,
                                       @Query("pwd") String pwd, @Query("originPwd") String originPwd,
                                       @Query("pwd2") String pwd2);

    /**
     * 用户个人中心信息
     */
    @POST("/clientUserDetail/personalCenter")
    Observable<ResultEntity<UserProfile>> userProfile(@Query("id") String id);

    /**
     * 修改用户名
     */
    @POST("/clientUserDetail/updateNickName")
    Observable<ResultEntity> updateUsername(@Query("id") String id, @Query("userName") String userName);

    /**
     * 获取职业列表
     */
    @POST("/clientUserDetail/showProfession")
    Observable<ResultEntity<ArrayList<Profession>>> queryProfession(@Query("id") String id);

    /**
     * 修改职业
     */
    @POST("/clientUserDetail/updateProfession")
    Observable<ResultEntity> updateProfession(@Query("id") String id, @Query("professionType") int professionType);

    /**
     * 退出登录
     */
    @POST("/clientUser/logout")
    Observable<ResultEntity> logout(@Query("id") String id);
}
