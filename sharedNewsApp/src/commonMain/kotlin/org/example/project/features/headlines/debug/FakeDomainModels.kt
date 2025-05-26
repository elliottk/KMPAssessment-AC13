package org.example.project.features.headlines.debug

import kotlinx.datetime.*
import org.example.project.features.headlines.domain.model.Headline

fun fakeHeadline(
    id: Long = 1L,
    title: String = "Breaking News Headline",
    description: String = "A description of something important that just happened.",
    author: String = "Andrew Carmichael",
    imageUrl: String = "https://i.cbc.ca/1.7384058.1731634500!/cumulusImage/httpImage/image.jpg_gen/derivatives/16x9_620/taylor-swift-eras-day-1.jpg",
    publishedDate: LocalDate = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
): Headline {
    return Headline(
        id = id,
        title = title,
        description = description,
        author = author,
        imageUrl = imageUrl,
        publishedDate = publishedDate
    )
}

fun fakeHeadlineList(count: Int = 10): List<Headline> {
    return List(count) { index ->
        fakeHeadline(
            id = index.toLong(),
            title = "Headline #$index",
            author = "Author $index"
        )
    }
}
