package com.moneyforward.githubapp.ui.userslist.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moneyforward.apis.common.ApiResult
import com.moneyforward.githubapp.ui.userslist.data.SearchUserRepository
import com.moneyforward.apis.model.SearchUserResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

/**
 * UI state for the UserList screen.
 *
 * @property users The list of users to display.
 * @property isLoading Whether the screen is currently loading data.
 * @property error An error message, if any.
 */
data class UserListUiState(
    val users: SearchUserResponse? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
    val lastQuery: String = ""
)

/**
 * ViewModel for the UserList screen.
 *
 * This ViewModel is responsible for fetching and managing the list of GitHub users.
 * It uses [SearchUserRepository] to interact with the data layer.
 *
 * @param searchUserRepository The repository for fetching user data.
 */
@HiltViewModel
class UserListViewModel @Inject constructor(
    private val searchUserRepository: SearchUserRepository,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : ViewModel() {

    /**
     * The private mutable state flow that holds the current UI state.
     */
    private val _uiState = MutableStateFlow(UserListUiState())

    /**
     * The public immutable state flow that exposes the UI state to the UI.
     */
    val uiState: StateFlow<UserListUiState> = _uiState.asStateFlow()

    /**
     * Fetches GitHub users based on the provided keyword.
     * @param keyword The search keyword.
     */
    fun fetchUsers(keyword: String) {
        viewModelScope.launch(dispatcher) {
            _uiState.update { it.copy(isLoading = true, error = null, lastQuery = keyword) }

            searchUserRepository.searchGithubUsers(keyword).collect { apiState ->
                when (val result = apiState.result) {
                    is ApiResult.Success -> {
                        _uiState.update {
                            it.copy(
                                users = result.data,
                                isLoading = false,
                                lastQuery = keyword
                            )
                        }
                    }

                    is ApiResult.Error -> {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                error = result.throwable.message ?: "Unknown error", // Ensure error is set
                                lastQuery = keyword
                            )
                        }
                        Timber.tag(TAG)
                            .d("Error fetching users: ${result.throwable.message}")
                    }

                    is ApiResult.NetworkError -> {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                error = "Network error: ${result.throwable.message}",
                                lastQuery = keyword
                            )
                        }
                    }

                    null -> {
                        // Loading state
                        Timber.tag(TAG).d("Loading users: $keyword")
                    }
                }
            }
        }
    }

    fun retry() {
        val lastQuery = _uiState.value.lastQuery
        if (lastQuery.isNotEmpty()) {
            fetchUsers(lastQuery)
        } else {
            // No previous query exists - set loading to false to prevent spinner
            _uiState.update { it.copy(isLoading = false) }
        }
    }


    companion object {
        private val TAG = UserListViewModel::class.java.simpleName.toString()
    }
}
