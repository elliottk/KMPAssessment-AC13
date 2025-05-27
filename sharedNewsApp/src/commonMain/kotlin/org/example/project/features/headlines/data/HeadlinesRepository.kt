package org.example.project.features.headlines.data

import org.example.project.features.headlines.data.model.HeadlinesResponseNetworkModel

interface HeadlinesRepository {
    suspend fun getAllHeadlines(): Result<HeadlinesResponseNetworkModel>
}
