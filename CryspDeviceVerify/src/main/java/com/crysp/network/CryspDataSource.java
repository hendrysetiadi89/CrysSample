package com.crysp.network;

import com.crysp.deviceverify.BuildConfig;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class CryspDataSource {
    private static CryspService service;

    public static CryspService getService() {
        if (service == null) {
            final Retrofit.Builder builder = new Retrofit.Builder()
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create());

            OkHttpClient.Builder okHttpClientBuilder = new OkHttpClient.Builder();
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            OkHttpClient okHttpClient = okHttpClientBuilder
                    .readTimeout(25, TimeUnit.SECONDS)
                    .addInterceptor(new Interceptor() {
                        @Override
                        public Response intercept(Chain chain) throws IOException {
                            return chain.proceed(chain.request()
                                    .newBuilder()
//                                    .header("Authorization", "2484F4ED016C41FEAAC1BB894490B034")
                                    .header("Authorization", "06D0B2E1AE5C433997947BA348FA4742")
                                    .build());
                        }
                    })
                    .addInterceptor(loggingInterceptor)
                    .build();
            Retrofit retrofit = builder.baseUrl(CrysptURL.BASE_URL).client(okHttpClient).build();
            service = retrofit.create(CryspService.class);
        }
        return service;
    }

}
