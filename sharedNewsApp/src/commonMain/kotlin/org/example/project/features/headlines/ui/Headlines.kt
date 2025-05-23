package org.example.project.features.headlines.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.ContentAlpha
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import kotlinx.collections.immutable.toPersistentList
import org.example.project.features.headlines.debug.fakeHeadlineList
import org.example.project.features.headlines.presentation.HeadlinesViewModel
import org.example.project.features.headlines.presentation.model.toPresentation
import org.example.project.features.headlines.presentation.rememberHeadlinesViewModel
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.example.project.features.headlines.presentation.model.Headline as HeadlinePresentationModel

@Composable
fun HeadlinesRoot(
    viewModel: HeadlinesViewModel = rememberHeadlinesViewModel(),
    modifier: Modifier = Modifier,
) {
    val state by viewModel.uiState.collectAsState()
    Headlines(
        state = state,
        modifier = modifier,
    )
}

@Composable
fun Headlines(
    state: HeadlinesViewModel.State,
    modifier: Modifier = Modifier,
) {
    when (state) {
        HeadlinesViewModel.State.Loading -> Loading(modifier)
        HeadlinesViewModel.State.Error -> Error(modifier)
        is HeadlinesViewModel.State.Success if (state.headlines.isEmpty()) -> Empty(modifier = modifier)
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

// TODO buildout
@Composable
private fun Error(
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center,
    ) {
        Text(text = "An error happened.")
    }
}

// TODO
@Composable
private fun Empty(
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center,
    ) {
        Text(text = "There are no headlines right now.")
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
    Box(modifier = modifier.background(color = Color.Red))
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
        modifier = Modifier.fillMaxSize(),
    )
}

@Preview
@Composable
private fun PreviewError() {
    Error(
        modifier = Modifier.fillMaxSize(),
    )
}