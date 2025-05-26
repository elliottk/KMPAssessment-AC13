package org.example.project.features.headlines.data

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.http.isSuccess
import io.ktor.http.path
import io.ktor.serialization.JsonConvertException
import kotlinx.coroutines.CancellationException
import okio.IOException
import org.example.project.core.network.Configuration
import org.example.project.core.network.HttpException
import org.example.project.core.network.MalformedResponseException
import org.example.project.core.network.NetworkUnavailableException
import org.example.project.features.headlines.data.model.HeadlinesResponseNetworkModel

class HeadlinesNetworkRepository(
    private val ktorClient: HttpClient,
    private val configuration: Configuration,
) : HeadlinesRepository {

    override suspend fun getAllHeadlines(): Result<HeadlinesResponseNetworkModel> {
        return try {
            val httpResponse = ktorClient.get {
                url {
                    path(configuration.baseUrl, configuration.headlinesEndpoint)
                }
            }
            if (!httpResponse.status.isSuccess()) {
                return Result.failure(
                    HttpException(
                        statusCode = httpResponse.status.value,
                        message = httpResponse.status.toString(),
                    )
                )
            }
            Result.success(httpResponse.body())
        } catch (e: CancellationException) {
            throw e
        } catch (e: IOException) {
            Result.failure(NetworkUnavailableException(e.message.orEmpty(), e))
        } catch (e: JsonConvertException) {
            Result.failure(MalformedResponseException(e.message.orEmpty(), e))
        } catch (e: Throwable) {
            Result.failure(e) // fallback for anything unexpected
        }
    }
}