package org.example.project.features.headlines.di

import org.example.project.features.headlines.data.HeadlinesNetworkRepository
import org.example.project.features.headlines.data.HeadlinesRepository
import org.example.project.features.headlines.domain.GetAllHeadlines
import org.example.project.features.headlines.domain.GetPaginatedHeadlines
import org.example.project.features.headlines.presentation.HeadlinesViewModel
import org.koin.dsl.module

val headlinesModule = module {
    // This can be swapped between FakeHeadlinesRepository and HeadlinesNetworkRepository
    factory<HeadlinesRepository> { HeadlinesNetworkRepository(ktorClient = get(), configuration = get()) }
    factory { GetAllHeadlines(get()) }
    factory { GetPaginatedHeadlines(get()) }
    factory {
        HeadlinesViewModel(
            getPaginatedHeadlines = get(),
        )
    }
}