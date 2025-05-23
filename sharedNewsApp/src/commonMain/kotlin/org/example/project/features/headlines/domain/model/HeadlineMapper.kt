package org.example.project.features.headlines.domain.model

import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.example.project.features.headlines.data.model.HeadlineDto

fun HeadlineDto.toDomain(): Headline {
    val publishedDate = Instant.fromEpochMilliseconds(publishedAtUnix)
        .toLocalDateTime(TimeZone.currentSystemDefault()).date
    return Headline(
        id = id,
        title = title,
        description = description,
        author = author,
        imageUrl = media.imageUrl,
        publishedDate = publishedDate,
    )
}
