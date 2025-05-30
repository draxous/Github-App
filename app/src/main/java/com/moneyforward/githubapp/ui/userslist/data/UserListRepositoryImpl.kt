package com.moneyforward.githubapp.ui.userslist.data

import com.moneyforward.apis.GithubApiService
import com.moneyforward.apis.common.ApiState
import com.moneyforward.apis.model.SearchUserResponse
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import java.io.IOException
import javax.inject.Inject

/**
 * Implementation of [UserListRepository] that fetches user list data from [GithubApiService].
 * @param githubApiService The service to fetch user list data from.
 * @param ioDispatcher The coroutine dispatcher for I/O operations.
 */
class UserListRepositoryImpl @Inject constructor(
    private val githubApiService: GithubApiService,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : UserListRepository {

    /**
     * Fetches a list of GitHub users.
     * @param keyword The username to search for (currently hardcoded to "draxous").
     * @return A [Flow] of [ApiState] that emits the list of [SearchUserResponse] objects on success,
     * or an error state on failure.
     */
    override suspend fun searchGithubUsers(keyword: String): Flow<ApiState<SearchUserResponse>> = flow {
        try {
            val users = withContext(ioDispatcher) {
                githubApiService.searchUsers(keyword)
            }
            emit(ApiState.success(users))
        }  catch (e: IOException) {
            emit(ApiState.networkError(e))
        } catch (e: Exception) {
            emit(ApiState.error(e))
        }
    }.flowOn(Dispatchers.IO)
}