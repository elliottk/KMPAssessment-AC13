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

/**
 * A network-backed implementation of [HeadlinesRepository] that retrieves headline data using a Ktor HTTP client.
 *
 * This class handles network requests to a configurable endpoint and safely wraps all responses in [Result].
 * Specific exceptions are caught and mapped to domain-specific exceptions to allow better error handling in consumers.
 *
 * Cancellation exceptions are thrown and not wrapped in the returned Result.
 *
 * @property ktorClient The [HttpClient] used for making network requests.
 * @property configuration The [Configuration] containing endpoint paths and base URL information.
 */
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
            val result: Result<HeadlinesResponseNetworkModel> = if (!httpResponse.status.isSuccess()) {
                Result.failure(
                    HttpException(
                        statusCode = httpResponse.status.value,
                        message = httpResponse.status.toString(),
                    )
                )
            } else {
                Result.success(httpResponse.body())
            }
            result
        } catch (e: CancellationException) {
            throw e
        } catch (e: IOException) {
            Result.failure(NetworkUnavailableException(e.message.orEmpty(), e))
        } catch (e: JsonConvertException) {
            Result.failure(MalformedResponseException(e.message.orEmpty(), e))
        } catch (e: Throwable) {
            println("Unexpected error while fetching headlines: ${e.message}")
            Result.failure(e)
        }
    }
}