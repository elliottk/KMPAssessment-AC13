package org.example.project.features.headlines.presentation

import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import org.koin.mp.KoinPlatform.getKoin

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
                    getKoin().get<HeadlinesViewModel>()
                } as T
            }
        }
    )
    return androidViewModel.delegate
}