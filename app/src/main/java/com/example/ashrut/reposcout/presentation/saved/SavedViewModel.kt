package com.example.ashrut.reposcout.presentation.saved


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ashrut.reposcout.domain.model.Repository
import com.example.ashrut.reposcout.domain.repository.GitHubRepository
import com.example.ashrut.reposcout.utils.Result
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

sealed interface SavedUiState {

    data object Loading : SavedUiState

    data class Success(
        val repositories: List<Repository>
    ) : SavedUiState

    data class Error(
        val message: String
    ) : SavedUiState
}

class SavedViewModel(
    private val repository: GitHubRepository
) : ViewModel() {

    private val _uiState =
        MutableStateFlow<SavedUiState>(
            SavedUiState.Loading
        )

    val uiState: StateFlow<SavedUiState> =
        _uiState.asStateFlow()

    init {
        observeSavedRepositories()
    }

    private fun observeSavedRepositories() {

        viewModelScope.launch {

            repository
                .getSavedRepositories()
                .catch { exception ->

                    _uiState.value =
                        SavedUiState.Error(
                            exception.message
                                ?: "Unable to load saved repositories."
                        )
                }
                .collect { repositories ->

                    _uiState.value =
                        SavedUiState.Success(
                            repositories = repositories
                        )
                }
        }
    }

    fun removeRepository(
        repository: Repository
    ) {

        viewModelScope.launch {

            when (
                val result =
                    this@SavedViewModel.repository
                        .removeRepository(repository)
            ) {

                is Result.Success -> {
                    // Room Flow automatically
                    // updates the SavedScreen.
                }

                is Result.Error -> {
                    // We'll handle this properly in UI later.
                }
            }
        }
    }
}