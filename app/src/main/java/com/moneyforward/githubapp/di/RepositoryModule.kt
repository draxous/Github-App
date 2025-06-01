package com.moneyforward.githubapp.di

import com.moneyforward.apis.GithubApiService
import com.moneyforward.githubapp.ui.repos.data.RepoListRepository
import com.moneyforward.githubapp.ui.repos.data.RepoListRepositoryImpl
import com.moneyforward.githubapp.ui.userslist.data.SearchUserRepository
import com.moneyforward.githubapp.ui.userslist.data.SearchUserRepositoryImpl
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
    ): SearchUserRepository {
        return SearchUserRepositoryImpl(
            apiService
        )
    }
    /**
     * Provides RepoListRepositoryImpl for RepoListRepository.
     *
     * @return RepoListRepositoryImpl.
     */
    @Singleton
    @Provides
    fun provideRepoListRepository(
        apiService: GithubApiService,
    ): RepoListRepository {
        return RepoListRepositoryImpl(
            apiService
        )
    }
}