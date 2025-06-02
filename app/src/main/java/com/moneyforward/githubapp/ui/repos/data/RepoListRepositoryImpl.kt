package com.moneyforward.githubapp.ui.repos.data

import com.moneyforward.apis.GithubApiService
import com.moneyforward.apis.common.ApiState
import com.moneyforward.apis.model.Profile
import com.moneyforward.apis.model.RepositoryList
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import timber.log.Timber
import java.io.IOException
import javax.inject.Inject

/**
 * Implementation of [RepoListRepository] that fetches repository data from the GitHub API.
 *
 * @property githubApiService The service used to interact with the GitHub API.
 * @property ioDispatcher The [CoroutineDispatcher] used for network operations, defaulting to [Dispatchers.IO].
 */
class RepoListRepositoryImpl @Inject constructor(
    private val githubApiService: GithubApiService,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : RepoListRepository {

    /**
     * Fetches a list of repositories for a given user login.
     *
     * @param login The GitHub login of the user.
     * @return A Flow emitting [ApiState] which encapsulates the result of the API call.
     *         It can be [ApiState.Success] with the list of repositories,
     *         [ApiState.NetworkError] for network issues, or [ApiState.Error] for other errors.
     */
    override fun profile(login: String): Flow<ApiState<Profile>> = flow {
        try {
            val users = withContext(ioDispatcher) {
                githubApiService.profile(login = login)
            }
            emit(ApiState.success(users))
        } catch (e: IOException) {
            emit(ApiState.networkError(e))
        } catch (e: HttpException) {
            Timber.e(e, "HttpException occurred during API call")
            //TODO: Suggest actions to user based on the error code.
            //https://docs.github.com/en/rest/search/search?apiVersion=2022-11-28#search-code--status-codes
            emit(ApiState.error(e))
        } catch (e: Exception) {
            Timber.e(e, "An unexpected error occurred")
            emit(ApiState.error(e))
        }
    }

    /**
     * Fetches a list of repositories for a given user login.
     *
     * @param login The GitHub login of the user.
     * @return A Flow emitting [ApiState] which encapsulates the result of the API call.
     *         It can be [ApiState.Success] with the list of repositories,
     *         [ApiState.NetworkError] for network issues, or [ApiState.Error] for other errors.
     */
    override fun repos(login: String): Flow<ApiState<RepositoryList>> = flow {
        try {
            val users = withContext(ioDispatcher) {
                githubApiService.repos(login = login)
            }
            emit(ApiState.success(users))
        } catch (e: IOException) {
            emit(ApiState.networkError(e))
        } catch (e: HttpException) {
            Timber.e(e, "HttpException occurred during API call")
            //TODO: Suggest actions to user based on the error code.
            //https://docs.github.com/en/rest/search/search?apiVersion=2022-11-28#search-code--status-codes
            emit(ApiState.error(e))
        } catch (e: Exception) {
            Timber.e(e, "An unexpected error occurred")
            emit(ApiState.error(e))
        }
    }
}