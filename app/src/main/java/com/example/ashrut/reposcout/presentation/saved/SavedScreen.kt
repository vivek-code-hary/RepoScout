package com.example.ashrut.reposcout.presentation.saved


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ashrut.reposcout.domain.model.Repository
import com.example.ashrut.reposcout.presentation.explore.RepositoryCard

@Composable
fun SavedScreen(
    viewModel: SavedViewModel,
    onRepositoryClick: (Repository) -> Unit
) {

    val uiState by viewModel.uiState
        .collectAsStateWithLifecycle()

    when (val state = uiState) {

        SavedUiState.Loading -> {

            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        is SavedUiState.Success -> {

            if (state.repositories.isEmpty()) {

                EmptySavedContent()

            } else {

                LazyColumn(
                    modifier = Modifier.fillMaxSize(),

                    contentPadding = PaddingValues(
                        horizontal = 16.dp,
                        vertical = 16.dp
                    ),

                    verticalArrangement =
                        Arrangement.spacedBy(8.dp)
                ) {

                    items(
                        items = state.repositories,
                        key = { it.id }
                    ) { repository ->

                        RepositoryCard(
                            repository = repository,
                            onClick = {
                                onRepositoryClick(repository)
                            }
                        )
                    }
                }
            }
        }

        is SavedUiState.Error -> {

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),

                contentAlignment = Alignment.Center
            ) {

                Text(
                    text = state.message,
                    style =
                        MaterialTheme.typography.bodyLarge
                )
            }
        }
    }
}

@Composable
private fun EmptySavedContent() {

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),

        contentAlignment = Alignment.Center
    ) {

        Text(
            text = "No saved repositories yet.",
            style = MaterialTheme.typography.bodyLarge
        )
    }
}