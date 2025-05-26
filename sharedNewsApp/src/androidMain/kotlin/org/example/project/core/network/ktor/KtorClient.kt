package org.example.project.core.network.ktor

import android.util.Log
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.http.URLProtocol
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.example.project.core.network.Configuration

actual fun constructKtorHttpClient(configuration: Configuration): HttpClient {
    return HttpClient(OkHttp) {
        expectSuccess = true
        install(Logging) {
            level = LogLevel.ALL
            logger = object : Logger {
                override fun log(message: String) {
                    Log.v("KtorClient", message)
                }
            }
        }
        install(ContentNegotiation) {
            json( Json {
                ignoreUnknownKeys = true
                isLenient = true
            })
        }
        defaultRequest {
            url {
                protocol = URLProtocol.HTTPS
                host = configuration.hostname
            }
        }
    }
}