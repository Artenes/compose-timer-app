package dev.artenes.budget.app

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.launch
import dev.artenes.template.core.models.foundation.Event

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DevelopmentScreen(
    viewModel: DevelopmentViewModel = hiltViewModel()
) {

    val state by viewModel.state.collectAsState()
    val coroutineScope = rememberCoroutineScope()
    val snackBarHostState = remember {
        SnackbarHostState()
    }

    LaunchedEffect(key1 = state.snackBar) {
        if (state.snackBar is Event.SuccessfulEvent) {
            (state.snackBar as Event.SuccessfulEvent<String>).consume {
                coroutineScope.launch {
                    snackBarHostState.showSnackbar(it)
                }
            }
        }

    }

    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackBarHostState)
        },
        topBar = {
            TopAppBar(title = { Text(text = "Development tools") })
        }
    ) {

        Column(
            modifier = Modifier
                .padding(it)
                .padding(30.dp)
        ) {

            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = { viewModel.openApp() },
                enabled = !state.operationRunning
            ) {
                Text(text = "Open app")
            }

            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = { viewModel.seedDatabase() },
                enabled = !state.operationRunning
            ) {
                Text(text = "Seed database")
            }

            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = { viewModel.clearDatabase() },
                enabled = !state.operationRunning
            ) {
                Text(text = "Clear database")
            }

        }

    }

}