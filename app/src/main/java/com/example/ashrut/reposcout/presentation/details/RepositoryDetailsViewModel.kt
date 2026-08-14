package com.example.ashrut.reposcout.presentation.details


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ashrut.reposcout.domain.model.Repository
import com.example.ashrut.reposcout.domain.repository.GitHubRepository
import com.example.ashrut.reposcout.utils.Result
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed interface DetailsUiState {

    data object Loading : DetailsUiState

    data class Success(
        val repository: Repository
    ) : DetailsUiState

    data class Error(
        val message: String
    ) : DetailsUiState
}

class RepositoryDetailsViewModel(
    private val repository: GitHubRepository
) : ViewModel() {

    private val _uiState =
        MutableStateFlow<DetailsUiState>(
            DetailsUiState.Loading
        )

    val uiState = _uiState.asStateFlow()

    private var currentOwner: String? = null
    private var currentRepo: String? = null

    fun loadRepository(
        owner: String,
        repo: String
    ) {

        // Rotation/recomposition ke baad same API dobara mat call karo
        if (
            _uiState.value is DetailsUiState.Success &&
            currentOwner == owner &&
            currentRepo == repo
        ) {
            return
        }

        currentOwner = owner
        currentRepo = repo

        viewModelScope.launch {

            _uiState.value =
                DetailsUiState.Loading

            when (
                val result =
                    repository.getRepositoryDetails(
                        owner = owner,
                        repo = repo
                    )
            ) {

                is Result.Success -> {

                    _uiState.value =
                        DetailsUiState.Success(
                            repository = result.data
                        )
                }

                is Result.Error -> {

                    _uiState.value =
                        DetailsUiState.Error(
                            message = result.message
                        )
                }
            }
        }
    }

    fun retry() {

        val owner = currentOwner
        val repo = currentRepo

        if (owner == null || repo == null) {
            return
        }

        // Error ko Loading mein change karke request retry
        _uiState.value = DetailsUiState.Loading

        viewModelScope.launch {

            when (
                val result =
                    repository.getRepositoryDetails(
                        owner = owner,
                        repo = repo
                    )
            ) {

                is Result.Success -> {

                    _uiState.value =
                        DetailsUiState.Success(
                            repository = result.data
                        )
                }

                is Result.Error -> {

                    _uiState.value =
                        DetailsUiState.Error(
                            message = result.message
                        )
                }
            }
        }
    }
}