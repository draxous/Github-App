package com.moneyforward.githubapp.navigation

sealed class Route(val route: String) {
    object UserList : Route("user_list")
    object RepoList : Route("repo_list/{username}") {
        fun createRoute(username: String) = "repo_list/$username"
    }

    // Optional: Add arguments for type safety
    object Arguments {
        const val USERNAME = "username"
    }
}