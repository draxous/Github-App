package com.moneyforward.githubapp.ui.userslist.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel

/**
 * UserListScreen composable function.
 * This screen displays a list of users.
 *
 * @param modifier The modifier to apply to this screen.
 * @param viewModel The view model for this screen.
 */
@Composable
fun UserListScreen(
    modifier: Modifier = Modifier,
    viewModel: UserListViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Column(modifier = modifier) {
        if (uiState.isLoading) {
            CircularProgressIndicator()
        }

        uiState.error?.let { error ->
            Text("Error: $error", color = Color.Red)
        }

        LazyColumn {
            uiState.users?.items?.let { items ->
                items(items) { user ->
                    Text(user.url)
                }
            }?: run {
                item {
                    Text("No users found")
                }
            }
        }

        Button(onClick = { viewModel.fetchUsers("draxous") }) {
            Text("Load Users")
        }
    }
}