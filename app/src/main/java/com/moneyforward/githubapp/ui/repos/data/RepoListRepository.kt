package com.moneyforward.githubapp.ui.repos.data

import com.moneyforward.apis.common.ApiState
import com.moneyforward.apis.model.Profile
import com.moneyforward.apis.model.RepositoryList
import kotlinx.coroutines.flow.Flow

interface RepoListRepository {
    fun profile(login: String): Flow<ApiState<Profile>>
    fun repos(login: String): Flow<ApiState<RepositoryList>>
}

