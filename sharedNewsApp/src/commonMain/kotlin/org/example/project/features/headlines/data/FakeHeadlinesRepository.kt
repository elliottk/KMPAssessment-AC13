package org.example.project.features.headlines.data

import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.toLocalDateTime
import org.example.project.features.headlines.data.model.HeadlineDto
import org.example.project.features.headlines.data.model.HeadlinesResponseNetworkModel
import org.example.project.features.headlines.data.model.MediaDto

class FakeHeadlinesRepository : HeadlinesRepository {
    override suspend fun getAllHeadlines(): Result<HeadlinesResponseNetworkModel> {
        val nowEpoch =
            Clock.System.now()
                .toLocalDateTime(TimeZone.Companion.currentSystemDefault()).date.atStartOfDayIn(
                TimeZone.Companion.currentSystemDefault()
            ).toEpochMilliseconds()
        return Result.success(
            HeadlinesResponseNetworkModel(
                status = "success",
                data = List(50) { index ->
                    HeadlineDto(
                        id = index.toLong(),
                        title = "Fake Headline #$index",
                        description = "This is a fake description for headline #$index.",
                        author = "Author $index",
                        source = "FakeSource",
                        isLocal = index % 2 == 0,
                        publishedAtUnix = nowEpoch - (index * 86_400_000), // subtract days
                        version = "1.0",
                        media = MediaDto("https://i.cbc.ca/1.7384058.1731634500!/cumulusImage/httpImage/image.jpg_gen/derivatives/16x9_620/taylor-swift-eras-day-1.jpg")
                    )
                }
            )
        )
    }
}