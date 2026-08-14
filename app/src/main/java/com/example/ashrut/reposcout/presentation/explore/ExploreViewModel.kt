package com.example.ashrut.reposcout.presentation.explore

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ashrut.reposcout.domain.repository.GitHubRepository
import com.example.ashrut.reposcout.presentation.common.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import com.example.ashrut.reposcout.utils.Result
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged



class ExploreViewModel(
    private val repository: GitHubRepository
) : ViewModel() {

    companion object {
        private const val DEFAULT_QUERY = "android"
        private const val PAGE_SIZE = 20
    }

    private val _isRefreshing =
        MutableStateFlow(false)

    val isRefreshing =
        _isRefreshing.asStateFlow()

    private val _uiState =
        MutableStateFlow<UiState>(UiState.Loading)

    val uiState = _uiState.asStateFlow()

    private val _searchQuery =
        MutableStateFlow(DEFAULT_QUERY)

    val searchQuery =
        _searchQuery.asStateFlow()

    private var currentQuery = DEFAULT_QUERY

    private var currentPage = 1

    private var isLoadingMore = false

    private var hasMorePages = true

    init {
        observeSearchQuery()
    }

    @OptIn(FlowPreview::class)
    private fun observeSearchQuery() {

        viewModelScope.launch {

            _searchQuery
                .debounce(500)
                .distinctUntilChanged()
                .collectLatest { query ->

                    val cleanQuery = query.trim()

                    if (cleanQuery.isBlank()) {

                        currentQuery = ""
                        currentPage = 1
                        hasMorePages = false
                        isLoadingMore = false

                        _uiState.value =
                            UiState.Success(
                                repositories = emptyList()
                            )

                        return@collectLatest
                    }

                    searchRepositories(
                        query = cleanQuery
                    )
                }
        }
    }

    fun onSearchQueryChanged(
        query: String
    ) {
        _searchQuery.value = query
    }


    private suspend fun searchRepositories(
        query: String
    ) {

        currentQuery = query
        currentPage = 1
        hasMorePages = true
        isLoadingMore = false

        _uiState.value =
            UiState.Loading

        when (
            val result =
                repository.searchRepositories(
                    query = query,
                    page = currentPage,
                    perPage = PAGE_SIZE
                )
        ) {

            is Result.Success -> {

                hasMorePages =
                    result.data.size == PAGE_SIZE

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

    fun loadNextPage() {

        if (isLoadingMore || !hasMorePages) {
            return
        }

        val currentState =
            _uiState.value as? UiState.Success
                ?: return

        if (currentQuery.isBlank()) {
            return
        }

        isLoadingMore = true

        _uiState.value =
            currentState.copy(
                isLoadingMore = true,
                paginationError = null
            )

        viewModelScope.launch {

            val nextPage =
                currentPage + 1

            when (
                val result =
                    repository.searchRepositories(
                        query = currentQuery,
                        page = nextPage,
                        perPage = PAGE_SIZE
                    )
            ) {

                is Result.Success -> {

                    val newRepositories =
                        currentState.repositories +
                                result.data

                    currentPage = nextPage

                    hasMorePages =
                        result.data.size == PAGE_SIZE

                    _uiState.value =
                        UiState.Success(
                            repositories =
                                newRepositories,
                            isLoadingMore = false,
                            paginationError = null
                        )
                }

                is Result.Error -> {

                    _uiState.value =
                        currentState.copy(
                            isLoadingMore = false,
                            paginationError =
                                result.message
                        )
                }
            }

            isLoadingMore = false
        }
    }

    fun retryNextPage() {
        loadNextPage()
    }

    fun retrySearch() {

        viewModelScope.launch {

            searchRepositories(
                query = currentQuery
            )
        }
    }


    fun refreshRepositories() {

        if (isLoadingMore) {
            return
        }

        val currentState =
            _uiState.value as? UiState.Success
                ?: return

        val query =
            currentQuery.trim()

        if (query.isBlank()) {
            return
        }

        viewModelScope.launch {

            _uiState.value =
                currentState.copy(
                    isRefreshing = true
                )

            when (
                val result =
                    repository.searchRepositories(
                        query = query,
                        page = 1,
                        perPage = PAGE_SIZE
                    )
            ) {

                is Result.Success -> {

                    currentPage = 1

                    hasMorePages =
                        result.data.size == PAGE_SIZE

                    _uiState.value =
                        UiState.Success(
                            repositories = result.data,
                            isLoadingMore = false,
                            paginationError = null,
                            isRefreshing = false
                        )
                }

                is Result.Error -> {

                    _uiState.value =
                        currentState.copy(
                            isRefreshing = false,
                            paginationError = result.message
                        )
                }
            }
        }
    }
}