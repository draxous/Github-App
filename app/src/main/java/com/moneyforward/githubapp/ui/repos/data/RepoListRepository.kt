package com.moneyforward.githubapp.ui.repos.data

import com.moneyforward.apis.common.ApiState
import com.moneyforward.apis.model.Profile
import com.moneyforward.apis.model.RepositoryList
import kotlinx.coroutines.flow.Flow

/**
 * Interface for fetching repository list data.
 */
interface RepoListRepository {
    /**
     * Fetches the profile of a user.
     * @param login The login name of the user.
     * @return A [Flow] of [ApiState] that emits the [Profile] data.
     */
    fun profile(login: String): Flow<ApiState<Profile>>
    /**
     * Fetches the repositories of a user.
     * @param login The login name of the user.
     * @return A [Flow] of [ApiState] that emits the [RepositoryList] data.
     */
    fun repos(login: String): Flow<ApiState<RepositoryList>>
}
