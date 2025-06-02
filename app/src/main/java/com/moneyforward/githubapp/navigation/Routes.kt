package com.moneyforward.githubapp.navigation

/**
 * Sealed class representing the different routes in the application.
 * Each route is defined as an object inheriting from this class.
 *
 * @property route The string representation of the route.
 */
sealed class Route(val route: String) {
    /**
     * Represents the user list screen route.
     */
    object UserList : Route("user_list")
    /**
     * Represents the repository list screen route.
     * This route requires a username parameter.
     */
    object RepoList : Route("repo_list/{username}") {
        /**
         * Creates the route string for the repository list screen with the given username.
         * @param username The username to be included in the route.
         * @return The complete route string.
         */
        fun createRoute(username: String) = "repo_list/$username"
    }
    /**  Object containing constants for argument keys used in navigation. */    object Arguments {
        /** Argument key for the username. */        const val USER_NAME = "username"
    }
}