package com.moneyforward.apis

import com.moneyforward.api.model.RepositoryList
import com.moneyforward.api.model.UserList
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Interface defining the API endpoints for interacting with the Github API.
 */
interface GithubApiService {

    /**
     * Fetches a list of users from the Github API.
     * @param since The ID of the last user seen. This is used for pagination.
     * @return A list of [UserList] objects.
     * Ex: https://api.github.com/search/users?q={userName}
     */
    @GET("search/users?")
    suspend fun users(
        @Query("q") userName: String
    ): List<UserList>

    /**
     * Fetches a list of repositories from the Github API.
     * @param userName The username of the user to fetch repositories for.
     * @return A list of [RepositoryList] objects.
     * Ex: https://api.github.com/users/{userName}/repos
     */
    @GET("users/{userName}/repos")
    suspend fun repositories(
        userName: String
    ): List<RepositoryList>
}