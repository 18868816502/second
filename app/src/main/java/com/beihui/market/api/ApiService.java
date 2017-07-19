package com.beihui.market.api;

import com.beihui.market.entity.UserProfileAbstract;
import com.beihui.market.entity.request.ReqLogin;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiService {

    @POST("/clientUser/login")
    Observable<ResultEntity<UserProfileAbstract>> login(@Body ReqLogin login);

}
