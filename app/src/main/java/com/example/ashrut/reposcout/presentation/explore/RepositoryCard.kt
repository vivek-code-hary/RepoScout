package com.example.ashrut.reposcout.presentation.explore

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.example.ashrut.reposcout.domain.model.Repository
import com.example.ashrut.reposcout.utils.formatGitHubDate

@Composable
fun RepositoryCard(
    repository: Repository,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = 16.dp,
                vertical = 6.dp
            )
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.Top
        ) {

            // Owner Avatar
            AsyncImage(
                model = repository.avatarUrl,
                contentDescription = "${repository.ownerName} avatar",
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
            )

            Spacer(
                modifier = Modifier.width(12.dp)
            )

            // Repository Information
            Column(
                modifier = Modifier.weight(1f)
            ) {

                // Repository Name
                Text(
                    text = repository.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1
                )

                Spacer(
                    modifier = Modifier.height(2.dp)
                )

                // Owner / Full Name
                Text(
                    text = repository.fullName,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1
                )

                Spacer(
                    modifier = Modifier.height(8.dp)
                )

                // Description
                Text(
                    text = repository.description
                        ?: "No description provided",
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 2
                )

                Spacer(
                    modifier = Modifier.height(12.dp)
                )

                // Stats
                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {

                    Text(
                        text = "⭐ ${repository.stars}",
                        style = MaterialTheme.typography.bodySmall
                    )

                    Text(
                        text = "🍴 ${repository.forks}",
                        style = MaterialTheme.typography.bodySmall
                    )

                    Text(
                        text = repository.language ?: "Unknown",
                        style = MaterialTheme.typography.bodySmall
                    )
                }

                Spacer(
                    modifier = Modifier.height(8.dp)
                )

                // License
                Text(
                    text = "License: ${
                        repository.license ?: "Not specified"
                    }",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(
                    modifier = Modifier.height(4.dp)
                )

                // Updated Date
                Text(
                    text = "Updated: ${formatGitHubDate(repository.updatedAt)}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1
                )
            }
        }
    }
}