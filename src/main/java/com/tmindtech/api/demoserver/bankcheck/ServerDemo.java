package com.tmindtech.api.demoserver.bankcheck;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import org.apache.commons.io.IOUtils;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ServerDemo {

    private String serverAddress;

    public ServerDemo(String serverAddress) {
        this.serverAddress = serverAddress;
    }

    public void getServerStatus() {
        try {
            Response<ResponseBody> response = buildServerService(serverAddress).getServerStatus().execute();
            if (response.isSuccessful()) {
                String content = IOUtils.toString(response.body().byteStream(), StandardCharsets.UTF_8);
                System.out.println(content);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public ServerService buildServerService(String serverAddress) {
        OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                .connectTimeout(3, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .addNetworkInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                .build();

        return new Retrofit.Builder()
                .baseUrl(serverAddress)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build()
                .create(ServerService.class);

    }
}
