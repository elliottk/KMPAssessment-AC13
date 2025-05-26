package org.example.project.core.network.di

import org.example.project.core.network.Configuration
import org.example.project.core.network.ktor.constructKtorHttpClient
import org.example.project.core.network.networkConfiguration
import org.koin.dsl.module

val networkModule = module {
    single<Configuration> { networkConfiguration }
    single { constructKtorHttpClient(configuration = get()) }
}
