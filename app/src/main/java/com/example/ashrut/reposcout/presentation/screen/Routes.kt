package com.example.ashrut.reposcout.presentation.screen

sealed class Routes(
    val route: String
) {

    data object Explore : Routes("explore")

    data object Saved : Routes("saved")

    data object Details : Routes(
        "details/{owner}/{repo}"
    ) {
        fun createRoute(
            owner: String,
            repo: String
        ): String {
            return "details/$owner/$repo"
        }
    }
}