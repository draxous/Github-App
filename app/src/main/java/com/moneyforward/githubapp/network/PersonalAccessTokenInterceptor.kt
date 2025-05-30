package com.moneyforward.githubapp.network

import com.moneyforward.githubapp.BuildConfig
import okhttp3.Interceptor
import okhttp3.Response

/**
 * Interceptor to add Personal Access Token to the request header.
 */

class PersonalAccessTokenInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()
            .addHeader("Authorization", "token ${BuildConfig.GITHUB_TOKEN}")
            .build()
        return chain.proceed(request)
    }
}