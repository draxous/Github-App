package com.moneyforward.apis

import com.moneyforward.apis.model.Profile
import com.moneyforward.apis.model.RepositoryList
import com.moneyforward.apis.model.SearchUserResponse
import retrofit2.http.GET
import retrofit2.http.Path
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
     * Fetches the profile of a user from the Github API.
     * @param login The username of the user to fetch the profile for.
     * @return A [Profile] object.
     * Ex: https://api.github.com/users/{login}
     */
    @GET("users/{login}")
    suspend fun profile(
        @Path("login") login: String
    ): Profile

    /**
     * Fetches a list of repositories from the Github API.
     * @param login The username of the user to fetch repositories for.
     * @return A list of [RepositoryList] objects.
     * Ex: https://api.github.com/users/{userName}/repos
     */
    @GET("users/{login}/repos")
    suspend fun repos(
        @Path("login") login: String
    ): RepositoryList
}