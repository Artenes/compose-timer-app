package dev.artenes.template.app.features

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LifecycleResumeEffect
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
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

            LazyRow(verticalAlignment = Alignment.CenterVertically) {

                item {
                    Digit(maxInclusive = 59)
                }

                item {
                    Colon()
                }

                item {
                    Digit(maxInclusive = 59)
                }

            }


        }

    }

}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun Digit(maxInclusive: Int) {

    val coroutineScope = rememberCoroutineScope()
    val listState = rememberLazyListState()

    LaunchedEffect(listState) {
        snapshotFlow { listState.isScrollInProgress }
            .filter { !it }
            .distinctUntilChanged()
            .collect {
                Timber.d("FINISHED")
            }
    }

    LazyColumn(
        modifier = Modifier.height(100.dp),
        state = listState,
        flingBehavior = rememberSnapFlingBehavior(lazyListState = listState),
    ) {

        items((maxInclusive + 1)) { number ->

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

@Composable
fun Colon() {
    val color = MaterialTheme.colorScheme.primary
    Canvas(modifier = Modifier.size(50.dp)) {
        val radius = 5.dp.toPx()
        val spacing = 30.dp.toPx()
        val centerX = size.width / 2
        val centerY1 = (size.height / 2 + spacing / 2) - spacing / 2
        val centerY2 = (size.height / 2 + spacing / 2) + spacing / 2

        drawCircle(
            color,
            radius,
            center = Offset(centerX, centerY1)
        )
        drawCircle(
            color,
            radius,
            center = Offset(centerX, centerY2)
        )
    }
}