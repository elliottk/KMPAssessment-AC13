package org.example.project.features.headlines.data

import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.toLocalDateTime
import org.example.project.features.headlines.data.model.HeadlineDto
import org.example.project.features.headlines.data.model.MediaDto

class FakeHeadlinesRepository : HeadlinesRepository {
    override suspend fun getAllHeadlines(): Result<List<HeadlineDto>> {
        val nowEpoch =
            Clock.System.now().toLocalDateTime(TimeZone.Companion.currentSystemDefault()).date.atStartOfDayIn(
                TimeZone.Companion.currentSystemDefault()
            ).toEpochMilliseconds()
        return Result.success(
            List(50) { index ->
                HeadlineDto(
                    id = index.toLong(),
                    title = "Fake Headline #$index",
                    description = "This is a fake description for headline #$index.",
                    author = "Author $index",
                    source = "FakeSource",
                    isLocal = index % 2 == 0,
                    publishedAtUnix = nowEpoch - (index * 86_400_000), // subtract days
                    version = "1.0",
                    media = MediaDto("https://placekitten.com/200/200?image=$index")
                )
            }
        )
    }
}