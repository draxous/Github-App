package com.moneyforward.githubapp.navigation

sealed class Route(val route: String) {
    object UserList : Route("user_list")
    object RepoList : Route("repo_list/{username}") {
        fun createRoute(username: String) = "repo_list/$username"
    }

    object Arguments {
        const val USER_NAME = "username"
    }
}