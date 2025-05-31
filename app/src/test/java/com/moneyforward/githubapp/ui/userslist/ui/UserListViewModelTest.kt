package com.moneyforward.githubapp.ui.userslist.ui

import com.moneyforward.apis.common.ApiState
import com.moneyforward.apis.model.SearchUserResponse
import com.moneyforward.githubapp.ui.userslist.data.SearchUserRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
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

@OptIn(ExperimentalCoroutinesApi::class)
class UserListViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var viewModel: UserListViewModel
    private val searchUserRepository: SearchUserRepository = mockk()

    private val mockResponse = SearchUserResponse(
        total_count = 1,
        incomplete_results = false,
        items = emptyList()
    )

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        viewModel = UserListViewModel(searchUserRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial state should be loading false and no error`() = runTest {
        val initialState = viewModel.uiState.value
        assertFalse(initialState.isLoading)
        assertNull(initialState.error)
        assertNull(initialState.users)
    }

    @Test
    fun `fetchUsers should set loading to true when starting`() = runTest {
        coEvery { searchUserRepository.searchGithubUsers(any()) } returns flowOf(
            ApiState.loading()
        )

        viewModel.fetchUsers("test")
        testDispatcher.scheduler.advanceUntilIdle()

        assertTrue(viewModel.uiState.value.isLoading)
    }

    @Test
    fun `fetchUsers should pass the correct keyword to repository`() = runTest {
        val testKeyword = "testUser"
        coEvery { searchUserRepository.searchGithubUsers(testKeyword) } returns flowOf(
            ApiState.success(
                SearchUserResponse(
                    total_count = 0,
                    incomplete_results = false,
                    items = emptyList()
                )
            )
        )

        viewModel.fetchUsers(testKeyword)
        testDispatcher.scheduler.advanceUntilIdle()

        coVerify { searchUserRepository.searchGithubUsers(testKeyword) }
    }


    @Test
    fun `fetchUsers emits loading and success state`() = runTest {
        coEvery { searchUserRepository.searchGithubUsers("mock") } returns flow {
            emit(ApiState.success(mockResponse))
        }

        launch {
            viewModel.fetchUsers("mock")
        }
        testDispatcher.scheduler.advanceUntilIdle()

        val states = viewModel.uiState.take(2).toList()

        assertTrue(states[0].isLoading)
        assertFalse(states[1].isLoading)
        assertEquals(mockResponse, states[1].users)
        assertNull(states[1].error)
    }

    @Test
    fun `fetchUsers emits loading and error state`() = runTest {
        val error = RuntimeException("Something went wrong")
        coEvery { searchUserRepository.searchGithubUsers("mock") } returns flow {
            emit(ApiState.error(error))
        }

        launch {
            viewModel.fetchUsers("mock")
        }
        testDispatcher.scheduler.advanceUntilIdle()

        val states = viewModel.uiState.take(2).toList()

        assertTrue(states[0].isLoading)
        assertFalse(states[1].isLoading)
        assertEquals("Something went wrong", states[1].error)
        assertNull(states[1].users)
    }

    @Test
    fun `fetchUsers emits loading and network error state`() = runTest {
        val networkError = RuntimeException("No internet")
        coEvery { searchUserRepository.searchGithubUsers("mock") } returns flow {
            emit(ApiState.networkError(networkError))
        }

        launch {
            viewModel.fetchUsers("mock")
        }
        testDispatcher.scheduler.advanceUntilIdle()

        val states = viewModel.uiState.take(2).toList()

        assertTrue(states[0].isLoading)
        assertFalse(states[1].isLoading)
        assertEquals("Network error: No internet", states[1].error)
        assertNull(states[1].users)
    }
}