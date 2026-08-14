package com.example.ashrut.reposcout.presentation.common

import com.example.ashrut.reposcout.domain.model.Repository


sealed interface UiState {

    data object Loading : UiState

    data class Success(
        val repositories: List<Repository>,
        val isLoadingMore: Boolean = false,
        val paginationError: String? = null,
        val isRefreshing: Boolean = false
    ) : UiState

    data class Error(
        val message: String
    ) : UiState
}