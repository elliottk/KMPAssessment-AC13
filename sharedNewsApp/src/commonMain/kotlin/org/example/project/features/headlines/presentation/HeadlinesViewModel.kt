package org.example.project.features.headlines.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.CoroutineDispatcher
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
import org.example.project.features.headlines.domain.GetPaginatedHeadlines
import org.example.project.features.headlines.presentation.model.Headline
import org.example.project.features.headlines.presentation.model.toPresentation

class HeadlinesViewModel(
    private val getPaginatedHeadlines: GetPaginatedHeadlines,
    dispatcher: CoroutineDispatcher = Dispatchers.Main,
) : HeadlinesIntentHandler {

    private val viewModelScope = CoroutineScope(SupervisorJob() + dispatcher)

    private val _uiState = MutableStateFlow<State>(State.Loading)
    val uiState = _uiState.asStateFlow()

    init {
        observePaginatedHeadlines()
        viewModelScope.launch {
            getPaginatedHeadlines.loadNextPage()
        }
    }

    private fun observePaginatedHeadlines() {
        viewModelScope.launch {
            launch {
                getPaginatedHeadlines.errors.collectLatest { throwable ->
                    _uiState.update {
                        // TODO error string
                        State.Error()
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
            getPaginatedHeadlines.reload()
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
            val errorMessage: String? = null,
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