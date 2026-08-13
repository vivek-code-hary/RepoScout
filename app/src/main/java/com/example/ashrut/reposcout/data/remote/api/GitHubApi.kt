package com.example.ashrut.reposcout.data.remote.api

import com.example.ashrut.reposcout.data.remote.dto.RepositorySearchResponseDto
import retrofit2.http.GET
import retrofit2.http.Query

interface GitHubApi {
    @GET("search/repositories")
    suspend fun searchRepositories(
        @Query("q") query: String,
        @Query("page") page: Int,
        @Query("per_page") perPage: Int
    ): RepositorySearchResponseDto

}