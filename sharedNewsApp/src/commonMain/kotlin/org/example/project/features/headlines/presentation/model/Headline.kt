package org.example.project.features.headlines.presentation.model

data class Headline(
    val id: Long,
    val title: String,
    val description: String,
    val author: String,
    val imageUrl: String,
    val publishedDate: String,
)

