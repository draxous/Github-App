package com.moneyforward.githubapp.ui.userslist.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import com.moneyforward.githubapp.R
import com.moneyforward.githubapp.utils.ConnectionState
import com.moneyforward.githubapp.utils.connectivityState

/**
 * UserListScreen composable function.
 * This screen displays a list of users.
 * @param onUserSelected Callback function to handle user selection.
 * @param viewModel The view model for this screen.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserListScreen(
    onUserSelected: (String) -> Unit,
    viewModel: UserListViewModel = hiltViewModel(),
) {
    var searchQuery by remember { mutableStateOf("") }
    val uiState by viewModel.uiState.collectAsState()
    val connection by connectivityState()
    val isConnected = connection == ConnectionState.Available

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(id = R.string.users_title)) },
            )
        },
        snackbarHost = {
            if (!isConnected) {
                Snackbar(modifier = Modifier.padding(8.dp)) {
                    Text(stringResource(R.string.there_is_no_internet))
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            // Search Bar
            SearchBar(
                query = searchQuery,
                onQueryChange = { query ->
                    searchQuery = query
                    viewModel.fetchUsers(query)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            )

            // User List
            when {
                uiState.isLoading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }

                uiState.error != null -> {
                    ErrorState(
                        message = uiState.error.orEmpty(),
                        onRetry = { viewModel.retry() }
                    )
                }

                else -> {
                    LazyColumn {
                        // User items
                        when {
                            uiState.users?.items?.isEmpty() == true -> {
                                item {
                                    Column(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(top = 32.dp),
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        Image(
                                            painter = painterResource(id = R.drawable.user_notfound),
                                            contentDescription = stringResource(id = R.string.no_user_image_description),
                                            modifier = Modifier.fillMaxSize()
                                        )
                                        Spacer(modifier = Modifier.height(12.dp))
                                        Text(
                                            text = stringResource(id = R.string.no_user_message),
                                            style = MaterialTheme.typography.bodyMedium,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                }
                            }

                            else -> {
                                items(uiState.users?.items.orEmpty().filter { user ->
                                    user.login?.contains(searchQuery, ignoreCase = true) == true ||
                                            user.login?.contains(
                                                searchQuery,
                                                ignoreCase = true
                                            ) == true
                                }) { user ->
                                    user.login?.let {
                                        UserListItem(
                                            name = it,
                                            onClick = {
                                                user.login?.let { userName ->
                                                    onUserSelected(
                                                        userName
                                                    )
                                                }
                                            },
                                            avatarUrl = user.avatar_url
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun UserListItem(
    name: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    avatarUrl: String? = null,
) {

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 16.dp)
            .clickable(onClick = onClick),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (avatarUrl.isNullOrBlank()) {
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = name.first().toString(),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        } else {
            val painter = rememberAsyncImagePainter(
                model = avatarUrl,
                placeholder = painterResource(id = R.drawable.ic_profile),
                error = painterResource(id = R.drawable.ic_profile)
            )
            Image(
                painter = painter,
                contentDescription = stringResource(R.string.user_avatar_content_description),
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        Text(
            text = name,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun SearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    TextField(
        value = query,
        onValueChange = onQueryChange,
        modifier = modifier,
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = stringResource(R.string.search_icon_content_description)
            )
        },
        placeholder = { Text(stringResource(R.string.search_users_placeholder)) },
        shape = RoundedCornerShape(16.dp),
        singleLine = true
    )
}

@Composable
private fun ErrorState(
    message: String,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = message,
            color = MaterialTheme.colorScheme.error,
            modifier = Modifier.padding(16.dp)
        )
        Button(
            onClick = onRetry,
            modifier = Modifier.padding(8.dp)
        ) {
            Text(stringResource(R.string.retry_button_text))
        }
    }
}