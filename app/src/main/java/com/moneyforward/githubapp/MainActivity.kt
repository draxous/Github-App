package com.moneyforward.githubapp

import AppNavHost
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.moneyforward.githubapp.ui.theme.GithubAppTheme
import dagger.hilt.android.AndroidEntryPoint

/**
 * Main activity of the application.
 */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GithubAppTheme {
                AppNavHost()
            }
        }
    }
}

/**
 * A composable function that displays a greeting message.
 * @param name The name to greet.
 * @param modifier The modifier to be applied to the layout.
 * @see GreetingPreview
 */
@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

/**
 * A preview of the Greeting composable function.
 */
@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    GithubAppTheme {
        Greeting("Android")
    }
}