package com.moneyforward.githubapp.ui.repos.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moneyforward.apis.common.ApiResult
import com.moneyforward.apis.model.Profile
import com.moneyforward.apis.model.RepositoryList
import com.moneyforward.githubapp.ui.repos.data.RepoListRepository
import com.moneyforward.githubapp.ui.userslist.ui.UserListViewModel
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
 * Represents the UI state for the repository list screen.
 *
 * @property repoList The list of repositories to display.
 * @property isLoading Indicates whether the data is currently being loaded.
 * @property error An error message to display, if any.
 */
data class RepoListUiState(
    val repoList: RepositoryList? = null,
    val profile: Profile? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
)

/**
 * ViewModel for the repository list screen.
 *
 * This ViewModel is responsible for fetching and managing the list of repositories
 * and exposing it to the UI through a [StateFlow] of [RepoListUiState].
 *
 * @param repoListRepository The repository for fetching repository data.
 */
@HiltViewModel
open class RepoListViewModel @Inject constructor(
    private val repoListRepository: RepoListRepository,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : ViewModel() {

    /**
     * The private mutable state flow that holds the current UI state.
     */
    private val _uiState = MutableStateFlow(RepoListUiState())

    /**
     * The public immutable state flow that exposes the UI state to the UI.
     */
    open val uiState: StateFlow<RepoListUiState> = _uiState.asStateFlow()

    fun fetchProfile(keyword: String) {
        viewModelScope.launch(dispatcher) {
            _uiState.update { it.copy(isLoading = true, error = null) }

            repoListRepository.profile(keyword).collect { apiState ->
                when (val result = apiState.result) {
                    is ApiResult.Success -> {
                        _uiState.update {
                            it.copy(
                                profile = result.data,
                                isLoading = false
                            )
                        }
                    }

                    is ApiResult.Error -> {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                error = result.throwable.message ?: "Unknown error"
                            )
                        }
                        Timber.tag(TAG)
                            .d("Error fetching users: ${result.throwable.message}")
                    }

                    is ApiResult.NetworkError -> {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                error = "Network error: ${result.throwable.message}"
                            )
                        }
                    }

                    null -> {
                        // Loading state already handled by initial update
                        Timber.tag(TAG).d("Loading users: $keyword")
                    }
                }
            }
        }
    }

    fun fetchRepos(keyword: String) {
        viewModelScope.launch(dispatcher) {
            _uiState.update { it.copy(isLoading = true, error = null) }

            repoListRepository.repos(keyword).collect { apiState ->
                when (val result = apiState.result) {
                    is ApiResult.Success -> {
                        _uiState.update {
                            it.copy(
                                repoList = result.data,
                                isLoading = false
                            )
                        }
                    }

                    is ApiResult.Error -> {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                error = result.throwable.message ?: "Unknown error"
                            )
                        }
                        Timber.tag(TAG)
                            .d("Error fetching users: ${result.throwable.message}")
                    }

                    is ApiResult.NetworkError -> {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                error = "Network error: ${result.throwable.message}"
                            )
                        }
                    }

                    null -> {
                        // Loading state already handled by initial update
                        Timber.tag(TAG).d("Loading users: $keyword")
                    }
                }
            }
        }
    }


    companion object {
        private val TAG = UserListViewModel::class.java.simpleName.toString()
    }
}