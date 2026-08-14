package com.example.ashrut.reposcout.presentation.details


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.example.ashrut.reposcout.domain.model.Repository
import com.example.ashrut.reposcout.utils.formatGitHubDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RepositoryDetailsScreen(
    repository: Repository,
    isSaved: Boolean,
    isSaving: Boolean,
    onBack: () -> Unit,
    onOpenGitHub: () -> Unit,
    onSaveClick: () -> Unit
) {

    Scaffold(

        topBar = {

            TopAppBar(

                title = {
                    Text(
                        text = repository.name,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },

                navigationIcon = {

                    IconButton(
                        onClick = onBack
                    ) {

                        Icon(
                            imageVector =
                                Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }

    ) { paddingValues ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),

            contentPadding = PaddingValues(
                horizontal = 16.dp,
                vertical = 16.dp
            ),

            verticalArrangement =
                Arrangement.spacedBy(16.dp)
        ) {

            item {

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment =
                        Alignment.CenterVertically
                ) {

                    AsyncImage(
                        model = repository.avatarUrl,
                        contentDescription =
                            "${repository.ownerName} avatar",

                        modifier = Modifier
                            .size(64.dp)
                            .clip(CircleShape)
                    )

                    Spacer(
                        modifier = Modifier.width(14.dp)
                    )

                    Column(
                        modifier = Modifier.weight(1f)
                    ) {

                        Text(
                            text = repository.name,
                            style =
                                MaterialTheme
                                    .typography
                                    .headlineSmall,

                            fontWeight =
                                FontWeight.Bold
                        )

                        Text(
                            text = repository.fullName,
                            style =
                                MaterialTheme
                                    .typography
                                    .bodyMedium,

                            color =
                                MaterialTheme
                                    .colorScheme
                                    .onSurfaceVariant
                        )
                    }
                }
            }

            item {

                Text(
                    text = repository.description
                        ?: "No description provided",

                    style =
                        MaterialTheme
                            .typography
                            .bodyLarge
                )
            }

            item {

                RepositoryStats(
                    repository = repository
                )
            }

            item {

                RepositoryInfoCard(
                    repository = repository
                )
            }

            item {

                Button(
                    onClick = onOpenGitHub,
                    modifier = Modifier.fillMaxWidth()
                ) {

                    Text("Open on GitHub")
                }
            }

            item {

                OutlinedButton(
                    onClick = onSaveClick,
                    enabled = !isSaving,
                    modifier = Modifier.fillMaxWidth()
                ) {

                    if (isSaving) {

                        CircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            strokeWidth = 2.dp
                        )

                    } else {

                        Text(
                            text = if (isSaved) {
                                "Remove from Saved"
                            } else {
                                "Save Repository"
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun RepositoryStats(
    repository: Repository
) {

    Card(
        modifier = Modifier.fillMaxWidth()
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),

            horizontalArrangement =
                Arrangement.SpaceEvenly
        ) {

            StatItem(
                value = repository.stars.toString(),
                label = "Stars"
            )

            StatItem(
                value = repository.forks.toString(),
                label = "Forks"
            )

            StatItem(
                value = repository.watchers.toString(),
                label = "Watchers"
            )

            StatItem(
                value = repository.openIssues.toString(),
                label = "Issues"
            )
        }
    }
}


@Composable
private fun StatItem(
    value: String,
    label: String
) {

    Column(
        horizontalAlignment =
            Alignment.CenterHorizontally
    ) {

        Text(
            text = value,
            style =
                MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = label,
            style =
                MaterialTheme.typography.bodySmall,

            color =
                MaterialTheme.colorScheme
                    .onSurfaceVariant
        )
    }
}

@Composable
private fun RepositoryInfoCard(
    repository: Repository
) {

    Card(
        modifier = Modifier.fillMaxWidth()
    ) {

        Column(
            modifier = Modifier.padding(16.dp),

            verticalArrangement =
                Arrangement.spacedBy(12.dp)
        ) {

            Text(
                text = "Repository Information",
                style =
                    MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            InfoRow(
                label = "Language",
                value = repository.language
                    ?: "Unknown"
            )

            InfoRow(
                label = "License",
                value = repository.license
                    ?: "Not specified"
            )

            InfoRow(
                label = "Created",
                value = formatGitHubDate(
                    repository.createdAt
                )
            )

            InfoRow(
                label = "Updated",
                value = formatGitHubDate(
                    repository.updatedAt
                )
            )
        }
    }
}

@Composable
private fun InfoRow(
    label: String,
    value: String
) {

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement =
            Arrangement.SpaceBetween
    ) {

        Text(
            text = label,
            color =
                MaterialTheme.colorScheme
                    .onSurfaceVariant
        )

        Text(
            text = value,
            fontWeight = FontWeight.Medium
        )
    }
}