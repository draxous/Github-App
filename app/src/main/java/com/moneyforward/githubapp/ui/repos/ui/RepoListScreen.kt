package com.moneyforward.githubapp.ui.repos.ui

import android.content.Intent
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
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import com.moneyforward.githubapp.R
import com.moneyforward.githubapp.utils.ConnectionState
import com.moneyforward.githubapp.utils.connectivityState

/**
 * Composable function for displaying the repository list screen.
 *
 * @param userName The username for which repositories are to be fetched.
 * @param onBackPressed Callback function to handle the back press event.
 * @param viewModel The view model for this screen.
 */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RepoListScreen(
    userName: String,
    onBackPressed: () -> Boolean,
    viewModel: RepoListViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val connection by connectivityState()
    val isConnected = connection == ConnectionState.Available

    LaunchedEffect(userName) {
        viewModel.fetchRepos(userName)
        viewModel.fetchProfile(userName)
    }

    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(id = R.string.profile_title)) },
                navigationIcon = {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = stringResource(id = R.string.back_button_description),
                        modifier = Modifier
                            .clickable { onBackPressed() }
                            .padding(horizontal = 16.dp)
                    )
                }
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
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            color = MaterialTheme.colorScheme.background
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Avatar
                item {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        if (uiState.profile?.avatar_url.isNullOrBlank()) {
                            Box(
                                modifier = Modifier
                                    .size(96.dp)
                                    .clip(CircleShape)
                                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = uiState.profile?.name.orEmpty().take(1),
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                        } else {
                            val painter = rememberAsyncImagePainter(
                                model = uiState.profile?.avatar_url,
                                placeholder = painterResource(id = R.drawable.ic_launcher_background),
                                error = painterResource(id = R.drawable.ic_launcher_background)
                            )
                            Image(
                                painter = painter,
                                contentDescription = stringResource(id = R.string.user_avatar_content_description),
                                modifier = Modifier
                                    .size(132.dp)
                                    .clip(CircleShape)
                            )
                        }
                    }
                }

                // Name + handle + bio
                item {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                uiState.profile?.name.orEmpty(),
                                style = MaterialTheme.typography.headlineMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                userName,
                                fontWeight = FontWeight.Bold,
                                style = MaterialTheme.typography.headlineSmall,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                uiState.profile?.bio.orEmpty(),
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }

                // Follower / Following boxes
                item {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(8.dp))
                                .background(MaterialTheme.colorScheme.surfaceVariant)
                                .padding(vertical = 16.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            StatBox(
                                count = uiState.profile?.followers?.toString().orEmpty(),
                                label = stringResource(id = R.string.followers_label)
                            )
                        }

                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(8.dp))
                                .background(MaterialTheme.colorScheme.surfaceVariant)
                                .padding(vertical = 16.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            StatBox(
                                count = uiState.profile?.following?.toString().orEmpty(),
                                label = stringResource(id = R.string.following_label)
                            )
                        }
                    }
                }

                item {
                    Text(
                        stringResource(id = R.string.repositories_title),
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                if (uiState.repoList.isNullOrEmpty()) {
                    item {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 32.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.no_repos),
                                contentDescription = stringResource(id = R.string.no_repositories_image_description),
                                modifier = Modifier.size(96.dp)
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                text = stringResource(id = R.string.no_repositories_yet_message),
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                } else {

                    // Filter out forked repositories.
                    val nonForkedRepos = uiState.repoList.orEmpty().filter { it.fork != true }

                    when {
                        nonForkedRepos.isEmpty() -> {
                            item {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(top = 32.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Image(
                                        painter = painterResource(id = R.drawable.no_repos),
                                        contentDescription = stringResource(id = R.string.no_repositories_image_description),
                                        modifier = Modifier.size(96.dp)
                                    )
                                    Spacer(modifier = Modifier.height(12.dp))
                                    Text(
                                        text = stringResource(id = R.string.no_original_repositories_message),
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        }

                        else -> {
                            items(nonForkedRepos) { repo ->
                                RepositoryItem(
                                    name = repo.full_name.orEmpty(),
                                    description = repo.description.orEmpty(),
                                    stars = repo.stargazers_count ?: 0,
                                    language = repo.language.orEmpty(),
                                    onClick = {
                                        repo.html_url?.let { url ->
                                            val intent = Intent(Intent.ACTION_VIEW, url.toUri())
                                            context.startActivity(intent)
                                        }
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

/**
 * Composable function for displaying a stat box.
 *
 * @param count The count to be displayed in the stat box.
 * @param label The label for the stat box.
 */

@Composable
fun StatBox(count: String, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = count,
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
            color = MaterialTheme.colorScheme.onSurface
        )
        Text(
            text = label,
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Preview(showBackground = true)
@Composable
fun StatBoxPreview() {
    StatBox(count = "100", label = "Followers")
}

/**
 * Composable function for displaying a repository item.
 *
 * @param name The name of the repository.
 * @param description The description of the repository.
 * @param stars The number of stars for the repository.
 * @param language The programming language of the repository.
 * @param onClick Callback function to handle the click event on the repository item.
 */

@Composable
fun RepositoryItem(
    name: String,
    description: String,
    stars: Int,
    language: String,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .padding(vertical = 8.dp)
            .clickable(onClick = onClick)
    ) {
        Text(name, fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.onSurface)

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(top = 4.dp, bottom = 8.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Star,
                contentDescription = stringResource(id = R.string.star_icon_description),
                tint = Color(0xFFFFD700),
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(stars.toString(), fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurface)
        }

        Text(description, fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Text(language, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}

@Preview(showBackground = true)
@Composable
fun RepositoryItemPreview() {
    RepositoryItem(
        name = "Repo Name",
        description = "This is a sample repository description.",
        stars = 123,
        language = "Kotlin",
        onClick = {}
    )
}
