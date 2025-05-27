package org.example.project.features.headlines.data

import org.example.project.features.headlines.data.model.HeadlinesResponseNetworkModel

fun interface HeadlinesRepository {
    suspend fun getAllHeadlines(): Result<HeadlinesResponseNetworkModel>
}
