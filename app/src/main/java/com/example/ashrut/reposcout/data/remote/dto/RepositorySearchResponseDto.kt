package com.example.ashrut.reposcout.data.remote.dto

data class RepositorySearchResponseDto(
    val total_count: Int,
    val incomplete_results: Boolean,
    val items: List<RepositoryDto>
)