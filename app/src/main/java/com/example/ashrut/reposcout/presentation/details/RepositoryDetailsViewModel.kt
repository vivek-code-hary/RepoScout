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
        val repository: Repository,
        val isSaved: Boolean = false,
        val isSaving: Boolean = false
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
    private var currentRepositoryId: Long? = null

    fun loadRepository(
        owner: String,
        repo: String,
        repositoryId: Long
    ) {

        if (
            _uiState.value is DetailsUiState.Success &&
            currentOwner == owner &&
            currentRepo == repo &&
            currentRepositoryId == repositoryId
        ) {
            return
        }

        currentOwner = owner
        currentRepo = repo
        currentRepositoryId = repositoryId

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

                    val isSaved =
                        repository.isRepositorySaved(
                            result.data.id
                        )

                    _uiState.value =
                        DetailsUiState.Success(
                            repository = result.data,
                            isSaved = isSaved
                        )
                }

                is Result.Error -> {



                    val savedRepository =
                        repository.getSavedRepository(
                            repositoryId
                        )

                    if (savedRepository != null) {

                        _uiState.value =
                            DetailsUiState.Success(
                                repository = savedRepository,
                                isSaved = true
                            )

                    } else {

                        _uiState.value =
                            DetailsUiState.Error(
                                result.message
                            )
                    }
                }
            }
        }
    }

    fun toggleSaved() {

        val currentState =
            _uiState.value as? DetailsUiState.Success
                ?: return

        if (currentState.isSaving) {
            return
        }

        viewModelScope.launch {

            _uiState.value =
                currentState.copy(
                    isSaving = true
                )

            val result =
                if (currentState.isSaved) {

                    repository.removeRepository(
                        currentState.repository
                    )

                } else {

                    repository.saveRepository(
                        currentState.repository
                    )
                }

            when (result) {

                is Result.Success -> {

                    _uiState.value =
                        currentState.copy(
                            isSaved =
                                !currentState.isSaved,
                            isSaving = false
                        )
                }

                is Result.Error -> {

                    _uiState.value =
                        currentState.copy(
                            isSaving = false
                        )
                }
            }
        }
    }

    fun retry() {

        val owner =
            currentOwner ?: return

        val repo =
            currentRepo ?: return

        val repositoryId =
            currentRepositoryId ?: return


        loadRepository(
            owner = owner,
            repo = repo,
            repositoryId = repositoryId
        )
    }
}