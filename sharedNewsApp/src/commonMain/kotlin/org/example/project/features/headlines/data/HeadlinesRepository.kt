package org.example.project.features.headlines.data

import org.example.project.features.headlines.data.model.HeadlineDto

interface HeadlinesRepository {
    suspend fun getAllHeadlines(): Result<List<HeadlineDto>>
}
