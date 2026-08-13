package com.example.ashrut.reposcout.utils

import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

fun formatGitHubDate(date: String): String {
    return try {
        val instant = Instant.parse(date)

        val formatter = DateTimeFormatter.ofPattern(
            "dd MMM yyyy",
            Locale.getDefault()
        )

        instant
            .atZone(ZoneId.systemDefault())
            .format(formatter)

    } catch (e: Exception) {
        date
    }
}