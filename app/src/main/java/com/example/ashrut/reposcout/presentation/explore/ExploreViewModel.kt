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
) : ViewModel(){

    private val _uiState = MutableStateFlow<UiState>(UiState.Loading)
    val uiState = _uiState.asStateFlow()

    fun loadRepositories(){
        viewModelScope.launch {
            _uiState.value = UiState.Loading

            when(
                val result = repository.searchRepositories(
                    query = "android",
                    page = 1,
                    perPage = 20
                )
                ){
                is Result.Success -> {
                    _uiState.value = UiState.Success(result.data)
                }
                is Result.Error -> {
                    _uiState.value = UiState.Error(result.message)
                }
            }

        }
    }


}