package com.moneyforward.githubapp.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

/**
 * Provides CoroutineDispatchers for Hilt dependency injection.
 */
@Module
@InstallIn(SingletonComponent::class)
object DispatcherModule {

    /**
     * Provides the IO CoroutineDispatcher.
     */
    @Provides
    fun provideIoDispatcher(): CoroutineDispatcher = Dispatchers.IO
}