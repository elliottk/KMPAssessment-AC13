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
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.example.project.features.headlines.domain.GetAllHeadlines
import org.example.project.features.headlines.presentation.model.Headline
import org.example.project.features.headlines.presentation.model.toPresentation

// TODO getAllHeadlines needs to be injected etc
class HeadlinesViewModel(
    private val getAllHeadlines: GetAllHeadlines,
    dispatcher: CoroutineDispatcher = Dispatchers.Main,
) : HeadlinesIntentHandler {
    private val viewModelScope = CoroutineScope(SupervisorJob() + dispatcher)

    private val _uiState = MutableStateFlow<State>(State.Loading)
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            loadHeadlines()
        }
    }

    // TODO, error not handled well enough
    private suspend fun loadHeadlines() {
        _uiState.update {
            getAllHeadlines.invoke().fold(
                onSuccess = { headlines ->
                    State.Success(
                        headlines = headlines.map { it.toPresentation() }.toPersistentList(),
                        isRefreshing = false,
                    )
                },
                onFailure = { throwable ->
                    State.Error()
                },
            )
        }
    }

    fun clear() {
        viewModelScope.cancel()
    }

    override fun handleIntent(intent: HeadlinesIntent) {
        when (intent) {
            HeadlinesIntent.ReloadHeadlines -> reloadHeadlines()
        }
    }

    private fun reloadHeadlines() {
        viewModelScope.launch {
            _uiState.update { State.Loading }
            loadHeadlines()
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
            val isRefreshing: Boolean,
        ) : State
    }
}

fun interface HeadlinesIntentHandler {
    fun handleIntent(intent: HeadlinesIntent)
}

sealed interface HeadlinesIntent {
    data object ReloadHeadlines : HeadlinesIntent
}

@Composable
expect fun rememberHeadlinesViewModel(): HeadlinesViewModel