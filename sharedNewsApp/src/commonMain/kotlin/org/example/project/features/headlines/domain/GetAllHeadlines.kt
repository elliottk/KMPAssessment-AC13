package org.example.project.features.headlines.domain

import org.example.project.features.headlines.data.HeadlinesRepository
import org.example.project.features.headlines.domain.model.Headline
import org.example.project.features.headlines.domain.model.toDomain

// TODO do something better with the errors
class GetAllHeadlines(
    private val headlinesRepository: HeadlinesRepository,
) {
    suspend operator fun invoke(): Result<List<Headline>> {
        return headlinesRepository.getAllHeadlines().mapCatching { headlineDtos ->
            headlineDtos.map { it.toDomain() }
        }
    }
}