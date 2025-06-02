package com.moneyforward.githubapp.ui.userslist.ui

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.moneyforward.apis.model.Item
import com.moneyforward.apis.model.SearchUserResponse
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
class UserListScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val mockViewModel: UserListViewModel = mockk(relaxed = true)

    @Test
    fun whenLoadingState_showsProgressIndicator() {
        val testUiState = UserListUiState(isLoading = true)
        every { mockViewModel.uiState } returns MutableStateFlow(testUiState)

        composeTestRule.setContent {
            UserListScreen(onUserSelected = {}, viewModel = mockViewModel)
        }

        composeTestRule.onNodeWithTag("loading-indicator").assertIsDisplayed()
    }

    @Test
    fun whenErrorState_showsErrorMessageAndRetryButton() {
        val errorMessage = "Network error"
        val testUiState = UserListUiState(error = errorMessage)
        every { mockViewModel.uiState } returns MutableStateFlow(testUiState)

        composeTestRule.setContent {
            UserListScreen(onUserSelected = {}, viewModel = mockViewModel)
        }

        composeTestRule.onNodeWithText(errorMessage).assertIsDisplayed()
        composeTestRule.onNodeWithText("Retry").assertIsDisplayed()

        composeTestRule.onNodeWithText("Retry").performClick()
        verify { mockViewModel.retry() }
    }

    @Test
    fun whenUsersExist_showsUserList() {
        val testUsers = listOf(
            Item(login = "user1", avatar_url = "url1"),
            Item(login = "user2", avatar_url = "url2")
        )
        val testUiState = UserListUiState(
            users = SearchUserResponse(items = testUsers)
        )
        every { mockViewModel.uiState } returns MutableStateFlow(testUiState)

        composeTestRule.setContent {
            UserListScreen(onUserSelected = {}, viewModel = mockViewModel)
        }

        composeTestRule.onNodeWithText("user1").assertIsDisplayed()
        composeTestRule.onNodeWithText("user2").assertIsDisplayed()
    }

    @Test
    fun whenSearching_filtersUserList() {
        val testUsers = listOf(
            Item(login = "androiduser", avatar_url = "url1"),
            Item(login = "kotlinuser", avatar_url = "url2")
        )
        val testUiState = UserListUiState(
            users = SearchUserResponse(items = testUsers),
            lastQuery = "android"
        )
        every { mockViewModel.uiState } returns MutableStateFlow(testUiState)
        every { mockViewModel.fetchUsers(any()) } just runs

        composeTestRule.setContent {
            UserListScreen(onUserSelected = {}, viewModel = mockViewModel)
        }

        composeTestRule.onNodeWithTag("search-field").performTextInput("android")
        composeTestRule.onNodeWithText("androiduser").assertIsDisplayed()
        composeTestRule.onNodeWithText("kotlinuser").assertDoesNotExist()
        verify { mockViewModel.fetchUsers("android") }
    }


    @Test
    fun whenUserItemClicked_invokesCallback() {
        val testUsers = listOf(Item(login = "testuser", avatar_url = "url1"))
        val testUiState = UserListUiState(
            users = SearchUserResponse(items = testUsers)
        )
        every { mockViewModel.uiState } returns MutableStateFlow(testUiState)
        var selectedUser: String? = null

        composeTestRule.setContent {
            UserListScreen(
                onUserSelected = { selectedUser = it },
                viewModel = mockViewModel
            )
        }

        composeTestRule.onNodeWithText("testuser").performClick()
    }

    @Test
    fun whenAvatarUrlExists_showsUserAvatar() {
        val testUsers = listOf(Item(login = "user1", avatar_url = "https://example.com/avatar.jpg"))
        val testUiState = UserListUiState(
            users = SearchUserResponse(items = testUsers)
        )
        every { mockViewModel.uiState } returns MutableStateFlow(testUiState)

        composeTestRule.setContent {
            UserListScreen(onUserSelected = {}, viewModel = mockViewModel)
        }

        composeTestRule.onNodeWithContentDescription("User avatar").assertIsDisplayed()
    }

}