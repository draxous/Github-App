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

private fun NavGraphBuilder.userListScreen(navController: NavHostController) {
    composable(route = Route.UserList.route) {
        UserListScreen(
            onUserSelected = { username ->
                navController.navigate(Route.RepoList.createRoute(username))
            },
        )
    }
}

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