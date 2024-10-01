package com.atfotiad.weare8.api

import com.atfotiad.weare8.BuildConfig
import okhttp3.Interceptor
import okhttp3.Response

class JWTInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val newRequest = originalRequest.newBuilder()
            .header("Authorization", "Bearer ${BuildConfig.JWT_TOKEN}")
            .build()
        return chain.proceed(newRequest)
    }

}
