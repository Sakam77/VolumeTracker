package com.volumetracker.app.data.api.interceptor

import okhttp3.Interceptor
import okhttp3.Response
import timber.log.Timber

class GmgnInterceptor : Interceptor {
    
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val newRequest = request.newBuilder()
            .addHeader("Accept", "application/json")
            .addHeader("User-Agent", "VolumeTracker/1.0")
            .build()
        
        Timber.d("API Request: ${newRequest.method} ${newRequest.url}")
        
        val response = chain.proceed(newRequest)
        
        Timber.d("API Response: ${response.code} ${response.message}")
        
        return response
    }
}
