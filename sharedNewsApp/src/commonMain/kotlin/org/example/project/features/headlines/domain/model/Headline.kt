package org.example.project.features.headlines.domain.model

import kotlinx.datetime.LocalDate

data class Headline(
    val id: Long,
    val title: String,
    val description: String,
    val author: String,
    val imageUrl: String,
    val publishedDate: LocalDate,
)
