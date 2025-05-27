package org.example.project.features.headlines

import app.cash.turbine.test
import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isInstanceOf
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.example.project.features.headlines.data.FakeHeadlinesRepository
import org.example.project.features.headlines.data.HeadlinesRepository
import org.example.project.features.headlines.data.model.HeadlinesResponseNetworkModel
import org.example.project.features.headlines.domain.GetPaginatedHeadlines
import org.example.project.features.headlines.presentation.HeadlinesIntent
import org.example.project.features.headlines.presentation.HeadlinesViewModel

@OptIn(ExperimentalCoroutinesApi::class)
class HeadlinesViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private val testScope = CoroutineScope(SupervisorJob() + testDispatcher)

    @BeforeTest
    fun setup() {
        Dispatchers.setMain(testDispatcher)
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial state is Loading then Success`() = runTest {
        viewModel(
            viewModelScope = testScope
        ).uiState.test {
            assertThat(awaitItem()).isEqualTo(HeadlinesViewModel.State.Loading)
            testDispatcher.scheduler.advanceUntilIdle()
            assertThat(awaitItem()).isInstanceOf<HeadlinesViewModel.State.Success>()
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `empty state emitted when no headlines present`() = runTest {
        val emptyResponse = HeadlinesResponseNetworkModel(
            status = "success",
            data = emptyList(),
        )
        viewModel(
            headlinesRepository = { Result.success(emptyResponse) },
            viewModelScope = testScope
        ).uiState.test {
            assertThat(awaitItem()).isEqualTo(HeadlinesViewModel.State.Loading)
            testDispatcher.scheduler.advanceUntilIdle()
            assertThat(awaitItem()).isEqualTo(HeadlinesViewModel.State.Success(
                headlines = persistentListOf(),
                hasMore = false,
                isLoadingMore = false,
            ))
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `error state emitted when repository fails`() = runTest {
        viewModel(
            headlinesRepository = { Result.failure(Throwable()) },
            viewModelScope = testScope
        ).uiState.test {
            assertThat(awaitItem()).isEqualTo(HeadlinesViewModel.State.Loading)
            testDispatcher.scheduler.advanceUntilIdle()
            assertThat(awaitItem()).isInstanceOf<HeadlinesViewModel.State.Error>()
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `state transitions to loading again when user reloads headlines`() = runTest {
        val viewModel = viewModel(viewModelScope = testScope)
        viewModel.uiState.test {
            assertThat(awaitItem()).isEqualTo(HeadlinesViewModel.State.Loading)
            testDispatcher.scheduler.advanceUntilIdle()
            assertThat(awaitItem()).isInstanceOf<HeadlinesViewModel.State.Success>()
            testDispatcher.scheduler.advanceUntilIdle()
            viewModel.handleIntent(HeadlinesIntent.ReloadHeadlines)
            assertThat(awaitItem()).isEqualTo(HeadlinesViewModel.State.Loading)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `next page is loaded when LoadMoreHeadlines intent is received`() = runTest {
        val viewModel = viewModel(viewModelScope = testScope)
        viewModel.uiState.test {
            assertThat(awaitItem()).isEqualTo(HeadlinesViewModel.State.Loading)
            testDispatcher.scheduler.advanceUntilIdle()
            with (awaitItem() as HeadlinesViewModel.State.Success) {
                assertThat(this.headlines.size).isEqualTo(10)
            }
            testDispatcher.scheduler.advanceUntilIdle()
            viewModel.handleIntent(HeadlinesIntent.LoadMoreHeadlines)
            with (awaitItem() as HeadlinesViewModel.State.Success) {
                assertThat(this.headlines.size).isEqualTo(20)
            }
            cancelAndIgnoreRemainingEvents()
        }
    }

    private fun viewModel(
        headlinesRepository: HeadlinesRepository = FakeHeadlinesRepository(),
        getPaginatedHeadlines: GetPaginatedHeadlines = GetPaginatedHeadlines(
            headlinesRepository = headlinesRepository,
            artificialDelay = null,
        ),
        viewModelScope: CoroutineScope
    ) = HeadlinesViewModel(
        getPaginatedHeadlines = getPaginatedHeadlines,
        viewModelScope = viewModelScope,
    )
}