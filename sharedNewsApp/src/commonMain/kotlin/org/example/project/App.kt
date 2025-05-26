package org.example.project

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.example.project.ui.NewsListScreen

@Composable
fun App(
    modifier: Modifier = Modifier,
) {
    MaterialTheme {
        NewsListScreen(modifier = modifier)
    }
}
