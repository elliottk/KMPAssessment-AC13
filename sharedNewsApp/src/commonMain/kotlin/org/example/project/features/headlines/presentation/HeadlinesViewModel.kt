package org.example.project.features.headlines.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import newsapp.sharednewsapp.generated.resources.Res
import newsapp.sharednewsapp.generated.resources.errorNetworkUnavailable
import org.example.project.core.network.NetworkUnavailableException
import org.example.project.features.headlines.domain.GetPaginatedHeadlines
import org.example.project.features.headlines.presentation.model.Headline
import org.example.project.features.headlines.presentation.model.toPresentation
import org.jetbrains.compose.resources.StringResource

class HeadlinesViewModel(
    private val getPaginatedHeadlines: GetPaginatedHeadlines,
    private val viewModelScope: CoroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Main),
) : HeadlinesIntentHandler {

    private val _uiState = MutableStateFlow<State>(State.Loading)
    val uiState = _uiState.asStateFlow()

    init {
        observePaginatedHeadlines()
        viewModelScope.launch {
            getPaginatedHeadlines.loadNextPage()
        }
    }

    // Observe both page and error emissions from getPaginatedHeadlines
    private fun observePaginatedHeadlines() {
        viewModelScope.launch {
            launch {
                getPaginatedHeadlines.errors.collectLatest { throwable ->
                    _uiState.update {
                        State.Error(
                            errorMessageResource = when(throwable) {
                                is NetworkUnavailableException -> Res.string.errorNetworkUnavailable
                                else -> null
                            }
                        )
                    }
                }
            }
            launch {
                getPaginatedHeadlines.pages
                    .filter { it.headlines.isNotEmpty() }
                    .collectLatest { data ->
                    _uiState.update {
                        State.Success(
                            headlines = data.headlines.map { it.toPresentation() }.toPersistentList(),
                            hasMore = !data.isLastPage,
                            isLoadingMore = data.isLoadingMore,
                        )
                    }
                }
            }
        }
    }

    // Android only, manage lifecycle of coroutine scope
    fun clear() {
        viewModelScope.cancel()
    }

    override fun handleIntent(intent: HeadlinesIntent) {
        when (intent) {
            HeadlinesIntent.ReloadHeadlines -> reloadHeadlines()
            HeadlinesIntent.LoadMoreHeadlines -> loadMoreHeadlines()
        }
    }

    private fun reloadHeadlines() {
        viewModelScope.launch {
            _uiState.update { State.Loading }
            getPaginatedHeadlines.reset()
            getPaginatedHeadlines.loadNextPage()
        }
    }

    private fun loadMoreHeadlines() {
        viewModelScope.launch {
            (_uiState.value as? State.Success) ?: return@launch
            getPaginatedHeadlines.loadNextPage()
        }
    }

    @Immutable
    sealed interface State {
        data object Loading : State

        data class Error(
            val errorMessageResource: StringResource? = null,
        ) : State

        data class Success(
            val headlines: ImmutableList<Headline>,
            val hasMore: Boolean,
            val isLoadingMore: Boolean,
        ) : State
    }
}

fun interface HeadlinesIntentHandler {
    fun handleIntent(intent: HeadlinesIntent)
}

sealed interface HeadlinesIntent {
    data object ReloadHeadlines : HeadlinesIntent
    data object LoadMoreHeadlines : HeadlinesIntent
}

@Composable
expect fun rememberHeadlinesViewModel(): HeadlinesViewModel