package com.example.ashrut.reposcout.data.repository

import com.example.ashrut.reposcout.data.local.dao.RepositoryDao
import com.example.ashrut.reposcout.data.local.mapper.toDomain
import com.example.ashrut.reposcout.data.local.mapper.toEntity
import com.example.ashrut.reposcout.utils.Result
import com.example.ashrut.reposcout.data.remote.api.GitHubApi
import com.example.ashrut.reposcout.data.remote.dto.RepositorySearchResponseDto
import com.example.ashrut.reposcout.data.remote.toDomain
import com.example.ashrut.reposcout.domain.model.Repository
import com.example.ashrut.reposcout.domain.repository.GitHubRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import okio.IOException
import retrofit2.HttpException


class GitHubRepositoryImpl(
    private val api: GitHubApi,
    private val repositoryDao: RepositoryDao
) : GitHubRepository {

    override suspend fun searchRepositories(
        query: String,
        page: Int,
        perPage: Int
    ): Result<List<Repository>> {

        return try {

            val response = api.searchRepositories(
                query = query,
                page = page,
                perPage = perPage
            )

            val repositories = response.items.map {
                it.toDomain()
            }

            Result.Success(repositories)

        } catch (e: IOException) {

            Result.Error(
                "No internet connection. Please check your network."
            )

        } catch (e: HttpException) {

            when (e.code()) {

                403 -> Result.Error(
                    "GitHub API rate limit reached. Please try again later."
                )

                404 -> Result.Error(
                    "Requested resource was not found."
                )

                422 -> Result.Error(
                    "Invalid search request."
                )

                500, 502, 503, 504 -> Result.Error(
                    "GitHub server is temporarily unavailable."
                )

                else -> Result.Error(
                    "Something went wrong. Error code: ${e.code()}"
                )
            }

        } catch (e: Exception) {

            Result.Error(
                e.message ?: "An unexpected error occurred."
            )
        }
    }

    override suspend fun getRepositoryDetails(
        owner: String,
        repo: String
    ): Result<Repository> {

        return try {

            val response = api.getRepositoryDetails(
                owner = owner,
                repo = repo
            )

            Result.Success(
                response.toDomain()
            )

        } catch (e: IOException) {

            Result.Error(
                "No internet connection. Please check your network."
            )

        } catch (e: HttpException) {

            when (e.code()) {

                403 -> Result.Error(
                    "GitHub API rate limit reached. Please try again later."
                )

                404 -> Result.Error(
                    "Repository not found."
                )

                500, 502, 503, 504 -> Result.Error(
                    "GitHub server is temporarily unavailable."
                )

                else -> Result.Error(
                    "Something went wrong. Error code: ${e.code()}"
                )
            }

        } catch (e: Exception) {

            Result.Error(
                e.message ?: "An unexpected error occurred."
            )
        }
    }

    override suspend fun saveRepository(
        repository: Repository
    ): Result<Unit> {

        return try {

            repositoryDao.saveRepository(
                repository.toEntity()
            )

            Result.Success(Unit)

        } catch (e: Exception) {

            Result.Error(
                e.message ?: "Unable to save repository."
            )
        }
    }

    override suspend fun removeRepository(
        repository: Repository
    ): Result<Unit> {

        return try {

            repositoryDao.deleteRepository(
                repository.toEntity()
            )

            Result.Success(Unit)

        } catch (e: Exception) {

            Result.Error(
                e.message ?: "Unable to remove repository."
            )
        }
    }

    override fun getSavedRepositories(): Flow<List<Repository>> {

        return repositoryDao
            .getSavedRepositories()
            .map { entities ->

                entities.map { entity ->
                    entity.toDomain()
                }
            }
    }

    override suspend fun isRepositorySaved(id: Long): Boolean {
        return repositoryDao.isRepositorySaved(id)
    }

    override suspend fun getSavedRepository(
        id: Long
    ): Repository? {

        return repositoryDao
            .getSavedRepository(id)
            ?.toDomain()
    }

}