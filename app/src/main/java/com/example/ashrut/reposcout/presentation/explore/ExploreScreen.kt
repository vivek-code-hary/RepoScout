package com.example.ashrut.reposcout.presentation.explore

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ashrut.reposcout.domain.model.Repository
import com.example.ashrut.reposcout.presentation.common.UiState

@Composable
fun ExploreScreen(
    viewModel: ExploreViewModel
){
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.loadRepositories()
    }

    when(val state = uiState){
        UiState.Loading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ){
                CircularProgressIndicator()
            }
        }
        is UiState.Success -> {
            RepositoryList(
                repositories = state.repositories
            )
        }

        is UiState.Error -> {
            ErrorContent(
                message = state.message,
                onRetry = {
                    viewModel.loadRepositories()
                }
            )
        }
    }
}

@Composable
fun RepositoryList(
    repositories: List<Repository>
) {

    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {

        items(
            items = repositories,
            key = { it.id }
        ) { repository ->

            Text(
                text = repository.name,
                modifier = Modifier.padding(16.dp)
            )
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