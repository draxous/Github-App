package com.moneyforward.githubapp.ui.userslist.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moneyforward.apis.common.ApiResult
import com.moneyforward.githubapp.ui.userslist.data.UserListRepository
import com.moneyforward.apis.model.SearchUserResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

data class UserListUiState(
    val users: SearchUserResponse? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class UserListViewModel @Inject constructor(
    private val userListRepository: UserListRepository
) : ViewModel() {

    companion object {
        private val TAG = UserListViewModel::class.java.simpleName.toString()
    }

    private val _uiState = MutableStateFlow(UserListUiState())
    val uiState: StateFlow<UserListUiState> = _uiState.asStateFlow()

    fun fetchUsers(keyword: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            userListRepository.searchGithubUsers(keyword).collect { apiState ->
                when (val result = apiState.result) {
                    is ApiResult.Success -> {
                        _uiState.update {
                            it.copy(
                                users = result.data,
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
}
