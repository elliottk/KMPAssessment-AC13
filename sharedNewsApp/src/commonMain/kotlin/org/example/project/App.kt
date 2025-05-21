package org.example.project

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import org.example.project.ui.NewsListScreen
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {
    MaterialTheme {
        NewsListScreen()
    }
}