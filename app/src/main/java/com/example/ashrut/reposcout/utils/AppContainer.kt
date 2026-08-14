package com.example.ashrut.reposcout.utils

import android.content.Context
import com.example.ashrut.reposcout.data.local.database.RepoScoutDatabase
import com.example.ashrut.reposcout.data.remote.api.RetrofitClient
import com.example.ashrut.reposcout.data.repository.GitHubRepositoryImpl
import com.example.ashrut.reposcout.domain.repository.GitHubRepository

object AppContainer {

    private lateinit var database: RepoScoutDatabase

    lateinit var gitHubRepository: GitHubRepository
        private set

    fun initialize(context: Context) {

        database = RepoScoutDatabase.getInstance(
            context.applicationContext
        )

        gitHubRepository =
            GitHubRepositoryImpl(
                api = RetrofitClient.api,
                repositoryDao = database.repositoryDao()
            )
    }
}