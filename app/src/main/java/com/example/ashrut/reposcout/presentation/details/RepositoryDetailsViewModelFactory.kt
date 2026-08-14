package com.example.ashrut.reposcout.presentation.details


import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.ashrut.reposcout.domain.repository.GitHubRepository

class RepositoryDetailsViewModelFactory(
    private val repository: GitHubRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(
        modelClass: Class<T>
    ): T {

        if (
            modelClass.isAssignableFrom(
                RepositoryDetailsViewModel::class.java
            )
        ) {
            return RepositoryDetailsViewModel(
                repository = repository
            ) as T
        }

        throw IllegalArgumentException(
            "Unknown ViewModel class"
        )
    }
}