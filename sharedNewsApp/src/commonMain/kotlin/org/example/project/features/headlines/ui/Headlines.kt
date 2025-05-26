package org.example.project.features.headlines.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.ContentAlpha
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import kotlinx.collections.immutable.toPersistentList
import newsapp.sharednewsapp.generated.resources.Res
import newsapp.sharednewsapp.generated.resources.Res.drawable
import newsapp.sharednewsapp.generated.resources.errorLoadingHeadlines
import newsapp.sharednewsapp.generated.resources.error_24dp_1f1f1f_fill0_wght400_grad0_opsz24
import newsapp.sharednewsapp.generated.resources.headlinesEmpty
import newsapp.sharednewsapp.generated.resources.news_24dp_1f1f1f_fill0_wght400_grad0_opsz24
import newsapp.sharednewsapp.generated.resources.retry
import org.example.project.features.headlines.debug.fakeHeadlineList
import org.example.project.features.headlines.presentation.HeadlinesIntent
import org.example.project.features.headlines.presentation.HeadlinesViewModel
import org.example.project.features.headlines.presentation.model.toPresentation
import org.example.project.features.headlines.presentation.rememberHeadlinesViewModel
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.example.project.features.headlines.presentation.model.Headline as HeadlinePresentationModel

@Composable
fun HeadlinesRoot(
    viewModel: HeadlinesViewModel = rememberHeadlinesViewModel(),
    modifier: Modifier = Modifier,
) {
    val state by viewModel.uiState.collectAsState()
    Headlines(
        onReloadHeadlines = { viewModel.handleIntent(HeadlinesIntent.ReloadHeadlines) },
        onLoadMoreHeadlines = {},
        state = state,
        modifier = modifier,
    )
}

@Composable
fun Headlines(
    onReloadHeadlines: () -> Unit,
    onLoadMoreHeadlines: () -> Unit,
    state: HeadlinesViewModel.State,
    modifier: Modifier = Modifier,
) {
    when (state) {
        HeadlinesViewModel.State.Loading -> Loading(modifier = modifier)
        is HeadlinesViewModel.State.Error -> Error(
            onRetryPressed = onReloadHeadlines,
            uiState = state,
            modifier = modifier
        )
        is HeadlinesViewModel.State.Success if (state.headlines.isEmpty()) -> Empty(
            onRetryPressed = onReloadHeadlines,
            modifier = modifier
        )
        is HeadlinesViewModel.State.Success -> Success(uiState = state, modifier = modifier)
    }
}

@Composable
private fun Loading(
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center,
    ) {
        CircularProgressIndicator()
    }
}

@Composable
private fun Error(
    onRetryPressed: () -> Unit,
    uiState: HeadlinesViewModel.State.Error,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Icon(
            painter = painterResource(drawable.error_24dp_1f1f1f_fill0_wght400_grad0_opsz24),
            modifier = Modifier.size(64.dp),
            contentDescription = null,
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = stringResource(Res.string.errorLoadingHeadlines),
            style = MaterialTheme.typography.h4,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
        )
        Spacer(modifier = Modifier.height(16.dp))
        uiState.errorMessage?.let { errorMessage ->
            Text(
                text = errorMessage,
                style = MaterialTheme.typography.h3,
                textAlign = TextAlign.Center,
            )
            Spacer(modifier = Modifier.height(16.dp))
        }
        TextButton(
            onClick = onRetryPressed
        ) {
            Text(
                text = stringResource(Res.string.retry),
                style = MaterialTheme.typography.h5,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
            )
        }
    }
}

@Composable
private fun Empty(
    onRetryPressed: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = stringResource(Res.string.headlinesEmpty),
            style = MaterialTheme.typography.h4,
            fontWeight = FontWeight.Bold,
        )
        Spacer(modifier = Modifier.height(16.dp))
        TextButton(
            onClick = onRetryPressed,
        ) {
            Text(
                text = stringResource(Res.string.retry),
                style = MaterialTheme.typography.h5,
                fontWeight = FontWeight.Bold,
            )
        }
    }
}

@Composable
private fun Success(
    uiState: HeadlinesViewModel.State.Success,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier,
    ) {
        items(
           count = uiState.headlines.size,
           key = { index -> uiState.headlines[index].id },
        ) {
            Headline(
                headline = uiState.headlines[it],
                modifier = Modifier.fillMaxWidth().padding(16.dp),
            )
        }
    }
}

@Composable
private fun Headline(
    headline: HeadlinePresentationModel,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
        ) {
            HeadlineImage(
                url = headline.imageUrl,
                contentDescription = headline.title,
                modifier = Modifier.fillMaxWidth().aspectRatio(16f / 9f).clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
            )
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = headline.title,
                    style = MaterialTheme.typography.h6,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = headline.description,
                    style = MaterialTheme.typography.body2,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = headline.publishedDate,
                    style = MaterialTheme.typography.caption,
                    color = MaterialTheme.colors.onSurface.copy(alpha = ContentAlpha.medium)
                )
            }
        }
    }
}

@Composable
fun HeadlineImage(
    url: String,
    contentDescription: String,
    modifier: Modifier = Modifier,
) {
    AsyncImage(
        model = url,
        contentDescription = contentDescription,
        placeholder = painterResource(drawable.news_24dp_1f1f1f_fill0_wght400_grad0_opsz24),
        error = painterResource(drawable.error_24dp_1f1f1f_fill0_wght400_grad0_opsz24),
        contentScale = ContentScale.Crop,
        modifier = modifier,
    )
}

@Preview
@Composable
private fun PreviewSuccess() {
    MaterialTheme {
        Success(
            uiState = HeadlinesViewModel.State.Success(
                headlines = fakeHeadlineList().map { it.toPresentation() }.toPersistentList(),
                isRefreshing = false,
            )
        )
    }
}

@Preview
@Composable
private fun PreviewLoading() {
    Loading(
        modifier = Modifier.fillMaxSize(),
    )
}

@Preview
@Composable
private fun PreviewEmpty() {
    Empty(
        onRetryPressed = {},
        modifier = Modifier.fillMaxSize(),
    )
}

@Preview
@Composable
private fun PreviewError() {
    Error(
        onRetryPressed = {},
        uiState = HeadlinesViewModel.State.Error(),
        modifier = Modifier.fillMaxSize(),
    )
}