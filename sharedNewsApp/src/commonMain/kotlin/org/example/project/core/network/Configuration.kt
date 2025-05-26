package org.example.project.core.network

import org.example.project.BuildKonfig

data class Configuration(
    val hostname: String,
    val baseUrl: String,
    val headlinesEndpoint: String,
)

internal val networkConfiguration by lazy {
    Configuration(
        hostname = BuildKonfig.HOSTNAME,
        baseUrl = BuildKonfig.BASE_URL,
        headlinesEndpoint = BuildKonfig.HEADLINES_ENDPOINT,
    )
}
