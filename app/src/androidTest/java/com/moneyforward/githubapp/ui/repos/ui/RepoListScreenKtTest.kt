package com.moneyforward.githubapp.ui.repos.ui

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.moneyforward.apis.model.Profile
import com.moneyforward.apis.model.RepositoryList
import com.moneyforward.apis.model.RepositoryListItem
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import io.mockk.verify
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class RepoListScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val mockViewModel: RepoListViewModel = mockk()
    private val mockOnBackPressed: () -> Boolean = mockk(relaxed = true)

    @Test
    fun repoListScreen_displaysProfileInfo() {
        val testProfile = Profile(
            name = "Test User",
            login = "testUser",
            bio = "Test bio",
            avatar_url = "",
            followers = 10,
            following = 5
        )

        val testUiState = RepoListUiState(
            profile = testProfile,
            repoList = null
        )

        every { mockViewModel.uiState } returns MutableStateFlow(testUiState)
        every { mockViewModel.fetchProfile(any()) } just Runs
        every { mockViewModel.fetchRepos(any()) } just Runs

        composeTestRule.setContent {
            RepoListScreen(
                userName = "testUser",
                onBackPressed = mockOnBackPressed,
                viewModel = mockViewModel
            )
        }

        composeTestRule.onNodeWithText("Test User").assertIsDisplayed()
        composeTestRule.onNodeWithText("testUser").assertIsDisplayed()
        composeTestRule.onNodeWithText("Test bio").assertIsDisplayed()
        composeTestRule.onNodeWithText("10").assertIsDisplayed()
        composeTestRule.onNodeWithText("5").assertIsDisplayed()
    }

    @Test
    fun repoListScreen_displaysRepositories() {
        val testRepos = RepositoryList().apply {
            add(
                RepositoryListItem(
                    id = 1,
                    name = "test-repo",
                    full_name = "testUser/test-repo",
                    description = "Test repository",
                    stargazers_count = 42,
                    language = "Kotlin",
                    fork = false,
                    html_url = "https://github.com/testUser/test-repo"
                )
            )
        }

        val testUiState = RepoListUiState(
            profile = null,
            repoList = testRepos
        )

        every { mockViewModel.uiState } returns MutableStateFlow(testUiState)
        every { mockViewModel.fetchRepos(any()) } just runs
        every { mockViewModel.fetchProfile(any()) } just runs

        composeTestRule.setContent {
            RepoListScreen(
                userName = "testUser",
                onBackPressed = mockOnBackPressed,
                viewModel = mockViewModel
            )
        }

        composeTestRule.onNodeWithText("Repositories").assertIsDisplayed()
        composeTestRule.onNodeWithText("testUser/test-repo").assertIsDisplayed()
        composeTestRule.onNodeWithText("Test repository").assertIsDisplayed()
        composeTestRule.onNodeWithText("42").assertIsDisplayed()
        composeTestRule.onNodeWithText("Kotlin").assertIsDisplayed()
    }

    @Test
    fun repoListScreen_fetchesDataOnLaunch() {
        val testUiState = RepoListUiState(
            profile = null,
            repoList = RepositoryList()
        )

        every { mockViewModel.uiState } returns MutableStateFlow(testUiState)
        every { mockViewModel.fetchRepos(any()) } just runs
        every { mockViewModel.fetchProfile(any()) } just runs

        composeTestRule.setContent {
            RepoListScreen(
                userName = "testUser",
                onBackPressed = mockOnBackPressed,
                viewModel = mockViewModel
            )
        }

        verify { mockViewModel.fetchRepos("testUser") }
        verify { mockViewModel.fetchProfile("testUser") }
    }

    @Test
    fun repoListScreen_filtersOutForkedRepositories() {
        val testRepos = RepositoryList().apply {
            add(
                RepositoryListItem(
                    id = 1,
                    name = "original-repo",
                    full_name = "testUser/original-repo",
                    description = "Original repo",
                    stargazers_count = 10,
                    language = "Kotlin",
                    fork = false,
                    html_url = ""
                )
            )
            add(
                RepositoryListItem(
                    id = 2,
                    name = "forked-repo",
                    full_name = "testUser/forked-repo",
                    description = "Forked repo",
                    stargazers_count = 5,
                    language = "Java",
                    fork = true,
                    html_url = ""
                )
            )
        }

        val testUiState = RepoListUiState(
            profile = null,
            repoList = testRepos
        )

        every { mockViewModel.uiState } returns MutableStateFlow(testUiState)
        every { mockViewModel.fetchRepos(any()) } just runs
        every { mockViewModel.fetchProfile(any()) } just runs

        composeTestRule.setContent {
            RepoListScreen(
                userName = "testUser",
                onBackPressed = mockOnBackPressed,
                viewModel = mockViewModel
            )
        }

        composeTestRule.onNodeWithText("testUser/original-repo").assertIsDisplayed()
        composeTestRule.onNodeWithText("testUser/forked-repo").assertDoesNotExist()
    }

    @Test
    fun statBox_displaysCountAndLabel() {
        composeTestRule.setContent {
            StatBox(
                count = "42",
                label = "Followers"
            )
        }

        composeTestRule.onNodeWithText("42").assertIsDisplayed()
        composeTestRule.onNodeWithText("Followers").assertIsDisplayed()
    }

    @Test
    fun repositoryItem_displaysAllInformation() {
        val mockOnClick: () -> Unit = mockk(relaxed = true)

        composeTestRule.setContent {
            RepositoryItem(
                name = "test/repo",
                description = "Test repository",
                stars = 42,
                language = "Kotlin",
                onClick = mockOnClick
            )
        }

        composeTestRule.onNodeWithText("test/repo").assertIsDisplayed()
        composeTestRule.onNodeWithText("Test repository").assertIsDisplayed()
        composeTestRule.onNodeWithText("42").assertIsDisplayed()
        composeTestRule.onNodeWithText("Kotlin").assertIsDisplayed()
    }

    @Test
    fun repositoryItem_onClick_triggersCallback() {
        val mockOnClick: () -> Unit = mockk(relaxed = true)

        composeTestRule.setContent {
            RepositoryItem(
                name = "test/repo",
                description = "Test repository",
                stars = 42,
                language = "Kotlin",
                onClick = mockOnClick
            )
        }

        composeTestRule.onNodeWithText("test/repo").performClick()

        verify { mockOnClick.invoke() }
    }
}