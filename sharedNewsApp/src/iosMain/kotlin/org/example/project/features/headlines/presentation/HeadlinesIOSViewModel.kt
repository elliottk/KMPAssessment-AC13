package org.example.project.features.headlines.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import org.example.project.features.headlines.FakeHeadlinesRepository
import org.example.project.features.headlines.domain.GetAllHeadlines

@Composable
actual fun rememberHeadlinesViewModel(): HeadlinesViewModel {
    return remember {
        HeadlinesViewModel(
            getAllHeadlines = GetAllHeadlines(headlinesRepository = FakeHeadlinesRepository())
        )
    }
}