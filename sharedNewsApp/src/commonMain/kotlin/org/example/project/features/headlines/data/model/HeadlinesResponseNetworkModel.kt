package org.example.project.features.headlines.data.model

import kotlinx.serialization.Serializable

@Serializable
data class HeadlinesResponseNetworkModel(
    val status: String,
    val data: List<HeadlineDto>,
)

@Serializable
data class HeadlineDto(
    val id: Long,
    val title: String,
    val description: String,
    val author: String,
    val source: String,
    val isLocal: Boolean,
    val publishedAtUnix: Long,
    val version: String,
    val media: MediaDto
)

@Serializable
data class MediaDto(
    val imageUrl: String
)