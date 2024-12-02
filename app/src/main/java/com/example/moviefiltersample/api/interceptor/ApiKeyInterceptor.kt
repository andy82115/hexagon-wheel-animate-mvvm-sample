package com.example.moviefiltersample.api.interceptor

import okhttp3.Interceptor
import okhttp3.Response

class ApiKeyInterceptor(private val apiKey: String, private val authToken: String) : Interceptor {
    override fun intercept(chain:Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val modifiedUrl = originalRequest.url.newBuilder()
            .addQueryParameter("api_key", apiKey)
            .build()
        // Modify the request to add Bearer token to the Authorization header
        val modifiedRequest = originalRequest.newBuilder()
            .url(modifiedUrl)
            .header("Authorization", "Bearer $authToken")  // Add Bearer token here
            .build()
        return chain.proceed(modifiedRequest)
    }
}