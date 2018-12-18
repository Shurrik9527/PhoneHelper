package com.gochicken3.mobilehelper;


import com.gochicken3.mobilehelper.bean.AdInfo;

import io.reactivex.Observable;
import retrofit2.http.POST;
import retrofit2.http.Query;


/**
 * Created by Shurrik on 2017/11/15.
 */

public interface HttpService {
    @POST("/android.php")
    Observable<HttpResult<AdInfo>> getAdType(@Query("action") String action, @Query("type") String type);
}
