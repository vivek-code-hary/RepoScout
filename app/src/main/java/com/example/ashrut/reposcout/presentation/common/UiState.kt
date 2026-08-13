package com.example.ashrut.reposcout.presentation.common

import com.example.ashrut.reposcout.domain.model.Repository
import com.example.ashrut.reposcout.domain.repository.GitHubRepository

sealed interface UiState {
    data object Loading : UiState

    data class Success(val repositories: List<Repository>) : UiState
    data class Error(val message: String) : UiState
}