package com.moneyforward.githubapp.ui.userslist.ui

import com.moneyforward.apis.common.ApiState
import com.moneyforward.apis.model.Item
import com.moneyforward.apis.model.SearchUserResponse
import com.moneyforward.githubapp.ui.userslist.data.SearchUserRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.io.IOException

@OptIn(ExperimentalCoroutinesApi::class)
class UserListViewModelTest {

    private val testDispatcher = UnconfinedTestDispatcher()
    private lateinit var viewModel: UserListViewModel
    private val mockRepository: SearchUserRepository = mockk()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        viewModel = UserListViewModel(mockRepository, testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial state should be loading false with empty data`() {
        val initialState = viewModel.uiState.value
        assertNull(initialState.users)
        assertFalse(initialState.isLoading)
        assertNull(initialState.error)
        assertEquals("", initialState.lastQuery)
    }

    @Test
    fun `fetchUsers should update state with user data on success`() = runTest {
        val testUsers = SearchUserResponse(
            total_count = 1,
            incomplete_results = false,
            items = listOf(Item(id = 1, login = "testUser"))
        )
        val apiState = ApiState.success(testUsers)

        coEvery { mockRepository.searchGithubUsers(any()) } returns flowOf(apiState)

        viewModel.fetchUsers("test")

        val state = viewModel.uiState.value
        assertEquals(testUsers, state.users)
        assertEquals(1, state.users?.items?.size)
        assertEquals("testUser", state.users?.items?.get(0)?.login)
        assertFalse(state.isLoading)
        assertNull(state.error)
        assertEquals("test", state.lastQuery)
    }

    @Test
    fun `fetchUsers should handle empty user list`() = runTest {
        val testUsers = SearchUserResponse(
            total_count = 0,
            incomplete_results = false,
            items = emptyList()
        )
        val apiState = ApiState.success(testUsers)

        coEvery { mockRepository.searchGithubUsers(any()) } returns flowOf(apiState)

        viewModel.fetchUsers("empty")

        val state = viewModel.uiState.value
        assertEquals(0, state.users?.items?.size)
        assertFalse(state.isLoading)
        assertNull(state.error)
        assertEquals("empty", state.lastQuery)
    }

    @Test
    fun `fetchUsers should update state with error on API error`() = runTest {
        val errorMessage = "Rate limit exceeded"
        val apiState = ApiState.error<SearchUserResponse>(Exception(errorMessage))

        coEvery { mockRepository.searchGithubUsers(any()) } returns flowOf(apiState)

        viewModel.fetchUsers("error")

        val state = viewModel.uiState.value
        assertNull(state.users)
        assertFalse(state.isLoading)
        assertEquals(errorMessage, state.error)
        assertEquals("error", state.lastQuery)
    }

    @Test
    fun `fetchUsers should update state with network error on IOException`() = runTest {
        val errorMessage = "No internet connection"
        val apiState = ApiState.networkError<SearchUserResponse>(IOException(errorMessage))

        coEvery { mockRepository.searchGithubUsers(any()) } returns flowOf(apiState)

        viewModel.fetchUsers("networkError")

        val state = viewModel.uiState.value
        assertNull(state.users)
        assertFalse(state.isLoading)
        assertTrue(state.error?.contains("Network error") == true)
        assertEquals("networkError", state.lastQuery)
    }

    @Test
    fun `fetchUsers should pass correct query to repository`() = runTest {
        val testUsers = SearchUserResponse(
            total_count = 1,
            incomplete_results = false,
            items = listOf(Item(id = 1, login = "testUser"))
        )
        val apiState = ApiState.success(testUsers)

        coEvery { mockRepository.searchGithubUsers("test query") } returns flowOf(apiState)

        viewModel.fetchUsers("test query")

        // Use coVerify for suspension functions
        coVerify { mockRepository.searchGithubUsers("test query") }
    }

    @Test
    fun `retry should fetch users with last query`() = runTest {
        val testUsers = SearchUserResponse(
            total_count = 1,
            incomplete_results = false,
            items = listOf(Item(id = 1, login = "testUser"))
        )

        // First call fails with specific error
        coEvery { mockRepository.searchGithubUsers("first") } returns flow {
            emit(ApiState.error(Exception("First error")))
        }

        // Second call succeeds when retrying
        coEvery { mockRepository.searchGithubUsers("first") } returns flowOf(
            ApiState.success(testUsers)
        )

        // First attempt (should fail)
        viewModel.fetchUsers("first")

        // Need to advance time to process the emission
        advanceUntilIdle()


        // Retry
        viewModel.retry()

        // Advance time again
        advanceUntilIdle()

        // Verify successful state after retry
        val state = viewModel.uiState.value
        assertEquals(testUsers, state.users)
        assertFalse(state.isLoading)
        assertNull(state.error)
        assertEquals("first", state.lastQuery)
    }

    @Test
    fun `retry should do nothing if no previous query exists`() = runTest {
        val initialState = viewModel.uiState.value


        viewModel.retry()

        // State should remain unchanged
        assertEquals(initialState, viewModel.uiState.value)
    }
}