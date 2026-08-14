package com.example.ashrut.reposcout.presentation.explore

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ashrut.reposcout.domain.model.Repository
import com.example.ashrut.reposcout.presentation.common.UiState
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.navigation.NavController
import com.example.ashrut.reposcout.presentation.screen.Routes


@Composable
fun ExploreScreen(
    viewModel: ExploreViewModel,
    navController: NavController
) {

    val uiState by viewModel.uiState
        .collectAsStateWithLifecycle()

    val searchQuery by viewModel.searchQuery
        .collectAsStateWithLifecycle()

    val listState = rememberLazyListState()

    val shouldLoadMore by remember {
        derivedStateOf {

            val lastVisibleItem =
                listState.layoutInfo.visibleItemsInfo
                    .lastOrNull()
                    ?.index ?: 0

            val totalItems =
                listState.layoutInfo.totalItemsCount

            totalItems > 0 &&
                    lastVisibleItem >= totalItems - 3
        }
    }

    LaunchedEffect(shouldLoadMore) {

        if (shouldLoadMore) {
            viewModel.loadNextPage()
        }
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {

        RepositorySearchBar(
            query = searchQuery,
            onQueryChange = { query ->
                viewModel.onSearchQueryChanged(query)
            }
        )

        when (val state = uiState) {

            UiState.Loading -> {

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {

                    CircularProgressIndicator()
                }
            }

            is UiState.Success -> {

                if (state.repositories.isEmpty()) {

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        contentAlignment = Alignment.Center
                    ) {
                        EmptyContent()
                    }

                } else {

                    PullToRefreshBox(
                        isRefreshing = state.isRefreshing,
                        onRefresh = {
                            viewModel.refreshRepositories()
                        },
                        modifier = Modifier.weight(1f)
                    ) {

                        RepositoryList(
                            repositories = state.repositories,
                            listState = listState,
                            isLoadingMore = state.isLoadingMore,
                            paginationError = state.paginationError,

                            onRetryNextPage = {
                                viewModel.retryNextPage()
                            },

                            onRepositoryClick = { repository ->

                                navController.navigate(
                                    Routes.Details.createRoute(
                                        owner = repository.ownerName,
                                        repo = repository.name,
                                        id = repository.id
                                    )
                                )
                            },

                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }
            }

            is UiState.Error -> {

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {

                    ErrorContent(
                        message = state.message,
                        onRetry = {
                            viewModel.retrySearch()
                        }
                    )
                }
            }
        }
    }
}




@Composable
fun RepositorySearchBar(
    query: String,
    onQueryChange: (String) -> Unit
) {
    OutlinedTextField(
        value = query,
        onValueChange = onQueryChange,
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = 16.dp,
                vertical = 12.dp
            ),
        placeholder = {
            Text("Search repositories")
        },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Search"
            )
        },
        singleLine = true
    )
}

@Composable
fun EmptyContent() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "No repositories found"
        )
    }
}

@Composable
fun RepositoryList(
    repositories: List<Repository>,
    listState: LazyListState,
    isLoadingMore: Boolean,
    paginationError: String?,
    onRetryNextPage: () -> Unit,
    onRepositoryClick: (Repository) -> Unit,
    modifier: Modifier = Modifier
) {

    LazyColumn(
        state = listState,
        modifier = modifier,
        contentPadding = PaddingValues(
            top = 8.dp,
            bottom = 16.dp
        ),
        verticalArrangement =
            Arrangement.spacedBy(4.dp)
    ) {

        items(
            items = repositories,
            key = { it.id }
        ) { repository ->

            RepositoryCard(
                repository = repository,
                onClick = {
                    onRepositoryClick(repository)
                }
            )
        }

        if (isLoadingMore) {

            item {

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {

                    CircularProgressIndicator()
                }
            }
        }

        if (paginationError != null) {

            item {

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Text(
                        text = paginationError,
                        style = MaterialTheme.typography.bodyMedium
                    )

                    Spacer(
                        modifier = Modifier.height(8.dp)
                    )

                    Button(
                        onClick = onRetryNextPage
                    ) {
                        Text("Retry")
                    }
                }
            }
        }
    }
}

@Composable
fun ErrorContent(
    message: String,
    onRetry: () -> Unit
) {

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Text(text = message)

        Spacer(
            modifier = Modifier.height(12.dp)
        )

        Button(
            onClick = onRetry
        ) {
            Text("Retry")
        }
    }
}