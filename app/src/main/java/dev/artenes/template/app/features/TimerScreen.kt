package dev.artenes.template.app.features

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LifecycleResumeEffect
import timber.log.Timber

@Composable
fun TimerScreen(
    viewModel: TimerViewModel = hiltViewModel()
) {

    val state by viewModel.state.collectAsState()

    LifecycleResumeEffect(key1 = Unit) {

        Timber.d("Resumed")
        viewModel.hideNotification()

        onPauseOrDispose {
            Timber.d("Paused")
            viewModel.showNotification()
        }
    }

    Scaffold { edges ->

        Column(modifier = Modifier.padding(edges)) {

            TextField(value = state.seconds, onValueChange = {
                viewModel.setTime(it)
            })

            Button(onClick = { viewModel.start() }) {
                Text(text = "Start")
            }

            Button(onClick = { viewModel.resume() }) {
                Text(text = "Resume")
            }

            Button(onClick = { viewModel.pause() }) {
                Text(text = "Pause")
            }

            Button(onClick = { viewModel.stop() }) {
                Text(text = "Stop")
            }


        }

    }

}