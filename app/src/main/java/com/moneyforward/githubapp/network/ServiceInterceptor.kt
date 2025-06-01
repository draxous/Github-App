package com.moneyforward.githubapp.network

import com.moneyforward.githubapp.BuildConfig
import okhttp3.Interceptor
import okhttp3.Response

/**
 * Accept header key.
 */
private const val ACCEPT_HEADER = "Accept"

/**
 * Accept header value. It's recommended by GitHub to send the following header with the requests.
 * https://docs.github.com/en/rest/search/search?apiVersion=2022-11-28#search-users
 */
private const val ACCEPT_HEADER_VALUE = "application/vnd.github+json"

/**
 * Authorization header key.
 */
private const val AUTHORIZATION_HEADER = "Authorization"

/**
 * This class implements [Interceptor] to intercept and modify HTTP requests.
 * OkHttp Interceptor to modify each request and append headers before they are sent.
 *
 * Add Personal Access Token(PAT) to the request header
 * Add Acceptd to the request header
 */

class ServiceInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()
            .addHeader(AUTHORIZATION_HEADER, "token ${BuildConfig.GITHUB_TOKEN}")
            .addHeader(ACCEPT_HEADER, ACCEPT_HEADER_VALUE)
            .build()
        return chain.proceed(request)
    }
}