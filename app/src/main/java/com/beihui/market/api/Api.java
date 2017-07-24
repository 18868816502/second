package com.beihui.market.api;


import com.beihui.market.entity.Phone;
import com.beihui.market.entity.UserProfile;
import com.beihui.market.entity.UserProfileAbstract;
import com.beihui.market.entity.request.RequestConstants;

import io.reactivex.Observable;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class Api {

    private static Api sInstance;
    private ApiService service;

    public static Api getInstance(OkHttpClient okHttpClient) {
        if (sInstance == null) {
            synchronized (Api.class) {
                if (sInstance == null) {
                    sInstance = new Api(okHttpClient);
                }
            }
        }
        return sInstance;
    }

    private Api(OkHttpClient okHttpClient) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(NetConstants.DOMAIN)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(okHttpClient)
                .build();
        service = retrofit.create(ApiService.class);
    }

    /**
     * 登录
     *
     * @param account 用户账号
     * @param pwd     用户密码
     */
    public Observable<ResultEntity<UserProfileAbstract>> login(String account, String pwd) {
        return service.login(account, pwd);
    }

    /**
     * 请求注册验证码
     *
     * @param phone 请求手机号
     */
    public Observable<ResultEntity<Phone>> requestRegisterSms(String phone) {
        return service.requestSms(phone, RequestConstants.VERIFICATION_TYPE_REGISTER);
    }

    /**
     * 请求重置密码验证码
     *
     * @param phone 请求手机号
     */
    public Observable<ResultEntity<Phone>> requestRestPwdSms(String phone) {
        return service.requestSms(phone, RequestConstants.VERIFICATION_TYPE_RESET_PWD);
    }

    /**
     * 验证注册验证码
     *
     * @param phone 请求的手机号
     * @param code  请求的验证码
     */
    public Observable<ResultEntity> verifyRegisterCode(String phone, String code) {
        return service.verifyCode(phone, code, RequestConstants.VERIFICATION_TYPE_REGISTER);
    }

    /**
     * 验证重置密码验证码
     *
     * @param phone 请求的手机号
     * @param code  请求的验证码
     */
    public Observable<ResultEntity> verifyResetPwdCode(String phone, String code) {
        return service.verifyCode(phone, code, RequestConstants.VERIFICATION_TYPE_RESET_PWD);
    }

    /**
     * 注册
     *
     * @param phone      注册手机号
     * @param pwd        用户密码
     * @param channelId  app渠道
     * @param inviteCode 邀请码，可控
     */
    public Observable<ResultEntity> register(String phone, String pwd, String channelId, String inviteCode) {
        return service.register(RequestConstants.PLATFORM, phone, pwd, channelId, inviteCode);
    }

    /**
     * 重置密码
     *
     * @param phone 用户手机号
     * @param pwd   新密码
     */
    public Observable<ResultEntity> resetPwd(String phone, String pwd) {
        return service.updatePwd(phone, RequestConstants.UPDATE_PWD_TYPE_RESET, pwd, null, null);
    }

    /**
     * 修改密码
     *
     * @param phone     用户手机号
     * @param pwd       新密码
     * @param originPwd 原密码
     */
    public Observable<ResultEntity> updatePwd(String phone, String pwd, String originPwd) {
        return service.updatePwd(phone, RequestConstants.UPDATE_PWD_TYPE_CHANGE, pwd, originPwd, originPwd);
    }

    /**
     * 查询用户个人信息
     *
     * @param id 用户id
     */
    public Observable<ResultEntity<UserProfile>> queryUserProfile(String id) {
        return service.userProfile(id);
    }


    /**
     * 修改用户名
     *
     * @param id       用户id
     * @param username 新用户名
     */
    public Observable<ResultEntity> updateUsername(String id, String username) {
        return service.updateUsername(id, username);
    }

    /**
     * 更新用户职业
     *
     * @param id             用户id
     * @param professionType 职业类型
     */
    public Observable<ResultEntity> updateUserProfession(String id, int professionType) {
        return service.updateProfession(id, professionType);
    }
}
