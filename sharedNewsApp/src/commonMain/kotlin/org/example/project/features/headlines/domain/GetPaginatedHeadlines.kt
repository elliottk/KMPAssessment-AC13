package org.example.project.features.headlines.domain

import io.ktor.utils.io.CancellationException
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import org.example.project.features.headlines.data.HeadlinesRepository
import org.example.project.features.headlines.domain.model.Headline
import org.example.project.features.headlines.domain.model.toDomain

/**
 * A use-case class responsible for paginating a list of headlines loaded from a [HeadlinesRepository].
 *
 * This class caches all headlines retrieved from the repository and exposes a paged subset through [pages],
 * emitting one page at a time when [loadNextPage] is called. It uses a mutex to protect state and
 * prevent race conditions from concurrent calls.
 *
 * Optionally, an [artificialDelay] can be introduced to simulate loading behavior during development or UI previews.
 *
 * @property headlinesRepository The data source providing all headline items.
 * @property pageSize The number of items to load per page.
 * @property artificialDelay An optional delay inserted before updating state (useful for simulating network latency).
 */
class GetPaginatedHeadlines(
    private val headlinesRepository: HeadlinesRepository,
    private val pageSize: Int = 10,
    private val artificialDelay: Duration? = 1.seconds,
) {
    private val mutex = Mutex()

    private var cachedHeadlines: List<Headline>? = null

    private val _pages = MutableStateFlow<Data>(Data())
    val pages = _pages.asStateFlow()

    private val _errors = MutableSharedFlow<Throwable>(replay = 1)
    val errors = _errors.asSharedFlow()

    suspend fun loadNextPage() {
        mutex.withLock {
            try {
                // being called for the first time, load from network and cache
                if (cachedHeadlines == null) {
                    headlinesRepository.getAllHeadlines().fold(
                        onSuccess = { networkModels ->
                            cachedHeadlines = networkModels.data.map { it.toDomain() }
                            if (cachedHeadlines.isNullOrEmpty()) {
                                _pages.update {
                                    it.copy(headlines = emptyList())
                                }
                                return
                            }
                        },
                        onFailure = {
                            _errors.emit(it)
                            return
                        },
                    )
                }

                if (_pages.value.headlines.isNotEmpty()) {
                    _pages.update {
                        it.copy(isLoadingMore = true)
                    }
                    artificialDelay?.let { delay(it) }
                }

                val currentData = _pages.value
                val allHeadlines = cachedHeadlines.orEmpty()
                val fromIndex = currentData.currentPage * pageSize
                if (fromIndex >= allHeadlines.size) return
                val toIndex = (fromIndex + pageSize).coerceAtMost(allHeadlines.size)
                val nextPage = allHeadlines.subList(fromIndex, toIndex)

                _pages.update {
                    Data(
                        headlines = it.headlines + nextPage,
                        isLoadingMore = false,
                        currentPage = it.currentPage + 1,
                        lastPage = (allHeadlines.size + pageSize - 1) / pageSize,
                    )
                }
            }
            catch (t: CancellationException) {
                throw t
            }
            catch (t: Throwable) {
                _errors.emit(t)
            }
            finally {
                if (_pages.value.isLoadingMore) {
                    _pages.update { it.copy(isLoadingMore = false) }
                }
            }
        }
    }

    suspend fun reset() {
        mutex.withLock {
            cachedHeadlines = null
            _pages.value = Data()
        }
    }

    data class Data(
        val headlines: List<Headline> = emptyList(),
        val isLoadingMore: Boolean = false,
        val currentPage: Int = 0,
        val lastPage: Int = 0,
    ) {
        val isLastPage: Boolean get() = currentPage >= lastPage
    }
}