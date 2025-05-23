package org.example.project.features.headlines.presentation

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
) {
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
                    State.Error
                },
            )
        }
    }

    fun clear() {
        viewModelScope.cancel()
    }

    @Immutable
    sealed interface State {
        data object Loading : State

        data object Error : State

        data class Success(
            val headlines: ImmutableList<Headline>,
            val isRefreshing: Boolean,
        ) : State
    }
}