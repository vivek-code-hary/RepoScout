package com.example.ashrut.reposcout

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.ashrut.reposcout.presentation.explore.ExploreScreen
import com.example.ashrut.reposcout.presentation.explore.ExploreViewModel
import com.example.ashrut.reposcout.presentation.explore.ExploreViewModelFactory
import com.example.ashrut.reposcout.presentation.screen.AppNavHost
import com.example.ashrut.reposcout.ui.theme.RepoScoutTheme
import com.example.ashrut.reposcout.utils.AppContainer

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {

            val navController = rememberNavController()

            val exploreFactory = ExploreViewModelFactory(
                repository = AppContainer.gitHubRepository
            )

            val exploreViewModel: ExploreViewModel =
                viewModel(
                    factory = exploreFactory
                )

            RepoScoutTheme {

                AppNavHost(
                    navController = navController,
                    exploreViewModel = exploreViewModel,
                    repository = AppContainer.gitHubRepository
                )
            }
        }
    }
}

