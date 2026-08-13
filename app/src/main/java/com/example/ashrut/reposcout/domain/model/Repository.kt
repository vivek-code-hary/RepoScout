package com.example.ashrut.reposcout.domain.model

data class Repository (
    val id: Long,
    val name: String,
    val fullName: String,
    val ownerName: String,
    val avatarUrl: String,
    val description: String?,
    val htmlUrl: String,
    val stars: Int,
    val watchers: Int,
    val forks: Int,
    val openIssues: Int,
    val language: String?,
    val createdAt: String,
    val updatedAt: String,
    val license: String?
)
