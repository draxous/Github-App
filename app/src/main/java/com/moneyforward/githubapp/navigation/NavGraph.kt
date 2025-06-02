import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.moneyforward.githubapp.navigation.Route
import com.moneyforward.githubapp.ui.repos.ui.RepoListScreen
import com.moneyforward.githubapp.ui.userslist.ui.UserListScreen

/**
 * Composable function for setting up the navigation host.
 *
 * @param navController The navigation controller to use for navigation. Defaults to a new instance of [rememberNavController].
 * @param startDestination The route of the start destination for the navigation graph. Defaults to [Route.UserList.route].
 */
@Composable
fun AppNavHost(
    navController: NavHostController = rememberNavController(),
    startDestination: String = Route.UserList.route
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        userListScreen(navController)
        repoListScreen(navController)
    }
}

/**
 * Extension function for [NavGraphBuilder] to define the user list screen.
 *
 * @param navController The navigation controller to use for navigation.
 */
private fun NavGraphBuilder.userListScreen(navController: NavHostController) {
    composable(route = Route.UserList.route) {
        UserListScreen(
            onUserSelected = { username ->
                navController.navigate(Route.RepoList.createRoute(username))
            },
        )
    }
}

/**
 * Extension function for [NavGraphBuilder] to define the repository list screen.
 *
 * @param navController The navigation controller to use for navigation.
 */
private fun NavGraphBuilder.repoListScreen(navController: NavHostController) {
    composable(
        route = Route.RepoList.route,
        arguments = listOf(
            navArgument(Route.Arguments.USER_NAME) {
                type = NavType.StringType
            }
        )
    ) { backStackEntry ->
        val username = backStackEntry.arguments?.getString(Route.Arguments.USER_NAME).orEmpty()
        RepoListScreen(
            userName = username,
            onBackPressed = { navController.popBackStack() }
        )
    }
}