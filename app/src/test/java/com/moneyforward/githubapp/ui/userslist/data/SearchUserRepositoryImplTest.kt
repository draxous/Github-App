@file:OptIn(ExperimentalCoroutinesApi::class)

package com.moneyforward.githubapp.ui.userslist.data

import com.moneyforward.apis.GithubApiService
import com.moneyforward.apis.common.ApiResult
import com.moneyforward.apis.model.Item
import com.moneyforward.apis.model.SearchUserResponse
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import okhttp3.ResponseBody
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException

/**
 * Unit tests for [SearchUserRepositoryImpl].
 */
class SearchUserRepositoryImplTest {

    private lateinit var repository: SearchUserRepositoryImpl
    private val githubApiService: GithubApiService = mockk()
    private val testDispatcher = StandardTestDispatcher()

    private val mockResponse = SearchUserResponse(
        total_count = 1,
        incomplete_results = false,
        items = listOf(
            Item(
                avatar_url = "https://avatar.url",
                events_url = null,
                followers_url = null,
                following_url = null,
                gists_url = null,
                gravatar_id = "",
                html_url = "https://github.com/mock",
                id = 1,
                login = "mock",
                node_id = "mock_node",
                organizations_url = null,
                received_events_url = null,
                repos_url = null,
                score = 1.0,
                site_admin = false,
                starred_url = null,
                subscriptions_url = null,
                type = "User",
                url = "https://api.github.com/users/mock",
                user_view_type = null
            )
        )
    )

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        repository = SearchUserRepositoryImpl(githubApiService, testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `searchGithubUsers emits success when API returns data`() = runTest {
        coEvery { githubApiService.searchUsers("mock") } returns mockResponse

        val result = repository.searchGithubUsers("mock").toList()

        assertEquals(1, result.size)
        val state = result.first()
        assertTrue(state.result is ApiResult.Success)
        assertEquals(mockResponse, (state.result as ApiResult.Success).data)
    }

    @Test
    fun `searchGithubUsers emits networkError on IOException`() = runTest {
        coEvery { githubApiService.searchUsers("mock") } throws IOException("No internet")

        val result = repository.searchGithubUsers("mock").toList()

        assertEquals(1, result.size)
        assertTrue(result.first().result is ApiResult.NetworkError)
    }

    @Test
    fun `searchGithubUsers emits error on HttpException`() = runTest {
        val responseBody = ResponseBody.create(null, "Forbidden")
        val response = Response.error<Any>(403, responseBody)
        val httpException = HttpException(response)

        coEvery { githubApiService.searchUsers("mock") } throws httpException

        val result = repository.searchGithubUsers("mock").toList()

        assertEquals(1, result.size)
        val state = result.first()
        assertTrue(state.result is ApiResult.Error)
        assertEquals(httpException, (state.result as ApiResult.Error).throwable)
    }

    @Test
    fun `searchGithubUsers emits error on unexpected Exception`() = runTest {
        val unexpected = RuntimeException("Unexpected crash")
        coEvery { githubApiService.searchUsers("mock") } throws unexpected

        val result = repository.searchGithubUsers("mock").toList()

        assertEquals(1, result.size)
        val state = result.first()
        val actualThrowable = (state.result as ApiResult.Error).throwable
        assertTrue(actualThrowable is RuntimeException)
        assertEquals("Unexpected crash", actualThrowable.message)
    }
}
