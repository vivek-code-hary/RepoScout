package com.example.ashrut.reposcout.data.remote.dto

data class RepositoryDto(
    val id: Long,
    val name: String,
    val full_name: String,
    val owner: OwnerDto,
    val html_url: String,
    val description: String?,
    val stargazers_count: Int,
    val watchers_count: Int,
    val forks_count: Int,
    val open_issues_count: Int,
    val language: String?,
    val created_at: String,
    val updated_at: String,
    val license: LicenseDto?
)