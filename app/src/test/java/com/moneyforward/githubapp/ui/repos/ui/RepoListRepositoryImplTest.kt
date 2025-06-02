package com.moneyforward.githubapp.ui.repos.ui

import com.moneyforward.apis.common.ApiResult
import com.moneyforward.githubapp.ui.repos.data.RepoListRepositoryImpl
import com.moneyforward.apis.GithubApiService
import com.moneyforward.apis.model.Profile
import com.moneyforward.apis.model.RepositoryList
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

class RepoListRepositoryImplTest {

    private val githubApiService: GithubApiService = mockk()
    private val testDispatcher = StandardTestDispatcher()
    private lateinit var repository: RepoListRepositoryImpl

    private val mockProfile = Profile(name = "rasika")
    private val mockRepoList = RepositoryList()

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        repository = RepoListRepositoryImpl(githubApiService, testDispatcher)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `profile emits success when API returns data`() = runTest {
        coEvery { githubApiService.profile("rasika") } returns mockProfile

        val result = repository.profile("rasika").toList()

        assertEquals(1, result.size)
        val state = result.first()
        assertTrue(state.result is ApiResult.Success)
        assertEquals(mockProfile, (state.result as ApiResult.Success).data)
    }

    @Test
    fun `profile emits network error on IOException`() = runTest {
        coEvery { githubApiService.profile("rasika") } throws IOException("no connection")

        val result = repository.profile("rasika").toList()

        assertEquals(1, result.size)
        val state = result.first()
        assertTrue(state.result is ApiResult.NetworkError)
    }

    @Test
    fun `profile emits error on HttpException`() = runTest {
        val errorBody = ResponseBody.create(null, "error")
        val response = Response.error<Any>(403, errorBody)
        val httpException = HttpException(response)

        coEvery { githubApiService.profile("rasika") } throws httpException

        val result = repository.profile("rasika").toList()

        assertEquals(1, result.size)
        val state = result.first()
        assertTrue(state.result is ApiResult.Error)
        assertEquals(httpException, (state.result as ApiResult.Error).throwable)
    }

    @Test
    fun `profile emits error on unexpected exception`() = runTest {
        val exception = RuntimeException("unknown")
        coEvery { githubApiService.profile("rasika") } throws exception

        val result = repository.profile("rasika").toList()

        assertEquals(1, result.size)
        val state = result.first()
        assertTrue(state.result is ApiResult.Error)
        assertEquals("unknown", (state.result as ApiResult.Error).throwable.message)
    }

    @Test
    fun `repos emits success when API returns data`() = runTest {
        coEvery { githubApiService.repos("rasika") } returns mockRepoList

        val result = repository.repos("rasika").toList()

        assertEquals(1, result.size)
        val state = result.first()
        assertTrue(state.result is ApiResult.Success)
        assertEquals(mockRepoList, (state.result as ApiResult.Success).data)
    }

    @Test
    fun `repos emits network error on IOException`() = runTest {
        coEvery { githubApiService.repos("rasika") } throws IOException("offline")

        val result = repository.repos("rasika").toList()

        assertEquals(1, result.size)
        val state = result.first()
        assertTrue(state.result is ApiResult.NetworkError)
    }

    @Test
    fun `repos emits error on HttpException`() = runTest {
        val errorBody = ResponseBody.create(null, "403 forbidden")
        val response = Response.error<Any>(403, errorBody)
        val httpException = HttpException(response)

        coEvery { githubApiService.repos("rasika") } throws httpException

        val result = repository.repos("rasika").toList()

        assertEquals(1, result.size)
        val state = result.first()
        assertTrue(state.result is ApiResult.Error)
        assertEquals(httpException, (state.result as ApiResult.Error).throwable)
    }

    @Test
    fun `repos emits error on unexpected exception`() = runTest {
        val crash = RuntimeException("crashed")
        coEvery { githubApiService.repos("rasika") } throws crash

        val result = repository.repos("rasika").toList()

        assertEquals(1, result.size)
        val state = result.first()
        assertTrue(state.result is ApiResult.Error)
        assertEquals("crashed", (state.result as ApiResult.Error).throwable.message)
    }
}

