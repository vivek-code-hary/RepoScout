package com.example.ashrut.reposcout.data.remote

import com.example.ashrut.reposcout.data.remote.dto.RepositoryDto
import com.example.ashrut.reposcout.domain.model.Repository

fun RepositoryDto.toDomain() : Repository{
    return Repository(
        id = id,
        name = name,
        fullName = full_name,
        ownerName = owner.login,
        avatarUrl = owner.avatar_url,
        description = description,
        htmlUrl = html_url,
        stars = stargazers_count,
        watchers = watchers_count,
        forks = forks_count,
        openIssues = open_issues_count,
        language = language,
        createdAt = created_at,
        updatedAt = updated_at,
        license = license?.name
    )
}