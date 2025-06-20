package com.moneyforward.githubapp.di

import com.moneyforward.apis.GithubApiService
import com.moneyforward.githubapp.BuildConfig
import com.moneyforward.githubapp.constants.ApiUrls
import com.moneyforward.githubapp.network.ServiceInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    /**
     * Provides BaseUrl as string.
     */
    @Singleton
    @Provides
    fun provideBaseURL(): String {
        return ApiUrls.BASE_URL
    }

    /**
     * Provides LoggingInterceptor for api information.
     */
    @Singleton
    @Provides
    fun provideLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
    }

    /**
     * Provides PAT Interceptor.
     * @return ServiceInterceptor.
     */
    @Singleton
    @Provides
    fun provideAccessTokenInterceptor(): ServiceInterceptor {
        return ServiceInterceptor()
    }

    /**
     * Provides custom OkkHttp.
     * @param loggingInterceptor for api information.
     * @param serviceInterceptor for access token.
     * @return OkHttpClient.
     */
    @Singleton
    @Provides
    fun provideOkHttpClient(
        loggingInterceptor: HttpLoggingInterceptor,
        serviceInterceptor: ServiceInterceptor
    ): OkHttpClient {
        val okHttpClient = OkHttpClient().newBuilder()

        okHttpClient.apply{
            callTimeout(40, TimeUnit.SECONDS)
            connectTimeout(40, TimeUnit.SECONDS)
            readTimeout(40, TimeUnit.SECONDS)
            writeTimeout(40, TimeUnit.SECONDS)
            if (BuildConfig.DEBUG) {
                addInterceptor(loggingInterceptor)
            }
            addInterceptor(serviceInterceptor)
        }
        return okHttpClient.build()
    }

    /**
     * Provides converter factory for retrofit
     */
    @Singleton
    @Provides
    fun provideConverterFactory(): Converter.Factory {
        return GsonConverterFactory.create()
    }

    /**
     * Provides GithubApiServices client for Retrofit.
     */
    @Singleton
    @Provides
    fun provideRetrofitClient(
        baseUrl: String,
        okHttpClient: OkHttpClient,
        converterFactory: Converter.Factory,
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(okHttpClient)
            .addConverterFactory(converterFactory)
            .build()
    }

    /**
     * Provides Api Service using retrofit.
     */
    @Singleton
    @Provides
    fun provideGithubApiService(retrofit: Retrofit): GithubApiService {
        return retrofit.create(GithubApiService::class.java)
    }

}