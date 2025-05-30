package com.moneyforward.githubapp.di

import com.moneyforward.apis.GithubApiService
import com.moneyforward.githubapp.ui.userslist.data.UserListRepository
import com.moneyforward.githubapp.ui.userslist.data.UserListRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
/**
 * Hilt module for repositories.
 */
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    /**
     * Provides UserListRepositoryImpl for UserListRepository.
     *
     * @return UserListRepositoryImpl.
     */
    @Singleton
    @Provides
    fun provideUserListRepository(
        apiService: GithubApiService,
    ): UserListRepository {
        return UserListRepositoryImpl(
            apiService
        )
    }
}