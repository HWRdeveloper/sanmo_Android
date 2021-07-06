package com.ruehyeon.sanmo;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface Myservice {

    @POST("api/postAbnormal")
    Call<JsonObject> postAbnormal (@Body JsonObject jsonObject);


    @POST("api/phoneverification")
    Call<JsonObject> verification(@Body JsonObject jsonObject);

}
