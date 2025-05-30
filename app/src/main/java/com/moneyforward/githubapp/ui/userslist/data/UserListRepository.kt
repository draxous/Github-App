package com.moneyforward.githubapp.ui.userslist.data

import com.moneyforward.api.common.ApiState
import com.moneyforward.api.model.UserList
import kotlinx.coroutines.flow.Flow

/**
 * Interface for accessing user list data.
 */
interface UserListRepository {
    /**
     * Fetches a list of GitHub users based on the provided username.
     * @param userName The username to search for.
     * @return A [Flow] emitting [ApiState] which wraps a list of [UserList].
     */
    suspend fun githubUsers(userName: String): Flow<ApiState<List<UserList>>>
}