package com.crysp.network;

import com.crysp.deviceverify.model.XConfirmModel;

import java.util.Map;

import io.reactivex.Observable;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface CryspService {

    @POST("cryspverify/xconfirm/")
    @Headers({"Content-Type: application/json"})
    Observable<XConfirmModel> xconfirm(@Body Map<String, String> body);

    @POST("cryspverify/getxcvr/")
    @Headers({"Content-Type: application/json"})
    Observable<XConfirmModel> getXcvr(@Body Map<String, String> body);
}
