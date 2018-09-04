package com.jerrywang.phonehelper;


import io.reactivex.Observable;
import retrofit2.http.POST;
import retrofit2.http.Query;


/**
 * Created by Shurrik on 2017/11/15.
 */

public interface HttpService {
    @POST("api/shop/goods/search.do")
    Observable<HttpResult<Object>> search(@Query("message") String keyword);
}
