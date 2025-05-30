package com.moneyforward.githubapp.ui.userslist.data

import com.moneyforward.apis.common.ApiState
import com.moneyforward.apis.model.SearchUserResponse
import kotlinx.coroutines.flow.Flow

/**
 * Interface for accessing user list data.
 */
interface SearchUserRepository {
    /**
     * Fetches a list of GitHub users based on the provided username.
     * @param keyword The username to search for.
     * @return A [Flow] emitting [ApiState] which wraps a list of [SearchUserResponse].
     */
    suspend fun searchGithubUsers(keyword: String): Flow<ApiState<SearchUserResponse>>
}