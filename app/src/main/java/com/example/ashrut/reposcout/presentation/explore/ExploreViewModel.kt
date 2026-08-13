package com.example.ashrut.reposcout.presentation.explore

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ashrut.reposcout.domain.repository.GitHubRepository
import com.example.ashrut.reposcout.presentation.common.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import com.example.ashrut.reposcout.utils.Result


class ExploreViewModel(
    private val repository: GitHubRepository
) : ViewModel() {

    companion object {
        private const val SEARCH_QUERY = "android"
        private const val PAGE_SIZE = 20
    }

    private val _uiState =
        MutableStateFlow<UiState>(UiState.Loading)

    val uiState = _uiState.asStateFlow()

    private var currentPage = 1

    private var isLoadingMore = false

    private var hasMorePages = true

    init {
        loadRepositories()
    }

    fun loadRepositories() {


        if (_uiState.value is UiState.Success) {
            return
        }

        viewModelScope.launch {

            _uiState.value = UiState.Loading

            currentPage = 1
            hasMorePages = true

            when (
                val result = repository.searchRepositories(
                    query = SEARCH_QUERY,
                    page = currentPage,
                    perPage = PAGE_SIZE
                )
            ) {

                is Result.Success -> {

                    _uiState.value =
                        UiState.Success(
                            repositories = result.data
                        )
                }

                is Result.Error -> {

                    _uiState.value =
                        UiState.Error(
                            result.message
                        )
                }
            }
        }
    }

    fun loadNextPage() {

        if (isLoadingMore || !hasMorePages) {
            return
        }

        val currentState =
            _uiState.value as? UiState.Success
                ?: return

        isLoadingMore = true

        _uiState.value = currentState.copy(
            isLoadingMore = true,
            paginationError = null
        )

        viewModelScope.launch {

            val nextPage = currentPage + 1

            when (
                val result = repository.searchRepositories(
                    query = SEARCH_QUERY,
                    page = nextPage,
                    perPage = PAGE_SIZE
                )
            ) {

                is Result.Success -> {

                    val newRepositories =
                        currentState.repositories + result.data

                    currentPage = nextPage

                    // Agar 20 se kam result aaye,
                    // iska matlab next page available nahi hai.
                    hasMorePages =
                        result.data.size == PAGE_SIZE

                    _uiState.value = UiState.Success(
                        repositories = newRepositories,
                        isLoadingMore = false,
                        paginationError = null
                    )
                }

                is Result.Error -> {

                    _uiState.value = currentState.copy(
                        isLoadingMore = false,
                        paginationError = result.message
                    )
                }
            }

            isLoadingMore = false
        }
    }
    fun retryNextPage() {
        loadNextPage()
    }
}