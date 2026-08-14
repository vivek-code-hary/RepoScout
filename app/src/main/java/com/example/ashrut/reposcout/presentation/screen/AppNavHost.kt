package com.example.ashrut.reposcout.presentation.screen

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.ashrut.reposcout.domain.repository.GitHubRepository
import com.example.ashrut.reposcout.presentation.details.DetailsUiState
import com.example.ashrut.reposcout.presentation.details.RepositoryDetailsScreen
import com.example.ashrut.reposcout.presentation.details.RepositoryDetailsViewModel
import com.example.ashrut.reposcout.presentation.details.RepositoryDetailsViewModelFactory
import com.example.ashrut.reposcout.presentation.explore.ErrorContent
import com.example.ashrut.reposcout.presentation.explore.ExploreScreen
import com.example.ashrut.reposcout.presentation.explore.ExploreViewModel
import com.example.ashrut.reposcout.presentation.saved.SavedScreen
import com.example.ashrut.reposcout.presentation.saved.SavedViewModel
import com.example.ashrut.reposcout.presentation.saved.SavedViewModelFactory
@Composable
fun AppNavHost(
    navController: NavHostController,
    exploreViewModel: ExploreViewModel,
    repository: GitHubRepository,
    paddingValues: PaddingValues
) {

    NavHost(
        navController = navController,
        startDestination = Routes.Explore.route,
        modifier = Modifier.padding(paddingValues)
    ) {

        // EXPLORE
        composable(
            route = Routes.Explore.route
        ) {

            ExploreScreen(
                viewModel = exploreViewModel,
                navController = navController
            )
        }

        // DETAIL
        composable(
            route = Routes.Details.route
        ) { backStackEntry ->

            val context = LocalContext.current

            val owner =
                backStackEntry.arguments
                    ?.getString("owner")
                    ?: return@composable

            val repo =
                backStackEntry.arguments
                    ?.getString("repo")
                    ?: return@composable

            val repositoryId =
                backStackEntry.arguments
                    ?.getString("id")
                    ?.toLongOrNull()
                    ?: return@composable

            val detailsViewModel:
                    RepositoryDetailsViewModel =
                viewModel(
                    factory =
                        RepositoryDetailsViewModelFactory(
                            repository = repository
                        )
                )

            LaunchedEffect(
                owner,
                repo,
                repositoryId
            ) {

                detailsViewModel.loadRepository(
                    owner = owner,
                    repo = repo,
                    repositoryId = repositoryId
                )
            }

            val uiState by detailsViewModel
                .uiState
                .collectAsStateWithLifecycle()

            when (val state = uiState) {

                DetailsUiState.Loading -> {

                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment =
                            Alignment.Center
                    ) {

                        CircularProgressIndicator()
                    }
                }

                is DetailsUiState.Success -> {

                    RepositoryDetailsScreen(
                        repository = state.repository,

                        isSaved = state.isSaved,

                        isSaving = state.isSaving,

                        onBack = {
                            navController.popBackStack()
                        },

                        onOpenGitHub = {

                            val intent = Intent(
                                Intent.ACTION_VIEW,
                                Uri.parse(
                                    state.repository.htmlUrl
                                )
                            )

                            context.startActivity(intent)
                        },

                        onSaveClick = {
                            detailsViewModel.toggleSaved()
                        }
                    )
                }

                is DetailsUiState.Error -> {

                    ErrorContent(
                        message = state.message,

                        onRetry = {
                            detailsViewModel.retry()
                        }
                    )
                }
            }
        }

        // SAVED
        composable(
            route = Routes.Saved.route
        ) {

            val savedViewModel:
                    SavedViewModel =
                viewModel(
                    factory =
                        SavedViewModelFactory(
                            repository = repository
                        )
                )

            SavedScreen(
                viewModel = savedViewModel,

                onRepositoryClick = { savedRepository ->

                    navController.navigate(
                        Routes.Details.createRoute(
                            owner =
                                savedRepository.ownerName,

                            repo =
                                savedRepository.name,

                            id =
                                savedRepository.id
                        )
                    )
                }
            )
        }
    }
}