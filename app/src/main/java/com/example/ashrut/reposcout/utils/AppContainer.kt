package com.example.ashrut.reposcout.utils

import com.example.ashrut.reposcout.data.remote.api.RetrofitClient
import com.example.ashrut.reposcout.data.repository.GitHubRepositoryImpl
import com.example.ashrut.reposcout.domain.repository.GitHubRepository

object AppContainer {

    val gitHubRepository: GitHubRepository by lazy {
        GitHubRepositoryImpl(
            api = RetrofitClient.api
        )
    }
}