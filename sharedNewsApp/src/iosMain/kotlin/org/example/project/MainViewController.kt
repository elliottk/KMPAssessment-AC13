package org.example.project

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.ComposeUIViewController

fun MainViewController() = ComposeUIViewController { App(modifier = Modifier.fillMaxSize()) }