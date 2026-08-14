package com.example.ashrut.reposcout.data.local.mapper


import com.example.ashrut.reposcout.data.local.entity.RepositoryEntity
import com.example.ashrut.reposcout.domain.model.Repository

fun Repository.toEntity(): RepositoryEntity {
    return RepositoryEntity(
        id = id,
        name = name,
        fullName = fullName,
        ownerName = ownerName,
        avatarUrl = avatarUrl,
        description = description,
        htmlUrl = htmlUrl,
        stars = stars,
        watchers = watchers,
        forks = forks,
        openIssues = openIssues,
        language = language,
        createdAt = createdAt,
        updatedAt = updatedAt,
        license = license
    )
}

fun RepositoryEntity.toDomain(): Repository {
    return Repository(
        id = id,
        name = name,
        fullName = fullName,
        ownerName = ownerName,
        avatarUrl = avatarUrl,
        description = description,
        htmlUrl = htmlUrl,
        stars = stars,
        watchers = watchers,
        forks = forks,
        openIssues = openIssues,
        language = language,
        createdAt = createdAt,
        updatedAt = updatedAt,
        license = license
    )
}