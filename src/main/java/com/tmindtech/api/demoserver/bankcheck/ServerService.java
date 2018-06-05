package com.tmindtech.api.demoserver.bankcheck;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;

public interface ServerService {

    @GET("label-print/health")
    Call<ResponseBody> getServerStatus();

}
