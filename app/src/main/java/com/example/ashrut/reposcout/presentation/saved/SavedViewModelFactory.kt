package com.example.ashrut.reposcout.presentation.saved


import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.ashrut.reposcout.domain.repository.GitHubRepository

class SavedViewModelFactory(
    private val repository: GitHubRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(
        modelClass: Class<T>
    ): T {

        if (
            modelClass.isAssignableFrom(
                SavedViewModel::class.java
            )
        ) {
            return SavedViewModel(
                repository = repository
            ) as T
        }

        throw IllegalArgumentException(
            "Unknown ViewModel class: ${modelClass.name}"
        )
    }
}