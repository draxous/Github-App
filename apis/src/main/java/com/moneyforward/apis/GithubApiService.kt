package com.moneyforward.apis

import com.moneyforward.apis.model.RepositoryList
import com.moneyforward.apis.model.SearchUserResponse
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Interface defining the API endpoints for interacting with the Github API.
 */
interface GithubApiService {

    /**
     * Fetches a list of users from the Github API.
     * @return A list of [SearchUserResponse] objects.
     * Ex: https://api.github.com/search/users?q={userName}
     */
    @GET("search/users?")
    suspend fun searchUsers(
        @Query("q") userName: String
    ): SearchUserResponse

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