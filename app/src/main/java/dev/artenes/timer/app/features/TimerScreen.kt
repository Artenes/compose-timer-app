package dev.artenes.timer.app.features

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LifecycleResumeEffect
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
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


        Box(
            modifier = Modifier
                .padding(edges)
                .fillMaxSize()
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.surface,
                            MaterialTheme.colorScheme.surfaceVariant
                        )
                    )
                ),
            contentAlignment = Alignment.Center
        ) {


            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {

                Time(
                    onChange = { minutes ->
                        viewModel.setMinutes(minutes)
                    },
                    value = state.minutes
                )

                Text(
                    text = ":",
                    fontSize = 60.sp,
                    color = MaterialTheme.colorScheme.primary
                )

                Time(
                    onChange = { seconds ->
                        viewModel.setSeconds(seconds)
                    },
                    value = state.seconds
                )

            }

            Row(
                modifier = Modifier.offset(y = 200.dp)
            ) {

                if (state.startVisible) {
                    Button(
                        onClick = { viewModel.start() },
                        shape = CircleShape,
                        modifier = Modifier
                            .size(50.dp),
                        contentPadding = PaddingValues(0.dp),
                        enabled = state.startEnabled
                    ) {
                        Icon(imageVector = Icons.Filled.PlayArrow, contentDescription = "")
                    }
                }

                if (state.resumeVisible) {
                    Button(
                        onClick = { viewModel.resume() },
                        shape = CircleShape,
                        modifier = Modifier
                            .size(50.dp),
                        contentPadding = PaddingValues(0.dp),
                    ) {
                        Icon(imageVector = Icons.Filled.PlayArrow, contentDescription = "")
                    }
                    Spacer(modifier = Modifier.width(20.dp))
                }

                if (state.pauseVisible) {
                    Button(
                        onClick = { viewModel.pause() },
                        shape = CircleShape,
                        modifier = Modifier
                            .size(50.dp),
                        contentPadding = PaddingValues(0.dp),
                    ) {
                        Icon(imageVector = Icons.Filled.Pause, contentDescription = "")
                    }
                    Spacer(modifier = Modifier.width(20.dp))
                }

                if (state.stopVisible) {
                    Button(
                        onClick = { viewModel.stop() },
                        shape = CircleShape,
                        modifier = Modifier
                            .size(50.dp),
                        contentPadding = PaddingValues(0.dp),
                    ) {
                        Icon(imageVector = Icons.Filled.Stop, contentDescription = "")
                    }
                }

            }

        }

    }

}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun Time(
    value: Int,
    onChange: (Int) -> Unit
) {

    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(listState) {
        snapshotFlow { listState.firstVisibleItemIndex }
            .distinctUntilChanged()
            .collect {
                onChange(it)
            }
    }

    LaunchedEffect(value) {
        coroutineScope.launch {
            listState.scrollToItem(value)
        }
    }

    LazyColumn(
        modifier = Modifier.height(100.dp),
        state = listState,
        flingBehavior = rememberSnapFlingBehavior(lazyListState = listState),
    ) {

        items(60) { number ->

            Box(
                modifier = Modifier
                    .height(100.dp)
                    .width(150.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    color = MaterialTheme.colorScheme.primary,
                    text = String.format("%02d", number),
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontSize = 100.sp,
                        fontWeight = FontWeight.Light
                    ),
                )
            }

        }

    }

}