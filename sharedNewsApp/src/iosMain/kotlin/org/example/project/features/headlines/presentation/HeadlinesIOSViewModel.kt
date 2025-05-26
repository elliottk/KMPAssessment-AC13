package org.example.project.features.headlines.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import org.example.project.core.network.ktor.constructKtorHttpClient
import org.example.project.core.network.networkConfiguration
import org.example.project.features.headlines.data.FakeHeadlinesRepository
import org.example.project.features.headlines.data.HeadlinesNetworkRepository
import org.example.project.features.headlines.domain.GetAllHeadlines

@Composable
actual fun rememberHeadlinesViewModel(): HeadlinesViewModel {
    return remember {
//        HeadlinesViewModel(
//            getAllHeadlines = GetAllHeadlines(headlinesRepository = FakeHeadlinesRepository())
//        )

        // TODO it would be nice to use koin for this...
        HeadlinesViewModel(
            getAllHeadlines = GetAllHeadlines(
                headlinesRepository = HeadlinesNetworkRepository(
                    ktorClient = constructKtorHttpClient(
                        configuration = networkConfiguration
                    ),
                    configuration = networkConfiguration
                )
            )
        )
    }
}