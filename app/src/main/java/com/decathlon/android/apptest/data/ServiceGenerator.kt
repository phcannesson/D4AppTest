package com.decathlon.android.apptest.data

import android.content.Context
import com.decathlon.android.apptest.BuildConfig
import com.decathlon.android.apptest.common.network.ConnectivityInterceptor
import com.decathlon.android.apptest.common.network.NetworkConstants
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

object ServiceGenerator {
    private val httpLoggingInterceptor: HttpLoggingInterceptor
    private val moshi: Moshi

    init {
        moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()

        httpLoggingInterceptor = HttpLoggingInterceptor()
        httpLoggingInterceptor.level =
            if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE
    }

    fun <S> createService(serviceClass: Class<S>, context: Context): S {
        val okHttpClient = OkHttpClient()
            .newBuilder()
            .addInterceptor {
                val originalRequest = it.request()
                val builder = originalRequest
                    .newBuilder()
                    .addHeader(NetworkConstants.ACCEPT_HEADER, NetworkConstants.REQUEST_GITHUB_V3_API)
                val newRequest = builder.build()
                it.proceed(newRequest)
            }
            .addInterceptor(httpLoggingInterceptor)
            .addInterceptor(ConnectivityInterceptor(context))
            .connectTimeout(NetworkConstants.NETWORK_TIMEOUT_IN_SECONDS, TimeUnit.SECONDS)
            .writeTimeout(NetworkConstants.NETWORK_TIMEOUT_IN_SECONDS, TimeUnit.SECONDS)
            .readTimeout(NetworkConstants.NETWORK_TIMEOUT_IN_SECONDS, TimeUnit.SECONDS)
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl(NetworkConstants.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()

        return retrofit.create(serviceClass)
    }
}