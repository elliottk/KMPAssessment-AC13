package org.example.project.core.network.ktor

import io.ktor.client.HttpClient
import org.example.project.core.network.Configuration

expect fun constructKtorHttpClient(configuration: Configuration): HttpClient