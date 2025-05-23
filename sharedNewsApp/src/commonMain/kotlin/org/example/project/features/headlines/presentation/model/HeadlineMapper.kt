package org.example.project.features.headlines.presentation.model

import org.example.project.features.headlines.domain.model.Headline as DomainHeadline

fun DomainHeadline.toPresentation(): Headline {
    return Headline(
        id = id,
        title = title,
        description = description,
        author = author,
        imageUrl = imageUrl,
        // TODO
        publishedDate = ""
    )
}