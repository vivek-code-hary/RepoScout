package com.example.ashrut.reposcout.domain.repository

import com.example.ashrut.reposcout.domain.model.Repository
import com.example.ashrut.reposcout.utils.Result

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
}