package org.example.project

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.example.project.ui.NewsListScreen
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun App(
    modifier: Modifier = Modifier,
) {
    MaterialTheme {
        NewsListScreen(modifier = modifier)
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    App(modifier = Modifier.fillMaxSize())
}