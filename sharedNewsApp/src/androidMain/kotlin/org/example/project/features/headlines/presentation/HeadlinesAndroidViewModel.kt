package org.example.project.features.headlines.presentation

import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import org.example.project.features.headlines.FakeHeadlinesRepository
import org.example.project.features.headlines.domain.GetAllHeadlines

class HeadlinesAndroidViewModel(
    factory: () -> HeadlinesViewModel,
) : ViewModel() {
    val delegate: HeadlinesViewModel by lazy(factory)

    override fun onCleared() {
        super.onCleared()
        delegate.clear()
    }
}

@Suppress("UNCHECKED_CAST")
@Composable
actual fun rememberHeadlinesViewModel(): HeadlinesViewModel {
    val storeOwner = checkNotNull(LocalViewModelStoreOwner.current)
    val androidViewModel = viewModel<HeadlinesAndroidViewModel>(
        viewModelStoreOwner = storeOwner,
        factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return HeadlinesAndroidViewModel {
                    HeadlinesViewModel(
                        getAllHeadlines = GetAllHeadlines(headlinesRepository = FakeHeadlinesRepository())
                    )
                } as T
            }
        }
    )
    return androidViewModel.delegate
}