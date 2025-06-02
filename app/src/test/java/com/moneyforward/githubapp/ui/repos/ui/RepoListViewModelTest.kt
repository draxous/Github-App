package com.moneyforward.githubapp.ui.repos.ui

import com.moneyforward.apis.common.ApiState
import com.moneyforward.apis.model.Profile
import com.moneyforward.apis.model.RepositoryList
import com.moneyforward.apis.model.RepositoryListItem
import com.moneyforward.githubapp.ui.repos.data.RepoListRepository
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
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
class RepoListViewModelTest {

    private val testDispatcher = UnconfinedTestDispatcher()
    private lateinit var viewModel: RepoListViewModel
    private val mockRepository: RepoListRepository = mockk()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        viewModel = RepoListViewModel(mockRepository, testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial state should be loading false with empty data`() {
        val initialState = viewModel.uiState.value
        assertNull(initialState.repoList)
        assertNull(initialState.profile)
        assertFalse(initialState.isLoading)
        assertNull(initialState.error)
    }

    @Test
    fun `fetchProfile should update state with profile data on success`() = runTest {
        val testProfile = Profile(login = "testUser", name = "Test User")
        val apiState = ApiState.success(testProfile)

        coEvery { mockRepository.profile(any()) } returns flowOf(apiState)

        viewModel.fetchProfile("testUser")

        val state = viewModel.uiState.value
        assertEquals(testProfile, state.profile)
        assertFalse(state.isLoading)
        assertNull(state.error)
    }

    @Test
    fun `fetchProfile should update state with error on API error`() = runTest {
        val errorMessage = "User not found"
        val apiState = ApiState.error<Profile>(Exception(errorMessage))

        coEvery { mockRepository.profile(any()) } returns flowOf(apiState)

        viewModel.fetchProfile("unknownUser")

        val state = viewModel.uiState.value
        assertNull(state.profile)
        assertFalse(state.isLoading)
        assertEquals(errorMessage, state.error)
    }

    @Test
    fun `fetchProfile should update state with network error on IOException`() = runTest {
        val errorMessage = "Network error"
        val apiState = ApiState.networkError<Profile>(IOException(errorMessage))

        coEvery { mockRepository.profile(any()) } returns flowOf(apiState)

        viewModel.fetchProfile("testUser")

        val state = viewModel.uiState.value
        assertNull(state.profile)
        assertFalse(state.isLoading)
        assertTrue(state.error?.contains("Network error") == true)
    }

    @Test
    fun `fetchRepos should update state with repo list on success`() = runTest {
        val testRepos = RepositoryList().apply {
            add(RepositoryListItem(id = 1, name = "repo1")) // Add your RepositoryListItem here
        }
        val apiState = ApiState.success(testRepos)

        coEvery { mockRepository.repos(any()) } returns flowOf(apiState)

        viewModel.fetchRepos("testUser")

        val state = viewModel.uiState.value
        assertEquals(testRepos, state.repoList)
        assertEquals(1, state.repoList?.size)
        assertEquals("repo1", state.repoList?.get(0)?.name)
        assertFalse(state.isLoading)
        assertNull(state.error)
    }

    @Test
    fun `fetchRepos should handle empty repository list`() = runTest {
        val testRepos = RepositoryList()
        val apiState = ApiState.success(testRepos)

        coEvery { mockRepository.repos(any()) } returns flowOf(apiState)

        viewModel.fetchRepos("testUser")

        val state = viewModel.uiState.value
        assertEquals(0, state.repoList?.size)
        assertFalse(state.isLoading)
        assertNull(state.error)
    }

    @Test
    fun `fetchRepos should update state with error on API error`() = runTest {
        val errorMessage = "Rate limit exceeded"
        val apiState = ApiState.error<RepositoryList>(Exception(errorMessage))

        coEvery { mockRepository.repos(any()) } returns flowOf(apiState)

        viewModel.fetchRepos("testUser")

        val state = viewModel.uiState.value
        assertNull(state.repoList)
        assertFalse(state.isLoading)
        assertEquals(errorMessage, state.error)
    }

    @Test
    fun `fetchRepos should update state with network error on IOException`() = runTest {
        val errorMessage = "No internet connection"
        val apiState = ApiState.networkError<RepositoryList>(IOException(errorMessage))

        coEvery { mockRepository.repos(any()) } returns flowOf(apiState)

        viewModel.fetchRepos("testUser")

        val state = viewModel.uiState.value
        assertNull(state.repoList)
        assertFalse(state.isLoading)
        assertTrue(state.error?.contains("Network error") == true)
    }

    @Test
    fun `fetchRepos should pass correct username to repository`() = runTest {
        val testRepos = RepositoryList().apply {
            add(RepositoryListItem(id = 1, name = "repo1"))
        }
        val apiState = ApiState.success(testRepos)

        coEvery { mockRepository.repos("testUser") } returns flowOf(apiState)

        viewModel.fetchRepos("testUser")

        verify { mockRepository.repos("testUser") }
    }

    // ... (keep all the existing profile-related tests as they were)
}