package com.moneyforward.githubapp.ui.userslist.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moneyforward.api.common.ApiResult
import com.moneyforward.githubapp.ui.userslist.data.UserListRepository
import com.moneyforward.api.model.UserList
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class UserListUiState(
    val users: List<UserList> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class UserListViewModel @Inject constructor(
    private val userListRepository: UserListRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(UserListUiState())
    val uiState: StateFlow<UserListUiState> = _uiState.asStateFlow()

    fun fetchUsers(userName: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            _uiState.update { it.copy(isLoading = true, error = null) }

            userListRepository.githubUsers(userName).collect { apiState ->
                when (val result =apiState.result) {
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
                        Log.d("UserListViewModel", "Loading users: $userName")
                    }
                }
            }
        }
    }
}
