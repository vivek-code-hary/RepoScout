package com.example.ashrut.reposcout.domain.repository

import com.example.ashrut.reposcout.domain.model.Repository
import com.example.ashrut.reposcout.utils.Result
import kotlinx.coroutines.flow.Flow

interface GitHubRepository {

    suspend fun searchRepositories(
        query: String,
        page: Int,
        perPage: Int
    ): Result<List<Repository>>

    suspend fun getRepositoryDetails(
        owner: String,
        repo: String
    ): Result<Repository>

    suspend fun saveRepository(
        repository: Repository
    ): Result<Unit>

    suspend fun removeRepository(
        repository: Repository
    ): Result<Unit>

    fun getSavedRepositories(): Flow<List<Repository>>

    suspend fun isRepositorySaved(
        id: Long
    ): Boolean

    suspend fun getSavedRepository(
        id: Long
    ): Repository?
}