import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.moneyforward.githubapp.navigation.Route
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
        repoListScreen()
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

private fun NavGraphBuilder.repoListScreen() {
    composable(
        route = Route.RepoList.route,
        arguments = listOf(
            navArgument(Route.Arguments.USERNAME) {
                type = NavType.StringType
            }
        )
    ) { backStackEntry ->
        val username = backStackEntry.arguments?.getString(Route.Arguments.USERNAME).orEmpty()
       /* RepoListScreen(
            username = username,
            onBackPressed = { navController.popBackStack() }
        )*/
    }
}