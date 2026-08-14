package com.example.ashrut.reposcout.presentation.screen


import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.ashrut.reposcout.domain.repository.GitHubRepository
import com.example.ashrut.reposcout.presentation.explore.ExploreViewModel

@Composable
fun AppScaffold(
    navController: NavHostController,
    exploreViewModel: ExploreViewModel,
    repository: GitHubRepository
) {

    val backStackEntry by navController
        .currentBackStackEntryAsState()

    val currentRoute =
        backStackEntry?.destination?.route

    val showBottomBar =
        currentRoute == Routes.Explore.route ||
                currentRoute == Routes.Saved.route

    Scaffold(
        bottomBar = {

            if (showBottomBar) {

                NavigationBar {

                    NavigationBarItem(
                        selected =
                            currentRoute ==
                                    Routes.Explore.route,

                        onClick = {

                            navController.navigate(
                                Routes.Explore.route
                            ) {

                                popUpTo(
                                    Routes.Explore.route
                                ) {
                                    saveState = true
                                }

                                launchSingleTop = true
                                restoreState = true
                            }
                        },

                        icon = {
                            Icon(
                                imageVector =
                                    Icons.Default.Search,
                                contentDescription =
                                    "Explore"
                            )
                        },

                        label = {
                            Text("Explore")
                        }
                    )

                    NavigationBarItem(
                        selected =
                            currentRoute ==
                                    Routes.Saved.route,

                        onClick = {

                            navController.navigate(
                                Routes.Saved.route
                            ) {

                                popUpTo(
                                    Routes.Explore.route
                                ) {
                                    saveState = true
                                }

                                launchSingleTop = true
                                restoreState = true
                            }
                        },

                        icon = {
                            Icon(
                                imageVector =
                                    Icons.Default.Bookmark,
                                contentDescription =
                                    "Saved"
                            )
                        },

                        label = {
                            Text("Saved")
                        }
                    )
                }
            }
        }
    ) { paddingValues ->

        AppNavHost(
            navController = navController,
            exploreViewModel = exploreViewModel,
            repository = repository,
            paddingValues = paddingValues
        )
    }
}