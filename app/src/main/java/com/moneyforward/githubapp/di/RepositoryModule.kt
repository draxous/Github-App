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

/**
 * Hilt module for repositories.
 */
@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    // region UserList
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
    // endregion

    // region RepoList
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
    // endregion
}