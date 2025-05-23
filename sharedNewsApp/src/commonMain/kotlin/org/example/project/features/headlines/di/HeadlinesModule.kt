package org.example.project.features.headlines.di

import org.example.project.features.headlines.data.FakeHeadlinesRepository
import org.example.project.features.headlines.data.HeadlinesRepository
import org.example.project.features.headlines.domain.GetAllHeadlines
import org.example.project.features.headlines.presentation.HeadlinesViewModel
import org.koin.dsl.module

val headlinesModule = module {
    // TODO the fake repo is ok only for now!
    factory<HeadlinesRepository> { FakeHeadlinesRepository() }
    factory { GetAllHeadlines(get()) }
    factory {
        HeadlinesViewModel(
            getAllHeadlines = get()
        )
    }
}