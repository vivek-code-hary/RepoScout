package com.example.ashrut.reposcout.data.repository

import com.example.ashrut.reposcout.utils.Result
import com.example.ashrut.reposcout.data.remote.api.GitHubApi
import com.example.ashrut.reposcout.data.remote.dto.RepositorySearchResponseDto
import com.example.ashrut.reposcout.data.remote.toDomain
import com.example.ashrut.reposcout.domain.model.Repository
import com.example.ashrut.reposcout.domain.repository.GitHubRepository
import okio.IOException
import retrofit2.HttpException


class GitHubRepositoryImpl(
    private val api: GitHubApi
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
}