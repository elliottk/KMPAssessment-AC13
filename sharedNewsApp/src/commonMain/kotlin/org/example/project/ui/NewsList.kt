package org.example.project.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.example.project.features.headlines.ui.HeadlinesRoot

@Composable
fun NewsListScreen(
    modifier: Modifier = Modifier,
) {
    HeadlinesRoot(modifier = modifier)
}
